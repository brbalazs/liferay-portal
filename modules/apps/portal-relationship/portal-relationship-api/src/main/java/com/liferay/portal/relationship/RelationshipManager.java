/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.relationship;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.ClassedModel;

import java.util.Collection;

/**
 * @author Máté Thurzó
 */
@ProviderType
public interface RelationshipManager {

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getInboundRelatedModels(Class<T> modelClass, long primKey);

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getInboundRelatedModels(
			Class<T> modelClass, long primKey, Degree degree);

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getOutboundRelatedModels(Class<T> modelClass, long primKey);

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getOutboundRelatedModels(
			Class<T> modelClass, long primKey, Degree degree);

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getRelatedModels(Class<T> modelClass, long primKey);

	public <T extends ClassedModel> Collection<? extends ClassedModel>
		getRelatedModels(Class<T> modelClass, long primKey, Degree degree);

}