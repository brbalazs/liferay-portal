<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String title = journalDisplayContext.getFolderTitle();

if (Validator.isNotNull(title)) {
	renderResponse.setTitle(journalDisplayContext.getFolderTitle());
}
%>

<portlet:actionURL name="restoreTrashEntries" var="restoreTrashEntriesURL" />

<liferay-trash:undo
	portletURL="<%= restoreTrashEntriesURL %>"
/>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems='<%= journalDisplayContext.getNavigationBarItems("web-content") %>'
/>

<liferay-util:include page="/toolbar.jsp" servletContext="<%= application %>">
	<liferay-util:param name="searchContainerId" value="articles" />
</liferay-util:include>

<div id="<portlet:namespace />journalContainer">
	<div class="closed container-fluid-1280 sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
		<c:if test="<%= journalDisplayContext.isShowInfoButton() %>">
			<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/journal/info_panel" var="sidebarPanelURL" />

			<liferay-frontend:sidebar-panel
				resourceURL="<%= sidebarPanelURL %>"
				searchContainerId="articles"
			>
				<liferay-util:include page="/info_panel.jsp" servletContext="<%= application %>" />
			</liferay-frontend:sidebar-panel>
		</c:if>

		<div class="sidenav-content">
			<div class="journal-breadcrumb" id="<portlet:namespace />breadcrumbContainer">
				<c:if test="<%= !journalDisplayContext.isNavigationMine() && !journalDisplayContext.isNavigationRecent() %>">
					<liferay-util:include page="/breadcrumb.jsp" servletContext="<%= application %>" />
				</c:if>
			</div>

			<%
			PortletURL portletURL = journalDisplayContext.getPortletURL();
			%>

			<aui:form action="<%= portletURL %>" method="get" name="fm">
				<aui:input name="<%= ActionRequest.ACTION_NAME %>" type="hidden" />
				<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
				<aui:input name="groupId" type="hidden" value="<%= scopeGroupId %>" />
				<aui:input name="newFolderId" type="hidden" />

				<div class="journal-container" id="<portlet:namespace />entriesContainer">
					<c:choose>
						<c:when test="<%= !journalDisplayContext.isSearch() || (!journalDisplayContext.hasResults() && !journalDisplayContext.hasCommentsResults() && !journalDisplayContext.hasVersionsResults()) %>">
							<liferay-util:include page="/view_entries.jsp" servletContext="<%= application %>">
								<liferay-util:param name="searchContainerId" value="articles" />
							</liferay-util:include>
						</c:when>
						<c:otherwise>

							<%
							String[] tabsNames = new String[0];
							String[] tabsValues = new String[0];

							if (journalDisplayContext.hasResults()) {
								String tabName = StringUtil.appendParentheticalSuffix(LanguageUtil.get(request, "web-content"), journalDisplayContext.getTotalItems());

								tabsNames = ArrayUtil.append(tabsNames, tabName);

								tabsValues = ArrayUtil.append(tabsValues, "web-content");
							}

							if (journalDisplayContext.hasVersionsResults()) {
								String tabName = StringUtil.appendParentheticalSuffix(LanguageUtil.get(request, "versions"), journalDisplayContext.getVersionsTotal());

								tabsNames = ArrayUtil.append(tabsNames, tabName);

								tabsValues = ArrayUtil.append(tabsValues, "versions");
							}

							if (journalDisplayContext.hasCommentsResults()) {
								String tabName = StringUtil.appendParentheticalSuffix(LanguageUtil.get(request, "comments"), journalDisplayContext.getCommentsTotal());

								tabsNames = ArrayUtil.append(tabsNames, tabName);

								tabsValues = ArrayUtil.append(tabsValues, "comments");
							}
							%>

							<liferay-ui:tabs
								names="<%= StringUtil.merge(tabsNames) %>"
								portletURL="<%= portletURL %>"
								tabsValues="<%= StringUtil.merge(tabsValues) %>"
								type="tabs nav-tabs-default"
							/>

							<c:choose>
								<c:when test="<%= journalDisplayContext.isWebContentTabSelected() %>">
									<liferay-util:include page="/view_entries.jsp" servletContext="<%= application %>">
										<liferay-util:param name="searchContainerId" value="articles" />
									</liferay-util:include>
								</c:when>
								<c:when test="<%= journalDisplayContext.isVersionsTabSelected() %>">
									<liferay-util:include page="/view_versions.jsp" servletContext="<%= application %>">
										<liferay-util:param name="searchContainerId" value="versions" />
									</liferay-util:include>
								</c:when>
								<c:when test="<%= journalDisplayContext.isCommentsTabSelected() %>">
									<liferay-util:include page="/view_comments.jsp" servletContext="<%= application %>">
										<liferay-util:param name="searchContainerId" value="comments" />
									</liferay-util:include>
								</c:when>
								<c:otherwise>
									<liferay-util:include page="/view_entries.jsp" servletContext="<%= application %>">
										<liferay-util:param name="searchContainerId" value="articles" />
									</liferay-util:include>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</aui:form>
		</div>
	</div>
</div>