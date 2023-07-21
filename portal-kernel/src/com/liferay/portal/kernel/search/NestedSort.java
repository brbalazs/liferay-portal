/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

/**
 * @author Jorge Díaz
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.portal.search.sort.NestedSort}
 */
@Deprecated
public class NestedSort extends Sort {

	public NestedSort(String fieldName, boolean reverse, String path) {
		this(fieldName, STRING_TYPE, reverse, path);
	}

	public NestedSort(
		String fieldName, int type, boolean reverse, String path) {

		super(fieldName, type, reverse);

		_path = path;
	}

	public Query getFilterQuery() {
		return _filterQuery;
	}

	public int getMaxChildren() {
		return _maxChildren;
	}

	public String getPath() {
		return _path;
	}

	public void setFilterQuery(Query filterQuery) {
		_filterQuery = filterQuery;
	}

	public void setMaxChildren(int maxChildren) {
		_maxChildren = maxChildren;
	}

	public void setPath(String path) {
		_path = path;
	}

	private Query _filterQuery;
	private int _maxChildren = Integer.MAX_VALUE;
	private String _path;

}