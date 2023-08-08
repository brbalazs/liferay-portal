/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.util.StringUtil;

/**
 * @author Marcos Martins
 */
@GraphQLType("Goal")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalDTO {

	public GoalDTO() {
	}

	public GoalDTO(Goal goal) {
		_goalMetric = StringUtil.get(goal.getGoalMetric(), null);
		_target = goal.getTarget();
	}

	@GraphQLProperty("metric")
	@JsonProperty("metric")
	public String getGoalMetric() {
		return _goalMetric;
	}

	public String getTarget() {
		return _target;
	}

	public void setGoalMetric(String goalMetric) {
		_goalMetric = goalMetric;
	}

	public void setTarget(String target) {
		_target = target;
	}

	private String _goalMetric;
	private String _target;

}