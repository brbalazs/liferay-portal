/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.converter.helper.DefaultFilterStringConverterHelper;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.DataSourceOrganization;
import com.liferay.osb.asah.common.entity.DataSourceSite;
import com.liferay.osb.asah.common.postgresql.converter.helper.DataSourceFilterStringConverterHelper;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
@Import(JDBCTestConfiguration.class)
public class DataSourceRepositoryTest
	extends BaseRepositoryTestCase<DataSource, Long> {

	@BeforeEach
	public void setUp() {
		DataSource dataSource1 = new DataSource("Liferay Brazil");

		dataSource1.setCredentialType("Token Authentication");

		dataSource1.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");

		DataSourceOrganization dataSourceOrganization1 =
			new DataSourceOrganization();

		dataSourceOrganization1.setEnableAllChildren(true);
		dataSourceOrganization1.setOrganizationId(1L);
		dataSourceOrganization1.setOrganizationIds(SetUtil.of(3L, 4L));

		DataSourceOrganization dataSourceOrganization2 =
			new DataSourceOrganization();

		dataSourceOrganization2.setEnableAllChildren(false);
		dataSourceOrganization2.setOrganizationId(5L);
		dataSourceOrganization2.setOrganizationIds(SetUtil.of(6L, 7L));

		dataSource1.setDataSourceOrganizations(
			SetUtil.of(dataSourceOrganization1, dataSourceOrganization2));

		dataSource1.setProviderType("LIFERAY");
		dataSource1.setState("READY");
		dataSource1.setStatus("STARTED");
		dataSource1.setURL("");

		DataSource dataSource2 = new DataSource("Liferay USA");

		dataSource2.setCredentialType("Basic Authentication");

		dataSource2.setProviderType("CSV");
		dataSource2.setState("CREDENTIALS_VALID");
		dataSource2.setURL("http://portal:8080");
		dataSource2.setWorkspaceURL("http://localhost:8080/workspace/10000");

		DataSource dataSource3 = new DataSource("Third Party");

		dataSource3.setCredentialType("OAuth 2 Authentication");

		dataSource3.setProviderType("SALESFORCE");
		dataSource3.setState("CREDENTIALS_INVALID");
		dataSource3.setWorkspaceURL("");

		setUpRepository(dataSource1, dataSource2, dataSource3);

		Channel channel = new Channel("Channel");

		entityModels.forEach(
			dataSource -> channel.addChannelDataSource(
				new ChannelDataSource(null, dataSource.getId(), null)));

		_channelRepository.save(channel);
	}

	@Test
	public void testCountDataSources() {
		Assertions.assertEquals(
			3, _dataSourceRepository.countDataSources(FilterHelper.EMPTY));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper,
					"credentials/type eq 'Token Authentication'",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper,
					"name eq 'Liferay Brazil'",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper,
					"provider/type eq 'LIFERAY'",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper,
					"contains(name, 'Liferay') and contains(name, 'Brazil')",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "state eq 'READY'",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "url eq null",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			1,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "workspaceURL eq null",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			0,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "name eq 'Liferay'",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			2,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper,
					"contains(name, 'Liferay')",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			2,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "url ne null",
					_dataSourceFilterStringConverterHelper)));

		Assertions.assertEquals(
			2,
			_dataSourceRepository.countDataSources(
				new FilterHelper(
					_defaultFilterStringConverterHelper, "workspaceURL ne null",
					_dataSourceFilterStringConverterHelper)));
	}

	@Test
	public void testExistsByFaroBackendSecuritySignature() {
		Assertions.assertTrue(
			_dataSourceRepository.existsByFaroBackendSecuritySignature(
				"faroBackendSecuritySignature"));
	}

	@Test
	public void testExistsByIdNotAndName() {
		DataSource dataSource1 = entityModels.get(0);

		Assertions.assertFalse(
			_dataSourceRepository.existsByIdNotAndName(
				dataSource1.getId(), "Liferay Brazil"));

		DataSource dataSource2 = entityModels.get(1);

		Assertions.assertTrue(
			_dataSourceRepository.existsByIdNotAndName(
				dataSource2.getId(), "Liferay Brazil"));
	}

	@Test
	public void testExistsByName() {
		Assertions.assertTrue(
			_dataSourceRepository.existsByName("Liferay Brazil"));
	}

	@Test
	public void testExistsByProviderType() {
		Assertions.assertFalse(
			_dataSourceRepository.existsByProviderType("LIFERAY"));

		DataSource dataSource1 = entityModels.get(0);

		dataSource1.setEnableAllSites(true);

		_dataSourceRepository.save(dataSource1);

		Assertions.assertTrue(
			_dataSourceRepository.existsByProviderType("LIFERAY"));

		dataSource1.setEnableAllSites(false);
		dataSource1.setSitesSelected(true);

		_dataSourceRepository.save(dataSource1);

		Assertions.assertTrue(
			_dataSourceRepository.existsByProviderType("LIFERAY"));

		DataSourceSite dataSourceSite = new DataSourceSite();

		dataSourceSite.setSiteId(1L);

		dataSource1.setDataSourceSites(Collections.singleton(dataSourceSite));

		dataSource1.setSitesSelected(false);

		_dataSourceRepository.save(dataSource1);

		Assertions.assertTrue(
			_dataSourceRepository.existsByProviderType("LIFERAY"));
	}

	@Test
	public void testFindByCredentialType() {
		List<DataSource> dataSources =
			_dataSourceRepository.findByCredentialType(
				"Token Authentication",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));
	}

	@Test
	public void testFindByCredentialTypeAndProviderType() {
		List<DataSource> dataSources =
			_dataSourceRepository.findByCredentialTypeAndProviderType(
				"Token Authentication", "LIFERAY",
				PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));
	}

	@Test
	public void testFindByProviderType1() {
		List<DataSource> dataSources = _dataSourceRepository.findByProviderType(
			"LIFERAY");

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));
	}

	@Test
	public void testFindByProviderType2() {
		List<DataSource> dataSources = _dataSourceRepository.findByProviderType(
			"LIFERAY", PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));
	}

	@Test
	public void testFindByProviderTypeAndStatus() {
		List<DataSource> dataSources =
			_dataSourceRepository.findByProviderTypeAndStatus(
				"LIFERAY", "STARTED");

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));
	}

	@Disabled
	@Test
	public void testSearchDataSources() {
		List<DataSource> dataSources = _dataSourceRepository.searchDataSources(
			FilterHelper.EMPTY,
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(3, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"credentials/type eq 'Token Authentication'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "name eq 'Liferay Brazil'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"provider/type eq 'LIFERAY'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"contains(name, 'Liferay') and contains(name, 'Brazil')",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "state eq 'READY'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "url eq ''",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "workspaceURL eq null",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(1, dataSources.size(), dataSources.toString());

		_assertDataSourceEquals(entityModels.get(0), dataSources.get(0));

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "name eq 'Liferay'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(0, dataSources.size(), dataSources.toString());

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"contains(name, 'Liferay')",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(2, dataSources.size(), dataSources.toString());

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"state eq ['CREDENTIALS_INVALID','CREDENTIALS_VALID']",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(2, dataSources.size(), dataSources.toString());

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper,
				"state eq 'IN_PROGRESS_DELETING'",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(0, dataSources.size(), dataSources.toString());

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "url ne null",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(2, dataSources.size(), dataSources.toString());

		dataSources = _dataSourceRepository.searchDataSources(
			new FilterHelper(
				_defaultFilterStringConverterHelper, "workspaceURL ne null",
				_dataSourceFilterStringConverterHelper),
			PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))));

		Assertions.assertEquals(2, dataSources.size(), dataSources.toString());
	}

	@Override
	protected PagingAndSortingRepository<DataSource, Long>
		getPagingAndSortingRepository() {

		return _dataSourceRepository;
	}

	private void _assertDataSourceEquals(
		DataSource expectedDataSource, DataSource actualDataSource) {

		Assertions.assertEquals(
			expectedDataSource.getCredentialType(),
			actualDataSource.getCredentialType());
		Assertions.assertEquals(
			expectedDataSource.getDataSourceOrganizations(),
			actualDataSource.getDataSourceOrganizations());
		Assertions.assertEquals(
			expectedDataSource.getFaroBackendSecuritySignature(),
			actualDataSource.getFaroBackendSecuritySignature());
		Assertions.assertEquals(
			expectedDataSource.getName(), actualDataSource.getName());
		Assertions.assertEquals(
			expectedDataSource.getProviderType(),
			actualDataSource.getProviderType());
		Assertions.assertEquals(
			expectedDataSource.getState(), actualDataSource.getState());
		Assertions.assertEquals(
			expectedDataSource.getStatus(), actualDataSource.getStatus());
		Assertions.assertEquals(
			expectedDataSource.getURL(), actualDataSource.getURL());
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceFilterStringConverterHelper
		_dataSourceFilterStringConverterHelper;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	private final DefaultFilterStringConverterHelper
		_defaultFilterStringConverterHelper =
			new DefaultFilterStringConverterHelper();

}