/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.process;

import java.io.Serializable;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ProcessExecutorUtil {

	public static <T extends Serializable> ProcessChannel<T> execute(
			ProcessConfig processConfig, ProcessCallable<T> processCallable)
		throws ProcessException {

		return _processExecutor.execute(processConfig, processCallable);
	}

	public void setProcessExecutor(ProcessExecutor processExecutor) {
		_processExecutor = processExecutor;
	}

	private static ProcessExecutor _processExecutor;

}