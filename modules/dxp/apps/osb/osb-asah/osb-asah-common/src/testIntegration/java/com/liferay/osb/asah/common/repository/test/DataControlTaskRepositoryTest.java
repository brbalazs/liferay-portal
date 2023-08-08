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
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
		_dataControlTaskRepository.deleteAll();

		DataControlTask dataControlTask1 = new DataControlTask();

		dataControlTask1.setBatchId(123456L);
		dataControlTask1.setCreateDate(new Date());
		dataControlTask1.setEmailAddress("joe.bloggs@liferay.com");
		dataControlTask1.setOwnerId("1");
		dataControlTask1.setStatus(
			String.valueOf(DataControlTaskStatus.PENDING));
		dataControlTask1.setType(DataControlTask.Type.ACCESS);

		DataControlTask dataControlTask2 = new DataControlTask();

		dataControlTask2.setBatchId(123457L);
		dataControlTask2.setCompleteDate(new Date());
		dataControlTask2.setCreateDate(new Date());
		dataControlTask2.setEmailAddress("john.doe@liferay.com");
		dataControlTask2.setOwnerId("2");
		dataControlTask2.setStartDate(new Date());
		dataControlTask2.setStatus(
			String.valueOf(DataControlTaskStatus.COMPLETED));
		dataControlTask2.setType(DataControlTask.Type.SUPPRESS);

		DataControlTask dataControlTask3 = new DataControlTask();

		dataControlTask3.setBatchId(123457L);
		dataControlTask3.setCompleteDate(new Date());
		dataControlTask3.setCreateDate(new Date());
		dataControlTask3.setEmailAddress("jane.doe@liferay.com");
		dataControlTask3.setOwnerId("3");
		dataControlTask3.setStartDate(new Date());
		dataControlTask3.setStatus(
			String.valueOf(DataControlTaskStatus.COMPLETED));
		dataControlTask3.setType(DataControlTask.Type.SUPPRESS);

		setUpRepository(dataControlTask1, dataControlTask2, dataControlTask3);

		_dataControlTask = entityModels.get(0);
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

	@Test
	public void testSearchDataControlTasks1() {
		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				new Date(),
				Arrays.asList(
					String.valueOf(DataControlTaskStatus.COMPLETED),
					String.valueOf(DataControlTaskStatus.PENDING)),
				Arrays.asList(String.valueOf(DataControlTask.Type.SUPPRESS)));

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
				Arrays.asList(String.valueOf(DataControlTask.Type.SUPPRESS)),
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
			1, dataControlTasks.size(), dataControlTasks.toString());

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