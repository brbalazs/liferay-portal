/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;

/**
 * @author Inácio Nery
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("data-export-tasks")
public class DataExportTaskDTO {

	public DataExportTaskDTO(DataExportTask dataExportTask) {
		_completedDate = dataExportTask.getCompletedDate();
		_createDate = dataExportTask.getCreateDate();
		_fromDate = dataExportTask.getFromDate();
		_id = StringUtil.get(dataExportTask.getId(), null);
		_startedDate = dataExportTask.getStartedDate();
		_status = StringUtil.get(dataExportTask.getStatus(), null);
		_toDate = dataExportTask.getToDate();
		_type = StringUtil.get(dataExportTask.getType(), null);
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("completedDate")
	public Date getCompletedDate() {
		if (_completedDate == null) {
			return null;
		}

		return new Date(_completedDate.getTime());
	}

	@JsonAlias("createDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createdDate")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("fromDate")
	public Date getFromDate() {
		if (_fromDate == null) {
			return null;
		}

		return new Date(_fromDate.getTime());
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonProperty("previousStatus")
	public String getPreviousStatus() {
		return _previousStatus;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("startedDate")
	public Date getStartedDate() {
		if (_startedDate == null) {
			return null;
		}

		return new Date(_startedDate.getTime());
	}

	@JsonProperty("status")
	public String getStatus() {
		return _status;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("toDate")
	public Date getToDate() {
		if (_toDate == null) {
			return null;
		}

		return new Date(_toDate.getTime());
	}

	@JsonProperty("type")
	public String getType() {
		return _type;
	}

	public void setCompletedDate(Date completedDate) {
		if (completedDate != null) {
			_completedDate = new Date(completedDate.getTime());
		}
	}

	public void setCreateDate(Date createdDate) {
		if (createdDate != null) {
			_createDate = new Date(createdDate.getTime());
		}
	}

	public void setFromDate(Date fromDate) {
		if (fromDate != null) {
			_fromDate = new Date(fromDate.getTime());
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public void setPreviousStatus(String previousStatus) {
		_previousStatus = previousStatus;
	}

	public void setStartedDate(Date startedDate) {
		if (startedDate != null) {
			_startedDate = new Date(startedDate.getTime());
		}
	}

	public void setStatus(String status) {
		_status = status;
	}

	public void setToDate(Date toDate) {
		if (toDate != null) {
			_toDate = new Date(toDate.getTime());
		}
	}

	public void setType(String type) {
		_type = type;
	}

	private Date _completedDate;
	private Date _createDate;
	private Date _fromDate;
	private String _id;
	private String _previousStatus;
	private Date _startedDate;
	private String _status;
	private Date _toDate;
	private String _type;

}