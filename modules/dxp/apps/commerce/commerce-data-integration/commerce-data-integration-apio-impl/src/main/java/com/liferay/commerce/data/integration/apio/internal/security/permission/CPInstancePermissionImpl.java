/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.apio.internal.security.permission;

import com.liferay.apio.architect.alias.routes.permission.HasNestedAddingPermissionFunction;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.commerce.data.integration.apio.identifiers.CPDefinitionIdentifier;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "model.class.name=com.liferay.commerce.product.model.CPInstance"
)
public class CPInstancePermissionImpl implements HasPermission<Long> {

	@Override
	public <S> HasNestedAddingPermissionFunction<S> forAddingIn(
		Class<? extends Identifier<S>> identifierClass) {

		if (identifierClass.equals(CPDefinitionIdentifier.class)) {
			return (credentials, cpDefinitionId) -> {
				CPDefinition cpDefinition =
					_cpDefinitionService.fetchCPDefinition(
						(Long)cpDefinitionId);

				if (cpDefinition == null) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"No CPDefinition exists with primary key " +
								cpDefinitionId);
					}

					return false;
				}

				return _cpDefinitionModelResourcePermission.contains(
					(PermissionChecker)credentials.get(), cpDefinition,
					ActionKeys.UPDATE);
			};
		}

		return (credentials, s) -> false;
	}

	@Override
	public Boolean forDeleting(Credentials credentials, Long entryId)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, entryId, ActionKeys.UPDATE);
	}

	@Override
	public Boolean forUpdating(Credentials credentials, Long entryId)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, entryId, ActionKeys.UPDATE);
	}

	private ThrowableTriFunction<Credentials, Long, String, Boolean>
		_forItemRoutesOperations() {

		return (credentials, cpInstanceId, actionId) -> {
			CPInstance cpInstance = _cpInstanceService.fetchCPInstance(
				cpInstanceId);

			if (cpInstance == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"No CPInstance exists with primary key " +
							cpInstanceId);
				}

				return false;
			}

			return _cpDefinitionModelResourcePermission.contains(
				(PermissionChecker)credentials.get(),
				cpInstance.getCPDefinitionId(), actionId);
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CPInstancePermissionImpl.class);

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CPDefinition)"
	)
	private ModelResourcePermission<CPDefinition>
		_cpDefinitionModelResourcePermission;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPInstanceService _cpInstanceService;

}