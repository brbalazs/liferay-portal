/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.rest.internal.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Adam Brandizzi
 */
@XmlRootElement
public class WorkflowTaskTransitionOperationModel {

	@XmlElement
	public String getComment() {
		return _comment;
	}

	@XmlElement
	public String getTransition() {
		return _transition;
	}

	public void setComment(String comment) {
		_comment = comment;
	}

	public void setTransition(String transition) {
		_transition = transition;
	}

	private String _comment;
	private String _transition;

}