/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.PageAssetDTO;
import com.liferay.osb.asah.common.dog.AssetDog;
import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.PropertyFilter;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "pageAssets", typeName = "QueryType")
public class PageAssetBagDataFetcher
	implements DataFetcher<ResultBag<PageAssetDTO>> {

	@Override
	public ResultBag<PageAssetDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<PropertyFilter> propertyFilters = ListUtil.map(
			dataFetchingEnvironment.getArgument("propertyFilters"),
			PropertyFilter::of);

		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		Page<Asset> assetPage = _assetDog.getAssetPage(
			"Page", _toFilterString(propertyFilters),
			dataFetchingEnvironment.getArgument("keywords"), start / size, size,
			_getSort(dataFetchingEnvironment.getArgument("sort")));

		return new ResultBag<>(
			ListUtil.map(assetPage.getContent(), PageAssetDTO::new),
			assetPage.getTotalElements());
	}

	private void _appendPropertyFilterString(
		List<String> filterStrings, PropertyFilter propertyFilter) {

		if (!Objects.equals(propertyFilter.getPropertyName(), "keywords")) {
			filterStrings.add(propertyFilter.toFilterString());

			return;
		}

		propertyFilter.setPropertyName("keywords.keyword");

		filterStrings.add(propertyFilter.toFilterString());

		PropertyFilter keywordTypePropertyFilter = new PropertyFilter(
			"keywords.type = keyword", false);

		filterStrings.add(keywordTypePropertyFilter.toFilterString());
	}

	private Sort _getSort(Map<String, String> sort) {
		if (Objects.equals(sort.get("column"), "title")) {
			return new Sort("name", sort.get("type"));
		}

		return Sort.of(sort);
	}

	private String _toFilterString(List<PropertyFilter> propertyFilters) {
		if ((propertyFilters == null) || propertyFilters.isEmpty()) {
			return null;
		}

		List<String> filterStrings = new ArrayList<>();

		for (PropertyFilter propertyFilter : propertyFilters) {
			_appendPropertyFilterString(filterStrings, propertyFilter);
		}

		return String.join("and", filterStrings);
	}

	@Autowired
	private AssetDog _assetDog;

}