/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.JobRun;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.JobRunStatus;

import java.util.Date;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
@GraphQLType("JobRun")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobRunDTO {

	public JobRunDTO(JobRun jobRun) {
		_jobRun = jobRun;
	}

	@GraphQLProperty("completedDate")
	public String getCompletedDateISO() {
		Date completedDate = _jobRun.getCompletedDate();

		if (completedDate == null) {
			return null;
		}

		return DateUtil.toUTCString(completedDate);
	}

	public Map<String, Object> getContext() {
		return JSONUtil.toMap(_jobRun.getContextJSONObject());
	}

	public String getId() {
		return String.valueOf(_jobRun.getId());
	}

	@GraphQLProperty("status")
	public JobRunStatus getJobRunStatus() {
		return _jobRun.getJobRunStatus();
	}

	private final JobRun _jobRun;

}