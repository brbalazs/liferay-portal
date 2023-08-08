/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.EventMetric;
import com.liferay.osb.asah.backend.model.EventMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQEventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class EventMetricDog {

	public EventMetric getEventMetric(SearchQueryContext searchQueryContext) {
		EventMetric eventMetric = new EventMetric();

		Metric totalEventsMetric = new Metric(EventMetricType.TOTAL_EVENTS);

		TimeRange timeRange = searchQueryContext.getTimeRange();

		totalEventsMetric.setValue(
			(double)_bqEventRepository.countBQEvents(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getEntityId(),
				searchQueryContext.getKeywords(),
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));

		eventMetric.setTotalEventsMetric(totalEventsMetric);

		Metric totalSessionsMetric = new Metric(EventMetricType.TOTAL_SESSIONS);

		// TODO Change countEventSessions to use individualId instead of userIds

		totalSessionsMetric.setValue(
			(double)_bqEventRepository.countEventSessions(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getEntityId(),
				searchQueryContext.getKeywords(),
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));

		eventMetric.setTotalSessionsMetric(totalSessionsMetric);

		return eventMetric;
	}

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}