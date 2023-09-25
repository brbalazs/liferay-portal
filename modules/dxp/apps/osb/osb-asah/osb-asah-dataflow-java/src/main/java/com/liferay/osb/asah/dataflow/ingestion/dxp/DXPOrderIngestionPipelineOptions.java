/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Riccardo Ferrari
 */
public interface DXPOrderIngestionPipelineOptions
	extends DXPIngestionPipelineOptions {

	@Description("Return the order BigQuery table name.")
	@Validation.Required
	public String getOrderBigQueryTable();

	public void setOrderBigQueryTable(String orderBigQueryTable);

}