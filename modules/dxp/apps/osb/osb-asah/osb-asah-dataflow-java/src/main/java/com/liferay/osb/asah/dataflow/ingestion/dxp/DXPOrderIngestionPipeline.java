/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.function.OrderParserDoFn;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.CommerceChannelIdMapPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityMessageWrapperZipReaderPTransform;

import java.util.Map;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;

/**
 * @author Riccardo Ferrari
 */
public class DXPOrderIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPOrderIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPOrderIngestionPipelineOptions dxpOrderIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(dxpOrderIngestionPipelineOptions);

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = pipeline.apply(
				new DXPEntityMessageWrapperZipReaderPTransform(
					dxpOrderIngestionPipelineOptions.getZipFilePath()));

		PCollectionView<Map<Long, Long>> commerceChannelIdMapPCollectionView =
			pipeline.apply(
				new CommerceChannelIdMapPTransform(
					dxpOrderIngestionPipelineOptions.getProject(),
					dxpOrderIngestionPipelineOptions.getProjectId(),
					dxpOrderIngestionPipelineOptions.getRegion()));

		dxpEntityMessageWrapperPCollection.apply(
			"Parse Orders",
			ParDo.of(
				new OrderParserDoFn(commerceChannelIdMapPCollectionView)
			).withSideInputs(
				commerceChannelIdMapPCollectionView
			)
		).apply(
			"Write Orders",
			new BigQueryWriterPTransform<>(
				dxpOrderIngestionPipelineOptions.
					getBigQueryWriterTempLocation(),
				dxpOrderIngestionPipelineOptions.getOrderBigQueryTable())
		);

		return pipeline.run();
	}

}