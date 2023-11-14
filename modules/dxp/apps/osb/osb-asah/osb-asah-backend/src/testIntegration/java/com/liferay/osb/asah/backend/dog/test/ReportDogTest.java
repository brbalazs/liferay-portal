/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.ReportDog;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.io.FileInputStream;
import java.io.InputStream;

import java.time.LocalDate;

import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Marcos Martins
 */
public class ReportDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		Channel channel = new Channel("Channel Test");

		channel.setId(1L);
		channel.setIsNew(true);

		_channelRepository.save(channel);
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetBlog() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_blog_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"blog"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetDocumentLibrary() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies" +
				"/test_get_csv_report_asset_document_library_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"document"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetForm() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_form_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"form"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetIndividual() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"https://www.beryl.com/delivery", "page", 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"individual"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetJournal() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_journal_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"journal"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetPage() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_page_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"page"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetPageFilteredByQuery() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_page_filtered_by_" +
				"query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Liferay", null,
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"page"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportAssetPageSortedByEntrances() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_page_sorted_by_" +
				"entrances_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null,
					new String[] {"entrancesMetric", "asc"},
					TimeRange.of(
						LocalDate.of(2023, 11, 6), LocalDate.of(2023, 11, 3)),
					"page"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private ReportDog _reportDog;

}