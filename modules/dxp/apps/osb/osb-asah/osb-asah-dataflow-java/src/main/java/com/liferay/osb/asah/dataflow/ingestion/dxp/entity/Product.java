/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Riccardo Ferrari
 */
@DefaultSchema(JavaFieldSchema.class)
public class Product extends BaseDXPEntity {

	@Nullable
	public Long catalogId;

	public List<Long> categoryIds;

	@Nullable
	public String createDate;

	@Nullable
	public Map<String, String> description;

	@Nullable
	public String displayDate;

	@Nullable
	public String expirationDate;

	@Nullable
	public String externalReferenceCode;

	@Nullable
	public Long id;

	@Nullable
	public Map<String, String> metaDescription;

	@Nullable
	public Map<String, String> metaKeyword;

	@Nullable
	public Map<String, String> metaTitle;

	@Nullable
	public String modifiedDate;

	@Nullable
	public Map<String, String> name;

	@Nullable
	public List<Long> productChannelIds;

	@Nullable
	public Long productId;

	@Nullable
	public List<ProductOption> productOptions;

	@Nullable
	public List<ProductSpecification> productSpecifications;

	@Nullable
	public String productType;

	@Nullable
	public List<Sku> skus;

	@Nullable
	public Long status;

	@Nullable
	public Boolean subscriptionEnabled;

	@Nullable
	public List<String> tags;

	@Nullable
	public Map<String, String> urls;

}