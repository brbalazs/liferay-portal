/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.bundle.friendlyurlmappertrackerimpl;

import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.Router;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=FriendlyURLMapperTrackerImplTest",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = FriendlyURLMapper.class
)
public class TestFriendlyURLMapper implements FriendlyURLMapper {

	@Override
	public String buildPath(LiferayPortletURL liferayPortletURL) {
		return null;
	}

	@Override
	public String getMapping() {
		return null;
	}

	@Override
	public String getPortletId() {
		return null;
	}

	@Override
	public Router getRouter() {
		return null;
	}

	@Override
	public boolean isCheckMappingWithPrefix() {
		return false;
	}

	@Override
	public boolean isPortletInstanceable() {
		return false;
	}

	@Override
	public void populateParams(
		String friendlyURLPath, Map<String, String[]> parameterMap,
		Map<String, Object> requestContext) {
	}

	@Override
	public void setMapping(String mapping) {
	}

	@Override
	public void setPortletId(String portletId) {
	}

	@Override
	public void setPortletInstanceable(boolean portletInstanceable) {
	}

	@Override
	public void setRouter(Router router) {
	}

}