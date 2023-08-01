/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Ivica Cardic
 */
@Import(JDBCTestConfiguration.class)
public class BQIdentityRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_identity_repository.sql")
	@Test
	public void testGetIndividualsCount() {
		LocalDate localDate = LocalDate.now();

		Assertions.assertEquals(
			1,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate,
				IndividualMetricType.ANONYMOUS_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			4,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate, IndividualMetricType.KNOWN_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			5,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate, IndividualMetricType.TOTAL_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			4,
			_bqIdentityRepository.getBQIndividualsCount(
				true, 1L, localDate, IndividualMetricType.TOTAL_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
	}

	@BQSQLResource(
		resourcePath = "test_bq_identity_repository_with_suppression.sql"
	)
	@Test
	public void testGetIndividualsCountWithSuppression() {
		LocalDate localDate = LocalDate.now();

		Assertions.assertEquals(
			4,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate,
				IndividualMetricType.ANONYMOUS_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			3,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate, IndividualMetricType.KNOWN_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			7,
			_bqIdentityRepository.getBQIndividualsCount(
				false, 1L, localDate, IndividualMetricType.TOTAL_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
		Assertions.assertEquals(
			4,
			_bqIdentityRepository.getBQIndividualsCount(
				true, 1L, localDate, IndividualMetricType.TOTAL_INDIVIDUALS,
				_timeZoneDog.getZoneId()),
			0);
	}

	@Autowired
	private BQIdentityRepository _bqIdentityRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}