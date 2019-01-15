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

package com.liferay.commerce.account.web.internal.display.context;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.account.web.internal.frontend.AccountFilterImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;
import com.liferay.users.admin.configuration.UserFileUploadsConfiguration;

import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
public class CommerceAccountDisplayContext
	extends BaseCommerceAccountDisplayContext {

	public CommerceAccountDisplayContext(
		CommerceAccountHelper commerceAccountHelper,
		CommerceAccountService commerceAccountService,
		HttpServletRequest httpServletRequest, Portal portal,
		UserFileUploadsConfiguration userFileUploadsConfiguration) {

		super(
			commerceAccountHelper, commerceAccountService, httpServletRequest,
			portal);

		_userFileUploadsConfiguration = userFileUploadsConfiguration;
	}

	public AccountFilterImpl getAccountFilter() throws PortalException {
		AccountFilterImpl accountFilter = new AccountFilterImpl();

		HttpServletRequest httpServletRequest =
			commerceAccountRequestHelper.getRequest();

		boolean filterPerAcount = (boolean)httpServletRequest.getAttribute(
			"view.jsp-filterPerAccount");

		if (filterPerAcount) {
			accountFilter.setAccountId(getCurrentCommerceAccountId());
		}

		accountFilter.setKeywords(getKeywords());

		return accountFilter;
	}

	public String getAddCommerceAccountHref() throws WindowStateException {
		HttpServletRequest httpServletRequest =
			commerceAccountRequestHelper.getRequest();
		LiferayPortletResponse liferayPortletResponse =
			commerceAccountRequestHelper.getLiferayPortletResponse();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter("mvcRenderCommandName", "addCommerceAccount");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		StringBundler sb = new StringBundler(9);

		sb.append("javascript:");
		sb.append(liferayPortletResponse.getNamespace());
		sb.append("addCommerceAccount");
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(StringPool.APOSTROPHE);
		sb.append(portletURL.toString());
		sb.append(StringPool.APOSTROPHE);
		sb.append(StringPool.CLOSE_PARENTHESIS);
		sb.append(StringPool.SEMICOLON);

		return sb.toString();
	}

	public String getLogo(CommerceAccount commerceAccount) {
		ThemeDisplay themeDisplay =
			commerceAccountRequestHelper.getThemeDisplay();

		StringBundler sb = new StringBundler(5);

		sb.append(themeDisplay.getPathImage());
		sb.append("/organization_logo?img_id=");
		sb.append(commerceAccount.getLogoId());
		sb.append("&t=");
		sb.append(
			WebServerServletTokenUtil.getToken(commerceAccount.getLogoId()));

		return sb.toString();
	}

	public UserFileUploadsConfiguration getUserFileUploadsConfiguration() {
		return _userFileUploadsConfiguration;
	}

	private final UserFileUploadsConfiguration _userFileUploadsConfiguration;

}