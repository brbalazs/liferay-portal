/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.Date;

/**
 * @author Leslie Wong
 */
public abstract class RecentVisit {

	public Date getFirstVisitDate() {
		if (firstVisitDate == null) {
			return null;
		}

		return new Date(firstVisitDate.getTime());
	}

	public Date getLastVisitDate() {
		if (lastVisitDate == null) {
			return null;
		}

		return new Date(lastVisitDate.getTime());
	}

	public Long getVisits() {
		return visits;
	}

	public void setFirstVisitDate(Date firstVisitDate) {
		if (firstVisitDate != null) {
			this.firstVisitDate = new Date(firstVisitDate.getTime());
		}
	}

	public void setLastVisitDate(Date lastVisitDate) {
		if (lastVisitDate != null) {
			this.lastVisitDate = new Date(lastVisitDate.getTime());
		}
	}

	public void setVisits(Long visits) {
		this.visits = visits;
	}

	protected Date firstVisitDate;
	protected Date lastVisitDate;
	protected Long visits;

}