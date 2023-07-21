/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.jdbc.aop;

import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.dao.jdbc.aop.Operation;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Stack;

import javax.sql.DataSource;

import org.springframework.aop.TargetSource;

/**
 * @author Michael Young
 */
public class DefaultDynamicDataSourceTargetSource
	implements DynamicDataSourceTargetSource, TargetSource {

	@Override
	public Stack<String> getMethodStack() {
		Stack<String> methodStack = _methodStack.get();

		if (methodStack == null) {
			methodStack = new Stack<>();

			_methodStack.set(methodStack);
		}

		return methodStack;
	}

	@Override
	public Operation getOperation() {
		Operation operation = _operationType.get();

		if (operation == null) {
			operation = Operation.WRITE;

			_operationType.set(operation);
		}

		return operation;
	}

	@Override
	public DataSource getReadDataSource() {
		return _readDataSource;
	}

	@Override
	public Object getTarget() {
		Operation operationType = getOperation();

		if (operationType == Operation.READ) {
			if (_log.isTraceEnabled()) {
				_log.trace("Returning read data source");
			}

			return _readDataSource;
		}

		if (_log.isTraceEnabled()) {
			_log.trace("Returning write data source");
		}

		return _writeDataSource;
	}

	@Override
	public Class<DataSource> getTargetClass() {
		return DataSource.class;
	}

	@Override
	public DataSource getWriteDataSource() {
		return _writeDataSource;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public String popMethod() {
		Stack<String> methodStack = getMethodStack();

		String method = methodStack.pop();

		if (methodStack.isEmpty()) {
			_operationType.remove();
		}

		return method;
	}

	@Override
	public void pushMethod(String method) {
		Stack<String> methodStack = getMethodStack();

		methodStack.push(method);
	}

	@Override
	public void releaseTarget(Object target) throws Exception {
	}

	@Override
	public void setOperation(Operation operation) {
		if (_log.isDebugEnabled()) {
			_log.debug("Method stack " + getMethodStack());
		}

		if (!inOperation() || (operation == Operation.WRITE)) {
			_operationType.set(operation);
		}
	}

	@Override
	public void setReadDataSource(DataSource readDataSource) {
		_readDataSource = readDataSource;
	}

	@Override
	public void setWriteDataSource(DataSource writeDataSource) {
		_writeDataSource = writeDataSource;
	}

	protected boolean inOperation() {
		Stack<String> methodStack = getMethodStack();

		return !methodStack.empty();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultDynamicDataSourceTargetSource.class);

	private static final ThreadLocal<Stack<String>> _methodStack =
		new ThreadLocal<>();
	private static final ThreadLocal<Operation> _operationType =
		new ThreadLocal<>();

	private DataSource _readDataSource;
	private DataSource _writeDataSource;

}