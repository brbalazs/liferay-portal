/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.internal.servlet.filter;

import com.liferay.portal.servlet.filters.authverifier.AuthVerifierFilter;

import javax.servlet.Filter;

import org.osgi.service.component.annotations.Component;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = {
		"filter.init.auth.verifier.PortalSessionAuthVerifier.urls.includes=/dynamic-data-mapping-data-provider-paginator/*",
		"osgi.http.whiteboard.filter.name=com.liferay.dynamic.data.mapping.data.provider.internal.servlet.filter.DDMDataProviderPaginatorAuthVerifierFilter",
		"osgi.http.whiteboard.filter.pattern=/dynamic-data-mapping-data-provider-paginator/*"
	},
	service = Filter.class
)
public class DDMDataProviderPaginatorAuthVerifierFilter
	extends AuthVerifierFilter {
}