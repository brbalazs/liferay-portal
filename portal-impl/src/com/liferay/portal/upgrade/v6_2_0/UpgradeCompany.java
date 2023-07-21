/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.petra.encryptor.Encryptor;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.Validator;

import java.security.Key;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Tomas Polesovsky
 */
public class UpgradeCompany extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		String keyAlgorithm = Encryptor.KEY_ALGORITHM;

		if (keyAlgorithm.equals("DES")) {
			return;
		}

		upgradeKey();
	}

	protected void upgradeKey() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps = connection.prepareStatement(
				"select companyId, key_ from Company");
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long companyId = rs.getLong("companyId");
				String key = rs.getString("key_");

				upgradeKey(companyId, key);
			}
		}
	}

	protected void upgradeKey(long companyId, String key) throws Exception {
		Key keyObj = null;

		if (Validator.isNotNull(key)) {
			keyObj = (Key)Base64.stringToObjectSilent(key);
		}

		if (keyObj != null) {
			String algorithm = keyObj.getAlgorithm();

			if (!algorithm.equals("DES")) {
				return;
			}
		}

		try (PreparedStatement ps = connection.prepareStatement(
				"update Company set key_ = ? where companyId = ?")) {

			ps.setString(1, Base64.objectToString(Encryptor.generateKey()));
			ps.setLong(2, companyId);

			ps.executeUpdate();
		}
	}

}