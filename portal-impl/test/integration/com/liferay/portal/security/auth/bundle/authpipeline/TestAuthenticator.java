/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authpipeline;

import com.liferay.portal.kernel.security.auth.Authenticator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"key=auth.pipeline.pre", "service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = Authenticator.class
)
public class TestAuthenticator implements Authenticator {

	@Override
	public int authenticateByEmailAddress(
		long companyId, String emailAddress, String password,
		Map<String, String[]> headerMap, Map<String, String[]> parameterMap) {

		_atomicBoolean.set(Boolean.TRUE);

		return Authenticator.SUCCESS;
	}

	@Override
	public int authenticateByScreenName(
		long companyId, String screenName, String password,
		Map<String, String[]> headerMap, Map<String, String[]> parameterMap) {

		_atomicBoolean.set(Boolean.TRUE);

		return Authenticator.SUCCESS;
	}

	@Override
	public int authenticateByUserId(
		long companyId, long userId, String password,
		Map<String, String[]> headerMap, Map<String, String[]> parameterMap) {

		_atomicBoolean.set(Boolean.TRUE);

		return Authenticator.SUCCESS;
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}