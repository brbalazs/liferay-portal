/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

/**
 * @author Riccardo Ferrari
 */
public interface DXPAssetEntityIngestionPipelineOptions
	extends DXPIngestionPipelineOptions {

	@Default.String("assetentity_raw")
	@Description("Return the asset entity BigQuery table name.")
	public String getAssetEntityBigQueryTable();

	public void setAssetEntityBigQueryTable(String assetEntityBigQueryTable);

}