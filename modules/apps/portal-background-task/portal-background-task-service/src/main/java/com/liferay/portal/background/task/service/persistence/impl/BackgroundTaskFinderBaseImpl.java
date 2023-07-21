/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.background.task.service.persistence.impl;

import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.persistence.BackgroundTaskPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class BackgroundTaskFinderBaseImpl
	extends BasePersistenceImpl<BackgroundTask> {

	public BackgroundTaskFinderBaseImpl() {
		setModelClass(BackgroundTask.class);
	}

	/**
	 * Returns the background task persistence.
	 *
	 * @return the background task persistence
	 */
	public BackgroundTaskPersistence getBackgroundTaskPersistence() {
		return backgroundTaskPersistence;
	}

	/**
	 * Sets the background task persistence.
	 *
	 * @param backgroundTaskPersistence the background task persistence
	 */
	public void setBackgroundTaskPersistence(
		BackgroundTaskPersistence backgroundTaskPersistence) {

		this.backgroundTaskPersistence = backgroundTaskPersistence;
	}

	@BeanReference(type = BackgroundTaskPersistence.class)
	protected BackgroundTaskPersistence backgroundTaskPersistence;

}