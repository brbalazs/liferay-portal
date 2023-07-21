/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.background.task.internal;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskThreadLocalManagerImplTest
	extends BaseBackgroundTaskTestCase {

	@Test
	public void testDeserializeThreadLocals() {
		Map<String, Serializable> taskContextMap = new HashMap<>();

		HashMap<String, Serializable> threadLocalValues =
			initializeThreadLocalValues();

		taskContextMap.put(
			BackgroundTaskThreadLocalManagerImpl.KEY_THREAD_LOCAL_VALUES,
			threadLocalValues);

		backgroundTaskThreadLocalManagerImpl.deserializeThreadLocals(
			taskContextMap);

		assertThreadLocalValues();
	}

	@Test
	public void testGetThreadLocalValues() {
		initalizeThreadLocals();

		Map<String, Serializable> threadLocalValues =
			backgroundTaskThreadLocalManagerImpl.getThreadLocalValues();

		assertThreadLocalValues(threadLocalValues);
	}

	@Test
	public void testSerializeThreadLocals() {
		initalizeThreadLocals();

		Map<String, Serializable> taskContextMap = new HashMap<>();

		backgroundTaskThreadLocalManagerImpl.serializeThreadLocals(
			taskContextMap);

		Map<String, Serializable> threadLocalValues =
			(Map<String, Serializable>)taskContextMap.get(
				BackgroundTaskThreadLocalManagerImpl.KEY_THREAD_LOCAL_VALUES);

		assertThreadLocalValues(threadLocalValues);
	}

	@Test
	public void testSetThreadLocalValues() {
		Map<String, Serializable> threadLocalValues =
			initializeThreadLocalValues();

		backgroundTaskThreadLocalManagerImpl.setThreadLocalValues(
			threadLocalValues);

		assertThreadLocalValues();
	}

}