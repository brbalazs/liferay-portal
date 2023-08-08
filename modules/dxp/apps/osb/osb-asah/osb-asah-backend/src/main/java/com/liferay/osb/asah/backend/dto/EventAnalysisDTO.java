/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.EventAnalysis;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.AttributeType;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Rachael Koestartyo
 */
@GraphQLType("EventAnalysis")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventAnalysisDTO {

	public EventAnalysisDTO(EventAnalysis eventAnalysis) {
		_eventAnalysis = eventAnalysis;

		JSONArray eventAnalysisBreakdownJSONArray =
			_eventAnalysis.getEventAnalysisBreakdownJSONArray();

		for (int i = 0; i < eventAnalysisBreakdownJSONArray.length(); i++) {
			JSONObject eventAnalysisBreakdownJSONObject =
				eventAnalysisBreakdownJSONArray.getJSONObject(i);

			EventAnalysisBreakdownDTO eventAnalysisBreakdownDTO =
				new EventAnalysisBreakdownDTO(
					eventAnalysisBreakdownJSONObject.toMap());

			_eventAnalysisBreakdownDTOs.add(eventAnalysisBreakdownDTO);
		}

		JSONArray eventAnalysisFilterJSONArray =
			_eventAnalysis.getEventAnalysisFilterJSONArray();

		for (int i = 0; i < eventAnalysisFilterJSONArray.length(); i++) {
			JSONObject eventAnalysisFilterJSONObject =
				eventAnalysisFilterJSONArray.getJSONObject(i);

			EventAnalysisFilterDTO eventAnalysisFilterDTO =
				new EventAnalysisFilterDTO(
					eventAnalysisFilterJSONObject.toMap());

			_eventAnalysisFilterDTOs.add(eventAnalysisFilterDTO);
		}
	}

	public String getAnalysisType() {
		return _eventAnalysis.getEventAnalysisType();
	}

	public String getChannelId() {
		return String.valueOf(_eventAnalysis.getChannelId());
	}

	public Boolean getCompareToPrevious() {
		return _eventAnalysis.getCompareToPrevious();
	}

	@GraphQLProperty("createDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createDate")
	public String getCreateDate() {
		if (_eventAnalysis.getCreateDate() != null) {
			return DateUtil.toUTCString(_eventAnalysis.getCreateDate());
		}

		return null;
	}

	public String getCreatedByUserId() {
		return String.valueOf(_eventAnalysis.getCreatedByUserId());
	}

	public String getCreatedByUserName() {
		return _eventAnalysis.getCreatedByUserName();
	}

	@GraphQLProperty("eventAnalysisBreakdowns")
	@JsonProperty("eventAnalysisBreakdowns")
	public List<EventAnalysisBreakdownDTO> getEventAnalysisBreakdownDTOs() {
		return _eventAnalysisBreakdownDTOs;
	}

	@GraphQLProperty("eventAnalysisFilters")
	@JsonProperty("eventAnalysisFilters")
	public List<EventAnalysisFilterDTO> getEventAnalysisFilterDTOs() {
		return _eventAnalysisFilterDTOs;
	}

	public String getEventDefinitionId() {
		return String.valueOf(_eventAnalysis.getEventDefinitionId());
	}

	public String getId() {
		return String.valueOf(_eventAnalysis.getId());
	}

	public String getModifiedByUserId() {
		return String.valueOf(_eventAnalysis.getModifiedByUserId());
	}

	public String getModifiedByUserName() {
		return _eventAnalysis.getModifiedByUserName();
	}

	@GraphQLProperty("modifiedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("modifiedDate")
	public String getModifiedDate() {
		if (_eventAnalysis.getModifiedDate() != null) {
			return DateUtil.toUTCString(_eventAnalysis.getModifiedDate());
		}

		return null;
	}

	public String getName() {
		return _eventAnalysis.getName();
	}

	@GraphQLProperty("rangeEnd")
	@JsonFormat(
		pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("rangeEnd")
	public LocalDate getRangeEndLocalDate() {
		return _eventAnalysis.getRangeEndLocalDate();
	}

	public Integer getRangeKey() {
		return _eventAnalysis.getRangeKey();
	}

	@GraphQLProperty("rangeStart")
	@JsonFormat(
		pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("rangeStart")
	public LocalDate getRangeStartLocalDate() {
		return _eventAnalysis.getRangeStartLocalDate();
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class EventAnalysisBreakdownDTO {

		public EventAnalysisBreakdownDTO(Map<String, Object> source) {
			BeanUtils.copyProperties(new CaseInsensitiveMap(source), this);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof EventAnalysisBreakdownDTO)) {
				return false;
			}

			EventAnalysisBreakdownDTO eventAnalysisBreakdownDTO =
				(EventAnalysisBreakdownDTO)obj;

			if (Objects.equals(
					_attributeId, eventAnalysisBreakdownDTO._attributeId) &&
				Objects.equals(
					_attributeType, eventAnalysisBreakdownDTO._attributeType) &&
				Objects.equals(_binSize, eventAnalysisBreakdownDTO._binSize) &&
				Objects.equals(
					_dataType, eventAnalysisBreakdownDTO._dataType) &&
				Objects.equals(
					_dateGrouping, eventAnalysisBreakdownDTO._dateGrouping) &&
				Objects.equals(
					_description, eventAnalysisBreakdownDTO._description) &&
				Objects.equals(
					_displayName, eventAnalysisBreakdownDTO._displayName) &&
				Objects.equals(
					_sortType, eventAnalysisBreakdownDTO._sortType)) {

				return true;
			}

			return false;
		}

		public String getAttributeId() {
			return _attributeId;
		}

		public AttributeType getAttributeType() {
			return _attributeType;
		}

		public Number getBinSize() {
			if ((_binSize != null) &&
				(_dataType == EventAttributeDefinition.DataType.DURATION)) {

				return _binSize.intValue();
			}

			return _binSize;
		}

		public EventAttributeDefinition.DataType getDataType() {
			return _dataType;
		}

		public DateGrouping getDateGrouping() {
			return _dateGrouping;
		}

		public String getDescription() {
			return _description;
		}

		public String getDisplayName() {
			return _displayName;
		}

		public String getSortType() {
			return _sortType;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_attributeId, _attributeType, _binSize, _dataType,
				_dateGrouping, _description, _displayName, _sortType);
		}

		public void setAttributeId(String attributeId) {
			_attributeId = attributeId;
		}

		public void setAttributeType(AttributeType attributeType) {
			_attributeType = attributeType;
		}

		public void setBinSize(Number binSize) {
			_binSize = binSize;
		}

		public void setDataType(EventAttributeDefinition.DataType dataType) {
			_dataType = dataType;
		}

		public void setDateGrouping(DateGrouping dateGrouping) {
			_dateGrouping = dateGrouping;
		}

		public void setDescription(String description) {
			_description = description;
		}

		public void setDisplayName(String displayName) {
			_displayName = displayName;
		}

		public void setSortType(String sortType) {
			_sortType = sortType;
		}

		private String _attributeId;
		private AttributeType _attributeType;
		private Number _binSize;
		private EventAttributeDefinition.DataType _dataType;
		private DateGrouping _dateGrouping;
		private String _description;
		private String _displayName;
		private String _sortType;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class EventAnalysisFilterDTO {

		public EventAnalysisFilterDTO(Map<String, Object> source) {
			BeanUtils.copyProperties(new CaseInsensitiveMap(source), this);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if ((obj == null) || !(obj instanceof EventAnalysisFilterDTO)) {
				return false;
			}

			EventAnalysisFilterDTO eventAnalysisFilterDTO =
				(EventAnalysisFilterDTO)obj;

			if (Objects.equals(
					_attributeId, eventAnalysisFilterDTO._attributeId) &&
				Objects.equals(
					_attributeType, eventAnalysisFilterDTO._attributeType) &&
				Objects.equals(_dataType, eventAnalysisFilterDTO._dataType) &&
				Objects.equals(
					_description, eventAnalysisFilterDTO._description) &&
				Objects.equals(
					_displayName, eventAnalysisFilterDTO._displayName) &&
				Objects.equals(_operator, eventAnalysisFilterDTO._operator) &&
				Objects.equals(_values, eventAnalysisFilterDTO._values)) {

				return true;
			}

			return false;
		}

		public String getAttributeId() {
			return _attributeId;
		}

		public AttributeType getAttributeType() {
			return _attributeType;
		}

		public EventAttributeDefinition.DataType getDataType() {
			return _dataType;
		}

		public String getDescription() {
			return _description;
		}

		public String getDisplayName() {
			return _displayName;
		}

		public String getOperator() {
			return _operator;
		}

		public List<String> getValues() {
			return _values;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_attributeId, _attributeType, _dataType, _description,
				_displayName, _operator, _values);
		}

		public void setAttributeId(String attributeId) {
			_attributeId = attributeId;
		}

		public void setAttributeType(AttributeType attributeFilterType) {
			_attributeType = attributeFilterType;
		}

		public void setDataType(EventAttributeDefinition.DataType dataType) {
			_dataType = dataType;
		}

		public void setDescription(String description) {
			_description = description;
		}

		public void setDisplayName(String displayName) {
			_displayName = displayName;
		}

		public void setOperator(String operator) {
			_operator = operator;
		}

		public void setValues(List<String> values) {
			_values = values;
		}

		private String _attributeId;
		private AttributeType _attributeType;
		private EventAttributeDefinition.DataType _dataType;
		private String _description;
		private String _displayName;
		private String _operator;
		private List<String> _values;

	}

	private final EventAnalysis _eventAnalysis;
	private final List<EventAnalysisBreakdownDTO> _eventAnalysisBreakdownDTOs =
		new ArrayList<>();
	private final List<EventAnalysisFilterDTO> _eventAnalysisFilterDTOs =
		new ArrayList<>();

}