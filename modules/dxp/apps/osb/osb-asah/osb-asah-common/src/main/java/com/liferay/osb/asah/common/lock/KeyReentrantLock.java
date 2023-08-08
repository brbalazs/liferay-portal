/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.lock;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Inácio Nery
 */
public class KeyReentrantLock {

	public static ReentrantLock getReentrantLock(
		Class<?> clazz, Object... keys) {

		return _reentrantLocks.get(
			clazz.getSimpleName() + "#" + Arrays.toString(keys),
			computedKey -> new ReentrantLock(true));
	}

	private static final Cache<String, ReentrantLock> _reentrantLocks =
		Caffeine.newBuilder(
		).expireAfterAccess(
			10, TimeUnit.MINUTES
		).maximumSize(
			1000000
		).build();

}