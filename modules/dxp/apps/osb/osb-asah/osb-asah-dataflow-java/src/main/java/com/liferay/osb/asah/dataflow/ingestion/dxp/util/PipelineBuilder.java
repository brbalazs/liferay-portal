/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.util;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.BaseDXPEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BaseParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryInsertErrorWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.FixedDurationOrCountWindowPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.GCSWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.PubsubReaderPTransform;

import java.util.HashMap;
import java.util.Map;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryInsertError;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.transforms.Values;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;

import org.joda.time.Duration;

/**
 * @author Riccardo Ferrari
 * @author Rachael Koestartyo
 */
public class PipelineBuilder {

	public PipelineBuilder(Pipeline pipeline) {
		_pipeline = pipeline;
	}

	public Pipeline build() {
		PubsubSubscriptionStep pubsubSubscriptionStep =
			(PubsubSubscriptionStep)_steps.get("withPubsubSubscription");

		PCollection<DXPEntityPubsubMessage> dxpEntityPubsubMessagePCollection =
			_pipeline.apply(
				"Read Pubsub Subscription " + pubsubSubscriptionStep.getTitle(),
				new PubsubReaderPTransform(
					pubsubSubscriptionStep.getSubscription()));

		if (_steps.containsKey("withGCSWriter")) {
			GCSWriterStep gcsWriterStep = (GCSWriterStep)_steps.get(
				"withGCSWriter");

			_writeToGCS(
				dxpEntityPubsubMessagePCollection, gcsWriterStep.getGCSBucket(),
				gcsWriterStep.getShardCount(),
				gcsWriterStep.getTriggerElementCount(),
				gcsWriterStep.getTriggerInterval());
		}

		if (_steps.containsKey("withBigQueryWriter")) {
			BigQueryWriterStep<?> bigQueryWriterStep =
				(BigQueryWriterStep<?>)_steps.get("withBigQueryWriter");

			BaseParserPTransform<?> parserPTransform =
				bigQueryWriterStep.getBaseParserPTransform();

			PCollectionTuple parsedMessagesPCollectionTuple =
				dxpEntityPubsubMessagePCollection.apply(parserPTransform);

			WriteResult writeResult = parsedMessagesPCollectionTuple.get(
				parserPTransform.getSuccessTupleTag()
			).apply(
				new BigQueryWriterPTransform<>(
					bigQueryWriterStep.getTable(), null)
			);

			if (_steps.containsKey("withFailedParsedItemsToGCS")) {
				GCSWriterStep gcsWriterStep = (GCSWriterStep)_steps.get(
					"withFailedParsedItemsToGCS");

				PCollection<DXPEntityPubsubMessage>
					failParseDxpEntityPubsubMessagePCollection =
						parsedMessagesPCollectionTuple.get(
							parserPTransform.getFailTupleTag()
						).apply(
							Values.create()
						);

				_writeToGCS(
					failParseDxpEntityPubsubMessagePCollection,
					gcsWriterStep.getGCSBucket(), gcsWriterStep.getShardCount(),
					gcsWriterStep.getTriggerElementCount(),
					gcsWriterStep.getTriggerInterval());
			}

			if (_steps.containsKey("withFailedBigQueryItemsToGCS")) {
				GCSWriterStep gcsWriterStep = (GCSWriterStep)_steps.get(
					"withFailedBigQueryItemsToGCS");

				PCollection<BigQueryInsertError>
					bigQueryInsertErrorPCollection =
						writeResult.getFailedInsertsWithErr();

				PCollection<DXPEntityPubsubMessage>
					bigqueryErrorDxpEntityPubsubMessagePCollection =
						bigQueryInsertErrorPCollection.apply(
							new BigQueryInsertErrorWriterPTransform());

				_writeToGCS(
					bigqueryErrorDxpEntityPubsubMessagePCollection,
					gcsWriterStep.getGCSBucket(), gcsWriterStep.getShardCount(),
					gcsWriterStep.getTriggerElementCount(),
					gcsWriterStep.getTriggerInterval());
			}
		}

		return _pipeline;
	}

	public <T extends BaseDXPEntity> PipelineBuilder withBigQueryWriter(
		BaseParserPTransform<T> baseParserPTransform, String table) {

		_steps.put(
			"withBigQueryWriter",
			new BigQueryWriterStep<T>(baseParserPTransform, table));

		return this;
	}

	public PipelineBuilder withFailedBigQueryItemsToGCS(
		String gcsBucket, int shardCount, int triggerElementCount,
		long triggerInterval) {

		_steps.put(
			"withFailedBigQueryItemsToGCS",
			new GCSWriterStep(
				gcsBucket, shardCount, triggerElementCount, triggerInterval));

		return this;
	}

	public PipelineBuilder withFailedParsedItemsToGCS(
		String gcsBucket, int shardCount, int triggerElementCount,
		long triggerInterval) {

		_steps.put(
			"withFailedParsedItemsToGCS",
			new GCSWriterStep(
				gcsBucket, shardCount, triggerElementCount, triggerInterval));

		return this;
	}

	public PipelineBuilder withGCSWriter(
		String gcsBucket, int shardCount, int triggerElementCount,
		long triggerInterval) {

		_steps.put(
			"withGCSWriter",
			new GCSWriterStep(
				gcsBucket, shardCount, triggerElementCount, triggerInterval));

		return this;
	}

	public PipelineBuilder withPubsubSubscription(
		String subscription, String title) {

		_steps.put(
			"withPubsubSubscription",
			new PubsubSubscriptionStep(subscription, title));

		return this;
	}

	private void _writeToGCS(
		PCollection<DXPEntityPubsubMessage> dxpEntityPubsubMessagePCollection,
		String gcsBucket, int shardCount, int triggerElementCount,
		long triggerInterval) {

		dxpEntityPubsubMessagePCollection.apply(
			String.format(
				"Window By %s Elements Count Or %s Seconds",
				triggerElementCount, triggerInterval),
			new FixedDurationOrCountWindowPTransform<>(
				triggerElementCount, Duration.standardSeconds(triggerInterval))
		).apply(
			String.format(
				"Write to GCS Bucket %s Using %s Shard Count", gcsBucket,
				shardCount),
			new GCSWriterPTransform("part", ".json", gcsBucket, shardCount)
		);
	}

	private final Pipeline _pipeline;
	private final Map<String, Object> _steps = new HashMap<>();

	private static class BigQueryWriterStep<T extends BaseDXPEntity> {

		public BigQueryWriterStep(
			BaseParserPTransform<T> baseParserPTransform, String table) {

			_baseParserPTransform = baseParserPTransform;
			_table = table;
		}

		public BaseParserPTransform<T> getBaseParserPTransform() {
			return _baseParserPTransform;
		}

		public String getTable() {
			return _table;
		}

		private final BaseParserPTransform<T> _baseParserPTransform;
		private final String _table;

	}

	private static class GCSWriterStep {

		public GCSWriterStep(
			String gcsBucket, int shardCount, int triggerElementCount,
			long triggerInterval) {

			_gcsBucket = gcsBucket;
			_shardCount = shardCount;
			_triggerElementCount = triggerElementCount;
			_triggerInterval = triggerInterval;
		}

		public String getGCSBucket() {
			return _gcsBucket;
		}

		public int getShardCount() {
			return _shardCount;
		}

		public int getTriggerElementCount() {
			return _triggerElementCount;
		}

		public long getTriggerInterval() {
			return _triggerInterval;
		}

		private final String _gcsBucket;
		private final int _shardCount;
		private final int _triggerElementCount;
		private final long _triggerInterval;

	}

	private static class PubsubSubscriptionStep {

		public PubsubSubscriptionStep(String subscription, String title) {
			_subscription = subscription;
			_title = title;
		}

		public String getSubscription() {
			return _subscription;
		}

		public String getTitle() {
			return _title;
		}

		private final String _subscription;
		private final String _title;

	}

}