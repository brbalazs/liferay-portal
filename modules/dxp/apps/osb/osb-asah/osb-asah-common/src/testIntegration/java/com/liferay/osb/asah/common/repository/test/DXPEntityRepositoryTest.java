/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DXPEntityRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Import(JDBCTestConfiguration.class)
public class DXPEntityRepositoryTest
	extends BaseRepositoryTestCase<DXPEntity, Long> {

	@BeforeEach
	public void setUp() throws Exception {
		DataSource dataSource = new DataSource("Liferay Brazil");

		dataSource.setCredentialType("Token Authentication");

		Channel channel = new Channel("channel1");

		channel.setId(11L);
		channel.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel);

		dataSource.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");
		dataSource.setId(123L);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("READY");
		dataSource.setStatus("STARTED");
		dataSource.setURL("");

		_dataSourceRepository.save(dataSource);

		DXPEntity dxpEntity1 = new DXPEntity();

		dxpEntity1.setDataSourceId(123L);
		dxpEntity1.setFieldsJSONObject(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/user_fields1.json", this));
		dxpEntity1.setType(DXPEntity.Type.USER);

		DXPEntity dxpEntity2 = new DXPEntity();

		dxpEntity2.setDataSourceId(123L);
		dxpEntity2.setFieldsJSONObject(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/user_fields2.json", this));
		dxpEntity2.setType(DXPEntity.Type.USER);

		DXPEntity dxpEntity3 = new DXPEntity();

		dxpEntity3.setDataSourceId(123L);
		dxpEntity3.setFieldsJSONObject(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/user_group_fields1.json", this));
		dxpEntity3.setModifiedDate(DateUtil.newDate());
		dxpEntity3.setType(DXPEntity.Type.USER_GROUP);

		setUpRepository(dxpEntity1, dxpEntity2, dxpEntity3);
	}

	@AfterEach
	@Override
	public void tearDown() {
		_dxpEntityRepository.deleteAll(entityModels);
	}

	@Override
	@Test
	public void testCount() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testCount);
	}

	@Override
	@Test
	public void testDelete() {
		DXPEntity dxpEntity = entityModels.get(0);

		_dxpEntityRepository.delete(dxpEntity);

		DXPEntity.Type type = dxpEntity.getType();

		List<DXPEntity> dxpEntities =
			_dxpEntityRepository.findByAfterAndFieldsAndType(
				null,
				new HashMap<String, Object>() {
					{
						put("dataSourceId", dxpEntity.getDataSourceId());
						put(
							"fields." + type.getIdFieldName(),
							dxpEntity.getIdFieldValue());
					}
				},
				1, type);

		Assertions.assertTrue(dxpEntities.isEmpty());
	}

	@Override
	@Test
	public void testDeleteAll1() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testDeleteAll1);
	}

	@Override
	@Test
	public void testDeleteAll2() {
		_dxpEntityRepository.deleteAll(entityModels);

		List<DXPEntity> dxpEntities = _dxpEntityRepository.findByFieldsAndType(
			new HashMap<String, Object>() {
				{
					put("dataSourceId", 123L);
					put(
						"fields." + DXPEntity.Type.USER.getIdFieldName(),
						ListUtil.map(entityModels, DXPEntity::getIdFieldValue));
				}
			},
			DXPEntity.Type.USER);

		Assertions.assertTrue(dxpEntities.isEmpty());
	}

	@Test
	public void testDeleteByFieldValue() {
		DXPEntity dxpEntity = entityModels.get(0);

		_dxpEntityRepository.deleteByFieldNameAndFieldValueAndType(
			"fields.emailAddress", "john.doe@liferay.com", DXPEntity.Type.USER);

		List<DXPEntity> dxpEntities = _dxpEntityRepository.findByFieldsAndType(
			new HashMap<String, Object>() {
				{
					put("dataSourceId", dxpEntity.getDataSourceId());
					put("fields.emailAddress", "john.doe@liferay.com");
				}
			},
			dxpEntity.getType());

		Assertions.assertTrue(dxpEntities.isEmpty());
	}

	@Override
	@Test
	public void testDeleteById() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testDeleteById);
	}

	@Override
	@Test
	public void testFindAll1() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testFindAll1);
	}

	@Override
	@Test
	public void testFindAll2() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testFindAll2);
	}

	@Override
	@Test
	public void testFindAll3() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testFindAll3);
	}

	@Override
	@Test
	public void testFindAllById() {
		Assertions.assertThrows(
			UnsupportedOperationException.class, super::testFindAllById);
	}

	@Test
	public void testFindByFieldsAndType() {
		List<DXPEntity> dxpEntities = _dxpEntityRepository.findByFieldsAndType(
			new HashMap<String, Object>() {
				{
					put("dataSourceId", 123);
					put("fields.contact.jobTitle", "electrician");
					put("fields.lastName", "Doe");
					put("fields.memberships." + _CLASS_NAME_GROUP, "20122");
				}
			},
			DXPEntity.Type.USER);

		Assertions.assertEquals(1, dxpEntities.size(), dxpEntities.toString());
	}

	@Test
	public void testFindByFieldsAndTypePaginated() {
		DXPEntity dxpEntity = entityModels.get(0);

		List<DXPEntity> dxpEntities =
			_dxpEntityRepository.findByAfterAndFieldsAndType(
				dxpEntity.getId(),
				Collections.singletonMap("fields.jobTitle", "electrician"), 2,
				DXPEntity.Type.USER);

		DXPEntity expectedDXPEntity = entityModels.get(1);
		DXPEntity actualDXPEntity = dxpEntities.get(0);

		Assertions.assertEquals(
			expectedDXPEntity.getId(), actualDXPEntity.getId(),
			dxpEntities.toString());
	}

	@Test
	public void testFindByMembershipIdAndType() {
		List<DXPEntity> dxpEntities =
			_dxpEntityRepository.findByMembershipClassNameAndMembershipId(
				DXPEntity.Type.GROUP.getClassName(), 20121L);

		Assertions.assertEquals(1, dxpEntities.size(), dxpEntities.toString());
	}

	@Test
	public void testSave() {
		super.testSave();

		DXPEntity dxpEntity = new DXPEntity();

		dxpEntity.setDataSourceId(123L);
		dxpEntity.setFieldsJSONObject(JSONUtil.put("name", "Test Group"));
		dxpEntity.setId(1L);
		dxpEntity.setIsNew(Boolean.TRUE);
		dxpEntity.setType(DXPEntity.Type.GROUP);

		Assertions.assertEquals(
			dxpEntity, _dxpEntityRepository.save(dxpEntity));

		DXPEntity organizationDXPEntity = new DXPEntity();

		organizationDXPEntity.setDataSourceId(123L);
		organizationDXPEntity.setFieldsJSONObject(
			JSONUtil.put("name", "Test Organization"));
		organizationDXPEntity.setId(2L);
		organizationDXPEntity.setIsNew(Boolean.TRUE);
		organizationDXPEntity.setType(DXPEntity.Type.ORGANIZATION);

		Assertions.assertEquals(
			organizationDXPEntity,
			_dxpEntityRepository.save(organizationDXPEntity));

		DXPEntity userDXPEntity = new DXPEntity();

		userDXPEntity.setDataSourceId(123L);
		userDXPEntity.setFieldsJSONObject(
			JSONUtil.put(
				"firstName", "Test"
			).put(
				"lastName", "Test"
			));
		userDXPEntity.setId(3L);
		userDXPEntity.setIsNew(Boolean.TRUE);
		userDXPEntity.setType(DXPEntity.Type.USER);

		Assertions.assertEquals(
			userDXPEntity, _dxpEntityRepository.save(userDXPEntity));
	}

	@Test
	public void testSearchByDataSourceIdsAndKeywordsAndTypeSortedByName() {
		List<DXPEntity> dxpEntities =
			_dxpEntityRepository.searchByDataSourceIdsAndKeywordsAndType(
				Arrays.asList(123L), null, DXPEntity.Type.USER,
				PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));

		Assertions.assertEquals(2, dxpEntities.size(), dxpEntities.toString());

		DXPEntity dxpEntity = dxpEntities.get(0);

		Assertions.assertEquals("Jane Doe", dxpEntity.getName());

		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		Assertions.assertEquals(
			"Jane", fieldsJSONObject.getString("firstName"));
		Assertions.assertEquals("Doe", fieldsJSONObject.getString("lastName"));

		dxpEntities =
			_dxpEntityRepository.searchByDataSourceIdsAndKeywordsAndType(
				Arrays.asList(123L), null, DXPEntity.Type.USER,
				PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name")));

		dxpEntity = dxpEntities.get(0);

		Assertions.assertEquals("John Doe", dxpEntity.getName());

		fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		Assertions.assertEquals(
			"John", fieldsJSONObject.getString("firstName"));
		Assertions.assertEquals("Doe", fieldsJSONObject.getString("lastName"));

		Assertions.assertEquals(2, dxpEntities.size(), dxpEntities.toString());

		dxpEntities =
			_dxpEntityRepository.searchByDataSourceIdsAndKeywordsAndType(
				Arrays.asList(123L), null, DXPEntity.Type.USER_GROUP,
				PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));

		dxpEntity = dxpEntities.get(0);

		Assertions.assertEquals("User Group 1", dxpEntity.getName());

		Assertions.assertEquals(1, dxpEntities.size(), dxpEntities.toString());
	}

	@Override
	protected PagingAndSortingRepository<DXPEntity, Long>
		getPagingAndSortingRepository() {

		return _dxpEntityRepository;
	}

	private static final String _CLASS_NAME_GROUP =
		"com.liferay.portal.kernel.model.Group";

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private DXPEntityRepository _dxpEntityRepository;

}