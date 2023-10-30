/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.AssetEntityParserDoFn;
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
public class DXPAssetEntityIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPAssetEntityIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPAssetEntityIngestionPipelineOptions
			dxpAssetEntityIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(
			dxpAssetEntityIngestionPipelineOptions);

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = pipeline.apply(
				new DXPEntityMessageWrapperZipReaderPTransform(
					dxpAssetEntityIngestionPipelineOptions.getZipFilePath()));

		dxpEntityMessageWrapperPCollection.apply(
			"Parse Asset Entities", ParDo.of(new AssetEntityParserDoFn())
		).apply(
			"Write Asset Entities",
			new BigQueryWriterPTransform<>(
				dxpAssetEntityIngestionPipelineOptions.
					getBigQueryWriterTempLocation(),
				dxpAssetEntityIngestionPipelineOptions.
					getAssetEntityBigQueryTable())
		);

		return pipeline.run();
	}

}