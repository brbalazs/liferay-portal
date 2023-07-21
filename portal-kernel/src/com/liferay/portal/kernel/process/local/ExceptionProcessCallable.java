/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.process.local;

import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.ProcessException;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ExceptionProcessCallable
	implements ProcessCallable<ProcessException> {

	public ExceptionProcessCallable(ProcessException processException) {
		_processException = processException;
	}

	@Override
	public ProcessException call() throws ProcessException {
		throw _processException;
	}

	private static final long serialVersionUID = 1L;

	private final ProcessException _processException;

}