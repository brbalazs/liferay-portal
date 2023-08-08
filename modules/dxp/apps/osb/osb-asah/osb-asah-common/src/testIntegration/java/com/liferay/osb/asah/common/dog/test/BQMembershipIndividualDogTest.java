/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQMembershipIndividualDog;
import com.liferay.osb.asah.common.entity.BQMembershipIndividual;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Marcellus Tavares
 */
public class BQMembershipIndividualDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_update_membership_individuals.sql")
	@Test
	public void testUpdateMembershipIndividuals1() {
		_bqMembershipIndividualDog.updateMembershipIndividuals();

		Page<BQMembershipIndividual> membershipIndividualPage =
			_bqMembershipIndividualDog.getMembershipIndividualPage(
				0, 1L, 10, new String[] {"dateModified", "desc"});

		Assertions.assertEquals(2, membershipIndividualPage.getTotalElements());

		List<BQMembershipIndividual> membershipIndividuals =
			membershipIndividualPage.getContent();

		BQMembershipIndividual membershipIndividual1 =
			membershipIndividuals.get(0);

		Assertions.assertEquals("A", membershipIndividual1.getIndividualId());

		List<BQMembershipIndividual.DataSourceUUID> dataSourceUUIDs1 =
			membershipIndividual1.getDataSourceUUIDs();

		Assertions.assertEquals(2, dataSourceUUIDs1.size());

		BQMembershipIndividual membershipIndividual2 =
			membershipIndividuals.get(1);

		Assertions.assertEquals("C", membershipIndividual2.getIndividualId());

		Assertions.assertEquals(
			Arrays.asList(
				new BQMembershipIndividual.DataSourceUUID(1L, "uuid-3")),
			membershipIndividual2.getDataSourceUUIDs());
	}

	@BQSQLResource(resourcePath = "test_bq_update_membership_individuals.sql")
	@Test
	public void testUpdateMembershipIndividuals2() {
		_bqMembershipIndividualDog.updateMembershipIndividuals(2L);

		Page<BQMembershipIndividual> membershipIndividualPage =
			_bqMembershipIndividualDog.getMembershipIndividualPage(
				0, 2L, 10, new String[] {"dateModified", "desc"});

		Assertions.assertEquals(1, membershipIndividualPage.getTotalElements());
	}

	@Autowired
	private BQMembershipIndividualDog _bqMembershipIndividualDog;

}