/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.user.facet.portlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.user.UserFacetFactory;

/**
 * @author Lino Alves
 */
public class UserFacetBuilder {

	public UserFacetBuilder(UserFacetFactory userFacetFactory) {
		_userFacetFactory = userFacetFactory;
	}

	public Facet build() {
		Facet facet = _userFacetFactory.newInstance(_searchContext);

		facet.setAggregationName(getAggregationName(facet.getFieldName()));
		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		facet.select(_selectedUserNames);

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

	public void setSelectedUserNames(String... selectedUserNames) {
		_selectedUserNames = selectedUserNames;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-user");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.1);

		UserFacetConfiguration userFacetConfiguration =
			new UserFacetConfigurationImpl(facetConfiguration);

		userFacetConfiguration.setFrequencyThreshold(_frequencyThreshold);
		userFacetConfiguration.setMaxTerms(_maxTerms);

		return facetConfiguration;
	}

	protected String getAggregationName(String fieldName) {
		return fieldName + StringPool.PERIOD + _portletId;
	}

	private int _frequencyThreshold;
	private int _maxTerms;
	private String _portletId;
	private SearchContext _searchContext;
	private String[] _selectedUserNames;
	private final UserFacetFactory _userFacetFactory;

}