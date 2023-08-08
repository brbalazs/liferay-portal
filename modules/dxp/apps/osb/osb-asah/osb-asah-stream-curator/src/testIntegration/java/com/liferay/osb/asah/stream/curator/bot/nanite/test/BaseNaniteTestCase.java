/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.test;

import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Inácio Nery
 */
public abstract class BaseNaniteTestCase
	implements OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		_projectDog.addProject("test");

		ProjectIdThreadLocal.setProjectId("test");
	}

	protected abstract Nanite getNanite();

	protected void runNanite() {
		try {
			Nanite nanite = getNanite();

			nanite.run();
		}
		finally {
			ProjectIdThreadLocal.setProjectId("test");
		}
	}

	@Autowired
	private ProjectDog _projectDog;

}