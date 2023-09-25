/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Riccardo Ferrari
 */
public interface DXPProductIngestionPipelineOptions
	extends DataflowPipelineOptions {

	@Description("Return the GCS output bucket")
	@Validation.Required
	public String getGCSBucket();

	@Description("Return the product BigQuery table name.")
	@Validation.Required
	public String getProductBigQueryTable();

	@Description("Return the Analytics Cloud project ID")
	@Validation.Required
	public String getProjectId();

	public void setGCSBucket(String gcsBucket);

	public void setProductBigQueryTable(String productBigQueryTable);

	public String setProjectId();

}