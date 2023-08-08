/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;

import java.time.LocalDateTime;

import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class Job implements Persistable<Long> {

	public Job() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Job)) {
			return false;
		}

		Job job = (Job)obj;

		if (equalsJob(job)) {
			Class<?> clazz = obj.getClass();

			if (!clazz.isInstance(this) && Job.class.isAssignableFrom(clazz)) {
				return obj.equals(this);
			}

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("osbAsahTaskId")
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getAsahTaskId() {
		return _asahTaskId;
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
	@JsonProperty("parameters")
	@MappedCollection(idColumn = "jobid")
	public Set<JobParameter> getJobParameters() {
		return _jobParameters;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("rundataperiod")
	@JsonProperty("runDataPeriod")
	public JobRunDataPeriod getJobRunDataPeriod() {
		return _jobRunDataPeriod;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("runfrequency")
	@JsonProperty("runFrequency")
	public JobRunFrequency getJobRunFrequency() {
		return _jobRunFrequency;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("type")
	@JsonProperty("type")
	public JobType getJobType() {
		return _jobType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("modifieddate")
	@JsonProperty("lastUpdatedDate")
	@JsonSerialize(using = ToStringSerializer.class)
	public LocalDateTime getModifiedLocalDateTime() {
		return _modifiedLocalDateTime;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_createLocalDateTime, _id, _jobParameters, _jobRunDataPeriod,
			_jobRunFrequency, _jobType, _modifiedLocalDateTime, _name);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAsahTaskId(Long asahTaskId) {
		_asahTaskId = asahTaskId;
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

	public void setJobParameters(Set<JobParameter> jobParameters) {
		_jobParameters = jobParameters;
	}

	public void setJobRunDataPeriod(JobRunDataPeriod jobRunDataPeriod) {
		_jobRunDataPeriod = jobRunDataPeriod;
	}

	public void setJobRunFrequency(JobRunFrequency jobRunFrequency) {
		_jobRunFrequency = jobRunFrequency;
	}

	public void setJobType(JobType jobType) {
		_jobType = jobType;
	}

	public void setModifiedLocalDateTime(LocalDateTime modifiedLocalDateTime) {
		_modifiedLocalDateTime = modifiedLocalDateTime;
	}

	public void setName(String name) {
		_name = name;
	}

	protected boolean equalsJob(Job job) {
		if (Objects.equals(_createLocalDateTime, job._createLocalDateTime) &&
			Objects.equals(_id, job._id) &&
			Objects.equals(_jobParameters, job._jobParameters) &&
			Objects.equals(_jobRunDataPeriod, job._jobRunDataPeriod) &&
			Objects.equals(_jobRunFrequency, job._jobRunFrequency) &&
			Objects.equals(_jobType, job._jobType) &&
			Objects.equals(
				_modifiedLocalDateTime, job._modifiedLocalDateTime) &&
			Objects.equals(_name, job._name)) {

			return true;
		}

		return false;
	}

	@Transient
	private Long _asahTaskId;

	@Transient
	private LocalDateTime _createLocalDateTime;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Set<JobParameter> _jobParameters;

	@Transient
	private JobRunDataPeriod _jobRunDataPeriod;

	@Transient
	private JobRunFrequency _jobRunFrequency;

	@Transient
	private JobType _jobType;

	@Transient
	private LocalDateTime _modifiedLocalDateTime;

	@Transient
	private String _name;

}