/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.web.internal.portlet.action;

import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Stian Sigvartsen
 */
@Component(
	property = {
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_ADMIN,
		"mvc.command.name=/admin/assign_scopes"
	},
	service = MVCActionCommand.class
)
public class AssignScopesMVCActionCommand implements MVCActionCommand {

	@Override
	public boolean processAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		long oAuth2ApplicationId = ParamUtil.getLong(
			actionRequest, "oAuth2ApplicationId");

		String[] scopeAliases = ParamUtil.getStringValues(
			actionRequest, "scopeAliases");

		List<String> scopeAliasesList = Arrays.asList(scopeAliases);

		try {
			_oAuth2ApplicationService.updateScopeAliases(
				oAuth2ApplicationId, scopeAliasesList);
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to load OAuth 2 application " + oAuth2ApplicationId,
					pe);
			}
		}

		String backURL = ParamUtil.get(
			actionRequest, "backURL", StringPool.BLANK);

		actionResponse.setRenderParameter("redirect", backURL);

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssignScopesMVCActionCommand.class);

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;

}