/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.type.facet.portlet.shared.search;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.facet.type.AssetEntriesFacetFactory;
import com.liferay.portal.search.web.internal.type.facet.constants.TypeFacetPortletKeys;
import com.liferay.portal.search.web.internal.type.facet.portlet.AssetEntriesFacetBuilder;
import com.liferay.portal.search.web.internal.type.facet.portlet.TypeFacetPortletPreferences;
import com.liferay.portal.search.web.internal.type.facet.portlet.TypeFacetPortletPreferencesImpl;
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
	property = "javax.portlet.name=" + TypeFacetPortletKeys.TYPE_FACET,
	service = PortletSharedSearchContributor.class
)
public class TypeFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		TypeFacetPortletPreferences typeFacetPortletPreferences =
			new TypeFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		Facet facet = buildFacet(
			typeFacetPortletPreferences, portletSharedSearchSettings);

		portletSharedSearchSettings.addFacet(facet);

		ThemeDisplay themeDisplay =
			portletSharedSearchSettings.getThemeDisplay();

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		searchContext.setEntryClassNames(
			getAssetTypesClassNames(typeFacetPortletPreferences, themeDisplay));
	}

	protected Facet buildFacet(
		TypeFacetPortletPreferences typeFacetPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		AssetEntriesFacetBuilder assetEntriesFacetBuilder =
			new AssetEntriesFacetBuilder(assetEntriesFacetFactory);

		assetEntriesFacetBuilder.setFrequencyThreshold(
			typeFacetPortletPreferences.getFrequencyThreshold());
		assetEntriesFacetBuilder.setPortletId(
			portletSharedSearchSettings.getPortletId());
		assetEntriesFacetBuilder.setSearchContext(
			portletSharedSearchSettings.getSearchContext());

		SearchOptionalUtil.copy(
			() -> portletSharedSearchSettings.getParameterValues(
				typeFacetPortletPreferences.getParameterName()),
			assetEntriesFacetBuilder::setSelectedEntryClassNames);

		return assetEntriesFacetBuilder.build();
	}

	protected String[] getAssetTypesClassNames(
		TypeFacetPortletPreferences typeFacetPortletPreferences,
		ThemeDisplay themeDisplay) {

		return typeFacetPortletPreferences.getCurrentAssetTypesArray(
			themeDisplay.getCompanyId());
	}

	@Reference
	protected AssetEntriesFacetFactory assetEntriesFacetFactory;

}