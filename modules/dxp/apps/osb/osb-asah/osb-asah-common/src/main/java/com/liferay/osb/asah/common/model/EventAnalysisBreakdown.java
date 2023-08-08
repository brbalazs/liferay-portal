/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.map.CaseInsensitiveMap;

/**
 * @author Matthew Kong
 */
public class EventAnalysisBreakdown {

	public EventAnalysisBreakdown(Map<String, Object> source) {
		BeanUtils.copyProperties(new CaseInsensitiveMap(source), this);
	}

	public EventAnalysisBreakdown(
		String attributeId, AttributeType attributeType, Number binSize,
		EventAttributeDefinition.DataType dataType, DateGrouping dateGrouping,
		String description, String displayName, String sortType) {

		_attributeId = attributeId;
		_attributeType = attributeType;
		_binSize = binSize;
		_dataType = dataType;
		_dateGrouping = dateGrouping;
		_description = description;
		_displayName = displayName;
		_sortType = sortType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventAnalysisBreakdown)) {
			return false;
		}

		EventAnalysisBreakdown eventAnalysisBreakdown =
			(EventAnalysisBreakdown)obj;

		if (Objects.equals(_attributeId, eventAnalysisBreakdown._attributeId) &&
			Objects.equals(
				_attributeType, eventAnalysisBreakdown._attributeType) &&
			Objects.equals(_binSize, eventAnalysisBreakdown._binSize) &&
			Objects.equals(_dataType, eventAnalysisBreakdown._dataType) &&
			Objects.equals(
				_dateGrouping, eventAnalysisBreakdown._dateGrouping) &&
			Objects.equals(_description, eventAnalysisBreakdown._description) &&
			Objects.equals(_displayName, eventAnalysisBreakdown._displayName) &&
			Objects.equals(_sortType, eventAnalysisBreakdown._sortType)) {

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
			_attributeId, _attributeType, _binSize, _dataType, _dateGrouping,
			_description, _displayName, _sortType);
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