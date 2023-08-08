/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomDataControlTaskRepository {

	@Cacheable
	public long countDataControlTasks(
		Long batchId, String emailAddress, Date startCreateDate,
		List<String> statuses, List<String> types);

	public Boolean existsByBatchIdAndStatusIn(
		@Nullable Long batchId, @Nullable List<String> statuses);

	@Cacheable
	public Set<String> findSuppressedEmailAddresses();

	@Cacheable
	public List<DataControlTask> searchDataControlTasks(
		@Nullable Date endCompleteDate, @Nullable List<String> statuses,
		@Nullable List<String> types);

	@Cacheable
	public List<DataControlTask> searchDataControlTasks(
		FilterHelper filterHelper, @Nullable String status);

	@Cacheable
	public List<DataControlTask> searchDataControlTasks(
		@Nullable Long batchId, @Nullable String emailAddress,
		@Nullable Date startCreateDate, @Nullable List<String> statuses,
		@Nullable List<String> types, Pageable pageable);

}