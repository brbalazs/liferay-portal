/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.service.persistence.impl;

import com.liferay.osb.faro.model.FaroChannel;
import com.liferay.osb.faro.service.persistence.FaroChannelPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

/**
 * @author Matthew Kong
 * @generated
 */
public class FaroChannelFinderBaseImpl
	extends BasePersistenceImpl<FaroChannel> {

	public FaroChannelFinderBaseImpl() {
		setModelClass(FaroChannel.class);
	}

	/**
	 * Returns the faro channel persistence.
	 *
	 * @return the faro channel persistence
	 */
	public FaroChannelPersistence getFaroChannelPersistence() {
		return faroChannelPersistence;
	}

	/**
	 * Sets the faro channel persistence.
	 *
	 * @param faroChannelPersistence the faro channel persistence
	 */
	public void setFaroChannelPersistence(
		FaroChannelPersistence faroChannelPersistence) {

		this.faroChannelPersistence = faroChannelPersistence;
	}

	@BeanReference(type = FaroChannelPersistence.class)
	protected FaroChannelPersistence faroChannelPersistence;

}