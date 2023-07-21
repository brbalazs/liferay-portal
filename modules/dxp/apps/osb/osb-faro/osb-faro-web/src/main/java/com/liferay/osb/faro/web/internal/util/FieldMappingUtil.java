/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.util;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.FieldMapping;
import com.liferay.osb.faro.engine.client.model.FieldMappingMap;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.model.FaroProject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Matthew Kong
 */
public class FieldMappingUtil {

	public static List<FieldMappingMap> getNewFieldMappingMaps(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String context, List<FieldMappingMap> fieldMappingMaps) {

		List<FieldMappingMap> newFieldMappingMaps = new ArrayList<>();

		Stream<FieldMappingMap> fieldMappingMapsStream =
			fieldMappingMaps.stream();

		Results<FieldMapping> results = contactsEngineClient.getFieldMappings(
			faroProject, context,
			fieldMappingMapsStream.map(
				FieldMappingMap::getName
			).collect(
				Collectors.toList()
			),
			1, 10000, null);

		List<FieldMapping> fieldMappings = results.getItems();

		Stream<FieldMapping> fieldMappingsStream = fieldMappings.stream();

		Set<String> currentFieldNames = fieldMappingsStream.map(
			FieldMapping::getFieldName
		).collect(
			Collectors.toSet()
		);

		Set<String> newFieldMappingNames = new HashSet<>();

		for (FieldMappingMap fieldMappingMap : fieldMappingMaps) {
			String name = fieldMappingMap.getName();

			if (currentFieldNames.contains(name) ||
				newFieldMappingNames.contains(name)) {

				continue;
			}

			newFieldMappingNames.add(name);

			newFieldMappingMaps.add(fieldMappingMap);
		}

		return newFieldMappingMaps;
	}

}