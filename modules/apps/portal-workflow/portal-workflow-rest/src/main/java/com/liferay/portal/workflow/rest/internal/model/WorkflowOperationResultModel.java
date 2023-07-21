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
public class WorkflowOperationResultModel {

	public static final String STATUS_ERROR = "error";

	public static final String STATUS_SUCCESS = "success";

	public WorkflowOperationResultModel() {
		_status = null;
		_message = null;
		_workflowTaskModel = null;
	}

	public WorkflowOperationResultModel(String status) {
		this(status, null, null);
	}

	public WorkflowOperationResultModel(String status, String message) {
		this(status, message, null);
	}

	public WorkflowOperationResultModel(
		String status, String message, WorkflowTaskModel workflowTaskModel) {

		_status = status;
		_message = message;
		_workflowTaskModel = workflowTaskModel;
	}

	public WorkflowOperationResultModel(
		String status, WorkflowTaskModel workflowTaskModel) {

		this(status, null, workflowTaskModel);
	}

	@XmlElement
	public String getMessage() {
		return _message;
	}

	@XmlElement
	public String getStatus() {
		return _status;
	}

	@XmlElement(name = "task")
	public WorkflowTaskModel getWorkflowTaskModel() {
		return _workflowTaskModel;
	}

	private final String _message;
	private final String _status;
	private final WorkflowTaskModel _workflowTaskModel;

}