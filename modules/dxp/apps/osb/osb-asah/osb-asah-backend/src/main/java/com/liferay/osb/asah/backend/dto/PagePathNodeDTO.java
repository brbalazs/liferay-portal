/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public class PagePathNodeDTO {

	public PagePathNodeDTO() {
	}

	public PagePathNodeDTO(
		String canonicalUrl, List<PagePathNodeDTO> followingPagePathNodeDTOs,
		List<PagePathNodeDTO> previousPagePathNodeDTOs, String title,
		Long views) {

		_canonicalUrl = canonicalUrl;
		_title = title;
		_views = views;

		_followingPagePathNodeDTOS = followingPagePathNodeDTOs;
		_previousPagePathNodeDTOS = previousPagePathNodeDTOs;
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	public List<PagePathNodeDTO> getFollowingPagePathNodeDTOs() {
		return _followingPagePathNodeDTOS;
	}

	public List<PagePathNodeDTO> getPreviousPagePathNodeDTOs() {
		return _previousPagePathNodeDTOS;
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
		List<PagePathNodeDTO> followingPagePathNodeDTOS) {

		_followingPagePathNodeDTOS = followingPagePathNodeDTOS;
	}

	public void setPreviousPagePathNodes(
		List<PagePathNodeDTO> previousPagePathNodeDTOS) {

		_previousPagePathNodeDTOS = previousPagePathNodeDTOS;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setViews(Long views) {
		_views = views;
	}

	private String _canonicalUrl;
	private List<PagePathNodeDTO> _followingPagePathNodeDTOS;
	private List<PagePathNodeDTO> _previousPagePathNodeDTOS;
	private String _title;
	private Long _views;

}