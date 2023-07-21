/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.search.request;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;

import java.util.Optional;

/**
 * @author André de Oliveira
 */
@ProviderType
public interface SearchSettings {

	public void addCondition(BooleanClause<Query> booleanClause);

	public void addFacet(Facet facet);

	public Optional<String> getKeywordsParameterName();

	public Optional<Integer> getPaginationDelta();

	public Optional<String> getPaginationDeltaParameterName();

	public Optional<Integer> getPaginationStart();

	public Optional<String> getPaginationStartParameterName();

	public QueryConfig getQueryConfig();

	public SearchContext getSearchContext();

	public void setKeywords(String keywords);

	public void setKeywordsParameterName(String keywordsParameterName);

	public void setPaginationDelta(int paginationDelta);

	public void setPaginationDeltaParameterName(
		String paginationDeltaParameterName);

	public void setPaginationStart(int paginationStart);

	public void setPaginationStartParameterName(
		String paginationStartParameterName);

}