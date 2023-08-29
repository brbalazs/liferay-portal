/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahDuplicateNameException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahNameException;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Michael Bowerman
 */
public class SegmentDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@SQLResource(resourcePath = "test_referenced_objects.sql")
	@Test
	public void testAddSegment() {
		Exception exception = Assertions.assertThrows(
			OSBAsahNameException.class, () -> _addSegment(1L, " "));

		Assertions.assertEquals("Name cannot be blank", exception.getMessage());

		String segmentName = "Segment 1";

		exception = Assertions.assertThrows(
			OSBAsahDuplicateNameException.class,
			() -> {
				_addSegment(1L, segmentName.toLowerCase());
				_addSegment(1L, segmentName);
			});

		Assertions.assertEquals("Name is already used", exception.getMessage());
	}

	@SQLResource(resourcePath = "test_referenced_objects.sql")
	@Test
	public void testEditSegmentNameToAnExistingOne() {
		_addSegment(1L, "Segment 1");

		Segment segment2 = _addSegment(1L, "Segment 2");

		Exception exception = Assertions.assertThrows(
			OSBAsahDuplicateNameException.class,
			() -> {
				segment2.setName("Segment 1");

				_segmentDog.updateSegment(segment2, segment2.getId());
			});

		Assertions.assertEquals("Name is already used", exception.getMessage());
	}

	@BQSQLResource(resourcePath = "test_get_bq_individual_segments_bq.sql")
	@SQLResource(resourcePath = "test_get_bq_individual_segments.sql")
	@Test
	public void testGetBQIndividualSegments() {
		List<Segment> segments = _segmentDog.getBQIndividualSegments(
			"47dcdddfd3819371e7b5ff33a62ebedd0db27fd651a5b5810488b36dd8818532");

		Assertions.assertEquals(
			Arrays.asList(12345L, 23456L, 45678L, 56789L),
			ListUtil.map(segments, Segment::getId));

		segments = _segmentDog.getBQIndividualSegments(
			"47ff64395860b1d498241d907069f649b98c198a95b3ba5303b87094058590c1");

		Assertions.assertTrue(segments.isEmpty());
	}

	@BQSQLResource(resourcePath = "test_referenced_objects_bq.sql")
	@SQLResource(resourcePath = "test_referenced_objects.sql")
	@Test
	public void testReferencedObjects() {
		Segment segment = new Segment();

		String id =
			"f8638b979b2f4f793ddb6dbd197e0ee25a7a6ea32b0ae22f5e3c5d119d839e75";

		segment.setFilter(
			"(activities.filterByCount(filter='(activityKey eq " +
				"''Form#formViewed#" + id + "'' and day gt ''last24Hours'')'," +
					"operator='ge',value=1))");

		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 1");
		segment.setType(Segment.Type.DYNAMIC);

		segment = _segmentDog.addSegment(segment);

		Set<String> referencedAssetIds = segment.getReferencedAssetIds();

		Assertions.assertEquals(1L, referencedAssetIds.size());
		Assertions.assertTrue(referencedAssetIds.contains(id));

		Set<Long> referencedDataSourceIds =
			segment.getReferencedDataSourceIds();

		Assertions.assertEquals(1L, referencedDataSourceIds.size());
		Assertions.assertTrue(referencedDataSourceIds.contains(123456789L));

		segment = new Segment();

		id = "c4f359787171fadfbcb37c96c324254260e0d164d03dd3384bd0d8eee6976cf5";

		segment.setFilter("(groupIds eq '" + id + "')");

		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 2");
		segment.setType(Segment.Type.DYNAMIC);

		segment = _segmentDog.addSegment(segment);

		Set<String> referencedGroupIds = segment.getReferencedGroupIds();

		Assertions.assertEquals(1L, referencedGroupIds.size());
		Assertions.assertTrue(referencedGroupIds.contains(id));

		referencedDataSourceIds = segment.getReferencedDataSourceIds();

		Assertions.assertEquals(1L, referencedDataSourceIds.size());
		Assertions.assertTrue(referencedDataSourceIds.contains(123456789L));

		segment = new Segment();

		id = "49b58366cb287f8d67cbb53ec1666cbc7e479d5d769c5dacadfc2b8981f287b0";

		segment.setFilter(
			"(organizations.filter(filter='(id eq ''" + id + "'')'))");

		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 3");
		segment.setType(Segment.Type.DYNAMIC);

		segment = _segmentDog.addSegment(segment);

		Set<String> referencedOrganizationIds =
			segment.getReferencedOrganizationIds();

		Assertions.assertEquals(1L, referencedOrganizationIds.size());
		Assertions.assertTrue(referencedOrganizationIds.contains(id));

		referencedDataSourceIds = segment.getReferencedDataSourceIds();

		Assertions.assertEquals(1L, referencedDataSourceIds.size());
		Assertions.assertTrue(referencedDataSourceIds.contains(123456789L));

		segment = new Segment();

		String roleId =
			"75524e028bbe9a84278b7b6285362da90600545f4e8f08a74568bff12d5a2760";
		String teamId =
			"2ca148650ffe346c482251a3b6e76dbb705d1eed0ebfa564d202fad2f0df0966";
		String userGroupId =
			"a5c97f6263b76cc59857c88139a3ed110ccb9bde454d5c2cde6556c12191337e";
		String userId =
			"e8931884a5cfc10b436de0bb5c35a0a37b7574c1f854c46a4cf3a7e522f7ecb2";

		segment.setFilter(
			"((roleIds eq '" + roleId + "') and (teamIds eq '" + teamId +
				"') and (userGroupIds ne '" + userGroupId + "') or (userId " +
					"eq '" + userId + "'))");

		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 4");
		segment.setType(Segment.Type.DYNAMIC);

		segment = _segmentDog.addSegment(segment);

		Set<String> referencedRoleIds = segment.getReferencedRoleIds();

		Assertions.assertEquals(1L, referencedRoleIds.size());
		Assertions.assertTrue(referencedRoleIds.contains(roleId));

		Set<String> referencedTeamIds = segment.getReferencedTeamIds();

		Assertions.assertEquals(1L, referencedTeamIds.size());
		Assertions.assertTrue(referencedTeamIds.contains(teamId));

		Set<String> referencedUserGroupIds =
			segment.getReferencedUserGroupIds();

		Assertions.assertEquals(1L, referencedUserGroupIds.size());
		Assertions.assertTrue(referencedUserGroupIds.contains(userGroupId));

		Set<String> referencedUserIds = segment.getReferencedUserIds();

		Assertions.assertEquals(1L, referencedUserIds.size());
		Assertions.assertTrue(referencedUserIds.contains(userId));

		referencedDataSourceIds = segment.getReferencedDataSourceIds();

		Assertions.assertEquals(1L, referencedDataSourceIds.size());
		Assertions.assertTrue(referencedDataSourceIds.contains(123456789L));

		segment = new Segment();

		segment.setFilter(
			"((organizations.filter(filter='(custom/Custom_Field/value eq ''" +
				"custom'')')) and demographics/email/value ne null)");
		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 5");
		segment.setType(Segment.Type.DYNAMIC);

		segment = _segmentDog.addSegment(segment);

		Set<String> referencedFieldMappingFieldNames =
			segment.getReferencedFieldMappingFieldNames();

		Assertions.assertEquals(2L, referencedFieldMappingFieldNames.size());
		Assertions.assertTrue(
			referencedFieldMappingFieldNames.contains("Custom_Field"));
		Assertions.assertTrue(
			referencedFieldMappingFieldNames.contains("emailAddress"));
	}

	@BQSQLResource(resourcePath = "test_referenced_objects_bq.sql")
	@SQLResource(resourcePath = "test_referenced_objects.sql")
	@Test
	public void testSearchSegment() {
		Segment segment = new Segment();

		segment.setChannelId(1L);
		segment.setFilter("demographics/email/value ne null");
		segment.setId(1L);
		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setIdentitiesCount(1L);
		segment.setIndividualsCount(1L);
		segment.setIsNew(Boolean.TRUE);
		segment.setModifiedDate(new Date());
		segment.setName("Segment 1");
		segment.setType(Segment.Type.DYNAMIC);

		_segmentDog.addSegment(segment);

		Page<Segment> segmentsPage = _segmentDog.searchSegmentPage(
			1L, "individualCount ge 1", 0, 1, null);

		List<Segment> segments = segmentsPage.getContent();

		Assertions.assertEquals(1, segments.size());
	}

	private Segment _addSegment(Long channelId, String name) {
		Segment segment = new Segment();

		segment.setChannelId(channelId);
		segment.setFilter("demographics/email/value ne null");
		segment.setIncludeAnonymousUsers(Boolean.FALSE);
		segment.setModifiedDate(new Date());
		segment.setName(name);
		segment.setType(Segment.Type.DYNAMIC);

		return _segmentDog.addSegment(segment);
	}

	@Autowired
	private SegmentDog _segmentDog;

}