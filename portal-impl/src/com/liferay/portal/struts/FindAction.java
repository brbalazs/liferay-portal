/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.portlet.BasePortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class FindAction extends Action {

	public FindAction() {
		_findActionHelper = new BaseStrutsPortletFindActionHelper() {

			@Override
			public long getGroupId(long primaryKey) throws Exception {
				return FindAction.this.getGroupId(primaryKey);
			}

			@Override
			public String getPrimaryKeyParameterName() {
				return FindAction.this.getPrimaryKeyParameterName();
			}

			@Override
			public String getStrutsAction(
				HttpServletRequest request, String portletId) {

				return FindAction.this.getStrutsAction(request, portletId);
			}

			@Override
			public PortletURL processPortletURL(
					HttpServletRequest request, PortletURL portletURL)
				throws Exception {

				return FindAction.this.processPortletURL(request, portletURL);
			}

			@Override
			public void setPrimaryKeyParameter(
					PortletURL portletURL, long primaryKey)
				throws Exception {

				FindAction.this.setPrimaryKeyParameter(portletURL, primaryKey);
			}

			@Override
			protected PortletLayoutFinder getPortletLayoutFinder() {
				return FindAction.this._portletLayoutFinder;
			}

		};
	}

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		_findActionHelper.execute(request, response);

		return null;
	}

	protected abstract long getGroupId(long primaryKey) throws Exception;

	protected abstract String getPrimaryKeyParameterName();

	protected abstract String getStrutsAction(
		HttpServletRequest request, String portletId);

	protected abstract String[] initPortletIds();

	protected PortletURL processPortletURL(
			HttpServletRequest request, PortletURL portletURL)
		throws Exception {

		return portletURL;
	}

	protected void setPrimaryKeyParameter(
			PortletURL portletURL, long primaryKey)
		throws Exception {

		portletURL.setParameter(
			getPrimaryKeyParameterName(), String.valueOf(primaryKey));
	}

	private final FindActionHelper _findActionHelper;

	private final PortletLayoutFinder _portletLayoutFinder =
		new BasePortletLayoutFinder() {

			@Override
			protected String[] getPortletIds() {
				return FindAction.this.initPortletIds();
			}

		};

}