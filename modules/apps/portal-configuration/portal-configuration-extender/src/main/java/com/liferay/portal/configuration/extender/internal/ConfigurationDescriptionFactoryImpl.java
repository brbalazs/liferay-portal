/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal;

import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.Supplier;

import java.io.IOException;
import java.io.InputStream;

import java.util.Dictionary;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = ConfigurationDescriptionFactory.class)
public class ConfigurationDescriptionFactoryImpl
	implements ConfigurationDescriptionFactory {

	@Override
	public ConfigurationDescription create(
		NamedConfigurationContent namedConfigurationContent) {

		if (!(namedConfigurationContent instanceof
				PropertiesFileNamedConfigurationContent)) {

			return null;
		}

		String pid = null;

		String name = namedConfigurationContent.getName();

		int index = name.lastIndexOf('-');

		if (index > 0) {
			String factoryPid = name.substring(0, index);
			pid = name.substring(index + 1);

			return new FactoryConfigurationDescription(
				factoryPid, pid,
				new PropertiesSupplier(
					namedConfigurationContent.getInputStream()));
		}

		pid = name;

		return new SingleConfigurationDescription(
			pid,
			new PropertiesSupplier(namedConfigurationContent.getInputStream()));
	}

	private class PropertiesSupplier
		implements Supplier<Dictionary<String, Object>> {

		public PropertiesSupplier(InputStream inputStream) {
			_inputStream = inputStream;
		}

		@Override
		public Dictionary<String, Object> get() {
			try {
				return _loadProperties();
			}
			catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}

		private Dictionary<String, Object> _loadProperties()
			throws IOException {

			Dictionary<?, ?> properties = PropertiesUtil.load(
				_inputStream, "UTF-8");

			return (Dictionary<String, Object>)properties;
		}

		private final InputStream _inputStream;

	}

}