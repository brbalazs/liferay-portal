/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.internal.upgrade.v11_0_0;

import com.liferay.osb.faro.constants.FaroNotificationConstants;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Geyson Silva
 */
public class UpgradeFaroNotification extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQL(
			StringBundler.concat(
				"create table OSBFaro_FaroNotification (faroNotificationId ",
				"LONG not null primary key, groupId LONG, userId LONG, ",
				"createTime LONG, modifiedTime LONG, scope VARCHAR(75) null, ",
				"read_ BOOLEAN, type_ VARCHAR(75) null, subtype VARCHAR(75) ",
				"null)"));

		_notifyFaroProjects();
	}

	private void _addFaroNotification(long groupId, long userId)
		throws SQLException {

		String sql = StringBundler.concat(
			"insert into OSBFaro_FaroNotification (faroNotificationId, ",
			"groupId, userId, createTime, modifiedTime, scope, read_, type_, ",
			"subtype) values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			long now = System.currentTimeMillis();

			ps.setLong(1, increment());
			ps.setLong(2, groupId);
			ps.setLong(3, userId);
			ps.setLong(4, now);
			ps.setLong(5, now);
			ps.setString(6, FaroNotificationConstants.SCOPE_WORKSPACE);
			ps.setBoolean(7, false);
			ps.setString(8, FaroNotificationConstants.TYPE_MODAL);
			ps.setString(9, FaroNotificationConstants.SUBTYPE_TIME_ZONE_ADMIN);

			ps.executeUpdate();
		}
	}

	private void _notifyFaroProjects() throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(
				"select groupId, userId from OSBFaro_FaroProject")) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				_addFaroNotification(rs.getLong(1), rs.getLong(2));
			}
		}
	}

}