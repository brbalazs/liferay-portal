/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author Preston Crary
 */
public class BeanNameMatcher implements BeanMatcher {

	public void afterPropertiesSet() {
		if (_beanNamePattern == null) {
			throw new IllegalStateException("Bean name pattern is null");
		}
	}

	@Override
	public boolean match(Class<?> beanClass, String beanName) {
		return StringUtil.wildcardMatches(
			beanName, _beanNamePattern, CharPool.QUESTION, CharPool.STAR,
			CharPool.PERCENT, true);
	}

	public void setBeanNamePattern(String beanNamePattern) {
		_beanNamePattern = beanNamePattern;
	}

	private String _beanNamePattern;

}