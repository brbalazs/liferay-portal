/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Riccardo Ferrari
 */
@DefaultSchema(JavaFieldSchema.class)
public class Order extends BaseDXPEntity {

	public long accountId;

	@JsonProperty("channelId")
	@Nullable
	public Long commerceChannelId;

	public String createDate;

	@Nullable
	public String currencyCode;

	@Nullable
	public String externalReferenceCode;

	public long id;
	public String modifiedDate;
	public String orderDate;
	public List<OrderItem> orderItems;
	public long orderStatus;

	@Nullable
	public String orderTypeExternalReferenceCode;

	@Nullable
	public Long orderTypeId;

	@Nullable
	public String paymentMethod;

	@Nullable
	public Long paymentStatus;

	public Long status;

	@Nullable
	public String total;

	@Nullable
	public Long userId;

}