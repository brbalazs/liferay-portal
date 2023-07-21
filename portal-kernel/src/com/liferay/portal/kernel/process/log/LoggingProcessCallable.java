/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.process.log;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.process.ProcessCallable;

import java.io.IOException;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class LoggingProcessCallable implements ProcessCallable<String> {

	public LoggingProcessCallable(byte[] bytes) {
		this(bytes, false);
	}

	public LoggingProcessCallable(byte[] bytes, boolean error) {
		_bytes = bytes;
		_error = error;
	}

	@Override
	public String call() {
		try {
			if (_error) {
				System.err.write(_bytes);
			}
			else {
				System.out.write(_bytes);
			}
		}
		catch (IOException ioe) {
			_log.error(
				"Unable to output log message: " + new String(_bytes), ioe);
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoggingProcessCallable.class);

	private static final long serialVersionUID = 1L;

	private final byte[] _bytes;
	private final boolean _error;

}