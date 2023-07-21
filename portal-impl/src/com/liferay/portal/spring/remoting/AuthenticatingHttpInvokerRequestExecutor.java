/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.remoting;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PwdEncryptorException;
import com.liferay.portal.kernel.security.pwd.PasswordEncryptorUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.IOException;

import java.net.HttpURLConnection;

import org.apache.commons.codec.binary.Base64;

import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;

/**
 * <p>
 * An HttpInvoker request executor for Spring Remoting that provides HTTP BASIC
 * authentication information for service invocations.
 * </p>
 *
 * @author Joel Kozikowski
 */
public class AuthenticatingHttpInvokerRequestExecutor
	extends SimpleHttpInvokerRequestExecutor {

	public String getPassword() {
		return _password;
	}

	public long getUserId() {
		return _userId;
	}

	public void setPassword(String password) throws PwdEncryptorException {
		_password = PasswordEncryptorUtil.encrypt(password);
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	/**
	 * Called every time a HTTP invocation is made. This implementation allows
	 * the parent to setup the connection, and then adds an
	 * <code>Authorization</code> HTTP header property for BASIC authentication.
	 */
	@Override
	protected void prepareConnection(HttpURLConnection con, int contentLength)
		throws IOException {

		super.prepareConnection(con, contentLength);

		if (getUserId() > 0) {
			String password = GetterUtil.getString(getPassword());

			String base64 = getUserId() + StringPool.COLON + password;

			con.setRequestProperty(
				"Authorization",
				"Basic " + new String(Base64.encodeBase64(base64.getBytes())));
		}
	}

	private String _password;
	private long _userId;

}