/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class JobRepositoryTest extends BaseRepositoryTestCase<Job, Long> {

	@BeforeEach
	public void setUp() {
		Job job = new Job();

		LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);

		job.setCreateLocalDateTime(localDateTime);

		job.setJobType(JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY);
		job.setJobRunFrequency(JobRunFrequency.MANUAL);
		job.setJobRunDataPeriod(JobRunDataPeriod.LAST_30_DAYS);
		job.setJobParameters(SetUtil.of(new JobParameter("parameter1", "1.2")));
		job.setModifiedLocalDateTime(localDateTime);
		job.setName("Product Recommendation Job");

		setUpRepository(job);
	}

	@Override
	protected PagingAndSortingRepository<Job, Long>
		getPagingAndSortingRepository() {

		return _jobRunRepository;
	}

	@Autowired
	private JobRepository _jobRunRepository;

}