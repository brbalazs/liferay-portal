/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.search.options.portlet.shared.search;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.constants.SearchContextAttributes;
import com.liferay.portal.search.web.internal.search.options.constants.SearchOptionsPortletKeys;
import com.liferay.portal.search.web.internal.search.options.portlet.SearchOptionsPortletPreferences;
import com.liferay.portal.search.web.internal.search.options.portlet.SearchOptionsPortletPreferencesImpl;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import org.osgi.service.component.annotations.Component;

/**
 * @author Wade Cao
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + SearchOptionsPortletKeys.SEARCH_OPTIONS,
	service = PortletSharedSearchContributor.class
)
public class SearchOptionsPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchOptionsPortletPreferences searchOptionsPortletPreferences =
			new SearchOptionsPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		enableBasicFacetSelection(
			searchOptionsPortletPreferences, portletSharedSearchSettings);

		enableEmptySearches(
			searchOptionsPortletPreferences, portletSharedSearchSettings);
	}

	protected void enableBasicFacetSelection(
		SearchOptionsPortletPreferences searchOptionsPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		if (!searchOptionsPortletPreferences.isBasicFacetSelection()) {
			return;
		}

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		searchContext.setAttribute(
			SearchContextAttributes.ATTRIBUTE_KEY_BASIC_FACET_SELECTION,
			Boolean.TRUE);
	}

	protected void enableEmptySearches(
		SearchOptionsPortletPreferences searchOptionsPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		if (!searchOptionsPortletPreferences.isAllowEmptySearches()) {
			return;
		}

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		searchContext.setAttribute(
			SearchContextAttributes.ATTRIBUTE_KEY_EMPTY_SEARCH, Boolean.TRUE);
	}

}