/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.verify.model;

import com.liferay.portal.kernel.verify.model.VerifiableUUIDModel;

/**
 * @author Miguel Pastor
 */
public class PhoneVerifiableModel implements VerifiableUUIDModel {

	@Override
	public String getPrimaryKeyColumnName() {
		return "phoneId";
	}

	@Override
	public String getTableName() {
		return "Phone";
	}

}