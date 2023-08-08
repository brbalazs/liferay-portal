/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author André Miranda
 */
public class URLUtilTest {

	@Test
	public void testDecode() {
		Assertions.assertEquals(
			"http://www.google.com/?q=\"hsR=0'hsR={{31337*31337}}>&lt;hsR>",
			URLUtil.decode(
				"http://www.google.com/%3Fq=%22hsR=0'hsR=%7B%7B31337*31337%7D" +
					"%7D%3E&lt;hsR%3E"));
	}

	@Test
	public void testToURI() throws URISyntaxException {
		URI uri = URLUtil.toURI("https://liferay.com/foo?q=escaping em ação!");

		Assertions.assertEquals("/foo?q=escaping em ação!", uri.getPath());
		Assertions.assertEquals("liferay.com", uri.getHost());
	}

}