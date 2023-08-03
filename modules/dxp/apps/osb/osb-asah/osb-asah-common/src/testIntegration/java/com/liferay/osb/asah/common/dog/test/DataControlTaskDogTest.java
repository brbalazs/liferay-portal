/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.AuditEventRepository;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Matthew Kong
 */
public class DataControlTaskDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() throws Exception {
		_tempPath = Files.createTempDirectory("temp");
	}

	@AfterEach
	public void tearDown() {
		File folder = _tempPath.toFile();

		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (!file.delete()) {
					_log.error(
						"Unable to delete file " + file.getAbsolutePath());
				}
			}
		}

		if (!folder.delete()) {
			_log.error("Unable to delete folder " + folder.getAbsolutePath());
		}

		_auditEventRepository.deleteAll();
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testAddDataControlTasksFile() throws Exception {
		String content = "test1@liferay.com\ntest2@liferay.com";

		Path path = Files.write(
			Paths.get(_tempPath + "/test.csv"),
			content.getBytes(StandardCharsets.UTF_8));

		_dataControlTaskDog.addDataControlTasks(
			null, Paths.get(_tempPath.toString(), "test.csv"), "1000",
			Collections.singletonList(DataControlTask.Type.SUPPRESS.toString()),
			"12345", "test@liferay.com");

		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getDataControlTasks(null, null, null);

		Assertions.assertEquals(6, dataControlTasks.size());

		DataControlTask dataControlTask = dataControlTasks.get(4);

		Assertions.assertEquals(
			"test1@liferay.com", dataControlTask.getEmailAddress());

		dataControlTask = dataControlTasks.get(5);

		Assertions.assertEquals(
			"test2@liferay.com", dataControlTask.getEmailAddress());

		File file = path.toFile();

		if (!file.delete()) {
			_log.error("Unable to delete file " + file.getAbsolutePath());
		}
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagBatch() {
		_checkResults(
			2, Arrays.asList("jane.doe@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				102L, null, null, 0, 10, Sort.desc("createDate"), null, null));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagCombination() {
		_checkResults(
			1, Collections.singletonList("john.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				101L, "liferay", 30, 0, 10, Sort.desc("createDate"),
				Collections.singletonList(
					DataControlTaskStatus.COMPLETED.toString()),
				Collections.singletonList(
					DataControlTask.Type.SUPPRESS.toString())));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagPagination() {
		_checkResults(
			4, Collections.singletonList("test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 1, 1, Sort.desc("createDate"), null, null));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagRange() {
		_checkResults(
			3,
			Arrays.asList(
				"jane.doe@liferay.com", "test@liferay.com",
				"john.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, 30, 0, 10, Sort.desc("createDate"), null, null));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagSearch() {
		_checkResults(
			2, Arrays.asList("jane.doe@liferay.com", "john.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, "doe", null, 0, 10, Sort.desc("createDate"), null, null));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagStatus() {
		_checkResults(
			1, Collections.singletonList("jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("createDate"),
				Collections.singletonList(
					DataControlTaskStatus.PENDING.toString()),
				null));
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testGetDataControlTaskResultBagTypes() {
		_checkResults(
			1, Collections.singletonList("test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("createDate"), null,
				Collections.singletonList(
					DataControlTask.Type.UNSUPPRESS.toString())));
	}

	private void _checkResults(
		long expectedTotal, List<String> expectedResults,
		Page<DataControlTask> dataControlTaskPage) {

		Assertions.assertEquals(
			expectedTotal, dataControlTaskPage.getTotalElements());

		Assertions.assertEquals(
			expectedResults,
			ListUtil.map(
				dataControlTaskPage.getContent(),
				DataControlTask::getEmailAddress));
	}

	private static final Log _log = LogFactory.getLog(
		DataControlTaskDogTest.class);

	@Autowired
	private AuditEventRepository _auditEventRepository;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	private Path _tempPath;

}