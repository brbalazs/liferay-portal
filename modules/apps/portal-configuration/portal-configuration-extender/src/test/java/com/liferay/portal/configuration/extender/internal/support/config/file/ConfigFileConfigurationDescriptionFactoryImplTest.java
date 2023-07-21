/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal.support.config.file;

import com.liferay.portal.configuration.extender.internal.ConfigurationDescription;
import com.liferay.portal.configuration.extender.internal.ConfigurationDescriptionFactory;
import com.liferay.portal.configuration.extender.internal.ConfigurationDescriptionFactoryImpl;
import com.liferay.portal.configuration.extender.internal.FactoryConfigurationDescription;
import com.liferay.portal.configuration.extender.internal.NamedConfigurationContent;
import com.liferay.portal.configuration.extender.internal.SingleConfigurationDescription;
import com.liferay.portal.kernel.util.Supplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.Charset;

import java.util.Dictionary;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Carlos Sierra Andrés
 */
public class ConfigFileConfigurationDescriptionFactoryImplTest {

	@Test
	public void testCreateFactoryConfiguration() {
		ConfigurationDescriptionFactory configurationDescriptionFactory =
			new ConfigFileConfigurationDescriptionFactoryImpl();

		String configurationContent =
			"key=\"value\"\nanotherKey=\"anotherValue\"";

		ConfigurationDescription configurationDescription =
			configurationDescriptionFactory.create(
				new ConfigFileNamedConfigurationContent(
					"factory.pid-config.pid",
					new ByteArrayInputStream(
						configurationContent.getBytes(
							Charset.forName("UTF-8")))));

		Assert.assertTrue(
			configurationDescription instanceof
				FactoryConfigurationDescription);

		FactoryConfigurationDescription factoryConfigurationDescription =
			(FactoryConfigurationDescription)configurationDescription;

		Assert.assertEquals(
			"config.pid", factoryConfigurationDescription.getPid());
		Assert.assertEquals(
			"factory.pid", factoryConfigurationDescription.getFactoryPid());

		Supplier<Dictionary<String, Object>> propertiesSupplier =
			factoryConfigurationDescription.getPropertiesSupplier();

		Dictionary<String, Object> properties = propertiesSupplier.get();

		Assert.assertEquals("anotherValue", properties.get("anotherKey"));
		Assert.assertEquals("value", properties.get("key"));
	}

	@Test
	public void testCreateReturnsNullWhenNotPropertiesFileNamedConfigurationContent() {
		ConfigurationDescriptionFactory configurationDescriptionFactory =
			new ConfigurationDescriptionFactoryImpl();

		ConfigurationDescription configurationDescription =
			configurationDescriptionFactory.create(
				new NamedConfigurationContent() {

					@Override
					public InputStream getInputStream() {
						return new ByteArrayInputStream(new byte[0]);
					}

					@Override
					public String getName() {
						return "aName";
					}

				});

		Assert.assertNull(configurationDescription);
	}

	@Test
	public void testCreateSingleConfiguration() {
		ConfigurationDescriptionFactory configurationDescriptionFactory =
			new ConfigFileConfigurationDescriptionFactoryImpl();

		String configurationContent =
			"key=\"value\"\nanotherKey=\"anotherValue\"";

		ConfigurationDescription configurationDescription =
			configurationDescriptionFactory.create(
				new ConfigFileNamedConfigurationContent(
					"config.pid",
					new ByteArrayInputStream(
						configurationContent.getBytes(
							Charset.forName("UTF-8")))));

		Assert.assertTrue(
			configurationDescription instanceof SingleConfigurationDescription);

		SingleConfigurationDescription singleConfigurationDescription =
			(SingleConfigurationDescription)configurationDescription;

		Assert.assertEquals(
			"config.pid", singleConfigurationDescription.getPid());

		Supplier<Dictionary<String, Object>> propertiesSupplier =
			singleConfigurationDescription.getPropertiesSupplier();

		Dictionary<String, Object> properties = propertiesSupplier.get();

		Assert.assertEquals("anotherValue", properties.get("anotherKey"));
		Assert.assertEquals("value", properties.get("key"));
	}

}