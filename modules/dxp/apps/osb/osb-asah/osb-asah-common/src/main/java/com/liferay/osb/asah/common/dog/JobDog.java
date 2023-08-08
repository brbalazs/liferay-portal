/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.AsahTask;
import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.entity.JobRun;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobRunStatus;
import com.liferay.osb.asah.common.model.JobRunsMonthlyStatistics;
import com.liferay.osb.asah.common.model.JobStatus;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class JobDog {

	public Job addJob(
		Set<JobParameter> jobParameters, JobRunDataPeriod jobRunDataPeriod,
		JobRunFrequency jobRunFrequency, JobType jobType, String name,
		boolean runNow) {

		Job job = new Job();

		LocalDateTime nowLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);

		job.setCreateLocalDateTime(nowLocalDateTime);

		job.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		job.setIsNew(Boolean.TRUE);
		job.setModifiedLocalDateTime(nowLocalDateTime);

		job.setName(name);
		job.setJobParameters(jobParameters);
		job.setJobRunDataPeriod(jobRunDataPeriod);
		job.setJobRunFrequency(jobRunFrequency);
		job.setJobType(jobType);

		job = _jobRepository.save(job);

		_scheduleAsahTask(job);

		if (runNow) {
			runJob(job.getId(), jobRunDataPeriod);
		}

		return job;
	}

	public void deleteJobs(List<Long> jobIds) {
		jobIds.forEach(this::_deleteJob);
	}

	public Job fetchJob(String name) {
		Optional<Job> jobOptional = _jobRepository.findFirstByName(name);

		return jobOptional.orElse(null);
	}

	public Job getJob(Long jobId) {
		Optional<Job> jobOptional = _jobRepository.findById(jobId);

		return jobOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST, "There is no job with ID " + jobId));
	}

	public String getJobNextRunDateString(Long jobId) {
		Job job = getJob(jobId);

		JobRunFrequency jobRunFrequency = job.getJobRunFrequency();

		if (jobRunFrequency == JobRunFrequency.MANUAL) {
			return null;
		}

		Date startDate = DateUtil.toUTCDate(job.getModifiedLocalDateTime());

		JobRun jobRun = _jobRunDog.fetchLatestJobRun(jobId, "SCHEDULE");

		if (jobRun != null) {
			startDate = DateUtil.toUTCDate(jobRun.getCreateLocalDateTime());
		}

		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(
			jobRunFrequency.getCronExpression(), TimeZone.getTimeZone("UTC"));

		return DateUtil.toUTCString(cronSequenceGenerator.next(startDate));
	}

	public Page<Job> getJobPage(
		String keywords, int page, int size, Sort sort) {

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_jobRepository.findByNameContainingIgnoreCase(
				keywords, pageRequest),
			pageRequest,
			() -> _jobRepository.countByNameContainingIgnoreCase(keywords));
	}

	public JobStatus getJobStatus(Long jobId) {
		if (_jobRunDog.existsJobRunPublished(jobId)) {
			return JobStatus.READY;
		}

		JobRun jobRun = _jobRunDog.fetchLatestJobRun(jobId);

		if (jobRun != null) {
			if (jobRun.getJobRunStatus() == JobRunStatus.FAILED) {
				return JobStatus.FAILED;
			}

			if ((jobRun.getJobRunStatus() == JobRunStatus.RUNNING) ||
				(jobRun.getJobRunStatus() == JobRunStatus.UNKNOWN)) {

				return JobStatus.RUNNING;
			}
		}

		Job job = getJob(jobId);

		if (job.getJobRunFrequency() == JobRunFrequency.MANUAL) {
			return JobStatus.PENDING;
		}

		return JobStatus.SCHEDULED;
	}

	public Job runJob(Long jobId, JobRunDataPeriod jobRunDataPeriod) {
		JobRun jobRun = _jobRunDog.fetchLatestJobRun(jobId);

		if ((jobRun != null) &&
			(jobRun.getJobRunStatus() == JobRunStatus.RUNNING)) {

			throw new IllegalStateException(
				String.format(
					"Unable to run job ID %s because it is already running",
					jobId));
		}

		Job job = getJob(jobId);

		JobRunsMonthlyStatistics jobRunsMonthlyStatistics =
			_jobRunDog.getJobRunsMonthlyStatistics(job);

		if (jobRunsMonthlyStatistics.getAvailableJobRuns() == 0) {
			throw new IllegalStateException(
				String.format(
					"Unable to run job ID %s because this run surpasses the " +
						"maximum allowed monthly runs threshold",
					jobId));
		}

		_asahTaskDog.scheduleAsahTask(
			_jobTypeNaniteMap.get(job.getJobType()),
			JSONUtil.put(
				"job", _objectMapper.convertValue(job, JSONObject.class)
			).put(
				"runDataPeriod", jobRunDataPeriod.toString()
			).put(
				"trigger", "MANUAL"
			));

		return job;
	}

	public Job updateJob(
		Long jobId, Set<JobParameter> jobParameters,
		JobRunDataPeriod jobRunDataPeriod, JobRunFrequency jobRunFrequency,
		String name, boolean runNow) {

		Job job = getJob(jobId);

		JobRunFrequency oldJobRunFrequency = job.getJobRunFrequency();

		job.setModifiedLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
		job.setName(name);
		job.setJobParameters(jobParameters);
		job.setJobRunFrequency(jobRunFrequency);
		job.setJobRunDataPeriod(jobRunDataPeriod);

		_rescheduleAsahTask(job, oldJobRunFrequency);

		job = _jobRepository.save(job);

		if (runNow) {
			runJob(jobId, jobRunDataPeriod);
		}

		return job;
	}

	private void _deleteJob(Long jobId) {
		_unscheduleAsahTask(getJob(jobId));

		_jobRunDog.deleteByJobId(jobId);

		_recommendationDog.deleteItemRecommendationsByJobId(jobId);

		_jobRepository.deleteById(jobId);
	}

	private void _rescheduleAsahTask(
		Job job, JobRunFrequency oldJobRunFrequency) {

		JobRunFrequency newJobRunFrequency = job.getJobRunFrequency();

		if (newJobRunFrequency == oldJobRunFrequency) {
			return;
		}

		_unscheduleAsahTask(job);

		_scheduleAsahTask(job);
	}

	private void _scheduleAsahTask(Job job) {
		JobRunFrequency jobRunFrequency = job.getJobRunFrequency();

		if (jobRunFrequency == JobRunFrequency.MANUAL) {
			return;
		}

		AsahTask asahTask = _asahTaskDog.scheduleAsahTask(
			_jobTypeNaniteMap.get(job.getJobType()),
			JSONUtil.put(
				"job", _objectMapper.convertValue(job, JSONObject.class)),
			jobRunFrequency.getCronExpression());

		job.setAsahTaskId(asahTask.getId());
	}

	private void _unscheduleAsahTask(Job job) {
		Long asahTaskId = job.getAsahTaskId();

		if (asahTaskId == null) {
			return;
		}

		_asahTaskDog.unscheduleAsahTask(asahTaskId);

		job.setAsahTaskId(null);
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private JobRepository _jobRepository;

	@Autowired
	private JobRunDog _jobRunDog;

	private final Map<JobType, String> _jobTypeNaniteMap =
		new HashMap<JobType, String>() {
			{
				put(
					JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY,
					"ContentRecommendationDataPreparationNanite");
			}
		};

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private RecommendationDog _recommendationDog;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}