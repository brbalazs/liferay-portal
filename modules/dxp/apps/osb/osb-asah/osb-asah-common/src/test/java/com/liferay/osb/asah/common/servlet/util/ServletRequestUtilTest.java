/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.servlet.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Ivica Cardic
 */
public class ServletRequestUtilTest {

	@Test
	public void testGetOriginalURL() {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.addHeader(
			"X-Forwarded-Host", "www.first-second.com");
		mockHttpServletRequest.addHeader("X-Forwarded-Proto", "https");

		String originalURL = ServletRequestUtil.getOriginalURL(
			mockHttpServletRequest);

		Assertions.assertEquals("https://www.first-second.com", originalURL);

		mockHttpServletRequest = new MockHttpServletRequest();

		mockHttpServletRequest.addHeader(
			"X-Forwarded-Host", "www.first second com");
		mockHttpServletRequest.addHeader("X-Forwarded-Proto", "https ");

		originalURL = ServletRequestUtil.getOriginalURL(mockHttpServletRequest);

		Assertions.assertEquals("https+://www.first+second+com", originalURL);
	}

}