/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mika Koivisto
 */
public class ActionAdapter extends Action {

	public ActionAdapter(StrutsAction strutsAction) {
		_strutsAction = strutsAction;
	}

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		StrutsAction originalStrutsAction = null;

		if (_originalAction != null) {
			originalStrutsAction = new StrutsActionAdapter(
				_originalAction, actionMapping, actionForm);
		}

		String forward = _strutsAction.execute(
			originalStrutsAction, request, response);

		if (Validator.isNull(forward)) {
			return null;
		}

		ActionForward actionForward = actionMapping.findForward(forward);

		if (actionForward == null) {
			actionForward = new ActionForward(forward);
		}

		return actionForward;
	}

	public void setOriginalAction(Action originalAction) {
		_originalAction = originalAction;
	}

	private Action _originalAction;
	private final StrutsAction _strutsAction;

}