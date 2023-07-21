/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.layoutconfiguration.util;

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
public class RuntimePageUtil {

	public static StringBundler getProcessedTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource)
		throws Exception {

		return getRuntimePage().getProcessedTemplate(
			request, response, portletId, templateResource);
	}

	public static RuntimePage getRuntimePage() {
		return _runtimePage;
	}

	public static void processCustomizationSettings(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource)
		throws Exception {

		getRuntimePage().processCustomizationSettings(
			request, response, templateResource);
	}

	public static void processCustomizationSettings(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource, String langType)
		throws Exception {

		getRuntimePage().processCustomizationSettings(
			request, response, templateResource, langType);
	}

	public static void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource)
		throws Exception {

		getRuntimePage().processTemplate(
			request, response, portletId, templateResource);
	}

	public static void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			String portletId, TemplateResource templateResource,
			String langType)
		throws Exception {

		getRuntimePage().processTemplate(
			request, response, portletId, templateResource, langType);
	}

	public static void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource)
		throws Exception {

		getRuntimePage().processTemplate(request, response, templateResource);
	}

	public static void processTemplate(
			HttpServletRequest request, HttpServletResponse response,
			TemplateResource templateResource, String langType)
		throws Exception {

		getRuntimePage().processTemplate(
			request, response, templateResource, langType);
	}

	public static String processXML(
			HttpServletRequest request, HttpServletResponse response,
			String content)
		throws Exception {

		return getRuntimePage().processXML(request, response, content);
	}

	public static String processXML(
			HttpServletRequest request, String content,
			RuntimeLogic runtimeLogic)
		throws Exception {

		return getRuntimePage().processXML(request, content, runtimeLogic);
	}

	public void setRuntimePage(RuntimePage runtimePage) {
		_runtimePage = runtimePage;
	}

	private static RuntimePage _runtimePage;

}