/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

/**
 * @author Marcellus Tavares
 */
public interface DXPEntityIngestionPipelineOptions
	extends DXPIngestionPipelineOptions {

	@Default.String("dxpentity")
	@Description("Return the DXP Entity table name.")
	public String getDXPEntityBigQueryTable();

	public void setDXPEntityBigQueryTable(String dxpEntityBigQueryTable);

}