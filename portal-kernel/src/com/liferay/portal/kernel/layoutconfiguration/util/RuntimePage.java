/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.layoutconfiguration.util;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.layoutconfiguration.util.xml.RuntimeLogic;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.StringBundler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
@ProviderType
public interface RuntimePage {

	public StringBundler getProcessedTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource)
		throws Exception;

	public void processCustomizationSettings(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource)
		throws Exception;

	public void processCustomizationSettings(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource, String langType)
		throws Exception;

	public void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource)
		throws Exception;

	public void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource,
			String langType)
		throws Exception;

	public void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource)
		throws Exception;

	public void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource, String langType)
		throws Exception;

	public String processXML(
			HttpServletRequest request, HttpServletResponse response,
			String content)
		throws Exception;

	public String processXML(
			HttpServletRequest request, String content,
			RuntimeLogic runtimeLogic)
		throws Exception;

}