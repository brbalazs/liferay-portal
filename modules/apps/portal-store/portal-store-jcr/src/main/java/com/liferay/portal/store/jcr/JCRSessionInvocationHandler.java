/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.jcr;

import com.liferay.petra.memory.FinalizeAction;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Session;

/**
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
public class JCRSessionInvocationHandler
	implements FinalizeAction, InvocationHandler {

	public JCRSessionInvocationHandler(Session session) {
		_session = session;

		if (_log.isDebugEnabled()) {
			_log.debug("Starting session " + _session);
		}
	}

	@Override
	public void doFinalize(Reference<?> reference) {
		for (Map.Entry<String, Binary> entry : _binaries.entrySet()) {
			Binary binary = entry.getValue();

			binary.dispose();
		}

		_session.logout();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		String methodName = method.getName();

		if (methodName.equals("logout")) {
			if (_log.isDebugEnabled()) {
				_log.debug("Skipping logout for session " + _session);
			}

			return null;
		}
		else if (methodName.equals("put")) {
			String key = (String)arguments[0];
			Binary binary = (Binary)arguments[1];

			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Tracking binary ", key, " for session ", _session));
			}

			_binaries.put(key, binary);

			return null;
		}

		try {
			return method.invoke(_session, arguments);
		}
		catch (InvocationTargetException ite) {
			throw ite.getCause();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JCRSessionInvocationHandler.class);

	private final Map<String, Binary> _binaries = new HashMap<>();
	private final Session _session;

}