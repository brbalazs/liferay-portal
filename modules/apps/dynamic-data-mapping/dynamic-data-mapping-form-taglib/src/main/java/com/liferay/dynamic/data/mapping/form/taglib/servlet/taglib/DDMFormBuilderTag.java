/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib;

import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsRequest;
import com.liferay.dynamic.data.mapping.form.builder.settings.DDMFormBuilderSettingsResponse;
import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.base.BaseDDMFormBuilderTag;
import com.liferay.dynamic.data.mapping.form.taglib.servlet.taglib.util.DDMFormTaglibUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rafael Praxedes
 */
public class DDMFormBuilderTag extends BaseDDMFormBuilderTag {

	public String getDDMFormBuilderContext(HttpServletRequest request) {
		return DDMFormTaglibUtil.getFormBuilderContext(
			GetterUtil.getLong(getDdmStructureId()),
			GetterUtil.getLong(getDdmStructureVersionId()), request);
	}

	protected DDMForm getDDMForm() {
		return DDMFormTaglibUtil.getDDMForm(
			GetterUtil.getLong(getDdmStructureId()),
			GetterUtil.getLong(getDdmStructureVersionId()));
	}

	protected DDMFormBuilderSettingsResponse getDDMFormBuilderSettings(
		HttpServletRequest request) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return DDMFormTaglibUtil.getDDMFormBuilderSettings(
			DDMFormBuilderSettingsRequest.with(
				themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
				getFieldSetClassNameId(), getDDMForm(),
				themeDisplay.getLocale()));
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		DDMFormBuilderSettingsResponse ddmFormBuilderSettingsResponse =
			getDDMFormBuilderSettings(request);

		setNamespacedAttribute(
			request, "dataProviderInstancesURL",
			ddmFormBuilderSettingsResponse.getDataProviderInstancesURL());
		setNamespacedAttribute(
			request, "dataProviderInstanceParameterSettingsURL",
			ddmFormBuilderSettingsResponse.
				getDataProviderInstanceParameterSettingsURL());
		setNamespacedAttribute(
			request, "evaluatorURL",
			ddmFormBuilderSettingsResponse.getFormContextProviderURL());
		setNamespacedAttribute(
			request, "fieldSets",
			ddmFormBuilderSettingsResponse.getFieldSets());
		setNamespacedAttribute(
			request, "fieldSetDefinitionURL",
			ddmFormBuilderSettingsResponse.getFieldSetDefinitionURL());
		setNamespacedAttribute(
			request, "fieldSettingsDDMFormContextURL",
			ddmFormBuilderSettingsResponse.getFieldSettingsDDMFormContextURL());

		setNamespacedAttribute(
			request, "formBuilderContext", getDDMFormBuilderContext(request));
		setNamespacedAttribute(
			request, "functionsMetadata",
			ddmFormBuilderSettingsResponse.getFunctionsMetadata());
		setNamespacedAttribute(
			request, "functionsURL",
			ddmFormBuilderSettingsResponse.getFunctionsURL());
		setNamespacedAttribute(
			request, "rolesURL", ddmFormBuilderSettingsResponse.getRolesURL());
		setNamespacedAttribute(
			request, "serializedDDMFormRules",
			ddmFormBuilderSettingsResponse.getSerializedDDMFormRules());
	}

}