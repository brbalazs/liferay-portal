/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.custom.facet.builder;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.custom.CustomFacetFactory;

/**
 * @author Wade Cao
 */
public class CustomFacetBuilder {

	public CustomFacetBuilder(CustomFacetFactory customFacetFactory) {
		_customFacetFactory = customFacetFactory;
	}

	public Facet build() {
		Facet facet = _customFacetFactory.newInstance(_searchContext);

		facet.setFieldName(_fieldToAggregate);

		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		facet.select(_selectedValues);

		facet.setAggregationName(_aggregationName);

		return facet;
	}

	public void setAggregationName(String aggregationName) {
		_aggregationName = aggregationName;
	}

	public void setFieldToAggregate(String fieldToAggregate) {
		_fieldToAggregate = fieldToAggregate;
	}

	public void setSearchContext(SearchContext searchContext) {
		_searchContext = searchContext;
	}

	public void setSelectedValues(String... selectedValues) {
		_selectedValues = selectedValues;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.1);

		return facetConfiguration;
	}

	private String _aggregationName;
	private final CustomFacetFactory _customFacetFactory;
	private String _fieldToAggregate;
	private SearchContext _searchContext;
	private String[] _selectedValues;

}