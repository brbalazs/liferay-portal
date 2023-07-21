/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.webdav;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.webdav.bundle.webdavutil.TestWebDAVStorage;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Collection;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class WebDAVUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.webdavutil"));

	@Test
	public void testGetStorage() {
		WebDAVStorage webDAVStorage = WebDAVUtil.getStorage(
			TestWebDAVStorage.TOKEN);

		Class<?> clazz = webDAVStorage.getClass();

		Assert.assertEquals(TestWebDAVStorage.class.getName(), clazz.getName());
	}

	@Test
	public void testGetStorageTokens() {
		Collection<String> storageTokens = WebDAVUtil.getStorageTokens();

		Assert.assertTrue(
			storageTokens.toString(),
			storageTokens.contains(TestWebDAVStorage.TOKEN));
	}

}