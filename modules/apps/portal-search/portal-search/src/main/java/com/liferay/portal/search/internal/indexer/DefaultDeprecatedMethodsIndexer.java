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

package com.liferay.portal.search.internal.indexer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.Summary;

import java.util.Locale;

/**
 * @author Adam Brandizzi
 */
public abstract class DefaultDeprecatedMethodsIndexer<T extends BaseModel<?>>
	implements Indexer<T> {

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getSearchClassNames}
	 */
	@Deprecated
	@Override
	public String[] getClassNames() {
		return getSearchClassNames();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #getClassName}
	 */
	@Deprecated
	@Override
	public String getPortletId() {
		return StringPool.BLANK;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             com.liferay.portal.search.sort.SortFieldBuilder
	 *             #getSortField}
	 */
	@Deprecated
	@Override
	public String getSortField(String orderByCol) {
		return Field.ENTRY_CLASS_PK;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             com.liferay.portal.search.sort.SortFieldBuilder
	 *             #getSortField}
	 */
	@Deprecated
	@Override
	public String getSortField(String orderByCol, int sortType) {
		if ((sortType == Sort.DOUBLE_TYPE) || (sortType == Sort.FLOAT_TYPE) ||
			(sortType == Sort.INT_TYPE) || (sortType == Sort.LONG_TYPE)) {

			return Field.getSortableFieldName(orderByCol);
		}

		return getSortField(orderByCol);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getSummary(Document, String, PortletRequest,
	 *             PortletResponse)}
	 */
	@Deprecated
	@Override
	public Summary getSummary(Document document, Locale locale, String snippet)
		throws SearchException {

		return getSummary(document, snippet, null, null);
	}

	/**
	 * @param      classPK
	 * @param      status
	 * @throws     Exception
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             RelatedEntryIndexer#isVisibleRelatedEntry(long, int)}
	 */
	@Deprecated
	@Override
	public boolean isVisibleRelatedEntry(long classPK, int status)
		throws Exception {

		return true;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #postProcessContextBooleanFilter(BooleanFilter,
	 *             SearchContext)}
	 */
	@Deprecated
	@Override
	public void postProcessContextQuery(
			BooleanQuery contextQuery, SearchContext searchContext)
		throws Exception {
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #postProcessSearchQuery(BooleanQuery, BooleanFilter,
	 *             SearchContext)}
	 */
	@Deprecated
	@Override
	public void postProcessSearchQuery(
			BooleanQuery searchQuery, SearchContext searchContext)
		throws Exception {
	}

}