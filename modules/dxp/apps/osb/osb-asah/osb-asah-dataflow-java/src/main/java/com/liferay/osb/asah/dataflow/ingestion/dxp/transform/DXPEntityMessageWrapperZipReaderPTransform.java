/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.util.Objects;

import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.MatchResult;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcellus Tavares
 */
public class DXPEntityMessageWrapperZipReaderPTransform
	extends PTransform<PBegin, PCollection<DXPEntityMessageWrapper>> {

	public DXPEntityMessageWrapperZipReaderPTransform(
		String filePattern, String resourceNameFilter) {

		_filePattern = filePattern;
		_resourceNameFilter = resourceNameFilter;
	}

	@Override
	public PCollection<DXPEntityMessageWrapper> expand(PBegin pBegin) {
		return pBegin.apply(
			"Read GCS Zip File ",
			FileIO.match(
			).filepattern(
				_filePattern
			)
		).apply(
			FileIO.readMatches(
			).withCompression(
				Compression.ZIP
			)
		).apply(
			"Read Zip File", ParDo.of(new ZipFileReader())
		);
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		DXPEntityMessageWrapperZipReaderPTransform.class);

	private final String _filePattern;
	private final String _resourceNameFilter;

	private class ZipFileReader
		extends DoFn<FileIO.ReadableFile, DXPEntityMessageWrapper> {

		@ProcessElement
		public void processElement(ProcessContext processContext) {
			FileIO.ReadableFile readableFile = processContext.element();

			MatchResult.Metadata metadata = readableFile.getMetadata();

			ResourceId resourceId = metadata.resourceId();

			String string = resourceId.toString();

			String[] split = string.split("/");

			int length = split.length;

			String resourceName = split[length - 4];

			if (!Objects.equals(_resourceNameFilter, resourceName)) {
				if (_logger.isInfoEnabled()) {
					_logger.error(
						"Skipping resource {} because it does not match with " +
							"resource name filter {}",
						resourceId.getFilename(), _resourceNameFilter);
				}

				return;
			}

			String dataSourceId = split[length - 5];
			String projectId = split[length - 6];
			String uploadTime = split[length - 2];
			String uploadType = split[length - 3];

			try {
				Compression compression = readableFile.getCompression();

				ReadableByteChannel readableByteChannel =
					compression.readDecompressed(FileSystems.open(resourceId));

				try (BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(
							Channels.newInputStream(readableByteChannel)))) {

					String line = null;

					while ((line = bufferedReader.readLine()) != null) {
						DXPEntityMessageWrapper dxpEntityMessageWrapper =
							new DXPEntityMessageWrapper();

						dxpEntityMessageWrapper.dataSourceId = dataSourceId;
						dxpEntityMessageWrapper.payload = line;
						dxpEntityMessageWrapper.projectId = projectId;
						dxpEntityMessageWrapper.resourceName = resourceName;
						dxpEntityMessageWrapper.uploadTime = uploadTime;
						dxpEntityMessageWrapper.uploadType = uploadType;

						processContext.output(dxpEntityMessageWrapper);
					}
				}
			}
			catch (Exception exception) {
				_logger.error(
					"Unable to read file: {}", resourceId.getFilename());
			}
		}

	}

}