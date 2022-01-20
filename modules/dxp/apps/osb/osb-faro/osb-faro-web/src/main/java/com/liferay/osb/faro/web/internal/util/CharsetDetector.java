/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * @author Alejo Ceballos
 */
public class CharsetDetector {

	public Charset detect(File file) throws IOException {
		UniversalDetector detector = new UniversalDetector();

		try (InputStream fileInputStream = new FileInputStream(file)) {
			byte[] bytes = new byte[4096];
			int bytesLength;

			while (((bytesLength = fileInputStream.read(bytes)) > 0) &&
				   !detector.isDone()) {

				detector.handleData(bytes, 0, bytesLength);
			}

			detector.dataEnd();

			String charsetString = detector.getDetectedCharset();

			try {
				return Charset.forName(charsetString);
			}
			catch (IllegalArgumentException illegalArgumentException) {
				return null;
			}
		}
		finally {
			detector.reset();
		}
	}

}