/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.internal.upgrade.v15_0_0;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Marcos Martins
 */
public class UpgradeFaroProject extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement ps = connection.prepareStatement(
				"select faroProjectId, createTime, subscription from " +
					"OSBFaro_FaroProject")) {

			try (Statement st = connection.createStatement();
				ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					JSONObject subscriptionJSONObject =
						JSONFactoryUtil.createJSONObject(
							rs.getString("subscription"));

					long startDate = subscriptionJSONObject.getLong(
						"startDate", 0);

					if (startDate != 0) {
						continue;
					}

					subscriptionJSONObject.put(
						"startDate", rs.getLong("createTime"));

					st.addBatch(
						String.format(
							"update OSBFaro_FaroProject set subscription = '" +
								"%s' where faroProjectId = %s",
							subscriptionJSONObject.toString(),
							rs.getLong("faroProjectId")));
				}

				st.executeBatch();
			}
		}
	}

}