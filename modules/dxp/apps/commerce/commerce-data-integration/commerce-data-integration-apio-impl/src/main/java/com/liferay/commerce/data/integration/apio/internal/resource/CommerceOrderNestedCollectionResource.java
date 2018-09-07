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

package com.liferay.commerce.data.integration.apio.internal.resource;

import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.NestedCollectionResource;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.exception.NoSuchCurrencyException;
import com.liferay.commerce.data.integration.apio.identifiers.ClassPKExternalReferenceCode;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceAccountIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceAddressIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommerceOrderIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.CommercePaymentMethodIdentifier;
import com.liferay.commerce.data.integration.apio.identifiers.SiteGroupIdOrganizationId;
import com.liferay.commerce.data.integration.apio.identifiers.WebSiteIdentifierWithOrganization;
import com.liferay.commerce.data.integration.apio.internal.form.CommerceOrderUpdaterForm;
import com.liferay.commerce.data.integration.apio.internal.form.CommerceOrderUpserterForm;
import com.liferay.commerce.data.integration.apio.internal.util.CommerceOrderHelper;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.organization.service.CommerceOrganizationService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.person.apio.architect.identifier.PersonIdentifier;
import com.liferay.portal.apio.permission.HasPermission;
import com.liferay.portal.apio.user.CurrentUser;
import com.liferay.portal.kernel.exception.NoSuchOrganizationException;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.site.apio.architect.identifier.WebSiteIdentifier;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.NotFoundException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodrigo Guedes de Souza
 */
@Component(immediate = true)
public class CommerceOrderNestedCollectionResource
	implements NestedCollectionResource
		<CommerceOrder, ClassPKExternalReferenceCode, CommerceOrderIdentifier,
		SiteGroupIdOrganizationId, WebSiteIdentifierWithOrganization> {

	@Override
	public NestedCollectionRoutes
		<CommerceOrder, ClassPKExternalReferenceCode, SiteGroupIdOrganizationId>
			collectionRoutes(
				NestedCollectionRoutes.Builder
					<CommerceOrder, ClassPKExternalReferenceCode,
					SiteGroupIdOrganizationId>
						builder) {

		return builder.addGetter(
			this::_getPageItems
		).addCreator(
			this::_createCommerceOrder, CurrentUser.class,
			_hasPermission.forAddingIn(WebSiteIdentifierWithOrganization.class),
			CommerceOrderUpserterForm::buildForm
		).build();
	}

	@Override
	public String getName() {
		return "commerce-order";
	}

	@Override
	public ItemRoutes<CommerceOrder, ClassPKExternalReferenceCode> itemRoutes(
		ItemRoutes.Builder<CommerceOrder, ClassPKExternalReferenceCode>
			builder) {

		return builder.addGetter(
			this::_getCommerceOrder
		).addUpdater(
			this::_updateCommerceOrder, _hasPermission::forUpdating,
			CommerceOrderUpdaterForm::buildForm
		).build();
	}

	@Override
	public Representor<CommerceOrder> representor(
		Representor.Builder<CommerceOrder, ClassPKExternalReferenceCode>
			builder) {

		return builder.types(
			"CommerceOrder"
		).identifier(
			_commerceOrderHelper::commerceOrderToClassPKExternalReferenceCode
		).addBidirectionalModel(
			"commerceAccount", "commerceOrders",
			CommerceAccountIdentifier.class,
			CommerceOrder::getOrderOrganizationId
		).addBidirectionalModel(
			"webSite", "commerceOrders", WebSiteIdentifier.class,
			CommerceOrder::getGroupId
		).addLinkedModel(
			"commerceAccount", CommerceAccountIdentifier.class,
			CommerceOrder::getOrderOrganizationId
		).addLinkedModel(
			"commercePaymentMethod", CommercePaymentMethodIdentifier.class,
			CommerceOrder::getCommercePaymentMethodId
		).addNumber(
			"id", CommerceOrder::getCommerceOrderId
		).addString(
			"purchaseOrderNumber", CommerceOrder::getPurchaseOrderNumber
		).addNumber(
			"shippingPrice", CommerceOrder::getShippingAmount
		).addNumber(
			"total", CommerceOrder::getTotal
		).addLocalizedStringByLocale(
			"orderStatus", this::_getOrderStatus
		).addString(
			"paymentStatus",
			order -> CommerceOrderConstants.getPaymentStatusLabel(
				order.getPaymentStatus())
		).addDate(
			"dateCreated", CommerceOrder::getCreateDate
		).addDate(
			"dateModified", CommerceOrder::getModifiedDate
		).addLinkedModel(
			"author", PersonIdentifier.class, CommerceOrder::getUserId
		).addString(
			"authorExternalReferenceCode", this::_getUserExternalReferenceCode
		).addLinkedModel(
			"shippingAddress", CommerceAddressIdentifier.class,
			CommerceOrder::getShippingAddressId
		).addLinkedModel(
			"billingAddress", CommerceAddressIdentifier.class,
			CommerceOrder::getBillingAddressId
		).addString(
			"accountExternalReferenceCode",
			this::_getAccountExternalReferenceCode
		).addString(
			"accountName", this::_getAccountName
		).addString(
			"billingStreet", CommerceOrderHelper::getBillingAddressStreet
		).addString(
			"billingCity", CommerceOrderHelper::getBillingAddressCity
		).addString(
			"billingState", CommerceOrderHelper::getBillingAddressState
		).addString(
			"billingZip", CommerceOrderHelper::getBillingAddressZip
		).addLocalizedStringByLocale(
			"billingCountry", CommerceOrderHelper::getBillingAddressCountry
		).addString(
			"shippingStreet", CommerceOrderHelper::getShippingAddressStreet
		).addString(
			"shippingCity", CommerceOrderHelper::getShippingAddressCity
		).addString(
			"shippingState", CommerceOrderHelper::getShippingAddressState
		).addString(
			"shippingZip", CommerceOrderHelper::getShippingAddressZip
		).addLocalizedStringByLocale(
			"shippingCountry", CommerceOrderHelper::getShippingAddressCountry
		).build();
	}

	private CommerceOrder _createCommerceOrder(
			SiteGroupIdOrganizationId siteGroupIdOrganizationId,
			CommerceOrderUpserterForm commerceOrderUpserterForm,
			User currentUser)
		throws PortalException {

		try {
			return _commerceOrderHelper.createCommerceOrder(
				siteGroupIdOrganizationId.getSiteGroupId(),
				commerceOrderUpserterForm.getOrderOrganizationId(),
				commerceOrderUpserterForm.getOrderUserId(),
				commerceOrderUpserterForm.getCurrency(),
				commerceOrderUpserterForm.getShippingAddressId(),
				commerceOrderUpserterForm.getPurchaseOrderNumber(),
				currentUser);
		}
		catch (NoSuchCurrencyException nsce) {
			throw new NotFoundException(
				String.format(
					"Unable to find currency with code: %s. Currency code " +
						"should be expressed with 3-letter ISO 4217 format",
					commerceOrderUpserterForm.getCurrency()),
				nsce);
		}
		catch (NoSuchOrganizationException nsoe) {
			throw new NotFoundException(
				"Unable to find organization with primary key " +
					commerceOrderUpserterForm.getOrderOrganizationId(),
				nsoe);
		}
		catch (NoSuchUserException nsue) {
			throw new NotFoundException(
				"Unable to find user with primary key " +
					commerceOrderUpserterForm.getOrderUserId(),
				nsue);
		}
	}

	private String _getAccountExternalReferenceCode(
		CommerceOrder commerceOrder) {

		if (commerceOrder != null) {
			try {
				Organization organization =
					commerceOrder.getOrderOrganization();

				return organization.getExternalReferenceCode();
			}
			catch (PortalException pe) {
				_log.error(
					"Unable to find Organization with ID " +
						commerceOrder.getOrderOrganizationId(),
					pe);
			}
		}

		return null;
	}

	private String _getAccountName(CommerceOrder commerceOrder) {
		if (commerceOrder != null) {
			try {
				Organization organization =
					commerceOrder.getOrderOrganization();

				return organization.getName();
			}
			catch (PortalException pe) {
				_log.error(
					"Unable to find Organization with ID " +
						commerceOrder.getOrderOrganizationId(),
					pe);
			}
		}

		return null;
	}

	private CommerceOrder _getCommerceOrder(
			ClassPKExternalReferenceCode classPKExternalReferenceCode)
		throws PortalException {

		return _commerceOrderHelper.
			getCommerceOrderByClassPKExternalReferenceCode(
				classPKExternalReferenceCode);
	}

	private String _getOrderStatus(CommerceOrder commerceOrder, Locale locale) {
		String key = CommerceOrderConstants.getOrderStatusLabel(
			commerceOrder.getOrderStatus());

		return LanguageUtil.get(locale, key);
	}

	private PageItems<CommerceOrder> _getPageItems(
			Pagination pagination,
			SiteGroupIdOrganizationId siteGroupIdOrganizationId)
		throws PortalException {

		long groupId = 0L;

		if (siteGroupIdOrganizationId.getOrganizationId() > 0) {
			Organization organization =
				_commerceOrganizationService.getOrganization(
					siteGroupIdOrganizationId.getOrganizationId());

			groupId = organization.getGroupId();
		}
		else {
			groupId = siteGroupIdOrganizationId.getSiteGroupId();
		}

		List<CommerceOrder> commerceOrders =
			_commerceOrderService.getCommerceOrders(
				groupId, pagination.getStartPosition(),
				pagination.getEndPosition(), null);

		int total = _commerceOrderService.getCommerceOrdersCount(groupId);

		return new PageItems<>(commerceOrders, total);
	}

	private String _getUserExternalReferenceCode(CommerceOrder commerceOrder) {
		if (commerceOrder != null) {
			try {
				User user = commerceOrder.getOrderUser();

				return user.getExternalReferenceCode();
			}
			catch (PortalException pe) {
				_log.error(
					"Unable to find User with ID " +
						commerceOrder.getOrderUserId(),
					pe);
			}
		}

		return null;
	}

	private CommerceOrder _updateCommerceOrder(
			ClassPKExternalReferenceCode
				commerceOrderClassPKExternalReferenceCode,
			CommerceOrderUpdaterForm commerceOrderUpdaterForm)
		throws PortalException {

		return _commerceOrderHelper.updateCommerceOrder(
			commerceOrderClassPKExternalReferenceCode,
			commerceOrderUpdaterForm.getOrderStatus(),
			commerceOrderUpdaterForm.getPaymentStatus(),
			commerceOrderUpdaterForm.getExternalReferenceCode());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderNestedCollectionResource.class);

	@Reference
	private CommerceOrderHelper _commerceOrderHelper;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceOrganizationService _commerceOrganizationService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.model.CommerceOrder)"
	)
	private HasPermission<ClassPKExternalReferenceCode> _hasPermission;

}