/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import com.liferay.osb.asah.common.storage.Storage;
import com.liferay.osb.asah.common.storage.StorageConfiguration;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Marcellus Tavares
 */
public class LocalStorage implements Storage {

	public LocalStorage(StorageConfiguration storageConfiguration) {
		_storageConfiguration = storageConfiguration;

		if (_log.isInfoEnabled()) {
			_log.info(
				"Local storage initialized for path " +
					_storageConfiguration.getPath());
		}

		try {
			_open();
		}
		catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}

	@Override
	public void close() {
		try {
			_fileEncoder.close();
		}
		catch (Exception exception) {
			_log.error(
				"Unable to close local storage for path " +
					_storageConfiguration.getPath(),
				exception);
		}
	}

	@Override
	public void flush() {
		try {
			_rollover();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	@Override
	public File readSparkJobResult(
		Date sparkJobResultDateAfter, String sparkJobResultPathPrefix) {

		if (!_isGoogleStorageArchiverEnabled()) {
			return null;
		}

		try {
			return _googleStorageArchiver.readSparkJobResult(
				_storageConfiguration.getGoogleBucket(),
				_storageConfiguration.getGoogleBucketFolder(),
				ProjectIdThreadLocal.getProjectId(), sparkJobResultDateAfter,
				sparkJobResultPathPrefix);
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		return null;
	}

	public void setGoogleStorageArchiver(
		GoogleStorageArchiver googleStorageArchiver) {

		if ((googleStorageArchiver != null) && _log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Google storage archiver initialized for bucket %s and " +
						"folder %s",
					_storageConfiguration.getGoogleBucket(),
					_storageConfiguration.getGoogleBucketFolder()));
		}

		_googleStorageArchiver = googleStorageArchiver;
	}

	@Override
	public boolean write(InputStream inputStream) {
		boolean status = true;

		try {
			IOUtils.copy(inputStream, _fileEncoder.getOutputStream());

			flush();

			_archiveSuccessFile();
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			status = false;
		}

		return status;
	}

	@Override
	public boolean write(String data) {
		try {
			if (_isFileSizeLimitReached()) {
				_rollover();
			}

			_fileEncoder.encode(data);

			return true;
		}
		catch (Exception exception) {
			_log.error("Unable to write data " + data, exception);

			return false;
		}
	}

	private void _archiveFile(File file) {
		if (!_isGoogleStorageArchiverEnabled()) {
			return;
		}

		if (_log.isInfoEnabled()) {
			_log.info(String.format("Archiving file %s ", file));
		}

		_googleStorageArchiver.archiveAsync(
			_storageConfiguration.getGoogleBucket(),
			_storageConfiguration.getGoogleBucketFolder(), file,
			_getArchiveFileName(file.getName()),
			ProjectIdThreadLocal.getProjectId());
	}

	private void _archiveSuccessFile() throws IOException {
		File successFile = new File(
			_storageConfiguration.getPath() + "._SUCCESS");

		if (successFile.createNewFile() && _log.isDebugEnabled()) {
			_log.debug("Local _SUCCESS created successfully");
		}

		_archiveFile(successFile);
	}

	private void _deleteFile(File file) throws Exception {
		if (!file.exists()) {
			return;
		}

		boolean result = file.delete();

		if (!result) {
			throw new IOException("Unable to delete file " + file);
		}
	}

	private String _getArchiveFileName(String fileName) {
		int index = fileName.lastIndexOf('.');

		return String.format(
			"%s/%s", fileName.substring(0, index),
			fileName.substring(index + 1));
	}

	private boolean _isFileSizeLimitReached() {
		if (_fileEncoder.getDataSize() >=
				_storageConfiguration.getChunkSize()) {

			return true;
		}

		return false;
	}

	private boolean _isGoogleStorageArchiverEnabled() {
		if ((_googleStorageArchiver == null) ||
			(_storageConfiguration.getGoogleBucket() == null)) {

			return false;
		}

		return true;
	}

	private void _open() throws Exception {
		_fileEncoder = new JSONFileEncoder(_storageConfiguration.getPath());

		_fileEncoder.open();
	}

	private void _renameFile(File srcFile, File targetFile) throws Exception {
		if (_log.isInfoEnabled()) {
			_log.info(
				String.format("Renaming file %s to %s ", srcFile, targetFile));
		}

		boolean result = srcFile.renameTo(targetFile);

		if (!result) {
			throw new IOException(
				String.format(
					"Unable to rename file %s to %s", srcFile, targetFile));
		}
	}

	private void _rollover() throws Exception {
		_fileEncoder.close();

		File targetFile = new File(
			_storageConfiguration.getPath() + "." + System.currentTimeMillis());

		_deleteFile(targetFile);

		_renameFile(new File(_storageConfiguration.getPath()), targetFile);

		_archiveFile(targetFile);

		_open();
	}

	private static final Log _log = LogFactory.getLog(LocalStorage.class);

	private FileEncoder _fileEncoder;
	private GoogleStorageArchiver _googleStorageArchiver;
	private final StorageConfiguration _storageConfiguration;

}