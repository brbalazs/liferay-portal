/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mail.util;

import com.liferay.portal.kernel.jndi.JNDIUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsUtil;

import java.util.Properties;

import javax.mail.Session;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * @author Brian Wing Shun Chan
 */
public class MailSessionFactoryBean extends AbstractFactoryBean<Session> {

	@Override
	public Class<Session> getObjectType() {
		return Session.class;
	}

	public void setPropertyPrefix(String propertyPrefix) {
		_propertyPrefix = propertyPrefix;
	}

	@Override
	protected Session createInstance() throws Exception {
		Properties properties = PropsUtil.getProperties(_propertyPrefix, true);

		String jndiName = properties.getProperty("jndi.name");

		if (Validator.isNotNull(jndiName)) {
			try {
				Properties jndiEnvironmentProperties = PropsUtil.getProperties(
					PropsKeys.JNDI_ENVIRONMENT, true);

				Context context = new InitialContext(jndiEnvironmentProperties);

				return (Session)JNDIUtil.lookup(context, jndiName);
			}
			catch (Exception e) {
				_log.error("Unable to lookup " + jndiName, e);
			}
		}

		Session session = Session.getInstance(properties);

		if (_log.isDebugEnabled()) {
			session.setDebug(true);

			SortedProperties sortedProperties = new SortedProperties(
				session.getProperties());

			_log.debug("Properties for prefix " + _propertyPrefix);

			sortedProperties.list(System.out);
		}

		return session;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MailSessionFactoryBean.class);

	private String _propertyPrefix;

}