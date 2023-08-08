/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQExpandoColumn;

import java.util.Optional;

/**
 * @author Marcellus Tavares
 */
public interface CustomBQExpandoColumnRepository {

	public long count();

	public void deleteById(String id);

	public Optional<BQExpandoColumn> findByColumnIdAndDataSourceId(
		String columnId, Long dataSourceId);

	public Optional<BQExpandoColumn> findById(String id);

	public BQExpandoColumn insert(BQExpandoColumn bqExpandoColumn);

}