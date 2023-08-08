/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.google.api.services.bigquery.model.ErrorProto;
import com.google.api.services.bigquery.model.TableDataInsertAllResponse;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.beam.sdk.io.gcp.bigquery.BigQueryInsertError;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class BigQueryInsertErrorWriterPTransform
	extends PTransform
		<PCollection<BigQueryInsertError>,
		 PCollection<DXPEntityPubsubMessage>> {

	@Override
	public PCollection<DXPEntityPubsubMessage> expand(
		PCollection<BigQueryInsertError> input) {

		return input.apply(
			MapElements.via(
				new SimpleFunction
					<BigQueryInsertError, DXPEntityPubsubMessage>() {

					@Override
					public DXPEntityPubsubMessage apply(
						BigQueryInsertError input) {

						TableRow tableRow = input.getRow();

						TableDataInsertAllResponse.InsertErrors insertErrors =
							input.getError();

						List<ErrorProto> errorProtos = insertErrors.getErrors();

						Stream<ErrorProto> errorProtosStream =
							errorProtos.stream();

						List<Map<String, String>> errors =
							errorProtosStream.map(
								errorProto -> new HashMap<String, String>() {
									{
										put(
											"id",
											String.valueOf(
												tableRow.getOrDefault(
													"id", "-1")));
										put(
											"location",
											errorProto.getLocation());
										put("message", errorProto.getMessage());
										put("reason", errorProto.getReason());
									}
								}
							).collect(
								Collectors.toList()
							);

						if (_logger.isErrorEnabled()) {
							_logger.error(
								ObjectMapperUtil.writeValueAsString(errors));
						}

						TableReference tableReference = input.getTable();

						Map<String, String> attributes =
							new HashMap<String, String>() {
								{
									put(
										"dataSourceId",
										(String)tableRow.getOrDefault(
											"dataSourceId", "dataSourceId"));
									put(
										"projectId",
										(String)tableRow.getOrDefault(
											"projectId", "projectId"));
									put(
										"resourceName",
										tableReference.getTableId());
									put(
										"uploadTime",
										(String)tableRow.getOrDefault(
											"uploadDate", "uploadDate"));
									put(
										"uploadType",
										(String)tableRow.getOrDefault(
											"uploadType", "uploadType"));
								}
							};

						return new DXPEntityPubsubMessage(
							attributes,
							ObjectMapperUtil.writeValueAsString(errors));
					}

				}));
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		BigQueryInsertErrorWriterPTransform.class);

}