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
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
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

		if (endIndex == -1) {
			return enterpriseAppHeader.substring(
				index + _KEY_PRODUCT_ID.length());
		}

		return enterpriseAppHeader.substring(
			index + _KEY_PRODUCT_ID.length(), endIndex);
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

	private static final Map<String, String> _productNames =
		Collections.singletonMap(
			"9a473157-06a6-44b6-b017-a360ffaf5f38",
			"Liferay Commerce Subscription Production");

	private BundleContext _bundleContext;
	private BundleListener _bundleListener;

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