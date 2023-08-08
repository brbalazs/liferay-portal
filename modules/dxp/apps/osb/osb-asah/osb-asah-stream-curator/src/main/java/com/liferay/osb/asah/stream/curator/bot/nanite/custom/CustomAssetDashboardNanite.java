/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.custom;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.lock.KeyReentrantLock;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.model.AnalyticsEvent;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * @author Marcellus Tavares
 */
@Component
public class CustomAssetDashboardNanite implements Nanite {

	@Override
	public long getInterval() {
		return DateUtil.MINUTE;
	}

	@Override
	public void run() {
		try {
			_run();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		_boundedExecutor.awaitPendingTasks();
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
	}

	private String _digest(Object... objects) {
		StringBuilder sb = new StringBuilder();

		for (Object object : objects) {
			if (object instanceof Date) {
				Date date = (Date)object;

				sb.append(DateUtil.toUTCString(date));
			}
			else {
				sb.append(object);
			}
		}

		return DigestUtils.sha256Hex(sb.toString());
	}

	private String _getCustomAssetPrimaryKey(AnalyticsEvent analyticsEvent) {
		Map<String, String> eventProperties =
			analyticsEvent.getEventProperties();

		return _digest(
			eventProperties.get("assetId"),
			eventProperties.getOrDefault("category", "default"),
			analyticsEvent.getChannelId());
	}

	private void _run() throws Exception {
		while (true) {
			long start = System.currentTimeMillis();

			List<Message<AnalyticsEvent>> messages =
				_messageSubscriber.pullMessages(
					_customAssetDashboardsNanitePullMessagesSize,
					AnalyticsEvent::toAnalyticsEvent);

			if (messages.isEmpty()) {
				break;
			}

			Stream<Message<AnalyticsEvent>> stream = messages.stream();

			stream.collect(
				Collectors.groupingBy(
					message -> {
						AnalyticsEvent analyticsEvent = message.getObject();

						return Tuples.of(
							analyticsEvent.getProjectId(),
							_getCustomAssetPrimaryKey(analyticsEvent));
					})
			).forEach(
				this::_runAsync
			);

			if (_log.isDebugEnabled()) {
				Class<?> clazz = getClass();

				_log.debug(
					String.format(
						"%s dispatched %d analytics events in %d ms",
						clazz.getSimpleName(), messages.size(),
						System.currentTimeMillis() - start));
			}
		}
	}

	private void _runAsync(
		Tuple2<String, String> tuple2, List<Message<AnalyticsEvent>> messages) {

		_boundedExecutor.runAsync(
			() -> {
				try {
					long start = System.currentTimeMillis();

					ProjectIdThreadLocal.setProjectId(tuple2.getT1());

					Optional<CustomAssetDashboard>
						customAssetDashboardOptional =
							_customAssetDashboardRepository.findById(
								tuple2.getT2());

					CustomAssetDashboard customAssetDashboard =
						_updateCustomAssetDashboard(
							ListUtil.map(messages, Message::getObject),
							customAssetDashboardOptional.orElse(
								new CustomAssetDashboard()),
							tuple2.getT2());

					if (!customAssetDashboardOptional.isPresent()) {
						customAssetDashboard.setIsNew(Boolean.TRUE);
					}

					_customAssetDashboardRepository.save(customAssetDashboard);

					_messageSubscriber.sendAckIds(
						ListUtil.map(messages, Message::getAckId));

					if (_log.isDebugEnabled()) {
						Class<?> clazz = getClass();

						_log.debug(
							String.format(
								"%s processed %d analytics events in %d ms",
								clazz.getSimpleName(), messages.size(),
								System.currentTimeMillis() - start));
					}
				}
				catch (Exception exception) {
					messages.forEach(
						message -> _messageSubscriber.registerException(
							exception, message));

					_log.error(
						"Unable to process analytics events " +
							ListUtil.map(messages, Message::getObject),
						exception);
				}
			},
			KeyReentrantLock.getReentrantLock(getClass(), tuple2));
	}

	private CustomAssetDashboard _updateCustomAssetDashboard(
		List<AnalyticsEvent> analyticsEvents,
		CustomAssetDashboard customAssetDashboard, String id) {

		Stream<AnalyticsEvent> stream = analyticsEvents.stream();

		AnalyticsEvent analyticsEvent = stream.filter(
			event -> Objects.equals(event.getEventId(), "assetViewed")
		).findAny(
		).orElse(
			analyticsEvents.get(analyticsEvents.size() - 1)
		);

		Map<String, String> eventProperties =
			analyticsEvent.getEventProperties();

		customAssetDashboard.setAssetId(eventProperties.get("assetId"));

		String title = eventProperties.get("title");

		if (StringUtils.isNotEmpty(title)) {
			customAssetDashboard.setAssetTitle(title);
		}

		customAssetDashboard.setCategory(
			eventProperties.getOrDefault("category", "default"));
		customAssetDashboard.setChannelId(
			Long.valueOf(analyticsEvent.getChannelId()));
		customAssetDashboard.setCreateDate(analyticsEvent.getEventDate());
		customAssetDashboard.setDataSourceId(
			Long.valueOf(analyticsEvent.getDataSourceId()));
		customAssetDashboard.setId(id);

		return customAssetDashboard;
	}

	private static final Log _log = LogFactory.getLog(
		CustomAssetDashboardNanite.class);

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(15, 10);

	@Autowired
	private CustomAssetDashboardRepository _customAssetDashboardRepository;

	@Value("${osb.asah.custom.asset.dashboards.nanite.pull.messages.size:50}")
	private int _customAssetDashboardsNanitePullMessagesSize;

	@MessageSubscriber.Autowired(
		channel = Channel.ANALYTICS_EVENTS_CUSTOM_ASSET
	)
	private MessageSubscriber _messageSubscriber;

}