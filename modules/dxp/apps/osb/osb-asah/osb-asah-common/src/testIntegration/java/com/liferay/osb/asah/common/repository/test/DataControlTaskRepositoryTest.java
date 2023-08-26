/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class DataControlTaskRepositoryTest
	extends BaseRepositoryTestCase<DataControlTask, Long> {

	@BeforeEach
	public void setUp() {
		if (_dataControlTaskRepository.count() > 1) {
			return;
		}

		_dataControlTaskRepository.deleteAll();

		Date date1 = new Date();

		DataControlTask dataControlTask1 = new DataControlTask();

		dataControlTask1.setBatchId(123456L);
		dataControlTask1.setCreateDate(date1);
		dataControlTask1.setEmailAddress("joe.bloggs@liferay.com");
		dataControlTask1.setOwnerId("1");
		dataControlTask1.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask1.setType(DataControlTask.Type.ACCESS);

		DataControlTask dataControlTask2 = new DataControlTask();

		dataControlTask2.setBatchId(123457L);
		dataControlTask2.setCompleteDate(date1);
		dataControlTask2.setCreateDate(date1);
		dataControlTask2.setEmailAddress("john.doe@liferay.com");
		dataControlTask2.setOwnerId("2");
		dataControlTask2.setStartDate(date1);
		dataControlTask2.setStatus(
			String.valueOf(DataControlTaskStatus.COMPLETED));
		dataControlTask2.setType(DataControlTask.Type.SUPPRESS);

		DataControlTask dataControlTask3 = new DataControlTask();

		dataControlTask3.setBatchId(123457L);
		dataControlTask3.setCompleteDate(date1);
		dataControlTask3.setCreateDate(date1);
		dataControlTask3.setEmailAddress("jane.doe@liferay.com");
		dataControlTask3.setOwnerId("3");
		dataControlTask3.setStartDate(date1);
		dataControlTask3.setStatus(
			String.valueOf(DataControlTaskStatus.COMPLETED));
		dataControlTask3.setType(DataControlTask.Type.SUPPRESS);

		Date date2 = DateUtil.addDays(date1, -1);

		DataControlTask dataControlTask4 = new DataControlTask();

		dataControlTask4.setBatchId(123458L);
		dataControlTask4.setCreateDate(date2);
		dataControlTask4.setEmailAddress("jack.doe@liferay.com");
		dataControlTask4.setOwnerId("7");
		dataControlTask4.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask4.setType(DataControlTask.Type.DELETE);

		DataControlTask dataControlTask5 = new DataControlTask();

		dataControlTask5.setBatchId(123458L);
		dataControlTask5.setCreateDate(date2);
		dataControlTask5.setEmailAddress("jack.doe@liferay.com");
		dataControlTask5.setOwnerId("7");
		dataControlTask5.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask5.setType(DataControlTask.Type.ACCESS);

		DataControlTask dataControlTask6 = new DataControlTask();

		dataControlTask6.setBatchId(123458L);
		dataControlTask6.setCreateDate(date2);
		dataControlTask6.setEmailAddress("jack.doe@liferay.com");
		dataControlTask6.setOwnerId("7");
		dataControlTask6.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask6.setType(DataControlTask.Type.SUPPRESS);

		Date date3 = DateUtil.addDays(date1, -2);

		DataControlTask dataControlTask7 = new DataControlTask();

		dataControlTask7.setBatchId(123459L);
		dataControlTask7.setCreateDate(date3);
		dataControlTask7.setEmailAddress("jeff.doe@liferay.com");
		dataControlTask7.setOwnerId("7");
		dataControlTask7.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask7.setType(DataControlTask.Type.DELETE);

		DataControlTask dataControlTask8 = new DataControlTask();

		dataControlTask8.setBatchId(123459L);
		dataControlTask8.setCreateDate(date3);
		dataControlTask8.setEmailAddress("jeff.doe@liferay.com");
		dataControlTask8.setOwnerId("7");
		dataControlTask8.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask8.setType(DataControlTask.Type.SUPPRESS);

		Date date4 = new Date();

		DataControlTask dataControlTask9 = new DataControlTask();

		dataControlTask9.setBatchId(123460L);
		dataControlTask9.setCreateDate(date4);
		dataControlTask9.setEmailAddress("jane.doe@liferay.com");
		dataControlTask9.setOwnerId("7");
		dataControlTask9.setStatus(
			String.valueOf(DataControlTaskStatus.COMPLETED));
		dataControlTask9.setType(DataControlTask.Type.UNSUPPRESS);

		setUpRepository(
			dataControlTask1, dataControlTask2, dataControlTask3,
			dataControlTask4, dataControlTask5, dataControlTask6,
			dataControlTask7, dataControlTask8, dataControlTask9);

		_dataControlTask = entityModels.get(0);
	}

	@AfterEach
	public void tearDown() {
		_dataControlTaskRepository.deleteAll();
	}

	@Test
	public void testCountDataControlTasks() {
		Assertions.assertEquals(
			1,
			_dataControlTaskRepository.countDataControlTasks(
				123457L, "jane.doe@liferay.com", null,
				Arrays.asList(String.valueOf(DataControlTaskStatus.COMPLETED)),
				null));
		Assertions.assertEquals(
			2,
			_dataControlTaskRepository.countDataControlTasks(
				123457L, null, null,
				Arrays.asList(String.valueOf(DataControlTaskStatus.COMPLETED)),
				null));
	}

	@Test
	public void testExistsByBatchIdAndStatusIn() {
		Assertions.assertFalse(
			_dataControlTaskRepository.existsByBatchIdAndStatusIn(
				123457L,
				Arrays.asList(String.valueOf(DataControlTaskStatus.RUNNING))));
		Assertions.assertTrue(
			_dataControlTaskRepository.existsByBatchIdAndStatusIn(
				123457L,
				Arrays.asList(
					String.valueOf(DataControlTaskStatus.COMPLETED))));
	}

	@Test
	public void testFindByIdAndStatus() {
		DataControlTask dataControlTasks =
			_dataControlTaskRepository.findByIdAndStatus(
				_dataControlTask.getId(),
				String.valueOf(DataControlTaskStatus.PENDING));

		Assertions.assertEquals(
			_dataControlTask, dataControlTasks, _dataControlTask.toString());
	}

	@SQLResource(
		resourcePath = "test_find_latest_active_suppression_data_control_task.sql"
	)
	@Test
	public void testFindLatestActiveSuppressionDataControlTask() {
		Optional<DataControlTask> dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"joan.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"jane.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"john.doe@liferay.com");

		Assertions.assertTrue(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"jack.doe@liferay.com");

		Assertions.assertTrue(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"jill.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"jeff.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"joe.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"jen.doe@liferay.com");

		Assertions.assertTrue(dataControlTaskOptional.isPresent());

		dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(
					"joel.doe@liferay.com");

		Assertions.assertFalse(dataControlTaskOptional.isPresent());
	}

	@SQLResource(resourcePath = "test_find_suppressed_email_addresses.sql")
	@Test
	public void testFindSuppressedEmailAddresses() {
		Set<String> suppressedEmailAddresses =
			_dataControlTaskRepository.findSuppressedEmailAddresses();

		Assertions.assertEquals(3, suppressedEmailAddresses.size());

		Set<String> expectedSuppressedEmailAddresses = new HashSet<String>() {
			{
				add("jack.doe@liferay.com");
				add("joe.doe@liferay.com");
				add("john.doe@liferay.com");
			}
		};

		Assertions.assertTrue(
			suppressedEmailAddresses.containsAll(
				expectedSuppressedEmailAddresses));
	}

	@Test
	public void testSearchDataControlTasks1() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				null, new Date(),
				Arrays.asList(
					String.valueOf(DataControlTaskStatus.COMPLETED.toString()),
					String.valueOf(DataControlTaskStatus.PENDING.toString())),
				Arrays.asList(DataControlTask.Type.SUPPRESS));

		Assertions.assertEquals(
			2, dataControlTasks.size(), dataControlTasks.toString());
	}

	@Test
	public void testSearchDataControlTasks2() {
		FilterHelper filterHelper = new FilterHelper("(batchId eq 123457)");

		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				filterHelper, String.valueOf(DataControlTaskStatus.COMPLETED));

		Assertions.assertEquals(
			2, dataControlTasks.size(), dataControlTasks.toString());
	}

	@Test
	public void testSearchDataControlTasks3() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				123457L, "jane.doe@liferay.com",
				DateUtil.addDays(new Date(), -1),
				Arrays.asList(
					String.valueOf(DataControlTaskStatus.COMPLETED),
					String.valueOf(DataControlTaskStatus.PENDING)),
				Arrays.asList(DataControlTask.Type.SUPPRESS),
				PageRequest.of(0, 10, Sort.desc("id")));

		Assertions.assertEquals(
			1, dataControlTasks.size(), dataControlTasks.toString());
	}

	@Test
	public void testSearchDataControlTasksByEmailAddress() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				null, "joe", null, null, null, PageRequest.of(0, 10));

		Assertions.assertEquals(
			1, dataControlTasks.size(), dataControlTasks.toString());

		DataControlTask dataControlTask = dataControlTasks.get(0);

		Assertions.assertEquals(
			"joe.bloggs@liferay.com", dataControlTask.getEmailAddress());
	}

	@Test
	public void testSearchDataControlTasksByStatuses() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				null, null, null,
				Arrays.asList(String.valueOf(DataControlTaskStatus.PENDING)),
				null, PageRequest.of(0, 10));

		Assertions.assertEquals(
			6, dataControlTasks.size(), dataControlTasks.toString());

		DataControlTask dataControlTask = dataControlTasks.get(0);

		Assertions.assertEquals(
			"joe.bloggs@liferay.com", dataControlTask.getEmailAddress());
	}

	@Override
	protected PagingAndSortingRepository<DataControlTask, Long>
		getPagingAndSortingRepository() {

		return _dataControlTaskRepository;
	}

	private DataControlTask _dataControlTask;

	@Autowired
	private DataControlTaskRepository _dataControlTaskRepository;

}