/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.deploy.hot;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.EmailAddress;
import com.liferay.portal.kernel.service.EmailAddressLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 * @author Miguel Pastor
 */
public class ServiceWrapperRegistryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.servicewrapperregistry"));

	@BeforeClass
	public static void setUpClass() throws Exception {
		_serviceWrapperRegistry = new ServiceWrapperRegistry();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_serviceWrapperRegistry.close();
	}

	@Test
	public void testInvokeOverrideMethod() throws PortalException {
		EmailAddressLocalService emailAddressLocalService =
			(EmailAddressLocalService)PortalBeanLocatorUtil.locate(
				EmailAddressLocalService.class.getName());

		EmailAddress emailAddress = emailAddressLocalService.getEmailAddress(1);

		Assert.assertEquals("email@liferay.com", emailAddress.getAddress());
	}

	private static ServiceWrapperRegistry _serviceWrapperRegistry;

}