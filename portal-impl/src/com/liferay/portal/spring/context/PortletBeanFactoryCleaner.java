/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.context;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.exception.LoggedExceptionInInitializerError;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.weaver.tools.ShadowMatch;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @author Shuyang Zhou
 */
public class PortletBeanFactoryCleaner implements BeanFactoryAware {

	public static void readBeans() {
		if (_beanFactory == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Bean factory is null");
			}

			return;
		}

		if (!(_beanFactory instanceof ListableBeanFactory)) {
			return;
		}

		ListableBeanFactory listableBeanFactory =
			(ListableBeanFactory)_beanFactory;

		String[] names = listableBeanFactory.getBeanDefinitionNames();

		for (String name : names) {
			try {
				_readBean(listableBeanFactory, name);
			}
			catch (Exception e) {
			}
		}
	}

	public void destroy() {
		for (BeanFactoryAware beanFactoryAware : _beanFactoryAwares) {
			try {
				beanFactoryAware.setBeanFactory(null);
			}
			catch (Exception e) {
			}
		}

		_beanFactoryAwares.clear();

		for (AspectJExpressionPointcut aspectJExpressionPointcut :
				_aspectJExpressionPointcuts) {

			try {
				Map<Method, ShadowMatch> shadowMatchCache =
					(Map<Method, ShadowMatch>)_SHADOW_MATCH_CACHE_FIELD.get(
						aspectJExpressionPointcut);

				shadowMatchCache.clear();
			}
			catch (Exception e) {
			}
		}

		_aspectJExpressionPointcuts.clear();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		_beanFactory = beanFactory;
	}

	private static void _readBean(
			ListableBeanFactory listableBeanFactory, String name)
		throws Exception {

		Object bean = listableBeanFactory.getBean(name);

		if (bean instanceof AspectJPointcutAdvisor) {
			AspectJPointcutAdvisor aspectJPointcutAdvisor =
				(AspectJPointcutAdvisor)bean;

			Pointcut pointcut = aspectJPointcutAdvisor.getPointcut();

			ClassFilter classFilter = pointcut.getClassFilter();

			if (classFilter instanceof AspectJExpressionPointcut) {
				AspectJExpressionPointcut aspectJExpressionPointcut =
					(AspectJExpressionPointcut)classFilter;

				_beanFactoryAwares.add(aspectJExpressionPointcut);
				_aspectJExpressionPointcuts.add(aspectJExpressionPointcut);
			}
		}
		else if (bean instanceof BeanFactoryAware) {
			_beanFactoryAwares.add((BeanFactoryAware)bean);
		}
	}

	private static final Field _SHADOW_MATCH_CACHE_FIELD;

	private static final Log _log = LogFactoryUtil.getLog(
		PortletBeanFactoryCleaner.class);

	private static final Set<AspectJExpressionPointcut>
		_aspectJExpressionPointcuts = new HashSet<>();
	private static BeanFactory _beanFactory;
	private static final Set<BeanFactoryAware> _beanFactoryAwares =
		new HashSet<>();

	static {
		try {
			_SHADOW_MATCH_CACHE_FIELD = ReflectionUtil.getDeclaredField(
				AspectJExpressionPointcut.class, "shadowMatchCache");
		}
		catch (Exception e) {
			throw new LoggedExceptionInInitializerError(e);
		}
	}

}