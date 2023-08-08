/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.RunLog;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.RunLogRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class RunLogRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		RunLog runLog = new RunLog();

		DataSource dataSource = _addDataSource();

		runLog.setDataSourceId(dataSource.getId());

		runLog.setDateLogged(new Date());
		runLog.setNaniteClassName("IndividualNanite");

		_runLog = _runLogRepository.save(runLog);
	}

	@AfterEach
	public void tearDown() {
		_runLogRepository.deleteAll();
	}

	@Test
	public void testCount() {
		Assertions.assertEquals(1, _runLogRepository.count());
	}

	@Test
	public void testDelete() {
		_runLogRepository.delete(_runLog);

		Assertions.assertEquals(0, _runLogRepository.count());
	}

	@Test
	public void testDeleteAll1() {
		_runLogRepository.deleteAll();

		Assertions.assertEquals(0, _runLogRepository.count());
	}

	@Test
	public void testDeleteAll2() {
		_runLogRepository.deleteAll(Collections.singletonList(_runLog));

		Assertions.assertEquals(0, _runLogRepository.count());
	}

	@Test
	public void testDeleteById() {
		Long id = _runLog.getId();

		Assertions.assertNotNull(id);

		_runLogRepository.deleteById(id);

		Assertions.assertEquals(0, _runLogRepository.count());
	}

	@Test
	public void testExistsById() {
		Assertions.assertTrue(_runLogRepository.existsById(_runLog.getId()));
	}

	@Test
	public void testFindAll() {
		Assertions.assertEquals(
			Collections.singletonList(_runLog), _runLogRepository.findAll());
	}

	@Test
	public void testFindAllById() {
		Assertions.assertEquals(
			Collections.singletonList(_runLog),
			_runLogRepository.findAllById(
				Collections.singletonList(_runLog.getId())));
	}

	@Test
	public void testFindById() {
		Long id = _runLog.getId();

		Assertions.assertNotNull(id);

		Optional<RunLog> modelOptional = _runLogRepository.findById(id);

		Assertions.assertTrue(modelOptional.isPresent());
	}

	@Test
	public void testFindRunLogByDataSourceIdAndNaniteClassName1() {
		Optional<RunLog> runLogOptional =
			_runLogRepository.
				findByDataSourceIdAndNaniteClassNameAndStatusOrderByDateLoggedDesc(
					_runLog.getDataSourceId(), "IndividualNanite", null);

		Assertions.assertTrue(runLogOptional.isPresent());
		Assertions.assertEquals(_runLog, runLogOptional.get());
	}

	@Test
	public void testFindRunLogByDataSourceIdAndNaniteClassName2() {
		Optional<RunLog> runLogOptional =
			_runLogRepository.
				findByDataSourceIdAndNaniteClassNameAndStatusOrderByDateLoggedDesc(
					_runLog.getDataSourceId(), "SegmentNanite", null);

		Assertions.assertFalse(runLogOptional.isPresent());
	}

	@Test
	public void testFindRunLogByNaniteClassName() {
		Optional<RunLog> runLogOptional =
			_runLogRepository.
				findByDataSourceIdAndNaniteClassNameAndStatusOrderByDateLoggedDesc(
					null, "IndividualNanite", null);

		Assertions.assertTrue(runLogOptional.isPresent());
		Assertions.assertEquals(_runLog, runLogOptional.get());
	}

	@Test
	public void testSave() {
		Assertions.assertEquals(_runLog, _runLogRepository.save(_runLog));
	}

	@Test
	public void testSaveAll() {
		Assertions.assertEquals(
			Collections.singletonList(_runLog),
			_runLogRepository.saveAll(Collections.singletonList(_runLog)));
	}

	private DataSource _addDataSource() {
		DataSource dataSource = new DataSource("Third Party");

		dataSource.setCredentialType("OAuth 2 Authentication");

		_channelRepository.save(new Channel("Channel"));

		dataSource.setProviderType("SALESFORCE");
		dataSource.setState("CREDENTIALS_INVALID");
		dataSource.setWorkspaceURL("");

		return _dataSourceRepository.save(dataSource);
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	private RunLog _runLog;

	@Autowired
	private RunLogRepository _runLogRepository;

}