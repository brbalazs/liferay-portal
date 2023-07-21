/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.process;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Shuyang Zhou
 */
public class CollectorOutputProcessor
	implements OutputProcessor<byte[], byte[]> {

	public static final OutputProcessor<byte[], byte[]> INSTANCE =
		new CollectorOutputProcessor();

	@Override
	public byte[] processStdErr(InputStream stdErrInputStream)
		throws ProcessException {

		return _collect(stdErrInputStream);
	}

	@Override
	public byte[] processStdOut(InputStream stdOutInputStream)
		throws ProcessException {

		return _collect(stdOutInputStream);
	}

	private byte[] _collect(InputStream inputStream) throws ProcessException {
		UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
			new UnsyncByteArrayOutputStream();

		try {
			StreamUtil.transfer(inputStream, unsyncByteArrayOutputStream);
		}
		catch (IOException ioe) {
			throw new ProcessException(ioe);
		}

		return unsyncByteArrayOutputStream.toByteArray();
	}

}