/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.social.service.impl;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portlet.social.service.impl.bundle.socialactivityinterpreterlocalserviceimpl.TestSocialActivityInterpreter;
import com.liferay.social.kernel.model.SocialActivityInterpreter;
import com.liferay.social.kernel.service.SocialActivityInterpreterLocalServiceUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class SocialActivityInterpreterLocalServiceImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule(
				"bundle.socialactivityinterpreterlocalserviceimpl"));

	@Test
	public void testGetActivityInterpreters1() {
		Map<String, List<SocialActivityInterpreter>> activityInterpreters =
			SocialActivityInterpreterLocalServiceUtil.getActivityInterpreters();

		List<SocialActivityInterpreter> socialActivityInterpreters =
			activityInterpreters.get(TestSocialActivityInterpreter.SELECTOR);

		Assert.assertEquals(
			socialActivityInterpreters.toString(), 1,
			socialActivityInterpreters.size());

		SocialActivityInterpreter socialActivityInterpreter =
			socialActivityInterpreters.get(0);

		Assert.assertEquals(
			TestSocialActivityInterpreter.SELECTOR,
			socialActivityInterpreter.getSelector());

		String[] classNames = socialActivityInterpreter.getClassNames();

		Assert.assertEquals(Arrays.toString(classNames), 1, classNames.length);
		Assert.assertEquals(
			TestSocialActivityInterpreter.class.getName(), classNames[0]);
	}

	@Test
	public void testGetActivityInterpreters2() {
		List<SocialActivityInterpreter> activityInterpreters =
			SocialActivityInterpreterLocalServiceUtil.getActivityInterpreters(
				TestSocialActivityInterpreter.SELECTOR);

		Assert.assertEquals(
			activityInterpreters.toString(), 1, activityInterpreters.size());

		SocialActivityInterpreter socialActivityInterpreter =
			activityInterpreters.get(0);

		Assert.assertEquals(
			TestSocialActivityInterpreter.SELECTOR,
			socialActivityInterpreter.getSelector());

		String[] classNames = socialActivityInterpreter.getClassNames();

		Assert.assertEquals(Arrays.toString(classNames), 1, classNames.length);
		Assert.assertEquals(
			TestSocialActivityInterpreter.class.getName(), classNames[0]);
	}

}