<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/journal_article/init.jsp" %>

<liferay-util:dynamic-include key="com.liferay.journal.taglib#/journal_article/page.jsp#pre" />

<%
JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute("liferay-journal:journal-article:articleDisplay");
%>

<div class="clearfix journal-content-article" data-analytics-asset-id="<%= articleDisplay.getArticleId() %>" data-analytics-asset-title="<%= HtmlUtil.escapeAttribute(articleDisplay.getTitle()) %>" data-analytics-asset-type="web-content">
	<c:if test='<%= GetterUtil.getBoolean((String)request.getAttribute("liferay-journal:journal-article:showTitle")) %>'>
		<%= HtmlUtil.escape(articleDisplay.getTitle()) %>
	</c:if>

	<%= articleDisplay.getContent() %>
</div>

<%
List<AssetTag> assetTags = AssetTagLocalServiceUtil.getTags(JournalArticleDisplay.class.getName(), articleDisplay.getResourcePrimKey());

PortalUtil.setPageKeywords(ListUtil.toString(assetTags, AssetTag.NAME_ACCESSOR), request);
%>

<liferay-util:dynamic-include key="com.liferay.journal.taglib#/journal_article/page.jsp#post" />