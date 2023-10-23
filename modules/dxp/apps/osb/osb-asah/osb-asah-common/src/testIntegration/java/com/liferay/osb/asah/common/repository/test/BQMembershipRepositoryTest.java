/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.ZoneId;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author Inácio Nery
 */
@Import(JDBCTestConfiguration.class)
public class BQMembershipRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testCountActiveMembersBySegmentId() {
		Assertions.assertEquals(
			2,
			_bqMembershipRepository.countActiveMembersBySegmentId(
				false, 34L, ZoneId.of("UTC")));
	}

	@BQSQLResource(
		resourcePath = "test_bq_membership_repository_with_suppression_bq.sql"
	)
	@Test
	public void testCountActiveMembersBySegmentIdWithSuppression() {
		Assertions.assertEquals(
			3,
			_bqMembershipRepository.countActiveMembersBySegmentId(
				false, 123L, ZoneId.of("UTC")));
		Assertions.assertEquals(
			5,
			_bqMembershipRepository.countActiveMembersBySegmentId(
				true, 123L, ZoneId.of("UTC")));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testCountByIdentityIdInAndSegmentIdAndStatus() {
		Assertions.assertEquals(
			2,
			_bqMembershipRepository.countByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 34L, "ACTIVE"));
		Assertions.assertEquals(
			0,
			_bqMembershipRepository.countByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 34L, "INACTIVE"));
		Assertions.assertEquals(
			1,
			_bqMembershipRepository.countByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 56L, "INACTIVE"));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testCountBySegmentIdAndStatus() {
		Assertions.assertEquals(
			3,
			_bqMembershipRepository.countBySegmentIdAndStatus(34L, "ACTIVE"));
		Assertions.assertEquals(
			0,
			_bqMembershipRepository.countBySegmentIdAndStatus(34L, "INACTIVE"));
		Assertions.assertEquals(
			1,
			_bqMembershipRepository.countBySegmentIdAndStatus(56L, "INACTIVE"));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testDeleteBySegmentIdIn() {
		List<BQMembership> bqMemberships = IterableUtils.toList(
			_bqMembershipRepository.findAll());

		Stream<BQMembership> stream = bqMemberships.stream();

		Assertions.assertTrue(
			stream.anyMatch(bqMembership -> bqMembership.getSegmentId() == 34));

		_bqMembershipRepository.deleteBySegmentIdIn(Arrays.asList(34L));

		bqMemberships = IterableUtils.toList(_bqMembershipRepository.findAll());

		stream = bqMemberships.stream();

		Assertions.assertFalse(
			stream.anyMatch(bqMembership -> bqMembership.getSegmentId() == 34));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testExistsByIdentityIdAndSegmentIdAndStatus() {
		Assertions.assertTrue(
			_bqMembershipRepository.existsByIdentityIdAndSegmentIdAndStatus(
				"12", 34L, "ACTIVE"));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindByIdentityIdAndStatus() {
		List<BQMembership> bqMemberships =
			_bqMembershipRepository.findByIdentityIdAndStatus("12", "ACTIVE");

		Assertions.assertEquals(
			1, bqMemberships.size(), bqMemberships.toString());
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindByIdentityIdInAndSegmentIdAndStatus() {
		List<BQMembership> bqMemberships =
			_bqMembershipRepository.findByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 34L, "ACTIVE",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			2, bqMemberships.size(), bqMemberships.toString());

		bqMemberships =
			_bqMembershipRepository.findByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 34L, "INACTIVE",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			0, bqMemberships.size(), bqMemberships.toString());

		bqMemberships =
			_bqMembershipRepository.findByIdentityIdInAndSegmentIdAndStatus(
				Arrays.asList("12", "78"), 56L, "INACTIVE",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			1, bqMemberships.size(), bqMemberships.toString());
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindByIndividualIdAndSegmentIdInAndStatus() {
		List<BQMembership> bqMemberships =
			_bqMembershipRepository.findByIndividualIdAndSegmentIdInAndStatus(
				"12", Arrays.asList(34L, 56L), "ACTIVE");

		Assertions.assertEquals(
			2, bqMemberships.size(), bqMemberships.toString());
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindBySegmentIdAndStatus() {
		List<BQMembership> bqMemberships =
			_bqMembershipRepository.findBySegmentIdAndStatus(
				34L, "ACTIVE",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			3, bqMemberships.size(), bqMemberships.toString());

		bqMemberships = _bqMembershipRepository.findBySegmentIdAndStatus(
			34L, "INACTIVE",
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			0, bqMemberships.size(), bqMemberships.toString());

		bqMemberships = _bqMembershipRepository.findBySegmentIdAndStatus(
			56L, "INACTIVE",
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(
			1, bqMemberships.size(), bqMemberships.toString());
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindIdentityIdBySegmentIdAndStatus() {
		List<String> identityIds =
			_bqMembershipRepository.findIdentityIdBySegmentIdAndStatus(
				34L, "ACTIVE");

		Assertions.assertEquals(3, identityIds.size(), identityIds.toString());
		Assertions.assertEquals("11", identityIds.get(0));
		Assertions.assertEquals("12", identityIds.get(1));
		Assertions.assertEquals("78", identityIds.get(2));

		identityIds =
			_bqMembershipRepository.findIdentityIdBySegmentIdAndStatus(
				34L, "INACTIVE");

		Assertions.assertEquals(0, identityIds.size(), identityIds.toString());

		identityIds =
			_bqMembershipRepository.findIdentityIdBySegmentIdAndStatus(
				56L, "INACTIVE");

		Assertions.assertEquals(1, identityIds.size(), identityIds.toString());
		Assertions.assertEquals("12", identityIds.get(0));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindIdentityIdBySegmentIdIn() {
		List<String> identityIds =
			_bqMembershipRepository.findIdentityIdBySegmentIdIn(
				Arrays.asList(34L, 56L), 10, 1, true);

		Assertions.assertEquals(Arrays.asList("11", "78", "12"), identityIds);

		identityIds = _bqMembershipRepository.findIdentityIdBySegmentIdIn(
			Arrays.asList(34L, 56L), 10, 1, false);

		Assertions.assertEquals(Arrays.asList("12", "11", "78"), identityIds);

		identityIds = _bqMembershipRepository.findIdentityIdBySegmentIdIn(
			Arrays.asList(34L, 56L), 10, 2, true);

		Assertions.assertEquals(Arrays.asList("12"), identityIds);

		identityIds = _bqMembershipRepository.findIdentityIdBySegmentIdIn(
			Arrays.asList(34L, 56L), 1, 1, true);

		Assertions.assertEquals(Arrays.asList("11", "78"), identityIds);
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindSegmentIdByIdentityIdAndStatus() {
		List<Long> segmentIds =
			_bqMembershipRepository.findSegmentIdByIdentityIdAndStatus(
				"12", "ACTIVE");

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(34, segmentIds.get(0));

		segmentIds = _bqMembershipRepository.findSegmentIdByIdentityIdAndStatus(
			"78", "INACTIVE");

		Assertions.assertEquals(0, segmentIds.size(), segmentIds.toString());

		segmentIds = _bqMembershipRepository.findSegmentIdByIdentityIdAndStatus(
			"12", "INACTIVE");

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(56, segmentIds.get(0));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindSegmentIdByIdentityIdInAndStatus() {
		List<Long> segmentIds =
			_bqMembershipRepository.findSegmentIdByIdentityIdInAndStatus(
				Arrays.asList("12", "78"), "ACTIVE");

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(34, segmentIds.get(0));

		segmentIds =
			_bqMembershipRepository.findSegmentIdByIdentityIdInAndStatus(
				Arrays.asList("12", "78"), "INACTIVE");

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(56, segmentIds.get(0));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindSegmentIdIdentitiesCountByIndividualIdAndStatus() {
		List<Map<String, Long>> segmentIdIdentitiesCounts =
			_bqMembershipRepository.
				findSegmentIdIdentitiesCountByIndividualIdAndStatus(
					"12", "ACTIVE");

		Assertions.assertEquals(
			1, segmentIdIdentitiesCounts.size(),
			segmentIdIdentitiesCounts.toString());

		Map<String, Long> segmentIdIdentitiesCount =
			segmentIdIdentitiesCounts.get(0);

		Assertions.assertEquals(34, segmentIdIdentitiesCount.get("segmentId"));
		Assertions.assertEquals(
			37, segmentIdIdentitiesCount.get("identitiesCount"));

		segmentIdIdentitiesCounts =
			_bqMembershipRepository.
				findSegmentIdIdentitiesCountByIndividualIdAndStatus(
					"12", "INACTIVE");

		Assertions.assertEquals(
			1, segmentIdIdentitiesCounts.size(),
			segmentIdIdentitiesCounts.toString());

		segmentIdIdentitiesCount = segmentIdIdentitiesCounts.get(0);

		Assertions.assertEquals(56, segmentIdIdentitiesCount.get("segmentId"));
		Assertions.assertEquals(
			27, segmentIdIdentitiesCount.get("identitiesCount"));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testFindTop20SegmentIdByIndividualId() {
		List<Long> segmentIds =
			_bqMembershipRepository.findTop20SegmentIdByIndividualId("12");

		Assertions.assertEquals(2, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(34, segmentIds.get(0));
		Assertions.assertEquals(56, segmentIds.get(1));

		segmentIds = _bqMembershipRepository.findTop20SegmentIdByIndividualId(
			"78");

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());
		Assertions.assertEquals(34, segmentIds.get(0));
	}

	@BQSQLResource(resourcePath = "test_bq_membership_repository_bq.sql")
	@SQLResource(resourcePath = "test_bq_membership_repository.sql")
	@Test
	public void testGetMembershipCountSnapshot() {
		MembershipCountSnapshot membershipCountSnapshot =
			_bqMembershipRepository.getMembershipCountSnapshot(34L, 34L);

		Assertions.assertEquals(
			2L, membershipCountSnapshot.getIdentitiesCount());
		Assertions.assertEquals(
			1L, membershipCountSnapshot.getIndividualsCount());
		Assertions.assertEquals(34L, membershipCountSnapshot.getSegmentId());
	}

	@BQSQLResource(resourcePath = "test_update_membership.sql")
	@Test
	public void testUpdateMembershipEventFilter() {
		_bqMembershipRepository.updateBQMemberships(
			1L,
			"(activities.filterByCount(filter='(activityKey eq " +
				"''Page#pageViewed#a57a8091bb47fec9165d5059ac0d020baee46475ef" +
					"05f632ff6b90ff8f720745'' and day gt ''last24Hours'')" +
						"',operator='ge',value=1))",
			true, 111L);

		Assertions.assertEquals(
			Collections.singletonList("identity1"),
			_bqMembershipRepository.findIdentityIdBySegmentIdAndStatus(
				111L, "ACTIVE"));
	}

	@BQSQLResource(
		resourcePath = "test_update_membership_with_suppressed_individual_bq.sql"
	)
	@SQLResource(
		resourcePath = "test_update_membership_with_suppressed_individual.sql"
	)
	@Test
	public void testUpdateMembershipWithSuppressedIndividual() {
		_bqMembershipRepository.updateBQMemberships(
			1L, "(((demographics/jobTitle/value ne null)))", true, 111L);

		List<BQMembership> bqMemberships =
			_bqMembershipRepository.findBySegmentIdAndStatus(
				111L, "ACTIVE", PageRequest.of(0, 20));

		Assertions.assertEquals(6, bqMemberships.size());

		Assertions.assertEquals(
			Arrays.asList(
				"identity1", "identity10", "identity2", "identity4",
				"identity5", "identity8"),
			ListUtil.map(bqMemberships, BQMembership::getIdentityId));

		Stream<BQMembership> stream = bqMemberships.stream();

		Assertions.assertEquals(
			Arrays.asList("1", "2", "4", "8"),
			stream.map(
				BQMembership::getIndividualId
			).filter(
				StringUtils::isNotBlank
			).collect(
				Collectors.toList()
			));
	}

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

}