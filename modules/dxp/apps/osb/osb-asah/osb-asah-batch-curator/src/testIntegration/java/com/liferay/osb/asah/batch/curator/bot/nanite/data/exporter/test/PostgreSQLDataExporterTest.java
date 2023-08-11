/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter.test;

import com.fasterxml.jackson.core.JsonFactory;

import com.liferay.osb.asah.batch.curator.OSBAsahBatchCuratorSpringTestContext;
import com.liferay.osb.asah.common.data.exporter.DataExporter;
import com.liferay.osb.asah.common.data.exporter.PostgreSQLDataExporter;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import org.jooq.DSLContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Thiago Buarque
 */
public class PostgreSQLDataExporterTest
	implements OSBAsahBatchCuratorSpringTestContext {

	@BeforeEach
	public void setUp() {
		ProjectIdThreadLocal.setProjectId("test");
	}

	@AfterEach
	public void tearDown() {
		_segmentRepository.deleteAll();
	}

	@Test
	public void testExportSegmentData() throws Exception {
		Segment segment1 = new Segment();

		segment1.setAuthorName("Test Test");
		segment1.setCreateDate(DateUtil.toUTCDate("2023-04-02T16:02:01.824Z"));
		segment1.setFilter("(demographics/birthday/value lt '2023-04-20')");
		segment1.setId(12345L);
		segment1.setIsNew(true);
		segment1.setName("Segment 1");
		segment1.setState("READY");
		segment1.setStatus("ACTIVE");
		segment1.setType(Segment.Type.DYNAMIC);

		Segment segment2 = new Segment();

		segment2.setAuthorName("Test Test");
		segment2.setCreateDate(DateUtil.toUTCDate("2023-04-03T03:02:01.824Z"));
		segment2.setFilter("(demographics/emailAddress/value ne null)");
		segment2.setId(67890L);
		segment2.setIsNew(true);
		segment2.setName("Segment 2");
		segment2.setState("READY");
		segment2.setStatus("ACTIVE");
		segment2.setType(Segment.Type.DYNAMIC);

		_segmentRepository.saveAll(Arrays.asList(segment1, segment2));

		DataExportTask dataExportTask = _createDataExportTask(
			DateUtil.toUTCDate("2023-04-01T00:00:00.000Z"), 1L,
			DateUtil.toUTCDate("2023-04-28T23:23:59.000Z"));

		_exportSegmentData(dataExportTask);

		_extractJSONFileFromZip();

		try (FileInputStream fileInputStream = new FileInputStream(
				"data.json")) {

			JSONAssert.assertEquals(
				ResourceUtil.readResourceToString(
					"dependencies/expected_segments_export.jsonl", this),
				IOUtils.toString(fileInputStream, StandardCharsets.UTF_8),
				true);
		}
		finally {
			Files.deleteIfExists(Paths.get("data.json"));
		}
	}

	private DataExportTask _createDataExportTask(
		Date fromDate, long id, Date toDate) {

		DataExportTask dataExportTask = new DataExportTask();

		dataExportTask.setFromDate(fromDate);
		dataExportTask.setId(id);
		dataExportTask.setToDate(toDate);

		return dataExportTask;
	}

	private void _exportSegmentData(DataExportTask dataExportTask)
		throws Exception {

		try (OutputStream zipOutputStream = _getZipOutputStream()) {
			DataExporter dataExporter = new PostgreSQLDataExporter(
				dataExportTask, "createDate", _dslContext, _jsonFactory,
				zipOutputStream, "segment");

			dataExporter.export();
		}
	}

	private void _extractJSONFileFromZip() throws Exception {
		try (ZipInputStream zipInputStream = new ZipInputStream(
				new FileInputStream("1.zip"), StandardCharsets.UTF_8)) {

			zipInputStream.getNextEntry();

			try (FileOutputStream fileOutputStream = new FileOutputStream(
					"data.json")) {

				while (zipInputStream.available() != 0) {
					fileOutputStream.write(zipInputStream.read());
				}
			}
		}
		finally {
			Files.deleteIfExists(Paths.get("1.zip"));
		}
	}

	private OutputStream _getZipOutputStream() throws IOException {
		ZipOutputStream zipOutputStream = new ZipOutputStream(
			new FileOutputStream("1.zip"));

		File file = new File("data.json");

		zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

		return zipOutputStream;
	}

	@Autowired
	private DSLContext _dslContext;

	private final JsonFactory _jsonFactory = new JsonFactory();

	@Autowired
	private SegmentRepository _segmentRepository;

}