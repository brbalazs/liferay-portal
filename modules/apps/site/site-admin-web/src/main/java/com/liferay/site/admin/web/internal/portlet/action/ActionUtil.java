/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.site.admin.web.internal.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.WorkflowDefinitionLink;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupServiceUtil;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.sites.kernel.util.SitesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ResourceRequest;

/**
 * @author Jürgen Kappler
 */
public class ActionUtil {

	public static List<Group> getGroups(ResourceRequest request)
		throws Exception {

		long[] groupIds = ParamUtil.getLongValues(request, "rowIds");

		List<Group> groups = new ArrayList<>();

		for (long groupId : groupIds) {
			Group group = GroupServiceUtil.getGroup(groupId);

			groups.add(group);
		}

		return groups;
	}

	public static void updateLayoutSetPrototypesLinks(
			ActionRequest actionRequest, Group liveGroup)
		throws Exception {

		long privateLayoutSetPrototypeId = ParamUtil.getLong(
			actionRequest, "privateLayoutSetPrototypeId");
		long publicLayoutSetPrototypeId = ParamUtil.getLong(
			actionRequest, "publicLayoutSetPrototypeId");

		LayoutSet privateLayoutSet = liveGroup.getPrivateLayoutSet();

		boolean privateLayoutSetPrototypeLinkEnabled = ParamUtil.getBoolean(
			actionRequest, "privateLayoutSetPrototypeLinkEnabled",
			privateLayoutSet.isLayoutSetPrototypeLinkEnabled());

		LayoutSet publicLayoutSet = liveGroup.getPublicLayoutSet();

		boolean publicLayoutSetPrototypeLinkEnabled = ParamUtil.getBoolean(
			actionRequest, "publicLayoutSetPrototypeLinkEnabled",
			publicLayoutSet.isLayoutSetPrototypeLinkEnabled());

		if ((privateLayoutSetPrototypeId == 0) &&
			(publicLayoutSetPrototypeId == 0) &&
			!privateLayoutSetPrototypeLinkEnabled &&
			!publicLayoutSetPrototypeLinkEnabled) {

			long layoutSetPrototypeId = ParamUtil.getLong(
				actionRequest, "layoutSetPrototypeId");
			int layoutSetVisibility = ParamUtil.getInteger(
				actionRequest, "layoutSetVisibility");
			boolean layoutSetPrototypeLinkEnabled = ParamUtil.getBoolean(
				actionRequest, "layoutSetPrototypeLinkEnabled",
				layoutSetPrototypeId > 0);
			boolean layoutSetVisibilityPrivate = ParamUtil.getBoolean(
				actionRequest, "layoutSetVisibilityPrivate");

			if ((layoutSetVisibility == _LAYOUT_SET_VISIBILITY_PRIVATE) ||
				layoutSetVisibilityPrivate) {

				privateLayoutSetPrototypeId = layoutSetPrototypeId;

				privateLayoutSetPrototypeLinkEnabled =
					layoutSetPrototypeLinkEnabled;
			}
			else {
				publicLayoutSetPrototypeId = layoutSetPrototypeId;

				publicLayoutSetPrototypeLinkEnabled =
					layoutSetPrototypeLinkEnabled;
			}
		}

		if (!liveGroup.isStaged() || liveGroup.isStagedRemotely()) {
			SitesUtil.updateLayoutSetPrototypesLinks(
				liveGroup, publicLayoutSetPrototypeId,
				privateLayoutSetPrototypeId,
				publicLayoutSetPrototypeLinkEnabled,
				privateLayoutSetPrototypeLinkEnabled);
		}
		else {
			SitesUtil.updateLayoutSetPrototypesLinks(
				liveGroup.getStagingGroup(), publicLayoutSetPrototypeId,
				privateLayoutSetPrototypeId,
				publicLayoutSetPrototypeLinkEnabled,
				privateLayoutSetPrototypeLinkEnabled);
		}
	}

	public static void updateWorkflowDefinitionLinks(
			ActionRequest actionRequest, Group liveGroup)
		throws PortalException {

		long groupId = liveGroup.getGroupId();
		long companyId = liveGroup.getCompanyId();
		long userId = liveGroup.getCreatorUserId();

		long layoutSetPrototypeId = ParamUtil.getLong(
			actionRequest, "layoutSetPrototypeId");

		Group layoutSetPrototypeGroup =
			GroupLocalServiceUtil.getLayoutSetPrototypeGroup(
				companyId, layoutSetPrototypeId);

		List<WorkflowDefinitionLink> workflowDefinitionLinks =
			WorkflowDefinitionLinkLocalServiceUtil.getWorkflowDefinitionLinks(
				companyId, layoutSetPrototypeGroup.getGroupId(), 0);

		for (WorkflowDefinitionLink workflowDefinitionLink :
				workflowDefinitionLinks) {

			WorkflowDefinitionLinkLocalServiceUtil.addWorkflowDefinitionLink(
				userId, companyId, groupId,
				workflowDefinitionLink.getClassName(),
				workflowDefinitionLink.getClassPK(),
				workflowDefinitionLink.getTypePK(),
				workflowDefinitionLink.getWorkflowDefinitionName(),
				workflowDefinitionLink.getWorkflowDefinitionVersion());
		}
	}

	private static final int _LAYOUT_SET_VISIBILITY_PRIVATE = 1;

}