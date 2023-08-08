/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.aop.framework.ProxyFactory;

/**
 * @author Alejo Ceballos
 */
public class OSBAsahCacheUtilTest {

	@Test
	public void testExtractTargetClassWithNoProxyAndNoInterfaces() {
		ProxyFactory proxyFactory = new ProxyFactory();

		proxyFactory.setTarget(new DummySimpleClass());

		Object target = OSBAsahCacheUtil.extractTargetClass(
			proxyFactory.getProxy());

		Class<?> dummySimpleClassClass = DummySimpleClass.class;

		Class<?> targetClass = (Class<?>)target;

		Assertions.assertEquals(
			dummySimpleClassClass.getName(), targetClass.getName());
	}

	@Test
	public void testExtractTargetClassWithProxyAndProxiedInterface() {
		ProxyFactory proxyFactory = new ProxyFactory();

		proxyFactory.addInterface(DummySimpleInterface.class);
		proxyFactory.setTarget(new DummySimpleClass());

		Object target = OSBAsahCacheUtil.extractTargetClass(
			proxyFactory.getProxy());

		Class<?> dummySimpleInterfaceClass = DummySimpleInterface.class;

		Class<?> targetClass = (Class<?>)target;

		Assertions.assertEquals(
			dummySimpleInterfaceClass.getName(), targetClass.getName());
	}

	@Test
	public void testExtractTargetClassWithProxyAndSimpleInterface() {
		ProxyFactory proxyFactory = new ProxyFactory();

		proxyFactory.setTarget(new DummyClassWithInterface());

		Object target = OSBAsahCacheUtil.extractTargetClass(
			proxyFactory.getProxy());

		Class<?> dummyClassWithInterfaceClass = DummyClassWithInterface.class;

		Class<?> targetClass = (Class<?>)target;

		Assertions.assertEquals(
			dummyClassWithInterfaceClass.getName(), targetClass.getName());
	}

}