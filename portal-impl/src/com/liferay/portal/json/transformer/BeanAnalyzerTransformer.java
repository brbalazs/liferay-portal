/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.json.transformer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jodd.introspector.PropertyDescriptor;

import jodd.json.JsonSerializer;
import jodd.json.TypeJsonVisitor;

/**
 * @author Igor Spasic
 */
public class BeanAnalyzerTransformer extends TypeJsonVisitor {

	public BeanAnalyzerTransformer(Class<?> clazz) {
		super(_jsonSerializer.createJsonContext(null), clazz);
	}

	public List<Map<String, String>> collect() {
		_propertiesList = new ArrayList<>();

		visit();

		return _propertiesList;
	}

	protected String getTypeName(Class<?> clazz) {
		return clazz.getName();
	}

	@Override
	protected void onSerializableProperty(
		String propertyName, PropertyDescriptor propertyDescriptor) {

		Map<String, String> properties = new LinkedHashMap<>();

		properties.put("name", propertyName);

		Class<?> propertyClass = propertyDescriptor.getType();

		properties.put("type", getTypeName(propertyClass));

		_propertiesList.add(properties);
	}

	private static final JsonSerializer _jsonSerializer = new JsonSerializer();

	private List<Map<String, String>> _propertiesList = new ArrayList<>();

}