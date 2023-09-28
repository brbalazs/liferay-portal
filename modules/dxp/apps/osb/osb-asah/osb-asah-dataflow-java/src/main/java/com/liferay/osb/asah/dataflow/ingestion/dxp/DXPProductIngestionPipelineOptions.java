/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

/**
 * @author Riccardo Ferrari
 */
public interface DXPProductIngestionPipelineOptions
	extends DXPIngestionPipelineOptions {

	@Default.String("product_raw")
	@Description("Return the product BigQuery table name.")
	public String getProductBigQueryTable();

	public void setProductBigQueryTable(String productBigQueryTable);

}