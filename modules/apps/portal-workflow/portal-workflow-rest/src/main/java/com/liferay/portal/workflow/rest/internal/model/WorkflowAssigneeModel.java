/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.rest.internal.model;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adam Brandizzi
 */
@XmlRootElement
public class WorkflowAssigneeModel {

	public static final String ASSIGNEE_ROLE = "role";

	public static final String ASSIGNEE_USER = "user";

	public WorkflowAssigneeModel() {
		_portraitURL = null;
		_roleId = null;
		_roleName = null;
		_userId = 0L;
		_userName = null;
		_type = null;
	}

	public WorkflowAssigneeModel(Role role) throws PortalException {
		_portraitURL = null;
		_roleId = role.getRoleId();
		_roleName = role.getName();
		_userId = null;
		_userName = null;
		_type = ASSIGNEE_ROLE;
	}

	public WorkflowAssigneeModel(User user) throws PortalException {
		_portraitURL = UserConstants.getPortraitURL(
			PortalUtil.getPathImage(), user.isMale(), user.getPortraitId(),
			user.getUserUuid());
		_roleId = null;
		_roleName = null;
		_userId = user.getUserId();
		_userName = user.getFullName();
		_type = ASSIGNEE_USER;
	}

	@XmlElement
	public String getPortraitURL() {
		return _portraitURL;
	}

	@XmlElement
	public Long getRoleId() {
		return _roleId;
	}

	@XmlElement
	public String getRoleName() {
		return _roleName;
	}

	@XmlElement
	public String getType() {
		return _type;
	}

	@XmlElement
	public Long getUserId() {
		return _userId;
	}

	@XmlElement
	public String getUserName() {
		return _userName;
	}

	private final String _portraitURL;
	private final Long _roleId;
	private final String _roleName;
	private final String _type;
	private final Long _userId;
	private final String _userName;

}