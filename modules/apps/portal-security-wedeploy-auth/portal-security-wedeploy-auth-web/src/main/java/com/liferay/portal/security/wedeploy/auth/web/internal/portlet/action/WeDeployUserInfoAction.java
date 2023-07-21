/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.wedeploy.auth.web.internal.portlet.action;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.security.wedeploy.auth.constants.WeDeployAuthTokenConstants;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthToken;
import com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthTokenLocalService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Supritha Sundaram
 */
@Component(
	immediate = true, property = "path=/portal/wedeploy/user",
	service = StrutsAction.class
)
public class WeDeployUserInfoAction extends BaseStrutsAction {

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		response.setContentType(ContentTypes.APPLICATION_JSON);
		response.setHeader(
			HttpHeaders.CACHE_CONTROL,
			HttpHeaders.CACHE_CONTROL_NO_CACHE_VALUE);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		String accessToken = ParamUtil.getString(request, "token");

		try {
			WeDeployAuthToken weDeployAuthToken =
				_weDeployAuthTokenLocalService.getWeDeployAuthToken(
					accessToken, WeDeployAuthTokenConstants.TYPE_AUTHORIZATION);

			User user = _userLocalService.getUser(
				weDeployAuthToken.getUserId());

			JSONObject userJSONObject = JSONFactoryUtil.createJSONObject();

			userJSONObject.put("email", user.getEmailAddress());
			userJSONObject.put("name", user.getFullName());

			jsonObject.put("info", userJSONObject);
		}
		catch (Exception e) {
			_log.error(e, e);

			jsonObject.put(
				"error_message",
				LanguageUtil.get(
					LocaleUtil.getDefault(),
					"an-error-occurred-while-processing-the-requested-" +
						"resource"));
		}

		ServletResponseUtil.write(response, jsonObject.toString());

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WeDeployUserInfoAction.class);

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private WeDeployAuthTokenLocalService _weDeployAuthTokenLocalService;

}