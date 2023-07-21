/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.facet.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author André de Oliveira
 */
public class FolderTitleLookupImpl implements FolderTitleLookup {

	public FolderTitleLookupImpl(HttpServletRequest request) {
		_request = request;
	}

	@Override
	public String getFolderTitle(long curFolderId) {
		Hits results = searchFolder(curFolderId);

		if (results.getLength() == 0) {
			return null;
		}

		Document document = results.doc(0);

		Field field = document.getField(Field.TITLE);

		return field.getValue();
	}

	protected SearchContext getSearchContext(long curFolderId) {
		SearchContext searchContext = SearchContextFactory.getInstance(
			_request);

		searchContext.setFolderIds(new long[] {curFolderId});
		searchContext.setGroupIds(new long[0]);
		searchContext.setKeywords(StringPool.BLANK);

		return searchContext;
	}

	protected Hits searchFolder(long curFolderId) {
		FolderSearcher folderSearcher = new FolderSearcher();

		try {
			return folderSearcher.search(getSearchContext(curFolderId));
		}
		catch (SearchException se) {
			throw new RuntimeException(se);
		}
	}

	private final HttpServletRequest _request;

}