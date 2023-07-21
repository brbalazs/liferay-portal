/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.service.persistence.impl;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTaskInstanceTokenPersistence;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class KaleoTaskInstanceTokenFinderBaseImpl
	extends BasePersistenceImpl<KaleoTaskInstanceToken> {

	public KaleoTaskInstanceTokenFinderBaseImpl() {
		setModelClass(KaleoTaskInstanceToken.class);
	}

	/**
	 * Returns the kaleo task instance token persistence.
	 *
	 * @return the kaleo task instance token persistence
	 */
	public KaleoTaskInstanceTokenPersistence
		getKaleoTaskInstanceTokenPersistence() {

		return kaleoTaskInstanceTokenPersistence;
	}

	/**
	 * Sets the kaleo task instance token persistence.
	 *
	 * @param kaleoTaskInstanceTokenPersistence the kaleo task instance token persistence
	 */
	public void setKaleoTaskInstanceTokenPersistence(
		KaleoTaskInstanceTokenPersistence kaleoTaskInstanceTokenPersistence) {

		this.kaleoTaskInstanceTokenPersistence =
			kaleoTaskInstanceTokenPersistence;
	}

	@BeanReference(type = KaleoTaskInstanceTokenPersistence.class)
	protected KaleoTaskInstanceTokenPersistence
		kaleoTaskInstanceTokenPersistence;

}