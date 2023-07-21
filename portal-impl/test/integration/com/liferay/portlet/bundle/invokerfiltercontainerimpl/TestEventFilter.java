/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.bundle.invokerfiltercontainerimpl;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=InvokerFilterContainerImplTest",
		"preinitialized.filter=true",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = PortletFilter.class
)
public class TestEventFilter implements EventFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
		EventRequest eventRequest, EventResponse eventResponse,
		FilterChain filterChain) {
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

}