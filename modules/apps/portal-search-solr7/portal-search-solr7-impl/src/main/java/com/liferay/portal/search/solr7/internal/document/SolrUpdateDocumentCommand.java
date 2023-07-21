/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.solr7.internal.document;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;

import java.util.Collection;

/**
 * @author Michael C. Han
 */
public interface SolrUpdateDocumentCommand {

	public String updateDocument(
			SearchContext searchContext, Document document, boolean deleteFirst)
		throws SearchException;

	public void updateDocuments(
			SearchContext searchContext, Collection<Document> documents,
			boolean deleteFirst)
		throws SearchException;

}