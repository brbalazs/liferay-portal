/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import com.liferay.portal.kernel.util.StringBundler;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 */
public class ChainableMethodAdviceInjector {

	public void inject() {
		if (!_injectCondition) {
			return;
		}

		_injectCondition = false;

		if (_newChainableMethodAdvice == null) {
			throw new IllegalArgumentException(
				"New Chainable method advice is null");
		}

		if (_parentChainableMethodAdvice == null) {
			throw new IllegalArgumentException(
				"Parent chainable method advice is null");
		}

		if (_childMethodInterceptor == null) {
			_newChainableMethodAdvice.nextMethodInterceptor =
				_parentChainableMethodAdvice.nextMethodInterceptor;
			_parentChainableMethodAdvice.nextMethodInterceptor =
				_newChainableMethodAdvice;

			return;
		}

		ChainableMethodAdvice parentChainableMethodAdvice =
			_parentChainableMethodAdvice;

		while ((parentChainableMethodAdvice != null) &&
			   (parentChainableMethodAdvice.nextMethodInterceptor !=
				   _childMethodInterceptor)) {

			MethodInterceptor methodInterceptor =
				parentChainableMethodAdvice.nextMethodInterceptor;

			if (!(methodInterceptor instanceof ChainableMethodAdvice)) {
				break;
			}

			parentChainableMethodAdvice =
				(ChainableMethodAdvice)methodInterceptor;
		}

		if (parentChainableMethodAdvice.nextMethodInterceptor !=
				_childMethodInterceptor) {

			throw new IllegalArgumentException(
				StringBundler.concat(
					"Unable to find ", String.valueOf(_childMethodInterceptor),
					" from ", String.valueOf(_parentChainableMethodAdvice)));
		}

		_newChainableMethodAdvice.nextMethodInterceptor =
			parentChainableMethodAdvice.nextMethodInterceptor;

		parentChainableMethodAdvice.nextMethodInterceptor =
			_newChainableMethodAdvice;
	}

	public void setChildMethodInterceptor(
		MethodInterceptor childMethodInterceptor) {

		_childMethodInterceptor = childMethodInterceptor;
	}

	public void setInjectCondition(boolean injectCondition) {
		_injectCondition = injectCondition;
	}

	public void setNewChainableMethodAdvice(
		ChainableMethodAdvice newChainableMethodAdvice) {

		_newChainableMethodAdvice = newChainableMethodAdvice;
	}

	public void setParentChainableMethodAdvice(
		ChainableMethodAdvice parentChainableMethodAdvice) {

		_parentChainableMethodAdvice = parentChainableMethodAdvice;
	}

	private MethodInterceptor _childMethodInterceptor;
	private boolean _injectCondition;
	private ChainableMethodAdvice _newChainableMethodAdvice;
	private ChainableMethodAdvice _parentChainableMethodAdvice;

}