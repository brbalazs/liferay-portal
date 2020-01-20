/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.internal.upgrade.v1_11_2;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Ethan Bustad
 */
public class CPDefinitionLinkUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement ps = connection.prepareStatement(
				_SELECT_CPRODUCT_SQL);
			Statement s = connection.createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet r1 = s.executeQuery(_SELECT_CPDEFINITIONLINK_SQL)) {

			while (r1.next()) {
				long cProductId = r1.getLong("CProductId");

				ps.setLong(1, cProductId);

				ResultSet r2 = ps.executeQuery();

				if (!r2.next()) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Removing CPDefinitionLink rows with CProductId " +
								cProductId);
					}

					runSQL(
						"DELETE FROM CPDefinitionLink WHERE CProductId = " +
							cProductId);
				}
			}
		}
	}

	private static final String _SELECT_CPDEFINITIONLINK_SQL =
		"SELECT DISTINCT CProductId FROM CPDefinitionLink";

	private static final String _SELECT_CPRODUCT_SQL =
		"SELECT CProductId FROM CProduct WHERE CProductId = ?";

	private static final Log _log = LogFactoryUtil.getLog(
		CPDefinitionLinkUpgradeProcess.class);

}