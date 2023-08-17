/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.entity.BQIndividual;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.AuditEventRepository;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQExpandoValueRepository;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.BQUserRepository;
import com.liferay.osb.asah.common.repository.DXPEntityRepository;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
		_dataControlTaskRepository.deleteAll();
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
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				null, null, null);

		Assertions.assertEquals(6, dataControlTasks.size());

		Assertions.assertEquals(
			1,
			_dataControlTaskRepository.countDataControlTasks(
				null, "test1@liferay.com", null, null, null));

		Assertions.assertEquals(
			1,
			_dataControlTaskRepository.countDataControlTasks(
				null, "test2@liferay.com", null, null, null));

		File file = path.toFile();

		if (!file.delete()) {
			_log.error("Unable to delete file " + file.getAbsolutePath());
		}
	}

	@BQSQLResource(resourcePath = "test_add_data_control_task_unsuppress.sql")
	@Test
	public void testAddDataControlTasksUnsuppress() {
		_dataControlTaskDog.addDataControlTasks(
			Collections.singletonList("test@liferay.com"), null, null,
			Collections.singletonList(
				DataControlTask.Type.UNSUPPRESS.toString()),
			"12345", "Test Test");

		Optional<Suppression> suppressionOptional =
			_suppressionRepository.findByEmailAddress("test@liferay.com");

		Suppression suppression = suppressionOptional.orElse(null);

		Assertions.assertNotNull(suppression);
		Assertions.assertTrue(suppression.getHidden());
	}

	@BQSQLResource(resourcePath = "test_data_control_task_delete_bq.sql")
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@SQLResource(resourcePath = "test_data_control_task_delete.sql")
	@Test
	public void testDeleteData() {
		Optional<DataControlTask> dataControlTaskOptional =
			_dataControlTaskRepository.findById(2222L);

		Assertions.assertTrue(dataControlTaskOptional.isPresent());

		_dataControlTaskDog.run(dataControlTaskOptional.get());

		Assertions.assertEquals(0, _bqExpandoValueRepository.count());
		Assertions.assertEquals(
			0,
			_bqIndividualRepository.countBQIndividuals(
				null, "email eq 'test1@liferay.com'", null, null, null));
		Assertions.assertEquals(1, _bqUserRepository.count());
		Assertions.assertNull(_bqIdentityRepository.getBQIndividualId("1"));
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
	public void testGetDataControlTaskResultBagSort() {
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "john.doe@liferay.com", "test@liferay.com",
				"jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.asc("batchId"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "jane.doe@liferay.com",
				"john.doe@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("batchId"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "john.doe@liferay.com", "test@liferay.com",
				"jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.asc("createDate"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"jane.doe@liferay.com", "test@liferay.com",
				"john.doe@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("createDate"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"jane.doe@liferay.com", "john.doe@liferay.com",
				"test@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.asc("emailAddress"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "test@liferay.com", "john.doe@liferay.com",
				"jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("emailAddress"), null,
				null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "john.doe@liferay.com", "test@liferay.com",
				"jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.asc("status"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"jane.doe@liferay.com", "test@liferay.com",
				"john.doe@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("status"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "john.doe@liferay.com",
				"jane.doe@liferay.com", "test@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.asc("type"), null, null));
		_checkResults(
			4,
			Arrays.asList(
				"test@liferay.com", "test@liferay.com", "john.doe@liferay.com",
				"jane.doe@liferay.com"),
			_dataControlTaskDog.getDataControlTaskPage(
				null, null, null, 0, 10, Sort.desc("type"), null, null));
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

	@SQLResource(resourcePath = "test_get_prioritized_data_control_tasks.sql")
	@Test
	public void testGetPrioritizedDataControlTasks1() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				DateUtil.newDate(),
				Arrays.asList(DataControlTaskStatus.PENDING.toString()), null);

		Assertions.assertEquals(6, dataControlTasks.size());

		Assertions.assertEquals(
			Arrays.asList(
				Pair.of(123459L, DataControlTask.Type.SUPPRESS),
				Pair.of(123459L, DataControlTask.Type.DELETE),
				Pair.of(123458L, DataControlTask.Type.ACCESS),
				Pair.of(123458L, DataControlTask.Type.SUPPRESS),
				Pair.of(123458L, DataControlTask.Type.DELETE),
				Pair.of(123456L, DataControlTask.Type.ACCESS)),
			ListUtil.map(
				dataControlTasks,
				dataControlTask -> Pair.of(
					dataControlTask.getBatchId(), dataControlTask.getType())));
	}

	@SQLResource(resourcePath = "test_data_control_task_dog_test.sql")
	@Test
	public void testGetPrioritizedDataControlTasks2() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(DateUtil.PATTERN_SHORT);

		Assertions.assertEquals(
			Arrays.asList(
				33333333L, 44444444L, 55555555L, 66666666L, 77777777L,
				88888888L),
			ListUtil.map(
				_dataControlTaskDog.getPrioritizedDataControlTasks(
					null, dateFormat.parse("2023-08-02"), null, null,
					dateFormat.parse("2023-08-09")),
				DataControlTask::getId));

		Assertions.assertEquals(
			Arrays.asList(55555555L, 66666666L),
			ListUtil.map(
				_dataControlTaskDog.getPrioritizedDataControlTasks(
					null, dateFormat.parse("2023-08-02"), null,
					DataControlTaskStatus.PENDING.toString(),
					dateFormat.parse("2023-08-09")),
				DataControlTask::getId));
	}

	@SQLResource(
		resourcePath = "test_get_prioritized_pending_data_control_tasks_1.sql"
	)
	@Test
	public void testGetPrioritizedPendingDataControlTasks1() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedPendingDataControlTasks();

		Assertions.assertEquals(5, dataControlTasks.size());

		Stream<DataControlTask> stream = dataControlTasks.stream();

		List<Long> dataControlTaskIds = stream.map(
			DataControlTask::getId
		).collect(
			Collectors.toList()
		);

		Assertions.assertTrue(
			dataControlTaskIds.containsAll(
				Arrays.asList(1111L, 2222L, 3333L, 4444L, 5555L)));
	}

	@SQLResource(
		resourcePath = "test_get_prioritized_pending_data_control_tasks_2.sql"
	)
	@Test
	public void testGetPrioritizedPendingDataControlTasks2() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedPendingDataControlTasks();

		Assertions.assertEquals(1, dataControlTasks.size());

		DataControlTask dataControlTask = dataControlTasks.get(0);

		Assertions.assertEquals(4444L, dataControlTask.getId());
		Assertions.assertEquals(
			DataControlTaskStatus.PENDING.toString(),
			dataControlTask.getStatus());
		Assertions.assertEquals(
			DataControlTask.Type.DELETE, dataControlTask.getType());
	}

	@SQLResource(
		resourcePath = "test_get_prioritized_pending_data_control_tasks_3.sql"
	)
	@Test
	public void testGetPrioritizedPendingDataControlTasks3() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedPendingDataControlTasks();

		Assertions.assertEquals(2, dataControlTasks.size());

		Stream<DataControlTask> stream = dataControlTasks.stream();

		List<Long> dataControlTaskIds = stream.map(
			DataControlTask::getId
		).collect(
			Collectors.toList()
		);

		Assertions.assertTrue(
			dataControlTaskIds.containsAll(Arrays.asList(3333L, 4444L)));
	}

	@SQLResource(
		resourcePath = "test_get_prioritized_pending_data_control_tasks_4.sql"
	)
	@Test
	public void testGetPrioritizedPendingDataControlTasks4() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedPendingDataControlTasks();

		Assertions.assertEquals(2, dataControlTasks.size());

		Stream<DataControlTask> stream = dataControlTasks.stream();

		List<Long> dataControlTaskIds = stream.map(
			DataControlTask::getId
		).collect(
			Collectors.toList()
		);

		Assertions.assertTrue(
			dataControlTaskIds.containsAll(Arrays.asList(1111L, 6666L)));
	}

	@Test
	public void testGetSuppressedEmailAddresses() {
		_dataControlTaskDog.addDataControlTasks(
			Arrays.asList(
				"test1@liferay.com", "test1@liferay.com", "test2@liferay.com"),
			null, "1000",
			Collections.singletonList(DataControlTask.Type.SUPPRESS.toString()),
			"12345", "test@liferay.com");

		List<DataControlTask> dataControlTasks =
			_dataControlTaskDog.getPrioritizedDataControlTasks(
				null, null, Arrays.asList(DataControlTask.Type.SUPPRESS));

		for (DataControlTask dataControlTask : dataControlTasks) {
			dataControlTask.setStatus(
				DataControlTaskStatus.COMPLETED.toString());

			_dataControlTaskDog.updateDataControlTask(dataControlTask);
		}

		Assertions.assertEquals(
			SetUtil.of("test1@liferay.com", "test2@liferay.com"),
			_dataControlTaskDog.getSuppressedEmailAddresses());
	}

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testIsSuppressedEmailAddress() {
		Assertions.assertFalse(
			_dataControlTaskDog.isSuppressedEmailAddress(
				DigestUtils.sha256Hex("test@liferay.com")));

		Assertions.assertTrue(
			_dataControlTaskDog.isSuppressedEmailAddress(
				DigestUtils.sha256Hex("john.doe@liferay.com")));
	}

	@BQSQLResource(resourcePath = "test_data_control_task_unsuppress_bq.sql")
	@Disabled
	@SQLResource(resourcePath = "test_data_control_task_unsuppress.sql")
	@Test
	public void testUnsuppress() {
		Optional<DataControlTask> dataControlTaskOptional =
			_dataControlTaskRepository.findById(12345L);

		Assertions.assertTrue(dataControlTaskOptional.isPresent());

		_dataControlTaskDog.run(dataControlTaskOptional.get());

		Optional<BQIndividual> bqIndividualOptional =
			_bqIndividualRepository.findByEmailAddress("test1@liferay.com");

		Assertions.assertTrue(bqIndividualOptional.isPresent());

		BQIndividual bqIndividual = bqIndividualOptional.get();

		Assertions.assertFalse(bqIndividual.getSuppressed());

		Assertions.assertEquals(
			Arrays.asList(
				"55f4730b-e774-487f-b186-e52fa81990d3",
				"72a22dce-b12b-4a82-9b3c-1bedb90baebf"),
			_bqIdentityRepository.getBQIdentityIds(
				bqIndividual.getId(), false));

		Assertions.assertEquals(
			10,
			_bqEventRepository.countBQEvents(
				1L, bqIndividual.getId(), null,
				LocalDateTime.parse("2023-08-10T00:00:00"),
				LocalDateTime.parse("2023-07-15T00:00:00"), "UTC"));

		Optional<Suppression> suppressionOptional =
			_suppressionRepository.findByEmailAddress("test1@liferay.com");

		Assertions.assertFalse(suppressionOptional.isPresent());
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
	private BQEventRepository _bqEventRepository;

	@Autowired
	private BQExpandoValueRepository _bqExpandoValueRepository;

	@Autowired
	private BQIdentityRepository _bqIdentityRepository;

	@Autowired
	private BQIndividualRepository _bqIndividualRepository;

	@Autowired
	private BQUserRepository _bqUserRepository;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private DataControlTaskRepository _dataControlTaskRepository;

	@Autowired
	private SuppressionRepository _suppressionRepository;

	private Path _tempPath;

}