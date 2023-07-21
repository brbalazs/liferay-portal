/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_1;

import com.liferay.petra.encryptor.Encryptor;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;

import java.security.Key;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Mika Koivisto
 */
public class UpgradeCompany extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		updateCompanyKey();
	}

	protected void updateCompanyKey() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps = connection.prepareStatement(
				"select companyId, key_ from Company");
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String companyId = rs.getString("companyId");

				String keyString = rs.getString("key_");

				Key key = (Key)Base64.stringToObject(keyString);

				keyString = Encryptor.serializeKey(key);

				runSQL(
					StringBundler.concat(
						"update Company set key_ = '", keyString,
						"' where companyId = ", companyId));
			}
		}
	}

}