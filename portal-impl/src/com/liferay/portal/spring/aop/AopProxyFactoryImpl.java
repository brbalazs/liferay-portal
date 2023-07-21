/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.kernel.spring.aop.AopProxy;
import com.liferay.portal.kernel.spring.aop.AopProxyFactory;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @author Tina Tian
 */
public class AopProxyFactoryImpl implements AopProxyFactory, BeanFactoryAware {

	public void afterPropertiesSet() {
		ListableBeanFactory listableBeanFactory =
			(ListableBeanFactory)_beanFactory;

		Map<String, ChainableMethodAdviceInjector>
			chainableMethodAdviceInjectors = listableBeanFactory.getBeansOfType(
				ChainableMethodAdviceInjector.class);

		for (ChainableMethodAdviceInjector chainableMethodAdviceInjector :
				chainableMethodAdviceInjectors.values()) {

			chainableMethodAdviceInjector.inject();
		}

		_serviceBeanAopCacheManager = new ServiceBeanAopCacheManager(
			_methodInterceptor);
	}

	public void destroy() {
	}

	@Override
	public AopProxy getAopProxy(AdvisedSupport advisedSupport) {
		return new ServiceBeanAopProxy(
			advisedSupport, _serviceBeanAopCacheManager);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		_beanFactory = beanFactory;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		_methodInterceptor = methodInterceptor;
	}

	private BeanFactory _beanFactory;
	private MethodInterceptor _methodInterceptor;
	private ServiceBeanAopCacheManager _serviceBeanAopCacheManager;

}