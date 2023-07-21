/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.item.selector.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portlet.usersadmin.search.GroupSearch;
import com.liferay.site.item.selector.criterion.SiteItemSelectorCriterion;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

/**
 * @author Julio Camarero
 */
public interface SitesItemSelectorViewDisplayContext {

	public String getClearResultsURL() throws PortletException;

	public String getDisplayStyle();

	public List<DropdownItem> getFilterDropdownItems();

	public String getGroupName(Group group) throws PortalException;

	public GroupSearch getGroupSearch() throws Exception;

	public String getItemSelectedEventName();

	public String getOrderByType();

	public PortletRequest getPortletRequest();

	public PortletResponse getPortletResponse();

	public PortletURL getPortletURL() throws PortletException;

	public String getSearchActionURL() throws PortletException;

	public SiteItemSelectorCriterion getSiteItemSelectorCriterion();

	public String getSortingURL() throws PortletException;

	public int getTotalItems() throws Exception;

	public List<ViewTypeItem> getViewTypeItems() throws PortletException;

	public boolean isShowChildSitesLink();

	public boolean isShowSearch();

	public boolean isShowSortFilter();

}