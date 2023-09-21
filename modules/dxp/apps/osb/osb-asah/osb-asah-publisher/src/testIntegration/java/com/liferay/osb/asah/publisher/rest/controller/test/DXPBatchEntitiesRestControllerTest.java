/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller.test;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.storage.Storage;
import com.liferay.osb.asah.common.storage.StorageConfiguration;
import com.liferay.osb.asah.common.storage.StorageFactory;
import com.liferay.osb.asah.common.zip.ZipFileBuilder;
import com.liferay.osb.asah.publisher.OSBAsahPublisherSpringTestContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.io.File;
import java.io.InputStream;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.util.Date;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Riccardo Ferrari
 */
public class DXPBatchEntitiesRestControllerTest
	implements OSBAsahPublisherSpringTestContext {

	@Test
	public void testGetNoContent() {
		Mockito.when(
			_storage.readSparkJobResult(
				ArgumentMatchers.any(Date.class), ArgumentMatchers.anyString())
		).thenReturn(
			null
		);

		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		ResponseEntity<Resource> responseEntity = _exchange(_getHttpHeaders());

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(204)
		);

		Assertions.assertThat(
			responseEntity.getBody()
		).isNull();
	}

	@Test
	public void testGetStatusCode200() throws Exception {
		Mockito.when(
			_storage.readSparkJobResult(
				ArgumentMatchers.any(Date.class), ArgumentMatchers.anyString())
		).thenReturn(
			File.createTempFile(RandomTestUtil.randomString(), null)
		);

		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		ResponseEntity<Resource> responseEntity = _exchange(_getHttpHeaders());

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);
	}

	@Test
	public void testGetStatusCode400() throws Exception {
		Mockito.when(
			_storage.readSparkJobResult(
				ArgumentMatchers.any(Date.class), ArgumentMatchers.anyString())
		).thenReturn(
			File.createTempFile(RandomTestUtil.randomString(), null)
		);

		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add("If-Modified-Since", _getModifiedSince());

		ResponseEntity<Resource> responseEntity = _exchange(httpHeaders);

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(400)
		);
	}

	@Test
	public void testGetWithInvalidIfModifiedSince() throws Exception {
		Mockito.when(
			_storage.readSparkJobResult(
				ArgumentMatchers.isNull(), ArgumentMatchers.anyString())
		).thenReturn(
			File.createTempFile(RandomTestUtil.randomString(), null)
		);

		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.DATA_SOURCE_ID, "test-data-source-id");
		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.add("If-Modified-Since", DateUtil.newDateString());

		ResponseEntity<Resource> responseEntity = _exchange(httpHeaders);

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);
	}

	@Test
	public void testGetWithNullIfModifiedSince() throws Exception {
		Mockito.when(
			_storage.readSparkJobResult(
				ArgumentMatchers.isNull(), ArgumentMatchers.anyString())
		).thenReturn(
			File.createTempFile(RandomTestUtil.randomString(), null)
		);

		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.DATA_SOURCE_ID, "test-data-source-id");
		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");

		ResponseEntity<Resource> responseEntity = _exchange(httpHeaders);

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);
	}

	@Test
	public void testPost() throws Exception {
		Mockito.when(
			_storageFactory.getStorage(
				ArgumentMatchers.any(StorageConfiguration.class))
		).thenReturn(
			_storage
		);

		Mockito.when(
			_storage.write(ArgumentMatchers.any(InputStream.class))
		).thenReturn(
			true
		);

		MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

		multipartBodyBuilder.part("file", _getFileSystemResource());

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(
			HeaderConstants.DATA_SOURCE_ID, RandomTestUtil.randomId());
		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		ResponseEntity<Resource> responseEntity = _testRestTemplate.exchange(
			"/dxp-batch-entities", HttpMethod.POST,
			new HttpEntity<>(multipartBodyBuilder.build(), httpHeaders),
			Resource.class);

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);

		Mockito.verify(
			_storage, Mockito.times(1)
		).write(
			ArgumentMatchers.any(InputStream.class)
		);
	}

	private ResponseEntity<Resource> _exchange(HttpHeaders httpHeaders) {
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
			String.format("http://localhost:%s/dxp-batch-entities", _serverPort)
		).queryParam(
			"resourceName", RandomTestUtil.randomString()
		).build();

		return _testRestTemplate.exchange(
			uriComponents.toString(), HttpMethod.GET,
			new HttpEntity<>(null, httpHeaders), Resource.class);
	}

	private FileSystemResource _getFileSystemResource() throws Exception {
		File tempFile = File.createTempFile("export", ".zip");

		ZipFileBuilder zipFileBuilder = new ZipFileBuilder(tempFile);

		zipFileBuilder.addToZip(
			"export.json",
			zipOutputStream -> {
				for (int i = 0; i < 5; i++) {
					String jsonString = String.valueOf(
						JSONUtil.put(
							"key1", RandomTestUtil.randomString()
						).put(
							"key2", RandomTestUtil.randomString()
						).put(
							"key3", RandomTestUtil.randomString()
						));

					zipOutputStream.write(jsonString.getBytes());

					zipOutputStream.write("\n".getBytes());
				}
			});

		zipFileBuilder.build();

		return new FileSystemResource(tempFile);
	}

	private HttpHeaders _getHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.DATA_SOURCE_ID, "test-data-source-id");
		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.add("If-Modified-Since", _getModifiedSince());

		return httpHeaders;
	}

	private String _getModifiedSince() {
		LocalDateTime localDateTime = LocalDateTime.now();

		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

		return _dateTimeFormatter.format(
			instant.atZone(ZoneId.systemDefault()));
	}

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz");

	@MockBean
	private MessageBus _messageBus;

	@LocalServerPort
	private int _serverPort;

	@MockBean
	private Storage _storage;

	@MockBean
	private StorageFactory _storageFactory;

	@Autowired
	private TestRestTemplate _testRestTemplate;

}