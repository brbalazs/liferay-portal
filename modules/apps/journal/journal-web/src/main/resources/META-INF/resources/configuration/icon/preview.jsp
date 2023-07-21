<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<liferay-ui:icon
	id="basicPreviewButton"
	message="preview"
	url="javascript:;"
/>

<%
JournalArticle article = ActionUtil.getArticle(renderRequest);
%>

<liferay-portlet:renderURL plid="<%= JournalUtil.getPreviewPlid(article, themeDisplay) %>" var="previewArticleContentURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcPath" value="/preview_article_content.jsp" />
	<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
	<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
	<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
	<portlet:param name="ddmTemplateKey" value="<%= article.getDDMTemplateKey() %>" />
</liferay-portlet:renderURL>

<c:if test='<%= SessionMessages.contains(renderRequest, "previewRequested") %>'>
	<aui:script>
		Liferay.fire(
			'previewArticle',
			{
				title: '<%= HtmlUtil.escapeJS(article.getTitle(locale)) %>',
				uri: '<%= HtmlUtil.escapeJS(previewArticleContentURL.toString()) %>'
			}
		);
	</aui:script>
</c:if>