/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_3;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class UpgradeOrganizationTest extends UpgradeOrganization {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testUpgrade() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		_organization.setType("regular-organization");

		OrganizationLocalServiceUtil.updateOrganization(_organization);

		upgrade();

		List<String> organizationTypes = getOrganizationTypes();

		List<Organization> organizations =
			OrganizationLocalServiceUtil.getOrganizations(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Organization organization : organizations) {
			Assert.assertTrue(
				organizationTypes.contains(organization.getType()));
		}
	}

	@DeleteAfterTestRun
	private Organization _organization;

}