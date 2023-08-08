/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.date.DateUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marcos Martins
 */
public class AnalyticsEventsMessageTest {

	@BeforeEach
	public void setUp() {
		ValidatorFactory validatorFactory =
			Validation.buildDefaultValidatorFactory();

		_validator = validatorFactory.getValidator();
	}

	@Test
	public void testAnalyticsEventsMessageEventWithFutureEventDate() {
		AnalyticsEventsMessage.Event event =
			new AnalyticsEventsMessage.Event() {
				{
					setApplicationId("Page");
					setEventDate(DateUtil.addDays(new Date(), 1));
					setEventId("pageViewed");
				}
			};

		Set<ConstraintViolation<AnalyticsEventsMessage.Event>>
			constraintViolations = _validator.validate(event);

		Assertions.assertEquals(1, constraintViolations.size());

		ConstraintViolation<AnalyticsEventsMessage.Event> constraintViolation =
			_getFirstConstraintViolation(constraintViolations);

		Assertions.assertEquals(
			"Date must be within the last hour",
			constraintViolation.getMessage());
	}

	@Test
	public void testAnalyticsEventsMessageEventWithPastEventDate1() {
		AnalyticsEventsMessage.Event event =
			new AnalyticsEventsMessage.Event() {
				{
					setApplicationId("Page");
					setEventDate(DateUtil.addDays(new Date(), -1));
					setEventId("pageViewed");
				}
			};

		Set<ConstraintViolation<AnalyticsEventsMessage.Event>>
			constraintViolations = _validator.validate(event);

		Assertions.assertEquals(1, constraintViolations.size());

		ConstraintViolation<AnalyticsEventsMessage.Event> constraintViolation =
			_getFirstConstraintViolation(constraintViolations);

		Assertions.assertEquals(
			"Date must be within the last hour",
			constraintViolation.getMessage());
	}

	@Test
	public void testAnalyticsEventsMessageEventWithPastEventDate2() {
		AnalyticsEventsMessage.Event event =
			new AnalyticsEventsMessage.Event() {
				{
					setApplicationId("Page");
					setEventDate(new Date());
					setEventId("pageViewed");
				}
			};

		Set<ConstraintViolation<AnalyticsEventsMessage.Event>>
			constraintViolations = _validator.validate(event);

		Assertions.assertTrue(constraintViolations.isEmpty());
	}

	@Test
	public void testAnalyticsEventsMessageWithInvalidContext() {
		AnalyticsEventsMessage analyticsEventsMessage =
			new AnalyticsEventsMessage();

		analyticsEventsMessage.setContext(
			Collections.singletonMap(
				RandomStringUtils.random(8), RandomStringUtils.random(2049)));
		analyticsEventsMessage.setDataSourceId("1");
		analyticsEventsMessage.setEvents(
			Arrays.asList(
				new AnalyticsEventsMessage.Event() {
					{
						setApplicationId("1");
						setEventDate(new Date());
						setEventId("1");
						setProperties(
							Collections.singletonMap(
								RandomStringUtils.random(8),
								RandomStringUtils.random(8)));
					}
				}));
		analyticsEventsMessage.setUserId("1");

		Set<ConstraintViolation<AnalyticsEventsMessage>> violations =
			_validator.validate(analyticsEventsMessage);

		Assertions.assertFalse(violations.isEmpty());
	}

	private <T> ConstraintViolation<T> _getFirstConstraintViolation(
		Set<ConstraintViolation<T>> constraintViolations) {

		Iterator<ConstraintViolation<T>> iterator =
			constraintViolations.iterator();

		return iterator.next();
	}

	private Validator _validator;

}