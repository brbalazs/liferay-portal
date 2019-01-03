/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.account.web.internal.frontend;

import com.liferay.commerce.account.web.internal.model.Member;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.user.service.CommerceUserService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommerceAccountUserClayTable.NAME,
		"commerce.table.name=" + CommerceAccountUserClayTable.NAME
	},
	service = {CommerceDataSetDataProvider.class, ClayTable.class}
)
public class CommerceAccountUserClayTable
	implements CommerceDataSetDataProvider<Member>, ClayTable {

	public static final String NAME = "commerce-account-users";

	@Override
	public int countItems(long groupId, Filter filter) throws PortalException {
		Group group = _groupLocalService.getGroup(groupId);

		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		BaseModelSearchResult<User> baseModelSearchResult =
			_commerceUserService.searchCommerceAccountUsers(
				group.getCompanyId(), accountFilter.getAccountId(),
				accountFilter.getKeywords(), WorkflowConstants.STATUS_ANY,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		return baseModelSearchResult.getLength();
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("name");
		clayTableSchemaBuilder.addField("roles");
		clayTableSchemaBuilder.addField("email");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public String getId() {
		return NAME;
	}

	@Override
	public List<Member> getItems(
			long groupId, Filter filter, Pagination pagination, Sort sort)
		throws PortalException {

		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		List<Member> members = new ArrayList<>();

		BaseModelSearchResult<User> baseModelSearchResult =
			_commerceUserService.searchCommerceAccountUsers(
				groupId, accountFilter.getAccountId(),
				accountFilter.getKeywords(), WorkflowConstants.STATUS_ANY,
				pagination.getStartPosition(), pagination.getEndPosition(),
				sort);

		for (User user : baseModelSearchResult.getBaseModels()) {
			members.add(
				new Member(
					user.getUserId(), user.getFullName(),
					user.getEmailAddress(), getUserRoles(user)));
		}

		return members;
	}

	@Override
	public boolean isShowActionsMenu() {
		return true;
	}

	protected String[] getUserRoles(User user) {
		List<Role> roles = user.getRoles();

		Stream<Role> stream = roles.stream();

		return (String[])stream.map(
			Role::getName
		).toArray();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceUserService _commerceUserService;

	@Reference
	private GroupLocalService _groupLocalService;

}