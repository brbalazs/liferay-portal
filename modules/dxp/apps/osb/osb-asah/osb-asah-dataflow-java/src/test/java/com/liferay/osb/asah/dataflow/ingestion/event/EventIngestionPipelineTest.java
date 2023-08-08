/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event;

import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.beam.sdk.coders.IterableCoder;
import org.apache.beam.sdk.coders.KvCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.extensions.avro.coders.AvroCoder;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.testing.TestStream;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TimestampedValue;

import org.joda.time.Duration;
import org.joda.time.Instant;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Leslie Wong
 */
public class EventIngestionPipelineTest {

	@Test
	public void testAnalyticsEventExtractor() {
		List<AnalyticsEvent> testAnalyticsEvents = new ArrayList<>();

		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"", _USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/web/guest/home/"));
		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"https://liferay.com", _USER_AGENT_MAC_OS_CHROME_DESKTOP,
				"https://liferay.com/web/guest/home/"));
		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"https://liferay.com", _USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/test-page-1/"));
		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"", _USER_AGENT_ANDROID_CHROME_MOBILE,
				"https://liferay.com/test-page-2/"));
		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"https://liferay.com", _USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/test-page-3/"));
		testAnalyticsEvents.add(
			_createTestAnalyticsEvent(
				"", _USER_AGENT_CRAWLER, "https://liferay.com/test-page-3/"));

		PCollection<AnalyticsEvent> pCollection = testPipeline.apply(
			"Create Test Batch",
			Create.of(
				testAnalyticsEvents
			).withCoder(
				AvroCoder.of(AnalyticsEvent.class)
			)
		).apply(
			"Extract Geolocation/Device Information",
			ParDo.of(new EventIngestionPipeline.AnalyticsEventExtractor())
		);

		List<AnalyticsEvent> expectedAnalyticsEvents = new ArrayList<>();

		expectedAnalyticsEvents.add(
			_createExpectedAnalyticsEvent(
				"Firefox", "https://liferay.com/web/guest/home/", "Desktop",
				"macOS", _USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/web/guest/home/"));
		expectedAnalyticsEvents.add(
			_createExpectedAnalyticsEvent(
				"Chrome", "https://liferay.com", "Desktop", "macOS",
				_USER_AGENT_MAC_OS_CHROME_DESKTOP,
				"https://liferay.com/web/guest/home/"));
		expectedAnalyticsEvents.add(
			_createExpectedAnalyticsEvent(
				"Firefox", "https://liferay.com", "Desktop", "macOS",
				_USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/test-page-1/"));
		expectedAnalyticsEvents.add(
			_createExpectedAnalyticsEvent(
				"Chrome", "https://liferay.com/test-page-2/", "SmartPhone",
				"Android", _USER_AGENT_ANDROID_CHROME_MOBILE,
				"https://liferay.com/test-page-2/"));
		expectedAnalyticsEvents.add(
			_createExpectedAnalyticsEvent(
				"Firefox", "https://liferay.com", "Desktop", "macOS",
				_USER_AGENT_MAC_OS_FIREFOX_DESKTOP,
				"https://liferay.com/test-page-3/"));

		PAssert.that(
			pCollection
		).containsInAnyOrder(
			expectedAnalyticsEvents
		);

		testPipeline.run();
	}

	@Test
	public void testAnalyticsEventParser() throws IOException {
		String analyticsEventsJSON = _readResourceAsString(
			"dependencies/test_analytics_event_parser.json");

		PCollection<AnalyticsEvent> pCollection = testPipeline.apply(
			Create.of(analyticsEventsJSON)
		).apply(
			"Parse Analytics Events",
			ParDo.of(new EventIngestionPipeline.AnalyticsEventParser())
		);

		AnalyticsEvent analyticsEvent = _createAnalyticsEvent(
			"Page", "123", null,
			new HashMap<String, String>() {
				{
					put(
						"description",
						"This is the Liferay Analytics JS client test page.");
					put("keywords", "Liferay, Analytics, Test, Javascript");
					put("languageId", "en-US");
					put("title", "Liferay Analytics JS Client Test Page");
					put("url", "http://www.liferay.com");
					put("userAgent", _USER_AGENT_MAC_OS_FIREFOX_DESKTOP);
				}
			},
			"2017-11-10T09:36:00.365Z", "350121114030678021", null,
			"2017-11-10T09:34:45.345Z", "pageViewed",
			Collections.singletonMap("referrer", "http://www.google.com"), "1",
			"test", "UTC", "aedfa915-c7a1-4309-abcf-024e247d414c");

		PAssert.that(
			pCollection
		).containsInAnyOrder(
			Collections.singletonList(analyticsEvent)
		);

		testPipeline.run();
	}

	@Test
	public void testAnalyticsEventsTableRowMapper() {
		Map<String, String> context = Collections.singletonMap(
			"title", "\n\n\n\tLiferay \\Analytics JS Client Test Page\n\n\t");
		Map<String, String> properties = Collections.singletonMap(
			"referrer", "http://www.google.com");

		AnalyticsEvent analyticsEvent = _createAnalyticsEvent(
			"Page", "123", "", context, "2017-11-10T09:36:00.365Z",
			"350121114030678021", "", "2017-11-10T09:34:45.345Z", "pageViewed",
			properties, "1", "test", "UTC",
			"aedfa915-c7a1-4309-abcf-024e247d414c");

		Instant baseTime = new Instant(0);

		PCollection<TableRow> pCollection = testPipeline.apply(
			TestStream.create(
				KvCoder.of(
					StringUtf8Coder.of(),
					IterableCoder.of(AvroCoder.of(AnalyticsEvent.class)))
			).advanceWatermarkTo(
				baseTime
			).addElements(
				TimestampedValue.of(
					KV.of(
						"session-1", Collections.singletonList(analyticsEvent)),
					baseTime.plus(Duration.standardSeconds(30)))
			).advanceWatermarkToInfinity()
		).apply(
			Window.into(FixedWindows.of(Duration.standardMinutes(3L)))
		).apply(
			"Create Event Table Rows",
			ParDo.of(new EventIngestionPipeline.AnalyticsEventsTableRowMapper())
		);

		IntervalWindow intervalWindow = new IntervalWindow(
			new Instant(0), new Instant(180000));

		TableRow tableRow = new TableRow();

		tableRow.set("applicationId", analyticsEvent.applicationId);
		tableRow.set("assetId", null);
		tableRow.set("assetTitle", "Liferay \\\\Analytics JS Client Test Page");
		tableRow.set("browserName", null);
		tableRow.set("canonicalUrl", null);
		tableRow.set("channelId", 123L);
		tableRow.set("city", null);
		tableRow.set("contentLanguageId", null);
		tableRow.set("context", ObjectMapperUtil.writeValueAsString(context));
		tableRow.set("country", null);
		tableRow.set("createDate", "2017-11-10T09:36:00.365Z");
		tableRow.set("dataSourceId", 350121114030678021L);
		tableRow.set("description", null);
		tableRow.set("deviceType", null);
		tableRow.set("eventDate", "2017-11-10T09:34:45.345Z");
		tableRow.set("eventId", "pageViewed");
		tableRow.set(
			"eventProperties", ObjectMapperUtil.writeValueAsString(properties));
		tableRow.set("experienceId", null);
		tableRow.set("id", "1");
		tableRow.set("keywords", null);
		tableRow.set("languageId", null);
		tableRow.set("platformName", null);
		tableRow.set("projectId", "test");
		tableRow.set("projectTimeZoneId", "UTC");
		tableRow.set("referrer", null);
		tableRow.set("region", null);
		tableRow.set(
			"sessionId",
			"b7dbcc73168d4f5ce2ce1836250a1253cda62a46619b1e7da5d763c63d2543af");
		tableRow.set("timezoneOffset", null);
		tableRow.set("title", "Liferay \\\\Analytics JS Client Test Page");
		tableRow.set("url", null);
		tableRow.set("userId", "aedfa915-c7a1-4309-abcf-024e247d414c");
		tableRow.set("variantId", null);

		PAssert.that(
			pCollection
		).inWindow(
			intervalWindow
		).containsInAnyOrder(
			Collections.singletonList(tableRow)
		);

		testPipeline.run(
		).waitUntilFinish();
	}

	@Test
	public void testEventBigQueryTableRowParserWithEscapedCharacters()
		throws IOException {

		String analyticsEventsJSON = _readResourceAsString(
			"dependencies/test_analytics_event_parser_escape_characters.json");

		Instant baseTime = new Instant(0);

		PCollection<TableRow> pCollection = testPipeline.apply(
			TestStream.create(
				StringUtf8Coder.of()
			).advanceWatermarkTo(
				baseTime
			).addElements(
				TimestampedValue.of(
					analyticsEventsJSON,
					baseTime.plus(Duration.standardSeconds(30)))
			).advanceWatermarkToInfinity()
		).apply(
			"Parse Analytics Events",
			ParDo.of(new EventIngestionPipeline.AnalyticsEventParser())
		).apply(
			"Add Session Key", WithKeys.of("session-1")
		).apply(
			Window.into(FixedWindows.of(Duration.standardMinutes(3L)))
		).apply(
			GroupByKey.create()
		).apply(
			"Create Event Table Rows",
			ParDo.of(new EventIngestionPipeline.AnalyticsEventsTableRowMapper())
		);

		Map<String, String> context = Collections.singletonMap(
			"title", "\n\n\n\tLiferay \\Analytics JS Client Test Page\n\n\t");

		Map<String, String> properties = Collections.singletonMap(
			"referrer", "http://www.google.com");

		AnalyticsEvent analyticsEvent = _createAnalyticsEvent(
			"Page", "123", "", context, "2017-11-10T09:36:00.365Z",
			"350121114030678021", "", "2017-11-10T09:34:45.345Z", "pageViewed",
			properties, "1", "test", "UTC",
			"aedfa915-c7a1-4309-abcf-024e247d414c");

		TableRow tableRow = new TableRow();

		tableRow.set("applicationId", analyticsEvent.applicationId);
		tableRow.set("assetId", null);
		tableRow.set("assetTitle", "Liferay \\\\Analytics JS Client Test Page");
		tableRow.set("browserName", null);
		tableRow.set("canonicalUrl", null);
		tableRow.set("channelId", 123L);
		tableRow.set("city", null);
		tableRow.set("contentLanguageId", null);
		tableRow.set("context", ObjectMapperUtil.writeValueAsString(context));
		tableRow.set("country", null);
		tableRow.set("createDate", "2017-11-10T09:36:00.365Z");
		tableRow.set("dataSourceId", 350121114030678021L);
		tableRow.set("description", null);
		tableRow.set("deviceType", null);
		tableRow.set("eventDate", "2017-11-10T09:34:45.345Z");
		tableRow.set("eventId", "pageViewed");
		tableRow.set(
			"eventProperties", ObjectMapperUtil.writeValueAsString(properties));
		tableRow.set("experienceId", null);
		tableRow.set("id", "1");
		tableRow.set("keywords", null);
		tableRow.set("languageId", null);
		tableRow.set("platformName", null);
		tableRow.set("projectId", "test");
		tableRow.set("projectTimeZoneId", "UTC");
		tableRow.set("referrer", null);
		tableRow.set("region", null);
		tableRow.set(
			"sessionId",
			"b7dbcc73168d4f5ce2ce1836250a1253cda62a46619b1e7da5d763c63d2543af");
		tableRow.set("timezoneOffset", null);
		tableRow.set("title", "Liferay \\\\Analytics JS Client Test Page");
		tableRow.set("url", null);
		tableRow.set("userId", "aedfa915-c7a1-4309-abcf-024e247d414c");
		tableRow.set("variantId", null);

		IntervalWindow intervalWindow = new IntervalWindow(
			new Instant(0), new Instant(180000));

		PAssert.that(
			pCollection
		).inWindow(
			intervalWindow
		).containsInAnyOrder(
			Collections.singletonList(tableRow)
		);

		testPipeline.run(
		).waitUntilFinish();
	}

	@Rule
	public final transient TestPipeline testPipeline = TestPipeline.create();

	private AnalyticsEvent _createAnalyticsEvent(
		String applicationId, String channelId, String clientIP,
		Map<String, String> context, String createDate, String dataSourceId,
		String emailAddressHashed, String eventDate, String eventId,
		Map<String, String> eventProperties, String id, String projectId,
		String projectTimeZoneId, String userId) {

		AnalyticsEvent analyticsEvent = new AnalyticsEvent();

		analyticsEvent.applicationId = applicationId;
		analyticsEvent.channelId = channelId;
		analyticsEvent.clientIP = clientIP;
		analyticsEvent.context = context;
		analyticsEvent.createDate = createDate;
		analyticsEvent.dataSourceId = dataSourceId;
		analyticsEvent.emailAddressHashed = emailAddressHashed;
		analyticsEvent.eventDate = eventDate;
		analyticsEvent.eventId = eventId;
		analyticsEvent.eventProperties = eventProperties;
		analyticsEvent.id = id;
		analyticsEvent.projectId = projectId;
		analyticsEvent.projectTimeZoneId = projectTimeZoneId;
		analyticsEvent.userId = userId;

		return analyticsEvent;
	}

	private AnalyticsEvent _createExpectedAnalyticsEvent(
		String browserName, String canonicalUrl, String deviceType,
		String platformName, String userAgent, String url) {

		Map<String, String> context = new HashMap<>();

		context.put("browserName", browserName);
		context.put("canonicalUrl", canonicalUrl);
		context.put("crawler", "false");
		context.put("deviceType", deviceType);
		context.put("platformName", platformName);
		context.put("url", url);
		context.put("userAgent", userAgent);

		return _createAnalyticsEvent(
			"", "", "", context, "", "", "", "", "", Collections.emptyMap(), "",
			"", "", "");
	}

	private AnalyticsEvent _createTestAnalyticsEvent(
		String canonicalUrl, String userAgent, String url) {

		Map<String, String> context = new HashMap<>();

		context.put("canonicalUrl", canonicalUrl);
		context.put("url", url);
		context.put("userAgent", userAgent);

		return _createAnalyticsEvent(
			"", "", "", context, "", "", "", "", "", Collections.emptyMap(), "",
			"", "", "");
	}

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

	private static final String _USER_AGENT_ANDROID_CHROME_MOBILE =
		"Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) " +
			"AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 " +
				"Mobile Safari/535.19";

	private static final String _USER_AGENT_CRAWLER = "Googlebot";

	private static final String _USER_AGENT_MAC_OS_CHROME_DESKTOP =
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 " +
			"(KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

	private static final String _USER_AGENT_MAC_OS_FIREFOX_DESKTOP =
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:45.0) " +
			"Gecko/20100101 Firefox/45.0";

}