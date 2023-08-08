/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.BQIndividualMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.IndividualMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matthew Kong
 */
public class BQIndividualMetricDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "identity_activities_histogram_info.sql")
	@RepositoryResource(
		repositoryClass = BQIdentityRepository.class,
		resourcePath = "osbasahfaroinfo/identities_histogram_info.json"
	)
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_histogram_info.json"
	)
	@Test
	public void testIndividualMetric() {
		IndividualMetric individualMetric = _getIndividualMetric();

		Metric anonymousIndividualsMetric =
			individualMetric.getAnonymousIndividualsMetric();

		Assertions.assertEquals(
			0, anonymousIndividualsMetric.getPreviousValue(), 0);
		Assertions.assertEquals(1, anonymousIndividualsMetric.getValue(), 0);

		Metric knownIndividualsMetric =
			individualMetric.getKnownIndividualsMetric();

		Assertions.assertEquals(
			1, knownIndividualsMetric.getPreviousValue(), 0);
		Assertions.assertEquals(4, knownIndividualsMetric.getValue(), 0);

		Metric totalIndividualsMetric =
			individualMetric.getTotalIndividualsMetric();

		Assertions.assertEquals(
			1, totalIndividualsMetric.getPreviousValue(), 0);
		Assertions.assertEquals(5, totalIndividualsMetric.getValue(), 0);
	}

	private IndividualMetric _getIndividualMetric() {
		return _bqIndividualMetricDog.getIndividualMetric(
			new SearchQueryContext(AssetType.INDIVIDUAL_METRIC) {
				{
					setInterval("D");
					setRangeKey(30);
				}
			},
			new HashSet<String>() {
				{
					add(IndividualMetricType.ANONYMOUS_INDIVIDUALS.getName());
					add(IndividualMetricType.KNOWN_INDIVIDUALS.getName());
					add(IndividualMetricType.TOTAL_INDIVIDUALS.getName());
				}
			});
	}

	@Autowired
	private BQIndividualMetricDog _bqIndividualMetricDog;

}