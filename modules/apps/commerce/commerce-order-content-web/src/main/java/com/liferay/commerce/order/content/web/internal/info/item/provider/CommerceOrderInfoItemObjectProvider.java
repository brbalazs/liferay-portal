/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.order.content.web.internal.info.item.provider;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.info.exception.NoSuchInfoItemException;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemIdentifier;
import com.liferay.info.item.provider.BaseInfoItemObjectProvider;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Danny Situ
 */
@Component(
	property = {
		"info.item.identifier=com.liferay.info.item.ClassPKInfoItemIdentifier",
		"info.item.identifier=com.liferay.info.item.ERCInfoItemIdentifier",
		"item.class.name=com.liferay.commerce.model.CommerceOrder",
		"service.ranking:Integer=100"
	},
	service = InfoItemObjectProvider.class
)
public class CommerceOrderInfoItemObjectProvider
	extends BaseInfoItemObjectProvider<CommerceOrder> {

	@Override
	protected CommerceOrder doGetInfoItem(
			long groupId, InfoItemIdentifier infoItemIdentifier)
		throws NoSuchInfoItemException {

		if (!(infoItemIdentifier instanceof ClassPKInfoItemIdentifier) &&
			!(infoItemIdentifier instanceof ERCInfoItemIdentifier)) {

			throw new NoSuchInfoItemException(
				"Unsupported info item identifier " + infoItemIdentifier);
		}

		if (infoItemIdentifier instanceof ClassPKInfoItemIdentifier) {
			ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
				(ClassPKInfoItemIdentifier)infoItemIdentifier;

			try {
				return _commerceOrderLocalService.fetchCommerceOrder(
					classPKInfoItemIdentifier.getClassPK());
			}
			catch (PortalException portalException) {
				throw new NoSuchInfoItemException(
					"Unable to get commerce order " + infoItemIdentifier,
					portalException);
			}
		}

		ERCInfoItemIdentifier ercInfoItemIdentifier =
			(ERCInfoItemIdentifier)infoItemIdentifier;

		try {
			Group group = _groupLocalService.getGroup(groupId);

			return _commerceOrderLocalService.
				fetchCommerceOrderByExternalReferenceCode(
					ercInfoItemIdentifier.getExternalReferenceCode(),
					group.getCompanyId());
		}
		catch (PortalException portalException) {
			throw new NoSuchInfoItemException(
				"Unable to get commerce order " + infoItemIdentifier,
				portalException);
		}
	}

	@Reference
	private CommerceOrderService _commerceOrderLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}