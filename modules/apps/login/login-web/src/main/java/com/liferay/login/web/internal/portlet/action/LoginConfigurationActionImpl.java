/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.login.web.internal.portlet.action;

import com.liferay.login.web.internal.constants.LoginPortletKeys;
import com.liferay.petra.content.ContentUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.util.PropsValues;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 * @author Julio Camarero
 */
@Component(
	property = {
		"javax.portlet.name=" + LoginPortletKeys.FAST_LOGIN,
		"javax.portlet.name=" + LoginPortletKeys.LOGIN
	},
	service = ConfigurationAction.class
)
public class LoginConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void postProcess(
		long companyId, PortletRequest portletRequest,
		PortletPreferences portletPreferences) {

		String languageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailPasswordResetBody_" + languageId,
			ContentUtil.get(
				PortalClassLoaderUtil.getClassLoader(),
				PropsValues.ADMIN_EMAIL_PASSWORD_RESET_BODY));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailPasswordResetSubject_" + languageId,
			ContentUtil.get(
				PortalClassLoaderUtil.getClassLoader(),
				PropsValues.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailPasswordSentBody_" + languageId,
			ContentUtil.get(
				PortalClassLoaderUtil.getClassLoader(),
				PropsValues.ADMIN_EMAIL_PASSWORD_SENT_BODY));
		removeDefaultValue(
			portletRequest, portletPreferences,
			"emailPasswordSentSubject_" + languageId,
			ContentUtil.get(
				PortalClassLoaderUtil.getClassLoader(),
				PropsValues.ADMIN_EMAIL_PASSWORD_SENT_SUBJECT));
	}

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		validateEmailFrom(actionRequest);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

}