/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.security.permission.bundle.permissioncheckerfactoryutil.TestPermissionCheckerFactory;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Tomas Polesovsky
 */
public class PermissionCheckerFactoryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.permissioncheckerfactoryutil"));

	@Test
	public void testGetPermissionCheckerFactory() {
		PermissionCheckerFactory permissionCheckerFactory =
			PermissionCheckerFactoryUtil.getPermissionCheckerFactory();

		Assert.assertNotNull(permissionCheckerFactory);

		Class<?> clazz = permissionCheckerFactory.getClass();

		Assert.assertEquals(
			TestPermissionCheckerFactory.class.getName(), clazz.getName());
	}

}