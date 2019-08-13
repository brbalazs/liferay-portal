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

package com.liferay.commerce.data.integration.talend.internal.process.type;

import com.liferay.commerce.data.integration.process.type.ProcessType;
import com.liferay.commerce.data.integration.process.type.ProcessTypeJSPContributor;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessService;
import com.liferay.commerce.data.integration.talend.internal.TalendScheduledTaskExecutorService;
import com.liferay.commerce.data.integration.trigger.CommerceDataIntegrationProcessFileEntryUploadActionHelper;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.integration.process.type.key=" + TalendScheduledTaskExecutorService.KEY,
		"commerce.data.integration.process.type.order=100"
	},
	service = {ProcessType.class, ProcessTypeJSPContributor.class}
)
public class TalendProcessTypeJSPContributor
	implements ProcessType, ProcessTypeJSPContributor {

	@Override
	public String getKey() {
		return TalendScheduledTaskExecutorService.KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, getKey());
	}

	@Override
	public Map<String, String> processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		DLFileEntry archiveFileEntry =
			_commerceDataIntegrationProcessFileEntryUploadActionHelper.upload(
				actionRequest, "srcArchive");

		Map<String, String> map = new HashMap<>();

		if (archiveFileEntry != null) {
			map.put(
				"fileEntryId",
				String.valueOf(archiveFileEntry.getFileEntryId()));
		}

		return map;
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		_jspRenderer.renderJSP(
			_servletContext, httpServletRequest, httpServletResponse,
			"/view.jsp");
	}

	@Reference
	private CommerceDataIntegrationProcessFileEntryUploadActionHelper
		_commerceDataIntegrationProcessFileEntryUploadActionHelper;

	@Reference
	private CommerceDataIntegrationProcessService
		_commerceDataIntegrationProcessService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.data.integration.talend)"
	)
	private ServletContext _servletContext;

}