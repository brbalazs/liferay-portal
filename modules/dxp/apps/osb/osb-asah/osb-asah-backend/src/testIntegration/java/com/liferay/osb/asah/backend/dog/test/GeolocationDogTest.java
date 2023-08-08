/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.constants.DataConstants;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQFormRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQJournalRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lino Alves
 */
public class GeolocationDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/geolocation_journal_info.json"
	)
	@Test
	public void testGeolocationMetrics() {
		List<Metric> geolocationMetrics = _metricDog.getGeolocationMetrics(
			JournalMetricType.VIEWS,
			new SearchQueryContext("1", AssetType.JOURNAL));

		Assertions.assertEquals(
			3, geolocationMetrics.size(), geolocationMetrics.toString());

		DogTestUtil.assertMetric(2, geolocationMetrics, "Australia");
		DogTestUtil.assertMetric(3, geolocationMetrics, "Brazil");
		DogTestUtil.assertMetric(2, geolocationMetrics, "Germany");
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQFormRepository.class,
		resourcePath = "osbasahcerebroinfo/forms_info.json"
	)
	@Test
	public void testUnknownGeolocationMetric() {
		List<Metric> geolocationMetrics = _metricDog.getGeolocationMetrics(
			FormMetricType.VIEWS,
			new SearchQueryContext("2", AssetType.FORM) {
				{
					setCountry(DataConstants.UNKNOWN);
				}
			});

		Assertions.assertEquals(
			1, geolocationMetrics.size(), geolocationMetrics.toString());

		DogTestUtil.assertMetric(1, geolocationMetrics, DataConstants.UNKNOWN);
	}

	@Autowired
	private MetricDog _metricDog;

}