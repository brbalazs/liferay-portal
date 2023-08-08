/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQMembershipDog;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Michael Bowerman
 * @author Vishal Reddy
 */
public class BQMembershipDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@Test
	public void testDeleteBQMembership() {
		Assertions.assertEquals(
			3, _bqMembershipRepository.countBySegmentId(338511398116723458L));

		_bqMembershipRepository.countBySegmentId(338511398116723458L);

		Segment segment = new Segment();

		segment.setChannelId(1L);
		segment.setId(338511398116723458L);

		_bqMembershipDog.deleteBQMembership("abc338511398389279307", segment);

		Assertions.assertEquals(
			2, _bqMembershipRepository.countBySegmentId(338511398116723458L));
	}

	@BQSQLResource(resourcePath = "test_bq_active_memberships_count.sql")
	@Test
	public void testGetActiveDynamicMembershipsCountWithAnonymous() {
		Assertions.assertEquals(
			2, _bqMembershipDog.getActiveBQMembershipsCount(Boolean.TRUE, 2L));
	}

	@BQSQLResource(resourcePath = "test_bq_active_memberships_count.sql")
	@Test
	public void testGetActiveStaticMembershipsCount() {
		Assertions.assertEquals(
			1, _bqMembershipDog.getActiveBQMembershipsCount(Boolean.FALSE, 1L));
	}

	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipChangeRepository.class,
		resourcePath = "osbasahfaroinfo/bq_membership_changes.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testGetIndividualSegmentIndividualIds() {
		List<String> identityIds = _bqMembershipDog.getActiveIdentityIds(
			338511398116723458L);

		Assertions.assertEquals(2, identityIds.size(), identityIds.toString());
		Assertions.assertTrue(identityIds.contains("338486037253283140"));
		Assertions.assertTrue(identityIds.contains("338486041327913341"));
	}

	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@Test
	public void testIsMember() {
		Assertions.assertFalse(_bqMembershipDog.isMember("0", 0L));
		Assertions.assertTrue(
			_bqMembershipDog.isMember(
				"338486041327913341", 338511398116723458L));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithActivities() {
		String assetId =
			"da70dfa4d9f95ac979f921e8e623358236313f334afcd06cddf8a5621cf6a1e9";

		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day eq " +
					"''2022-12-16'')', operator='ge', value=1))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(not(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day eq " +
					"''2022-12-16'')', operator='ge', value=1)))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day gt " +
					"''2022-12-17'')', operator='ge', value=1))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(not(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day gt " +
					"''2022-12-17'')', operator='ge', value=1)))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			4L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day lt " +
					"''2022-12-17'')', operator='ge', value=1))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(not(activities.filterByCount(filter='(activityKey eq " +
				"''WebContent#webContentViewed#" + assetId + "'' and day lt " +
					"''2022-12-17'')', operator='ge', value=1)))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		assetId =
			"0e12831a7047f759733b21f028525039607350b1b1b4fe904595427e72ea0d9b";

		_bqMembershipDog.updateBQMemberships(
			"(activities.filterByCount(filter='(activityKey eq ''Blog#" +
				"commentPosted#" + assetId + "'' and day lt ''2022-12-17'')'" +
					", operator='ge', value=1))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithCustomFields() {
		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"contains(custom/Favorite_Food/value, 'Rice')", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"not contains(custom/Favorite_Food/value, 'Rice')", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"contains(custom/Favorite_Number/value, 3)", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"not contains(custom/Favorite_Number/value, 5)", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Favorite_Number/value ge 3", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Favorite_Number/value gt 2 and " +
				"custom/Favorite_Number/value lt 3",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Favorite_Number/value gt 3", Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Favorite_Number/value le 4", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Favorite_Number/value lt 2", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Hobbies/value eq 'ing'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Hobbies/value eq 'Exercise'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Hobbies/value ne 'Exercise'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Hobbies/value ne 'ing'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			4L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Salary/value ge 120000.30", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Salary/value gt 100000", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Salary/value le 100000.00", Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Salary/value lt 100001", Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithDateCustomFields() {
		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"custom/Joined_Date/value eq '2022-04-30'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Joined_Date/value ge '2022-04-30'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Joined_Date/value gt '2022-01-01'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			4L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Joined_Date/value le '2022-06-01'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			4L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Joined_Date/value lt '2022-05-03'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithDemographics() {
		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"contains(demographics/email/value, 'delta.com')", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"not(contains(demographics/email/value, 'delta.com'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(demographics/firstName/value eq 'Marcus')", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(demographics/jobTitle/value eq 'Engineer')", Boolean.TRUE,
			segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_2.sql")
	@Test
	public void testUpdateBQMembershipsWithDifferentChannelIds() {
		Segment segment1 = new Segment();

		segment1.setChannelId(1L);
		segment1.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"(organizations.filter(filter='(id eq ''23k92323l923lf0as'')'))",
			Boolean.TRUE, segment1);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment1.getId()));

		_bqMembershipDog.updateBQMemberships(
			"custom/Organization/value eq 'Developer'", Boolean.TRUE, segment1);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment1.getId()));

		Segment segment2 = new Segment();

		segment2.setChannelId(2L);
		segment2.setId(2L);

		_bqMembershipDog.updateBQMemberships(
			"custom/Organization/value eq 'Developer'", Boolean.TRUE, segment2);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment2.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_with_interest.sql")
	@Test
	public void testUpdateBQMembershipsWithInterest1() {
		Segment segment = new Segment();

		segment.setChannelId(1L);
		segment.setId(2L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		Page<BQMembership> bqMembershipPage =
			_bqMembershipDog.getBQMembershipPage(
				2L, null, 0, 20, new String[0]);

		List<BQMembership> bqMemberships = bqMembershipPage.getContent();

		Map<String, String> expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"abc-123",
			"761319ac0d9f6e0f3467ad26bc8c63989d06c5f491849d6aa12fabdbd6c6b7bb");

		_assertBQMemberships(bqMemberships, expectedIndividuals);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''false'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		bqMembershipPage = _bqMembershipDog.getBQMembershipPage(
			2L, null, 0, 20, new String[0]);

		bqMemberships = bqMembershipPage.getContent();

		expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"bcd-456",
			"5970d88ec4ed505177361de1b17a3f2debf7c4f630c14f075a823ec97942692a");
		expectedIndividuals.put(
			"efg-789",
			"5f20f61b2cfaa86c4f3cb3557751a702776af029deabed8e943fb55cfa604e34");
		expectedIndividuals.put(
			"ghi-101",
			"def73c7b1d2934d8bcdc8080a221c39df40e7ccfa499ad49d862138f5bc055f9");

		_assertBQMemberships(bqMemberships, expectedIndividuals);
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_with_interest.sql")
	@Test
	public void testUpdateBQMembershipsWithInterest2() {
		Segment segment2 = new Segment();

		segment2.setChannelId(1L);
		segment2.setId(2L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')') and interests.filter(filter='(name eq ''cloud'' " +
					"and score eq ''true'')'))",
			Boolean.TRUE, segment2);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment2.getId()));

		Segment segment3 = new Segment();

		segment3.setChannelId(1L);
		segment3.setId(3L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''metrics'' and score eq " +
				"''true'')') and interests.filter(filter='(name eq " +
					"''quality'' and score eq ''true'')'))",
			Boolean.TRUE, segment3);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment3.getId()));

		Page<BQMembership> bqMembershipPage =
			_bqMembershipDog.getBQMembershipPage(
				3L, null, 0, 20, new String[0]);

		List<BQMembership> bqMemberships = bqMembershipPage.getContent();

		Map<String, String> expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"efg-789",
			"5f20f61b2cfaa86c4f3cb3557751a702776af029deabed8e943fb55cfa604e34");

		_assertBQMemberships(bqMemberships, expectedIndividuals);

		Segment segment4 = new Segment();

		segment4.setChannelId(1L);
		segment4.setId(4L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')') and interests.filter(filter='(name eq ''cloud'' " +
					"and score eq ''false'')'))",
			Boolean.TRUE, segment4);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment4.getId()));

		bqMembershipPage = _bqMembershipDog.getBQMembershipPage(
			4L, null, 0, 20, new String[0]);

		bqMemberships = bqMembershipPage.getContent();

		expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"abc-123",
			"761319ac0d9f6e0f3467ad26bc8c63989d06c5f491849d6aa12fabdbd6c6b7bb");

		_assertBQMemberships(bqMemberships, expectedIndividuals);
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_with_interest.sql")
	@Test
	public void testUpdateBQMembershipsWithInterest3() {
		Segment segment2 = new Segment();

		segment2.setChannelId(1L);
		segment2.setId(2L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')') or interests.filter(filter='(name eq ''cloud'' " +
					"and score eq ''true'')'))",
			Boolean.TRUE, segment2);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment2.getId()));

		Page<BQMembership> bqMembershipPage =
			_bqMembershipDog.getBQMembershipPage(
				2L, null, 0, 20, new String[0]);

		List<BQMembership> bqMemberships = bqMembershipPage.getContent();

		Map<String, String> expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"abc-123",
			"761319ac0d9f6e0f3467ad26bc8c63989d06c5f491849d6aa12fabdbd6c6b7bb");
		expectedIndividuals.put(
			"bcd-456",
			"5970d88ec4ed505177361de1b17a3f2debf7c4f630c14f075a823ec97942692a");

		_assertBQMemberships(bqMemberships, expectedIndividuals);

		Segment segment3 = new Segment();

		segment3.setChannelId(1L);
		segment3.setId(3L);

		_bqMembershipDog.updateBQMemberships(
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')') or interests.filter(filter='(name eq ''cloud'' " +
					"and score eq ''false'')'))",
			Boolean.TRUE, segment3);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment3.getId()));

		bqMembershipPage = _bqMembershipDog.getBQMembershipPage(
			3L, null, 0, 20, new String[0]);

		bqMemberships = bqMembershipPage.getContent();

		expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"abc-123",
			"761319ac0d9f6e0f3467ad26bc8c63989d06c5f491849d6aa12fabdbd6c6b7bb");
		expectedIndividuals.put(
			"efg-789",
			"5f20f61b2cfaa86c4f3cb3557751a702776af029deabed8e943fb55cfa604e34");
		expectedIndividuals.put(
			"ghi-101",
			"def73c7b1d2934d8bcdc8080a221c39df40e7ccfa499ad49d862138f5bc055f9");

		_assertBQMemberships(bqMemberships, expectedIndividuals);
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_with_interest.sql")
	@Test
	public void testUpdateBQMembershipsWithInterest4() {
		Segment segment2 = new Segment();

		segment2.setChannelId(1L);
		segment2.setId(2L);

		_bqMembershipDog.updateBQMemberships(
			"(not contains(demographics/email/value, 'gamma.com') and " +
				"(interests.filter(filter='(name eq ''analytics'' and score " +
					"eq ''true'')') or interests.filter(filter='(name eq " +
						"''cloud'' and score eq ''false'')')))",
			Boolean.TRUE, segment2);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment2.getId()));

		Page<BQMembership> bqMembershipPage =
			_bqMembershipDog.getBQMembershipPage(
				2L, null, 0, 20, new String[0]);

		List<BQMembership> bqMemberships = bqMembershipPage.getContent();

		Map<String, String> expectedIndividuals = new HashMap<>();

		expectedIndividuals.put(
			"abc-123",
			"761319ac0d9f6e0f3467ad26bc8c63989d06c5f491849d6aa12fabdbd6c6b7bb");
		expectedIndividuals.put(
			"efg-789",
			"5f20f61b2cfaa86c4f3cb3557751a702776af029deabed8e943fb55cfa604e34");

		_assertBQMemberships(bqMemberships, expectedIndividuals);
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithMemberships() {
		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"groupIds ne '9823423jh23908234'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"roleIds eq '32oiaejf8e32433wr'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"roleIds ne '32oiaejf8e32433wr'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"userGroupIds eq 'newr87232kjhdsf89'", Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships.sql")
	@Test
	public void testUpdateBQMembershipsWithOrganization() {
		Segment segment = new Segment();

		segment.setChannelId(11L);
		segment.setId(1L);

		// Custom fields

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(custom/Organization_Type/value eq " +
				"''test'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(custom/Divisions/value ge 35)')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(custom/Divisions/value gt 35)')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(custom/Year/value le 2023)')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(custom/Year/value lt 2023)')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		// Hierarchy path known/unknown

		_bqMembershipDog.updateBQMemberships(
			"(organizations.filter(filter='(hierarchyPath ne null)'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(organizations.filter(filter='(hierarchyPath eq null)'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		// Id

		_bqMembershipDog.updateBQMemberships(
			"(organizations.filter(filter='(id eq ''23k92323l923lf0as'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		// Modified date

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(modifiedDate eq ''2022-12-18'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(modifiedDate gt ''2022-12-17'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(modifiedDate lt ''2022-12-17'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		// Name

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(name eq ''Organization 1'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"organizations.filter(filter='(name ne ''Organization 1'')')",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			0L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	@BQSQLResource(resourcePath = "test_bq_memberships_sessions.sql")
	@Test
	public void testUpdateBQMembershipsWithSessions() {
		Segment segment = new Segment();

		segment.setChannelId(1L);
		segment.setId(1L);

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(contains(context/referrer, " +
				"''facebook.com'') and between(completeDate, ''2022-08-30'', " +
					"''2050-09-02''))'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(contains(context/referrer, " +
				"''facebook.com'') and completeDate gt ''2022-09-01'')'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(contains(context/referrer, " +
				"''facebook.com'') and completeDate gt ''2022-09-01'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/referrer eq " +
				"''https://facebook.com'' and completeDate gt " +
					"''2022-09-01'')'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			1L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/referrer eq " +
				"''https://facebook.com'' and completeDate gt " +
					"''2022-09-01'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(contains(context/url, ''url2.com'') " +
				"and completeDate gt ''2022-09-01'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			5L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/url eq ''https://url2.com'' " +
				"and completeDate gt ''2022-09-01'')'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/browserName eq ''Chrome'')'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/deviceType ne ''Phone'')'))",
			Boolean.TRUE, segment);

		Assertions.assertEquals(
			3L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));

		_bqMembershipDog.updateBQMemberships(
			"(sessions.filter(filter='(context/platformName eq ''Linux'')'))",
			Boolean.FALSE, segment);

		Assertions.assertEquals(
			2L, _bqMembershipDog.getBQMembershipsCount(segment.getId()));
	}

	private void _assertBQMemberships(
		List<BQMembership> bqMemberships,
		Map<String, String> expectedIndividuals) {

		for (BQMembership bqMembership : bqMemberships) {
			Assertions.assertEquals(
				expectedIndividuals.get(bqMembership.getIdentityId()),
				bqMembership.getIndividualId());

			expectedIndividuals.remove(bqMembership.getIdentityId());
		}

		Assertions.assertEquals(0, expectedIndividuals.size());
	}

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

}