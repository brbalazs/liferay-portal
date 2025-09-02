/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.order.content.web.internal.info.item.provider;

import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.GroupKeyInfoItemIdentifier;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.provider.BaseInfoItemDetailsProvider;
import com.liferay.info.item.provider.InfoItemDetailsProvider;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		Constants.SERVICE_RANKING + ":Integer=10",
		"item.class.name=com.liferay.commerce.model.CommerceOrderItem"
	},
	service = InfoItemDetailsProvider.class
)
public class CommerceOrderItemInfoItemDetailsProvider
	extends BaseInfoItemDetailsProvider<CommerceOrderItem> {

	@Override
	public InfoItemClassDetails getInfoItemClassDetails() {
		return new InfoItemClassDetails(CommerceOrderItem.class.getName());
	}

	@Override
	protected InfoItemIdentifierFactory<CommerceOrderItem>
		getInfoItemIdentifierFactory() {

		return new InfoItemIdentifierFactory<>() {

			@Override
			public ClassPKInfoItemIdentifier createClassPKInfoItemIdentifier(
				CommerceOrderItem commerceOrderItem) {

				return new ClassPKInfoItemIdentifier(
					commerceOrderItem.getCommerceOrderId());
			}

			@Override
			public ERCInfoItemIdentifier createERCInfoItemIdentifier(
				String externalReferenceCode,
				String scopeExternalReferenceCode) {

				return new ERCInfoItemIdentifier(
					externalReferenceCode, scopeExternalReferenceCode);
			}

			@Override
			public GroupKeyInfoItemIdentifier createGroupKeyInfoItemIdentifier(
				long groupId, CommerceOrderItem commerceOrderItem) {

				return new GroupKeyInfoItemIdentifier(
					commerceOrderItem.getGroupId(),
					String.valueOf(commerceOrderItem.getCommerceOrderItemId()));
			}

		};
	}

}