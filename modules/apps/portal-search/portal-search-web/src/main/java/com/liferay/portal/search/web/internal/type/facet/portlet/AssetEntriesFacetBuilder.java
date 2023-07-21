/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.type.facet.portlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.type.AssetEntriesFacetFactory;

/**
 * @author Lino Alves
 */
public class AssetEntriesFacetBuilder {

	public AssetEntriesFacetBuilder(
		AssetEntriesFacetFactory assetEntriesFacetFactory) {

		_assetEntriesFacetFactory = assetEntriesFacetFactory;
	}

	public Facet build() {
		Facet facet = _assetEntriesFacetFactory.newInstance(_searchContext);

		facet.setAggregationName(getAggregationName(facet.getFieldName()));
		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		facet.select(_selectedEntryClassNames);

		return facet;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		_frequencyThreshold = frequencyThreshold;
	}

	public void setPortletId(String portletId) {
		_portletId = portletId;
	}

	public void setSearchContext(SearchContext searchContext) {
		_searchContext = searchContext;
	}

	public void setSelectedEntryClassNames(String... selectedEntryClassNames) {
		_selectedEntryClassNames = selectedEntryClassNames;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-asset");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.6);

		AssetEntriesFacetConfiguration assetEntriesFacetConfiguration =
			new AssetEntriesFacetConfigurationImpl(facetConfiguration);

		assetEntriesFacetConfiguration.setFrequencyThreshold(
			_frequencyThreshold);

		return facetConfiguration;
	}

	protected String getAggregationName(String fieldName) {
		return fieldName + StringPool.PERIOD + _portletId;
	}

	private final AssetEntriesFacetFactory _assetEntriesFacetFactory;
	private int _frequencyThreshold;
	private String _portletId;
	private SearchContext _searchContext;
	private String[] _selectedEntryClassNames;

}