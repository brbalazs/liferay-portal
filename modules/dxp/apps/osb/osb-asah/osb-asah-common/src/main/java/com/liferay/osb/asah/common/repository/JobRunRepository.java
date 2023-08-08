/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.JobRun;
import com.liferay.osb.asah.common.model.JobRunStatus;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * @author Marcellus Tavares
 */
public interface JobRunRepository extends Repository<JobRun, Long> {

	@Cacheable
	public long countByJobId(Long jobId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByJobId(@Param("jobId") Long jobId);

	@Cacheable
	public boolean existsByJobIdAndJobRunStatus(
		Long jobId, JobRunStatus jobRunStatus);

	@Cacheable
	public List<JobRun> findByCreateLocalDateTimeBetweenAndJobId(
		LocalDateTime createLocalDateTime1, LocalDateTime createLocalDateTime2,
		Long jobId);

	@Cacheable
	public List<JobRun> findByJobId(Long jobId, Pageable pageable);

	@Cacheable
	public List<JobRun> findByJobRunStatusAndJobTypeAndStep(
		JobRunStatus jobRunStatus, String jobType, String step);

	@Cacheable
	public List<JobRun> findByJobRunStatusIn(List<String> jobRunStatus);

	@Cacheable
	public Optional<JobRun> findFirstByJobIdAndJobRunStatusOrderByIdDesc(
		Long jobId, JobRunStatus jobRunStatus);

	@Cacheable
	public Optional<JobRun> findFirstByJobIdAndTriggerOrderByIdDesc(
		Long jobId, String trigger);

	@Cacheable
	public Optional<JobRun> findFirstByJobIdOrderByIdDesc(Long jobId);

}