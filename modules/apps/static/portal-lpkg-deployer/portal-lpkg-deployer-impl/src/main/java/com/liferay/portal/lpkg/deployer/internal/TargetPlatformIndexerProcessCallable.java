/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.lpkg.deployer.internal;

import com.liferay.petra.process.ProcessCallable;
import com.liferay.petra.process.ProcessException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.target.platform.indexer.internal.TargetPlatformIndexerUtil;

import java.io.File;

import java.util.List;

/**
 * @author Shuyang Zhou
 */
public class TargetPlatformIndexerProcessCallable
	implements ProcessCallable<byte[]> {

	public TargetPlatformIndexerProcessCallable(
		List<File> additionalJarFiles, long stopWaitTimeout,
		String... dirNames) {

		_additionalJarFiles = additionalJarFiles;
		_stopWaitTimeout = stopWaitTimeout;
		_dirNames = dirNames;
	}

	@Override
	public byte[] call() throws ProcessException {
		try {
			UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream();

			TargetPlatformIndexerUtil.indexTargetPlatform(
				unsyncByteArrayOutputStream, _additionalJarFiles,
				_stopWaitTimeout, _dirNames);

			return unsyncByteArrayOutputStream.toByteArray();
		}
		catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	@Override
	public String toString() {
		return "Target Platform Indexer";
	}

	private static final long serialVersionUID = 1L;

	private final List<File> _additionalJarFiles;
	private final String[] _dirNames;
	private final long _stopWaitTimeout;

}