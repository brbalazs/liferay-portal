/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.jcr;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.store.jcr.configuration.JCRStoreConfiguration;
import com.liferay.portal.store.jcr.jackrabbit.JCRFactoryImpl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author Michael Young
 * @author Manuel de la Peña
 */
public class JCRFactoryWrapper {

	public JCRFactoryWrapper(JCRStoreConfiguration jcrStoreConfiguration)
		throws RepositoryException {

		_jcrStoreConfiguration = jcrStoreConfiguration;

		_jcrFactory = new JCRFactoryImpl(jcrStoreConfiguration);
	}

	public void closeSession(Session session) {
		if (session != null) {
			session.logout();
		}
	}

	public Session createSession() throws RepositoryException {
		return createSession(null);
	}

	public Session createSession(String workspaceName)
		throws RepositoryException {

		if (workspaceName == null) {
			workspaceName = _jcrStoreConfiguration.workspaceName();
		}

		if (!_jcrStoreConfiguration.wrapSession()) {
			JCRFactory jcrFactory = getJCRFactory();

			return jcrFactory.createSession(workspaceName);
		}

		Map<String, Session> sessions = _sessions.get();

		Session session = sessions.get(workspaceName);

		if (session != null) {
			return session;
		}

		JCRFactory jcrFactory = getJCRFactory();

		Session jcrSession = jcrFactory.createSession(workspaceName);

		JCRSessionInvocationHandler jcrSessionInvocationHandler =
			new JCRSessionInvocationHandler(jcrSession);

		Class<?> clazz = getClass();

		Object sessionProxy = ProxyUtil.newProxyInstance(
			clazz.getClassLoader(), new Class<?>[] {Map.class, Session.class},
			jcrSessionInvocationHandler);

		FinalizeManager.register(
			sessionProxy, jcrSessionInvocationHandler,
			FinalizeManager.PHANTOM_REFERENCE_FACTORY);

		session = (Session)sessionProxy;

		sessions.put(workspaceName, session);

		return session;
	}

	public JCRFactory getJCRFactory() {
		return _jcrFactory;
	}

	public void initialize() throws RepositoryException {
		JCRFactory jcrFactory = getJCRFactory();

		jcrFactory.initialize();
	}

	public void prepare() throws RepositoryException {
		JCRFactory jcrFactory = getJCRFactory();

		jcrFactory.prepare();
	}

	public void shutdown() {
		JCRFactory jcrFactory = getJCRFactory();

		jcrFactory.shutdown();
	}

	private static final ThreadLocal<Map<String, Session>> _sessions =
		new CentralizedThreadLocal<>(
			JCRFactoryWrapper.class + "._sessions", HashMap::new);

	private final JCRFactory _jcrFactory;
	private final JCRStoreConfiguration _jcrStoreConfiguration;

}