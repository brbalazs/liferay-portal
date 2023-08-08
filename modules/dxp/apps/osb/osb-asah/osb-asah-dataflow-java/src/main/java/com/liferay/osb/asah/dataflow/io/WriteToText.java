/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.io;

import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.fs.ResolveOptions;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author Marcellus Tavares
 */
public class WriteToText {

	public static class WriteOneFilePerWindow
		extends PTransform<PCollection<String>, PDone> {

		public WriteOneFilePerWindow(
			String fileNamePrefix, String outputDirectory) {

			_fileNamePrefix = fileNamePrefix;
			_outputDirectory = outputDirectory;
		}

		@Override
		public PDone expand(PCollection<String> pCollection) {
			ResourceId resource = FileBasedSink.convertToFileResourceIfPossible(
				_outputDirectory + "/" + _fileNamePrefix);

			return pCollection.apply(
				TextIO.write(
				).withNumShards(
					1
				).withTempDirectory(
					resource.getCurrentDirectory()
				).withWindowedWrites(
				).to(
					new PerWindowFiles(resource)
				));
		}

		private final String _fileNamePrefix;
		private final String _outputDirectory;

	}

	protected static class PerWindowFiles extends FileBasedSink.FilenamePolicy {

		public PerWindowFiles(ResourceId resourceId) {
			_resourceId = resourceId;
		}

		@Override
		public ResourceId unwindowedFilename(
			int shardNumber, int numShards,
			FileBasedSink.OutputFileHints outputFileHints) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ResourceId windowedFilename(
			int shardNumber, int numShards, BoundedWindow boundedWindow,
			PaneInfo paneInfo, FileBasedSink.OutputFileHints outputFileHints) {

			ResourceId resourceId = _resourceId.getCurrentDirectory();

			IntervalWindow intervalWindow = (IntervalWindow)boundedWindow;

			return resourceId.resolve(
				String.format(
					"%s/%s%s",
					_dateDateTimeFormatter.print(intervalWindow.start()),
					_getFileNameForWindow(intervalWindow),
					outputFileHints.getSuggestedFilenameSuffix()),
				ResolveOptions.StandardResolveOptions.RESOLVE_FILE);
		}

		private String _getFileNameForWindow(IntervalWindow intervalWindow) {
			String filePrefix = "";

			if (!_resourceId.isDirectory()) {
				filePrefix = _resourceId.getFilename();
			}

			return String.format(
				"%s-%s-to-%s", filePrefix,
				_timeDateTimeFormatter.print(intervalWindow.start()),
				_timeDateTimeFormatter.print(intervalWindow.end()));
		}

		private static final DateTimeFormatter _dateDateTimeFormatter =
			ISODateTimeFormat.date(
			).withZoneUTC();
		private static final DateTimeFormatter _timeDateTimeFormatter =
			ISODateTimeFormat.hourMinute(
			).withZoneUTC();

		private final ResourceId _resourceId;

	}

}