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

package com.liferay.commerce.data.integration.manager.talend.util;

import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessActionHelper;
import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessFileEntryUploadActionHelper;
import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessWebKeys;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.process.type.ProcessTypeJSPContributor;
import com.liferay.commerce.data.integration.manager.talend.util.launcher.TalendJobLauncher;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import javax.portlet.ActionRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	property = "commerce.data.integration.process.type.key=" + TalendJobLauncher.KEY,
	service = ProcessTypeJSPContributor.class
)
public class TalendJobJSPContributor implements ProcessTypeJSPContributor {

	@Override
	public Process processAction(ActionRequest actionRequest, Process process)
		throws Exception {

		String className = ParamUtil.getString(actionRequest, "className");

		DLFileEntry archiveFileEntry = null;

		DLFileEntry contextFileEntry = null;

		contextFileEntry =
			_dataIntegrationProcessFileEntryUploadActionHelper.upload(
				actionRequest, "contextProperties");

		long contextFileEntryId = 0L;

		if (contextFileEntry != null) {
			contextFileEntryId = contextFileEntry.getFileEntryId();
		}

		archiveFileEntry =
			_dataIntegrationProcessFileEntryUploadActionHelper.upload(
				actionRequest, "srcArchive");

		process.setClassName(className);

		process.setSrcArchiveFileEntryId(archiveFileEntry.getFileEntryId());

		process.setContextPropertiesFileEntryId(contextFileEntryId);

		return process;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		httpServletRequest.setAttribute(
			DataIntegrationProcessWebKeys.DI_PROCESS_ACTION_HELPER,
			_dataIntegrationProcessActionHelper);

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/view.jsp");
	}

	@Reference
	private DataIntegrationProcessActionHelper
		_dataIntegrationProcessActionHelper;

	@Reference
	private DataIntegrationProcessFileEntryUploadActionHelper
		_dataIntegrationProcessFileEntryUploadActionHelper;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.data.integration.manager.talend.util)"
	)
	private ServletContext _servletContext;

}