/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris.asset.category.demo.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.meris.MerisProfile;
import com.liferay.meris.MerisProfileManager;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo García
 */
@RunWith(Arquillian.class)
public class AssetCategoryMerisProfileManagerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		ServiceTestUtil.setUser(TestPropsValues.getUser());

		_user = UserTestUtil.addUser();

		_merisProfileId = String.valueOf(_user.getUserId());
	}

	@Test
	public void testGetMerisProfile() {
		Assert.assertNotNull(
			"Meris profile was not found",
			_merisProfileManager.getMerisProfile(_merisProfileId));
	}

	@Test
	public void testGetMerisProfiles() {
		Comparator<MerisProfile> comparator = Comparator.comparing(
			p -> p.getMerisProfileId());

		List<MerisProfile> merisProfiles =
			_merisProfileManager.getMerisProfiles(0, 1, comparator);

		Assert.assertFalse(
			"No meris profiles were found", merisProfiles.isEmpty());
	}

	@Inject
	private static MerisProfileManager _merisProfileManager;

	private String _merisProfileId;

	@DeleteAfterTestRun
	private User _user;

}