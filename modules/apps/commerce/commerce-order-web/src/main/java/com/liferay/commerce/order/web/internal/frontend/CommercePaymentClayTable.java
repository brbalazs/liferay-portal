/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.order.web.internal.frontend;

import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.frontend.ClayTable;
import com.liferay.commerce.frontend.ClayTableSchema;
import com.liferay.commerce.frontend.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.ClayTableSchemaField;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.StatusField;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderPayment;
import com.liferay.commerce.order.web.internal.model.Payment;
import com.liferay.commerce.service.CommerceOrderPaymentLocalService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"commerce.data.provider.key=" + CommercePaymentClayTable.NAME,
		"commerce.table.name=" + CommercePaymentClayTable.NAME
	},
	service = {ClayTable.class, CommerceDataSetDataProvider.class}
)
public class CommercePaymentClayTable
	implements ClayTable, CommerceDataSetDataProvider<Payment> {

	public static final String NAME = "commerceOrderPayments";

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		return _commerceOrderPaymentLocalService.getCommerceOrderPaymentsCount(
			commerceOrderId);
	}

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		ClayTableSchemaField typeField = clayTableSchemaBuilder.addField(
			"type", "type");

		typeField.setContentRenderer("label");

		clayTableSchemaBuilder.addField("amount", "amount");

		clayTableSchemaBuilder.addField("createDate", "create-date");

		clayTableSchemaBuilder.addField("content", "content");

		return clayTableSchemaBuilder.build();
	}

	@Override
	public String getId() {
		return NAME;
	}

	@Override
	public List<Payment> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Payment> payments = new ArrayList<>();

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			commerceOrderId);

		String amount = StringPool.BLANK;

		CommerceMoney totalMoney = commerceOrder.getTotalMoney();

		if (totalMoney != null) {
			amount = totalMoney.format(_portal.getLocale(httpServletRequest));
		}

		List<CommerceOrderPayment> commerceOrderPayments =
			_commerceOrderPaymentLocalService.getCommerceOrderPayments(
				commerceOrder.getCommerceOrderId(),
				pagination.getStartPosition(), pagination.getEndPosition(),
				null);

		for (CommerceOrderPayment commerceOrderPayment :
				commerceOrderPayments) {

			payments.add(
				new Payment(
					commerceOrderPayment.getCommerceOrderPaymentId(),
					new StatusField(
						CommerceOrderPaymentConstants.getOrderPaymentLabelStyle(
							commerceOrderPayment.getStatus()),
						LanguageUtil.get(
							httpServletRequest,
							CommerceOrderPaymentConstants.
								getOrderPaymentStatusLabel(
									commerceOrderPayment.getStatus()))),
					amount, commerceOrderPayment.getCreateDate(),
					commerceOrderPayment.getContent()));
		}

		return payments;
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

	@Reference
	private CommerceOrderPaymentLocalService _commerceOrderPaymentLocalService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private Portal _portal;

}