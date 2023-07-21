/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search;

import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.usersadmin.util.ContactIndexer;
import com.liferay.portlet.usersadmin.util.OrganizationIndexer;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

/**
 * @author Gregory Amerson
 */
public class IndexerRegistryUtilTest {

	@ClassRule
	@Rule
	public static final TestRule testRule = new LiferayIntegrationTestRule();

	@Test
	public void testGetIndexerByIndexerClassName() throws Exception {
		Indexer<Contact> contactIndexer = IndexerRegistryUtil.getIndexer(
			ContactIndexer.class.getName());

		Assert.assertNotNull(contactIndexer);

		Indexer<Organization> organizationIndexer =
			IndexerRegistryUtil.getIndexer(OrganizationIndexer.class.getName());

		Assert.assertNotNull(organizationIndexer);
	}

	@Test
	public void testGetIndexerByModelClassName() throws Exception {
		Indexer<User> userIndexer = IndexerRegistryUtil.getIndexer(
			User.class.getName());

		Assert.assertNotNull(userIndexer);

		Indexer<UserGroup> userGroupIndexer = IndexerRegistryUtil.getIndexer(
			UserGroup.class.getName());

		Assert.assertNotNull(userGroupIndexer);
	}

}