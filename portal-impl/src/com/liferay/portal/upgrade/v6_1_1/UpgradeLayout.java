/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_1_1;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Jorge Ferrer
 */
public class UpgradeLayout extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		updateSourcePrototypeLayoutUuid();
	}

	protected long getLayoutPrototypeGroupId(String layoutPrototypeUuid)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				"select groupId from Group_ where classPK = (select " +
					"layoutPrototypeId from LayoutPrototype where uuid_ = " +
						"?)")) {

			ps.setString(1, layoutPrototypeUuid);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					return rs.getLong("groupId");
				}
			}
		}

		return 0;
	}

	protected boolean isGroupPrivateLayout(
			long groupId, String sourcePrototypeLayoutUuid)
		throws Exception {

		try (PreparedStatement ps = connection.prepareStatement(
				"select count(*) from Layout where uuid_ = ? and groupId = ? " +
					"and privateLayout = ?")) {

			ps.setString(1, sourcePrototypeLayoutUuid);
			ps.setLong(2, groupId);
			ps.setBoolean(3, true);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int count = rs.getInt(1);

					if (count > 0) {
						return true;
					}

					return false;
				}
			}
		}

		return false;
	}

	protected void updateSourcePrototypeLayoutUuid() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			StringBundler sb = new StringBundler(4);

			sb.append("select plid, layoutPrototypeUuid, ");
			sb.append("sourcePrototypeLayoutUuid from Layout where ");
			sb.append("layoutPrototypeUuid != '' and ");
			sb.append("sourcePrototypeLayoutUuid != ''");

			try (PreparedStatement ps = connection.prepareStatement(
					sb.toString());
				ResultSet rs = ps.executeQuery()) {

				// Get pages with a sourcePrototypeLayoutUuid that have a page
				// template. If the layoutUuid points to a page template, remove
				// it. Otherwise, it points to a site template page, so leave
				// it.

				while (rs.next()) {
					String layoutPrototypeUuid = rs.getString(
						"layoutPrototypeUuid");

					long groupId = getLayoutPrototypeGroupId(
						layoutPrototypeUuid);

					if (groupId == 0) {
						continue;
					}

					String sourcePrototypeLayoutUuid = rs.getString(
						"sourcePrototypeLayoutUuid");

					if (isGroupPrivateLayout(
							groupId, sourcePrototypeLayoutUuid)) {

						long plid = rs.getLong("plid");

						runSQL(
							"update Layout set sourcePrototypeLayoutUuid = " +
								"null where plid = " + plid);
					}
				}
			}
		}
	}

}