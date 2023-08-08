/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.http.ChannelHttp;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author André Miranda
 */
public class DataSourceDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddDataSourceWithDefaultChannel() {
		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		Assertions.assertNotNull(
			_dataSourceDog.fetchDefaultChannelId(dataSource.getId()));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testChannelsCleared() {
		_dataSourceDog.disconnectDataSource(405201047787757795L);

		List<Channel> channels = _channelRepository.findByDataSourceId(
			405201047787757795L);

		Assertions.assertEquals(0, channels.size());

		channels = IterableUtils.toList(_channelRepository.findAll());

		Assertions.assertEquals(3, channels.size());

		for (Channel channel : channels) {
			for (ChannelDataSource channelDataSource :
					channel.getChannelDataSources()) {

				Assertions.assertNotEquals(
					405201047787757795L, channelDataSource.getDataSourceId());
			}
		}
	}

	@Test
	public void testDisconnectDataSource() {
		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource(
				"Token Authentication", "Liferay", "http://localhost:8080"));

		Assertions.assertEquals("CREDENTIALS_VALID", dataSource.getState());
		Assertions.assertEquals("ACTIVE", dataSource.getStatus());

		dataSource = _dataSourceDog.disconnectDataSource(dataSource.getId());

		Assertions.assertEquals("DISCONNECTED", dataSource.getState());
		Assertions.assertEquals("INACTIVE", dataSource.getStatus());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testFieldMappingsCleared() throws Exception {
		DataSource dataSource = _dataSourceDog.fetchDataSource(
			405201047787757795L);

		_dataSourceDog.deleteDataSource(dataSource);

		// TODO Assert field mapping related to datasource are deleted

	}

	@Disabled
	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testGetDataSources() {
		Page<DataSource> dataSourcePage = _dataSourceDog.getDataSourcePage(
			"(channelId eq [1])", 0, 10, null);

		Assertions.assertFalse(dataSourcePage.isEmpty());

		dataSourcePage = _dataSourceDog.getDataSourcePage(
			"(channelId eq [4])", 0, 10, null);

		Assertions.assertTrue(dataSourcePage.isEmpty());
	}

	@Disabled
	@Test
	public void testPatchDataSource() throws Exception {
		DataSource dataSource = new DataSource();

		dataSource.setId(405201047787757795L);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setName("Test Data Source");
		dataSource.setProviderType("LIFERAY");

		dataSource = _dataSourceDog.addDataSource(dataSource);

		DXPEntity dxpEntity = new DXPEntity();

		dxpEntity.setDataSourceId(dataSource.getId());
		dxpEntity.setDataSourceName("Data Source Test");
		dxpEntity.setIsNew(Boolean.TRUE);

		_dxpEntityDog.addDXPEntity(dxpEntity, DXPEntity.Type.USER);

		Individual individual = new Individual();

		individual.addBQDataSourceUser(
			new BQDataSourceUser(
				Collections.emptySet(), 405201047787757795L, null,
				Collections.singleton("123")));

		Field emailField = new Field();

		emailField.setContext("demographics");
		emailField.setDataSourceId(dataSource.getId());
		emailField.setDataSourceName("Source 1");
		emailField.setFieldType("Text");
		emailField.setName("email");
		emailField.setOwnerId(123L);
		emailField.setOwnerType("individual");
		emailField.setSourceName("emailAddress");
		emailField.setValue("test@liferay.com");

		individual.setFields(Collections.singleton(emailField));

		individual.setId("123");

		// TODO Add individual

		dataSource.setName("Edited Data Source Test");

		BoundedExecutor boundedExecutor = BoundedExecutor.newBoundedExecutor(
			10, 1);

		ReflectionTestUtils.setField(
			_dataSourceDog, "_boundedExecutor", boundedExecutor);

		dataSource = _dataSourceDog.patchDataSource(dataSource);

		boundedExecutor.awaitPendingTasks();

		Assertions.assertEquals(
			"Edited Data Source Test", dataSource.getName());

		List<Field> fields = new ArrayList<>();

		fields.forEach(
			field -> Assertions.assertEquals(
				"Edited Data Source Test", field.getDataSourceName()));

		Set<Field> customFields = individual.getCustomFields();

		customFields.forEach(
			customField -> Assertions.assertEquals(
				"Edited Data Source Test", customField.getDataSourceName()));

		Set<Field> individualFields = individual.getFields();

		individualFields.forEach(
			field -> Assertions.assertEquals(
				"Edited Data Source Test", field.getDataSourceName()));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testUpgradeFromOAuthToToken() {
		DataSource liferayDataSource = FaroInfoTestUtil.buildLiferayDataSource(
			"Token Authentication", "Liferay", "http://localhost:8080");

		liferayDataSource.setId(405201047787757796L);

		DataSource dataSource = _dataSourceDog.patchDataSource(
			liferayDataSource);

		Assertions.assertNotNull(dataSource.getPrivateKey());
		Assertions.assertNotNull(dataSource.getPublicKey());
		Assertions.assertEquals(
			"Token Authentication", dataSource.getCredentialType());
	}

	@MockBean
	private ChannelHttp _channelHttp;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

}