/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.DXPEntityParserDoFn;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityMessageWrapperZipReaderPTransform;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

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

		dxpEntityMessageWrapperPCollection.apply(
			"Parse DXP Entities", ParDo.of(new DXPEntityParserDoFn())
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