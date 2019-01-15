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

package com.liferay.commerce.data.integration.manager.web.internal.portlet.action;

import com.liferay.commerce.data.integration.manager.service.HistoryLocalService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskExectutorService;
import com.liferay.commerce.data.integration.manager.web.internal.display.context.HistoryDataIntegrationDisplayContext;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DataIntegrationWebPortletKeys;
import com.liferay.commerce.data.integration.manager.web.internal.util.DataIntegrationAdminModuleRegistry;
import com.liferay.document.library.kernel.service.DLFileEntryService;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	property = {
		"javax.portlet.name=" + DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB,
		"mvc.command.name=viewHistoryDetails"
	},
	service = MVCRenderCommand.class
)
public class ViewHistorydetailsRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		HistoryDataIntegrationDisplayContext
			historyDataIntegrationDisplayContext =
				new HistoryDataIntegrationDisplayContext(
					_portletResourcePermission, _historyLocalService,
					_dlFileEntryService, _portal, _actionHelper, renderRequest);

		renderRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT,
			historyDataIntegrationDisplayContext);

		renderRequest.setAttribute(
			DataIntegrationWebPortletKeys.
				DATA_INTEGRATION_ADMIN_MODULE_REGISTRY,
			_lrDataIntegrationAdminModuleRegistry);

		return "/history/historical_task_details.jsp";
	}

	@Activate
	@Modified
	protected void init(ComponentContext context) {
		processTypes = new HashMap<>();
		BundleContext bundleContext = context.getBundleContext();
		ServiceReference<?>[] references = null;

		try {
			Class<?> scheduledTaskExectutorService =
				ScheduledTaskExectutorService.class;

			references = bundleContext.getAllServiceReferences(
				scheduledTaskExectutorService.getName(), null);
		}
		catch (InvalidSyntaxException ise) {
			ise.printStackTrace();
		}

		if (references != null) {
			for (ServiceReference<?> reference : references) {
				ScheduledTaskExectutorService scheduledTaskExectutorService =
					(ScheduledTaskExectutorService)
						bundleContext.getService(reference);

				Class<?> scheduledTaskExectutorServiceClass =
					scheduledTaskExectutorService.getClass();

				processTypes.put(
					scheduledTaskExectutorService.getName(),
					scheduledTaskExectutorServiceClass.getName());
			}
		}
	}

	protected Map<String, String> processTypes;

	@Reference
	private ActionHelper _actionHelper;

	@Reference
	private DLFileEntryService _dlFileEntryService;

	@Reference
	private HistoryLocalService _historyLocalService;

	@Reference
	private DataIntegrationAdminModuleRegistry
		_lrDataIntegrationAdminModuleRegistry;

	@Reference
	private Portal _portal;

	@Reference
	private PortletResourcePermission _portletResourcePermission;

}