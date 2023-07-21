/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 */
public class ServiceBeanMethodInvocation
	implements MethodInvocation, Serializable {

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #ServiceBeanMethodInvocation(Object, Method, Object[])}
	 */
	@Deprecated
	public ServiceBeanMethodInvocation(
		Object target, Class<?> targetClass, Method method,
		Object[] arguments) {

		this(target, method, arguments);
	}

	public ServiceBeanMethodInvocation(
		Object target, Method method, Object[] arguments) {

		_target = target;
		_method = method;
		_arguments = arguments;

		_method.setAccessible(true);

		if (_method.getDeclaringClass() == Object.class) {
			String methodName = _method.getName();

			if (methodName.equals("equals")) {
				_equalsMethod = true;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ServiceBeanMethodInvocation)) {
			return false;
		}

		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			(ServiceBeanMethodInvocation)obj;

		if (Objects.equals(_method, serviceBeanMethodInvocation._method)) {
			return true;
		}

		return false;
	}

	@Override
	public Object[] getArguments() {
		return _arguments;
	}

	@Override
	public Method getMethod() {
		return _method;
	}

	@Override
	public AccessibleObject getStaticPart() {
		return _method;
	}

	public Class<?> getTargetClass() {
		return _target.getClass();
	}

	@Override
	public Object getThis() {
		return _target;
	}

	@Override
	public int hashCode() {
		if (_hashCode == 0) {
			_hashCode = _method.hashCode();
		}

		return _hashCode;
	}

	public void mark() {
		_markIndex = _index;
	}

	@Override
	public Object proceed() throws Throwable {
		if (_index < _methodInterceptors.length) {
			return _methodInterceptors[_index++].invoke(this);
		}

		if (_equalsMethod) {
			Object argument = _arguments[0];

			if (argument == null) {
				return false;
			}

			if (ProxyUtil.isProxyClass(argument.getClass())) {
				AdvisedSupport advisedSupport =
					ServiceBeanAopProxy.getAdvisedSupport(argument);

				if (advisedSupport != null) {
					argument = advisedSupport.getTarget();
				}
			}

			return _target.equals(argument);
		}

		try {
			return _method.invoke(_target, _arguments);
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	public void reset() {
		_index = _markIndex;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setMethodInterceptors(
		List<MethodInterceptor> methodInterceptors) {

		setMethodInterceptors(
			methodInterceptors.toArray(new MethodInterceptor[0]));
	}

	public void setMethodInterceptors(MethodInterceptor[] methodInterceptors) {
		_methodInterceptors = methodInterceptors;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public ServiceBeanMethodInvocation toCacheKeyModel() {
		ServiceBeanMethodInvocation serviceBeanMethodInvocation =
			new ServiceBeanMethodInvocation(null, _method, null);

		serviceBeanMethodInvocation._equalsMethod = _equalsMethod;
		serviceBeanMethodInvocation._hashCode = _hashCode;

		return serviceBeanMethodInvocation;
	}

	@Override
	public String toString() {
		if (_toString != null) {
			return _toString;
		}

		Class<?>[] parameterTypes = _method.getParameterTypes();

		StringBundler sb = new StringBundler((parameterTypes.length * 2) + 6);

		Class<?> declaringClass = _method.getDeclaringClass();

		sb.append(declaringClass.getName());

		sb.append(StringPool.PERIOD);
		sb.append(_method.getName());
		sb.append(StringPool.OPEN_PARENTHESIS);

		for (Class<?> parameterType : parameterTypes) {
			sb.append(parameterType.getName());

			sb.append(StringPool.COMMA);
		}

		if (parameterTypes.length > 0) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append(StringPool.CLOSE_PARENTHESIS);

		sb.append(StringPool.AT);

		Class<?> targetClass = _target.getClass();

		sb.append(targetClass.getName());

		_toString = sb.toString();

		return _toString;
	}

	private final Object[] _arguments;
	private boolean _equalsMethod;
	private int _hashCode;
	private int _index;
	private int _markIndex;
	private final Method _method;
	private MethodInterceptor[] _methodInterceptors;
	private final Object _target;
	private String _toString;

}