/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.ResourceActionsBag;

import java.util.HashSet;
import java.util.Set;

/**
 * @author     László Csontos
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ResourceActionsBagImpl implements Cloneable, ResourceActionsBag {

	public ResourceActionsBagImpl() {
	}

	public ResourceActionsBagImpl(ResourceActionsBag resourceActionsBag) {
		_resourceActions.addAll(resourceActionsBag.getResourceActions());
		_resourceGroupDefaultActions.addAll(
			resourceActionsBag.getResourceGroupDefaultActions());
		_resourceGuestDefaultActions.addAll(
			resourceActionsBag.getResourceGuestDefaultActions());
		_resourceGuestUnsupportedActions.addAll(
			resourceActionsBag.getResourceGuestUnsupportedActions());
		_resources.addAll(resourceActionsBag.getResources());
	}

	@Override
	public ResourceActionsBag clone() {
		return new ResourceActionsBagImpl(this);
	}

	@Override
	public Set<String> getResourceActions() {
		return _resourceActions;
	}

	@Override
	public Set<String> getResourceGroupDefaultActions() {
		return _resourceGroupDefaultActions;
	}

	@Override
	public Set<String> getResourceGuestDefaultActions() {
		return _resourceGuestDefaultActions;
	}

	@Override
	public Set<String> getResourceGuestUnsupportedActions() {
		return _resourceGuestUnsupportedActions;
	}

	@Override
	public Set<String> getResources() {
		return _resources;
	}

	private final Set<String> _resourceActions = new HashSet<>();
	private final Set<String> _resourceGroupDefaultActions = new HashSet<>();
	private final Set<String> _resourceGuestDefaultActions = new HashSet<>();
	private final Set<String> _resourceGuestUnsupportedActions =
		new HashSet<>();
	private final Set<String> _resources = new HashSet<>();

}