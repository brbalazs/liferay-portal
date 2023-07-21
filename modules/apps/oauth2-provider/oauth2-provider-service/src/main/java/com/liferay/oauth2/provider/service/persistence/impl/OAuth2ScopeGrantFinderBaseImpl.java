/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.service.persistence.impl;

import com.liferay.oauth2.provider.model.OAuth2ScopeGrant;
import com.liferay.oauth2.provider.service.persistence.OAuth2ScopeGrantPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class OAuth2ScopeGrantFinderBaseImpl
	extends BasePersistenceImpl<OAuth2ScopeGrant> {

	public OAuth2ScopeGrantFinderBaseImpl() {
		setModelClass(OAuth2ScopeGrant.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put(
			"oAuth2ApplicationScopeAliasesId", "oA2AScopeAliasesId");

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
				"_dbColumnNames");

			field.setAccessible(true);

			field.set(this, dbColumnNames);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}
	}

	@Override
	public Set<String> getBadColumnNames() {
		return getOAuth2ScopeGrantPersistence().getBadColumnNames();
	}

	/**
	 * Returns the o auth2 scope grant persistence.
	 *
	 * @return the o auth2 scope grant persistence
	 */
	public OAuth2ScopeGrantPersistence getOAuth2ScopeGrantPersistence() {
		return oAuth2ScopeGrantPersistence;
	}

	/**
	 * Sets the o auth2 scope grant persistence.
	 *
	 * @param oAuth2ScopeGrantPersistence the o auth2 scope grant persistence
	 */
	public void setOAuth2ScopeGrantPersistence(
		OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence) {

		this.oAuth2ScopeGrantPersistence = oAuth2ScopeGrantPersistence;
	}

	@BeanReference(type = OAuth2ScopeGrantPersistence.class)
	protected OAuth2ScopeGrantPersistence oAuth2ScopeGrantPersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		OAuth2ScopeGrantFinderBaseImpl.class);

}