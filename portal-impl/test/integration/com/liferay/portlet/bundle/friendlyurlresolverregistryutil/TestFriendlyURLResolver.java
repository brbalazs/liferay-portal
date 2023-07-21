/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.bundle.friendlyurlresolverregistryutil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutFriendlyURLComposite;
import com.liferay.portal.kernel.portlet.FriendlyURLResolver;

import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Raymond Augé
 */
@Component(immediate = true, service = FriendlyURLResolver.class)
public class TestFriendlyURLResolver implements FriendlyURLResolver {

	public static final String SEPARATOR = "/-foo-";

	@Override
	public String getActualURL(
			long companyId, long groupId, boolean privateLayout,
			String mainPath, String friendlyURL, Map<String, String[]> params,
			Map<String, Object> requestContext)
		throws PortalException {

		return null;
	}

	@Override
	public LayoutFriendlyURLComposite getLayoutFriendlyURLComposite(
			long companyId, long groupId, boolean privateLayout,
			String friendlyURL, Map<String, String[]> params,
			Map<String, Object> requestContext)
		throws PortalException {

		return null;
	}

	@Override
	public String getURLSeparator() {
		return SEPARATOR;
	}

}