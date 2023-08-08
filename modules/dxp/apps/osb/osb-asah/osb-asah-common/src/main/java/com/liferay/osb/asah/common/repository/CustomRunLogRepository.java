/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.RunLog;

import java.util.Optional;

import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomRunLogRepository {

	public Optional<RunLog>
		findByDataSourceIdAndNaniteClassNameAndStatusOrderByDateLoggedDesc(
			@Nullable Long dataSourceId, String naniteClassName,
			@Nullable String status);

}