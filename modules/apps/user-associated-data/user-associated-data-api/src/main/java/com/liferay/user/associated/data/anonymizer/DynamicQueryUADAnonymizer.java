/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.anonymizer;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.user.associated.data.util.UADDynamicQueryUtil;

/**
 * The base implementation of {@link UADAnonymizer} for entities created using
 * ServiceBuilder. The count and batch actions are based on
 * {@link ActionableDynamicQuery}, which is available in the service for the
 * entity of type {@code T}.
 *
 * @author William Newbury
 * @review
 */
public abstract class DynamicQueryUADAnonymizer<T extends BaseModel>
	implements UADAnonymizer<T> {

	@Override
	public void autoAnonymizeAll(long userId, User anonymousUser)
		throws PortalException {

		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		actionableDynamicQuery.setPerformActionMethod(
			(T baseModel) -> autoAnonymize(baseModel, userId, anonymousUser));

		actionableDynamicQuery.performActions();
	}

	@Override
	public long count(long userId) throws PortalException {
		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		return actionableDynamicQuery.performCount();
	}

	@Override
	public void deleteAll(long userId) throws PortalException {
		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		actionableDynamicQuery.setPerformActionMethod(
			(T baseModel) -> delete(baseModel));

		actionableDynamicQuery.performActions();
	}

	/**
	 * Returns an {@link ActionableDynamicQuery} for type {@code T}. This can be
	 * retrieved from the service.
	 *
	 * @return an {@link ActionableDynamicQuery} for type {@code T}
	 * @review
	 */
	protected abstract ActionableDynamicQuery doGetActionableDynamicQuery();

	/**
	 * Returns the names identifying fields on the entity of type {@code T} that
	 * contain the primary key of a user.
	 *
	 * @return fields that may contain the primary key of a user
	 * @review
	 */
	protected abstract String[] doGetUserIdFieldNames();

	/**
	 * Returns an {@link ActionableDynamicQuery} for type {@code T}. It should
	 * be populated with criterion and ready for use by the service.
	 *
	 * @param userId the primary key of the user to pre-filter the
	 * 				 {@link ActionableDynamicQuery}
	 * @return a pre-filtered {@link ActionableDynamicQuery}
	 * @review
	 */
	protected ActionableDynamicQuery getActionableDynamicQuery(long userId) {
		return UADDynamicQueryUtil.addActionableDynamicQueryCriteria(
			doGetActionableDynamicQuery(), doGetUserIdFieldNames(), userId);
	}

}