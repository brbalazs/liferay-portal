/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.servlet.taglib;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageConstants;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.LexiconUtil;
import com.liferay.users.admin.configuration.UserFileUploadsConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class UserVerticalCardTag extends VerticalCardTag {

	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_userId = 0;
	}

	@Override
	protected String getPage() {
		return "/card/user_vertical_card/page.jsp";
	}

	protected User getUser() {
		return UserLocalServiceUtil.fetchUser(_userId);
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return true;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		User user = getUser();

		request.setAttribute(
			"liferay-frontend:card:colorCssClass",
			LexiconUtil.getUserColorCssClass(user));

		if ((user != null) &&
			((user.getPortraitId() > 0) || !_isImageDefaultUseInitials(user))) {

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			try {
				request.setAttribute(
					"liferay-frontend:card:portraitURL",
					user.getPortraitURL(themeDisplay));
			}
			catch (PortalException pe) {

				// LPS-52675

				if (_log.isDebugEnabled()) {
					_log.debug(pe, pe);
				}
			}
		}

		String initials = StringPool.BLANK;

		if (user != null) {
			initials = user.getInitials();
		}

		request.setAttribute("liferay-frontend:card:userInitials", initials);
	}

	private boolean _isImageDefaultUseInitials(User user) {
		boolean imageDefaultUseInitials = true;

		try {
			UserFileUploadsConfiguration userFileUploadsConfiguration =
				ConfigurationProviderUtil.getSystemConfiguration(
					UserFileUploadsConfiguration.class);

			imageDefaultUseInitials =
				userFileUploadsConfiguration.imageDefaultUseInitials();
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		if (LanguageConstants.VALUE_IMAGE.equals(
				LanguageUtil.get(
					user.getLocale(),
					LanguageConstants.KEY_USER_DEFAULT_PORTRAIT,
					LanguageConstants.VALUE_INITIALS))) {

			imageDefaultUseInitials = false;
		}

		return imageDefaultUseInitials;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UserVerticalCardTag.class);

	private long _userId;

}