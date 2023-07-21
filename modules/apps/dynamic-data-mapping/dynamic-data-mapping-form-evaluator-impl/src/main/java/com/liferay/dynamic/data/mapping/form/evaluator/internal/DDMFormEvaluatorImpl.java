/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderInvoker;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationException;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluator;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluatorContext;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pablo Carvalho
 */
@Component(immediate = true, service = DDMFormEvaluator.class)
public class DDMFormEvaluatorImpl implements DDMFormEvaluator {

	@Override
	public DDMFormEvaluationResult evaluate(
			DDMFormEvaluatorContext ddmFormEvaluatorContext)
		throws DDMFormEvaluationException {

		try {
			DDMFormEvaluatorHelper ddmFormRuleEvaluatorHelper =
				new DDMFormEvaluatorHelper(
					_ddmDataProviderInvoker, _ddmExpressionFactory,
					ddmFormEvaluatorContext, _ddmFormFieldTypeServicesTracker,
					_jsonFactory, _roleLocalService, _userGroupRoleLocalService,
					_userLocalService);

			return ddmFormRuleEvaluatorHelper.evaluate();
		}
		catch (PortalException pe) {
			throw new DDMFormEvaluationException(pe);
		}
	}

	@Reference
	private DDMDataProviderInvoker _ddmDataProviderInvoker;

	@Reference
	private DDMExpressionFactory _ddmExpressionFactory;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserGroupRoleLocalService _userGroupRoleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}