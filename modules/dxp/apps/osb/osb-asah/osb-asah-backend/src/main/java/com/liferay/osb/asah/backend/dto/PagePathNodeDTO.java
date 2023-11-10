/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
@GraphQLType("PagePathNode")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagePathNodeDTO {

	public PagePathNodeDTO() {
	}

	public PagePathNodeDTO(
		String canonicalUrl, List<PagePathNodeDTO> followingPagePathNodeDTOs,
		List<PagePathNodeDTO> previousPagePathNodeDTOs, String title,
		Long views) {

		_canonicalUrl = canonicalUrl;
		_followingPagePathNodeDTOs = followingPagePathNodeDTOs;
		_previousPagePathNodeDTOs = previousPagePathNodeDTOs;
		_title = title;
		_views = views;
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	@GraphQLProperty("followingPagePathNodes")
	@JsonProperty("followingPagePathNodes")
	public List<PagePathNodeDTO> getFollowingPagePathNodeDTOs() {
		return _followingPagePathNodeDTOs;
	}

	@GraphQLProperty("previousPagePathNodes")
	@JsonProperty("previousPagePathNodes")
	public List<PagePathNodeDTO> getPreviousPagePathNodeDTOs() {
		return _previousPagePathNodeDTOs;
	}

	public String getTitle() {
		return _title;
	}

	public Long getViews() {
		return _views;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setFollowingPagePathNodeDTOS(
		List<PagePathNodeDTO> followingPagePathNodeDTOs) {

		_followingPagePathNodeDTOs = followingPagePathNodeDTOs;
	}

	public void setPreviousPagePathNodes(
		List<PagePathNodeDTO> previousPagePathNodeDTOs) {

		_previousPagePathNodeDTOs = previousPagePathNodeDTOs;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setViews(Long views) {
		_views = views;
	}

	private String _canonicalUrl;
	private List<PagePathNodeDTO> _followingPagePathNodeDTOs;
	private List<PagePathNodeDTO> _previousPagePathNodeDTOs;
	private String _title;
	private Long _views;

}