/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.runtime.form.internal;

import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalService;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskForm;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.runtime.form.FormDefinitionRetriever;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = FormDefinitionRetriever.class)
public class DDMFormDefinitionRetriever implements FormDefinitionRetriever {

	@Override
	public String getFormDefinition(
			KaleoTaskForm kaleoTaskForm,
			KaleoTaskInstanceToken kaleoTaskInstanceToken)
		throws PortalException {

		DDLRecordSet ddlRecordSet = _ddlRecordSetLocalService.getRecordSet(
			kaleoTaskForm.getFormUuid(), kaleoTaskInstanceToken.getGroupId());

		DDMStructure ddmStructure = ddlRecordSet.getDDMStructure();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		return _ddmFormJSONSerializer.serialize(ddmForm);
	}

	@Reference
	private DDLRecordSetLocalService _ddlRecordSetLocalService;

	@Reference
	private DDMFormJSONSerializer _ddmFormJSONSerializer;

}