/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.context;

import com.liferay.portal.kernel.spring.util.SpringFactoryUtil;

import java.util.Map;

import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletBeanFactoryPostProcessor
	implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(
		ConfigurableListableBeanFactory configurableListableBeanFactory) {

		ClassLoader classLoader = getClassLoader();

		configurableListableBeanFactory.setBeanClassLoader(classLoader);

		ListableBeanFactory parentListableBeanFactory =
			(ListableBeanFactory)
				configurableListableBeanFactory.getParentBeanFactory();

		if (parentListableBeanFactory != null) {
			Map<String, BeanPostProcessor> beanPostProcessors =
				parentListableBeanFactory.getBeansOfType(
					BeanPostProcessor.class, true, false);

			for (BeanPostProcessor beanPostProcessor :
					beanPostProcessors.values()) {

				if (beanPostProcessor instanceof BeanFactoryAware) {
					BeanFactoryAware beanFactoryAware =
						(BeanFactoryAware)beanPostProcessor;

					beanFactoryAware.setBeanFactory(
						configurableListableBeanFactory);
				}

				if (beanPostProcessor instanceof AbstractAutoProxyCreator) {
					AbstractAutoProxyCreator abstractAutoProxyCreator =
						(AbstractAutoProxyCreator)beanPostProcessor;

					abstractAutoProxyCreator.setProxyClassLoader(classLoader);
				}

				configurableListableBeanFactory.addBeanPostProcessor(
					beanPostProcessor);
			}
		}

		String[] names =
			configurableListableBeanFactory.getBeanDefinitionNames();

		for (String name : names) {
			if (!name.contains(SpringFactoryUtil.class.getName())) {
				continue;
			}

			try {
				Object bean = configurableListableBeanFactory.getBean(name);

				if (bean instanceof BeanPostProcessor) {
					configurableListableBeanFactory.addBeanPostProcessor(
						(BeanPostProcessor)bean);
				}
			}
			catch (BeanIsAbstractException biae) {
			}
		}
	}

	protected ClassLoader getClassLoader() {
		return PortletApplicationContext.getBeanClassLoader();
	}

}