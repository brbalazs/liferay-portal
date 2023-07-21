/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.ui.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Eduardo Lundgren
 */
public class SessionTreeJSClicks {

	public static void closeLayoutNodes(
		HttpServletRequest request, String treeId, boolean privateLayout,
		long layoutId, boolean recursive) {

		try {
			List<String> layoutIds = new ArrayList<>();

			layoutIds.add(String.valueOf(layoutId));

			if (recursive) {
				getLayoutIds(request, privateLayout, layoutId, layoutIds);
			}

			closeNodes(request, treeId, layoutIds.toArray(new String[0]));
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	public static void closeNode(
		HttpServletRequest request, String treeId, String nodeId) {

		while (true) {
			try {
				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(request);

				String openNodesString = portalPreferences.getValue(
					SessionTreeJSClicks.class.getName(), treeId);

				openNodesString = StringUtil.removeFromList(
					openNodesString, nodeId);

				portalPreferences.setValue(
					SessionTreeJSClicks.class.getName(), treeId,
					openNodesString);

				return;
			}
			catch (ConcurrentModificationException cme) {
			}
			catch (Exception e) {
				_log.error(e, e);

				return;
			}
		}
	}

	public static void closeNodes(HttpServletRequest request, String treeId) {
		while (true) {
			try {
				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(request);

				portalPreferences.setValue(
					SessionTreeJSClicks.class.getName(), treeId,
					StringPool.BLANK);

				return;
			}
			catch (ConcurrentModificationException cme) {
			}
			catch (Exception e) {
				_log.error(e, e);

				return;
			}
		}
	}

	public static void closeNodes(
		HttpServletRequest request, String treeId, String[] nodeIds) {

		while (true) {
			try {
				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(request);

				String openNodesString = portalPreferences.getValue(
					SessionTreeJSClicks.class.getName(), treeId);

				for (String nodeId : nodeIds) {
					openNodesString = StringUtil.removeFromList(
						openNodesString, nodeId);
				}

				portalPreferences.setValue(
					SessionTreeJSClicks.class.getName(), treeId,
					openNodesString);

				return;
			}
			catch (ConcurrentModificationException cme) {
			}
			catch (Exception e) {
				_log.error(e, e);

				return;
			}
		}
	}

	public static String getOpenNodes(
		HttpServletRequest request, String treeId) {

		try {
			PortalPreferences portalPreferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(request);

			return portalPreferences.getValue(
				SessionTreeJSClicks.class.getName(), treeId);
		}
		catch (Exception e) {
			_log.error(e, e);

			return null;
		}
	}

	public static void openLayoutNodes(
		HttpServletRequest request, String treeId, boolean privateLayout,
		long layoutId, boolean recursive) {

		try {
			List<String> layoutIds = new ArrayList<>();

			layoutIds.add(String.valueOf(layoutId));

			if (recursive) {
				getLayoutIds(request, privateLayout, layoutId, layoutIds);
			}

			openNodes(request, treeId, layoutIds.toArray(new String[0]));
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	public static void openNode(
		HttpServletRequest request, String treeId, String nodeId) {

		while (true) {
			try {
				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(request);

				String openNodesString = portalPreferences.getValue(
					SessionTreeJSClicks.class.getName(), treeId);

				openNodesString = StringUtil.add(openNodesString, nodeId);

				portalPreferences.setValue(
					SessionTreeJSClicks.class.getName(), treeId,
					openNodesString);

				return;
			}
			catch (ConcurrentModificationException cme) {
			}
			catch (Exception e) {
				_log.error(e, e);

				return;
			}
		}
	}

	public static void openNodes(
		HttpServletRequest request, String treeId, String[] nodeIds) {

		while (true) {
			try {
				PortalPreferences portalPreferences =
					PortletPreferencesFactoryUtil.getPortalPreferences(request);

				String openNodesString = portalPreferences.getValue(
					SessionTreeJSClicks.class.getName(), treeId);

				for (String nodeId : nodeIds) {
					openNodesString = StringUtil.add(openNodesString, nodeId);
				}

				portalPreferences.setValue(
					SessionTreeJSClicks.class.getName(), treeId,
					openNodesString);

				return;
			}
			catch (ConcurrentModificationException cme) {
			}
			catch (Exception e) {
				_log.error(e, e);

				return;
			}
		}
	}

	protected static List<String> getLayoutIds(
			HttpServletRequest request, boolean privateLayout,
			long parentLayoutId, List<String> layoutIds)
		throws Exception {

		long groupId = ParamUtil.getLong(request, "groupId");

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, privateLayout, parentLayoutId);

		for (Layout layout : layouts) {
			layoutIds.add(String.valueOf(layout.getLayoutId()));

			getLayoutIds(
				request, privateLayout, layout.getLayoutId(), layoutIds);
		}

		return layoutIds;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SessionTreeJSClicks.class);

}