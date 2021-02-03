/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.license.enterprise.app.internal;

import com.liferay.osgi.util.BundleUtil;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.osgi.util.bundle.BundleStartLevelUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.license.messaging.LCSPortletState;
import com.liferay.portal.kernel.license.util.LicenseManager;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.servlet.PortletServlet;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.license.enterprise.app.internal.constants.PortalLicenseEnterpriseAppDestinationNames;
import com.liferay.portal.lpkg.deployer.LPKGDeployer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Filter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Tina Tian
 */
@Component(immediate = true, service = {})
public class PortalLicenseEnterpriseAppGateKeeper {

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_bundleListener = new PortalLicenseEnterpriseAppBundleListener(
			bundleContext.getBundle());

		bundleContext.addBundleListener(_bundleListener);

		_scanBundles(bundleContext);

		Dictionary<String, Object> dictionary = new HashMapDictionary<>();

		dictionary.put(
			"destination.name",
			PortalLicenseEnterpriseAppDestinationNames.
				PORTAL_LICENSE_ENTERPRISE_APP);

		_serviceRegistration = bundleContext.registerService(
			MessageListener.class,
			new PortalLicenseEnterpriseAppMessageListener(), dictionary);

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext,
			StringBundler.concat(
				"(&(", HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
				"=*)(objectClass=org.osgi.service.http.context.",
				"ServletContextHelper))"),
			new PortalLicenseEnterpriseAppWebContextServiceTrackerCustomizer());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceRegistration.unregister();

		_bundleContext.removeBundleListener(_bundleListener);
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setClusterExecutor(ClusterExecutor clusterExecutor) {
		_clusterExecutorAtomicReference.set(clusterExecutor);

		_scanBlockedBundles();
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void setLicenseManager(LicenseManager licenseManager) {
		_licenseManagerAtomicReference.set(licenseManager);

		_scanBlockedBundles();
	}

	protected void unsetClusterExecutor(ClusterExecutor clusterExecutor) {
		_clusterExecutorAtomicReference.compareAndSet(clusterExecutor, null);
	}

	protected void unsetLicenseManager(LicenseManager licenseManager) {
		_licenseManagerAtomicReference.compareAndSet(licenseManager, null);
	}

	protected static LCSPortletState lcsPortletState;

	private static String _getLPKGPath(String location) {
		int startIndex = location.indexOf("lpkgPath");

		if (startIndex == -1) {
			return null;
		}

		int endIndex = location.indexOf('&', startIndex);

		if (endIndex == -1) {
			endIndex = location.length();
		}

		return location.substring(startIndex + 9, endIndex);
	}

	private String _getFragmentHost(Dictionary<String, String> headers) {
		String fragmentHost = headers.get(Constants.FRAGMENT_HOST);

		if (fragmentHost == null) {
			return null;
		}

		int index = fragmentHost.indexOf(CharPool.SEMICOLON);

		if (index != -1) {
			fragmentHost = fragmentHost.substring(0, index);
		}

		return fragmentHost;
	}

	private String _getProductId(Dictionary<String, String> headers) {
		String enterpriseAppHeader = headers.get("Liferay-Enterprise-App");

		if (enterpriseAppHeader == null) {
			return null;
		}

		int index = enterpriseAppHeader.indexOf(_KEY_PRODUCT_ID);

		if (index == -1) {
			return null;
		}

		int endIndex = enterpriseAppHeader.indexOf(CharPool.SEMICOLON, index);

		if (endIndex == -1) {
			return enterpriseAppHeader.substring(
				index + _KEY_PRODUCT_ID.length());
		}

		return enterpriseAppHeader.substring(
			index + _KEY_PRODUCT_ID.length(), endIndex);
	}

	private void _installBundles(
		String productId,
		Set<PortalLicenseEnterpriseAppBlockedBundleData>
			portalLicenseEnterpriseAppBlockedBundleDataSet) {

		if (portalLicenseEnterpriseAppBlockedBundleDataSet == null) {
			return;
		}

		Set<String> lpkgPaths = new TreeSet<>();

		Iterator<PortalLicenseEnterpriseAppBlockedBundleData> iterator =
			portalLicenseEnterpriseAppBlockedBundleDataSet.iterator();

		while (iterator.hasNext()) {
			PortalLicenseEnterpriseAppBlockedBundleData
				portalLicenseEnterpriseAppBlockedBundleData = iterator.next();

			String lpkgPath = _getLPKGPath(
				portalLicenseEnterpriseAppBlockedBundleData.getLocation());

			if (lpkgPath != null) {
				lpkgPaths.add(lpkgPath);

				String webContextPath =
					portalLicenseEnterpriseAppBlockedBundleData.
						getWebContextPath();

				if (webContextPath != null) {
					_webContextPathMap.put(webContextPath, productId);
				}

				iterator.remove();
			}
		}

		List<Bundle> uninstalledBundles = new ArrayList<>();
		Map<String, Integer> lpkgBundleMap = new TreeMap<>();

		for (String lpkgPath : lpkgPaths) {
			Bundle bundle = _bundleContext.getBundle(lpkgPath);

			if (bundle == null) {
				continue;
			}

			BundleStartLevel bundleStartLevel = bundle.adapt(
				BundleStartLevel.class);

			lpkgBundleMap.put(
				bundle.getLocation(), bundleStartLevel.getStartLevel());

			try {
				bundle.uninstall();

				uninstalledBundles.add(bundle);
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to uninstall bundle " +
							bundle.getSymbolicName(),
						exception);
				}

				lpkgBundleMap.remove(bundle.getLocation());
			}
		}

		if (!uninstalledBundles.isEmpty()) {
			BundleUtil.refreshBundles(_bundleContext, uninstalledBundles);
		}

		for (Map.Entry<String, Integer> entry : lpkgBundleMap.entrySet()) {
			try {
				BundleUtil.installBundle(
					_bundleContext, _lpkgDeployer, entry.getKey(),
					entry.getValue());
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to install bundle " + entry.getKey(),
						exception);
				}
			}
		}

		if (portalLicenseEnterpriseAppBlockedBundleDataSet.isEmpty()) {
			return;
		}

		List<Map.Entry<Bundle, PortalLicenseEnterpriseAppBlockedBundleData>>
			bundleEntries = new ArrayList<>();

		Set<String> fragmentHosts = new HashSet<>();

		for (PortalLicenseEnterpriseAppBlockedBundleData
				portalLicenseEnterpriseAppBlockedBundleData :
					portalLicenseEnterpriseAppBlockedBundleDataSet) {

			String location =
				portalLicenseEnterpriseAppBlockedBundleData.getLocation();

			try {
				bundleEntries.add(
					new AbstractMap.SimpleImmutableEntry<>(
						_bundleContext.installBundle(location),
						portalLicenseEnterpriseAppBlockedBundleData));

				String fragmentHost =
					portalLicenseEnterpriseAppBlockedBundleData.
						getFragmentHost();

				if (Validator.isNotNull(fragmentHost)) {
					fragmentHosts.add(fragmentHost);
				}
			}
			catch (Exception exception) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to install bundle " + location, exception);
				}
			}
		}

		List<Bundle> refreshBundles = new ArrayList<>();

		for (Map.Entry<Bundle, PortalLicenseEnterpriseAppBlockedBundleData>
				bundleEntry : bundleEntries) {

			Bundle bundle = bundleEntry.getKey();

			PortalLicenseEnterpriseAppBlockedBundleData blockedBundleData =
				bundleEntry.getValue();

			String webContextPath = blockedBundleData.getWebContextPath();

			if (webContextPath != null) {
				_webContextPathMap.put(webContextPath, productId);
			}

			try {
				BundleStartLevelUtil.setStartLevelAndStart(
					bundle, blockedBundleData.getStartLevel(), _bundleContext);

				if (fragmentHosts.contains(bundle.getSymbolicName())) {
					refreshBundles.add(bundle);
				}
			}
			catch (Exception exception) {
				if (webContextPath != null) {
					_webContextPathMap.remove(webContextPath);
				}

				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to start bundle " + bundle.getSymbolicName(),
						exception);
				}
			}
		}

		if (!refreshBundles.isEmpty()) {
			BundleUtil.refreshBundles(_bundleContext, refreshBundles);
		}
	}

	private boolean _processBundle(Bundle bundle) {
		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		String productId = _getProductId(headers);

		if (Validator.isNull(productId)) {
			return false;
		}

		if (_productNames.get(productId) == null) {
			throw new IllegalArgumentException(
				"Invalid product id " + productId);
		}

		String webContextPath = headers.get("Web-ContextPath");

		if (webContextPath == null) {
			String symbolicName = bundle.getSymbolicName();

			if (symbolicName.endsWith(".web")) {
				webContextPath = StringPool.SLASH + symbolicName;
			}
		}

		synchronized (this) {
			if (!_portalLicenseEnterpriseAppBlockedBundleDataSetMap.containsKey(
					productId) &&
				_verifyLicense(productId, false)) {

				if (webContextPath != null) {
					_webContextPathMap.put(webContextPath, productId);
				}

				return false;
			}

			BundleStartLevel bundleStartLevel = bundle.adapt(
				BundleStartLevel.class);

			int startLevel = bundleStartLevel.getStartLevel();

			try {
				bundle.uninstall();

				Set<PortalLicenseEnterpriseAppBlockedBundleData>
					portalLicenseEnterpriseAppBlockedBundleDataSet =
						_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
							computeIfAbsent(
								productId, key -> new TreeSet<>(_comparator));

				portalLicenseEnterpriseAppBlockedBundleDataSet.add(
					new PortalLicenseEnterpriseAppBlockedBundleData(
						_getFragmentHost(headers), bundle.getLocation(),
						startLevel, webContextPath));
			}
			catch (Exception exception) {
				_log.error(
					"Unable to uninstall bundle " + bundle.getSymbolicName(),
					exception);
			}
		}

		return true;
	}

	private void _receive(String productId) {
		if (!productId.equals("Portal")) {
			if (_portalLicenseEnterpriseAppBlockedBundleDataSetMap.containsKey(
					productId) &&
				_verifyLicense(productId, false)) {

				_installBundles(
					productId,
					_portalLicenseEnterpriseAppBlockedBundleDataSetMap.remove(
						productId));
			}

			return;
		}

		Set<Map.Entry<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
			set = _portalLicenseEnterpriseAppBlockedBundleDataSetMap.entrySet();

		Iterator
			<Map.Entry
				<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
					iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>
				entry = iterator.next();

			if (_verifyLicense(entry.getKey(), true)) {
				iterator.remove();

				_installBundles(entry.getKey(), entry.getValue());
			}
		}
	}

	private void _scanBlockedBundles() {
		synchronized (this) {
			Set
				<Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
						set =
							_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
								entrySet();

			Iterator
				<Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
						iterator = set.iterator();

			while (iterator.hasNext()) {
				Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>
						entry = iterator.next();

				if (_verifyLicense(entry.getKey(), true)) {
					iterator.remove();

					_installBundles(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private void _scanBundles(BundleContext bundleContext) {
		List<Bundle> uninstalledBundles = new ArrayList<>();

		for (Bundle bundle : bundleContext.getBundles()) {
			if ((bundle.getState() != Bundle.UNINSTALLED) &&
				_processBundle(bundle)) {

				uninstalledBundles.add(bundle);
			}
		}

		if (!uninstalledBundles.isEmpty()) {
			BundleUtil.refreshBundles(bundleContext, uninstalledBundles);
		}
	}

	private boolean _verifyLicense(String productId, boolean swallowException) {
		ClusterExecutor clusterExecutor = _clusterExecutorAtomicReference.get();

		if (clusterExecutor == null) {
			return false;
		}

		LicenseManager licenseManager = _licenseManagerAtomicReference.get();

		if (licenseManager == null) {
			return false;
		}

		if (PortalLicenseEnterpriseAppLicenseUtil.getPortalLicenseState(
				licenseManager) != LicenseManager.STATE_GOOD) {

			return false;
		}

		try {
			PortalLicenseEnterpriseAppLicenseUtil.verify(
				licenseManager, productId);

			return true;
		}
		catch (Exception exception) {
			if (!swallowException && _log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Failed to verify license for ",
						_productNames.get(productId), ": ",
						exception.getMessage()));
			}
		}

		return false;
	}

	private static final String _KEY_PRODUCT_ID = "product.id=";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalLicenseEnterpriseAppGateKeeper.class);

	private static final Comparator<PortalLicenseEnterpriseAppBlockedBundleData>
		_comparator =
			new Comparator<PortalLicenseEnterpriseAppBlockedBundleData>() {

				@Override
				public int compare(
					PortalLicenseEnterpriseAppBlockedBundleData
						portalLicenseEnterpriseAppBlockedBundleData1,
					PortalLicenseEnterpriseAppBlockedBundleData
						portalLicenseEnterpriseAppBlockedBundleData2) {

					String location =
						portalLicenseEnterpriseAppBlockedBundleData1.
							getLocation();

					return location.compareTo(
						portalLicenseEnterpriseAppBlockedBundleData2.
							getLocation());
				}

			};

	private static final Map<String, String> _productNames =
		Collections.singletonMap(
			"9a473157-06a6-44b6-b017-a360ffaf5f38",
			"Liferay Commerce Subscription Production");

	private BundleContext _bundleContext;
	private BundleListener _bundleListener;
	private final AtomicReference<ClusterExecutor>
		_clusterExecutorAtomicReference = new AtomicReference<>();
	private final AtomicReference<LicenseManager>
		_licenseManagerAtomicReference = new AtomicReference<>();

	@Reference
	private LPKGDeployer _lpkgDeployer;

	private final Map<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>
		_portalLicenseEnterpriseAppBlockedBundleDataSetMap = new HashMap<>();
	private ServiceRegistration<MessageListener> _serviceRegistration;
	private ServiceTracker<Object, ServiceRegistration<Filter>> _serviceTracker;
	private final Map<String, String> _webContextPathMap =
		new ConcurrentHashMap<>();

	private class PortalLicenseEnterpriseAppBundleListener
		implements SynchronousBundleListener {

		@Override
		public void bundleChanged(BundleEvent bundleEvent) {
			if (bundleEvent.getType() != BundleEvent.INSTALLED) {
				return;
			}

			Bundle bundle = bundleEvent.getBundle();

			String location = bundle.getLocation();

			String lpkgPath = _getLPKGPath(location);

			if (Validator.isNull(lpkgPath) && location.endsWith(".lpkg")) {
				_lpkgOriginBundles.put(
					bundle.getSymbolicName(), bundleEvent.getOrigin());

				return;
			}

			Bundle originBundle = bundleEvent.getOrigin();

			if (Validator.isNotNull(lpkgPath)) {
				Bundle lpkgBundle = _bundleContext.getBundle(lpkgPath);

				originBundle = _lpkgOriginBundles.get(
					lpkgBundle.getSymbolicName());
			}

			if (originBundle == _bundle) {
				return;
			}

			_processBundle(bundleEvent.getBundle());
		}

		private PortalLicenseEnterpriseAppBundleListener(Bundle bundle) {
			_bundle = bundle;
		}

		private final Bundle _bundle;
		private Map<String, Bundle> _lpkgOriginBundles =
			new ConcurrentHashMap<>();

	}

	private class PortalLicenseEnterpriseAppMessageListener
		implements MessageListener {

		@Override
		public void receive(Message message) {
			String productId = (String)message.getPayload();

			if (Validator.isNull(productId)) {
				return;
			}

			synchronized (PortalLicenseEnterpriseAppGateKeeper.this) {
				LCSPortletState lcsPortletState = (LCSPortletState)message.get(
					"LCSPortletState");

				if (lcsPortletState != null) {
					PortalLicenseEnterpriseAppGateKeeper.lcsPortletState =
						lcsPortletState;
				}

				_receive(productId);
			}
		}

	}

	private class PortalLicenseEnterpriseAppWebContextServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<Object, ServiceRegistration<Filter>> {

		@Override
		public ServiceRegistration<Filter> addingService(
			ServiceReference<Object> serviceReference) {

			String webContextPath = GetterUtil.getString(
				serviceReference.getProperty(
					HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH));

			String productId = _webContextPathMap.remove(webContextPath);

			if (productId == null) {
				return null;
			}

			return _bundleContext.registerService(
				Filter.class,
				new PortalLicenseEnterpriseAppPortletServletFilter(productId),
				_buildProperties(serviceReference));
		}

		@Override
		public void modifiedService(
			ServiceReference<Object> serviceReference,
			ServiceRegistration<Filter> filterServiceRegistration) {

			filterServiceRegistration.setProperties(
				_buildProperties(serviceReference));
		}

		@Override
		public void removedService(
			ServiceReference<Object> serviceReference,
			ServiceRegistration<Filter> filterServiceRegistration) {

			filterServiceRegistration.unregister();
		}

		private Dictionary<String, Object> _buildProperties(
			ServiceReference<Object> serviceReference) {

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			for (String key : serviceReference.getPropertyKeys()) {
				if (key.startsWith("osgi.http.whiteboard")) {
					properties.put(key, serviceReference.getProperty(key));
				}
			}

			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
				StringBundler.concat(
					"(", HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
					"=",
					GetterUtil.getString(
						serviceReference.getProperty(
							HttpWhiteboardConstants.
								HTTP_WHITEBOARD_CONTEXT_NAME)),
					")"));
			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER,
				new String[] {
					HttpWhiteboardConstants.DISPATCHER_INCLUDE,
					HttpWhiteboardConstants.DISPATCHER_FORWARD,
					HttpWhiteboardConstants.DISPATCHER_REQUEST
				});
			properties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_SERVLET,
				PortletServlet.class.getName());

			return properties;
		}

	}

}