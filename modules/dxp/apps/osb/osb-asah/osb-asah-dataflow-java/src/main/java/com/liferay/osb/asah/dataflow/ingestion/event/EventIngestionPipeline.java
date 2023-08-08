/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event;

import com.google.api.services.bigquery.model.TableRow;
import com.google.common.collect.Lists;

import com.liferay.osb.asah.dataflow.common.DateUtil;
import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.event.browscap.BrowscapDevice;
import com.liferay.osb.asah.dataflow.ingestion.event.browscap.BrowscapEngine;
import com.liferay.osb.asah.dataflow.ingestion.event.ip.geocoder.IPGeocoder;
import com.liferay.osb.asah.dataflow.ingestion.event.ip.geocoder.IPInfo;
import com.liferay.osb.asah.dataflow.io.WriteToText;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.TableDestination;
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.AfterProcessingTime;
import org.apache.beam.sdk.transforms.windowing.AfterWatermark;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.apache.beam.sdk.transforms.windowing.Repeatedly;
import org.apache.beam.sdk.transforms.windowing.Sessions;
import org.apache.beam.sdk.transforms.windowing.TimestampCombiner;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.apache.beam.sdk.values.ValueInSingleWindow;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import org.joda.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcellus Tavares
 */
public class EventIngestionPipeline {

	public static void main(String[] args) {
		run(
			PipelineOptionsFactory.fromArgs(
				args
			).withValidation(
			).as(
				EventIngestionPipelineOptions.class
			));
	}

	public static PipelineResult run(
		EventIngestionPipelineOptions eventIngestionPipelineOptions) {

		Pipeline pipeline = Pipeline.create(eventIngestionPipelineOptions);

		pipeline.apply(
			"Backup PubSub Messages",
			new BackupPubsubMessages(
				eventIngestionPipelineOptions.getInputSubscription(),
				eventIngestionPipelineOptions.getOutputDirectory(),
				eventIngestionPipelineOptions.getOutputFileNamePrefix(),
				eventIngestionPipelineOptions.getWriteInterval()));

		PCollection<KV<String, AnalyticsEvent>> pCollection = pipeline.apply(
			"Read PubSub Messages",
			PubsubIO.readStrings(
			).fromSubscription(
				eventIngestionPipelineOptions.getInputSubscription()
			).withIdAttribute(
				"id"
			).withTimestampAttribute(
				"eventDate"
			)
		).apply(
			"Parse Analytics Events", ParDo.of(new AnalyticsEventParser())
		).apply(
			"Extract Geolocation/Device Information",
			ParDo.of(new AnalyticsEventExtractor())
		).apply(
			"Add Session Key", _buildWithKeysPTransform()
		);

		Window<KV<String, AnalyticsEvent>> window = _buildWindow(
			eventIngestionPipelineOptions);

		Window<KV<String, AnalyticsEvent>> analyticsEventsWindow =
			window.discardingFiredPanes();

		PCollection<KV<String, Iterable<AnalyticsEvent>>> eventsPCollection =
			pCollection.apply(
				"Create Events Windowing",
				analyticsEventsWindow.triggering(
					Repeatedly.forever(
						AfterWatermark.pastEndOfWindow(
						).withEarlyFirings(
							AfterProcessingTime.pastFirstElementInPane(
							).plusDelayOf(
								Duration.standardMinutes(
									eventIngestionPipelineOptions.
										getSessionWindowEarlyFiringsInterval())
							)
						)))
			).apply(
				GroupByKey.create()
			);

		PCollection<KV<String, Iterable<AnalyticsEvent>>> sessionsPCollection =
			pCollection.apply(
				"Create Sessions Windowing", window.accumulatingFiredPanes()
			).apply(
				GroupByKey.create()
			);

		eventsPCollection.apply("Write Events", new EventBigQueryWriter());
		eventsPCollection.apply(
			"Write Event Properties", new EventPropertyBigQueryWriter());
		sessionsPCollection.apply(
			"Write Sessions", new SessionBigQueryWriter());

		return pipeline.run();
	}

	public static class AnalyticsEventExtractor
		extends DoFn<AnalyticsEvent, AnalyticsEvent> {

		@ProcessElement
		public void processElement(ProcessContext processContext) {
			AnalyticsEvent analyticsEvent = processContext.element();

			AnalyticsEvent extractedAnalyticsEvent = new AnalyticsEvent(
				analyticsEvent);

			Map<String, String> context = _getExtractedContext(analyticsEvent);

			if (Boolean.parseBoolean(context.getOrDefault("crawler", null))) {
				return;
			}

			extractedAnalyticsEvent.context = context;
			extractedAnalyticsEvent.eventProperties = _getSafeMap(
				analyticsEvent.eventProperties);

			processContext.output(extractedAnalyticsEvent);
		}

		private Map<String, String> _getExtractedContext(
			AnalyticsEvent analyticsEvent) {

			Map<String, String> context = _getSafeMap(analyticsEvent.context);

			BrowscapDevice browscapDevice = BrowscapEngine.getDevice(
				context.get("userAgent"));

			if (browscapDevice != null) {
				Map<String, String> convertedValues =
					ObjectMapperUtil.convertValue(Map.class, browscapDevice);

				context.putAll(convertedValues);
			}

			IPInfo ipInfo = IPGeocoder.getIPInfo(analyticsEvent.clientIP);

			if (ipInfo != null) {
				Map<String, String> convertedValues =
					ObjectMapperUtil.convertValue(Map.class, ipInfo);

				context.putAll(convertedValues);
			}

			if (StringUtils.isBlank(context.get("canonicalUrl"))) {
				context.put("canonicalUrl", context.get("url"));
			}

			String screenHeight = context.get("screenHeight");

			if (screenHeight != null) {
				context.put("screenHeightSize", screenHeight);
			}

			String screenWidth = context.get("screenWidth");

			if (screenWidth != null) {
				context.put("screenWidthSize", screenWidth);
			}

			return context;
		}

		private Map<String, String> _getSafeMap(Map<String, String> map) {
			Map<String, String> safeMap = new HashMap<>();

			for (Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();

				if (value == null) {
					value = "";
				}

				safeMap.put(entry.getKey(), value);
			}

			return safeMap;
		}

	}

	public static class AnalyticsEventParser
		extends DoFn<String, AnalyticsEvent> {

		@ProcessElement
		public void processElement(ProcessContext processContext) {
			try {
				String element = processContext.element();

				boolean newlineDetected = false;

				if (StringUtils.isNotBlank(element) &&
					element.contains("\\n")) {

					newlineDetected = true;
				}

				AnalyticsEvent analyticsEvent = ObjectMapperUtil.readValue(
					AnalyticsEvent.class, element);

				if (newlineDetected && _logger.isInfoEnabled()) {
					_logger.info(
						"Detected newline in event message for " +
							analyticsEvent.projectId);
				}

				processContext.output(analyticsEvent);
			}
			catch (Exception exception) {
				_logger.error(
					"Unable to parse analytics event message {}",
					processContext.element());
			}
		}

		private static final Logger _logger = LoggerFactory.getLogger(
			AnalyticsEventParser.class);

	}

	public static class AnalyticsEventsTableRowMapper
		extends DoFn<KV<String, Iterable<AnalyticsEvent>>, TableRow> {

		@ProcessElement
		public void process(
			IntervalWindow intervalWindow, PaneInfo paneInfo,
			ProcessContext processContext) {

			KV<String, Iterable<AnalyticsEvent>> element =
				processContext.element();

			String sessionId = DigestUtils.sha256Hex(
				element.getKey() + "#" + intervalWindow.start());

			for (AnalyticsEvent analyticsEvent : element.getValue()) {
				_outputEventTableRow(analyticsEvent, processContext, sessionId);
			}
		}

	}

	public static class BackupPubsubMessages extends PTransform<PBegin, PDone> {

		public BackupPubsubMessages(
			String inputSubscription, String outputDirectory,
			String outputFileNamePrefix, long writeIntervalInMinutes) {

			_inputSubscription = inputSubscription;
			_outputDirectory = outputDirectory;
			_outputFileNamePrefix = outputFileNamePrefix;
			_writeIntervalInMinutes = writeIntervalInMinutes;
		}

		@Override
		public PDone expand(PBegin pBegin) {
			return pBegin.apply(
				PubsubIO.readStrings(
				).fromSubscription(
					String.format("%s_backup", _inputSubscription)
				)
			).apply(
				_writeIntervalInMinutes + " Minutes Window",
				Window.into(
					FixedWindows.of(
						Duration.standardMinutes(_writeIntervalInMinutes)))
			).apply(
				"Write Files",
				new WriteToText.WriteOneFilePerWindow(
					_outputFileNamePrefix, _outputDirectory)
			);
		}

		private final String _inputSubscription;
		private final String _outputDirectory;
		private final String _outputFileNamePrefix;
		private final long _writeIntervalInMinutes;

	}

	public static class EventBigQueryWriter
		extends PTransform
			<PCollection<KV<String, Iterable<AnalyticsEvent>>>, WriteResult> {

		@Override
		public WriteResult expand(
			PCollection<KV<String, Iterable<AnalyticsEvent>>> pCollection) {

			return pCollection.apply(
				"Create Event Table Rows",
				ParDo.of(new AnalyticsEventsTableRowMapper())
			).apply(
				"Write Event Rows to Big Query",
				BigQueryIO.writeTableRows(
				).to(
					new SerializableFunction
						<ValueInSingleWindow<TableRow>, TableDestination>() {

						@Override
						public TableDestination apply(
							ValueInSingleWindow<TableRow> valueInSingleWindow) {

							TableRow tableRow = valueInSingleWindow.getValue();

							return new TableDestination(
								String.format(
									"%s.%s", tableRow.get("projectId"),
									"event"),
								null);
						}

					}
				).withCreateDisposition(
					BigQueryIO.Write.CreateDisposition.CREATE_NEVER
				).withMethod(
					BigQueryIO.Write.Method.STREAMING_INSERTS
				).withWriteDisposition(
					BigQueryIO.Write.WriteDisposition.WRITE_APPEND
				).withoutValidation()
			);
		}

	}

	public static class EventPropertyBigQueryWriter
		extends PTransform
			<PCollection<KV<String, Iterable<AnalyticsEvent>>>, WriteResult> {

		@Override
		public WriteResult expand(
			PCollection<KV<String, Iterable<AnalyticsEvent>>> pCollection) {

			return pCollection.apply(
				"Create Event Properties Table Rows",
				ParDo.of(
					new DoFn<KV<String, Iterable<AnalyticsEvent>>, TableRow>() {

						@ProcessElement
						public void process(ProcessContext processContext) {
							KV<String, Iterable<AnalyticsEvent>> element =
								processContext.element();

							for (AnalyticsEvent analyticsEvent :
									element.getValue()) {

								_outputEventPropertyTableRows(
									analyticsEvent, processContext);
							}
						}

					})
			).apply(
				"Write Event Property Rows to Big Query",
				BigQueryIO.writeTableRows(
				).to(
					new SerializableFunction
						<ValueInSingleWindow<TableRow>, TableDestination>() {

						@Override
						public TableDestination apply(
							ValueInSingleWindow<TableRow> valueInSingleWindow) {

							TableRow tableRow = valueInSingleWindow.getValue();

							return new TableDestination(
								String.format(
									"%s.%s", tableRow.get("projectId"),
									"eventproperty"),
								null);
						}

					}
				).withCreateDisposition(
					BigQueryIO.Write.CreateDisposition.CREATE_NEVER
				).withMethod(
					BigQueryIO.Write.Method.STREAMING_INSERTS
				).withWriteDisposition(
					BigQueryIO.Write.WriteDisposition.WRITE_APPEND
				).withoutValidation()
			);
		}

	}

	public static class SessionBigQueryWriter
		extends PTransform
			<PCollection<KV<String, Iterable<AnalyticsEvent>>>, WriteResult> {

		@Override
		public WriteResult expand(
			PCollection<KV<String, Iterable<AnalyticsEvent>>> pCollection) {

			return pCollection.apply(
				"Create Session Table Rows",
				ParDo.of(
					new DoFn<KV<String, Iterable<AnalyticsEvent>>, TableRow>() {

						@ProcessElement
						public void process(
							IntervalWindow intervalWindow, PaneInfo paneInfo,
							ProcessContext processContext) {

							if (paneInfo.isLast()) {
								_outputSessionTableRow(
									intervalWindow, processContext);
							}
						}

					})
			).apply(
				"Write Session Rows to Big Query",
				BigQueryIO.writeTableRows(
				).to(
					new SerializableFunction
						<ValueInSingleWindow<TableRow>, TableDestination>() {

						@Override
						public TableDestination apply(
							ValueInSingleWindow<TableRow> valueInSingleWindow) {

							TableRow tableRow = valueInSingleWindow.getValue();

							return new TableDestination(
								String.format(
									"%s.%s", tableRow.get("projectId"),
									"session"),
								null);
						}

					}
				).withCreateDisposition(
					BigQueryIO.Write.CreateDisposition.CREATE_NEVER
				).withMethod(
					BigQueryIO.Write.Method.STREAMING_INSERTS
				).withWriteDisposition(
					BigQueryIO.Write.WriteDisposition.WRITE_APPEND
				).withoutValidation()
			);
		}

	}

	private static Window<KV<String, AnalyticsEvent>> _buildWindow(
		EventIngestionPipelineOptions eventIngestionPipelineOptions) {

		return Window.<KV<String, AnalyticsEvent>>into(
			Sessions.withGapDuration(
				Duration.standardMinutes(
					eventIngestionPipelineOptions.
						getSessionWindowGapDuration()))
		).withAllowedLateness(
			Duration.standardMinutes(
				eventIngestionPipelineOptions.
					getSessionWindowAllowedLateness()),
			Window.ClosingBehavior.FIRE_ALWAYS
		).withTimestampCombiner(
			TimestampCombiner.END_OF_WINDOW
		);
	}

	private static WithKeys<String, AnalyticsEvent> _buildWithKeysPTransform() {
		WithKeys<String, AnalyticsEvent> withKeys = WithKeys.of(
			new SerializableFunction<AnalyticsEvent, String>() {

				@Override
				public String apply(AnalyticsEvent analyticsEvent) {
					ZonedDateTime utcZonedDateTime =
						DateUtil.toUTCZonedDateTime(analyticsEvent.eventDate);

					ZonedDateTime projectZonedDateTime =
						utcZonedDateTime.withZoneSameInstant(
							ZoneId.of(analyticsEvent.projectTimeZoneId));

					return String.format(
						"%s#%s#%s#%s#%s", analyticsEvent.projectId,
						analyticsEvent.dataSourceId, analyticsEvent.channelId,
						analyticsEvent.userId,
						projectZonedDateTime.toLocalDate());
				}

			});

		return withKeys.withKeyType(TypeDescriptor.of(String.class));
	}

	private static String _formatFieldValue(String fieldValue) {
		return StringUtils.replaceAll(
			StringUtils.trim(fieldValue), "\\\\", "\\\\\\\\");
	}

	private static String _getAssetId(AnalyticsEvent analyticsEvent) {
		if (Objects.equals(analyticsEvent.applicationId, "Page")) {
			Map<String, String> context = analyticsEvent.context;

			return context.get("canonicalUrl");
		}

		String assetIdFieldName = _applicationAssetIdFieldNames.get(
			analyticsEvent.applicationId);

		if (assetIdFieldName != null) {
			Map<String, String> eventProperties =
				analyticsEvent.eventProperties;

			return eventProperties.get(assetIdFieldName);
		}

		return null;
	}

	private static String _getAssetTitle(AnalyticsEvent analyticsEvent) {
		if (Objects.equals(analyticsEvent.applicationId, "Page")) {
			Map<String, String> context = analyticsEvent.context;

			return context.get("title");
		}

		if (_applicationAssetIdFieldNames.containsKey(
				analyticsEvent.applicationId)) {

			Map<String, String> eventProperties =
				analyticsEvent.eventProperties;

			return eventProperties.get("title");
		}

		return null;
	}

	private static void _outputEventPropertyTableRows(
		AnalyticsEvent analyticsEvent, DoFn.ProcessContext processContext) {

		Map<String, String> eventProperties = analyticsEvent.eventProperties;

		for (Map.Entry<String, String> entry : eventProperties.entrySet()) {
			TableRow tableRow = new TableRow();

			tableRow.set("channelId", Long.parseLong(analyticsEvent.channelId));
			tableRow.set("eventDate", analyticsEvent.eventDate);
			tableRow.set("id", analyticsEvent.id);
			tableRow.set("name", entry.getKey());
			tableRow.set("projectId", analyticsEvent.projectId);
			tableRow.set("value", entry.getValue());

			processContext.output(tableRow);
		}
	}

	private static void _outputEventTableRow(
		AnalyticsEvent analyticsEvent, DoFn.ProcessContext processContext,
		String sessionId) {

		TableRow tableRow = new TableRow();

		tableRow.set("applicationId", analyticsEvent.applicationId);
		tableRow.set("assetId", _formatFieldValue(_getAssetId(analyticsEvent)));
		tableRow.set(
			"assetTitle", _formatFieldValue(_getAssetTitle(analyticsEvent)));

		Map<String, String> context = analyticsEvent.context;

		tableRow.set("browserName", context.get("browserName"));
		tableRow.set("canonicalUrl", context.get("canonicalUrl"));

		tableRow.set("channelId", Long.parseLong(analyticsEvent.channelId));
		tableRow.set("city", context.get("city"));
		tableRow.set("contentLanguageId", context.get("contentLanguageId"));
		tableRow.set("context", ObjectMapperUtil.writeValueAsString(context));
		tableRow.set("country", context.get("country"));
		tableRow.set("createDate", analyticsEvent.createDate);
		tableRow.set(
			"dataSourceId", Long.parseLong(analyticsEvent.dataSourceId));
		tableRow.set(
			"description", _formatFieldValue(context.get("description")));
		tableRow.set("deviceType", context.get("deviceType"));

		if (StringUtils.isNotBlank(analyticsEvent.emailAddressHashed)) {
			tableRow.set(
				"emailAddressHashed", analyticsEvent.emailAddressHashed);
		}

		tableRow.set("eventDate", analyticsEvent.eventDate);
		tableRow.set("eventId", analyticsEvent.eventId);
		tableRow.set(
			"eventProperties",
			ObjectMapperUtil.writeValueAsString(
				analyticsEvent.eventProperties));
		tableRow.set("experienceId", context.get("experienceId"));

		String experimentId = context.get("experimentId");

		if (StringUtils.isNotBlank(experimentId)) {
			tableRow.set("experimentId", Long.parseLong(experimentId));
		}

		tableRow.set("id", analyticsEvent.id);
		tableRow.set("keywords", _formatFieldValue(context.get("keywords")));
		tableRow.set("languageId", context.get("languageId"));
		tableRow.set("platformName", context.get("platformName"));
		tableRow.set("projectId", analyticsEvent.projectId);
		tableRow.set("projectTimeZoneId", analyticsEvent.projectTimeZoneId);
		tableRow.set("referrer", context.get("referrer"));
		tableRow.set("region", context.get("region"));
		tableRow.set("sessionId", sessionId);
		tableRow.set("timezoneOffset", context.get("timezoneOffset"));
		tableRow.set("title", _formatFieldValue(context.get("title")));
		tableRow.set("url", context.get("url"));
		tableRow.set("userId", analyticsEvent.userId);
		tableRow.set("variantId", context.get("variantId"));

		processContext.output(tableRow);
	}

	private static void _outputSessionTableRow(
		IntervalWindow intervalWindow, DoFn.ProcessContext processContext) {

		TableRow tableRow = new TableRow();

		KV<String, Iterable<AnalyticsEvent>> element =
			(KV<String, Iterable<AnalyticsEvent>>)processContext.element();

		String sessionKey = element.getKey();

		String[] sessionKeyParts = sessionKey.split("#");

		List<AnalyticsEvent> analyticsEvents = Lists.newArrayList(
			element.getValue());

		analyticsEvents.sort(
			Comparator.comparing(
				analyticsEvent -> DateUtil.toUTCZonedDateTime(
					analyticsEvent.eventDate)));

		AnalyticsEvent firstAnalyticsEvent = analyticsEvents.get(0);
		AnalyticsEvent lastAnalyticsEvent = analyticsEvents.get(
			analyticsEvents.size() - 1);

		Map<String, String> context = firstAnalyticsEvent.context;

		Acquisition acquisition = new Acquisition(
			context.get("referrer"), context.get("url"));

		tableRow.set("acquisitionCampaign", acquisition.getCampaign());
		tableRow.set("acquisitionChannel", acquisition.getChannel());
		tableRow.set("acquisitionContent", acquisition.getContent());
		tableRow.set("acquisitionMedium", acquisition.getMedium());
		tableRow.set("acquisitionSource", acquisition.getSource());
		tableRow.set("acquisitionTerm", acquisition.getTerm());

		int interactionsCount = 0;
		int pageViewsCount = 0;
		Set<String> referrers = new HashSet();
		Set<String> urls = new HashSet();

		for (AnalyticsEvent analyticsEvent : analyticsEvents) {
			String eventId = analyticsEvent.eventId;

			if (analyticsEvent.applicationId.equals("Page") &&
				eventId.equals("pageViewed")) {

				pageViewsCount++;
			}

			if (!eventId.equals("blogViewed") &&
				!eventId.equals("documentPreviewed") &&
				!eventId.equals("formViewed") &&
				!eventId.equals("pageLoaded") &&
				!eventId.equals("pageUnloaded") &&
				!eventId.equals("pageViewed") &&
				!eventId.equals("webContentViewed")) {

				interactionsCount++;
			}

			Map<String, String> analyticsEventContext = analyticsEvent.context;

			String referrer = analyticsEventContext.get("referrer");

			if (StringUtils.isNotEmpty(referrer)) {
				referrers.add(referrer);
			}

			String url = analyticsEventContext.get("url");

			if (StringUtils.isNotEmpty(url)) {
				urls.add(url);
			}
		}

		if ((interactionsCount < 1) && (pageViewsCount < 2)) {
			tableRow.set("bounce", 1);
		}
		else {
			tableRow.set("bounce", 0);
		}

		tableRow.set("browserName", context.get("browserName"));
		tableRow.set("channelId", Long.parseLong(sessionKeyParts[2]));
		tableRow.set("city", context.get("city"));
		tableRow.set("country", context.get("country"));
		tableRow.set("deviceType", context.get("deviceType"));
		tableRow.set(
			"duration",
			DateUtil.getDeltaMilliseconds(
				firstAnalyticsEvent.eventDate, lastAnalyticsEvent.eventDate));
		tableRow.set(
			"id",
			DigestUtils.sha256Hex(sessionKey + "#" + intervalWindow.start()));
		tableRow.set("platformName", context.get("platformName"));
		tableRow.set("projectId", sessionKeyParts[0]);
		tableRow.set("referrers", referrers);
		tableRow.set("region", context.get("region"));
		tableRow.set("sessionEnd", lastAnalyticsEvent.eventDate);
		tableRow.set("sessionStart", firstAnalyticsEvent.eventDate);
		tableRow.set("urls", urls);
		tableRow.set("userId", sessionKeyParts[3]);

		processContext.output(tableRow);
	}

	private static final Map<String, String> _applicationAssetIdFieldNames =
		new HashMap<String, String>() {
			{
				put("Blog", "entryId");
				put("Comment", "classPK");
				put("Custom", "assetId");
				put("Document", "fileEntryId");
				put("Form", "formId");
				put("Ratings", "classPK");
				put("WebContent", "articleId");
			}
		};

}