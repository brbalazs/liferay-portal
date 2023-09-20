/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@ConditionalOnGoogleApplicationCredentials
public class GoogleStorageArchiver {

	public void archiveAsync(
		String bucket, String bucketFolder, File file, String fileName,
		String projectId) {

		_executorService.submit(
			() -> _archive(bucket, bucketFolder, file, fileName, projectId));
	}

	public void archiveSync(
		String bucket, String bucketFolder, File file, String fileName,
		String projectId) {

		_archive(bucket, bucketFolder, file, fileName, projectId);
	}

	public File readSparkJobResult(
			String bucket, String bucketFolder, String projectId,
			Date sparkJobResultDateAfter, String sparkJobResultPathPrefix)
		throws Exception {

		BlobId successBlobId = BlobId.of(
			bucket,
			_getBlobName(
				bucketFolder, sparkJobResultPathPrefix + "/_SUCCESS",
				projectId));

		Blob successBlob = _storage.get(successBlobId);

		if ((successBlob == null) || !successBlob.exists()) {
			_log.error("_SUCCESS file missing");

			return null;
		}

		if ((sparkJobResultDateAfter != null) &&
			(sparkJobResultDateAfter.getTime() > successBlob.getCreateTime())) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"Success blob '%s' is older than the requested date %s",
						successBlob.getName(), sparkJobResultDateAfter));
			}

			return null;
		}

		Page<Blob> blobs = _storage.list(
			bucket,
			Storage.BlobListOption.prefix(
				_getBlobName(
					bucketFolder, sparkJobResultPathPrefix, projectId)));

		String tempFileName = _createTempFileName(".zip", "export");

		ZipOutputStream zipOutputStream = new ZipOutputStream(
			new FileOutputStream(tempFileName));

		File file = new File("export.json");

		zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

		for (Blob blob : blobs.iterateAll()) {
			String blobName = blob.getName();

			if (!blobName.endsWith(".json")) {
				continue;
			}

			try {
				byte[] bytes = blob.getContent();

				zipOutputStream.write(bytes, 0, bytes.length);
			}
			catch (IOException ioException) {
				_log.error(ioException.getMessage(), ioException);
			}
		}

		zipOutputStream.closeEntry();

		zipOutputStream.close();

		return new File(tempFileName);
	}

	private void _archive(
		String bucket, String bucketFolder, File file, String fileName,
		String projectId) {

		byte[] content = null;

		try {
			content = Files.readAllBytes(file.toPath());
		}
		catch (IOException ioException) {
			_log.error(
				String.format("Unable to read file %s bytes", file),
				ioException);

			return;
		}

		Blob blob = _uploadBlob(
			0,
			_buildBlobInfo(
				bucket, _getBlobName(bucketFolder, fileName, projectId)),
			content);

		if (blob == null) {
			_log.error(
				String.format(
					"Unable to upload file %s to the bucket %s", file, bucket));

			return;
		}

		if (_log.isInfoEnabled()) {
			_log.info(String.format("Successfully uploaded blob %s", blob));
		}

		boolean result = file.delete();

		if (result && _log.isDebugEnabled()) {
			_log.debug("Deleted file " + file);
		}
	}

	private BlobInfo _buildBlobInfo(String bucket, String fileName) {
		BlobInfo.Builder builder = BlobInfo.newBuilder(
			BlobId.of(bucket, fileName));

		return builder.build();
	}

	private String _createTempFileName(String extension, String prefix) {
		StringBuilder sb = new StringBuilder(7);

		sb.append(System.getProperty(_TMP_DIR));
		sb.append(File.separator);
		sb.append(prefix);
		sb.append("-");
		sb.append(System.currentTimeMillis());
		sb.append(extension);

		return sb.toString();
	}

	@PreDestroy
	private void _destroy() {
		_executorService.shutdown();

		try {
			if (!_executorService.awaitTermination(1, TimeUnit.MINUTES)) {
				_executorService.shutdownNow();
			}
		}
		catch (InterruptedException interruptedException) {
			_log.error(
				"Interrupted while waiting for termination of executor",
				interruptedException);
		}
	}

	private String _getBlobName(
		String bucketFolder, String fileName, String projectId) {

		StringBuilder sb = new StringBuilder();

		sb.append(projectId);

		if (bucketFolder != null) {
			sb.append("/");
			sb.append(bucketFolder);
		}

		if (!fileName.startsWith("/")) {
			sb.append("/");
		}

		sb.append(fileName);

		return sb.toString();
	}

	@PostConstruct
	private void _init() {
		StorageOptions storageOptions = StorageOptions.getDefaultInstance();

		_storage = storageOptions.getService();
	}

	private Blob _uploadBlob(int attempt, BlobInfo blobInfo, byte[] content) {
		try {
			return _storage.create(blobInfo, content);
		}
		catch (StorageException storageException) {
			_log.error(
				String.format(
					"Unable to upload blob %s to the bucket %s",
					blobInfo.getName(), blobInfo.getBucket()),
				storageException);

			if (attempt < 3) {
				_uploadBlob(++attempt, blobInfo, content);
			}
		}

		return null;
	}

	private static final String _TMP_DIR = "java.io.tmpdir";

	private static final Log _log = LogFactory.getLog(
		GoogleStorageArchiver.class);

	private final ExecutorService _executorService =
		Executors.newSingleThreadExecutor();
	private Storage _storage;

}