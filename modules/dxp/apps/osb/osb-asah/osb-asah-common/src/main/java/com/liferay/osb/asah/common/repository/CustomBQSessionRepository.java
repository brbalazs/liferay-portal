/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.model.AcquisitionType;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.SiteVisitorBehaviorMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.math.BigDecimal;

import java.time.ZoneId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

/**
 * @author Marcos Martins
 */
public interface CustomBQSessionRepository {

	public long countSessionFieldValues(
		String fieldName, FilterHelper filterHelper, String value);

	public List<BQSession> findAllById(Collection<String> sessionIds);

	public Map<String, BigDecimal> getAcquisitionsMetrics(
		AcquisitionType acquisitionType, Long channelId, Pageable pageable,
		TimeRange timeRange, ZoneId zoneId);

	public long getAcquisitionsMetricsCount(
		AcquisitionType acquisitionType, Long channelId, TimeRange timeRange,
		ZoneId zoneId);

	public List<Map<String, Object>> getCohortHeatMapTuples(
		Long channelId, Interval interval, TimeRange timeRange, ZoneId zoneId);

	public Map<String, BigDecimal> getSessionsCountGroupedByBrowserName(
		Long channelId, TimeRange timeRange, ZoneId zoneId);

	public List<Map<String, Object>> getSessionsCountGroupedByDeviceName(
		Long channelId, TimeRange timeRange, ZoneId zoneId);

	public Map<String, BigDecimal> getSessionsCountGroupedByGeolocation(
		Long channelId, TimeRange timeRange, ZoneId zoneId);

	public List<SiteVisitorBehaviorMetric> getSiteVisitorBehaviorMetrics(
		Long channelId, boolean includePrevious, TimeRange timeRange,
		ZoneId zoneId);

	public List<SiteVisitorBehaviorMetric>
		getSiteVisitorBehaviorMetricsGroupedBySessionStart(
			Long channelId, boolean includePrevious, Interval interval,
			TimeRange timeRange, ZoneId zoneId);

	public List<Map<String, BigDecimal>> getVisitorsCountGroupedByDayAndTime(
		Long channelId, TimeRange timeRange, ZoneId zoneId);

	public BQSession insert(BQSession bqSession);

	public List<String> searchSessionFieldValues(
		String fieldName, FilterHelper filterHelper, Pageable pageable,
		String value);

}