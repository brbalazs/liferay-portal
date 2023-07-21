/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.action;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.JSONAction;
import com.liferay.portlet.layoutsadmin.util.LayoutsTreeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Eduardo Lundgren
 * @author Zsolt Szabó
 * @author Tibor Lipusz
 */
public class GetLayoutsAction extends JSONAction {

	@Override
	public String getJSON(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String cmd = ParamUtil.getString(request, Constants.CMD);

		long groupId = ParamUtil.getLong(request, "groupId");
		String treeId = ParamUtil.getString(request, "treeId");

		if (cmd.equals("get")) {
			return getLayoutsJSON(request, groupId, treeId);
		}
		else if (cmd.equals("getAll")) {
			return LayoutsTreeUtil.getLayoutsJSON(request, groupId, treeId);
		}
		else if (cmd.equals("getSiblingLayoutsJSON")) {
			return getSiblingLayoutsJSON(request, groupId);
		}

		return null;
	}

	protected String getLayoutsJSON(
			HttpServletRequest request, long groupId, String treeId)
		throws Exception {

		boolean privateLayout = ParamUtil.getBoolean(request, "privateLayout");
		long parentLayoutId = ParamUtil.getLong(request, "parentLayoutId");
		boolean incomplete = ParamUtil.getBoolean(request, "incomplete", true);

		return LayoutsTreeUtil.getLayoutsJSON(
			request, groupId, privateLayout, parentLayoutId, incomplete,
			treeId);
	}

	protected String getSiblingLayoutsJSON(
			HttpServletRequest request, long groupId)
		throws Exception {

		boolean privateLayout = ParamUtil.getBoolean(request, "privateLayout");
		long layoutId = ParamUtil.getLong(request, "layoutId");
		int max = ParamUtil.getInteger(request, "max");

		return LayoutsTreeUtil.getLayoutsJSON(
			request, groupId, privateLayout, layoutId, max);
	}

}