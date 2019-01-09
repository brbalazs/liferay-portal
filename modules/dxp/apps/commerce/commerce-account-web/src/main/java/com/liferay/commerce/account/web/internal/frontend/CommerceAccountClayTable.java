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

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.web.internal.model.Account;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableAction;
import com.liferay.commerce.frontend.ClayTableActionProvider;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.ClayTableSchemaField;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletProvider.Action;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;

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
		ClayTable.class, ClayTableActionProvider.class,
		CommerceDataSetDataProvider.class
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

		String viewURL = _getAccountViewDetailURL(
			account.getAccountId(), httpServletRequest);

		ClayTableAction clayTableAction = new ClayTableAction(
			viewURL, StringPool.BLANK,
			LanguageUtil.get(httpServletRequest, "view"), false, false);

		clayTableActions.add(clayTableAction);

		return clayTableActions;
	}

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		return _commerceAccountService.getUserCommerceAccountsCount(
			accountFilter.getAccountId(), accountFilter.getCommerceSiteType(),
			accountFilter.getKeywords());
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		ClayTableSchemaField nameField = clayTableSchemaBuilder.addField(
			"name", "name");

		nameField.setContentRenderer("imageName");

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
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		List<Account> accounts = new ArrayList<>();
		List<CommerceAccount> commerceAccounts = new ArrayList<>();

		AccountFilterImpl accountFilter = (AccountFilterImpl)filter;

		commerceAccounts = _commerceAccountService.getUserCommerceAccounts(
			accountFilter.getAccountId(), accountFilter.getCommerceSiteType(),
			accountFilter.getKeywords(), pagination.getStartPosition(),
			pagination.getEndPosition());

		for (CommerceAccount commerceAccount : commerceAccounts) {
			StringBundler thumbnailSB = new StringBundler(5);

			thumbnailSB.append(themeDisplay.getPathImage());

			if (commerceAccount.getLogoId() == 0) {
				thumbnailSB.append("/organization_logo?img_id=0");
			}
			else {
				thumbnailSB.append("/organization_logo?img_id=");
				thumbnailSB.append(commerceAccount.getLogoId());
				thumbnailSB.append("&t=");
				thumbnailSB.append(
					WebServerServletTokenUtil.getToken(
						commerceAccount.getLogoId()));
			}

			accounts.add(
				new Account(
					commerceAccount.getCommerceAccountId(),
					commerceAccount.getName(), StringPool.BLANK,
					StringPool.BLANK, thumbnailSB.toString(),
					_getAccountViewDetailURL(
						commerceAccount.getCommerceAccountId(),
						httpServletRequest)));
		}

		return accounts;
	}

	@Override
	public boolean isShowActionsMenu() {
		return true;
	}

	private String _getAccountViewDetailURL(
			long commerceAccountId, HttpServletRequest httpServletRequest)
		throws PortalException {

		PortletURL viewURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CommerceAccount.class.getName(), Action.VIEW);

		viewURL.setParameter(
			"commerceAccountId", String.valueOf(commerceAccountId));

		viewURL.setParameter(
			PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL",
			_portal.getCurrentURL(httpServletRequest));

		return viewURL.toString();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private Portal _portal;

}