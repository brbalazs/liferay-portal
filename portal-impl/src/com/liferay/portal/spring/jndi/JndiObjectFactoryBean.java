/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.jndi;

import com.liferay.portal.kernel.jndi.JNDIUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Brian Wing Shun Chan
 */
public class JndiObjectFactoryBean
	extends org.springframework.jndi.JndiObjectFactoryBean {

	@Override
	protected Object lookup() {
		try {
			Properties properties = PropsUtil.getProperties(
				PropsKeys.JNDI_ENVIRONMENT, true);

			Context context = new InitialContext(properties);

			return JNDIUtil.lookup(context, getJndiName());
		}
		catch (Exception e) {
			_log.error("Unable to lookup " + getJndiName());

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JndiObjectFactoryBean.class);

}