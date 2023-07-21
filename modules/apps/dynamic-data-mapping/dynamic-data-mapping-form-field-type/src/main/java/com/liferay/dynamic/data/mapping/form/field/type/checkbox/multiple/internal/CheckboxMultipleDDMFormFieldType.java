/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type.checkbox.multiple.internal;

import com.liferay.dynamic.data.mapping.form.field.type.BaseDDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeSettings;

import org.osgi.service.component.annotations.Component;

/**
 * @author Dylan Rebelak
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.field.type.data.domain=list",
		"ddm.form.field.type.description=checkbox-multiple-field-type-description",
		"ddm.form.field.type.display.order:Integer=7",
		"ddm.form.field.type.group=basic",
		"ddm.form.field.type.icon=select-from-list",
		"ddm.form.field.type.js.class.name=Liferay.DDM.Field.CheckboxMultiple",
		"ddm.form.field.type.js.module=liferay-ddm-form-field-checkbox-multiple",
		"ddm.form.field.type.label=checkbox-multiple-field-type-label",
		"ddm.form.field.type.name=checkbox_multiple"
	},
	service = DDMFormFieldType.class
)
public class CheckboxMultipleDDMFormFieldType extends BaseDDMFormFieldType {

	@Override
	public Class<? extends DDMFormFieldTypeSettings>
		getDDMFormFieldTypeSettings() {

		return CheckboxMultipleDDMFormFieldTypeSettings.class;
	}

	@Override
	public String getName() {
		return "checkbox_multiple";
	}

}