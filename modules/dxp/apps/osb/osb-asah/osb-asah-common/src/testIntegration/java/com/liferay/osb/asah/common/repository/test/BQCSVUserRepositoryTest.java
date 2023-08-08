/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.repository.BQCSVUserRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Marcellus Tavares
 */
@Disabled
@Import(JDBCTestConfiguration.class)
public class BQCSVUserRepositoryTest {

	@BeforeEach
	public void setUp() {
		DataSource dataSource = new DataSource("Liferay Brazil");

		dataSource.setCredentialType("Token Authentication");

		Channel channel1 = new Channel("channel1");

		channel1.setId(11L);
		channel1.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel1);

		dataSource.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");
		dataSource.setId(1L);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("READY");
		dataSource.setStatus("STARTED");
		dataSource.setURL("");

		_dataSourceRepository.save(dataSource);

		BQCSVUser bqCSVUser1 = new BQCSVUser(
			1L,
			Arrays.asList(
				new BQCSVUser.Field("givenName", "Bennie"),
				new BQCSVUser.Field("age", "20")));

		bqCSVUser1.setDataSourceUserPK("1");

		BQCSVUser bqCSVUser2 = new BQCSVUser(
			1L, Arrays.asList(new BQCSVUser.Field("givenName", "Ellie")));

		bqCSVUser2.setDataSourceUserPK("2");

		_bqCSVUserRepository.insertAll(Arrays.asList(bqCSVUser1, bqCSVUser2));
	}

	@AfterEach
	public void tearDown() {
		_channelRepository.deleteAll();
		_dataSourceRepository.deleteAll();
	}

	@Test
	public void testCountByDataSourceId() {
		Assertions.assertEquals(
			2L, _bqCSVUserRepository.countByDataSourceId(1L));
	}

	@Test
	public void testDeleteByDataSourceId() {
		_bqCSVUserRepository.deleteByDataSourceId(1L);

		Assertions.assertEquals(
			0L, _bqCSVUserRepository.countByDataSourceId(1L));
	}

	@Test
	public void testDeleteByDataSourceIdAndDataSourceUserPKIn() {
		_bqCSVUserRepository.deleteByDataSourceIdAndDataSourceUserPKIn(
			1L, Arrays.asList("1", "2"));

		Assertions.assertEquals(
			0L, _bqCSVUserRepository.countByDataSourceId(1L));
	}

	@Test
	public void testFindByDataSourceId() {
		List<BQCSVUser> bqCSVIdentitiesById =
			_bqCSVUserRepository.findByDataSourceId(1L, PageRequest.of(0, 10));

		Assertions.assertEquals(2L, bqCSVIdentitiesById.size());
	}

	private BQCSVUser _bqCSVUser1;
	private BQCSVUser _bqCSVUser2;

	@Autowired
	private BQCSVUserRepository _bqCSVUserRepository;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}