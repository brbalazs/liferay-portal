/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.wiki.web.internal.display.context;

import com.liferay.trash.TrashHelper;
import com.liferay.wiki.display.context.WikiDisplayContextFactory;
import com.liferay.wiki.display.context.WikiEditPageDisplayContext;
import com.liferay.wiki.display.context.WikiListPagesDisplayContext;
import com.liferay.wiki.display.context.WikiNodeInfoPanelDisplayContext;
import com.liferay.wiki.display.context.WikiPageInfoPanelDisplayContext;
import com.liferay.wiki.display.context.WikiViewPageDisplayContext;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiPage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Iván Zaera
 * @author Sergio González
 */
@Component(service = WikiDisplayContextProvider.class)
public class WikiDisplayContextProvider {

	public WikiEditPageDisplayContext getWikiEditPageDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		WikiPage wikiPage) {

		WikiEditPageDisplayContext wikiEditPageDisplayContext =
			new DefaultWikiEditPageDisplayContext(request, response, wikiPage);

		for (WikiDisplayContextFactory wikiDisplayContextFactory :
				_wikiDisplayContextFactories) {

			wikiEditPageDisplayContext =
				wikiDisplayContextFactory.getWikiEditPageDisplayContext(
					wikiEditPageDisplayContext, request, response, wikiPage);
		}

		return wikiEditPageDisplayContext;
	}

	public WikiListPagesDisplayContext getWikiListPagesDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		WikiNode wikiNode) {

		WikiListPagesDisplayContext wikiListPagesDisplayContext =
			new DefaultWikiListPagesDisplayContext(
				request, response, wikiNode, _trashHelper);

		for (WikiDisplayContextFactory wikiDisplayContextFactory :
				_wikiDisplayContextFactories) {

			wikiListPagesDisplayContext =
				wikiDisplayContextFactory.getWikiListPagesDisplayContext(
					wikiListPagesDisplayContext, request, response, wikiNode);
		}

		return wikiListPagesDisplayContext;
	}

	public WikiNodeInfoPanelDisplayContext getWikiNodeInfoPanelDisplayContext(
		HttpServletRequest request, HttpServletResponse response) {

		WikiNodeInfoPanelDisplayContext wikiNodeInfoPanelDisplayContext =
			new DefaultWikiNodeInfoPanelDisplayContext(request, response);

		for (WikiDisplayContextFactory wikiDisplayContextFactory :
				_wikiDisplayContextFactories) {

			wikiNodeInfoPanelDisplayContext =
				wikiDisplayContextFactory.getWikiNodeInfoPanelDisplayContext(
					wikiNodeInfoPanelDisplayContext, request, response);
		}

		return wikiNodeInfoPanelDisplayContext;
	}

	public WikiPageInfoPanelDisplayContext getWikiPageInfoPanelDisplayContext(
		HttpServletRequest request, HttpServletResponse response) {

		WikiPageInfoPanelDisplayContext wikiPageInfoPanelDisplayContext =
			new DefaultWikiPageInfoPanelDisplayContext(request, response);

		for (WikiDisplayContextFactory wikiDisplayContextFactory :
				_wikiDisplayContextFactories) {

			wikiPageInfoPanelDisplayContext =
				wikiDisplayContextFactory.getWikiPageInfoPanelDisplayContext(
					wikiPageInfoPanelDisplayContext, request, response);
		}

		return wikiPageInfoPanelDisplayContext;
	}

	public WikiViewPageDisplayContext getWikiViewPageDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		WikiPage wikiPage) {

		WikiViewPageDisplayContext wikiViewPageDisplayContext =
			new DefaultWikiViewPageDisplayContext(request, response, wikiPage);

		for (WikiDisplayContextFactory wikiDisplayContextFactory :
				_wikiDisplayContextFactories) {

			wikiViewPageDisplayContext =
				wikiDisplayContextFactory.getWikiViewPageDisplayContext(
					wikiViewPageDisplayContext, request, response, wikiPage);
		}

		return wikiViewPageDisplayContext;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.RELUCTANT,
		service = WikiDisplayContextFactory.class
	)
	protected void setWikiDisplayContextFactory(
		WikiDisplayContextFactory wikiDisplayContextFactory) {

		_wikiDisplayContextFactories.add(wikiDisplayContextFactory);
	}

	protected void unsetWikiDisplayContextFactory(
		WikiDisplayContextFactory wikiDisplayContextFactory) {

		_wikiDisplayContextFactories.remove(wikiDisplayContextFactory);
	}

	@Reference
	private TrashHelper _trashHelper;

	private final List<WikiDisplayContextFactory> _wikiDisplayContextFactories =
		new CopyOnWriteArrayList<>();

}