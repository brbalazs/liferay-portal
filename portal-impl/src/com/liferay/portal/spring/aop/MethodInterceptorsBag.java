/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class MethodInterceptorsBag {

	public MethodInterceptorsBag(
		List<MethodInterceptor> classLevelMethodInterceptors,
		List<MethodInterceptor> mergedMethodInterceptors) {

		_classLevelMethodInterceptors = classLevelMethodInterceptors;
		_mergedMethodInterceptors = mergedMethodInterceptors;
	}

	public List<MethodInterceptor> getClassLevelMethodInterceptors() {
		return _classLevelMethodInterceptors;
	}

	public List<MethodInterceptor> getMergedMethodInterceptors() {
		return _mergedMethodInterceptors;
	}

	public void setClassLevelMethodInterceptors(
		List<MethodInterceptor> classLevelMethodInterceptors) {

		_classLevelMethodInterceptors = classLevelMethodInterceptors;
	}

	public void setMergedMethodInterceptors(
		List<MethodInterceptor> mergedMethodInterceptors) {

		_mergedMethodInterceptors = mergedMethodInterceptors;
	}

	private List<MethodInterceptor> _classLevelMethodInterceptors;
	private List<MethodInterceptor> _mergedMethodInterceptors;

}