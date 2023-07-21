/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MappingEnumeration;

import java.net.URL;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = NamedConfigurationContentFactory.class)
public class NamedConfigurationPathContentFactory
	implements NamedConfigurationContentFactory {

	@Override
	public List<NamedConfigurationContent> create(BundleStorage bundleStorage) {
		Dictionary<String, String> headers = bundleStorage.getHeaders();

		String configurationPath = headers.get("Liferay-Configuration-Path");

		if (configurationPath == null) {
			return null;
		}

		final Enumeration<URL> entries = bundleStorage.findEntries(
			configurationPath, "*.properties", true);

		return ListUtil.fromEnumeration(
			new MappingEnumeration<>(
				entries,
				new MappingEnumeration.Mapper
					<URL, NamedConfigurationContent>() {

					@Override
					public NamedConfigurationContent map(URL url) {
						return new PropertiesFileNamedConfigurationContent(url);
					}

				}));
	}

}