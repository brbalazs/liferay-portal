/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.CommerceChannelIdMapPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityMessageWrapperZipReaderPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.OrderParserDoFn;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.ProductParserDoFn;

import java.util.Map;
import java.util.Objects;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

/**
 * @author Riccardo Ferrari
 */
public class DXPCommerceEntitiesIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPCommerceEntitiesIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPCommerceEntitiesIngestionPipelineOptions
			dxpCommerceEntitiesIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(
			dxpCommerceEntitiesIngestionPipelineOptions);

		String filePattern =
			dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket() +
				"/*.zip";

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = pipeline.apply(
				new DXPEntityMessageWrapperZipReaderPTransform(filePattern));

		PCollectionTuple pCollectionTuple =
			dxpEntityMessageWrapperPCollection.apply(
				"Branch by Resource Name",
				ParDo.of(
					new DoFn
						<DXPEntityMessageWrapper, DXPEntityMessageWrapper>() {

						@ProcessElement
						public void processElement(
							ProcessContext processContext) {

							DXPEntityMessageWrapper dxpEntityMessageWrapper =
								processContext.element();

							if (Objects.equals(
									dxpEntityMessageWrapper.resourceName,
									_ORDER_V1_RESOURCE_NAME)) {

								processContext.output(
									_orderTupleTag, dxpEntityMessageWrapper);
							}
							else if (Objects.equals(
										dxpEntityMessageWrapper.resourceName,
										_PRODUCT_V1_RESOURCE_NAME)) {

								processContext.output(
									_productTupleTag, dxpEntityMessageWrapper);
							}
						}

					}
				).withOutputTags(
					_orderTupleTag, TupleTagList.of(_productTupleTag)
				));

		// Order

		PCollectionView<Map<Long, Long>> commerceChannelIdMapPCollectionView =
			pipeline.apply(
				new CommerceChannelIdMapPTransform(
					dxpCommerceEntitiesIngestionPipelineOptions.getProject(),
					dxpCommerceEntitiesIngestionPipelineOptions.getProjectId(),
					dxpCommerceEntitiesIngestionPipelineOptions.getRegion()));

		pCollectionTuple.get(
			_orderTupleTag
		).apply(
			"Parse Orders",
			ParDo.of(
				new OrderParserDoFn(commerceChannelIdMapPCollectionView)
			).withSideInputs(
				commerceChannelIdMapPCollectionView
			)
		).apply(
			"Write Orders",
			new BigQueryWriterPTransform<>(
				dxpCommerceEntitiesIngestionPipelineOptions.
					getOrderBigQueryTable(),
				dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket() +
					"/order-temp")
		);

		// Product

		pCollectionTuple.get(
			_productTupleTag
		).apply(
			"Parse Products", ParDo.of(new ProductParserDoFn())
		).apply(
			"Write Products",
			new BigQueryWriterPTransform<>(
				dxpCommerceEntitiesIngestionPipelineOptions.
					getProductBigQueryTable(),
				dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket() +
					"/product-temp")
		);

		return pipeline.run();
	}

	private static final String _ORDER_V1_RESOURCE_NAME =
		"com.liferay.headless.commerce.machine.learning.dto.v1_0.Order";

	private static final String _PRODUCT_V1_RESOURCE_NAME =
		"com.liferay.headless.commerce.machine.learning.dto.v1_0.Product";

	private static final TupleTag<DXPEntityMessageWrapper> _orderTupleTag =
		new TupleTag<DXPEntityMessageWrapper>() {
		};
	private static final TupleTag<DXPEntityMessageWrapper> _productTupleTag =
		new TupleTag<DXPEntityMessageWrapper>() {
		};

}