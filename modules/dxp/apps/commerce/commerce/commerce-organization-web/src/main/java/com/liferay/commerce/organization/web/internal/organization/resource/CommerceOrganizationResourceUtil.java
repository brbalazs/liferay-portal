/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.organization.web.internal.organization.resource;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountOrganizationRel;
import com.liferay.commerce.account.service.CommerceAccountOrganizationRelService;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.organization.web.internal.organization.model.Account;
import com.liferay.commerce.organization.web.internal.organization.model.AccountList;
import com.liferay.commerce.organization.web.internal.organization.model.Organization;
import com.liferay.commerce.organization.web.internal.organization.model.OrganizationList;
import com.liferay.commerce.organization.web.internal.organization.model.User;
import com.liferay.commerce.organization.web.internal.organization.model.UserList;
import com.liferay.portal.kernel.service.OrganizationService;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(service = CommerceOrganizationResourceUtil.class)
public class CommerceOrganizationResourceUtil {

	public OrganizationList getOrganizationList(
			long companyId, long parentOrganizationId, Pagination pagination)
		throws Exception {

		return new OrganizationList(
			_toOrganizationList(
				_organizationService.getOrganizations(
					companyId, parentOrganizationId),
				pagination),
			_organizationService.getOrganizationsCount(
				companyId, parentOrganizationId));
	}

	protected AccountList getAccountList(
			long organizationId, Pagination pagination)
		throws Exception {

		List<Account> accounts = new ArrayList<>();

		List<CommerceAccountOrganizationRel> commerceAccountOrganizationRels =
			_commerceAccountOrganizationRelService.
				getCommerceAccountOrganizationRelsByOrganizationId(
					organizationId, pagination.getStartPosition(),
					pagination.getEndPosition());

		for (CommerceAccountOrganizationRel commerceAccountOrganizationRel :
				commerceAccountOrganizationRels) {

			CommerceAccount commerceAccount =
				_commerceAccountService.getCommerceAccount(
					commerceAccountOrganizationRel.getCommerceAccountId());

			accounts.add(
				new Account(
					commerceAccount.getCommerceAccountId(),
					commerceAccount.getName()));
		}

		int total =
			_commerceAccountOrganizationRelService.
				getCommerceAccountOrganizationRelsByOrganizationIdCount(
					organizationId);

		return new AccountList(accounts, total);
	}

	protected UserList getUserList(long organizationId, Pagination pagination)
		throws Exception {

		List<User> users = new ArrayList<>();

		List<com.liferay.portal.kernel.model.User> userList =
			_userService.getOrganizationUsers(
				organizationId, WorkflowConstants.STATUS_APPROVED,
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);

		for (com.liferay.portal.kernel.model.User user : userList) {
			users.add(new User(user.getUserId(), user.getFullName()));
		}

		int total = _userService.getOrganizationUsersCount(
			organizationId, WorkflowConstants.STATUS_APPROVED);

		return new UserList(users, total);
	}

	private List<Organization> _toOrganizationList(
			List<com.liferay.portal.kernel.model.Organization> organizations,
			Pagination pagination)
		throws Exception {

		List<Organization> organizationList = new ArrayList<>();

		for (com.liferay.portal.kernel.model.Organization organization :
				organizations) {

			organizationList.add(
				new Organization(
					organization.getOrganizationId(),
					organization.getParentOrganizationId(),
					organization.getName(),
					new OrganizationList(
						_toOrganizationList(
							organization.getSuborganizations(), pagination),
						_organizationService.getOrganizationsCount(
							organization.getCompanyId(),
							organization.getParentOrganizationId()))));
		}

		return organizationList;
	}

	@Reference
	private CommerceAccountOrganizationRelService
		_commerceAccountOrganizationRelService;

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private OrganizationService _organizationService;

	@Reference
	private UserService _userService;

}