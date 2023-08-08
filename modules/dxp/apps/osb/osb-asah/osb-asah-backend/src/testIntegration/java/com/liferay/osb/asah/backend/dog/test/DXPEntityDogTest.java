/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DXPEntityRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import org.json.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Matthew Kong
 */
public class DXPEntityDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_dxpEntityDog.deleteByType(DXPEntity.Type.GROUP);
		_dxpEntityDog.deleteByType(DXPEntity.Type.ORGANIZATION);
		_dxpEntityDog.deleteByType(DXPEntity.Type.ROLE);
		_dxpEntityDog.deleteByType(DXPEntity.Type.TEAM);
		_dxpEntityDog.deleteByType(DXPEntity.Type.USER);
		_dxpEntityDog.deleteByType(DXPEntity.Type.USER_GROUP);
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@Test
	public void testFindByAfterAndFieldsAndType() {
		List<DXPEntity> dxpEntities = _dxpEntityDog.findByAfterAndFieldsAndType(
			null, Collections.singletonMap("fields.screenName", "bruno.admin"),
			10, DXPEntity.Type.USER);

		Stream<DXPEntity> stream = dxpEntities.stream();

		stream.map(
			DXPEntity::getFieldsJSONObject
		).map(
			fieldsJSONObject ->
				fieldsJSONObject.optString("firstName") + " " +
					fieldsJSONObject.optString("lastName")
		).forEach(
			Assertions::assertNotNull
		);
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@Test
	public void testFindByFieldsAndType() {
		List<DXPEntity> dxpEntities = _dxpEntityDog.findByFieldsAndType(
			Collections.singletonMap("fields.screenName", "bruno.admin"),
			DXPEntity.Type.USER);

		Assertions.assertEquals(
			"Bruno Admin", _getUserName(dxpEntities.get(0)));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/groups.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetGroups() {
		_testGetDXPEntityPage(
			null, "groups", Arrays.asList("Global", "Guest"), 2, null,
			Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066676L, "groups", Arrays.asList("Global", "Guest"), 2,
			null, Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066677L, "groups", Collections.emptyList(), 0, null,
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/organizations.json"
	)
	@Test
	public void testGetOrganizations() {
		_testGetDXPEntityPage(
			null, "organizations",
			Arrays.asList("engineering", "marketing", "sales engineering"), 3,
			null, Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/organizations.json"
	)
	@Test
	public void testGetOrganizationsSearch() {
		_testGetDXPEntityPage(
			null, "organizations",
			Arrays.asList("engineering", "sales engineering"), 2, "engine",
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/roles.json"
	)
	@Test
	public void testGetRoles() {
		_testGetDXPEntityPage(
			null, "roles",
			Arrays.asList("Administrator", "Guest", "Owner", "Power User"), 4,
			null, Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/teams.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetTeams() {
		_testGetDXPEntityPage(
			414686271857066676L, "teams",
			Arrays.asList("teamA", "teamB", "teamC", "teamD"), 4, null,
			Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066677L, "teams", Arrays.asList("teamE"), 1, null,
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/user_groups.json"
	)
	@Test
	public void testGetUserGroups() {
		_testGetDXPEntityPage(
			null, "user-groups", Arrays.asList("Mac Users"), 1, null,
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetUsers() {
		_testGetDXPEntityPage(
			null, "users",
			Arrays.asList(
				"Bruno Admin", "Bruno Badmin", "Test1 Test1", "Test2 Test2",
				"Test3 Test3"),
			5, null, Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066676L, "users",
			Arrays.asList(
				"Bruno Admin", "Bruno Badmin", "Test1 Test1", "Test2 Test2"),
			4, null, Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066677L, "users", Arrays.asList("Test3 Test3"), 1, null,
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@Test
	public void testGetUsersCaseInsensitiveSearch() {
		_testGetDXPEntityPage(
			null, "users",
			Arrays.asList("Test1 Test1", "Test2 Test2", "Test3 Test3"), 3,
			"test", Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@Test
	public void testGetUsersSearch() {
		_testGetDXPEntityPage(
			null, "users",
			Arrays.asList("Test1 Test1", "Test2 Test2", "Test3 Test3"), 3,
			"Test", Sort.asc("fields.name"));
		_testGetDXPEntityPage(
			414686271857066676L, "users",
			Arrays.asList("Test1 Test1", "Test2 Test2"), 2, "Test",
			Sort.asc("fields.name"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@Test
	public void testGetUsersSearchAndSort() {
		_testGetDXPEntityPage(
			null, "users", Arrays.asList("Bruno Badmin", "Bruno Admin"), 2,
			"Bruno", Sort.desc("name"));
	}

	private String _getUserName(DXPEntity dxpEntity) {
		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		return String.format(
			"%s %s", fieldsJSONObject.optString("firstName", ""),
			fieldsJSONObject.optString("lastName", ""));
	}

	private void _testGetDXPEntityPage(
		Long channelId, String collectionName, List<String> expectedNames,
		int expectedTotal, String keywords, Sort sort) {

		Page<DXPEntity> dxpEntityPage = _dxpEntityDog.getDXPEntityPage(
			channelId, keywords, 10, sort, 0,
			DXPEntity.Type.ofCollectionName(collectionName));

		List<DXPEntity> dxpEntities = dxpEntityPage.getContent();

		if (StringUtils.equals(collectionName, "users")) {
			dxpEntities.forEach(
				dxpEntity -> dxpEntity.setName(_getUserName(dxpEntity)));
		}

		Assertions.assertEquals(
			expectedTotal, dxpEntityPage.getTotalElements(),
			dxpEntityPage.toString());

		Assertions.assertEquals(
			expectedNames,
			ListUtil.map(dxpEntityPage.getContent(), DXPEntity::getName));
	}

	@Autowired
	private DXPEntityDog _dxpEntityDog;

}