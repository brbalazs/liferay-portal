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

import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessActionHelper;
import com.liferay.commerce.data.integration.manager.model.ProcessConstants;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributorRegistry;
import com.liferay.commerce.data.integration.manager.service.ProcessService;
import com.liferay.commerce.data.integration.manager.service.ScheduledTaskExectutorService;
import com.liferay.commerce.data.integration.manager.web.internal.display.context.DataIntegrationProcessListDisplayContext;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DataIntegrationWebPortletKeys;
import com.liferay.commerce.data.integration.manager.web.internal.util.DataIntegrationAdminModuleRegistry;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	property = {
		"javax.portlet.name=" + DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB,
		"mvc.command.name=editProcess"
	},
	service = MVCRenderCommand.class
)
public class EditProcessRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		DataIntegrationProcessListDisplayContext
			lrDataIntegrationDisplayContext =
				new DataIntegrationProcessListDisplayContext(
					_portletResourcePermission, _processService,
					_processTypeJSPContributorRegistry, _portal,
					_dataIntegrationProcessActionHelper, renderRequest);

		renderRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT, lrDataIntegrationDisplayContext);

		renderRequest.setAttribute("processTypes", getProcessTypes());

		renderRequest.setAttribute(
			DataIntegrationWebPortletKeys.
				DATA_INTEGRATION_ADMIN_MODULE_REGISTRY,
			_lrDataIntegrationAdminModuleRegistry);

		return "/processes/edit_process.jsp";
	}

	@Activate
	@Modified
	protected void activate(BundleContext bundleContext) {
		_scheduledTaskExectutorServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScheduledTaskExectutorService.class,
				"data.integration.service.executor.key");
	}

	@Deactivate
	protected void deactivate() {
		_scheduledTaskExectutorServiceTrackerMap.close();
	}

	protected Map<String, String> getProcessTypes() {
		Map<String, String> processTypes = new HashMap<>();

		Class<?> serviceClass;

		if (_scheduledTaskExectutorServiceTrackerMap != null) {
			for (String key :
					_scheduledTaskExectutorServiceTrackerMap.keySet()) {

				ScheduledTaskExectutorService scheduledTaskExectutorService =
					_scheduledTaskExectutorServiceTrackerMap.getService(key);

				serviceClass = scheduledTaskExectutorService.getClass();

				processTypes.put(
					scheduledTaskExectutorService.getName(),
					serviceClass.getName());
			}
		}

		return processTypes;
	}

	@Reference
	private DataIntegrationProcessActionHelper
		_dataIntegrationProcessActionHelper;

	@Reference
	private DataIntegrationAdminModuleRegistry
		_lrDataIntegrationAdminModuleRegistry;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + ProcessConstants.RESOURCE_NAME + ")",
		unbind = "-"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private ProcessService _processService;

	@Reference
	private ProcessTypeJSPContributorRegistry
		_processTypeJSPContributorRegistry;

	private ServiceTrackerMap<String, ScheduledTaskExectutorService>
		_scheduledTaskExectutorServiceTrackerMap;

}