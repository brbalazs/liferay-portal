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

import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.account.web.internal.servlet.taglib.ui.CommerceAccountScreenNavigationConstants;
import com.liferay.commerce.user.constants.CommerceUserPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
public class CommerceAccountMembersDisplayContext
	extends BaseCommerceAccountDisplayContext {

	public CommerceAccountMembersDisplayContext(
		CommerceAccountHelper commerceAccountHelper,
		CommerceAccountService commerceAccountService,
		HttpServletRequest httpServletRequest, Portal portal) {

		super(
			commerceAccountHelper, commerceAccountService, httpServletRequest,
			portal);

		setDefaultOrderByCol("name");
		setDefaultOrderByType("asc");
	}

	public String getEditURL(User user) throws PortalException {
		long groupId = portal.getScopeGroupId(
			commerceAccountRequestHelper.getRequest());

		long plid = portal.getPlidFromPortletId(
			groupId, CommerceUserPortletKeys.COMMERCE_USER);

		if (plid <= 0) {
			return StringPool.BLANK;
		}

		PortletURL portletURL = PortletURLFactoryUtil.create(
			commerceAccountRequestHelper.getRequest(),
			CommerceUserPortletKeys.COMMERCE_USER, plid,
			PortletRequest.RENDER_PHASE);

		String redirect = portal.getCurrentCompleteURL(
			commerceAccountRequestHelper.getRequest());

		portletURL.setParameter("redirect", redirect);

		portletURL.setParameter("userId", String.valueOf(user.getUserId()));

		return portletURL.toString();
	}

	public String getInviteUserHref() throws WindowStateException {
		HttpServletRequest httpServletRequest =
			commerceAccountRequestHelper.getRequest();
		LiferayPortletResponse liferayPortletResponse =
			commerceAccountRequestHelper.getLiferayPortletResponse();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter("mvcRenderCommandName", "inviteUser");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		StringBundler sb = new StringBundler(9);

		sb.append("javascript:");
		sb.append(liferayPortletResponse.getNamespace());
		sb.append("inviteUser");
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(StringPool.APOSTROPHE);
		sb.append(portletURL.toString());
		sb.append(StringPool.APOSTROPHE);
		sb.append(StringPool.CLOSE_PARENTHESIS);
		sb.append(StringPool.SEMICOLON);

		return sb.toString();
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(
			commerceAccountRequestHelper.getRequest(), "keywords");

		return _keywords;
	}

	@Override
	public PortletURL getPortletURL() throws PortalException {
		PortletURL portletURL = super.getPortletURL();

		portletURL.setParameter(
			"screenNavigationCategoryKey",
			CommerceAccountScreenNavigationConstants.CATEGORY_DETAILS);

		portletURL.setParameter(
			"screenNavigationEntryKey",
			CommerceAccountScreenNavigationConstants.ENTRY_KEY_ACCOUNT_MEMBERS);

		return portletURL;
	}

	private String _keywords;

}