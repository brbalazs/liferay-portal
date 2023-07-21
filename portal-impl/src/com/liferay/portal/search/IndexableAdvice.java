/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 */
public class IndexableAdvice
	extends AnnotationChainableMethodAdvice<Indexable> {

	@Override
	public void afterReturning(MethodInvocation methodInvocation, Object result)
		throws Throwable {

		if (result == null) {
			return;
		}

		if (CompanyThreadLocal.isDeleteInProcess() ||
			IndexWriterHelperUtil.isIndexReadOnly()) {

			if (_log.isDebugEnabled()) {
				if (CompanyThreadLocal.isDeleteInProcess()) {
					_log.debug(
						"Skip indexing because company delete is in process");
				}
				else if (IndexWriterHelperUtil.isIndexReadOnly()) {
					_log.debug("Skip indexing because the index is read only");
				}
			}

			return;
		}

		Indexable indexable = findAnnotation(methodInvocation);

		if (indexable == _nullIndexable) {
			return;
		}

		Method method = methodInvocation.getMethod();

		Class<?> returnType = method.getReturnType();

		if (!BaseModel.class.isAssignableFrom(returnType)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					methodInvocation + " does not have a valid return type");
			}

			return;
		}

		Indexer<Object> indexer = IndexerRegistryUtil.getIndexer(
			returnType.getName());

		if (indexer == null) {
			serviceBeanAopCacheManager.removeMethodInterceptor(
				methodInvocation, this);

			return;
		}

		if (IndexWriterHelperUtil.isIndexReadOnly(indexer.getClassName())) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Skipping indexing read only index for " +
						indexer.getClassName());
			}

			return;
		}

		Object[] arguments = methodInvocation.getArguments();

		for (int i = arguments.length - 1; i >= 0; i--) {
			if (arguments[i] instanceof ServiceContext) {
				ServiceContext serviceContext = (ServiceContext)arguments[i];

				if (serviceContext.isIndexingEnabled()) {
					break;
				}

				return;
			}
		}

		if (indexable.type() == IndexableType.DELETE) {
			indexer.delete(result);
		}
		else {
			indexer.reindex(result);
		}
	}

	@Override
	public Indexable getNullAnnotation() {
		return _nullIndexable;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		IndexableAdvice.class);

	private static final Indexable _nullIndexable = new Indexable() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return Indexable.class;
		}

		@Override
		public IndexableType type() {
			return null;
		}

	};

}