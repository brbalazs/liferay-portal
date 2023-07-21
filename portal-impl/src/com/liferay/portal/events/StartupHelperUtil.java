/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.events;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.verify.VerifyException;

import java.sql.Connection;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 * @author Raymond Augé
 */
public class StartupHelperUtil {

	public static StartupHelper getStartupHelper() {
		return _startupHelper;
	}

	public static boolean isDBNew() {
		return getStartupHelper().isDBNew();
	}

	public static boolean isStartupFinished() {
		return getStartupHelper().isStartupFinished();
	}

	public static boolean isUpgraded() {
		return getStartupHelper().isUpgraded();
	}

	public static boolean isUpgrading() {
		return getStartupHelper().isUpgrading();
	}

	public static boolean isVerified() {
		return getStartupHelper().isVerified();
	}

	public static void printPatchLevel() {
		getStartupHelper().printPatchLevel();
	}

	public static void setDbNew(boolean dbNew) {
		getStartupHelper().setDbNew(dbNew);
	}

	public static void setDropIndexes(boolean dropIndexes) {
		getStartupHelper().setDropIndexes(dropIndexes);
	}

	public static void setStartupFinished(boolean startupFinished) {
		getStartupHelper().setStartupFinished(startupFinished);
	}

	public static void updateIndexes() {
		getStartupHelper().updateIndexes();
	}

	public static void updateIndexes(boolean dropIndexes) {
		getStartupHelper().updateIndexes(dropIndexes);
	}

	public static void updateIndexes(
		DB db, Connection connection, boolean dropIndexes) {

		getStartupHelper().updateIndexes(db, connection, dropIndexes);
	}

	public static void upgradeProcess(int buildNumber) throws UpgradeException {
		getStartupHelper().upgradeProcess(buildNumber);
	}

	public static void verifyProcess(boolean newBuildNumber, boolean verified)
		throws VerifyException {

		getStartupHelper().verifyProcess(newBuildNumber, verified);
	}

	public void setStartupHelper(StartupHelper startupHelper) {
		_startupHelper = startupHelper;
	}

	private static StartupHelper _startupHelper;

}