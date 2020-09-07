/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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