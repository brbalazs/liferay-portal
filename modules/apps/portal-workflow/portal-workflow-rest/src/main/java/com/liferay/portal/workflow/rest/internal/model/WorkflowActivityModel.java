/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.rest.internal.model;

import com.liferay.portal.kernel.exception.PortalException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adam Brandizzi
 */
@XmlRootElement
public class WorkflowActivityModel {

	public WorkflowActivityModel() {
		_createTime = 0;
		_message = null;
		_details = null;
	}

	public WorkflowActivityModel(
			long createTime, String message, String details)
		throws PortalException {

		_createTime = createTime;
		_message = message;
		_details = details;
	}

	@XmlElement
	public long getCreateTime() {
		return _createTime;
	}

	@XmlElement
	public String getDetails() {
		return _details;
	}

	@XmlElement
	public String getMessage() {
		return _message;
	}

	private final long _createTime;
	private final String _details;
	private final String _message;

}