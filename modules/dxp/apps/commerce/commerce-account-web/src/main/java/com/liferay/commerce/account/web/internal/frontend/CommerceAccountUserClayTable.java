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

package com.liferay.commerce.account.web.internal.frontend;

import com.liferay.commerce.account.model.CommerceAccountUserRel;
import com.liferay.commerce.account.service.CommerceAccountUserRelService;
import com.liferay.commerce.account.web.internal.model.Member;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Sort;

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
		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		return _commerceAccountUserRelService.getCommerceAccountUserRelsCount(
			accountFilter.getAccountId());
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

		List<CommerceAccountUserRel> commerceAccountUserRels =
			_commerceAccountUserRelService.getCommerceAccountUserRels(
				accountFilter.getAccountId(), pagination.getStartPosition(),
				pagination.getEndPosition());

		for (CommerceAccountUserRel commerceAccountUserRel :
				commerceAccountUserRels) {

			User user = commerceAccountUserRel.getUser();

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
	private CommerceAccountUserRelService _commerceAccountUserRelService;

}