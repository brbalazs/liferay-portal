/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.DXPEntityParserDoFn;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.DXPEntityUserSuppressor;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityMessageWrapperZipReaderPTransform;

import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.beam.sdk.values.TypeDescriptors;

/**
 * @author Marcellus Tavares
 */
public class DXPEntityIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPEntityIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPEntityIngestionPipelineOptions dxpEntityIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(dxpEntityIngestionPipelineOptions);

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = pipeline.apply(
				new DXPEntityMessageWrapperZipReaderPTransform(
					dxpEntityIngestionPipelineOptions.getZipFilePath()));

		PCollection<String> suppressedEmailAddressesPCollection =
			pipeline.apply(
				"Read Suppressed Email Addresses from BigQuery",
				BigQueryIO.readTableRows(
				).fromQuery(
					String.format(
						"SELECT emailAddress FROM %s.%s",
						dxpEntityIngestionPipelineOptions.getProjectId(),
						"suppression")
				).withMethod(
					BigQueryIO.TypedRead.Method.DIRECT_READ
				)
			).apply(
				"Map Table Row Results",
				MapElements.into(
					TypeDescriptors.strings()
				).via(
					(SerializableFunction<TableRow, String>)
						tableRow -> (String)tableRow.get("emailAddress")
				)
			);

		PCollectionView<List<String>> suppressedEmailAddressesPCollectionView =
			suppressedEmailAddressesPCollection.apply(View.asList());

		dxpEntityMessageWrapperPCollection.apply(
			"Parse DXP Entities", ParDo.of(new DXPEntityParserDoFn())
		).apply(
			"Suppress DXP Entity Users",
			ParDo.of(
				new DXPEntityUserSuppressor(
					suppressedEmailAddressesPCollectionView)
			).withSideInputs(
				suppressedEmailAddressesPCollectionView
			)
		).apply(
			"Write DXP Entities",
			new BigQueryWriterPTransform<>(
				dxpEntityIngestionPipelineOptions.
					getBigQueryWriterTempLocation(),
				dxpEntityIngestionPipelineOptions.getDXPEntityBigQueryTable())
		);

		return pipeline.run();
	}

}