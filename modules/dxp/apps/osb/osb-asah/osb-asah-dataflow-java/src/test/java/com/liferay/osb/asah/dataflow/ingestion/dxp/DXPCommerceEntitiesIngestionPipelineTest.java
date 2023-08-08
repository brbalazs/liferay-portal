/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.OrderParserPTransform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollectionTuple;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Riccardo Ferrari
 */
public class DXPCommerceEntitiesIngestionPipelineTest {

	@Test
	public void testOrderParser() throws IOException {
		String orderJSON = _readResourceAsString(
			"dependencies/test_dxp_entity_pubsub_message_parser.json");

		OrderParserPTransform orderParserPTransform =
			new OrderParserPTransform();

		PCollectionTuple pCollectionTuple = testPipeline.apply(
			Create.of(orderJSON)
		).apply(
			"Parse DXPEntityPubsubMessage",
			ParDo.of(new DXPEntityPubsubMessageParser())
		).apply(
			"Parse Orders", orderParserPTransform
		);

		PAssert.that(
			pCollectionTuple.get(orderParserPTransform.getFailTupleTag())
		).empty();

		testPipeline.run();
	}

	@Rule
	public final transient TestPipeline testPipeline = TestPipeline.create();

	private String _readResourceAsString(String resourcePath)
		throws IOException {

		Class<?> clazz = getClass();

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
					clazz.getResourceAsStream(resourcePath),
					StandardCharsets.UTF_8))) {

			Stream<String> lines = bufferedReader.lines();

			return lines.collect(Collectors.joining());
		}
	}

}