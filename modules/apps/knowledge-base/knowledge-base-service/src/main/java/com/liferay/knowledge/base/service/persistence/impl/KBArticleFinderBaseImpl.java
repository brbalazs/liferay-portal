/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.knowledge.base.service.persistence.impl;

import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.service.persistence.KBArticlePersistence;
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
public class KBArticleFinderBaseImpl extends BasePersistenceImpl<KBArticle> {

	public KBArticleFinderBaseImpl() {
		setModelClass(KBArticle.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");

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
		return getKBArticlePersistence().getBadColumnNames();
	}

	/**
	 * Returns the kb article persistence.
	 *
	 * @return the kb article persistence
	 */
	public KBArticlePersistence getKBArticlePersistence() {
		return kbArticlePersistence;
	}

	/**
	 * Sets the kb article persistence.
	 *
	 * @param kbArticlePersistence the kb article persistence
	 */
	public void setKBArticlePersistence(
		KBArticlePersistence kbArticlePersistence) {

		this.kbArticlePersistence = kbArticlePersistence;
	}

	@BeanReference(type = KBArticlePersistence.class)
	protected KBArticlePersistence kbArticlePersistence;

	private static final Log _log = LogFactoryUtil.getLog(
		KBArticleFinderBaseImpl.class);

}