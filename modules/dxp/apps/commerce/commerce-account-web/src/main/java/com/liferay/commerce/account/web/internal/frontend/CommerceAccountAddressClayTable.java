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
import com.liferay.commerce.account.web.internal.model.Account;
import com.liferay.commerce.account.web.internal.model.Address;
import com.liferay.commerce.constants.CommerceActionKeys;
import com.liferay.commerce.constants.CommerceConstants;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableAction;
import com.liferay.commerce.frontend.ClayTableActionProvider;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommerceAccountAddressClayTable.NAME,
		"commerce.table.name=" + CommerceAccountAddressClayTable.NAME
	},
	service = {
		CommerceDataSetDataProvider.class, ClayTable.class,
		ClayTableActionProvider.class
	}
)
public class CommerceAccountAddressClayTable
	implements CommerceDataSetDataProvider<Address>, ClayTable,
			   ClayTableActionProvider {

	public static final String NAME = "commerceAccountAddresses";

	@Override
	public List<ClayTableAction> clayTableActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!_portletResourcePermission.contains(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(),
				CommerceActionKeys.MANAGE_COMMERCE_ADDRESSES)) {

			return Collections.emptyList();
		}

		List<ClayTableAction> clayTableActions = new ArrayList<>();

		Address address = (Address)model;

		String title = LanguageUtil.get(httpServletRequest, "add-address");

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CommerceAccount.class.getName(),
			PortletProvider.Action.VIEW);

		portletURL.setParameter("mvcRenderCommandName", "editCommerceAddress");

		if (address != null) {
			title = LanguageUtil.get(httpServletRequest, "edit-address");

			portletURL.setParameter(
				"commerceAddressId", String.valueOf(address.getAddressId()));
		}

		try {
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (WindowStateException wse) {
			_log.error(wse, wse);
		}

		StringBundler sb = new StringBundler(11);

		sb.append("javascript:editCommerceAddress");
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(StringPool.APOSTROPHE);
		sb.append(title);
		sb.append(StringPool.APOSTROPHE);
		sb.append(StringPool.COMMA_AND_SPACE);
		sb.append(StringPool.APOSTROPHE);
		sb.append(portletURL.toString());
		sb.append(StringPool.APOSTROPHE);
		sb.append(StringPool.CLOSE_PARENTHESIS);
		sb.append(StringPool.SEMICOLON);

		ClayTableAction clayTableAction = new ClayTableAction(
			sb.toString(), StringPool.BLANK,
			LanguageUtil.get(httpServletRequest, "edit"), false, false);

		clayTableActions.add(clayTableAction);

		return clayTableActions;
	}

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		AccountAddressFilterImpl accountFilter =
			(AccountAddressFilterImpl)filter;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _commerceAddressService.getCommerceAddressesCount(
			themeDisplay.getScopeGroupId(), accountFilter.getClassName(),
			accountFilter.getClassPK());
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("address", "address");
		clayTableSchemaBuilder.addField("referent", "referent");
		clayTableSchemaBuilder.addField("phone-number", "phone-number");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public String getId() {
		return NAME;
	}

	@Override
	public List<Address> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		AccountAddressFilterImpl accountFilter =
			(AccountAddressFilterImpl)filter;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		List<Address> addresses = new ArrayList<>();

		List<CommerceAddress> commerceAddresses =
			_commerceAddressService.getCommerceAddresses(
				themeDisplay.getScopeGroupId(), accountFilter.getClassName(),
				accountFilter.getClassPK(), pagination.getStartPosition(),
				pagination.getEndPosition(), null);

		for (CommerceAddress commerceAddress : commerceAddresses) {
			addresses.add(
				new Address(
					commerceAddress.getCommerceAddressId(),
					getCompleteAddress(commerceAddress),
					commerceAddress.getName(),
					commerceAddress.getPhoneNumber()));
		}

		return addresses;
	}

	@Override
	public boolean isShowActionsMenu() {
		return true;
	}

	protected String getCompleteAddress(CommerceAddress commerceAddress)
		throws PortalException {

		StringBundler sb = new StringBundler(9);

		sb.append(commerceAddress.getZip());
		sb.append(StringPool.SPACE);
		sb.append(commerceAddress.getStreet1());
		sb.append(StringPool.SPACE);
		sb.append(commerceAddress.getCity());
		sb.append(StringPool.SPACE);
		sb.append(StringPool.DASH);
		sb.append(StringPool.SPACE);

		CommerceCountry commerceCountry = commerceAddress.getCommerceCountry();

		sb.append(commerceCountry.getThreeLettersISOCode());

		return sb.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceAccountAddressClayTable.class);

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference(
		target = "(resource.name=" + CommerceConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}