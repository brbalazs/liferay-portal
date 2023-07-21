/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.internal.cluster;

import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutorUtil;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.cluster.ClusterableInvokerUtil;
import com.liferay.portal.kernel.cluster.NullClusterable;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 */
public class ClusterableAdvice
	extends AnnotationChainableMethodAdvice<Clusterable> {

	@Override
	public void afterReturning(MethodInvocation methodInvocation, Object result)
		throws Throwable {

		if (!ClusterInvokeThreadLocal.isEnabled()) {
			return;
		}

		Clusterable clusterable = findAnnotation(methodInvocation);

		if (clusterable == NullClusterable.NULL_CLUSTERABLE) {
			return;
		}

		ClusterableInvokerUtil.invokeOnCluster(
			clusterable.acceptor(), methodInvocation.getThis(),
			methodInvocation.getMethod(), methodInvocation.getArguments());
	}

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		if (!ClusterInvokeThreadLocal.isEnabled()) {
			return null;
		}

		Clusterable clusterable = findAnnotation(methodInvocation);

		if (clusterable == NullClusterable.NULL_CLUSTERABLE) {
			return null;
		}

		if (!clusterable.onMaster()) {
			return null;
		}

		Object result = null;

		if (ClusterMasterExecutorUtil.isMaster()) {
			result = methodInvocation.proceed();
		}
		else {
			result = ClusterableInvokerUtil.invokeOnMaster(
				clusterable.acceptor(), methodInvocation.getThis(),
				methodInvocation.getMethod(), methodInvocation.getArguments());
		}

		Method method = methodInvocation.getMethod();

		Class<?> returnType = method.getReturnType();

		if (returnType == void.class) {
			result = nullResult;
		}

		return result;
	}

	@Override
	public Clusterable getNullAnnotation() {
		return NullClusterable.NULL_CLUSTERABLE;
	}

}