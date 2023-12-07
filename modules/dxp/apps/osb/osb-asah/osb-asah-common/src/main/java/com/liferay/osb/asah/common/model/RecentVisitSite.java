/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author Marcos Martins
 */
public class RecentVisitSite extends RecentVisit {

	public RecentVisitSite() {
	}

	public RecentVisitSite(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RecentVisitSite)) {
			return false;
		}

		RecentVisitSite recentVisitSite = (RecentVisitSite)obj;

		if (Objects.equals(dataSourceId, recentVisitSite.dataSourceId) &&
			Objects.equals(groupId, recentVisitSite.groupId) &&
			Objects.equals(firstVisitDate, recentVisitSite.firstVisitDate) &&
			Objects.equals(lastVisitDate, recentVisitSite.lastVisitDate) &&
			Objects.equals(visits, recentVisitSite.visits)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			dataSourceId, groupId, firstVisitDate, lastVisitDate, visits);
	}

}