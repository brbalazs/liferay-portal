/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.resiliency.service;

import com.liferay.portal.internal.resiliency.service.ServiceMethodProcessCallable;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiServiceInvokerUtil;
import com.liferay.portal.kernel.nio.intraband.rpc.IntrabandRPCUtil;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIRegistryUtil;
import com.liferay.portal.kernel.security.access.control.AccessControl;
import com.liferay.portal.kernel.security.access.control.AccessControlThreadLocal;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.servlet.ServletContextClassLoaderPool;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.concurrent.Future;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 */
public class PortalResiliencyAdvice
	extends AnnotationChainableMethodAdvice<AccessControlled> {

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		AccessControlled accessControlled = findAnnotation(methodInvocation);

		if (accessControlled == AccessControl.NULL_ACCESS_CONTROLLED) {
			return null;
		}

		boolean remoteAccess = AccessControlThreadLocal.isRemoteAccess();

		if (!remoteAccess) {
			return null;
		}

		Object targetObject = methodInvocation.getThis();

		Class<?> targetClass = targetObject.getClass();

		String servletContextName =
			ServletContextClassLoaderPool.getServletContextName(
				targetClass.getClassLoader());

		SPI spi = null;

		if (servletContextName != null) {
			spi = SPIRegistryUtil.getServletContextSPI(servletContextName);
		}

		if (spi == null) {
			serviceBeanAopCacheManager.removeMethodInterceptor(
				methodInvocation, this);

			return null;
		}

		ServiceMethodProcessCallable serviceMethodProcessCallable =
			new ServiceMethodProcessCallable(
				IdentifiableOSGiServiceInvokerUtil.createMethodHandler(
					methodInvocation.getThis(), methodInvocation.getMethod(),
					methodInvocation.getArguments()));

		Future<Serializable> future = IntrabandRPCUtil.execute(
			spi.getRegistrationReference(), serviceMethodProcessCallable);

		Object result = future.get();

		Method method = methodInvocation.getMethod();

		Class<?> returnType = method.getReturnType();

		if (returnType == void.class) {
			result = nullResult;
		}

		return result;
	}

	@Override
	public AccessControlled getNullAnnotation() {
		return AccessControl.NULL_ACCESS_CONTROLLED;
	}

}