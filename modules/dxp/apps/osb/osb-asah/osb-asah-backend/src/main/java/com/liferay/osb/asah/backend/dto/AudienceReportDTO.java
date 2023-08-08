/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.Trend;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.ResultBag;

/**
 * @author Robson Pastor
 */
@GraphQLType("AudienceReport")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AudienceReportDTO {

	public AudienceReportDTO() {
	}

	public AudienceReportDTO(AudienceReport audienceReport) {
		_anonymousIndividualsCount =
			audienceReport.getAnonymousIndividualsCount();
		_knownIndividualsCount = audienceReport.getKnownIndividualsCount();
		_nonsegmentedIndividualsCount =
			audienceReport.getNonsegmentedIndividualsCount();
		_segmentedIndividualsCount =
			audienceReport.getSegmentedIndividualsCount();
	}

	@GraphQLProperty("anonymousUsersCount")
	@JsonProperty("anonymousUsersCount")
	public Long getAnonymousIndividualsCount() {
		return _anonymousIndividualsCount;
	}

	@GraphQLProperty("knownUsersCount")
	@JsonProperty("knownUsersCount")
	public Long getKnownIndividualsCount() {
		return _knownIndividualsCount;
	}

	@GraphQLProperty("nonsegmentedKnownUsersCount")
	@JsonProperty("nonsegmentedIndividualsCount")
	public Long getNonsegmentedIndividualsCount() {
		return _nonsegmentedIndividualsCount;
	}

	@GraphQLProperty("segmentedKnownUsersCount")
	@JsonProperty("segmentedIndividualsCount")
	public Long getSegmentedIndividualsCount() {
		return _segmentedIndividualsCount;
	}

	@JsonProperty("segments")
	public ResultBag<SegmentMetricDTO> getSegmentMetricDTOReportResultBag() {
		return _segmentMetricDTOReportResultBag;
	}

	public void setAnonymousIndividualsCount(Long anonymousIndividualsCount) {
		_anonymousIndividualsCount = anonymousIndividualsCount;
	}

	public void setKnownIndividualsCount(Long knownIndividualsCount) {
		_knownIndividualsCount = knownIndividualsCount;
	}

	public void setNonsegmentedIndividualsCount(
		Long nonsegmentedIndividualsCount) {

		_nonsegmentedIndividualsCount = nonsegmentedIndividualsCount;
	}

	public void setSegmentedIndividualsCount(Long segmentedIndividualsCount) {
		_segmentedIndividualsCount = segmentedIndividualsCount;
	}

	public void setSegmentMetricDTOReportResultBag(
		ResultBag<SegmentMetricDTO> segmentMetricDTOReportResultBag) {

		_segmentMetricDTOReportResultBag = segmentMetricDTOReportResultBag;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SegmentMetricDTO {

		public SegmentMetricDTO(Metric metric) {
			_previousValue = metric.getPreviousValue();
			_trend = metric.getTrend();
			_value = metric.getValue();
			_valueKey = metric.getValueKey();
		}

		public Double getPreviousValue() {
			return _previousValue;
		}

		public Trend getTrend() {
			if ((_trend == null) || (_trend.getPercentage() == null)) {
				return null;
			}

			return _trend;
		}

		public Double getValue() {
			return _value;
		}

		public String getValueKey() {
			return _valueKey;
		}

		private final Double _previousValue;
		private Trend _trend;
		private final Double _value;
		private final String _valueKey;

	}

	private Long _anonymousIndividualsCount;
	private Long _knownIndividualsCount;
	private Long _nonsegmentedIndividualsCount;
	private Long _segmentedIndividualsCount;
	private ResultBag<SegmentMetricDTO> _segmentMetricDTOReportResultBag;

}