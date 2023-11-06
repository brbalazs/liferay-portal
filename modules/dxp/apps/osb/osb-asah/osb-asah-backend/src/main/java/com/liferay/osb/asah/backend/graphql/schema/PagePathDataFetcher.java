/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.PagePathDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.PagePathNodeDTO;
import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "pagePath", typeName = "QueryType")
public class PagePathDataFetcher extends BaseDataFetcher<PagePathNodeDTO> {

	@Override
	public PagePathNodeDTO get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		List<AdjacentPageViewsMetric> adjacentPagesViewsMetric =
			_pagePathDog.getAdjacentPagesViewsMetric(
				searchQueryContext.getCanonicalUrl(),
				searchQueryContext.getChannelIdAsLong(),
				dataFetchingEnvironment.getArgument("segmentId"),
				searchQueryContext.getTimeRange(),
				searchQueryContext.getTitle());

		PagePathNodeDTO rootPagePathNodeDTO = new PagePathNodeDTO();

		rootPagePathNodeDTO.setTitle(searchQueryContext.getTitle());
		rootPagePathNodeDTO.setCanonicalUrl(
			searchQueryContext.getCanonicalUrl());

		_setPreviousPagePathNodeDTOs(
			adjacentPagesViewsMetric, rootPagePathNodeDTO);

		_setFollowingPagePathNodeDTOs(
			adjacentPagesViewsMetric, rootPagePathNodeDTO);

		return rootPagePathNodeDTO;
	}

	private void _setFollowingPagePathNodeDTOs(
		List<AdjacentPageViewsMetric> adjacentPagesViewsMetrics,
		PagePathNodeDTO rootPagePathNodeDTO) {

		List<PagePathNodeDTO> followingPagePathNodeDTOs = new ArrayList<>();

		long totalViews = 0;

		for (AdjacentPageViewsMetric adjacentPageViewsMetric :
				adjacentPagesViewsMetrics) {

			if (adjacentPageViewsMetric.isPrevious()) {
				continue;
			}

			PagePathNodeDTO pagePathNodeDTO = new PagePathNodeDTO();

			pagePathNodeDTO.setTitle(adjacentPageViewsMetric.getTitle());
			pagePathNodeDTO.setCanonicalUrl(
				adjacentPageViewsMetric.getCanonicalUrl());

			Long views = adjacentPageViewsMetric.getViewsAsLong();

			pagePathNodeDTO.setViews(views);

			totalViews += views;

			followingPagePathNodeDTOs.add(pagePathNodeDTO);
		}

		Collections.sort(
			followingPagePathNodeDTOs,
			new Comparator<PagePathNodeDTO>() {

				@Override
				public int compare(
					PagePathNodeDTO pagePathNodeDTO1,
					PagePathNodeDTO pagePathNodeDTO2) {

					String title1 = pagePathNodeDTO1.getTitle();
					String title2 = pagePathNodeDTO2.getTitle();

					if (Objects.equals(title1, "others")) {
						return 1;
					}

					if (Objects.equals(title2, "others")) {
						return -1;
					}

					return Long.compare(
						pagePathNodeDTO2.getViews(),
						pagePathNodeDTO1.getViews());
				}

			});

		PagePathNodeDTO dropOffPagePathNodeDTO = new PagePathNodeDTO();

		dropOffPagePathNodeDTO.setTitle("drop-offs");
		dropOffPagePathNodeDTO.setCanonicalUrl("drop-offs");
		dropOffPagePathNodeDTO.setViews(
			rootPagePathNodeDTO.getViews() - totalViews);

		followingPagePathNodeDTOs.add(dropOffPagePathNodeDTO);

		rootPagePathNodeDTO.setFollowingPagePathNodeDTOS(
			followingPagePathNodeDTOs);
	}

	private void _setPreviousPagePathNodeDTOs(
		List<AdjacentPageViewsMetric> adjacentPagesViewsMetrics,
		PagePathNodeDTO rootPagePathNodeDTO) {

		List<PagePathNodeDTO> previousPagePathNodeDTOs = new ArrayList<>();

		long totalViews = 0;

		for (AdjacentPageViewsMetric adjacentPageViewsMetric :
				adjacentPagesViewsMetrics) {

			if (!adjacentPageViewsMetric.isPrevious()) {
				continue;
			}

			PagePathNodeDTO pagePathNodeDTO = new PagePathNodeDTO();

			pagePathNodeDTO.setTitle(adjacentPageViewsMetric.getTitle());
			pagePathNodeDTO.setCanonicalUrl(
				adjacentPageViewsMetric.getCanonicalUrl());

			Long views = adjacentPageViewsMetric.getViewsAsLong();

			pagePathNodeDTO.setViews(views);

			totalViews += views;

			previousPagePathNodeDTOs.add(pagePathNodeDTO);
		}

		Collections.sort(
			previousPagePathNodeDTOs,
			new Comparator<PagePathNodeDTO>() {

				@Override
				public int compare(
					PagePathNodeDTO pagePathNodeDTO1,
					PagePathNodeDTO pagePathNodeDTO2) {

					String title1 = pagePathNodeDTO1.getTitle();
					String title2 = pagePathNodeDTO2.getTitle();

					if (Objects.equals(title1, "direct") &&
						Objects.equals(title2, "others")) {

						return -1;
					}

					if (Objects.equals(title1, "others") &&
						Objects.equals(title2, "direct")) {

						return 1;
					}

					if (Objects.equals(title1, "direct")) {
						return 1;
					}

					if (Objects.equals(title2, "direct")) {
						return -1;
					}

					if (Objects.equals(title1, "others")) {
						return 1;
					}

					if (Objects.equals(title2, "others")) {
						return -1;
					}

					return Long.compare(
						pagePathNodeDTO2.getViews(),
						pagePathNodeDTO1.getViews());
				}

			});

		rootPagePathNodeDTO.setPreviousPagePathNodes(previousPagePathNodeDTOs);
		rootPagePathNodeDTO.setViews(totalViews);
	}

	@Autowired
	private PagePathDog _pagePathDog;

}