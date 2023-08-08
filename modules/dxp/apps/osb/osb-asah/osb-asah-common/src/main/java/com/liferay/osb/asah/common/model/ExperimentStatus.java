/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Marcellus Tavares
 * @author André Miranda
 */
public enum ExperimentStatus {

	COMPLETED(true), DRAFT(true), FINISHED_NO_WINNER(false),
	FINISHED_WINNER(false), PAUSED(true), RUNNING(false), SCHEDULED(true),
	TERMINATED(true);

	public static boolean isValidTransition(
		ExperimentStatus fromExperimentStatus,
		ExperimentStatus toExperimentStatus) {

		if ((fromExperimentStatus == null) || (toExperimentStatus == null)) {
			return false;
		}

		if (Objects.equals(fromExperimentStatus, toExperimentStatus)) {
			return true;
		}

		Set<ExperimentStatus> possibleStatusesSet =
			_validTransitions.getOrDefault(
				fromExperimentStatus, Collections.emptySet());

		return possibleStatusesSet.contains(toExperimentStatus);
	}

	public boolean isDeleteAllowed() {
		return _deleteAllowed;
	}

	private ExperimentStatus(boolean deleteAllowed) {
		_deleteAllowed = deleteAllowed;
	}

	private static final Map<ExperimentStatus, Set<ExperimentStatus>>
		_validTransitions =
			new HashMap<ExperimentStatus, Set<ExperimentStatus>>() {
				{
					put(COMPLETED, Collections.emptySet());
					put(DRAFT, SetUtil.of(RUNNING, SCHEDULED));
					put(FINISHED_NO_WINNER, SetUtil.of(COMPLETED));
					put(FINISHED_WINNER, SetUtil.of(COMPLETED));
					put(PAUSED, SetUtil.of(RUNNING));
					put(
						RUNNING,
						SetUtil.of(
							FINISHED_NO_WINNER, FINISHED_WINNER, PAUSED,
							TERMINATED));
					put(SCHEDULED, SetUtil.of(RUNNING));
					put(TERMINATED, Collections.emptySet());
				}
			};

	private final boolean _deleteAllowed;

}