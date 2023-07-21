/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts.bundle.strutsactionregistryutil;

import com.liferay.portal.kernel.struts.StrutsPortletAction;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"path=TestStrutsPortletAction",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = StrutsPortletAction.class
)
public class TestStrutsPortletAction implements StrutsPortletAction {

	@Override
	public boolean isCheckMethodOnProcessAction() {
		_atomicBoolean.set(Boolean.TRUE);

		return false;
	}

	@Override
	public void processAction(
		PortletConfig portletConfig, ActionRequest actionRequest,
		ActionResponse actionResponse) {
	}

	@Override
	public void processAction(
		StrutsPortletAction originalStrutsPortletAction,
		PortletConfig portletConfig, ActionRequest actionRequest,
		ActionResponse actionResponse) {
	}

	@Override
	public String render(
		PortletConfig portletConfig, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		return null;
	}

	@Override
	public String render(
		StrutsPortletAction originalStrutsPortletAction,
		PortletConfig portletConfig, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		return null;
	}

	@Override
	public void serveResource(
		PortletConfig portletConfig, ResourceRequest resourceRequest,
		ResourceResponse resourceResponse) {
	}

	@Override
	public void serveResource(
		StrutsPortletAction originalStrutsPortletAction,
		PortletConfig portletConfig, ResourceRequest resourceRequest,
		ResourceResponse resourceResponse) {
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}