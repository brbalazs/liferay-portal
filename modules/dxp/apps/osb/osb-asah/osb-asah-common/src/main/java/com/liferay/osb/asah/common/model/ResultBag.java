/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author André Miranda
 * @author Inácio Nery
 */
public class ResultBag<T> {

	public ResultBag() {
	}

	public ResultBag(List<T> results, long total) {
		this.results = results;
		this.total = total;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ResultBag)) {
			return false;
		}

		ResultBag resultBag = (ResultBag)obj;

		if (Objects.equals(results, resultBag.results) &&
			Objects.equals(total, resultBag.total)) {

			return true;
		}

		return false;
	}

	public List<T> getResults() {
		return results;
	}

	public long getTotal() {
		return total;
	}

	@Override
	public int hashCode() {
		return Objects.hash(results, total);
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	protected List<T> results = new ArrayList<>();
	protected long total;

}