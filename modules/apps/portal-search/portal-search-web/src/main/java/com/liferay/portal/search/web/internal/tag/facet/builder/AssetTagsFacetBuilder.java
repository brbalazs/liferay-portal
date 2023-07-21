/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.tag.facet.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.tag.AssetTagNamesFacetFactory;

/**
 * @author Lino Alves
 */
public class AssetTagsFacetBuilder {

	public AssetTagsFacetBuilder(
		AssetTagNamesFacetFactory assetTagNamesFacetFactory) {

		_assetTagNamesFacetFactory = assetTagNamesFacetFactory;
	}

	public Facet build() {
		Facet facet = _assetTagNamesFacetFactory.newInstance(_searchContext);

		facet.setAggregationName(getAggregationName(facet.getFieldName()));
		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		facet.select(_selectedTagNames);

		return facet;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		_frequencyThreshold = frequencyThreshold;
	}

	public void setMaxTerms(int maxTerms) {
		_maxTerms = maxTerms;
	}

	public void setPortletId(String portletId) {
		_portletId = portletId;
	}

	public void setSearchContext(SearchContext searchContext) {
		_searchContext = searchContext;
	}

	public void setSelectedTagNames(String... selectedTagNames) {
		_selectedTagNames = selectedTagNames;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-tag");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.4);

		AssetTagsFacetConfiguration assetTagsFacetConfiguration =
			new AssetTagsFacetConfigurationImpl(facetConfiguration);

		assetTagsFacetConfiguration.setFrequencyThreshold(_frequencyThreshold);
		assetTagsFacetConfiguration.setMaxTerms(_maxTerms);

		return facetConfiguration;
	}

	protected String getAggregationName(String fieldName) {
		return fieldName + StringPool.PERIOD + _portletId;
	}

	private final AssetTagNamesFacetFactory _assetTagNamesFacetFactory;
	private int _frequencyThreshold;
	private int _maxTerms;
	private String _portletId;
	private SearchContext _searchContext;
	private String[] _selectedTagNames;

}