/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.PagePathDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.PagePathNodeDTO;
import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

		Set<AdjacentPageViewsMetric> adjacentPagesViewsMetric =
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

	private List<PagePathNodeDTO> _getPagePathNodeDTOs(
		Set<AdjacentPageViewsMetric> adjacentPagesViewsMetrics,
		boolean includePrevious) {

		List<PagePathNodeDTO> previousPagePathNodeDTOs = new ArrayList<>();

		for (AdjacentPageViewsMetric adjacentPageViewsMetric :
				adjacentPagesViewsMetrics) {

			if ((adjacentPageViewsMetric.isPrevious() && includePrevious) ||
				(!adjacentPageViewsMetric.isPrevious() && !includePrevious)) {

				PagePathNodeDTO pagePathNodeDTO = new PagePathNodeDTO();

				pagePathNodeDTO.setCanonicalUrl(
					adjacentPageViewsMetric.getCanonicalUrl());
				pagePathNodeDTO.setTitle(adjacentPageViewsMetric.getTitle());
				pagePathNodeDTO.setViews(
					adjacentPageViewsMetric.getViewsAsLong());

				previousPagePathNodeDTOs.add(pagePathNodeDTO);
			}
		}

		return previousPagePathNodeDTOs;
	}

	private long _getTotalViews(List<PagePathNodeDTO> pagePathNodeDTOs) {
		Stream<PagePathNodeDTO> stream = pagePathNodeDTOs.stream();

		Optional<Long> totalViewsOptional = stream.map(
			PagePathNodeDTO::getViews
		).reduce(
			Long::sum
		);

		return totalViewsOptional.orElse(0L);
	}

	private void _setFollowingPagePathNodeDTOs(
		Set<AdjacentPageViewsMetric> adjacentPagesViewsMetrics,
		PagePathNodeDTO rootPagePathNodeDTO) {

		List<PagePathNodeDTO> followingPagePathNodeDTOs = _getPagePathNodeDTOs(
			adjacentPagesViewsMetrics, false);

		Collections.sort(
			followingPagePathNodeDTOs,
			new FollowingPagePathNodeDTOComparator());

		PagePathNodeDTO dropOffPagePathNodeDTO = new PagePathNodeDTO();

		dropOffPagePathNodeDTO.setTitle("drop-offs");
		dropOffPagePathNodeDTO.setCanonicalUrl("drop-offs");
		dropOffPagePathNodeDTO.setViews(
			rootPagePathNodeDTO.getViews() -
				_getTotalViews(followingPagePathNodeDTOs));

		followingPagePathNodeDTOs.add(dropOffPagePathNodeDTO);

		rootPagePathNodeDTO.setFollowingPagePathNodeDTOS(
			followingPagePathNodeDTOs);
	}

	private void _setPreviousPagePathNodeDTOs(
		Set<AdjacentPageViewsMetric> adjacentPagesViewsMetrics,
		PagePathNodeDTO rootPagePathNodeDTO) {

		List<PagePathNodeDTO> previousPagePathNodeDTOs = _getPagePathNodeDTOs(
			adjacentPagesViewsMetrics, true);

		Collections.sort(
			previousPagePathNodeDTOs, new PreviousPagePathNodeDTOComparator());

		rootPagePathNodeDTO.setPreviousPagePathNodes(previousPagePathNodeDTOs);
		rootPagePathNodeDTO.setViews(_getTotalViews(previousPagePathNodeDTOs));
	}

	@Autowired
	private PagePathDog _pagePathDog;

	private static class FollowingPagePathNodeDTOComparator
		implements Comparator<PagePathNodeDTO>, Serializable {

		@Override
		public int compare(
			PagePathNodeDTO pagePathNodeDTO1,
			PagePathNodeDTO pagePathNodeDTO2) {

			if (Objects.equals(pagePathNodeDTO1.getTitle(), "others")) {
				return 1;
			}

			if (Objects.equals(pagePathNodeDTO2.getTitle(), "others")) {
				return -1;
			}

			return Long.compare(
				pagePathNodeDTO2.getViews(), pagePathNodeDTO1.getViews());
		}

	}

	private static class PreviousPagePathNodeDTOComparator
		implements Comparator<PagePathNodeDTO>, Serializable {

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
				pagePathNodeDTO2.getViews(), pagePathNodeDTO1.getViews());
		}

	}

}