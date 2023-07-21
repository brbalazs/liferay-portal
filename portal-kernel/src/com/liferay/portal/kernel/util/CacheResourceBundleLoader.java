/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carlos Sierra Andrés
 */
public class CacheResourceBundleLoader implements ResourceBundleLoader {

	public CacheResourceBundleLoader(
		ResourceBundleLoader resourceBundleLoader) {

		_resourceBundleLoader = resourceBundleLoader;
	}

	@Override
	public ResourceBundle loadResourceBundle(Locale locale) {
		ResourceBundle resourceBundle = _resourceBundles.get(locale);

		if (resourceBundle == _nullResourceBundle) {
			return null;
		}

		if (resourceBundle == null) {
			try {
				resourceBundle = _resourceBundleLoader.loadResourceBundle(
					locale);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}

			if (resourceBundle == null) {
				_resourceBundles.put(locale, _nullResourceBundle);
			}
			else {
				_resourceBundles.put(locale, resourceBundle);
			}
		}

		return resourceBundle;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #loadResourceBundle(Locale)}
	 */
	@Deprecated
	@Override
	public ResourceBundle loadResourceBundle(String languageId) {
		return ResourceBundleLoader.super.loadResourceBundle(languageId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CacheResourceBundleLoader.class);

	private static final ResourceBundle _nullResourceBundle =
		new ResourceBundle() {

			@Override
			public Enumeration<String> getKeys() {
				throw new UnsupportedOperationException();
			}

			@Override
			protected Object handleGetObject(String key) {
				throw new UnsupportedOperationException();
			}

		};

	private final ResourceBundleLoader _resourceBundleLoader;
	private final Map<Locale, ResourceBundle> _resourceBundles =
		new ConcurrentHashMap<>();

}