/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.messaging.async;

import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiServiceInvokerUtil;
import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.util.MethodHandler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), moved to {@link
 *             com.liferay.portal.internal.messaging.async.AsyncProcessCallable}
 */
@Deprecated
public class AsyncProcessCallable
	implements Externalizable, ProcessCallable<Serializable> {

	public AsyncProcessCallable() {
		this(null);
	}

	public AsyncProcessCallable(MethodInvocation methodInvocation) {
		_methodInvocation = methodInvocation;
	}

	@Override
	public Serializable call() {
		try {
			if (_methodInvocation != null) {
				_methodInvocation.proceed();
			}
			else {
				AsyncInvokeThreadLocal.setEnabled(true);

				try {
					_methodHandler.invoke(null);
				}
				finally {
					AsyncInvokeThreadLocal.setEnabled(false);
				}
			}
		}
		catch (Throwable t) {
			throw new RuntimeException(t);
		}

		return null;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		_methodHandler = (MethodHandler)objectInput.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		MethodHandler methodHandler = _methodHandler;

		if (methodHandler == null) {
			methodHandler =
				IdentifiableOSGiServiceInvokerUtil.createMethodHandler(
					_methodInvocation.getThis(), _methodInvocation.getMethod(),
					_methodInvocation.getArguments());
		}

		objectOutput.writeObject(methodHandler);
	}

	private MethodHandler _methodHandler;
	private final MethodInvocation _methodInvocation;

}