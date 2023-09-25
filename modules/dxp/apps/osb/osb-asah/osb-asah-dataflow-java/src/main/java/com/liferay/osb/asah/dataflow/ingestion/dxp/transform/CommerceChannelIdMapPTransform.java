/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.google.api.services.bigquery.model.TableRow;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcellus Tavares
 */
public class CommerceChannelIdMapPTransform
	extends PTransform<PBegin, PCollectionView<Map<Long, Long>>> {

	public CommerceChannelIdMapPTransform(
		String googleProjectId, String projectId, String region) {

		_googleProjectId = googleProjectId;
		_projectId = projectId;
		_region = region;
	}

	@Override
	public PCollectionView<Map<Long, Long>> expand(PBegin pBegin) {
		return pBegin.apply(
			"Read Commerce Channels from BigQuery",
			BigQueryIO.readTableRows(
			).fromQuery(
				StringUtils.replaceEach(
					_COMMERCE_CHANNEL_ID_QUERY_TEMPLATE,
					new String[]{
						"${googleProjectId}", "${projectId}", "${region}"
					},
					new String[]{
						_googleProjectId,
						_projectId,
						_region
					})
			).usingStandardSql(
			).withMethod(
				BigQueryIO.TypedRead.Method.DIRECT_READ
			).withQueryLocation(
				_region
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
						String commerceChannelId = (String) tableRow.get(
							"commercechannelid");
						String channelId = (String) tableRow.get("id");

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
	}

	private static final String _COMMERCE_CHANNEL_ID_QUERY_TEMPLATE =
		"SELECT * FROM EXTERNAL_QUERY('${googleProjectId}.${region}." +
			"postgresql', 'SELECT unnest(commercechannelids) AS " +
			"commercechannelid, id FROM ${projectId}.channel JOIN " +
			"${projectId}.channeldatasource ON (channel.id = " +
			"channeldatasource.channelid);')";

	private String _googleProjectId;
	private String _projectId;
	private String _region;

}