/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.RunLog;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcellus Tavares
 */
public interface RunLogRepository
	extends CustomRunLogRepository, PagingAndSortingRepository<RunLog, Long> {

	@Modifying
	public void deleteByDataSourceId(@Param("dataSourceId") Long dataSourceId);

	@Modifying
	@Query("UPDATE RunLog SET status = :status WHERE status = :previousStatus")
	public void updateStatus(
		@Param("previousStatus") String previousStatus,
		@Param("status") String status);

}