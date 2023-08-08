/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.scheduling.test;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskManager;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskRunnable;
import com.liferay.osb.asah.batch.curator.bot.scheduling.AsahTaskScheduler;
import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.repository.AsahTaskRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author André Miranda
 */
public class AsahTaskManagerTest
	implements OSBAsahBatchCuratorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() throws Exception {
		ReflectionTestUtils.setField(
			_asahTaskManager, "_boundedExecutor", _boundedExecutor);
		ReflectionTestUtils.setField(
			_asahTaskManager, "_updateMembershipsNaniteBoundedExecutor",
			_updateMembershipsNaniteBoundedExecutor);
	}

	@RepositoryResource(
		repositoryClass = AsahTaskRepository.class,
		resourcePath = "osbasahfaroinfo/osbasahtasks.json"
	)
	@Test
	public void testDeleteAsahTask() {
		_asahTaskManager.deleteAsahTask(450553576847486527L);

		List<AsahTask> asahTasks = _asahTaskDog.getAsahTasks();

		Assertions.assertEquals(3, asahTasks.size(), asahTasks.toString());
	}

	@Test
	public void testExecuteAsahTask1() {
		AsahTask asahTask = new AsahTask("DataControlNanite", null, "test");

		asahTask.setId(450553576847486527L);

		_asahTaskManager.executeAsahTask(asahTask, false);

		ArgumentCaptor<AsahTaskRunnable> argumentCaptor =
			ArgumentCaptor.forClass(AsahTaskRunnable.class);

		Mockito.verify(
			_boundedExecutor, Mockito.times(1)
		).runAsync(
			argumentCaptor.capture()
		);

		AsahTaskRunnable asahTaskRunnable = argumentCaptor.getValue();

		Assertions.assertArrayEquals(
			new String[] {"DataControlNanite"},
			asahTaskRunnable.getNaniteClassNames());
		Assertions.assertEquals(
			Long.valueOf("450553576847486527"),
			asahTaskRunnable.getAsahTaskId());
		Assertions.assertEquals("test", asahTaskRunnable.getProjectId());
		Assertions.assertFalse(asahTaskRunnable.isForce());
	}

	@RepositoryResource(
		repositoryClass = AsahTaskRepository.class,
		resourcePath = "osbasahfaroinfo/osbasahtasks.json"
	)
	@Test
	public void testExecuteAsahTask2() {
		_asahTaskManager.executeAsahTask(450553576847486529L, false);

		ArgumentCaptor<AsahTaskRunnable> asahTaskRunnableArgumentCaptor =
			ArgumentCaptor.forClass(AsahTaskRunnable.class);

		ArgumentCaptor<ReentrantLock> reentrantLockArgumentCaptor =
			ArgumentCaptor.forClass(ReentrantLock.class);

		Mockito.verify(
			_updateMembershipsNaniteBoundedExecutor, Mockito.times(1)
		).runAsync(
			asahTaskRunnableArgumentCaptor.capture(),
			reentrantLockArgumentCaptor.capture()
		);

		AsahTaskRunnable asahTaskRunnable =
			asahTaskRunnableArgumentCaptor.getValue();

		Assertions.assertArrayEquals(
			new String[] {"UpdateMembershipsNanite"},
			asahTaskRunnable.getNaniteClassNames());
		Assertions.assertEquals(
			Long.valueOf("450553576847486529"),
			asahTaskRunnable.getAsahTaskId());
		Assertions.assertEquals("test", asahTaskRunnable.getProjectId());
		Assertions.assertFalse(asahTaskRunnable.isForce());
	}

	@RepositoryResource(
		repositoryClass = AsahTaskRepository.class,
		resourcePath = "osbasahfaroinfo/osbasahtasks.json"
	)
	@Test
	public void testExecuteAsahTasks1() {
		_asahTaskManager.executeAsahTasks();

		Mockito.verify(
			_boundedExecutor, Mockito.times(2)
		).runAsync(
			ArgumentMatchers.any(AsahTaskRunnable.class)
		);
	}

	@RepositoryResource(
		repositoryClass = AsahTaskRepository.class,
		resourcePath = "osbasahfaroinfo/osbasahtasks.json"
	)
	@Test
	public void testExecuteAsahTasks2() {
		_asahTaskManager.executeAsahTasks(
			Arrays.asList(450553576847486527L, 450553576847486529L), false);

		ArgumentCaptor<AsahTaskRunnable> asahTaskRunnableArgumentCaptor =
			ArgumentCaptor.forClass(AsahTaskRunnable.class);

		ArgumentCaptor<ReentrantLock> reentrantLockArgumentCaptor =
			ArgumentCaptor.forClass(ReentrantLock.class);

		Mockito.verify(
			_updateMembershipsNaniteBoundedExecutor, Mockito.times(1)
		).runAsync(
			asahTaskRunnableArgumentCaptor.capture(),
			reentrantLockArgumentCaptor.capture()
		);

		AsahTaskRunnable asahTaskRunnable =
			asahTaskRunnableArgumentCaptor.getValue();

		Assertions.assertArrayEquals(
			new String[] {"UpdateMembershipsNanite"},
			asahTaskRunnable.getNaniteClassNames());
		Assertions.assertEquals(
			Long.valueOf("450553576847486529"),
			asahTaskRunnable.getAsahTaskId());
		Assertions.assertFalse(asahTaskRunnable.isForce());

		Mockito.verify(
			_boundedExecutor, Mockito.times(1)
		).runAsync(
			asahTaskRunnableArgumentCaptor.capture()
		);

		asahTaskRunnable = asahTaskRunnableArgumentCaptor.getValue();

		Assertions.assertArrayEquals(
			new String[] {"DataControlNanite"},
			asahTaskRunnable.getNaniteClassNames());
		Assertions.assertEquals(
			Long.valueOf("450553576847486527"),
			asahTaskRunnable.getAsahTaskId());
		Assertions.assertFalse(asahTaskRunnable.isForce());
	}

	@Test
	public void testScheduleAsahTaskFail() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _asahTaskManager.scheduleAsahTask(
				new AsahTask("Foo", null, null)));
	}

	@RepositoryResource(
		repositoryClass = AsahTaskRepository.class,
		resourcePath = "osbasahfaroinfo/osbasahtasks.json"
	)
	@Test
	public void testScheduleAsahTasks() {
		_asahTaskManager.scheduleAsahTasks();

		ArgumentCaptor<AsahTaskRunnable> argumentCaptor =
			ArgumentCaptor.forClass(AsahTaskRunnable.class);

		Mockito.verify(
			_asahTaskScheduler, Mockito.times(1)
		).schedule(
			ArgumentMatchers.eq("0 0 0 * * ?"), argumentCaptor.capture(),
			ArgumentMatchers.eq("450553576847486530")
		);

		AsahTaskRunnable asahTaskRunnable = argumentCaptor.getValue();

		Assertions.assertArrayEquals(
			new String[] {"DataControlNanite"},
			asahTaskRunnable.getNaniteClassNames());
		Assertions.assertEquals(
			450553576847486530L, asahTaskRunnable.getAsahTaskId());
		Assertions.assertEquals("test", asahTaskRunnable.getProjectId());
		Assertions.assertFalse(asahTaskRunnable.isForce());
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private AsahTaskManager _asahTaskManager;

	@MockBean
	private AsahTaskScheduler _asahTaskScheduler;

	@Mock
	private BoundedExecutor _boundedExecutor;

	@Mock
	private BoundedExecutor _updateMembershipsNaniteBoundedExecutor;

}