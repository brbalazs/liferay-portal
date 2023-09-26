/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.ProductParserDoFn;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityMessageWrapperZipReaderPTransform;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

/**
 * @author Riccardo Ferrari
 */
public class DXPProductIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPProductIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPProductIngestionPipelineOptions dxpProductIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(dxpProductIngestionPipelineOptions);

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = pipeline.apply(
				new DXPEntityMessageWrapperZipReaderPTransform(
					dxpProductIngestionPipelineOptions.getZipFilePath()));

		dxpEntityMessageWrapperPCollection.apply(
			"Parse Products", ParDo.of(new ProductParserDoFn())
		).apply(
			"Write Products",
			new BigQueryWriterPTransform<>(
				dxpProductIngestionPipelineOptions.
					getBigQueryWriterTempLocation(),
				dxpProductIngestionPipelineOptions.getProductBigQueryTable())
		);

		return pipeline.run();
	}

}