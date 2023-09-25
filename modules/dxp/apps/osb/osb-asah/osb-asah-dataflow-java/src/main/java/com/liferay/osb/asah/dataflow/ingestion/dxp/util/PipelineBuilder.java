/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.util;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.BaseDXPEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BaseParserPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.BigQueryWriterPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.FixedDurationOrCountWindowPTransform;
import com.liferay.osb.asah.dataflow.ingestion.dxp.transform.GCSWriterPTransform;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;

import org.joda.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 * @author Rachael Koestartyo
 */
public class PipelineBuilder {

	public PipelineBuilder(Pipeline pipeline) {
		_pipeline = pipeline;
	}

	public Pipeline build() {
		GCSReaderStep gcsReaderStep = (GCSReaderStep)_steps.get(
			"withGCSReader");

		PCollection<DXPEntityMessageWrapper>
			dxpEntityMessageWrapperPCollection = _pipeline.apply(
				"Read GCS Zip File ",
				FileIO.match(
				).filepattern(
					gcsReaderStep.getGCSBucket() + "*.zip"
				)
			).apply(
				FileIO.readMatches(
				).withCompression(
					Compression.ZIP
				)
			).apply(
				"Read Zip File",
				ParDo.of(
					new ZipFileReader(gcsReaderStep.getResourceNameFilter()))
			);

		if (_steps.containsKey("withBigQueryWriter")) {
			BigQueryWriterStep<DXPEntityMessageWrapper, ?> bigQueryWriterStep =
				(BigQueryWriterStep<DXPEntityMessageWrapper, ?>)_steps.get(
					"withBigQueryWriter");

			BaseParserPTransform<DXPEntityMessageWrapper, ?> parserPTransform =
				bigQueryWriterStep.getBaseParserPTransform();

			PCollectionTuple parsedMessagesPCollectionTuple =
				dxpEntityMessageWrapperPCollection.apply(parserPTransform);

			parsedMessagesPCollectionTuple.get(
				parserPTransform.getSuccessTupleTag()
			).apply(
				new BigQueryWriterPTransform<>(
					bigQueryWriterStep.getTable(), null)
			);
		}

		return _pipeline;
	}

	public <E, T extends BaseDXPEntity> PipelineBuilder withBigQueryWriter(
		BaseParserPTransform<E, T> baseParserPTransform, String table) {

		_steps.put(
			"withBigQueryWriter",
			new BigQueryWriterStep<E, T>(baseParserPTransform, table));

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

	public PipelineBuilder withGCSReader(
		String gcsBucket, String resourceNameFilter) {

		_steps.put(
			"withGCSReader", new GCSReaderStep(gcsBucket, resourceNameFilter));

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

	public static class ZipFileReader
		extends DoFn<FileIO.ReadableFile, DXPEntityMessageWrapper> {

		public ZipFileReader(String resourceNameFilter) {
			_resourceNameFilter = resourceNameFilter;
		}

		@ProcessElement
		public void processElement(ProcessContext processContext) {
			FileIO.ReadableFile readableFile = processContext.element();

			MatchResult.Metadata metadata = readableFile.getMetadata();

			ResourceId resourceId = metadata.resourceId();

			String string = resourceId.toString();

			String[] split = string.split("/");

			int length = split.length;

			String resourceName = split[length - 4];

			if (!Objects.equals(_resourceNameFilter, resourceName)) {
				if (_logger.isInfoEnabled()) {
					_logger.error(
						"Skipping resource {} because it does not match with " +
							"resource name filter {}",
						resourceId.getFilename(), _resourceNameFilter);
				}

				return;
			}

			String dataSourceId = split[length - 5];
			String projectId = split[length - 6];
			String uploadTime = split[length - 2];
			String uploadType = split[length - 3];

			try {
				Compression compression = readableFile.getCompression();

				ReadableByteChannel readableByteChannel =
					compression.readDecompressed(FileSystems.open(resourceId));

				try (BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(
							Channels.newInputStream(readableByteChannel)))) {

					String line = null;

					while ((line = bufferedReader.readLine()) != null) {
						DXPEntityMessageWrapper dxpEntityMessageWrapper =
							new DXPEntityMessageWrapper();

						dxpEntityMessageWrapper.dataSourceId = dataSourceId;
						dxpEntityMessageWrapper.payload = line;
						dxpEntityMessageWrapper.projectId = projectId;
						dxpEntityMessageWrapper.resourceName = resourceName;
						dxpEntityMessageWrapper.uploadTime = uploadTime;
						dxpEntityMessageWrapper.uploadType = uploadType;

						processContext.output(dxpEntityMessageWrapper);
					}
				}
			}
			catch (Exception exception) {
				_logger.error(
					"Unable to read file: {}", resourceId.getFilename());
			}
		}

		private static final Logger _logger = LoggerFactory.getLogger(
			ZipFileReader.class);

		private String _resourceNameFilter;

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

	private static class BigQueryWriterStep<E, T extends BaseDXPEntity> {

		public BigQueryWriterStep(
			BaseParserPTransform<E, T> baseParserPTransform, String table) {

			_baseParserPTransform = baseParserPTransform;
			_table = table;
		}

		public BaseParserPTransform<E, T> getBaseParserPTransform() {
			return _baseParserPTransform;
		}

		public String getTable() {
			return _table;
		}

		private final BaseParserPTransform<E, T> _baseParserPTransform;
		private final String _table;

	}

	private static class GCSReaderStep {

		public GCSReaderStep(String gcsBucket, String resourceNameFilter) {
			_gcsBucket = gcsBucket;
			_resourceNameFilter = resourceNameFilter;
		}

		public String getGCSBucket() {
			return _gcsBucket;
		}

		public String getResourceNameFilter() {
			return _resourceNameFilter;
		}

		private final String _gcsBucket;
		private final String _resourceNameFilter;

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