/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.reports.engine.console.web.admin.internal.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.reports.engine.console.model.Source;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

/**
 * @author Rafael Praxedes
 */
public class SourceSearch extends SearchContainer<Source> {

	public static final String EMPTY_RESULTS_MESSAGE = "no-sources-were-found";

	public static List<String> headerNames = new ArrayList<String>() {
		{
			add("source-name");
			add("create-date");
		}
	};

	public SourceSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
		super(
			portletRequest, new SourceDisplayTerms(portletRequest),
			new SourceSearchTerms(portletRequest), DEFAULT_CUR_PARAM,
			DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

		SourceDisplayTerms definitionDisplayTerms =
			(SourceDisplayTerms)getDisplayTerms();

		iteratorURL.setParameter(
			SourceDisplayTerms.NAME, definitionDisplayTerms.getName());
		iteratorURL.setParameter(
			SourceDisplayTerms.DRIVER_URL,
			definitionDisplayTerms.getDriverUrl());
	}

}