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
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.service.CPDefinitionOptionRelService;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(
	property = "model.class.name=com.liferay.commerce.product.model.CPDefinitionOptionRel"
)
public class CPDefinitionOptionRelPermissionImpl
	implements HasPermission<Long> {

	@Override
	public <S> HasNestedAddingPermissionFunction<S> forAddingIn(
		Class<? extends Identifier<S>> identifierClass) {

		if (identifierClass.equals(CPDefinitionIdentifier.class)) {
			return (credentials, cpDefinitionId) -> {
				CPDefinition cpDefinition =
					_cpDefinitionService.fetchCPDefinition(
						(Long)cpDefinitionId);

				return _portletResourcePermission.contains(
					(PermissionChecker)credentials.get(),
					cpDefinition.getGroupId(),
					CPActionKeys.ADD_COMMERCE_PRODUCT_INSTANCE);
			};
		}

		return (credentials, s) -> false;
	}

	@Override
	public Boolean forDeleting(Credentials credentials, Long entryId)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, entryId,
			CPActionKeys.DELETE_COMMERCE_PRODUCT_INSTANCE);
	}

	@Override
	public Boolean forUpdating(Credentials credentials, Long entryId)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, entryId,
			CPActionKeys.UPDATE_COMMERCE_PRODUCT_INSTANCE);
	}

	private ThrowableTriFunction<Credentials, Long, String, Boolean>
		_forItemRoutesOperations() {

		return (credentials, cpDefinitionOptionRelId, actionId) -> {
			CPDefinitionOptionRel cpDefinitionOptionRel =
				_cpDefinitionOptionRelService.fetchCPDefinitionOptionRel(
					cpDefinitionOptionRelId);

			return _portletResourcePermission.contains(
				(PermissionChecker)credentials.get(),
				cpDefinitionOptionRel.getGroupId(), actionId);
		};
	}

	@Reference
	private CPDefinitionOptionRelService _cpDefinitionOptionRelService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference(target = "(resource.name=" + CPConstants.RESOURCE_NAME + ")")
	private PortletResourcePermission _portletResourcePermission;

}