/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.ingestion.dxp.util.TableRowConverter;

import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.InsertRetryPolicy;
import org.apache.beam.sdk.io.gcp.bigquery.TableDestination;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.ValueInSingleWindow;

/**
 * @author Riccardo Ferrari
 */
public class BigQueryWriterPTransform<T>
	extends PTransform<PCollection<T>, WriteResult> {

	public BigQueryWriterPTransform(String tableName) {
		_tableName = tableName;
	}

	@Override
	public WriteResult expand(PCollection<T> input) {
		return input.apply(
			MapElements.via(
				new SimpleFunction<T, TableRow>() {

					@Override
					public TableRow apply(T input) {
						return TableRowConverter.asTableRow(input);
					}

				})
		).apply(
			String.format("Write to BigQuery table: %s", _tableName),
			BigQueryIO.writeTableRows(
			).to(
				new SerializableFunction
					<ValueInSingleWindow<TableRow>, TableDestination>() {

					@Override
					public TableDestination apply(
						ValueInSingleWindow<TableRow> valueInSingleWindow) {

						TableRow tableRow = valueInSingleWindow.getValue();

						return new TableDestination(
							String.format(
								"%s.%s", tableRow.get("projectId"), _tableName),
							null);
					}

				}
			).withCreateDisposition(
				BigQueryIO.Write.CreateDisposition.CREATE_NEVER
			).withExtendedErrorInfo(
			).withFailedInsertRetryPolicy(
				InsertRetryPolicy.retryTransientErrors()
			).withMethod(
				BigQueryIO.Write.Method.STREAMING_INSERTS
			).withWriteDisposition(
				BigQueryIO.Write.WriteDisposition.WRITE_APPEND
			).withoutValidation()
		);
	}

	private final String _tableName;

}