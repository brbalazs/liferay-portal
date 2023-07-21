/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.folder.facet.portlet.shared.search;

import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.search.facet.folder.FolderFacetFactory;
import com.liferay.portal.search.web.internal.folder.facet.constants.FolderFacetPortletKeys;
import com.liferay.portal.search.web.internal.folder.facet.portlet.FolderFacetBuilder;
import com.liferay.portal.search.web.internal.folder.facet.portlet.FolderFacetPortletPreferences;
import com.liferay.portal.search.web.internal.folder.facet.portlet.FolderFacetPortletPreferencesImpl;
import com.liferay.portal.search.web.internal.util.SearchOptionalUtil;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.Arrays;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lino Alves
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + FolderFacetPortletKeys.FOLDER_FACET,
	service = PortletSharedSearchContributor.class
)
public class FolderFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		FolderFacetPortletPreferences folderFacetPortletPreferences =
			new FolderFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		Facet facet = buildFacet(
			folderFacetPortletPreferences, portletSharedSearchSettings);

		portletSharedSearchSettings.addFacet(facet);
	}

	protected Facet buildFacet(
		FolderFacetPortletPreferences folderFacetPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		FolderFacetBuilder folderFacetBuilder = new FolderFacetBuilder(
			folderFacetFactory);

		folderFacetBuilder.setFrequencyThreshold(
			folderFacetPortletPreferences.getFrequencyThreshold());
		folderFacetBuilder.setMaxTerms(
			folderFacetPortletPreferences.getMaxTerms());
		folderFacetBuilder.setPortletId(
			portletSharedSearchSettings.getPortletId());
		folderFacetBuilder.setSearchContext(
			portletSharedSearchSettings.getSearchContext());

		SearchOptionalUtil.copy(
			() -> {
				Optional<String[]> optional =
					portletSharedSearchSettings.getParameterValues(
						folderFacetPortletPreferences.getParameterName());

				return optional.map(
					parameterValues -> ListUtil.toLongArray(
						Arrays.asList(parameterValues), GetterUtil::getLong));
			},
			folderFacetBuilder::setSelectedFolderIds);

		return folderFacetBuilder.build();
	}

	@Reference
	protected FolderFacetFactory folderFacetFactory;

}