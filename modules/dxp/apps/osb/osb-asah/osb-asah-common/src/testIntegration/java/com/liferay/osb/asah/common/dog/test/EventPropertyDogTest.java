/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.dog.EventPropertyDog;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Alejo Ceballos
 */
public class EventPropertyDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testFindAttributeValuesByRelationshipIdsAndKeywords()
		throws Exception {

		Channel channel = _channelDog.addChannel("Test Channel");

		Date date = DateUtil.newDayDate();

		Set<BQEventProperty> bqEventProperties =
			new HashSet<BQEventProperty>() {
				{
					add(
						new BQEventProperty(
							null, "viewDuration", "Event Attribute Value 1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "event attribute value 2"));
					add(
						new BQEventProperty(
							null, "viewDuration", "EVENT ATTRIBUTE VALUE 3"));
					add(
						new BQEventProperty(
							null, "viewDuration", "EvEnT AtTrIbuTe VaLuE 4"));
					add(
						new BQEventProperty(
							null, "viewDuration", "EvEnT AtTrIbuTe VaLuE 1"));
					add(
						new BQEventProperty(
							null, "viewDuration", "A totally different value"));
				}
			};

		_bqEventDog.addBQEvent(
			"Page", bqEventProperties, channel.getId(), date, 1L, date,
			"pageUnloaded", "analyticsEventId", "sessionId", "abcdef");

		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageUnloaded");

		Page<String> bqEventPropertyValuePage =
			_eventPropertyDog.getBQEventPropertyValuePage(
				channel.getId(), eventAttributeDefinition.getId(),
				eventDefinition.getId(), "Attribute Value", 100, 0);

		Assertions.assertEquals(4, bqEventPropertyValuePage.getTotalElements());

		List<String> eventAttributeValues =
			bqEventPropertyValuePage.getContent();

		Assertions.assertEquals(4, eventAttributeValues.size());

		for (String value :
				Arrays.asList(
					"event attribute value 4", "event attribute value 3",
					"event attribute value 2", "event attribute value 1")) {

			Assertions.assertTrue(eventAttributeValues.contains(value));
		}

		bqEventPropertyValuePage =
			_eventPropertyDog.getBQEventPropertyValuePage(
				channel.getId(), eventAttributeDefinition.getId(),
				eventDefinition.getId(), "Attribute Value", 3, 1);

		Assertions.assertEquals(4, bqEventPropertyValuePage.getTotalElements());

		eventAttributeValues = bqEventPropertyValuePage.getContent();

		Assertions.assertEquals(1, eventAttributeValues.size());
	}

	@Autowired
	private BQEventDog _bqEventDog;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

	@Autowired
	private EventPropertyDog _eventPropertyDog;

}