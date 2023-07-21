/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.util;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.spring.util.FactoryBean;
import com.liferay.portal.kernel.spring.util.SpringFactory;
import com.liferay.portal.kernel.spring.util.SpringFactoryException;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
public class SpringFactoryImpl implements SpringFactory {

	@Override
	public Object newBean(String className) throws SpringFactoryException {
		return newBean(className, null);
	}

	@Override
	public Object newBean(String className, Map<String, Object> properties)
		throws SpringFactoryException {

		try {
			return doNewBean(className, properties);
		}
		catch (SpringFactoryException sfe) {
			throw sfe;
		}
		catch (Exception e) {
			throw new SpringFactoryException(e);
		}
	}

	public void setBeanDefinitions(Map<String, String> beanDefinitions) {
		_beanDefinitions = new HashMap<>();

		for (Map.Entry<String, String> entry : beanDefinitions.entrySet()) {
			String className = entry.getKey();

			Set<String> properties = SetUtil.fromArray(
				StringUtil.split(entry.getValue()));

			_beanDefinitions.put(className, properties);
		}
	}

	protected Object doNewBean(String className, Map<String, Object> properties)
		throws Exception {

		Set<String> allowedProperties = _beanDefinitions.get(className);

		if (allowedProperties == null) {
			throw new SpringFactoryException("Undefined class " + className);
		}

		Object bean = InstanceFactory.newInstance(
			PortalClassLoaderUtil.getClassLoader(), className);

		FactoryBean<Object> factoryBean = null;

		if (bean instanceof FactoryBean) {
			factoryBean = (FactoryBean<Object>)bean;

			bean = factoryBean.create();
		}

		if (properties != null) {
			for (Map.Entry<String, Object> entry : properties.entrySet()) {
				String name = entry.getKey();

				if (!allowedProperties.contains(name)) {
					throw new SpringFactoryException(
						StringBundler.concat(
							"Undefined property ", name, " for class ",
							className));
				}

				Object value = entry.getValue();

				BeanPropertiesUtil.setProperty(bean, name, value);
			}
		}

		if (factoryBean != null) {
			bean = factoryBean.postProcessing(bean);
		}

		return bean;
	}

	private Map<String, Set<String>> _beanDefinitions;

}