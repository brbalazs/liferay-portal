/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.util.SetUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Leilany Ulisses
 */
public class EventAttributeDefinitionDTOTest extends BaseDTOTestCase {

	@Override
	public String getClassName() {
		return EventAttributeDefinitionDTO.class.getName();
	}

	@Test
	public void testEventAttributeDefinitionDTO() {
		EventAttributeDefinition eventAttributeDefinition =
			new EventAttributeDefinition();

		eventAttributeDefinition.setDataType(
			EventAttributeDefinition.DataType.STRING);
		eventAttributeDefinition.setDescription(null);
		eventAttributeDefinition.setDisplayName("canonicalUrl");
		eventAttributeDefinition.setEventDefinitionEventAttributeDefinitions(
			SetUtil.of(
				new EventDefinitionEventAttributeDefinition(17L, null),
				new EventDefinitionEventAttributeDefinition(17L, null),
				new EventDefinitionEventAttributeDefinition(
					17L, "http://192.168.111.140:8089"),
				new EventDefinitionEventAttributeDefinition(17L, null),
				new EventDefinitionEventAttributeDefinition(17L, null)));

		eventAttributeDefinition.setId(10L);
		eventAttributeDefinition.setName("canonicalUrl");
		eventAttributeDefinition.setType(EventAttributeDefinition.Type.GLOBAL);

		EventAttributeDefinitionDTO eventAttributeDefinitionDTO =
			new EventAttributeDefinitionDTO(eventAttributeDefinition);

		Assertions.assertEquals(
			EventAttributeDefinition.DataType.STRING,
			eventAttributeDefinitionDTO.getDataType());

		Assertions.assertNull(eventAttributeDefinitionDTO.getDescription());

		Assertions.assertEquals(
			"canonicalUrl", eventAttributeDefinitionDTO.getDisplayName());

		Assertions.assertEquals("10", eventAttributeDefinitionDTO.getId());

		Assertions.assertEquals(
			"canonicalUrl", eventAttributeDefinitionDTO.getName());

		Assertions.assertEquals(
			"http://192.168.111.140:8089",
			eventAttributeDefinitionDTO.getSampleValue());

		Assertions.assertEquals(
			EventAttributeDefinition.Type.GLOBAL,
			eventAttributeDefinitionDTO.getType());
	}

}