/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.lock.bundle.locklistenerregistryutil;

import com.liferay.portal.kernel.lock.LockListener;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = LockListener.class
)
public class TestLockListener implements LockListener {

	@Override
	public String getClassName() {
		return TestLockListener.class.getName();
	}

	@Override
	public void onAfterExpire(String key) {
	}

	@Override
	public void onAfterRefresh(String key) {
	}

	@Override
	public void onBeforeExpire(String key) {
	}

	@Override
	public void onBeforeRefresh(String key) {
	}

}