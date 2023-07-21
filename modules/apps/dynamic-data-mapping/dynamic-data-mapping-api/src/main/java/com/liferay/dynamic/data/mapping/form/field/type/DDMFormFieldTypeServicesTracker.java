/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type;

import aQute.bnd.annotation.ProviderType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marcellus Tavares
 */
@ProviderType
public interface DDMFormFieldTypeServicesTracker {

	public DDMFormFieldRenderer getDDMFormFieldRenderer(String name);

	public DDMFormFieldTemplateContextContributor
		getDDMFormFieldTemplateContextContributor(String name);

	public DDMFormFieldType getDDMFormFieldType(String name);

	public Set<String> getDDMFormFieldTypeNames();

	public Map<String, Object> getDDMFormFieldTypeProperties(String name);

	public List<DDMFormFieldType> getDDMFormFieldTypes();

	public List<DDMFormFieldType> getDDMFormFieldTypesByDataDomain(
		String dataDomain);

	public <T> DDMFormFieldValueAccessor<T> getDDMFormFieldValueAccessor(
		String name);

	public DDMFormFieldValueLocalizer getDDMFormFieldValueLocalizer(
		String name);

	public DDMFormFieldValueRenderer getDDMFormFieldValueRenderer(String name);

}