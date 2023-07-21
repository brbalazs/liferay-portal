/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.soy.internal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.Map;
import java.util.Set;

/**
 * @author Shuyang Zhou
 */
public class SoyJavaScriptRendererUtil {

	public static String getJavaScript(
		Map<String, Object> context, String id, Set<String> modules) {

		return getJavaScript(context, id, modules, true);
	}

	public static String getJavaScript(
		Map<String, Object> context, String id, Set<String> modules,
		boolean wrapper) {

		JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();

		String contextString = jsonSerializer.serializeDeep(context);
		String modulesString = jsonSerializer.serialize(modules);
		String wrapperString = jsonSerializer.serialize(wrapper);

		return StringUtil.replace(
			_JAVA_SCRIPT_TPL,
			new String[] {"$CONTEXT", "$ID", "$MODULES", "$WRAPPER"},
			new String[] {contextString, id, modulesString, wrapperString});
	}

	private static final String _JAVA_SCRIPT_TPL;

	private static final Log _log = LogFactoryUtil.getLog(
		SoyJavaScriptRendererUtil.class);

	static {
		InputStream inputStream =
			SoyJavaScriptRendererUtil.class.getResourceAsStream(
				"dependencies/bootstrap.js.tpl");

		String js = StringPool.BLANK;

		try {
			js = StringUtil.read(inputStream);
		}
		catch (Exception e) {
			_log.error("Unable to read template", e);
		}

		_JAVA_SCRIPT_TPL = js;
	}

}