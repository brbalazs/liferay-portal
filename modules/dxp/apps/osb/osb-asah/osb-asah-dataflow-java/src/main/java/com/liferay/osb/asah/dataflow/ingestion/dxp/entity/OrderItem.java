/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.io.Serializable;

import java.util.Map;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Riccardo Ferrari
 */
@DefaultSchema(JavaFieldSchema.class)
public class OrderItem implements Serializable {

	public long cpDefinitionId;
	public String createDate;

	@Nullable
	public Map<String, String> customFields;

	@Nullable
	public String externalReferenceCode;

	@Nullable
	public String finalPrice;

	public long id;

	@Nullable
	public String modifiedDate;

	@Nullable
	public Map<String, String> name;

	@Nullable
	public String options;

	public long parentOrderItemId;
	public long quantity;
	public String sku;

	@Nullable
	public Boolean subscription;

	@Nullable
	public String unitOfMeasure;

	@Nullable
	public String unitPrice;

	public long userId;

}