/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring;

import java.lang.reflect.Field;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.aop.framework.AdvisedSupport;

/**
 * @author Shuyang Zhou
 */
public class SpringCompatibilityTest {

	@Test
	public void testAbstractAutowireCapableBeanFactory() throws Exception {
		Class<?> abstractAutowireCapableBeanFactoryClass = Class.forName(
			"org.springframework.beans.factory.support." +
				"AbstractAutowireCapableBeanFactory");

		Field filteredPropertyDescriptorsCacheField =
			abstractAutowireCapableBeanFactoryClass.getDeclaredField(
				"filteredPropertyDescriptorsCache");

		Class<?> filteredPropertyDescriptorsCacheClass =
			filteredPropertyDescriptorsCacheField.getType();

		Class<?> clazz = filteredPropertyDescriptorsCacheClass.getClass();

		Assert.assertTrue(
			clazz.getName() + " is not " + Map.class.getName(),
			Map.class.isAssignableFrom(filteredPropertyDescriptorsCacheClass));
	}

	@Test
	public void testAspectJExpressionPointcut() throws Exception {
		Class<?> aspectJExpressionPointcutClass = Class.forName(
			"org.springframework.aop.aspectj.AspectJExpressionPointcut");

		Field shadowMatchCacheField =
			aspectJExpressionPointcutClass.getDeclaredField("shadowMatchCache");

		Class<?> shadowMatchCacheClass = shadowMatchCacheField.getType();

		Class<?> clazz = shadowMatchCacheClass.getClass();

		Assert.assertTrue(
			clazz.getName() + " is not " + Map.class.getName(),
			Map.class.isAssignableFrom(shadowMatchCacheClass));
	}

	@Test
	public void testJdkDynamicAopProxy() throws Exception {
		Class<?> jdkDynamicAopProxyClass = Class.forName(
			"org.springframework.aop.framework.JdkDynamicAopProxy");

		Field advisedField = jdkDynamicAopProxyClass.getDeclaredField(
			"advised");

		Class<?> advisedSupportClass = advisedField.getType();

		Class<?> clazz = advisedSupportClass.getClass();

		Assert.assertTrue(
			clazz.getName() + " is not " + AdvisedSupport.class.getName(),
			advisedSupportClass.equals(AdvisedSupport.class));
	}

}