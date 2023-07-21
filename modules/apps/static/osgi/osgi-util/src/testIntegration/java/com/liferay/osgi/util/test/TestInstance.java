/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.util.test;

import com.liferay.osgi.util.service.Reference;

/**
 * @author Carlos Sierra Andrés
 */
public class TestInstance {

	public TrackedOne getTrackedOne() {
		return _trackedOne;
	}

	public TrackedTwo getTrackedTwo() {
		return _trackedTwo;
	}

	@Reference
	public void setTrackedOne(TrackedOne trackedOne) {
		_trackedOne = trackedOne;
	}

	@Reference
	public void setTrackedTwo(TrackedTwo trackedTwo) {
		_trackedTwo = trackedTwo;
	}

	private TrackedOne _trackedOne;
	private TrackedTwo _trackedTwo;

}