/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author Marcellus Tavares
 */
public class OSBAsahCachingConfigurerSupportTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testKeyGenerator() throws Exception {
		Class<?> clazz = Math.class;

		Method method1 = clazz.getMethod(
			"multiplyExact", Long.class, Integer.class);
		Method method2 = clazz.getMethod(
			"multiplyExact", Integer.class, Long.class);

		Assertions.assertNotEquals(method1.toString(), method2.toString());

		Assertions.assertNotEquals(
			_keyGenerator.generate(this, method1, 1, 2),
			_keyGenerator.generate(this, method2, 1, 2));
	}

	@Autowired
	private KeyGenerator _keyGenerator;

	private class Math {

		@SuppressWarnings("unused")
		public Double multiplyExact(Integer number1, Long number2) {
			return (double)(number1 * number2);
		}

		@SuppressWarnings("unused")
		public Double multiplyExact(Long number1, Integer number2) {
			return (double)(number1 * number2);
		}

	}

}