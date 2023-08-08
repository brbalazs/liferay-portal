/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.test;

import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;
import com.liferay.osb.asah.upgrade.UpgradeProcess;
import com.liferay.osb.asah.upgrade.UpgradeProcessRunner;
import com.liferay.osb.asah.upgrade.spring.OSBAsahUpgradeSpringBootApplication;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author André Miranda
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(classes = OSBAsahUpgradeSpringBootApplication.class)
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ResetMocksTestExecutionListener.class
	}
)
public class UpgradeProcessRunnerTest {

	@Test
	public void testProjectId() throws Exception {
		_projectDog.addProject("test1");

		_projectDog.updateVersion("test1", "0.0.0");

		Set<String> projectIds = new HashSet<>();

		Mockito.when(
			_upgradeProcess.getUpgradeSteps(ArgumentMatchers.eq("0.0.0"))
		).thenReturn(
			Collections.singletonList(
				version -> projectIds.add(ProjectIdThreadLocal.getProjectId()))
		);

		Mockito.when(
			_upgradeProcess.getToVersionString(ArgumentMatchers.eq("0.0.0"))
		).thenReturn(
			"1.0.0"
		);

		_upgradeProcessRunner.runProjectUpgrades();

		Assertions.assertEquals(1, projectIds.size(), projectIds.toString());
		Assertions.assertTrue(projectIds.contains("test1"));
	}

	@Test
	public void testVersionFormat() throws Exception {
		_projectDog.addProject("test1");

		_projectDog.updateVersion("test1", "2.11.0");

		Mockito.when(
			_upgradeProcess.getUpgradeSteps(ArgumentMatchers.eq("2.11.0"))
		).thenReturn(
			Collections.singletonList(
				version -> {
				})
		);

		Mockito.when(
			_upgradeProcess.getToVersionString(ArgumentMatchers.eq("2.11.0"))
		).thenReturn(
			"2.12.0"
		);

		_upgradeProcessRunner.runProjectUpgrades();

		Project project = _projectDog.getProject("test1");

		Assertions.assertEquals("2.12.0", project.getVersion());
	}

	@Autowired
	private ProjectDog _projectDog;

	@MockBean
	private UpgradeProcess _upgradeProcess;

	@Autowired
	@InjectMocks
	private UpgradeProcessRunner _upgradeProcessRunner;

}