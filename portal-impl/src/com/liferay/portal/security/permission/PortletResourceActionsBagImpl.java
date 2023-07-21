/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.security.permission.PortletResourceActionsBag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author     László Csontos
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PortletResourceActionsBagImpl
	extends ResourceActionsBagImpl implements PortletResourceActionsBag {

	public PortletResourceActionsBagImpl() {
	}

	public PortletResourceActionsBagImpl(
		PortletResourceActionsBag portletResourceActionsBag) {

		super(portletResourceActionsBag);

		_portletRootModelResources.putAll(
			portletResourceActionsBag.getPortletRootModelResources());
		_resourceLayoutManagerActions.addAll(
			portletResourceActionsBag.getResourceLayoutManagerActions());
	}

	@Override
	public PortletResourceActionsBag clone() {
		return new PortletResourceActionsBagImpl(this);
	}

	@Override
	public Map<String, String> getPortletRootModelResources() {
		return _portletRootModelResources;
	}

	@Override
	public Set<String> getResourceLayoutManagerActions() {
		return _resourceLayoutManagerActions;
	}

	private final Map<String, String> _portletRootModelResources =
		new HashMap<>();
	private final Set<String> _resourceLayoutManagerActions = new HashSet<>();

}