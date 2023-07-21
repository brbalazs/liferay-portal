/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.renderer.internal.servlet.taglib.helper;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.internal.servlet.taglib.DDMFormFieldTypesDynamicInclude;
import com.liferay.dynamic.data.mapping.io.DDMFormFieldTypesJSONSerializer;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
public abstract class BaseDDMFormFieldTypesDynamicInclude
	extends BaseDynamicInclude {

	protected void include(HttpServletResponse response) throws IOException {
		ScriptData scriptData = new ScriptData();

		List<DDMFormFieldType> ddmFormFieldTypes =
			ddmFormFieldTypeServicesTracker.getDDMFormFieldTypes();

		try {
			scriptData.append(
				null,
				StringUtil.replaceToStringBundler(
					_TMPL_CONTENT, StringPool.POUND, StringPool.POUND,
					Collections.singletonMap(
						"fieldTypes",
						ddmFormFieldTypesJSONSerializer.serialize(
							ddmFormFieldTypes))),
				_MODULES, ScriptData.ModulesType.AUI);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		scriptData.writeTo(response.getWriter());
	}

	@Reference
	protected DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker;

	@Reference
	protected DDMFormFieldTypesJSONSerializer ddmFormFieldTypesJSONSerializer;

	private static final String _MODULES =
		"liferay-ddm-form-renderer-types,liferay-ddm-soy-template-util";

	private static final String _TMPL_CONTENT = StringUtil.read(
		DDMFormFieldTypesDynamicInclude.class,
		"/META-INF/resources/dynamic_include/field_types.tmpl");

	private static final Log _log = LogFactoryUtil.getLog(
		BaseDDMFormFieldTypesDynamicInclude.class);

}