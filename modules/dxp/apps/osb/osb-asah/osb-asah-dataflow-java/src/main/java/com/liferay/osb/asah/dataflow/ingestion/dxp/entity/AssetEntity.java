/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Riccardo Ferrari
 */
@DefaultSchema(JavaFieldSchema.class)
public class AssetEntity extends BaseDXPEntity {

	public List<Long> assetCategoryIds;

	@Nullable
	public List<String> assetTagNames;

	public String className;
	public long classPK;

	@Nullable
	public Long classTypeId;

	@Nullable
	public String classTypeName;

	@Nullable
	public String createDate;

	@Nullable
	public String expirationDate;

	@Nullable
	public Long groupId;

	public long id;

	@Nullable
	public String modifiedDate;

	@Nullable
	public String publishDate;

	@Nullable
	public String title;

}