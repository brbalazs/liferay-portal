/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.ratings.transformer.bundle.ratingsdatatransformerutil;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery.PerformActionMethod;
import com.liferay.ratings.kernel.RatingsType;
import com.liferay.ratings.kernel.model.RatingsEntry;
import com.liferay.ratings.kernel.transformer.RatingsDataTransformer;

import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = RatingsDataTransformer.class
)
public class TestRatingsDataTransformer implements RatingsDataTransformer {

	@Override
	public PerformActionMethod<RatingsEntry> transformRatingsData(
		RatingsType fromRatingsType, RatingsType toRatingsType) {

		_atomicBoolean.set(Boolean.TRUE);

		return null;
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}