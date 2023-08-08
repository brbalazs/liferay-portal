/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import org.json.JSONObject;

/**
 * @author Marcellus Tavares
 */
public class ParquetFileEncoder implements FileEncoder {

	public ParquetFileEncoder(
		JSONAvroTransformer jsonAvroTransformer, String pathString,
		Schema schema) {

		_jsonAvroTransformer = jsonAvroTransformer;
		_pathString = pathString;
		_schema = schema;
	}

	@Override
	public void close() throws Exception {
		_parquetWriter.close();
	}

	@Override
	public void encode(String data) throws Exception {
		synchronized (this) {
			_parquetWriter.write(
				_jsonAvroTransformer.transform(new JSONObject(data), _schema));
		}
	}

	@Override
	public long getDataSize() {
		return _parquetWriter.getDataSize();
	}

	@Override
	public void open() throws Exception {
		AvroParquetWriter.Builder<GenericRecord> builder =
			AvroParquetWriter.builder(new Path(_pathString));

		builder.withCompressionCodec(CompressionCodecName.SNAPPY);
		builder.withSchema(_schema);
		builder.withWriteMode(ParquetFileWriter.Mode.OVERWRITE);

		_parquetWriter = builder.build();
	}

	private final JSONAvroTransformer _jsonAvroTransformer;
	private ParquetWriter<GenericRecord> _parquetWriter;
	private final String _pathString;
	private final Schema _schema;

}