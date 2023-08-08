/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Riccardo Ferrari
 */
@DefaultSchema(JavaFieldSchema.class)
public class Sku implements Serializable {

	@Nullable
	public String cost;

	@Nullable
	public Boolean discontinued;

	@Nullable
	public String displayDate;

	@Nullable
	public String expirationDate;

	@Nullable
	public String externalReferenceCode;

	@Nullable
	public String gtin;

	@Nullable
	public Long id;

	@Nullable
	public String manufacturerPartNumber;

	@Nullable
	public Boolean published;

	@Nullable
	public Boolean purchasable;

	public String sku;

}