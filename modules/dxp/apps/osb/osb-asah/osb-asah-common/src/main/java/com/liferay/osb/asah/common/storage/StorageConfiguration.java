/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage;

import org.apache.avro.Schema;

/**
 * @author Marcellus Tavares
 * @author Riccardo Ferrari
 */
public class StorageConfiguration {

	public static Builder builder() {
		return new Builder(null);
	}

	public static Builder builder(String path) {
		return new Builder(path);
	}

	public long getChunkSize() {
		return _chunkSize;
	}

	public FileFormat getFileFormat() {
		return _fileFormat;
	}

	public Schema getFileSchema() {
		return _fileSchema;
	}

	public String getGoogleBucket() {
		return _googleBucket;
	}

	public String getGoogleBucketFolder() {
		return _googleBucketFolder;
	}

	public String getPath() {
		return _path;
	}

	public static class Builder {

		public Builder(String path) {
			_storageConfiguration._path = path;
		}

		public StorageConfiguration build() {
			if (_storageConfiguration.getChunkSize() == 0) {
				_storageConfiguration._chunkSize = _DEFAULT_CHUNK_SIZE;
			}

			if ((_storageConfiguration._fileFormat ==
					FileFormat.SNAPPY_PARQUET) &&
				(_storageConfiguration._fileSchema == null)) {

				throw new IllegalStateException(
					"Schema is required for Parquet file format");
			}

			return _storageConfiguration;
		}

		public Builder chunkSize(long chunkSize) {
			_storageConfiguration._chunkSize = chunkSize;

			return this;
		}

		public Builder fileFormat(FileFormat fileFormat) {
			_storageConfiguration._fileFormat = fileFormat;

			return this;
		}

		public Builder fileSchema(Schema fileSchema) {
			_storageConfiguration._fileSchema = fileSchema;

			return this;
		}

		public Builder googleBucket(String googleBucket) {
			if (googleBucket == null) {
				throw new IllegalArgumentException("Google bucket is null");
			}

			_storageConfiguration._googleBucket = googleBucket;

			return this;
		}

		public Builder googleBucketFolder(String googleBucketFolder) {
			_storageConfiguration._googleBucketFolder = googleBucketFolder;

			return this;
		}

		private static final long _DEFAULT_CHUNK_SIZE = 64 * 1024 * 1024;

		private final StorageConfiguration _storageConfiguration =
			new StorageConfiguration();

	}

	public enum FileFormat {

		JSON, SNAPPY_PARQUET

	}

	private StorageConfiguration() {
	}

	private long _chunkSize;
	private FileFormat _fileFormat = FileFormat.JSON;
	private Schema _fileSchema;
	private String _googleBucket;
	private String _googleBucketFolder;
	private String _path;

}