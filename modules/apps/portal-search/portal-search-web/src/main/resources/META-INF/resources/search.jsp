<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNotNull(redirect)) {
	portletDisplay.setURLBack(redirect);
}

long groupId = ParamUtil.getLong(request, SearchPortletParameterNames.GROUP_ID);

String format = ParamUtil.getString(request, SearchPortletParameterNames.FORMAT);
%>

<liferay-portlet:renderURL varImpl="searchURL">
	<portlet:param name="mvcPath" value="/search.jsp" />
</liferay-portlet:renderURL>

<aui:form action="<%= searchURL %>" method="get" name="fm" onSubmit="event.preventDefault();">
	<liferay-portlet:renderURLParams varImpl="searchURL" />
	<aui:input name="<%= SearchContainer.DEFAULT_CUR_PARAM %>" type="hidden" value="<%= ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR) %>" />
	<aui:input name="format" type="hidden" value="<%= format %>" />

	<div id="<%= renderResponse.getNamespace() %>searchContainer">
		<aui:input autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>" inlineField="<%= true %>" label="" name="keywords" size="30" title="search" value="<%= HtmlUtil.escape(searchDisplayContext.getKeywords()) %>" />
		<aui:input name="scope" type="hidden" value="<%= searchDisplayContext.getSearchScopeParameterString() %>" />
		<aui:input name="useAdvancedSearchSyntax" type="hidden" value="<%= searchDisplayContext.isUseAdvancedSearchSyntax() %>" />

		<aui:field-wrapper inlineField="<%= true %>">
			<aui:button icon="icon-search" onClick='<%= renderResponse.getNamespace() + "search();" %>' type="submit" value="search" />
		</aui:field-wrapper>
	</div>

	<%@ include file="/main_search.jspf" %>

	<c:if test="<%= searchDisplayContext.isDisplayOpenSearchResults() %>">
		<liferay-ui:panel
			collapsible="<%= true %>"
			cssClass="open-search-panel"
			extended="<%= true %>"
			id="searchOpenSearchPanelContainer"
			persistState="<%= true %>"
			title="open-search"
		>
			<%@ include file="/open_search.jspf" %>
		</liferay-ui:panel>
	</c:if>
</aui:form>

<aui:script sandbox="<%= true %>">
	$('#<portlet:namespace />keywords').on(
		'keydown',
		function(event) {
			if (event.keyCode === 13) {
				<portlet:namespace />search();
			}
		}
	);
</aui:script>

<aui:script>
	function <portlet:namespace />addSearchProvider() {
		<portlet:resourceURL var="openSearchDescriptionXMLURL">
			<portlet:param name="mvcPath" value="/open_search_description.jsp" />
			<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" />
		</portlet:resourceURL>

		window.external.AddSearchProvider('<%= openSearchDescriptionXMLURL.toString() %>');
	}

	window.<portlet:namespace />search = function() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= SearchContainer.DEFAULT_CUR_PARAM %>').val(1);

		var keywords = form.fm('keywords').val();

		keywords = keywords.replace(/^\s+|\s+$/, '');

		if (keywords != '') {
			submitForm(form);
		}
	}
</aui:script>

<%
String pageSubtitle = LanguageUtil.get(request, "search-results");

String pageKeywords = LanguageUtil.get(request, "search");

if (Validator.isNotNull(searchDisplayContext.getKeywords())) {
	pageKeywords = searchDisplayContext.getKeywords();

	if (StringUtil.startsWith(pageKeywords, Field.ASSET_TAG_NAMES + StringPool.COLON)) {
		pageKeywords = StringUtil.replace(pageKeywords, Field.ASSET_TAG_NAMES + StringPool.COLON, StringPool.BLANK);
	}
}

PortalUtil.setPageSubtitle(pageSubtitle, request);
PortalUtil.setPageKeywords(pageKeywords, request);
%>