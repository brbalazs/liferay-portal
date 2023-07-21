/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.folder.facet.portlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.folder.FolderFacetFactory;

/**
 * @author Lino Alves
 */
public class FolderFacetBuilder {

	public FolderFacetBuilder(FolderFacetFactory folderFacetFactory) {
		_folderFacetFactory = folderFacetFactory;
	}

	public Facet build() {
		Facet facet = _folderFacetFactory.newInstance(_searchContext);

		facet.setAggregationName(getAggregationName(facet.getFieldName()));
		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		if (_selectedFolderIds != null) {
			facet.select(ArrayUtil.toStringArray(_selectedFolderIds));
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

	public void setSelectedFolderIds(long... selectedFolderIds) {
		_selectedFolderIds = selectedFolderIds;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-folder");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.4);

		FolderFacetConfiguration folderFacetConfiguration =
			new FolderFacetConfigurationImpl(facetConfiguration);

		folderFacetConfiguration.setFrequencyThreshold(_frequencyThreshold);
		folderFacetConfiguration.setMaxTerms(_maxTerms);

		return facetConfiguration;
	}

	protected String getAggregationName(String fieldName) {
		return fieldName + StringPool.PERIOD + _portletId;
	}

	private final FolderFacetFactory _folderFacetFactory;
	private int _frequencyThreshold;
	private int _maxTerms;
	private String _portletId;
	private SearchContext _searchContext;
	private long[] _selectedFolderIds;

}