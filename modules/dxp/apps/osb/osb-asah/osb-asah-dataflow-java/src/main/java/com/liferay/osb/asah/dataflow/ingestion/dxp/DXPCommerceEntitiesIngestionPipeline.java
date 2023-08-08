/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Order;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Product;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.OrderParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.ProductParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.util.PipelineBuilder;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

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

		// Order

		PipelineBuilder orderPipelineBuilder = new PipelineBuilder(
			Pipeline.create(dxpCommerceEntitiesIngestionPipelineOptions));

		Pipeline pipeline = orderPipelineBuilder.<Order>withBigQueryWriter(
			new OrderParserPTransform(),
			dxpCommerceEntitiesIngestionPipelineOptions.getOrderBigQueryTable()
		).withGCSWriter(
			dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket(),
			dxpCommerceEntitiesIngestionPipelineOptions.getShardCount(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getTriggerElementCount(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getTriggerIntervalDuration()
		).withPubsubSubscription(
			dxpCommerceEntitiesIngestionPipelineOptions.
				getOrderPubsubSubscription(),
			"Order"
		).build();

		// Product

		PipelineBuilder productPipelineBuilder = new PipelineBuilder(pipeline);

		pipeline = productPipelineBuilder.<Product>withBigQueryWriter(
			new ProductParserPTransform(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getProductBigQueryTable()
		).withGCSWriter(
			dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket(),
			dxpCommerceEntitiesIngestionPipelineOptions.getShardCount(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getTriggerElementCount(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getTriggerIntervalDuration()
		).withPubsubSubscription(
			dxpCommerceEntitiesIngestionPipelineOptions.
				getProductPubsubSubscription(),
			"Product"
		).build();

		return pipeline.run();
	}

}