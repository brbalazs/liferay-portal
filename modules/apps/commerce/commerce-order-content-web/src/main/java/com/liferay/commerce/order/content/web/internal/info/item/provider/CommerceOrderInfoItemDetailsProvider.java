/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.order.content.web.internal.info.item.provider;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.GroupKeyInfoItemIdentifier;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.provider.BaseInfoItemDetailsProvider;
import com.liferay.info.item.provider.InfoItemDetailsProvider;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/**
 * @author Danny Situ
 */
@Component(
	property = {
		Constants.SERVICE_RANKING + ":Integer=10",
		"item.class.name=com.liferay.commerce.model.CommerceOrder"
	},
	service = InfoItemDetailsProvider.class
)
public class CommerceOrderInfoItemDetailsProvider
	extends BaseInfoItemDetailsProvider<CommerceOrder> {

	@Override
	public InfoItemClassDetails getInfoItemClassDetails() {
		return new InfoItemClassDetails(CommerceOrder.class.getName());
	}

	@Override
	protected InfoItemIdentifierFactory<CommerceOrder>
		getInfoItemIdentifierFactory() {

		return new InfoItemIdentifierFactory<>() {

			@Override
			public ClassPKInfoItemIdentifier createClassPKInfoItemIdentifier(
				CommerceOrder commerceOrder) {

				return new ClassPKInfoItemIdentifier(
					commerceOrder.getCommerceOrderId());
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
				long groupId, CommerceOrder commerceOrder) {

				return new GroupKeyInfoItemIdentifier(
					commerceOrder.getGroupId(),
					String.valueOf(commerceOrder.getCommerceOrderId()));
			}

		};
	}

}