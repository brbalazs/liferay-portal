/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.user.facet.portlet.shared.search;

import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.search.facet.user.UserFacetFactory;
import com.liferay.portal.search.web.internal.user.facet.constants.UserFacetPortletKeys;
import com.liferay.portal.search.web.internal.user.facet.portlet.UserFacetBuilder;
import com.liferay.portal.search.web.internal.user.facet.portlet.UserFacetPortletPreferences;
import com.liferay.portal.search.web.internal.user.facet.portlet.UserFacetPortletPreferencesImpl;
import com.liferay.portal.search.web.internal.util.SearchOptionalUtil;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lino Alves
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + UserFacetPortletKeys.USER_FACET,
	service = PortletSharedSearchContributor.class
)
public class UserFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		UserFacetPortletPreferences userFacetPortletPreferences =
			new UserFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		Facet facet = buildFacet(
			userFacetPortletPreferences, portletSharedSearchSettings);

		portletSharedSearchSettings.addFacet(facet);
	}

	protected Facet buildFacet(
		UserFacetPortletPreferences userFacetPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		UserFacetBuilder userFacetBuilder = new UserFacetBuilder(
			userFacetFactory);

		userFacetBuilder.setFrequencyThreshold(
			userFacetPortletPreferences.getFrequencyThreshold());
		userFacetBuilder.setMaxTerms(userFacetPortletPreferences.getMaxTerms());
		userFacetBuilder.setPortletId(
			portletSharedSearchSettings.getPortletId());
		userFacetBuilder.setSearchContext(
			portletSharedSearchSettings.getSearchContext());

		SearchOptionalUtil.copy(
			() -> portletSharedSearchSettings.getParameterValues(
				userFacetPortletPreferences.getParameterName()),
			userFacetBuilder::setSelectedUserNames);

		return userFacetBuilder.build();
	}

	@Reference
	protected UserFacetFactory userFacetFactory;

}