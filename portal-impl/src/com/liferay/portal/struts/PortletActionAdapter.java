/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mika Koivisto
 */
public class PortletActionAdapter extends PortletAction {

	public PortletActionAdapter(StrutsPortletAction strutsPortletAction) {
		_strutsPortletAction = strutsPortletAction;
	}

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		StrutsPortletAction originalStrutsPortletAction = null;

		if (_originalPortletAction != null) {
			originalStrutsPortletAction = new StrutsPortletActionAdapter(
				_originalPortletAction, actionMapping, actionForm);
		}

		_strutsPortletAction.processAction(
			originalStrutsPortletAction, portletConfig, actionRequest,
			actionResponse);
	}

	@Override
	public ActionForward render(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		StrutsPortletAction originalStrutsPortletAction = null;

		if (_originalPortletAction != null) {
			originalStrutsPortletAction = new StrutsPortletActionAdapter(
				_originalPortletAction, actionMapping, actionForm);
		}

		String forward = _strutsPortletAction.render(
			originalStrutsPortletAction, portletConfig, renderRequest,
			renderResponse);

		if (Validator.isNull(forward)) {
			return null;
		}

		ActionForward actionForward = actionMapping.findForward(forward);

		if (actionForward == null) {
			actionForward = new ActionForward(forward);
		}

		return actionForward;
	}

	@Override
	public void serveResource(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
		throws Exception {

		StrutsPortletAction originalStrutsPortletAction = null;

		if (_originalPortletAction != null) {
			originalStrutsPortletAction = new StrutsPortletActionAdapter(
				_originalPortletAction, actionMapping, actionForm);
		}

		_strutsPortletAction.serveResource(
			originalStrutsPortletAction, portletConfig, resourceRequest,
			resourceResponse);
	}

	public void setOriginalPortletAction(PortletAction portletAction) {
		_originalPortletAction = portletAction;
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		if (_originalPortletAction != null) {
			return _originalPortletAction.isCheckMethodOnProcessAction();
		}

		return _strutsPortletAction.isCheckMethodOnProcessAction();
	}

	private PortletAction _originalPortletAction;
	private final StrutsPortletAction _strutsPortletAction;

}