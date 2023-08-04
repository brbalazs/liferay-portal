/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
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