/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.model.RecentVisitSite;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.List;
import java.util.Set;

/**
 * @author Marcos Martins
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-sites")
public class RecentVisitSiteDTO extends RecentVisitDTO {

	public RecentVisitSiteDTO() {
	}

	public RecentVisitSiteDTO(List<RecentVisitSite> recentVisitSites) {
		_recentVisitSiteDTOS = SetUtil.map(
			recentVisitSites, RecentVisitSiteDTO::new);
	}

	public RecentVisitSiteDTO(RecentVisitSite recentVisitSite) {
		super(recentVisitSite);

		_groupId = recentVisitSite.getGroupId();
	}

	public String getGroupId() {
		return _groupId;
	}

	@JsonProperty("recent-sites")
	public Set<RecentVisitSiteDTO> getRecentVisitSiteDTOs() {
		return _recentVisitSiteDTOS;
	}

	private String _groupId;
	private Set<RecentVisitSiteDTO> _recentVisitSiteDTOS;

}