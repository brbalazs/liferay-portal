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
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.commerce.data.integration.apio.identifiers.ClassPKExternalReferenceCode;
import com.liferay.commerce.data.integration.apio.identifiers.CommercePriceListIdentifier;
import com.liferay.commerce.data.integration.apio.internal.util.CommercePriceEntryHelper;
import com.liferay.commerce.data.integration.apio.internal.util.CommercePriceListHelper;
import com.liferay.commerce.price.list.constants.CommercePriceListActionKeys;
import com.liferay.commerce.price.list.constants.CommercePriceListConstants;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceEntryService;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Zoltán Takács
 */
@Component(
	property = "model.class.name=com.liferay.commerce.price.list.model.CommercePriceEntry"
)
public class CommercePriceEntryHasPermissionImpl
	implements HasPermission<ClassPKExternalReferenceCode> {

	@Override
	public <S> HasNestedAddingPermissionFunction<S> forAddingIn(
		Class<? extends Identifier<S>> identifierClass) {

		if (identifierClass.equals(CommercePriceListIdentifier.class)) {
			return (
				credentials,
				priceListClassPKExternalReferenceCode) -> {
					CommercePriceList commercePriceList =
						_commercePriceListHelper.
							getCommercePriceListByClassPKExternalReferenceCode(
								(ClassPKExternalReferenceCode)
									priceListClassPKExternalReferenceCode);

					return _portletResourcePermission.contains(
						(PermissionChecker)credentials.get(),
						commercePriceList.getGroupId(),
						CommercePriceListActionKeys.
							MANAGE_COMMERCE_PRICE_LISTS);
				};
		}

		return (credentials, s) -> false;
	}

	@Override
	public Boolean forDeleting(
			Credentials credentials,
			ClassPKExternalReferenceCode classPKExternalReferenceCode)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, classPKExternalReferenceCode);
	}

	@Override
	public Boolean forUpdating(
			Credentials credentials,
			ClassPKExternalReferenceCode classPKExternalReferenceCode)
		throws Exception {

		return _forItemRoutesOperations().apply(
			credentials, classPKExternalReferenceCode);
	}

	private ThrowableBiFunction
		<Credentials, ClassPKExternalReferenceCode, Boolean>
			_forItemRoutesOperations() {

		return (credentials, classPKExternalReferenceCode) -> {
			CommercePriceEntry commercePriceEntry =
				_commercePriceEntryHelper.
					getCommercePriceEntryByClassPKExternalReferenceCode(
						classPKExternalReferenceCode);

			return _portletResourcePermission.contains(
				(PermissionChecker)credentials.get(),
				commercePriceEntry.getGroupId(),
				CommercePriceListActionKeys.MANAGE_COMMERCE_PRICE_LISTS);
		};
	}

	@Reference
	private CommercePriceEntryHelper _commercePriceEntryHelper;

	@Reference
	private CommercePriceEntryService _commercePriceEntryService;

	@Reference
	private CommercePriceListHelper _commercePriceListHelper;

	@Reference
	private CommercePriceListService _commercePriceListService;

	@Reference(
		target = "(resource.name=" + CommercePriceListConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}