/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marcellus Tavares
 */
public class PropertyFilter {

	public static PropertyFilter of(Map<String, Object> propertyFilter) {
		return new PropertyFilter(
			MapUtil.get(propertyFilter, "filter"),
			MapUtil.get(propertyFilter, "negate"));
	}

	public PropertyFilter() {
	}

	public PropertyFilter(String filterString, boolean negate) {
		_negate = negate;

		List<String> filterTokens = _getFilterTokens(filterString);

		_operator = filterTokens.get(1);
		_propertyName = filterTokens.get(0);
		_propertyValue = filterTokens.get(2);
	}

	public void and(PropertyFilter propertyFilter) {
		_propertyFilters.add(propertyFilter);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PropertyFilter)) {
			return false;
		}

		PropertyFilter propertyFilter = (PropertyFilter)obj;

		if (Objects.equals(_negate, propertyFilter._negate) &&
			Objects.equals(_operator, propertyFilter._operator) &&
			Objects.equals(_propertyFilters, propertyFilter._propertyFilters) &&
			Objects.equals(_propertyName, propertyFilter._propertyName) &&
			Objects.equals(
				_propertyNamespace, propertyFilter._propertyNamespace) &&
			Objects.equals(_propertyValue, propertyFilter._propertyValue)) {

			return true;
		}

		return false;
	}

	public String getOperator() {
		return _operator;
	}

	public List<PropertyFilter> getPropertyFilters() {
		return _propertyFilters;
	}

	public String getPropertyName() {
		return _propertyNamespace + _propertyName;
	}

	public String getPropertyNamespace() {
		return _propertyNamespace;
	}

	public String getPropertyValue() {
		return _propertyValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_operator, _negate, _propertyFilters, _propertyName,
			_propertyNamespace, _propertyValue);
	}

	public boolean isNegate() {
		return _negate;
	}

	public void setNegate(boolean negate) {
		_negate = negate;
	}

	public void setOperator(String operator) {
		_operator = operator;
	}

	public void setPropertyName(String propertyName) {
		_propertyName = propertyName;
	}

	public void setPropertyNamespace(String propertyNamespace) {
		_propertyNamespace = propertyNamespace;
	}

	public void setPropertyValue(String propertyValue) {
		_propertyValue = propertyValue;
	}

	public String toFilterString() {
		String filterStringFormat = "";

		if (_negate) {
			filterStringFormat = "not ";
		}

		if (Objects.equals(_operator, "=")) {
			filterStringFormat += "%s eq '%s'";
		}
		else {
			filterStringFormat += "similarTo(%s, '%s')";
		}

		return String.format(filterStringFormat, _propertyName, _propertyValue);
	}

	private List<String> _getFilterTokens(String filterString) {
		Matcher matcher = _filterStringPattern.matcher(filterString);

		if (!matcher.matches()) {
			throw new IllegalArgumentException(
				"Invalid filter " + filterString);
		}

		return new ArrayList<String>() {
			{
				add(matcher.group(1));
				add(matcher.group(2));
				add(matcher.group(3));
			}
		};
	}

	private static final Pattern _filterStringPattern = Pattern.compile(
		"(.+)\\s([=~])\\s(.+)");

	private boolean _negate;
	private String _operator;
	private final List<PropertyFilter> _propertyFilters = new ArrayList<>();
	private String _propertyName;
	private String _propertyNamespace = "";
	private String _propertyValue;

}