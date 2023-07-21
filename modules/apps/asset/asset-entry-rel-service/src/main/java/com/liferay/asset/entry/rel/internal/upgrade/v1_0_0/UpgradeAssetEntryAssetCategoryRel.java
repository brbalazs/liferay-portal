/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.entry.rel.internal.upgrade.v1_0_0;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Eudaldo Alonso
 */
public class UpgradeAssetEntryAssetCategoryRel extends UpgradeProcess {

	protected void addAssetEntryAssetCategoryRels() throws Exception {
		StringBundler sb = new StringBundler(3);

		sb.append("insert into AssetEntryAssetCategoryRel (");
		sb.append("assetEntryAssetCategoryRelId, assetEntryId, ");
		sb.append("assetCategoryId) values (?, ?, ?)");

		try (PreparedStatement ps1 = connection.prepareStatement(
				"select * from AssetEntries_AssetCategories");
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, sb.toString());
			ResultSet rs = ps1.executeQuery()) {

			while (rs.next()) {
				long assetEntryId = rs.getLong("entryId");
				long assetCategoryId = rs.getLong("categoryId");

				ps2.setLong(1, increment());
				ps2.setLong(2, assetEntryId);
				ps2.setLong(3, assetCategoryId);

				ps2.executeUpdate();
			}

			ps2.executeBatch();
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		upgradeSchema();

		addAssetEntryAssetCategoryRels();
	}

	protected void upgradeSchema() throws Exception {
		String template = StringUtil.read(
			UpgradeAssetEntryAssetCategoryRel.class.getResourceAsStream(
				"dependencies/update.sql"));

		runSQLTemplateString(template, false, false);
	}

}