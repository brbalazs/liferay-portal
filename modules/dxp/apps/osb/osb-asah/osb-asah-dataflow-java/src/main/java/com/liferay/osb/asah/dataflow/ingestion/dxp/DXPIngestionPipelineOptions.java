/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Marcellus Tavares
 */
public interface DXPIngestionPipelineOptions extends DataflowPipelineOptions {

	@Description("Return the BigQuery GCS temporary location")
	@Validation.Required
	public String getBigQueryWriterTempLocation();

	@Description("Return the Analytics Cloud project ID")
	@Validation.Required
	public String getProjectId();

	@Description("Return the ZIP file path")
	@Validation.Required
	public String getZipFilePath();

	public void setBigQueryWriterTempLocation(
		String bigQueryWriterTempLocation);

	public void setProjectId(String projectId);

	public void setZipFilePath(String zipFilePath);

}