/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1;

import com.liferay.osb.asah.backend.dog.HistogramDog;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.PageDog;
import com.liferay.osb.asah.backend.dog.PageReferrerDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.dto.SearchKeywordDTO;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.TimeRange;

import java.time.LocalDate;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shinn Lok
 * @author André Miranda
 */
@RequestMapping(produces = "application/json", value = "/api/1.0/pages")
@RestController
public class PagesRestController extends BaseRestController {

	@GetMapping("/acquisition-channels")
	public Map<String, Double> getAcquisitionChannels(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@RequestParam(defaultValue = "D") String interval,
		@RequestParam(defaultValue = "7") int rangeKey,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		if (StringUtils.isBlank(canonicalURL)) {
			return Collections.emptyMap();
		}

		return _pageReferrerDog.getAcquisitionChannels(
			new SearchQueryContext() {
				{
					setCanonicalUrl(canonicalURL);
					setIncludeActiveSessions(true);
					setInterval(interval);
					setRangeKey(rangeKey);

					if ((endLocalDate != null) && (startLocalDate != null)) {
						setTimeRange(
							TimeRange.of(endLocalDate, startLocalDate));
					}
				}
			});
	}

	@GetMapping("/geolocations")
	public String getGeolocations(@RequestParam String canonicalURL) {
		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setCanonicalUrl(canonicalURL);

		return String.valueOf(
			new JSONArray(
				_metricDog.getGeolocationMetrics(
					PageMetricType.VIEWS, searchQueryContext)));
	}

	@GetMapping("/page-referrer-hosts")
	public Map<String, Double> getPageReferrerHosts(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@RequestParam(defaultValue = "D") String interval,
		@RequestParam(defaultValue = "7") int rangeKey,
		@RequestParam(defaultValue = "30") int size,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		if (StringUtils.isBlank(canonicalURL)) {
			return Collections.emptyMap();
		}

		return _pageReferrerDog.getPageReferrers(
			"referrerHost",
			new SearchQueryContext() {
				{
					setCanonicalUrl(canonicalURL);
					setInterval(interval);
					setRangeKey(rangeKey);

					if ((endLocalDate != null) && (startLocalDate != null)) {
						setTimeRange(
							TimeRange.of(endLocalDate, startLocalDate));
					}
				}
			},
			size);
	}

	@GetMapping("/page-referrers")
	public Map<String, Double> getPageReferrers(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@RequestParam(defaultValue = "D") String interval,
		@RequestParam(defaultValue = "7") int rangeKey,
		@RequestParam(defaultValue = "30") int size,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		if (StringUtils.isBlank(canonicalURL)) {
			return Collections.emptyMap();
		}

		return _pageReferrerDog.getPageReferrers(
			"referrerCanonicalUrl",
			new SearchQueryContext() {
				{
					setCanonicalUrl(canonicalURL);
					setInterval(interval);
					setRangeKey(rangeKey);

					if ((endLocalDate != null) && (startLocalDate != null)) {
						setTimeRange(
							TimeRange.of(endLocalDate, startLocalDate));
					}
				}
			},
			size);
	}

	@GetMapping("/read-counts")
	public String getReadCounts(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate")
		LocalDate endLocalDate,
		@RequestParam String interval,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate")
		LocalDate startLocalDate) {

		long readsMetricValue = _pageDog.getReadsMetricValue(
			canonicalURL, startLocalDate, endLocalDate);

		return _getHistogramMetrics(
			canonicalURL, endLocalDate, interval, PageMetricType.READS,
			startLocalDate, readsMetricValue);
	}

	@GetMapping("/read-count")
	public String getReadsCount(@RequestParam String canonicalURL) {
		return String.valueOf(
			_pageDog.getReadsMetricValue(canonicalURL, null, null));
	}

	@GetMapping("/search-keywords")
	public PageDTO<SearchKeywordDTO> getSearchKeywords(
		@RequestParam(required = false) String displayLanguageId,
		@RequestParam(required = false) String groupId,
		@RequestParam(defaultValue = "0") int minCounts,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return _toSearchKeywordDTOPageDTO(
			_bqEventDog.getSearchKeywordPage(
				displayLanguageId, groupId, null, minCounts, page, size, sorts,
				null));
	}

	@GetMapping("/social-page-referrers")
	public Map<String, Double> getSocialPageReferrers(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate", required = false)
		LocalDate endLocalDate,
		@RequestParam(defaultValue = "D") String interval,
		@RequestParam(defaultValue = "7") int rangeKey,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate", required = false)
		LocalDate startLocalDate) {

		if (StringUtils.isBlank(canonicalURL)) {
			return Collections.emptyMap();
		}

		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setCanonicalUrl(canonicalURL);
		searchQueryContext.setInterval(interval);
		searchQueryContext.setRangeKey(rangeKey);

		return _pageReferrerDog.getSocialPageReferrers(
			new SearchQueryContext() {
				{
					setCanonicalUrl(canonicalURL);
					setInterval(interval);
					setRangeKey(rangeKey);

					if ((endLocalDate != null) && (startLocalDate != null)) {
						setTimeRange(
							TimeRange.of(endLocalDate, startLocalDate));
					}
				}
			});
	}

	@GetMapping("/view-counts")
	public String getViewCounts(
		@RequestParam String canonicalURL,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "endDate")
		LocalDate endLocalDate,
		@RequestParam String interval,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@RequestParam(name = "startDate")
		LocalDate startLocalDate) {

		long viewsMetricValue = _pageDog.getViewsMetricValue(
			canonicalURL, startLocalDate, endLocalDate);

		return _getHistogramMetrics(
			canonicalURL, endLocalDate, interval, PageMetricType.VIEWS,
			startLocalDate, viewsMetricValue);
	}

	@GetMapping("/view-count")
	public String getViewsCount(@RequestParam String canonicalURL) {
		return String.valueOf(_pageDog.getViewsMetricValue(canonicalURL));
	}

	private String _getHistogramMetrics(
		String canonicalUrl, LocalDate endLocalDate, String interval,
		PageMetricType pageMetricType, LocalDate startLocalDate,
		long totalMetricValue) {

		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				pageMetricType,
				new SearchQueryContext() {
					{
						setCanonicalUrl(canonicalUrl);
						setIncludeActiveSessions(Boolean.TRUE);
						setIncludePrevious(Boolean.TRUE);
						setInterval(interval);
						setTimeRange(
							TimeRange.of(endLocalDate, startLocalDate));
					}
				});

		return JSONUtil.put(
			"histogram", histogramMetricBag.getMetrics()
		).put(
			"value", totalMetricValue
		).toString();
	}

	private PageDTO<SearchKeywordDTO> _toSearchKeywordDTOPageDTO(
		Page<SearchKeyword> searchKeywordsPage) {

		return new PageDTO<>(
			"_embedded", new SearchKeywordDTO(searchKeywordsPage.getContent()),
			searchKeywordsPage.getNumber(), searchKeywordsPage.getSize(),
			searchKeywordsPage.getTotalElements(),
			searchKeywordsPage.getTotalPages());
	}

	@Autowired
	private BQEventDog _bqEventDog;

	@Autowired
	private HistogramDog _histogramDog;

	@Autowired
	private MetricDog _metricDog;

	@Autowired
	private PageDog _pageDog;

	@Autowired
	private PageReferrerDog _pageReferrerDog;

}