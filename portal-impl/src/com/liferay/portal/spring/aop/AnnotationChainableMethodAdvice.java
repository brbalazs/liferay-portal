/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public abstract class AnnotationChainableMethodAdvice<T extends Annotation>
	extends ChainableMethodAdvice {

	public AnnotationChainableMethodAdvice() {
		_nullAnnotation = getNullAnnotation();

		_annotationClass = _nullAnnotation.annotationType();
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return _annotationClass;
	}

	public abstract T getNullAnnotation();

	protected T findAnnotation(MethodInvocation methodInvocation) {
		T annotation = serviceBeanAopCacheManager.findAnnotation(
			methodInvocation, _annotationClass, _nullAnnotation);

		if (annotation == _nullAnnotation) {
			serviceBeanAopCacheManager.removeMethodInterceptor(
				methodInvocation, this);
		}

		return annotation;
	}

	private final Class<? extends Annotation> _annotationClass;
	private final T _nullAnnotation;

}