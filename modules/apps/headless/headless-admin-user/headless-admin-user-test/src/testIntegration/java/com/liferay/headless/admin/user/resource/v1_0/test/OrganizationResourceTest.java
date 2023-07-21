/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.user.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.admin.user.client.dto.v1_0.Organization;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.UserTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class OrganizationResourceTest extends BaseOrganizationResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_user = UserTestUtil.addGroupAdminUser(testGroup);
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"name"};
	}

	@Override
	protected Organization testGetOrganization_addOrganization()
		throws Exception {

		return _addUserOrganization(_user.getUserId(), randomOrganization());
	}

	@Override
	protected Organization testGetOrganizationOrganizationsPage_addOrganization(
			Long parentOrganizationId, Organization organization)
		throws Exception {

		return _toOrganization(
			_addOrganization(organization, parentOrganizationId));
	}

	@Override
	protected Long
			testGetOrganizationOrganizationsPage_getParentOrganizationId()
		throws Exception {

		com.liferay.portal.kernel.model.Organization organization =
			_addOrganization(randomOrganization(), 0);

		return organization.getOrganizationId();
	}

	@Override
	protected Organization testGetOrganizationsPage_addOrganization(
			Organization organization)
		throws Exception {

		return _addUserOrganization(_user.getUserId(), organization);
	}

	@Override
	protected Organization testGraphQLOrganization_addOrganization()
		throws Exception {

		return testGetOrganization_addOrganization();
	}

	private com.liferay.portal.kernel.model.Organization _addOrganization(
			Organization organization, long parentOrganizationId)
		throws PortalException {

		com.liferay.portal.kernel.model.Organization
			serviceBuilderOrganization =
				OrganizationLocalServiceUtil.addOrganization(
					_user.getUserId(), parentOrganizationId,
					organization.getName(), true);

		if (parentOrganizationId == 0) {
			_organizations.add(serviceBuilderOrganization);
		}
		else {
			_childOrganizations.add(serviceBuilderOrganization);
		}

		return serviceBuilderOrganization;
	}

	private Organization _addUserOrganization(
			Long userAccountId, Organization organization)
		throws Exception {

		Organization parentOrganization = _toOrganization(
			_addOrganization(organization, 0));

		if (userAccountId != null) {
			UserLocalServiceUtil.addOrganizationUser(
				parentOrganization.getId(), userAccountId);
		}

		return parentOrganization;
	}

	private Organization _toOrganization(
		com.liferay.portal.kernel.model.Organization organization) {

		return new Organization() {
			{
				dateCreated = organization.getCreateDate();
				dateModified = organization.getModifiedDate();
				id = organization.getOrganizationId();
				name = organization.getName();
			}
		};
	}

	@DeleteAfterTestRun
	private final List<com.liferay.portal.kernel.model.Organization>
		_childOrganizations = new ArrayList<>();

	@DeleteAfterTestRun
	private final List<com.liferay.portal.kernel.model.Organization>
		_organizations = new ArrayList<>();

	@DeleteAfterTestRun
	private User _user;

}