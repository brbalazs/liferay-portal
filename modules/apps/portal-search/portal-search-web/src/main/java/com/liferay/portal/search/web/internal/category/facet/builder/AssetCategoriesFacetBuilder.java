/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.category.facet.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.category.CategoryFacetFactory;

/**
 * @author Lino Alves
 */
public class AssetCategoriesFacetBuilder {

	public AssetCategoriesFacetBuilder(
		CategoryFacetFactory categoryFacetFactory) {

		_categoryFacetFactory = categoryFacetFactory;
	}

	public Facet build() {
		Facet facet = _categoryFacetFactory.newInstance(_searchContext);

		facet.setAggregationName(getAggregationName(facet.getFieldName()));
		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		if (_selectedCategoryIds != null) {
			facet.select(ArrayUtil.toStringArray(_selectedCategoryIds));
		}

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

	public void setSelectedCategoryIds(long... selectedCategoryIds) {
		_selectedCategoryIds = selectedCategoryIds;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-category");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.6);

		AssetCategoriesFacetConfiguration assetCategoriesFacetConfiguration =
			new AssetCategoriesFacetConfigurationImpl(facetConfiguration);

		assetCategoriesFacetConfiguration.setFrequencyThreshold(
			_frequencyThreshold);
		assetCategoriesFacetConfiguration.setMaxTerms(_maxTerms);

		return facetConfiguration;
	}

	protected String getAggregationName(String fieldName) {
		return fieldName + StringPool.PERIOD + _portletId;
	}

	private final CategoryFacetFactory _categoryFacetFactory;
	private int _frequencyThreshold;
	private int _maxTerms;
	private String _portletId;
	private SearchContext _searchContext;
	private long[] _selectedCategoryIds;

}