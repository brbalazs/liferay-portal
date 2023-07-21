/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.hibernate.configuration;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Miguel Pastor
 */
public class ModuleHibernateConfigurationTest {

	@Test
	public void testConfigurationPaths() {
		String[] configurationResources =
			_moduleHibernateConfiguration.getConfigurationResources();

		Assert.assertEquals(
			Arrays.toString(configurationResources), 1,
			configurationResources.length);
		Assert.assertEquals(
			"META-INF/module-hbm.xml", configurationResources[0]);
	}

	private final ModuleHibernateConfiguration _moduleHibernateConfiguration =
		new ModuleHibernateConfiguration();

}