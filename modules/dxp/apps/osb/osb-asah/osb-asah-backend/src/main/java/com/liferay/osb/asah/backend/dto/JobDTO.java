/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcellus Tavares
 */
@GraphQLType("Job")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDTO {

	public JobDTO(Job job) {
		_job = job;
	}

	public String getId() {
		return String.valueOf(_job.getId());
	}

	@GraphQLProperty("parameters")
	public List<JobParameter> getJobParameters() {
		return new ArrayList<>(_job.getJobParameters());
	}

	@GraphQLProperty("runDataPeriod")
	public JobRunDataPeriod getJobRunDataPeriod() {
		return _job.getJobRunDataPeriod();
	}

	@GraphQLProperty("runFrequency")
	public JobRunFrequency getJobRunFrequency() {
		return _job.getJobRunFrequency();
	}

	@GraphQLProperty("type")
	public JobType getJobType() {
		return _job.getJobType();
	}

	public String getName() {
		return _job.getName();
	}

	private final Job _job;

}