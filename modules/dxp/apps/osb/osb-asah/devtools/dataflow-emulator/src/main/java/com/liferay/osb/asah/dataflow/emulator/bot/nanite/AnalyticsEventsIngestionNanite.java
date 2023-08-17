/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.model.Acquisition;
import com.liferay.osb.asah.common.model.AnalyticsEvent;
import com.liferay.osb.asah.common.repository.BQEventPropertyRepository;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.common.util.MapUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.dataflow.emulator.browscap.BrowscapDevice;
import com.liferay.osb.asah.dataflow.emulator.browscap.BrowscapEngine;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class AnalyticsEventsIngestionNanite {

	public void checkWatermark() {

		// Data driven watermark was not advancing for the last minute

		if ((System.currentTimeMillis() - _watermarkModifiedTimestamp) <
				DateUtil.MINUTE) {

			return;
		}

		_advanceWatermark(System.currentTimeMillis());
	}

	public void closeOpenSessions() {
		for (Map.Entry<String, SessionContext> entry : _sessions.entrySet()) {
			SessionContext sessionContext = entry.getValue();

			if ((_watermarkTimestamp - sessionContext.sessionEnd.getTime()) >=
					0) {

				if (_log.isInfoEnabled()) {
					_log.info(
						String.format(
							"Closing session {%s, %s}", sessionContext.userId,
							sessionContext.id));
				}

				ProjectIdThreadLocal.setProjectId(sessionContext.projectId);

				_writeBQSession(sessionContext);

				_sessions.remove(entry.getKey());
			}
		}
	}

	public void run() throws Exception {
		while (true) {
			List<Message<String>> messages = _messageSubscriber.pullMessages(
				100, String::valueOf);

			if (messages.isEmpty()) {
				break;
			}

			List<AnalyticsEvent> analyticsEvents = _parseMessages(messages);

			Stream<AnalyticsEvent> stream = analyticsEvents.stream();

			stream.collect(
				Collectors.groupingBy(
					analyticsEvent -> analyticsEvent.getProjectId())
			).forEach(
				(projectId, analyticsEventsList) -> {
					ProjectIdThreadLocal.setProjectId(projectId);

					if (!_projectIds.contains(projectId)) {
						_projectIds.add(projectId);
					}

					_processAnalyticsEvents(analyticsEvents);
				}
			);

			_advanceWatermark(analyticsEvents);

			_acknowledgeMessages(messages);
		}
	}

	private void _acknowledgeMessages(List<Message<String>> messages) {
		Stream<Message<String>> stream = messages.stream();

		_messageSubscriber.sendAckIds(
			stream.map(
				Message::getAckId
			).collect(
				Collectors.toList()
			));
	}

	private void _advanceWatermark(List<AnalyticsEvent> analyticsEvents) {
		Stream<AnalyticsEvent> stream = analyticsEvents.stream();

		Optional<Date> lastEventDateOptional = stream.map(
			AnalyticsEvent::getEventDate
		).max(
			Date::compareTo
		);

		if (!lastEventDateOptional.isPresent()) {
			return;
		}

		Date lastEventDate = lastEventDateOptional.get();

		_advanceWatermark(lastEventDate.getTime());
	}

	private void _advanceWatermark(long lastEventDateTimestamp) {
		long newWatermarkTimestamp =
			lastEventDateTimestamp - (_allowedLateness * DateUtil.MINUTE);

		if (newWatermarkTimestamp > _watermarkTimestamp) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Advancing watermark to " +
						new Date(newWatermarkTimestamp));
			}

			_watermarkTimestamp = newWatermarkTimestamp;
			_watermarkModifiedTimestamp = System.currentTimeMillis();
		}
	}

	private String _formatFieldValue(String fieldValue) {
		return StringUtils.replaceAll(
			StringUtils.trim(fieldValue), "\\\\", "\\\\\\\\");
	}

	private String _getAssetId(AnalyticsEvent analyticsEvent) {
		if (Objects.equals(analyticsEvent.getApplicationId(), "Page")) {
			Map<String, String> context = analyticsEvent.getContext();

			return context.get("canonicalUrl");
		}

		String assetIdFieldName = _applicationAssetIdFieldNames.get(
			analyticsEvent.getApplicationId());

		if (assetIdFieldName != null) {
			Map<String, String> eventProperties =
				analyticsEvent.getEventProperties();

			return eventProperties.get(assetIdFieldName);
		}

		return null;
	}

	private String _getAssetTitle(AnalyticsEvent analyticsEvent) {
		if (Objects.equals(analyticsEvent.getApplicationId(), "Page")) {
			Map<String, String> context = analyticsEvent.getContext();

			return context.get("title");
		}

		if (_applicationAssetIdFieldNames.containsKey(
				analyticsEvent.getApplicationId())) {

			Map<String, String> eventProperties =
				analyticsEvent.getEventProperties();

			return eventProperties.get("title");
		}

		return null;
	}

	private Map<String, String> _getContext(AnalyticsEvent analyticsEvent) {
		Map<String, String> context = new HashMap<>();

		Map<String, String> analyticsEventsContext =
			analyticsEvent.getContext();

		for (Map.Entry<String, String> entry :
				analyticsEventsContext.entrySet()) {

			context.put(entry.getKey(), entry.getValue());
		}

		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			MapUtil.getString(context, "userAgent"));

		if (browscapDevice != null) {
			Map<String, String> convertedValues = _objectMapper.convertValue(
				browscapDevice, Map.class);

			context.putAll(convertedValues);
		}

		if (StringUtils.isBlank(MapUtil.getString(context, "canonicalUrl"))) {
			context.put("canonicalUrl", MapUtil.getString(context, "url"));
		}

		String screenHeight = MapUtil.getString(context, "screenHeight");

		if (screenHeight != null) {
			context.put("screenHeightSize", screenHeight);
		}

		String screenWidth = MapUtil.getString(context, "screenWidth");

		if (screenWidth != null) {
			context.put("screenWidthSize", screenWidth);
		}

		return context;
	}

	private String _getSessionKey(AnalyticsEvent analyticsEvent) {
		return String.format(
			"%s#%s#%s#%s", analyticsEvent.getProjectId(),
			analyticsEvent.getDataSourceId(), analyticsEvent.getChannelId(),
			analyticsEvent.getUserId());
	}

	private boolean _isCrawler(Map<String, String> context) {
		if (Boolean.parseBoolean(context.getOrDefault("crawler", null))) {
			return true;
		}

		return false;
	}

	private boolean _isLate(AnalyticsEvent analyticsEvent) {
		Date eventDate = analyticsEvent.getEventDate();

		if ((eventDate.getTime() - _watermarkTimestamp) >= 0) {
			return false;
		}

		return true;
	}

	private boolean _isValidURL(Map<String, String> context) {
		String url = context.get("url");

		if ((url == null) || url.startsWith("file://")) {
			return false;
		}

		return true;
	}

	private List<AnalyticsEvent> _parseMessages(
		List<Message<String>> messages) {

		Stream<Message<String>> stream = messages.stream();

		return stream.map(
			message -> {
				try {
					boolean containsNewline = false;

					String messageObject = message.getObject();

					if (StringUtils.isNotBlank(messageObject) &&
						messageObject.contains("\\n")) {

						containsNewline = true;
					}

					AnalyticsEvent analyticsEvent =
						AnalyticsEvent.toAnalyticsEvent(messageObject);

					if (containsNewline && _log.isInfoEnabled()) {
						_log.info(
							"Detected newline in event message for " +
								analyticsEvent.getProjectId());
					}

					return analyticsEvent;
				}
				catch (Exception exception) {
					_log.error(exception, exception);

					return null;
				}
			}
		).filter(
			Objects::nonNull
		).collect(
			Collectors.toList()
		);
	}

	private void _processAnalyticsEvent(AnalyticsEvent analyticsEvent) {
		try {
			Map<String, String> context = _getContext(analyticsEvent);

			if (_isCrawler(context)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Discarding analytics event from crawler: " +
							analyticsEvent.toJSON());
				}

				return;
			}

			if (!_isValidURL(context)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Discarding analytics event from invalid host: " +
							analyticsEvent.toJSON());
				}

				return;
			}

			analyticsEvent.setContext(context);

			SessionContext sessionContext = _updateOrCreateSessionContext(
				analyticsEvent);

			Date eventDate = analyticsEvent.getEventDate();

			if ((eventDate.getTime() - sessionContext.sessionStart.getTime()) <
					0) {

				if (_log.isInfoEnabled()) {
					_log.info(
						"Discarding late user event " +
							analyticsEvent.toJSON());
				}

				return;
			}

			_writeBQEvent(analyticsEvent, sessionContext);
			_writeBQEventProperties(analyticsEvent);
		}
		catch (Exception exception) {
			_log.error(
				"Unable to process analytics event " + analyticsEvent.toJSON() +
					". " + exception.getMessage());
		}
	}

	private void _processAnalyticsEvents(List<AnalyticsEvent> analyticsEvents) {
		for (AnalyticsEvent analyticsEvent : analyticsEvents) {
			if (_isLate(analyticsEvent)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Discarding late analytics event " +
							analyticsEvent.toJSON());
				}

				continue;
			}

			_processAnalyticsEvent(analyticsEvent);
		}
	}

	private SessionContext _updateOrCreateSessionContext(
		AnalyticsEvent analyticsEvent) {

		Date eventDate = analyticsEvent.getEventDate();

		String sessionKey = _getSessionKey(analyticsEvent);

		SessionContext sessionContext = _sessions.get(sessionKey);

		if (sessionContext == null) {
			sessionContext = new SessionContext();

			sessionContext.channelId = analyticsEvent.getChannelId();
			sessionContext.id = DigestUtils.sha256Hex(
				sessionKey + "#" + eventDate.getTime());
			sessionContext.projectId = analyticsEvent.getProjectId();
			sessionContext.sessionStart = analyticsEvent.getEventDate();
			sessionContext.userId = analyticsEvent.getUserId();

			_sessions.put(sessionKey, sessionContext);
		}

		sessionContext.analyticsEvents.add(analyticsEvent);

		sessionContext.maxEventDateTimestamp = Math.max(
			sessionContext.maxEventDateTimestamp, eventDate.getTime());

		Date sessionEnd = new Date(
			sessionContext.maxEventDateTimestamp +
				(_gapDuration * DateUtil.MINUTE));

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"User session {%s, %s} will be closed at %s",
					sessionContext.userId, sessionContext.id, sessionEnd));
		}

		sessionContext.sessionEnd = sessionEnd;

		return sessionContext;
	}

	private void _writeBQEvent(
		AnalyticsEvent analyticsEvent, SessionContext sessionContext) {

		BQEvent bqEvent = new BQEvent();

		Map<String, String> context = analyticsEvent.getContext();

		bqEvent.setAssetId(_formatFieldValue(_getAssetId(analyticsEvent)));
		bqEvent.setAssetTitle(
			_formatFieldValue(_getAssetTitle(analyticsEvent)));
		bqEvent.setApplicationId(analyticsEvent.getApplicationId());
		bqEvent.setBrowserName(context.get("browserName"));
		bqEvent.setCanonicalUrl(context.get("canonicalUrl"));
		bqEvent.setChannelId(Long.valueOf(analyticsEvent.getChannelId()));
		bqEvent.setCity("Local Network");
		bqEvent.setContentLanguageId(context.get("contentLanguageId"));

		try {
			bqEvent.setContext(_objectMapper.writeValueAsString(context));
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException);
		}

		bqEvent.setCountry("Local Network");
		bqEvent.setCreateDate(analyticsEvent.getCreateDate());
		bqEvent.setDataSourceId(Long.valueOf(analyticsEvent.getDataSourceId()));
		bqEvent.setDescription(_formatFieldValue(context.get("description")));
		bqEvent.setDeviceType(context.get("deviceType"));

		String emailAddressHashed = analyticsEvent.getEmailAddressHashed();

		if (StringUtils.isNotBlank(emailAddressHashed)) {
			bqEvent.setEmailAddressHashed(emailAddressHashed);
		}

		bqEvent.setEventDate(analyticsEvent.getEventDate());
		bqEvent.setEventId(analyticsEvent.getEventId());

		try {
			bqEvent.setEventProperties(
				_objectMapper.writeValueAsString(
					analyticsEvent.getEventProperties()));
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException);
		}

		bqEvent.setExperienceId(context.get("experienceId"));

		String experimentId = context.get("experimentId");

		if (StringUtils.isNotBlank(experimentId)) {
			bqEvent.setExperimentId(Long.valueOf(experimentId));
		}

		bqEvent.setId(analyticsEvent.getId());
		bqEvent.setKeywords(_formatFieldValue(context.get("keywords")));
		bqEvent.setLanguageId(context.get("languageId"));
		bqEvent.setPlatformName(context.get("platformName"));
		bqEvent.setProjectTimeZoneId(analyticsEvent.getProjectTimeZoneId());
		bqEvent.setReferrer(context.get("referrer"));
		bqEvent.setRegion("Local Network");
		bqEvent.setSessionId(sessionContext.id);
		bqEvent.setTimezoneOffset(context.get("timezoneOffset"));
		bqEvent.setTitle(_formatFieldValue(context.get("title")));
		bqEvent.setUrl(context.get("url"));
		bqEvent.setUserId(analyticsEvent.getUserId());
		bqEvent.setVariantId(context.get("variantId"));

		_bqEventRepository.insert(bqEvent);
	}

	private void _writeBQEventProperties(AnalyticsEvent analyticsEvent) {
		Map<String, String> eventProperties =
			analyticsEvent.getEventProperties();

		for (Map.Entry<String, String> entry : eventProperties.entrySet()) {
			BQEventProperty bqEventProperty = new BQEventProperty();

			bqEventProperty.setChannelId(
				Long.valueOf(analyticsEvent.getChannelId()));
			bqEventProperty.setEventDate(analyticsEvent.getEventDate());
			bqEventProperty.setId(analyticsEvent.getId());
			bqEventProperty.setName(entry.getKey());
			bqEventProperty.setProjectId(analyticsEvent.getProjectId());
			bqEventProperty.setValue(entry.getValue());

			_bqEventPropertyRepository.insert(bqEventProperty);
		}
	}

	private void _writeBQSession(SessionContext sessionContext) {
		BQSession bqSession = new BQSession();

		SortedSet<AnalyticsEvent> analyticsEvents =
			sessionContext.analyticsEvents;

		AnalyticsEvent firstAnalyticsEvent = analyticsEvents.first();
		AnalyticsEvent lastAnalyticsEvent = analyticsEvents.last();

		Map<String, String> context = firstAnalyticsEvent.getContext();

		Acquisition acquisition = new Acquisition(
			MapUtil.getString(context, "referrer"),
			MapUtil.getString(context, "url"));

		bqSession.setAcquisitionCampaign(acquisition.getCampaign());
		bqSession.setAcquisitionChannel(acquisition.getChannel());
		bqSession.setAcquisitionContent(acquisition.getContent());
		bqSession.setAcquisitionMedium(acquisition.getMedium());
		bqSession.setAcquisitionSource(acquisition.getSource());
		bqSession.setAcquisitionTerm(acquisition.getTerm());

		int interactionsCount = 0;
		int pageViewsCount = 0;
		Set<String> referrers = new HashSet();
		Set<String> urls = new HashSet();

		for (AnalyticsEvent analyticsEvent : analyticsEvents) {
			String eventId = analyticsEvent.getEventId();

			if (Objects.equals(analyticsEvent.getApplicationId(), "Page") &&
				eventId.equals("pageViewed")) {

				pageViewsCount++;
			}

			if (!_noninteractionEvents.contains(analyticsEvent.getEventId())) {
				interactionsCount++;
			}

			Map<String, String> analyticsEventContext =
				analyticsEvent.getContext();

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
			bqSession.setBounce(1);
		}
		else {
			bqSession.setBounce(0);
		}

		bqSession.setBrowserName(context.get("browserName"));
		bqSession.setChannelId(Long.valueOf(sessionContext.channelId));
		bqSession.setCity("Local Network");
		bqSession.setCountry("Local Network");
		bqSession.setDeviceType(context.get("deviceType"));
		bqSession.setDuration(
			DateUtil.getDeltaMilliseconds(
				firstAnalyticsEvent.getEventDate(),
				lastAnalyticsEvent.getEventDate()));
		bqSession.setId(sessionContext.id);
		bqSession.setPlatformName(context.get("platformName"));
		bqSession.setReferrers(referrers);
		bqSession.setRegion("Local Network");
		bqSession.setSessionEnd(lastAnalyticsEvent.getEventDate());
		bqSession.setSessionStart(firstAnalyticsEvent.getEventDate());
		bqSession.setUrls(urls);
		bqSession.setUserId(sessionContext.userId);

		_bqSessionRepository.insert(bqSession);
	}

	private static final Log _log = LogFactory.getLog(
		AnalyticsEventsIngestionNanite.class);

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
	private static final Set<String> _noninteractionEvents =
		new HashSet<String>() {
			{
				add("blogViewed");
				add("documentPreviewed");
				add("formViewed");
				add("pageLoaded");
				add("pageUnloaded");
				add("pageViewed");
				add("webContentViewed");
			}
		};
	private static final Set<String> _projectIds = new HashSet<>();

	@Value("${session.window.allowed.lateness:1}")
	private long _allowedLateness;

	@Autowired
	private BQEventPropertyRepository _bqEventPropertyRepository;

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private BQSessionRepository _bqSessionRepository;

	@Autowired
	private BrowscapEngine _browscapEngine;

	@Value("${session.window.gap.duration:3}")
	private long _gapDuration;

	@Autowired
	private MessageBus _messageBus;

	@MessageSubscriber.Autowired(channel = Channel.ANALYTICS_EVENTS)
	private MessageSubscriber _messageSubscriber;

	@Autowired
	private ObjectMapper _objectMapper;

	private final Map<String, SessionContext> _sessions =
		new ConcurrentHashMap<>();
	private long _watermarkModifiedTimestamp;
	private long _watermarkTimestamp;

	private static class SessionContext {

		public SortedSet<AnalyticsEvent> analyticsEvents = new TreeSet<>(
			Comparator.comparing(AnalyticsEvent::getEventDate));
		public String channelId;
		public String id;
		public long maxEventDateTimestamp;
		public String projectId;
		public Date sessionEnd;
		public Date sessionStart;
		public String userId;

	}

}