/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.search.request;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.facet.Facet;

import java.util.List;
import java.util.Optional;

/**
 * @author Rodrigo Paulino
 * @author André de Oliveira
 */
@ProviderType
public interface SearchResponse {

	public List<Document> getDocuments();

	public Facet getFacet(String fieldName);

	/**
	 * @deprecated As of Judson (7.1.x)
	 */
	@Deprecated
	public String[] getHighlights();

	public Optional<String> getKeywordsOptional();

	public int getPaginationDelta();

	public int getPaginationStart();

	public String getQueryString();

	public List<String> getRelatedQueriesSuggestions();

	public SearchSettings getSearchSettings();

	public Optional<String> getSpellCheckSuggestionOptional();

	public int getTotalHits();

}