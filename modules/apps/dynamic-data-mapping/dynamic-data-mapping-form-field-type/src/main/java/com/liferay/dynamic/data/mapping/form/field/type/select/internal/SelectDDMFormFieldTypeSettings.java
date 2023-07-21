/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.select.internal;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.annotations.DDMFormRule;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;

/**
 * @author Marcellus Tavares
 */
@DDMForm(
	rules = {
		@DDMFormRule(
			actions = "call('getDataProviderInstanceOutputParameters', concat('dataProviderInstanceId=', getValue('ddmDataProviderInstanceId')), 'ddmDataProviderInstanceOutput=outputParameterNames')",
			condition = "not(equals(getValue('ddmDataProviderInstanceId'), ''))"
		),
		@DDMFormRule(
			actions = {
				"setMultiple('predefinedValue', getValue('multiple'))",
				"setOptions('predefinedValue', getValue('options'))",
				"setRequired('ddmDataProviderInstanceId', equals(getValue('dataSourceType'), \"data-provider\"))",
				"setRequired('ddmDataProviderInstanceOutput', equals(getValue('dataSourceType'), \"data-provider\"))",
				"setRequired('options', equals(getValue('dataSourceType'), \"manual\"))",
				"setVisible('ddmDataProviderInstanceId', equals(getValue('dataSourceType'), \"data-provider\"))",
				"setVisible('ddmDataProviderInstanceOutput', equals(getValue('dataSourceType'), \"data-provider\"))",
				"setVisible('options', equals(getValue('dataSourceType'), \"manual\"))",
				"setVisible('predefinedValue', equals(getValue('dataSourceType'), \"manual\"))",
				"setVisible('validation', false)"
			},
			condition = "TRUE"
		)
	}
)
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "%basic",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"label", "tip", "required", "dataSourceType",
								"options", "ddmDataProviderInstanceId",
								"ddmDataProviderInstanceOutput"
							}
						)
					}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "%properties",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"predefinedValue", "visibilityExpression",
								"validation", "fieldNamespace", "indexType",
								"localizable", "readOnly", "dataType", "type",
								"name", "showLabel", "repeatable", "multiple"
							}
						)
					}
				)
			}
		)
	}
)
public interface SelectDDMFormFieldTypeSettings
	extends DefaultDDMFormFieldTypeSettings {

	@DDMFormField(
		label = "%create-list",
		optionLabels = {"%manually", "%from-data-provider", "%from-autofill"},
		optionValues = {"manual", "data-provider", "from-autofill"},
		predefinedValue = "[\"manual\"]", type = "radio"
	)
	public String dataSourceType();

	@DDMFormField(
		label = "%choose-a-data-provider",
		properties = {
			"dataSourceType=data-provider",
			"ddmDataProviderInstanceId=getDataProviderInstances"
		},
		type = "select"
	)
	public long ddmDataProviderInstanceId();

	@DDMFormField(
		label = "%choose-an-output-parameter",
		properties = "tooltip=%choose-an-output-parameter-for-a-data-provider-previously-created",
		type = "select"
	)
	public String ddmDataProviderInstanceOutput();

	@DDMFormField(
		label = "%allow-multiple-selections", properties = "showAsSwitcher=true"
	)
	public boolean multiple();

	@DDMFormField(
		dataType = "ddm-options", label = "%options",
		properties = "showLabel=false", type = "options"
	)
	public DDMFormFieldOptions options();

	@DDMFormField(
		label = "%predefined-value",
		properties = {
			"placeholder=%enter-a-default-value",
			"tooltip=%enter-a-default-value-that-is-submitted-if-no-other-value-is-entered"
		},
		type = "select"
	)
	@Override
	public LocalizedValue predefinedValue();

}