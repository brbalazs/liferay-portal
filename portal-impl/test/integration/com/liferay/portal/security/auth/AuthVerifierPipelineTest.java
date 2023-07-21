/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Peter Fellwock
 */
public class AuthVerifierPipelineTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.authverifierpipeline"));

	@Test
	public void testVerifyRequest() throws PortalException {
		AccessControlContext accessControlContext = new AccessControlContext();

		MockHttpServletRequest mockHttpServletRequest = createHttpRequest(
			"/foo/hello");

		mockHttpServletRequest.setAttribute(
			WebKeys.COMPANY_ID, TestPropsValues.getCompanyId());

		accessControlContext.setRequest(mockHttpServletRequest);

		AuthVerifierPipeline.verifyRequest(accessControlContext);
	}

	protected MockHttpServletRequest createHttpRequest(String pathInfo) {
		MockServletContext mockServletContext = new MockServletContext();

		mockServletContext.setContextPath(StringPool.BLANK);
		mockServletContext.setServletContextName(StringPool.BLANK);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(mockServletContext);

		mockHttpServletRequest.setMethod(HttpMethods.GET);
		mockHttpServletRequest.setPathInfo(pathInfo);

		return mockHttpServletRequest;
	}

}