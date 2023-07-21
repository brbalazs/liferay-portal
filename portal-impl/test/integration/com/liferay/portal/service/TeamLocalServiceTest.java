/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Team;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.TeamLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TeamTestUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Jonathan McCann
 */
public class TeamLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testDeleteTeam() throws Exception {
		_group = GroupTestUtil.addGroup();

		Team team = TeamTestUtil.addTeam();

		UnicodeProperties typeSettingsProperties =
			_group.getTypeSettingsProperties();

		typeSettingsProperties.setProperty(
			"defaultTeamIds", String.valueOf(team.getTeamId()));

		GroupLocalServiceUtil.updateGroup(_group);

		TeamLocalServiceUtil.deleteTeam(team);

		_group = GroupLocalServiceUtil.getGroup(_group.getGroupId());

		typeSettingsProperties = _group.getTypeSettingsProperties();

		List<Long> defaultTeamIds = ListUtil.fromArray(
			StringUtil.split(
				typeSettingsProperties.getProperty("defaultTeamIds"), 0L));

		Assert.assertFalse(defaultTeamIds.contains(team.getTeamId()));
	}

	@DeleteAfterTestRun
	private Group _group;

}