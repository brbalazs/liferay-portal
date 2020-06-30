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

package com.liferay.osb.faro.admin.web.internal.portlet;

import com.liferay.osb.faro.admin.web.internal.constants.FaroAdminPortletKeys;
import com.liferay.osb.faro.constants.UpgradeConstants;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PrefsPropsUtil;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shinn Lok
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.scopeable=true",
		"javax.portlet.display-name=Analytics Cloud",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.portlet-title-based-navigation=true",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + FaroAdminPortletKeys.FARO_ADMIN,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator"
	},
	service = Portlet.class
)
public class FaroAdminPortlet extends MVCPortlet {

	@Override
	public void render(RenderRequest request, RenderResponse response)
		throws IOException, PortletException {

		super.render(request, response);
	}

	public void updatePreferences(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		try {
			PortletPreferences portletPreferences =
				PrefsPropsUtil.getPreferences(
					_portal.getCompanyId(actionRequest));

			String version = ParamUtil.getString(actionRequest, "version");

			portletPreferences.setValue(
				UpgradeConstants.REPOSITORY_SHA, version);

			int threadCount = ParamUtil.getInteger(
				actionRequest, "threadCount");

			portletPreferences.setValue(
				UpgradeConstants.UPGRADE_THREAD_COUNT,
				String.valueOf(threadCount));

			portletPreferences.store();
		}
		catch (Exception exception) {
			throw new PortletException(exception);
		}
	}

	@Reference
	private Portal _portal;

}