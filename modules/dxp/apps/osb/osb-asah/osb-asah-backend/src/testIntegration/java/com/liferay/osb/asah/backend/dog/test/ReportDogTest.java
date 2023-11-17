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
	public void testGetCSVReportAssetBlogFilteredByQuery() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_blog_filtered_by_" +
				"query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Blog 1", null,
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
	public void testGetCSVReportAssetBlogSortedByViews() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies" +
				"/test_get_csv_report_asset_blog_sorted_by_views_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, new String[] {"viewsMetric", "asc"},
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
	public void testGetCSVReportAssetDocumentLibraryFilteredByQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_" +
				"document_library_filtered_by_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Document 3", null,
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
	public void testGetCSVReportAssetDocumentLibrarySortedByDownloads()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_" +
				"document_library_sorted_by_downloads_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null,
					new String[] {"downloadsMetric", "asc"},
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
	public void testGetCSVReportAssetFormFilteredByQuery() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_form_filtered_by_" +
				"query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Form 1", null,
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
	public void testGetCSVReportAssetFormSortedBySubmissions()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_form_sorted_by_" +
				"submissions_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null,
					new String[] {"submissionsMetric", "asc"},
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
	public void testGetCSVReportAssetIndividualForAGivenBlogFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"blog_filtered_by_email_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"3", "blog", 1L, "test3@liferay.com", null,
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
	public void testGetCSVReportAssetIndividualForAGivenBlogFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"blog_filtered_by_name_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"1", "blog", 1L, "Test 1", null,
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
	public void testGetCSVReportAssetIndividualForAGivenDocumentLibraryFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"document_library_filtered_by_email_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"3", "document", 1L, "test3@liferay.com", null,
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
	public void testGetCSVReportAssetIndividualForAGivenDocumentLibraryFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"document_library_filtered_by_name_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"1", "document", 1L, "Test 1", null,
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
	public void testGetCSVReportAssetIndividualForAGivenFormFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"form_filtered_by_email_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"2", "form", 1L, "test2@liferay.com", null,
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
	public void testGetCSVReportAssetIndividualForAGivenFormFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"form_filtered_by_name_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"1", "form", 1L, "Test 1", null,
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
	public void testGetCSVReportAssetIndividualForAGivenJournalFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"journal_filtered_by_email_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"3", "journal", 1L, "test3@liferay.com", null,
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
	public void testGetCSVReportAssetIndividualForAGivenJournalFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_individual_for_a_given_" +
				"journal_filtered_by_name_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"2", "journal", 1L, "Test 2", null,
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
	public void testGetCSVReportAssetIndividualForAGivenPageFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_" +
				"individual_for_a_given_page_filtered_by_" +
					"email_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"https://www.beryl.com/delivery", "page", 1L,
					"test3@liferay.com", null,
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
	public void testGetCSVReportAssetIndividualForAGivenPageFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_" +
				"individual_for_a_given_page_filtered_by_" +
					"name_in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					"https://www.beryl.com/delivery", "page", 1L, "Test 1",
					null,
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
	public void testGetCSVReportAssetJournalFilteredByQuery() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_journal_filtered_by_" +
				"query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Journal 3", null,
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
	public void testGetCSVReportAssetJournalSortedByViews() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_asset_journal_sorted_by_" +
				"views_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, new String[] {"viewsMetric", "asc"},
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

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportIndividual() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_individual_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, null, null, "individual"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportIndividualFilteredByEmailInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_individual_filtered_by_email_" +
				"in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "test2@liferay.com", null, null,
					"individual"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportIndividualFilteredByNameInQuery()
		throws Exception {

		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_individual_filtered_by_name_" +
				"in_query_expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, "Test 3", null, null, "individual"))) {

			Assertions.assertTrue(
				IOUtils.contentEquals(
					classPathResource.getInputStream(), inputStream));
		}
	}

	@BQSQLResource(resourcePath = "test_report_dog.sql")
	@Test
	public void testGetCSVReportIndividualSortedByJobTitle() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(
			"dependencies/test_get_csv_report_individual_sorted_by_jobtitle_" +
				"expected.csv",
			getClass());

		try (InputStream inputStream = new FileInputStream(
				_reportDog.getCSVReport(
					null, null, 1L, null, new String[] {"jobTitle", "asc"},
					null, "individual"))) {

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