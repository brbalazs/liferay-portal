/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.constants;

/**
 * @author Edward Kwok-Yu Wong
 */
public final class ExperimentConstants {

	public static final double DEFAULT_MINIMUM_DETECTABLE_EFFECT_PERCENTAGE =
		.10;

	public static final double DEFAULT_POWER_LEVEL = .80;

	public static final int DISCARDED_SAMPLES = 2000;

	public static final long MINIMUM_EXPERIMENT_DURATION_IN_DAYS = 14;

	public static final int MINIMUM_TRAFFIC_SAMPLE_SIZE = 14;

	public static final int MONTE_CARLO_SAMPLE_SIZE = 100000;

}