/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.OrderParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.ProductParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.util.PipelineBuilder;

import java.util.Iterator;
import java.util.Map;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.commons.lang3.StringUtils;

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

		// Order Side Input

		Pipeline pipeline = Pipeline.create(
			dxpCommerceEntitiesIngestionPipelineOptions);

		PCollectionView<Map<Long, Long>>
			commerceChanelIdChannelIdPCollectionView = pipeline.apply(
				"Read Commerce Channels from BigQuery",
				BigQueryIO.readTableRows(
				).fromQuery(
					StringUtils.replaceEach(
						_COMMERCE_CHANNEL_IDS_QUERY_TEMPLATE,
						new String[] {
							"${googleProjectId}", "${projectId}", "${region}"
						},
						new String[] {
							dxpCommerceEntitiesIngestionPipelineOptions.
								getProject(),
							dxpCommerceEntitiesIngestionPipelineOptions.
								getProjectId(),
							dxpCommerceEntitiesIngestionPipelineOptions.
								getRegion()
						})
				).usingStandardSql(
				).withMethod(
					BigQueryIO.TypedRead.Method.DIRECT_READ
				).withQueryLocation(
					dxpCommerceEntitiesIngestionPipelineOptions.getRegion()
				).withoutValidation()
			).apply(
				"Map Table Row Results",
				MapElements.into(
					TypeDescriptors.kvs(
						TypeDescriptors.longs(), TypeDescriptors.longs())
				).via(
					new SerializableFunction<TableRow, KV<Long, Long>>() {

						@Override
						public KV<Long, Long> apply(TableRow tableRow) {
							String commerceChannelId = (String)tableRow.get(
								"commercechannelid");
							String channelId = (String)tableRow.get("id");

							return KV.of(
								Long.parseLong(commerceChannelId),
								Long.parseLong(channelId));
						}

					}
				)
			).apply(
				Combine.perKey(
					new SerializableFunction<Iterable<Long>, Long>() {

						@Override
						public Long apply(Iterable<Long> input) {
							Iterator<Long> iterator = input.iterator();

							return iterator.next();
						}

					})
			).apply(
				View.asMap()
			);

		// Order

		PipelineBuilder orderPipelineBuilder = new PipelineBuilder(pipeline);

		orderPipelineBuilder.withBigQueryWriter(
			new OrderParserPTransform(),
			dxpCommerceEntitiesIngestionPipelineOptions.getOrderBigQueryTable()
		).withGCSReader(
			dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket(),
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Order"
		).build();

		// Product

		PipelineBuilder productPipelineBuilder = new PipelineBuilder(pipeline);

		pipeline = productPipelineBuilder.withBigQueryWriter(
			new ProductParserPTransform(),
			dxpCommerceEntitiesIngestionPipelineOptions.
				getProductBigQueryTable()
		).withGCSReader(
			dxpCommerceEntitiesIngestionPipelineOptions.getGCSBucket(),
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Product"
		).build();

		return pipeline.run();
	}

	private static final String _COMMERCE_CHANNEL_IDS_QUERY_TEMPLATE =
		"SELECT * FROM EXTERNAL_QUERY('${googleProjectId}.${region}." +
			"postgresql', 'SELECT unnest(commercechannelids) AS " +
				"commercechannelid, id FROM ${projectId}.channel JOIN " +
					"${projectId}.channeldatasource ON (channel.id = " +
						"channeldatasource.channelid);')";

}