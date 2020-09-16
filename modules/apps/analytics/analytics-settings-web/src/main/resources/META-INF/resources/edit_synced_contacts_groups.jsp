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
PortletURL portletURL = renderResponse.createRenderURL();

boolean includeSyncContactsFields = ParamUtil.getBoolean(request, "includeSyncContactsFields");

if (includeSyncContactsFields) {
	portletURL.setParameter("mvcRenderCommandName", "/analytics_settings/view");
	portletURL.setParameter("tabs1", "synced-contact-data");
}
else {
	portletURL.setParameter("mvcRenderCommandName", "/analytics_settings/view");
	portletURL.setParameter("tabs1", "synced-contacts");
}

String redirect = ParamUtil.getString(request, "redirect", portletURL.toString());

int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);
int delta = ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM);
String entriesNavigation = ParamUtil.getString(request, "entriesNavigation");
String orderByCol = ParamUtil.getString(request, "orderByCol", "user-group-name");
String orderByType = ParamUtil.getString(request, "orderByType", "asc");

PortletURL navigationPortletURL = renderResponse.createRenderURL();

navigationPortletURL.setParameter("mvcRenderCommandName", "/analytics_settings/edit_synced_contacts_groups");
navigationPortletURL.setParameter("redirect", redirect);

if (delta > 0) {
	navigationPortletURL.setParameter("delta", String.valueOf(delta));
}

PortletURL sortURL = PortletURLUtil.clone(navigationPortletURL, liferayPortletResponse);

sortURL.setParameter("entriesNavigation", entriesNavigation);

navigationPortletURL.setParameter("orderBycol", orderByCol);
navigationPortletURL.setParameter("orderByType", orderByType);

PortletURL displayStyleURL = PortletURLUtil.clone(navigationPortletURL, liferayPortletResponse);

portletURL.setParameter("entriesNavigation", entriesNavigation);

if (cur > 0) {
	displayStyleURL.setParameter("cur", String.valueOf(cur));
}

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(ParamUtil.getString(request, "backURL", redirect));

if (includeSyncContactsFields) {
	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "select-contact-data"), portletURL.toString());
	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "select-contacts"), redirect);
}
else {
	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "select-contacts"), portletURL.toString());
}

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(resourceBundle, "sync-by-user-groups"), currentURL);
%>

<portlet:actionURL name="/analytics_settings/edit_synced_contacts" var="editSyncedContactsURL" />

<portlet:renderURL var="editSyncedContactsFieldsURL">
	<portlet:param name="mvcRenderCommandName" value="/analytics_settings/edit_synced_contacts_fields" />
</portlet:renderURL>

<div class="container-fluid-1280">
	<div class="col-12">
		<div id="breadcrumb">
			<liferay-ui:breadcrumb
				showCurrentGroup="<%= false %>"
				showGuestGroup="<%= false %>"
				showLayout="<%= false %>"
				showPortletBreadcrumb="<%= true %>"
			/>
		</div>
	</div>
</div>

<div class="container-fluid-1280">
	<h2 class="autofit-row">
		<span class="autofit-col autofit-col-expand">
			<liferay-ui:message key="select-contacts-by-user-groups" />
		</span>
	</h2>

	<hr />

	<div class="autofit-row form-text">
		<span class="autofit-col autofit-col-expand pb-3">
			<liferay-ui:message key="select-contacts-by-user-groups-help" />
		</span>
	</div>

	<liferay-frontend:management-bar
		includeCheckBox="<%= true %>"
		searchContainerId="selectUserGroups"
	>
		<liferay-frontend:management-bar-buttons>
			<liferay-frontend:management-bar-display-buttons
				displayViews='<%= new String[] {"list"} %>'
				portletURL="<%= displayStyleURL %>"
				selectedDisplayStyle="list"
			/>
		</liferay-frontend:management-bar-buttons>

		<liferay-frontend:management-bar-filters>
			<liferay-frontend:management-bar-navigation
				navigationKeys='<%= new String[] {"all"} %>'
				navigationParam="entriesNavigation"
				portletURL="<%= navigationPortletURL %>"
			/>

			<liferay-frontend:management-bar-sort
				orderByCol="<%= orderByCol %>"
				orderByType="<%= orderByType %>"
				orderColumns='<%= new String[] {"user-group-name"} %>'
				portletURL="<%= sortURL %>"
			/>
		</liferay-frontend:management-bar-filters>
	</liferay-frontend:management-bar>

	<%
	UserGroupDisplayContext userGroupDisplayContext = new UserGroupDisplayContext(renderRequest, renderResponse);
	%>

	<aui:form action="<%= includeSyncContactsFields ? editSyncedContactsFieldsURL : editSyncedContactsURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="update_synced_groups" />
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="includeSyncContactsFields" type="hidden" value="<%= String.valueOf(includeSyncContactsFields) %>" />

		<liferay-ui:search-container
			id="selectUserGroups"
			searchContainer="<%= userGroupDisplayContext.getUserGroupSearch() %>"
			var="userGroupSearchContainer"
		>
			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.UserGroup"
				escapedModel="<%= true %>"
				keyProperty="userGroupId"
				modelVar="userGroup"
			>
				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
					name="user-group-name"
					value="<%= HtmlUtil.escape(userGroup.getName()) %>"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>

		<aui:button-row>
			<aui:button type="submit" value='<%= includeSyncContactsFields ? "save-and-next" : "save" %>' />
			<aui:button href="<%= redirect %>" type="cancel" value="cancel" />
		</aui:button-row>
	</aui:form>
</div>