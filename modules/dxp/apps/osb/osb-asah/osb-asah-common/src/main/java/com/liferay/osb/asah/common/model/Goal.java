/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

/**
 * @author Marcellus Tavares
 */
public class Goal {

	public Goal() {
	}

	public Goal(GoalMetric goalMetric, String target) {
		_goalMetric = goalMetric;
		_target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Goal)) {
			return false;
		}

		Goal goal = (Goal)obj;

		if (Objects.equals(_goalMetric, goal._goalMetric) &&
			Objects.equals(_target, goal._target)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("metric")
	@JsonProperty("metric")
	@NotNull
	public GoalMetric getGoalMetric() {
		return _goalMetric;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTarget() {
		return _target;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_goalMetric, _target);
	}

	public void setGoalMetric(GoalMetric goalMetric) {
		_goalMetric = goalMetric;
	}

	public void setTarget(String target) {
		_target = target;
	}

	@Transient
	private GoalMetric _goalMetric;

	@Transient
	private String _target;

}