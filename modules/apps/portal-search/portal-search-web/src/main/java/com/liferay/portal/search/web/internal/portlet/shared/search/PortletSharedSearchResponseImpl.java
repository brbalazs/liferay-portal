/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.portlet.shared.search;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.web.internal.display.context.PortletRequestThemeDisplaySupplier;
import com.liferay.portal.search.web.internal.display.context.ThemeDisplaySupplier;
import com.liferay.portal.search.web.internal.portlet.shared.task.PortletSharedRequestHelper;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchResponse;
import com.liferay.portal.search.web.search.request.SearchResponse;
import com.liferay.portal.search.web.search.request.SearchSettings;

import java.util.List;
import java.util.Optional;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;

/**
 * @author André de Oliveira
 */
public class PortletSharedSearchResponseImpl
	implements PortletSharedSearchResponse {

	public PortletSharedSearchResponseImpl(
		SearchResponse searchResponse,
		PortletSharedRequestHelper portletSharedRequestHelper) {

		_searchResponse = searchResponse;
		_portletSharedRequestHelper = portletSharedRequestHelper;
	}

	@Override
	public List<Document> getDocuments() {
		return _searchResponse.getDocuments();
	}

	@Override
	public Facet getFacet(String fieldName) {
		return _searchResponse.getFacet(fieldName);
	}

	/**
	 * @deprecated As of Judson (7.1.x)
	 */
	@Deprecated
	@Override
	public String[] getHighlights() {
		return _searchResponse.getHighlights();
	}

	@Override
	public Optional<String> getKeywordsOptional() {
		return _searchResponse.getKeywordsOptional();
	}

	@Override
	public int getPaginationDelta() {
		return _searchResponse.getPaginationDelta();
	}

	@Override
	public int getPaginationStart() {
		return _searchResponse.getPaginationStart();
	}

	@Override
	public Optional<String> getParameter(
		String name, RenderRequest renderRequest) {

		return _portletSharedRequestHelper.getParameter(name, renderRequest);
	}

	@Override
	public Optional<String[]> getParameterValues(
		String name, RenderRequest renderRequest) {

		return _portletSharedRequestHelper.getParameterValues(
			name, renderRequest);
	}

	@Override
	public Optional<PortletPreferences> getPortletPreferences(
		RenderRequest renderRequest) {

		return Optional.ofNullable(renderRequest.getPreferences());
	}

	@Override
	public String getQueryString() {
		return _searchResponse.getQueryString();
	}

	@Override
	public List<String> getRelatedQueriesSuggestions() {
		return _searchResponse.getRelatedQueriesSuggestions();
	}

	@Override
	public SearchSettings getSearchSettings() {
		return _searchResponse.getSearchSettings();
	}

	@Override
	public Optional<String> getSpellCheckSuggestionOptional() {
		return _searchResponse.getSpellCheckSuggestionOptional();
	}

	@Override
	public ThemeDisplay getThemeDisplay(RenderRequest renderRequest) {
		ThemeDisplaySupplier themeDisplaySupplier =
			new PortletRequestThemeDisplaySupplier(renderRequest);

		return themeDisplaySupplier.getThemeDisplay();
	}

	@Override
	public int getTotalHits() {
		return _searchResponse.getTotalHits();
	}

	private final PortletSharedRequestHelper _portletSharedRequestHelper;
	private final SearchResponse _searchResponse;

}