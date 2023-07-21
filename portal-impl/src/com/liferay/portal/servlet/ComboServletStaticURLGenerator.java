/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.comparator.PortletNameComparator;
import com.liferay.portlet.PortletResourceAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Carlos Sierra Andrés
 */
public class ComboServletStaticURLGenerator {

	public List<String> generate(List<Portlet> portlets) {
		List<String> urls = new ArrayList<>();

		StringBundler sb = new StringBundler();

		long timestamp = _timestamp;

		portlets = ListUtil.copy(portlets);

		portlets = ListUtil.sort(portlets, _portletNameComparator);

		for (Portlet portlet : portlets) {
			for (PortletResourceAccessor portletResourceAccessor :
					_portletResourceAccessors) {

				List<String> portletResources = portletResourceAccessor.get(
					portlet);

				for (String portletResource : portletResources) {
					if (!_predicateFilter.filter(portletResource)) {
						continue;
					}

					String url = portletResource;

					if (!HttpUtil.hasProtocol(portletResource)) {
						url =
							PortalUtil.getPathProxy() +
								portlet.getContextPath() + portletResource;
					}

					if (_visitedURLs.contains(url)) {
						continue;
					}

					if (HttpUtil.hasProtocol(portletResource)) {
						urls.add(portletResource);
					}
					else {
						sb.append(StringPool.AMPERSAND);

						if (!portletResourceAccessor.isPortalResource()) {
							sb.append(portlet.getPortletId());
							sb.append(StringPool.COLON);
						}

						sb.append(HtmlUtil.escapeURL(portletResource));

						timestamp = Math.max(timestamp, portlet.getTimestamp());
					}

					_visitedURLs.add(url);
				}
			}
		}

		if (sb.length() > 0) {
			String url = _urlPrefix + sb.toString();

			url = HttpUtil.addParameter(url, "t", timestamp);

			urls.add(url);
		}

		return urls;
	}

	public void setPortletResourceAccessors(
		PortletResourceAccessor... portletResourceAccessors) {

		_portletResourceAccessors = portletResourceAccessors;
	}

	public void setPredicateFilter(PredicateFilter<String> predicateFilter) {
		_predicateFilter = predicateFilter;
	}

	public void setTimestamp(long timestamp) {
		_timestamp = timestamp;
	}

	public void setURLPrefix(String urlPrefix) {
		_urlPrefix = urlPrefix;
	}

	public void setVisitedURLs(Set<String> visitedURLs) {
		_visitedURLs = visitedURLs;
	}

	private static final PortletNameComparator _portletNameComparator =
		new PortletNameComparator();

	private PortletResourceAccessor[] _portletResourceAccessors;
	private PredicateFilter<String> _predicateFilter = PredicateFilter.ALL;
	private long _timestamp;
	private String _urlPrefix;
	private Set<String> _visitedURLs;

}