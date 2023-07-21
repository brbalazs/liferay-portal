/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.site.facet.portlet.shared.search;

import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.search.facet.site.SiteFacetFactory;
import com.liferay.portal.search.web.internal.site.facet.constants.SiteFacetPortletKeys;
import com.liferay.portal.search.web.internal.site.facet.portlet.ScopeFacetBuilder;
import com.liferay.portal.search.web.internal.site.facet.portlet.SiteFacetPortletPreferences;
import com.liferay.portal.search.web.internal.site.facet.portlet.SiteFacetPortletPreferencesImpl;
import com.liferay.portal.search.web.internal.util.SearchOptionalUtil;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + SiteFacetPortletKeys.SITE_FACET,
	service = PortletSharedSearchContributor.class
)
public class SiteFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SiteFacetPortletPreferences siteFacetPortletPreferences =
			new SiteFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		Facet facet = buildFacet(
			siteFacetPortletPreferences, portletSharedSearchSettings);

		portletSharedSearchSettings.addFacet(facet);
	}

	protected Facet buildFacet(
		SiteFacetPortletPreferences siteFacetPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		ScopeFacetBuilder scopeFacetBuilder = new ScopeFacetBuilder(
			siteFacetFactory);

		scopeFacetBuilder.setFrequencyThreshold(
			siteFacetPortletPreferences.getFrequencyThreshold());
		scopeFacetBuilder.setMaxTerms(
			siteFacetPortletPreferences.getMaxTerms());
		scopeFacetBuilder.setPortletId(
			portletSharedSearchSettings.getPortletId());
		scopeFacetBuilder.setSearchContext(
			portletSharedSearchSettings.getSearchContext());

		SearchOptionalUtil.copy(
			() -> portletSharedSearchSettings.getParameterValues(
				siteFacetPortletPreferences.getParameterName()),
			scopeFacetBuilder::setSelectedGroupIds);

		return scopeFacetBuilder.build();
	}

	@Reference
	protected SiteFacetFactory siteFacetFactory;

}