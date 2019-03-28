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

package com.liferay.commerce.data.integration.manager.internal.helper;

import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessActionHelper;
import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessWebKeys;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.service.ProcessLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = DataIntegrationProcessActionHelper.class)
public class DataIntegrationProcessActionHelperImpl
	implements DataIntegrationProcessActionHelper {

	@Override
	public Process getProcess(HttpServletRequest httpServletRequest)
		throws PortalException {

		Process process = (Process)httpServletRequest.getAttribute(
			DataIntegrationProcessWebKeys.DI_PROCESS);

		if (process != null) {
			return process;
		}

		long processId = ParamUtil.getLong(httpServletRequest, "processId");

		if (processId > 0) {
			process = _processLocalService.fetchProcess(processId);
		}

		if (process != null) {
			httpServletRequest.setAttribute(
				DataIntegrationProcessWebKeys.DI_PROCESS, process);
		}

		return process;
	}

	@Reference
	private ProcessLocalService _processLocalService;

}