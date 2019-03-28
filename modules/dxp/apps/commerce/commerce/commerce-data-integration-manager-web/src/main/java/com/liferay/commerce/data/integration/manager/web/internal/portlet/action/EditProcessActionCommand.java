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

import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributor;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributorRegistry;
import com.liferay.commerce.data.integration.manager.service.ProcessService;
import com.liferay.commerce.data.integration.manager.web.internal.portlet.constants.DataIntegrationWebPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	property = {
		"javax.portlet.name=" + DataIntegrationWebPortletKeys.DATA_INTEGRATION_WEB,
		"mvc.command.name=editProcess"
	},
	service = MVCActionCommand.class
)
public class EditProcessActionCommand extends BaseMVCActionCommand {

	protected void deleteProcess(ActionRequest actionRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] deleteProcessIds = ParamUtil.getLongValues(
			actionRequest, "deleteProcessIds");

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			if ((deleteProcessIds != null) && (deleteProcessIds.length > 0)) {
				for (long processId : deleteProcessIds) {
					_processService.deleteProcess(
						themeDisplay.getUserId(), processId, serviceContext);
				}
			}
			else {
				long processId = ParamUtil.getLong(actionRequest, "processId");

				_processService.deleteProcess(
					themeDisplay.getUserId(), processId, serviceContext);
			}
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			SessionErrors.add(actionRequest, "errorDeletingProcess");
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (Constants.DELETE.equals(cmd)) {
			deleteProcess(actionRequest);
		}
		else {
			editProcess(actionRequest);
		}
	}

	protected void editProcess(ActionRequest actionRequest) {
		String name = ParamUtil.getString(actionRequest, "name");
		long processId = ParamUtil.getLong(actionRequest, "processId", 0L);
		String processType = ParamUtil.getString(actionRequest, "processType");
		String version = ParamUtil.getString(actionRequest, "version");

		ProcessTypeJSPContributor processTypeJSPContributor =
			_processTypeJSPContributorRegistry.getProcessTypeJSPContributor(
				processType);

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			Process process = null;

			if (processId > 0) {
				process = _processService.getProcess(
					serviceContext.getUserId(), processId);
			}
			else {
				process = _processService.create();
			}

			process.setName(name);

			process.setProcessType(processType);

			process.setVersion(version);

			process = processTypeJSPContributor.processAction(
				actionRequest, process);

			if (processId > 0) {
				_processService.updateProcess(process, serviceContext);
			}
			else {
				_processService.addProcess(process, serviceContext);
			}
		}
		catch (Exception e) {
			_log.error(e, e);

			SessionErrors.add(actionRequest, "errorAddingProcess");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditProcessActionCommand.class);

	@Reference
	private ProcessService _processService;

	@Reference
	private ProcessTypeJSPContributorRegistry
		_processTypeJSPContributorRegistry;

}