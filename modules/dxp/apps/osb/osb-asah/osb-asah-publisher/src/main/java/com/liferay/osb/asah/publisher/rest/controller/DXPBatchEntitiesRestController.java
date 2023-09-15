/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.antivirus.ClamAVScanner;
import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.storage.Storage;
import com.liferay.osb.asah.common.storage.StorageConfiguration;
import com.liferay.osb.asah.common.storage.StorageFactory;
import com.liferay.osb.asah.common.util.ArrayUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.publisher.messaging.DXPEntitiesChannels;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

			_messageBus.sendMessage(
				Channel.COMPOSER,
				JSONUtil.put(
					"dataSourceId", dataSourceId
				).put(
					"projectId", ProjectIdThreadLocal.getProjectId()
				).put(
					"resourceName", name
				).put(
					"uploadComplete", success
				).put(
					"uploadTime", DateUtil.toUTCString(new Date())
				).put(
					"uploadType", (uploadType != null) ? uploadType : "FULL"
				).toString());

			if (!success) {
				return new ResponseEntity(
					Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return ResponseEntity.ok(Collections.emptyList());
	}

	private Map<Long, Long> _getCommerceChannelIdChannelIds(long dataSourceId) {
		List<com.liferay.osb.asah.common.entity.Channel> channels =
			_channelDog.getChannels(dataSourceId);

		Map<Long, Long> commerceChanelIdChannelIds = new HashMap<>();

		for (com.liferay.osb.asah.common.entity.Channel channel : channels) {
			for (ChannelDataSource channelDataSource :
					channel.getChannelDataSources()) {

				if (channelDataSource.getCommerceChannelIds() == null) {
					continue;
				}

				for (Long commerceChannelId :
						channelDataSource.getCommerceChannelIds()) {

					commerceChanelIdChannelIds.put(
						commerceChannelId, channel.getId());
				}
			}
		}

		return commerceChanelIdChannelIds;
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

		String dateString = DateUtil.newDateString();

		StorageConfiguration.Builder builder = StorageConfiguration.builder(
			String.format(
				"%s/%s/%s.zip",
				StringUtils.replace(
					_dxpBatchEntitiesStoragePathTemplate, "{projectId}",
					ProjectIdThreadLocal.getProjectId()),
				resourceName, dateString));

		builder.fileFormat(StorageConfiguration.FileFormat.JSON);

		builder.googleBucket(
			StringUtils.replace(
				_dxpEntitiesBucketTemplate, "{googleProjectId}",
				_gcloudProjectId));

		StringBuilder sb = new StringBuilder(7);

		sb.append(dataSourceId);
		sb.append("/");
		sb.append(resourceName);

		if (StringUtils.isNotBlank(uploadType)) {
			sb.append("/");
			sb.append(uploadType);
		}

		sb.append("/");
		sb.append(dateString);

		builder.googleBucketFolder(sb.toString());

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

	private boolean _publishMessages(
			String dataSourceId, String resourceName, InputStream inputStream,
			String uploadType)
		throws Exception {

		Channel channel = _dxpEntitiesChannels.getChannel(resourceName);

		Map<String, String> messageAttributes = new HashMap<>();

		if (ArrayUtil.contains(
				_REQUIRE_COMMERCE_CHANNEL_ID_CHANNEL_ID_RESOURCE_NAMES,
				resourceName)) {

			messageAttributes.put(
				"commerceChannelIdChannelIds",
				_objectMapper.writeValueAsString(
					_getCommerceChannelIdChannelIds(
						Long.parseLong(dataSourceId))));
		}
		else {
			messageAttributes.put(
				"suppressedEmailAddresses",
				_objectMapper.writeValueAsString(
					_dataControlTaskDog.getSuppressedEmailAddresses()));
		}

		messageAttributes.put("dataSourceId", dataSourceId);
		messageAttributes.put("projectId", ProjectIdThreadLocal.getProjectId());
		messageAttributes.put("resourceName", resourceName);
		messageAttributes.put("uploadTime", DateUtil.toUTCString(new Date()));
		messageAttributes.put(
			"uploadType", (uploadType != null) ? uploadType : "FULL");

		boolean status = false;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			long count = 1;

			String line = bufferedReader.readLine();

			while (line != null) {
				String nextLine = bufferedReader.readLine();

				messageAttributes.put("count", String.valueOf(count));
				messageAttributes.put(
					"last", (nextLine == null) ? "true" : "false");

				_messageBus.sendMessage(channel, line, messageAttributes);

				count += 1;

				line = nextLine;
			}

			status = true;
		}
		catch (IOException ioException) {
			_log.error(ioException, ioException);
		}

		return status;
	}

	private boolean _storeMessages(
		String dataSourceId, String resourceName, InputStream inputStream,
		String uploadType) {

		Storage uploadStorage = _storageFactory.getStorage(
			_getUploadStorageConfiguration(
				dataSourceId, resourceName, uploadType));

		return uploadStorage.write(inputStream);
	}

	private static final String[]
		_REQUIRE_COMMERCE_CHANNEL_ID_CHANNEL_ID_RESOURCE_NAMES = {
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Order",
			"com.liferay.headless.commerce.machine.learning.dto.v1_0.Product"
		};

	private static final Log _log = LogFactory.getLog(
		DXPBatchEntitiesRestController.class);

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");

	@Autowired
	private ChannelDog _channelDog;

	@Autowired(required = false)
	private ClamAVScanner _clamAVScanner;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage/{projectId}}")
	private String _dxpBatchEntitiesStoragePathTemplate;

	@Value(
		"${osb.asah.dxp.batch.entities.google.bucket:{googleProjectId}-dxp-entities}"
	)
	private String _dxpEntitiesBucketTemplate;

	@Autowired
	private DXPEntitiesChannels _dxpEntitiesChannels;

	@Value("${osb.asah.gcloud.project.id:liferaycloud-customer-ac}")
	private String _gcloudProjectId;

	@Autowired
	private MessageBus _messageBus;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private StorageFactory _storageFactory;

}