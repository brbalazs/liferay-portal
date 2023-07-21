/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.servlet.filters.compoundsessionid.CompoundSessionIdSplitterUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * See https://issues.liferay.com/browse/LEP-1466.
 * </p>
 *
 * @author     Rudy Hilado
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PortletSessionTracker {

	public static void add(HttpSession session) {
		String sessionId = session.getId();

		if (CompoundSessionIdSplitterUtil.hasSessionDelimiter()) {
			sessionId = CompoundSessionIdSplitterUtil.parseSessionId(sessionId);
		}

		Map<String, HttpSession> sessions = _sessions.get(sessionId);

		if (sessions == null) {
			sessions = new ConcurrentHashMap<>();

			Map<String, HttpSession> previousSessions = _sessions.putIfAbsent(
				sessionId, sessions);

			if (previousSessions != null) {
				sessions = previousSessions;
			}
		}

		ServletContext servletContext = session.getServletContext();

		String contextPath = servletContext.getContextPath();

		// ConcurrentHashMap's read is faster than its write. This check is
		// logically unnecessary, but is a performance improvement.

		if (!sessions.containsKey(contextPath)) {
			sessions.put(contextPath, session);
		}
	}

	public static void invalidate(String sessionId) {
		if (CompoundSessionIdSplitterUtil.hasSessionDelimiter()) {
			sessionId = CompoundSessionIdSplitterUtil.parseSessionId(sessionId);
		}

		Map<String, HttpSession> sessions = _sessions.remove(sessionId);

		if (sessions == null) {
			return;
		}

		for (HttpSession session : sessions.values()) {
			try {
				session.invalidate();
			}
			catch (Exception e) {
			}
		}
	}

	private static final ConcurrentMap<String, Map<String, HttpSession>>
		_sessions = new ConcurrentHashMap<>();

}