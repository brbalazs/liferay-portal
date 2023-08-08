/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.map.CaseInsensitiveMap;

/**
 * @author Leslie Wong
 */
public class EventAnalysisFilter {

	public EventAnalysisFilter(Map<String, Object> source) {
		BeanUtils.copyProperties(new CaseInsensitiveMap(source), this);
	}

	public EventAnalysisFilter(
		String attributeId, AttributeType attributeType,
		EventAttributeDefinition.DataType dataType, String description,
		String displayName, String operator, List<String> values) {

		_attributeId = attributeId;
		_attributeType = attributeType;
		_dataType = dataType;
		_description = description;
		_displayName = displayName;
		_operator = operator;
		_values = values;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof EventAnalysisFilter)) {
			return false;
		}

		EventAnalysisFilter eventAnalysisFilter = (EventAnalysisFilter)obj;

		if (Objects.equals(_attributeId, eventAnalysisFilter._attributeId) &&
			Objects.equals(
				_attributeType, eventAnalysisFilter._attributeType) &&
			Objects.equals(_dataType, eventAnalysisFilter._dataType) &&
			Objects.equals(_description, eventAnalysisFilter._description) &&
			Objects.equals(_displayName, eventAnalysisFilter._displayName) &&
			Objects.equals(_operator, eventAnalysisFilter._operator) &&
			Objects.equals(_values, eventAnalysisFilter._values)) {

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
			_attributeId, _attributeType, _dataType, _description, _displayName,
			_operator, _values);
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