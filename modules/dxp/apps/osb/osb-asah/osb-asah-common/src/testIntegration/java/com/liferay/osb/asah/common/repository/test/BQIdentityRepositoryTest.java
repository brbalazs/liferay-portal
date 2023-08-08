/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
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