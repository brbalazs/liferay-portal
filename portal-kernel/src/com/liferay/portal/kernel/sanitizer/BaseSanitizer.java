/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.sanitizer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.util.Map;

/**
 * @author Zsolt Balogh
 * @author Brian Wing Shun Chan
 */
@SuppressWarnings("deprecation")
public abstract class BaseSanitizer implements Sanitizer {

	@Override
	public byte[] sanitize(
			long companyId, long groupId, long userId, String className,
			long classPK, String contentType, String[] modes, byte[] bytes,
			Map<String, Object> options)
		throws SanitizerException {

		if (bytes == null) {
			return null;
		}

		try {
			String content = new String(bytes, StringPool.UTF8);

			String result = sanitize(
				companyId, groupId, userId, className, classPK, contentType,
				modes, content, options);

			return result.getBytes(StringPool.UTF8);
		}
		catch (UnsupportedEncodingException uee) {
			throw new SanitizerException(uee);
		}
	}

	@Override
	public void sanitize(
			long companyId, long groupId, long userId, String className,
			long classPK, String contentType, String[] modes,
			InputStream inputStream, OutputStream outputStream,
			Map<String, Object> options)
		throws SanitizerException {

		if ((inputStream == null) || (outputStream == null)) {
			return;
		}

		ByteArrayOutputStream byteArrayOutputStream =
			new ByteArrayOutputStream();

		try {
			StreamUtil.transfer(inputStream, byteArrayOutputStream);

			String content = new String(
				byteArrayOutputStream.toByteArray(), StringPool.UTF8);

			String result = sanitize(
				companyId, groupId, userId, className, classPK, contentType,
				modes, content, options);

			byte[] bytes = result.getBytes(StringPool.UTF8);

			outputStream.write(bytes);
		}
		catch (IOException ioe) {
			throw new SanitizerException(ioe);
		}
	}

	@Override
	public abstract String sanitize(
			long companyId, long groupId, long userId, String className,
			long classPK, String contentType, String[] modes, String content,
			Map<String, Object> options)
		throws SanitizerException;

}