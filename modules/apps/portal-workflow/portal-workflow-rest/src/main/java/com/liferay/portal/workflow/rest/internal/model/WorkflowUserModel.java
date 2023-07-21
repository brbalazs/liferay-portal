/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.rest.internal.model;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adam Brandizzi
 */
@XmlRootElement
public class WorkflowUserModel {

	public WorkflowUserModel() {
		_name = null;
		_portraitURL = null;
		_userId = 0;
	}

	public WorkflowUserModel(User user) throws PortalException {
		_name = user.getFullName();
		_portraitURL = UserConstants.getPortraitURL(
			PortalUtil.getPathImage(), user.isMale(), user.getPortraitId(),
			user.getUserUuid());
		_userId = user.getUserId();
	}

	@XmlElement
	public String getName() {
		return _name;
	}

	@XmlElement
	public String getPortraitURL() {
		return _portraitURL;
	}

	@XmlElement(name = "id")
	public long getUserId() {
		return _userId;
	}

	private final String _name;
	private final String _portraitURL;
	private final long _userId;

}