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
public class ProductSpecification implements Serializable {

	@Nullable
	public Long id;

	@Nullable
	public Map<String, String> label;

	@Nullable
	public Long optionCategoryId;

	@Nullable
	public Float priority;

	@Nullable
	public Long specificationId;

	public String specificationKey;
	public Map<String, String> value;

}