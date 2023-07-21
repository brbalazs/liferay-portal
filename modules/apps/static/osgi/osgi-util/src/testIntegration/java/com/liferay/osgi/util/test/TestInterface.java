/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.util.test;

import com.liferay.osgi.util.service.Reference;

/**
 * @author Carlos Sierra Andrés
 */
public class TestInterface {

	public InterfaceOne getTrackedOne() {
		return _trackedOne;
	}

	public InterfaceTwo getTrackedTwo() {
		return _trackedTwo;
	}

	@Reference
	public void setTrackedOne(InterfaceOne trackedOne) {
		_trackedOne = trackedOne;
	}

	@Reference
	public void setTrackedTwo(InterfaceTwo trackedTwo) {
		_trackedTwo = trackedTwo;
	}

	private InterfaceOne _trackedOne;
	private InterfaceTwo _trackedTwo;

}