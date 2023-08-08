/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 * @author André Miranda
 */
public class DataSourceDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_info.json"
	)
	@Test
	public void testDataSourceNotFound() {
		Assertions.assertNull(_dataSourceDog.fetchDataSource(0L));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_info.json"
	)
	@Test
	public void testGetAllDataSources() {
		List<DataSource> dataSources = _dataSourceDog.getDataSources(
			null, null, null, null);

		Assertions.assertEquals(4, dataSources.size(), dataSources.toString());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_info.json"
	)
	@Test
	public void testGetChannelId() {
		Assertions.assertNotNull(
			_dataSourceDog.fetchDefaultChannelId(405057430327289648L));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_info.json"
	)
	@Test
	public void testGetDataSource() {
		DataSource dataSource = _dataSourceDog.getDataSource(200L);

		Assertions.assertNotNull(dataSource);
		Assertions.assertEquals("Liferay 1", dataSource.getName());
		Assertions.assertEquals("http://portal:8081", dataSource.getURL());
	}

	@Disabled
	@Test
	public void testGetDataSourcesJSONObjects() throws Exception {
		DataSource dataSource = new DataSource();

		dataSource.setName("Test Data Source");
		dataSource.setProviderType("LIFERAY");

		dataSource = _dataSourceDog.addDataSource(dataSource);

		// TODO Add individual related to channel, dataSource and ID

		Individual individual = new Individual();

		Map<String, JSONObject> dataSourcesJSONObjects =
			_dataSourceDog.getDataSourcesJSONObjects(
				Collections.singletonList(individual));

		JSONObject jsonObject = dataSourcesJSONObjects.get(individual.getId());

		JSONArray jsonArray = jsonObject.getJSONArray("data-sources");

		Assertions.assertEquals(
			dataSource,
			_objectMapper.convertValue(
				jsonArray.getJSONObject(0), DataSource.class));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_info.json"
	)
	@Test
	public void testGetFilteredDataSources() {
		List<DataSource> dataSources = _dataSourceDog.getDataSources(
			"Token Authentication", "LIFERAY", 1,
			Sort.by(Sort.Order.desc("modifiedDate")));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		DataSource dataSource = dataSources.get(0);

		Assertions.assertEquals(400, dataSource.getId());
		Assertions.assertEquals("Liferay 3", dataSource.getName());
		Assertions.assertEquals("http://portal:8083", dataSource.getURL());
	}

	@Test
	public void testPatchDataSource() {
		DataSource dataSource = new DataSource();

		dataSource.setName("Test Data Source");
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("CREDENTIALS_VALID");
		dataSource.setStatus("ACTIVE");

		dataSource = _dataSourceRepository.save(dataSource);

		dataSource.setName("Test Data Source (1)");

		dataSource = _dataSourceDog.patchDataSource(dataSource);

		Assertions.assertEquals("Test Data Source (1)", dataSource.getName());
		Assertions.assertEquals("CREDENTIALS_VALID", dataSource.getState());
		Assertions.assertEquals("ACTIVE", dataSource.getStatus());

		dataSource.setState("DISCONNECTED");
		dataSource.setStatus("INACTIVE");

		dataSource = _dataSourceRepository.save(dataSource);

		dataSource.setName("Test Data Source (2)");

		dataSource = _dataSourceDog.patchDataSource(dataSource);

		Assertions.assertEquals("Test Data Source (2)", dataSource.getName());
		Assertions.assertEquals("DISCONNECTED", dataSource.getState());
		Assertions.assertEquals("INACTIVE", dataSource.getStatus());
	}

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private ObjectMapper _objectMapper;

}