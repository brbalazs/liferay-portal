/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQAccountEntry;

import org.springframework.stereotype.Repository;

/**
 * @author Marcos Martins
 */
@Repository
public interface BQAccountEntryRepository
	extends BigQueryRepository<BQAccountEntry, String>,
			CustomBQAccountEntryRepository {
}