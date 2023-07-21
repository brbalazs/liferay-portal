/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.builder.internal.context;

import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormBuilderContextFactory;
import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormBuilderContextRequest;
import com.liferay.dynamic.data.mapping.form.builder.context.DDMFormBuilderContextResponse;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.portal.kernel.json.JSONFactory;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(immediate = true, service = DDMFormBuilderContextFactory.class)
public class DDMFormBuilderContextFactoryImpl
	implements DDMFormBuilderContextFactory {

	@Override
	public DDMFormBuilderContextResponse create(
		DDMFormBuilderContextRequest ddmFormBuilderContextRequest) {

		Optional<DDMStructure> ddmStructureOptional = Optional.ofNullable(
			ddmFormBuilderContextRequest.getProperty("ddmStructure"));
		Optional<DDMStructureVersion> ddmStructureVersionOptional =
			Optional.ofNullable(
				ddmFormBuilderContextRequest.getProperty(
					"ddmStructureVersion"));
		HttpServletRequest request =
			ddmFormBuilderContextRequest.getHttpServletRequest();
		HttpServletResponse response =
			ddmFormBuilderContextRequest.getHttpServletResponse();
		Locale locale = ddmFormBuilderContextRequest.getLocale();
		boolean readOnly = ddmFormBuilderContextRequest.getReadOnly();

		DDMFormBuilderContextFactoryHelper ddmFormBuilderContextFactoryHelper =
			new DDMFormBuilderContextFactoryHelper(
				ddmStructureOptional, ddmStructureVersionOptional,
				_ddmFormFieldTypeServicesTracker,
				_ddmFormTemplateContextFactory, request, response, _jsonFactory,
				locale, readOnly);

		DDMFormBuilderContextResponse ddmFormBuilderContextResponse =
			new DDMFormBuilderContextResponse();

		ddmFormBuilderContextResponse.setContext(
			ddmFormBuilderContextFactoryHelper.create());

		return ddmFormBuilderContextResponse;
	}

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private DDMFormTemplateContextFactory _ddmFormTemplateContextFactory;

	@Reference
	private JSONFactory _jsonFactory;

}