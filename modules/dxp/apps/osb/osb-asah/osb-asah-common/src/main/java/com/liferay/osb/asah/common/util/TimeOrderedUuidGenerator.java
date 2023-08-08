/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.security.SecureRandom;

/**
 * Provides a simple way to generate unique ordered IDs across distributed
 * shards (or even multiple systems). It's based on Simpleflake's ID generation
 * and requires no coordination with server IPs, Mac addresses, or database
 * content.
 *
 * <p>
 * This implementation follows a pattern that a 64-bits long is prefixed with a
 * millisecond timestamp, but the remaining bits are completely random. For
 * example,
 * </p>
 *
 * <p>
 * <pre>
 * id = 0000000000000000000000000000000000000000000   0   00000000000000000000
 *     |-------------------------------------------| |-| |--------------------|
 *                    timestamp                    sequence      random
 *                      bits                      safety bit      bits
 * </pre>
 * </p>
 *
 * @author Eduardo Lundgren
 */
public class TimeOrderedUuidGenerator {

	/**
	 * Gets start count timestamp value.
	 */
	public TimeOrderedUuidGenerator() {
		this(_TIMESTAMP_MIN);
	}

	/**
	 * Instantiates a new time ordered unique generator.
	 *
	 * @param startTimestamp the start timestamp
	 */
	public TimeOrderedUuidGenerator(long startTimestamp) {
		if (startTimestamp < 0) {
			throw new IllegalArgumentException(
				"Start timestamp is less than 0");
		}

		_startTimestamp = startTimestamp;
	}

	/**
	 * Generates a unique ordered ID across distributed shards (or even multiple
	 * systems).
	 *
	 * @return the unique ordered ID
	 */
	public synchronized String generateId() {
		return String.valueOf(generateIdAsLong());
	}

	public synchronized long generateIdAsLong() {
		long timestamp = System.currentTimeMillis();

		// Prevent the clock from moving backwards

		while (timestamp < _lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}

		if ((timestamp < _startTimestamp) || (timestamp > _TIMESTAMP_MAX)) {
			throw new RuntimeException(
				"Refusing to generate ID because system clock is invalid");
		}

		if (timestamp != _lastTimestamp) {
			_lastRandom = _secureRandom.nextInt(_RANDOM_MAX);
			_lastTimestamp = timestamp;
		}
		else {
			_lastRandom++;
		}

		long uuid = timestamp - _startTimestamp;

		uuid <<= _SEQUENCE_SAFETY_BITS;
		uuid <<= _RANDOM_BITS;
		uuid |= _lastRandom;

		return uuid;
	}

	private static final int _RANDOM_BITS = 20;

	private static final int _RANDOM_MAX = -1 ^ (-1 << _RANDOM_BITS);

	private static final int _SEQUENCE_SAFETY_BITS = 1;

	private static final int _TIMESTAMP_BITS = 43;

	private static final long _TIMESTAMP_MAX = -1L ^ (-1L << _TIMESTAMP_BITS);

	private static final long _TIMESTAMP_MIN = 1388502000000L;

	private static int _lastRandom;
	private static long _lastTimestamp = -1L;
	private static final SecureRandom _secureRandom = new SecureRandom();

	private final long _startTimestamp;

}