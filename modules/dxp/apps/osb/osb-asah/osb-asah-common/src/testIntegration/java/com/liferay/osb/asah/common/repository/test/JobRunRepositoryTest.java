/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.entity.JobRun;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobRunStatus;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.common.repository.JobRunRepository;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class JobRunRepositoryTest extends BaseRepositoryTestCase<JobRun, Long> {

	@BeforeEach
	public void setUp() {
		Job job = _addJob();

		JobRun jobRun1 = new JobRun();

		jobRun1.setCompletedDate(new Date());
		jobRun1.setContextJSONObject(JSONUtil.put("key1", "value1"));
		jobRun1.setCreateLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
		jobRun1.setJobId(job.getId());
		jobRun1.setJobRunStatus(JobRunStatus.RUNNING);
		jobRun1.setJobType(String.valueOf(job.getJobType()));
		jobRun1.setStep("DATA_PREPARATION");
		jobRun1.setTrigger("manual");

		JobRun jobRun2 = new JobRun();

		jobRun2.setCompletedDate(new Date());
		jobRun2.setContextJSONObject(JSONUtil.put("key2", "value2"));
		jobRun2.setCreateLocalDateTime(LocalDateTime.now(ZoneOffset.UTC));
		jobRun2.setJobId(job.getId());
		jobRun2.setJobRunStatus(JobRunStatus.PUBLISHED);
		jobRun2.setJobType(String.valueOf(job.getJobType()));
		jobRun2.setStep("DATA_OUTPUT");
		jobRun2.setTrigger("manual");

		setUpRepository(jobRun1, jobRun2);
	}

	@Test
	public void testCountByJobId() {
		Assertions.assertEquals(2, _jobRunRepository.countByJobId(321L));
	}

	@Test
	public void testDeleteByJobId() {
		_jobRunRepository.deleteByJobId(321L);

		Assertions.assertEquals(0, _jobRunRepository.countByJobId(321L));
	}

	@Test
	public void testExistsByJobIdAndJobRunStatus() {
		Assertions.assertFalse(
			_jobRunRepository.existsByJobIdAndJobRunStatus(
				321L, JobRunStatus.FAILED));
		Assertions.assertTrue(
			_jobRunRepository.existsByJobIdAndJobRunStatus(
				321L, JobRunStatus.RUNNING));
	}

	@Test
	public void testFindByCreateLocalDateTimeBetweenAndJobId() {
		LocalDateTime createLocalDateTime2 = LocalDateTime.now(
			_timeZoneDog.getZoneId());

		LocalDateTime createLocalDateTime1 =
			createLocalDateTime2.withDayOfMonth(1);

		createLocalDateTime1 = createLocalDateTime1.truncatedTo(
			ChronoUnit.DAYS);

		List<JobRun> jobRuns =
			_jobRunRepository.findByCreateLocalDateTimeBetweenAndJobId(
				createLocalDateTime1, createLocalDateTime2, 321L);

		Assertions.assertEquals(2, jobRuns.size(), jobRuns.toString());
	}

	@Test
	public void testFindByJobId() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.asc("id"));

		List<JobRun> jobRuns = _jobRunRepository.findByJobId(321L, pageRequest);

		Assertions.assertEquals(2, jobRuns.size(), jobRuns.toString());

		JobRun jobRun = jobRuns.get(0);

		Assertions.assertEquals(
			JobRunStatus.RUNNING, jobRun.getJobRunStatus(), jobRun.toString());
	}

	@Test
	public void testFindByJobRunStatusAndJobTypeAndStep() {
		List<JobRun> jobRuns =
			_jobRunRepository.findByJobRunStatusAndJobTypeAndStep(
				JobRunStatus.RUNNING,
				String.valueOf(JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY),
				"DATA_PREPARATION");

		Assertions.assertEquals(1, jobRuns.size(), jobRuns.toString());

		JobRun jobRun = jobRuns.get(0);

		Assertions.assertEquals(
			JobRunStatus.RUNNING, jobRun.getJobRunStatus(), jobRun.toString());
	}

	@Test
	public void testFindByJobRunStatusIn() {
		List<String> jobRunStatuses = Arrays.asList(
			String.valueOf(JobRunStatus.RUNNING),
			String.valueOf(JobRunStatus.UNKNOWN));

		List<JobRun> jobRuns = _jobRunRepository.findByJobRunStatusIn(
			jobRunStatuses);

		Assertions.assertEquals(1, jobRuns.size(), jobRuns.toString());
	}

	@Test
	public void testFindFirstByJobIdAndJobRunStatusOrderByIdDesc() {
		Optional<JobRun> jobRunOptional =
			_jobRunRepository.findFirstByJobIdAndJobRunStatusOrderByIdDesc(
				321L, JobRunStatus.RUNNING);

		Assertions.assertTrue(jobRunOptional.isPresent());

		JobRun jobRun = jobRunOptional.get();

		Assertions.assertEquals(
			JobRunStatus.RUNNING, jobRun.getJobRunStatus(), jobRun.toString());
	}

	@Test
	public void testFindFirstByJobIdAndTriggerOrderByIdDesc() {
		Optional<JobRun> jobRunOptional =
			_jobRunRepository.findFirstByJobIdAndTriggerOrderByIdDesc(
				321L, "manual");

		Assertions.assertTrue(jobRunOptional.isPresent());

		JobRun jobRun = jobRunOptional.get();

		Assertions.assertEquals(
			JobRunStatus.PUBLISHED, jobRun.getJobRunStatus(),
			jobRun.toString());
	}

	@Test
	public void testFindFirstByJobIdOrderByIdDesc() {
		Optional<JobRun> jobRunOptional =
			_jobRunRepository.findFirstByJobIdOrderByIdDesc(321L);

		Assertions.assertTrue(jobRunOptional.isPresent());

		JobRun jobRun = jobRunOptional.get();

		Assertions.assertEquals(
			JobRunStatus.PUBLISHED, jobRun.getJobRunStatus(),
			jobRun.toString());
	}

	@Override
	protected PagingAndSortingRepository<JobRun, Long>
		getPagingAndSortingRepository() {

		return _jobRunRepository;
	}

	private Job _addJob() {
		Job job = new Job();

		LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);

		job.setCreateLocalDateTime(localDateTime);

		job.setId(321L);
		job.setIsNew(Boolean.TRUE);
		job.setJobType(JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY);
		job.setJobRunFrequency(JobRunFrequency.MANUAL);
		job.setJobRunDataPeriod(JobRunDataPeriod.LAST_30_DAYS);
		job.setJobParameters(SetUtil.of(new JobParameter("parameter1", "1.2")));
		job.setModifiedLocalDateTime(localDateTime);
		job.setName("Product Recommendation Job");

		return _jobRepository.save(job);
	}

	@Autowired
	private JobRepository _jobRepository;

	@Autowired
	private JobRunRepository _jobRunRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}