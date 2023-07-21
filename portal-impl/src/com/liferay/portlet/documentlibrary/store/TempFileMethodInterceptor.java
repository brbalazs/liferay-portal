/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.documentlibrary.store;

import com.liferay.petra.memory.DeleteFileFinalizeAction;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.portal.kernel.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author     Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class TempFileMethodInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object result = methodInvocation.proceed();

		if (result instanceof InputStream) {
			InputStream inputStream = (InputStream)result;

			File tempFile = FileUtil.createTempFile(inputStream);

			result = new FileInputStream(tempFile);

			FinalizeManager.register(
				result,
				new DeleteFileFinalizeAction(tempFile.getAbsolutePath()),
				FinalizeManager.PHANTOM_REFERENCE_FACTORY);
		}

		return result;
	}

}