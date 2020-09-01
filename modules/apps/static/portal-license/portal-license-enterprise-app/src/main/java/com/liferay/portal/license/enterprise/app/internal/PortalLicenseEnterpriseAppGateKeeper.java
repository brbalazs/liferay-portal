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
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Filter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
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
	protected void setLicenseManager(LicenseManager licenseManager) {
		_licenseManagerAtomicReference.set(licenseManager);

		synchronized (this) {
			Set
				<Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
						entrySet =
							_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
								entrySet();

			Iterator
				<Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>>
						iterator = entrySet.iterator();

			while (iterator.hasNext()) {
				Map.Entry
					<String, Set<PortalLicenseEnterpriseAppBlockedBundleData>>
						entry = iterator.next();

				if (_verifyLicense(entry.getKey())) {
					_installBundles(entry.getKey(), entry.getValue());

					iterator.remove();
				}
			}
		}
	}

	protected void unsetLicenseManager(LicenseManager licenseManager) {
		_licenseManagerAtomicReference.compareAndSet(licenseManager, null);
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

		int endIndex = enterpriseAppHeader.indexOf(index, CharPool.SEMICOLON);

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

		for (PortalLicenseEnterpriseAppBlockedBundleData
				portalLicenseEnterpriseAppBlockedBundleData :
					portalLicenseEnterpriseAppBlockedBundleDataSet) {

			String webContextPath =
				portalLicenseEnterpriseAppBlockedBundleData.getWebContextPath();

			if (webContextPath != null) {
				_webContextPathMap.put(webContextPath, productId);
			}

			try {
				BundleUtil.installBundle(
					_bundleContext, _lpkgDeployer,
					portalLicenseEnterpriseAppBlockedBundleData.getLocation(),
					portalLicenseEnterpriseAppBlockedBundleData.
						getStartLevel());
			}
			catch (Exception exception) {
				if (webContextPath != null) {
					_webContextPathMap.remove(webContextPath);
				}

				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to install bundle " +
							portalLicenseEnterpriseAppBlockedBundleData.
								getLocation(),
						exception);
				}
			}
		}
	}

	private boolean _processBundle(Bundle bundle) {
		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		String productId = _getProductId(headers);

		if (Validator.isNull(productId)) {
			return false;
		}

		String webContextPath = headers.get("Web-ContextPath");

		synchronized (this) {
			if (!_portalLicenseEnterpriseAppBlockedBundleDataSetMap.containsKey(
					productId) &&
				_verifyLicense(productId)) {

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
							computeIfAbsent(productId, key -> new HashSet<>());

				portalLicenseEnterpriseAppBlockedBundleDataSet.add(
					new PortalLicenseEnterpriseAppBlockedBundleData(
						bundle.getLocation(), startLevel, webContextPath));
			}
			catch (Exception exception) {
				_log.error(
					"Unable to uninstall bundle " + bundle.getSymbolicName(),
					exception);
			}
		}

		return true;
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

	private boolean _verifyLicense(String productId) {
		LicenseManager licenseManager = _licenseManagerAtomicReference.get();

		if (licenseManager == null) {
			return false;
		}

		try {
			PortalLicenseEnterpriseAppLicenseUtil.verify(
				licenseManager, productId);

			return true;
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Failed to verify license for ", productId, ": ",
						exception.getMessage()));
			}
		}

		return false;
	}

	private static final String _KEY_PRODUCT_ID = "product.id=";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalLicenseEnterpriseAppGateKeeper.class);

	private BundleContext _bundleContext;
	private BundleListener _bundleListener;
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
			if ((bundleEvent.getType() == BundleEvent.INSTALLED) &&
				(bundleEvent.getOrigin() != _bundle)) {

				_processBundle(bundleEvent.getBundle());
			}
		}

		private PortalLicenseEnterpriseAppBundleListener(Bundle bundle) {
			_bundle = bundle;
		}

		private final Bundle _bundle;

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
				Set<String> blockedProductIds = Collections.emptySet();

				if (productId.equals("Portal")) {
					blockedProductIds =
						_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
							keySet();
				}
				else if (_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
							containsKey(productId)) {

					blockedProductIds = Collections.singleton(productId);
				}

				for (String blockedProductId : blockedProductIds) {
					if (_verifyLicense(blockedProductId)) {
						_installBundles(
							blockedProductId,
							_portalLicenseEnterpriseAppBlockedBundleDataSetMap.
								remove(blockedProductId));
					}
				}
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