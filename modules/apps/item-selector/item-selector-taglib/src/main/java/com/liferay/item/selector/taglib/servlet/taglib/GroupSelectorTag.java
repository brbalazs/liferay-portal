/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.item.selector.taglib.servlet.taglib;

import com.liferay.item.selector.taglib.internal.servlet.ServletContextUtil;
import com.liferay.item.selector.taglib.internal.servlet.item.selector.ItemSelectorUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchPaginationUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.IncludeTag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Roberto Díaz
 */
public class GroupSelectorTag extends IncludeTag {

	public void setGroups(List<Group> groups) {
		_groups = groups;
	}

	public void setGroupsCount(int groupsCount) {
		_groupsCount = groupsCount;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_groups = null;
		_groupsCount = -1;
	}

	protected List<Group> getGroups(HttpServletRequest request) {
		if (_groups == null) {
			_search(request);
		}

		return _groups;
	}

	protected int getGroupsCount(HttpServletRequest request) {
		if (_groupsCount < 0) {
			_search(request);
		}

		return _groupsCount;
	}

	@Override
	protected String getPage() {
		return "/group_selector/page.jsp";
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-item-selector:group-selector:groups", getGroups(request));
		request.setAttribute(
			"liferay-item-selector:group-selector:groupsCount",
			getGroupsCount(request));
		request.setAttribute(
			"liferay-item-selector:group-selector:itemSelector",
			ItemSelectorUtil.getItemSelector());
	}

	private void _search(HttpServletRequest request) {
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			String keywords = ParamUtil.getString(request, "keywords");

			LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

			groupParams.put("site", Boolean.TRUE);

			List<Group> groups = GroupServiceUtil.search(
				themeDisplay.getCompanyId(), _CLASS_NAME_IDS, keywords,
				groupParams, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

			int cur = ParamUtil.getInteger(
				request, SearchContainer.DEFAULT_CUR_PARAM,
				SearchContainer.DEFAULT_CUR);
			int delta = ParamUtil.getInteger(
				request, SearchContainer.DEFAULT_DELTA_PARAM,
				SearchContainer.DEFAULT_DELTA);

			int[] startAndEnd = SearchPaginationUtil.calculateStartAndEnd(
				cur, delta);

			_groups = ListUtil.subList(groups, startAndEnd[0], startAndEnd[1]);

			_groupsCount = groups.size();
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			_groups = Collections.emptyList();
			_groupsCount = 0;
		}
	}

	private static final long[] _CLASS_NAME_IDS = {
		ClassNameLocalServiceUtil.getClassNameId(Company.class),
		ClassNameLocalServiceUtil.getClassNameId(Group.class),
		ClassNameLocalServiceUtil.getClassNameId(Organization.class)
	};

	private static final Log _log = LogFactoryUtil.getLog(
		GroupSelectorTag.class);

	private List<Group> _groups;
	private int _groupsCount = -1;

}