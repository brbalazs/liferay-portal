/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.license.enterprise.app.internal;

import com.liferay.osgi.util.BundleUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

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
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext.removeBundleListener(_bundleListener);
	}

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

		String productId = null;

		if (endIndex == -1) {
			productId = enterpriseAppHeader.substring(
				index + _KEY_PRODUCT_ID.length());
		}
		else {
			productId = enterpriseAppHeader.substring(
				index + _KEY_PRODUCT_ID.length(), endIndex);
		}

		String productName = _productNames.get(productId);

		if (productName == null) {
			productName = productId;
		}

		if (GetterUtil.getBoolean(
				_props.get("enterprise.product." + productName + ".enabled"))) {

			return null;
		}

		return productId;
	}

	private boolean _processBundle(Bundle bundle) {
		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		String productId = _getProductId(headers);

		if (Validator.isNull(productId)) {
			return false;
		}

		synchronized (this) {
			try {
				bundle.uninstall();
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

	private static final String _KEY_PRODUCT_ID = "product.id=";

	private static final Log _log = LogFactoryUtil.getLog(
		PortalLicenseEnterpriseAppGateKeeper.class);

	private static final Map<String, String> _productNames = HashMapBuilder.put(
		"9a473157-06a6-44b6-b017-a360ffaf5f38", "commerce"
	).put(
		"22b7e30f-34d4-4a63-9696-56987ad66e4e", "enterprise.search"
	).build();

	private BundleContext _bundleContext;
	private BundleListener _bundleListener;

	@Reference
	private Props _props;

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

}