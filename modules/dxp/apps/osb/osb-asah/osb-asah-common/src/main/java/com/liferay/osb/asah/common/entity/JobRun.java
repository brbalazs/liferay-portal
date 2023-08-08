/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.JobRunStatus;

import java.time.LocalDateTime;

import java.util.Date;
import java.util.Objects;

import org.json.JSONObject;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class JobRun implements Persistable<Long> {

	public JobRun() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof JobRun)) {
			return false;
		}

		JobRun jobRun = (JobRun)obj;

		if (Objects.equals(_completedDate, jobRun._completedDate) &&
			Objects.equals(
				JSONUtil.toMap(_contextJSONObject),
				JSONUtil.toMap(jobRun._contextJSONObject)) &&
			Objects.equals(_createLocalDateTime, jobRun._createLocalDateTime) &&
			Objects.equals(_id, jobRun._id) &&
			Objects.equals(_jobRef, jobRun._jobRef) &&
			Objects.equals(_jobRunStatus, jobRun._jobRunStatus) &&
			Objects.equals(_trigger, jobRun._trigger)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCompletedDate() {
		if (_completedDate == null) {
			return null;
		}

		return new Date(_completedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("context")
	@JsonProperty("context")
	public JSONObject getContextJSONObject() {
		return _contextJSONObject;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("createdate")
	@JsonProperty("createdDate")
	@JsonSerialize(using = ToStringSerializer.class)
	public LocalDateTime getCreateLocalDateTime() {
		return _createLocalDateTime;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Long getJobId() {
		if (_jobRef == null) {
			return null;
		}

		return _jobRef.getId();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("status")
	@JsonProperty("status")
	public JobRunStatus getJobRunStatus() {
		return _jobRunStatus;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getJobType() {
		if (_jobRef == null) {
			return null;
		}

		return _jobRef.getType();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("modifieddate")
	@JsonProperty("lastUpdatedDate")
	@JsonSerialize(using = ToStringSerializer.class)
	public LocalDateTime getModifiedLocalDateTime() {
		return _modifiedLocalDateTime;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getStep() {
		return _step;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTrigger() {
		return _trigger;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_completedDate, _contextJSONObject, _createLocalDateTime, _id,
			_jobRef, _jobRunStatus, _trigger);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setCompletedDate(Date completedDate) {
		if (completedDate != null) {
			_completedDate = new Date(completedDate.getTime());
		}
	}

	public void setContextJSONObject(JSONObject contextJSONObject) {
		_contextJSONObject = contextJSONObject;
	}

	public void setCreateLocalDateTime(LocalDateTime createLocalDateTime) {
		_createLocalDateTime = createLocalDateTime;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setJobId(Long jobId) {
		if (jobId == null) {
			return;
		}

		if (_jobRef == null) {
			_jobRef = new JobRef();
		}

		_jobRef.setId(jobId);
	}

	public void setJobRunStatus(JobRunStatus jobRunStatus) {
		_jobRunStatus = jobRunStatus;
	}

	public void setJobType(String jobType) {
		if (jobType == null) {
			return;
		}

		if (_jobRef == null) {
			_jobRef = new JobRef();
		}

		_jobRef.setType(jobType);
	}

	public void setModifiedLocalDateTime(LocalDateTime modifiedLocalDateTime) {
		_modifiedLocalDateTime = modifiedLocalDateTime;
	}

	public void setStep(String step) {
		_step = step;
	}

	public void setTrigger(String trigger) {
		_trigger = trigger;
	}

	@JsonProperty("job")
	protected JobRef getJobRef() {
		return _jobRef;
	}

	protected void setJobRef(JobRef jobRef) {
		_jobRef = jobRef;
	}

	@Transient
	private Date _completedDate;

	@Transient
	private JSONObject _contextJSONObject;

	@Transient
	private LocalDateTime _createLocalDateTime;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private JobRef _jobRef;

	@Transient
	private JobRunStatus _jobRunStatus;

	@Transient
	private LocalDateTime _modifiedLocalDateTime;

	@Transient
	private String _step;

	@Transient
	private String _trigger;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class JobRef {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof JobRef)) {
				return false;
			}

			JobRef jobRef = (JobRef)obj;

			if (Objects.equals(_id, jobRef._id) &&
				Objects.equals(_type, jobRef._type)) {

				return true;
			}

			return false;
		}

		@JsonSerialize(using = ToStringSerializer.class)
		public Long getId() {
			return _id;
		}

		public String getType() {
			return _type;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_id, _type);
		}

		public void setId(Long id) {
			_id = id;
		}

		public void setType(String type) {
			_type = type;
		}

		private Long _id;
		private String _type;

	}

}