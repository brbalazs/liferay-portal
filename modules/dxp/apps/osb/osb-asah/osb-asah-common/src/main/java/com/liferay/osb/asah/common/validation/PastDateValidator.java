/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.validation;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Marcellus Tavares
 */
public class PastDateValidator implements ConstraintValidator<PastDate, Date> {

	@Override
	public void initialize(PastDate pastDate) {
		_allowedLateness = pastDate.allowedLateness();
		_clock = Clock.systemUTC();
	}

	@Override
	public boolean isValid(
		Date date, ConstraintValidatorContext constraintValidatorContext) {

		if (date == null) {
			return false;
		}

		Instant instant = date.toInstant();

		Instant clockInstant = _clock.instant();

		int result = instant.compareTo(clockInstant);

		if (result >= 0) {
			return false;
		}

		result = instant.compareTo(
			clockInstant.minus(_allowedLateness, ChronoUnit.MILLIS));

		if (result >= 0) {
			return true;
		}

		return false;
	}

	private long _allowedLateness;
	private Clock _clock;

}