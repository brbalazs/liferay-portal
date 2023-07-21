/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.jdbc.aop;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.dao.jdbc.aop.MasterDataSource;
import com.liferay.portal.kernel.dao.jdbc.aop.Operation;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;
import com.liferay.portal.spring.aop.ServiceBeanAopCacheManager;
import com.liferay.portal.spring.transaction.TransactionInterceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

/**
 * @author Shuyang Zhou
 * @author László Csontos
 */
public class DynamicDataSourceAdvice
	extends AnnotationChainableMethodAdvice<MasterDataSource> {

	@Override
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		Operation operation = Operation.WRITE;

		Object targetBean = methodInvocation.getThis();

		Class<?> targetClass = targetBean.getClass();

		Method targetMethod = methodInvocation.getMethod();

		MasterDataSource masterDataSource = findAnnotation(methodInvocation);

		if (masterDataSource == _nullMasterDataSource) {
			TransactionAttribute transactionAttribute =
				_transactionInterceptor.getTransactionAttribute(
					methodInvocation);

			if ((transactionAttribute != null) &&
				transactionAttribute.isReadOnly()) {

				operation = Operation.READ;
			}
		}

		_dynamicDataSourceTargetSource.setOperation(operation);

		String targetClassName = targetClass.getName();

		_dynamicDataSourceTargetSource.pushMethod(
			targetClassName.concat(
				StringPool.PERIOD
			).concat(
				targetMethod.getName()
			));

		return null;
	}

	@Override
	public void duringFinally(MethodInvocation methodInvocation) {
		_dynamicDataSourceTargetSource.popMethod();
	}

	@Override
	public MasterDataSource getNullAnnotation() {
		return _nullMasterDataSource;
	}

	public void setDynamicDataSourceTargetSource(
		DynamicDataSourceTargetSource dynamicDataSourceTargetSource) {

		_dynamicDataSourceTargetSource = dynamicDataSourceTargetSource;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setTransactionAttributeSource(
		TransactionAttributeSource transactionAttributeSource) {
	}

	public void setTransactionInterceptor(
		TransactionInterceptor transactionInterceptor) {

		setNextMethodInterceptor(transactionInterceptor);

		_transactionInterceptor = transactionInterceptor;
	}

	@Override
	protected MasterDataSource findAnnotation(
		MethodInvocation methodInvocation) {

		return serviceBeanAopCacheManager.findAnnotation(
			methodInvocation, MasterDataSource.class, _nullMasterDataSource);
	}

	@Override
	protected void setServiceBeanAopCacheManager(
		ServiceBeanAopCacheManager serviceBeanAopCacheManager) {

		super.setServiceBeanAopCacheManager(serviceBeanAopCacheManager);
	}

	private static final MasterDataSource _nullMasterDataSource =
		new MasterDataSource() {

			@Override
			public Class<? extends MasterDataSource> annotationType() {
				return MasterDataSource.class;
			}

		};

	private DynamicDataSourceTargetSource _dynamicDataSourceTargetSource;
	private TransactionInterceptor _transactionInterceptor;

}