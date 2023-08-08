/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.dog.exception.InvalidProjectIdException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author André Miranda
 */
public class ProjectIdThreadLocalTest {

	@Test
	public void testSetInvalidProjectId() {
		Assertions.assertThrows(
			InvalidProjectIdException.class,
			() -> ProjectIdThreadLocal.setProjectId(
				"../../../now_whatever?param1=&"));
	}

	@Test
	public void testSetProjectId() {
		ProjectIdThreadLocal.setProjectId(
			"asah652a6babdba143d086a19db542781bc2");

		Assertions.assertEquals(
			"asah652a6babdba143d086a19db542781bc2",
			ProjectIdThreadLocal.getProjectId());
	}

}