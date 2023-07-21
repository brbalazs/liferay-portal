/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.ModelResourceActionsBag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author     László Csontos
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ModelResourceActionsBagImpl
	extends ResourceActionsBagImpl implements ModelResourceActionsBag {

	public ModelResourceActionsBagImpl() {
	}

	public ModelResourceActionsBagImpl(
		ModelResourceActionsBag modelResourceActionsBag) {

		super(modelResourceActionsBag);

		_resourceOwnerDefaultActions.addAll(
			modelResourceActionsBag.getResourceOwnerDefaultActions());
		_resourceWeights.putAll(modelResourceActionsBag.getResourceWeights());
	}

	@Override
	public ModelResourceActionsBag clone() {
		return new ModelResourceActionsBagImpl(this);
	}

	@Override
	public Set<String> getResourceOwnerDefaultActions() {
		return _resourceOwnerDefaultActions;
	}

	@Override
	public Map<String, Double> getResourceWeights() {
		return _resourceWeights;
	}

	private final Set<String> _resourceOwnerDefaultActions = new HashSet<>();
	private final Map<String, Double> _resourceWeights = new HashMap<>();

}