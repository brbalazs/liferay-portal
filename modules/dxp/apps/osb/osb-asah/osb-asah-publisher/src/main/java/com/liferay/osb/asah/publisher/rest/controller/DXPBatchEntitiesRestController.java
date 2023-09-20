/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller;

import com.liferay.osb.asah.common.antivirus.ClamAVScanner;
import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.storage.Storage;
import com.liferay.osb.asah.common.storage.StorageConfiguration;
import com.liferay.osb.asah.common.storage.StorageFactory;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.InputStream;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Riccardo Ferrari
 */
@CrossOrigin
@RequestMapping("/dxp-batch-entities")
@RestController
public class DXPBatchEntitiesRestController {

	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> get(
			@RequestHeader(value = HeaderConstants.DATA_SOURCE_ID) String
				dataSourceId,
			@RequestParam("resourceName") String resourceName,
			@RequestHeader(required = false, value = "If-Modified-Since") String
				ifModifiedSince)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Received download request for resource: " + resourceName);
		}

		Storage downloadStorage = _storageFactory.getStorage(
			_getDownloadStorageConfiguration(dataSourceId));

		File file = downloadStorage.readSparkJobResult(
			_parseDate(ifModifiedSince), resourceName);

		if (file == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

		bodyBuilder.headers(
			new HttpHeaders() {
				{
					add(
						HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + resourceName + ".zip");
					add(
						HttpHeaders.CONTENT_LENGTH,
						String.valueOf(file.length()));
				}
			});

		return bodyBuilder.body(new FileSystemResource(file));
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> post(
			@RequestHeader(value = HeaderConstants.DATA_SOURCE_ID) String
				dataSourceId,
			@RequestPart(value = "file") List<MultipartFile> multipartFiles,
			@RequestPart(required = false, value = "uploadType") String
				uploadType)
		throws Exception {

		for (MultipartFile multipartFile : multipartFiles) {
			String name = multipartFile.getOriginalFilename();

			if (_log.isDebugEnabled()) {
				_log.debug("Received upload request " + name);
			}

			if (_clamAVScanner != null) {
				_clamAVScanner.scan(multipartFile.getInputStream());
			}

			boolean success = _storeMessages(
				dataSourceId, name, multipartFile.getInputStream(), uploadType);

			if (!success) {
				return new ResponseEntity(
					Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return ResponseEntity.ok(Collections.emptyList());
	}

	private StorageConfiguration _getDownloadStorageConfiguration(
		String googleBucketFolder) {

		StorageConfiguration.Builder builder = StorageConfiguration.builder();

		builder.googleBucket(
			StringUtils.replace(
				_dxpEntitiesBucketTemplate, "{googleProjectId}",
				_gcloudProjectId));
		builder.googleBucketFolder(googleBucketFolder);

		return builder.build();
	}

	private StorageConfiguration _getUploadStorageConfiguration(
		String dataSourceId, String resourceName, String uploadType) {

		StorageConfiguration.Builder builder = StorageConfiguration.builder(
			String.format(
				"%s/%s/%s/%s/%s.zip", _dxpBatchEntitiesStoragePath,
				ProjectIdThreadLocal.getProjectId(), dataSourceId, resourceName,
				uploadType, DateUtil.newDateString()));

		return builder.build();
	}

	private Date _parseDate(String dateString) {
		try {
			if (dateString == null) {
				return null;
			}

			Instant instant = Instant.from(
				_dateTimeFormatter.parse(dateString));

			ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

			Date date = Date.from(zonedDateTime.toInstant());

			if (_log.isDebugEnabled()) {
				_log.debug("Resource modified date: " + date);
			}

			return date;
		}
		catch (DateTimeParseException dateTimeParseException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to parse last modified date",
					dateTimeParseException);
			}

			return null;
		}
	}

	private boolean _storeMessages(
		String dataSourceId, String resourceName, InputStream inputStream,
		String uploadType) {

		Storage uploadStorage = _storageFactory.getStorage(
			_getUploadStorageConfiguration(
				dataSourceId, resourceName, uploadType));

		return uploadStorage.write(inputStream);
	}

	private static final Log _log = LogFactory.getLog(
		DXPBatchEntitiesRestController.class);

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");

	@Autowired(required = false)
	private ClamAVScanner _clamAVScanner;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage}")
	private String _dxpBatchEntitiesStoragePath;

	@Value(
		"${osb.asah.dxp.batch.entities.google.bucket:{googleProjectId}-dxp-entities}"
	)
	private String _dxpEntitiesBucketTemplate;

	@Value("${osb.asah.gcloud.project.id:liferaycloud-customer-ac}")
	private String _gcloudProjectId;

	@Autowired
	private StorageFactory _storageFactory;

}