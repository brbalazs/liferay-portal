/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQDataSourceUser;

import java.util.List;

/**
 * @author Ivica Cardic
 */
public interface CustomBQDataSourceUserRepository {

	public List<BQDataSourceUser> findBQDataSourceUsersByUserEmailAddressHashed(
		String userEmailAddressHashed);

}