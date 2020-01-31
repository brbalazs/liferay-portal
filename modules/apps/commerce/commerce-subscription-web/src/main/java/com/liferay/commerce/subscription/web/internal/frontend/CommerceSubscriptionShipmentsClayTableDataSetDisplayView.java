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

package com.liferay.commerce.subscription.web.internal.frontend;

import com.liferay.commerce.frontend.clay.data.set.ClayDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableSchema;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilderFactory;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaField;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.set.display.name=" + CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_SHIPMENTS,
	service = ClayDataSetDisplayView.class
)
public class CommerceSubscriptionShipmentsClayTableDataSetDisplayView
	extends ClayTableDataSetDisplayView {

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("createDate", "createDate");

		ClayTableSchemaField shipmentId = clayTableSchemaBuilder.addField(
			"shipmentId", "shipmentId");

		shipmentId.setContentRenderer("link");

		ClayTableSchemaField status = clayTableSchemaBuilder.addField(
			"status", "status");

		status.setContentRenderer("label");

		ClayTableSchemaField orderId = clayTableSchemaBuilder.addField(
			"orderId", "orderId");

		orderId.setContentRenderer("link");

		clayTableSchemaBuilder.addField("receiver", "sent-to");

		ClayTableSchemaField tracking = clayTableSchemaBuilder.addField(
			"tracking", "tracking");

		tracking.setContentRenderer("link");

		return clayTableSchemaBuilder.build();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;

}