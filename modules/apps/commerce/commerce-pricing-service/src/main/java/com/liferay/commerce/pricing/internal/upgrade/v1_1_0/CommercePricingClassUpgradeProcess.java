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

package com.liferay.commerce.pricing.internal.upgrade.v1_1_0;

import com.liferay.commerce.pricing.model.impl.CommercePricingClassImpl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Riccardo Alberti
 */
public class CommercePricingClassUpgradeProcess extends UpgradeProcess {

	protected void changeColumnType(
			Class<?> tableClass, String tableName, String columnName,
			String newColumnType)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Changing column %s to type %s for table %s", columnName,
					newColumnType, tableName));
		}

		if (hasColumn(tableName, columnName)) {
			alter(tableClass, new AlterColumnType(columnName, newColumnType));
		}
		else {
			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"No column %s exists on table %s", columnName,
						tableName));
			}
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		changeColumnType(
			CommercePricingClassImpl.class, CommercePricingClassImpl.TABLE_NAME,
			"title", "TEXT");

		changeColumnType(
			CommercePricingClassImpl.class, CommercePricingClassImpl.TABLE_NAME,
			"description", "TEXT");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePricingClassUpgradeProcess.class);

}