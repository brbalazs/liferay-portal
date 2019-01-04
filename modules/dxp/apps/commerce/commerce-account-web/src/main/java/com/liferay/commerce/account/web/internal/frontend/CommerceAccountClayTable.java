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

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.web.internal.model.Account;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableAction;
import com.liferay.commerce.frontend.ClayTableActionProvider;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.Sort;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommerceAccountClayTable.NAME,
		"commerce.table.name=" + CommerceAccountClayTable.NAME
	},
	service = {
		CommerceDataSetDataProvider.class, ClayTable.class,
		ClayTableActionProvider.class
	}
)
public class CommerceAccountClayTable
	implements CommerceDataSetDataProvider, ClayTable, ClayTableActionProvider {

	public static final String NAME = "commerceAccounts";

	@Override
	public List<ClayTableAction> clayTableActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		List<ClayTableAction> clayTableActions = new ArrayList<>();

		Account account = (Account)model;

		PortletURL editURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, Account.class.getName(),
			PortletProvider.Action.EDIT);

		editURL.setParameter("mvcRenderCommandName", "editCommerceAccount");
		editURL.setParameter(
			"commerceAccountId", String.valueOf(account.getAccountId()));

		ClayTableAction clayTableAction = new ClayTableAction(
			editURL.toString(), StringPool.BLANK,
			LanguageUtil.get(httpServletRequest, "edit"), false, false);

		clayTableActions.add(clayTableAction);

		return clayTableActions;
	}

	@Override
	public int countItems(long groupId, Filter filter) throws PortalException {
		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		return _commerceAccountService.getUserCommerceAccountsCount(
			accountFilter.getAccountId());
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("name");
		clayTableSchemaBuilder.addField("accountId", "id");
		clayTableSchemaBuilder.addField("email", "contact");
		clayTableSchemaBuilder.addField("address", "billing-address");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public String getId() {
		return NAME;
	}

	@Override
	public List<Account> getItems(
			long groupId, Filter filter, Pagination pagination, Sort sort)
		throws PortalException {

		List<Account> accounts = new ArrayList<>();

		List<CommerceAccount> commerceAccounts;

		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		commerceAccounts = _commerceAccountService.getUserCommerceAccounts(
			accountFilter.getAccountId(), pagination.getStartPosition(),
			pagination.getEndPosition());

		for (CommerceAccount commerceAccount : commerceAccounts) {
			accounts.add(
				new Account(
					commerceAccount.getCommerceAccountId(),
					commerceAccount.getName(), StringPool.BLANK,
					StringPool.BLANK));
		}

		return accounts;
	}

	@Override
	public boolean isShowActionsMenu() {
		return true;
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceAccountService _commerceAccountService;

}