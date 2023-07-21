/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.LayoutTestUtil;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Eduardo García
 */
public class UpgradeLookAndFeelTest extends UpgradeLookAndFeel {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testUpgrade() throws Exception {
		_group = GroupTestUtil.addGroup();

		_layout = LayoutTestUtil.addLayout(_group);

		setPortletShowBorders("portlet1", true);
		setPortletShowBorders("portlet2", false);

		upgrade();

		CacheRegistryUtil.clear();

		_layout = LayoutLocalServiceUtil.getLayout(_layout.getPlid());

		Assert.assertEquals(
			StringPool.BLANK, getPortletDecoratorId("portlet1"));
		Assert.assertEquals("borderless", getPortletDecoratorId("portlet2"));
	}

	protected String getPortletDecoratorId(String portletId) throws Exception {
		PortletPreferences portletPreferences =
			LayoutTestUtil.getPortletPreferences(_layout, portletId);

		return portletPreferences.getValue(
			"portletSetupPortletDecoratorId", StringPool.BLANK);
	}

	protected void setPortletShowBorders(String portletId, boolean showBorders)
		throws Exception {

		Map<String, String> portletPreferencesMap = new HashMap<>();

		portletPreferencesMap.put(
			"portletSetupShowBorders", String.valueOf(showBorders));

		LayoutTestUtil.updateLayoutPortletPreferences(
			_layout, portletId, portletPreferencesMap);
	}

	@DeleteAfterTestRun
	private Group _group;

	private Layout _layout;

}