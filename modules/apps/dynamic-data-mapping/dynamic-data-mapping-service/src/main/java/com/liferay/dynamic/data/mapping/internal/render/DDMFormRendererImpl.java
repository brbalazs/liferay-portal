/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.internal.render;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRendererRegistryUtil;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.dynamic.data.mapping.render.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.DDMFieldsCounter;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marcellus Tavares
 */
public class DDMFormRendererImpl implements DDMFormRenderer {

	@Override
	public String render(
			DDMForm ddmForm,
			DDMFormFieldRenderingContext ddmFormFieldRenderingContext)
		throws PortalException {

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		StringBundler sb = new StringBundler(ddmFormFields.size());

		ddmFormFieldRenderingContext.setProperty(
			"fieldNamespaceSet", new HashSet<String>());

		for (DDMFormField ddmFormField : ddmFormFields) {
			if (isDDMFormFieldSkippable(
					ddmFormField, ddmFormFieldRenderingContext)) {

				continue;
			}

			DDMFormFieldRenderer ddmFormFieldRenderer =
				DDMFormFieldRendererRegistryUtil.getDDMFormFieldRenderer(
					ddmFormField.getType());

			sb.append(
				ddmFormFieldRenderer.render(
					ddmFormField, ddmFormFieldRenderingContext));
		}

		clearDDMFieldsCounter(ddmFormFieldRenderingContext);

		return sb.toString();
	}

	protected void clearDDMFieldsCounter(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		HttpServletRequest request =
			ddmFormFieldRenderingContext.getHttpServletRequest();

		String fieldsCounterKey =
			ddmFormFieldRenderingContext.getPortletNamespace() +
				ddmFormFieldRenderingContext.getNamespace() + "fieldsCount";

		DDMFieldsCounter ddmFieldsCounter =
			(DDMFieldsCounter)request.getAttribute(fieldsCounterKey);

		if (ddmFieldsCounter != null) {
			ddmFieldsCounter.clear();
		}
	}

	protected boolean isDDMFormFieldSkippable(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		if (!ddmFormFieldRenderingContext.isReadOnly() ||
			ddmFormFieldRenderingContext.isShowEmptyFieldLabel()) {

			return false;
		}

		Fields fields = ddmFormFieldRenderingContext.getFields();

		if (fields.contains(ddmFormField.getName())) {
			return false;
		}

		for (DDMFormField nestedDDMFormField :
				ddmFormField.getNestedDDMFormFields()) {

			if (!isDDMFormFieldSkippable(
					nestedDDMFormField, ddmFormFieldRenderingContext)) {

				return false;
			}
		}

		return true;
	}

}