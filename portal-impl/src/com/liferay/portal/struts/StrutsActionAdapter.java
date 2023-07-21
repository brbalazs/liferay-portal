/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mika Koivisto
 */
public class StrutsActionAdapter extends BaseStrutsAction {

	public StrutsActionAdapter(
		Action action, ActionMapping actionMapping, ActionForm actionForm) {

		_action = action;
		_actionMapping = actionMapping;
		_actionForm = actionForm;
	}

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		currentThread.setContextClassLoader(
			PortalClassLoaderUtil.getClassLoader());

		try {
			ActionForward actionForward = _action.execute(
				_actionMapping, _actionForm, request, response);

			if (actionForward == null) {
				return null;
			}

			return actionForward.getPath();
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	private final Action _action;
	private final ActionForm _actionForm;
	private final ActionMapping _actionMapping;

}