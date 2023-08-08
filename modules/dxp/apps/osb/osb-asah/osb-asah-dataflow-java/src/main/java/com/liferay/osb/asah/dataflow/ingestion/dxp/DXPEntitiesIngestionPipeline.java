/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.DXPEntityParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.util.PipelineBuilder;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

/**
 * @author Marcos Martins
 * @author Rachael Koestartyo
 */
public class DXPEntitiesIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				DXPEntitiesIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		DXPEntitiesIngestionPipelineOptions
			dxpEntitiesIngestionPipelineOptions) {

		PipelineBuilder defaultPipelineBuilder = new PipelineBuilder(
			Pipeline.create(dxpEntitiesIngestionPipelineOptions));

		Pipeline pipeline = defaultPipelineBuilder.withBigQueryWriter(
			new DXPEntityParserPTransform(), "dxpentity"
		).withGCSWriter(
			dxpEntitiesIngestionPipelineOptions.getGCSBucket(),
			dxpEntitiesIngestionPipelineOptions.getShardCount(),
			dxpEntitiesIngestionPipelineOptions.getTriggerElementCount(),
			dxpEntitiesIngestionPipelineOptions.getTriggerIntervalDuration()
		).withPubsubSubscription(
			dxpEntitiesIngestionPipelineOptions.getPubsubSubscription(),
			"DXPEntity"
		).build();

		return pipeline.run();
	}

}