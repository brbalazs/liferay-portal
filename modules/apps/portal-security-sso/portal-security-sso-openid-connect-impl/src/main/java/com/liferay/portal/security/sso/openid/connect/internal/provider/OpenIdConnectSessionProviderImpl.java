/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.internal.provider;

import com.liferay.petra.io.Deserializer;
import com.liferay.petra.io.Serializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectSession;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;
import com.liferay.portal.security.sso.openid.connect.provider.OpenIdConnectSessionProvider;

import java.io.Serializable;

import java.nio.ByteBuffer;

import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;

/**
 * @author Istvan Sajtos
 */
@Component(
	service = {
		OpenIdConnectSessionProvider.class,
		OpenIdConnectSessionProviderImpl.class
	}
)
public class OpenIdConnectSessionProviderImpl
	implements OpenIdConnectSessionProvider {

	public static void setOpenIdConnectSession(
		HttpSession httpSession, OpenIdConnectSession openIdConnectSession) {

		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION,
			_getData((Serializable)openIdConnectSession));
	}

	public OpenIdConnectSession getOpenIdConnectSession(
		HttpSession httpSession) {

		byte[] data = (byte[])httpSession.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION);

		if (data == null) {
			return null;
		}

		return (OpenIdConnectSession)_getSerializable(data);
	}

	private static byte[] _getData(Serializable serializable) {
		Serializer serializer = new Serializer();

		serializer.writeObject(serializable);

		ByteBuffer byteBuffer = serializer.toByteBuffer();

		return byteBuffer.array();
	}

	private Serializable _getSerializable(byte[] data) {
		Deserializer deserializer = new Deserializer(ByteBuffer.wrap(data));

		try {
			return deserializer.readObject();
		}
		catch (ClassNotFoundException classNotFoundException) {
			_log.error("Unable to deserialize object", classNotFoundException);

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectSessionProviderImpl.class);

}