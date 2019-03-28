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

package com.liferay.commerce.data.integration.manager.web.internal.admin;

import com.liferay.commerce.data.integration.manager.model.ProcessConstants;
import com.liferay.commerce.data.integration.manager.service.HistoryLocalService;
import com.liferay.commerce.data.integration.manager.web.internal.admin.api.DataIntegrationAdminModule;
import com.liferay.commerce.data.integration.manager.web.internal.display.context.HistoryDataIntegrationDisplayContext;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.action.ActionHelper;
import com.liferay.document.library.kernel.service.DLFileEntryService;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	immediate = true,
	property = "data.integration.admin.module.key=" + HistoryAdminModule.KEY,
	service = DataIntegrationAdminModule.class
)
public class HistoryAdminModule implements DataIntegrationAdminModule {

	public static final String KEY = "history-tasks";

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "history-tasks-admin");
	}

	@Override
	public PortletURL getSearchURL(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		return null;
	}

	@Override
	public boolean isVisible(long groupId) throws PortalException {
		return true;
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException {

		HistoryDataIntegrationDisplayContext
			historyDataIntegrationDisplayContext =
				new HistoryDataIntegrationDisplayContext(
					_portletResourcePermission, _historyLocalService,
					_dlFileEntryService, _portal, _actionHelper, renderRequest);

		renderRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			historyDataIntegrationDisplayContext);

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			renderRequest);
		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(renderResponse);

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/history/historical_task_list.jsp");
	}

	@Reference
	private ActionHelper _actionHelper;

	@Reference
	private DLFileEntryService _dlFileEntryService;

	@Reference
	private HistoryLocalService _historyLocalService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + ProcessConstants.RESOURCE_NAME + ")",
		unbind = "-"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.data.integration.manager.web)"
	)
	private ServletContext _servletContext;

}