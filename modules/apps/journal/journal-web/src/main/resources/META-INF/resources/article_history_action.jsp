<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

JournalArticle article = (JournalArticle)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.VIEW) %>">
		<liferay-portlet:renderURL plid="<%= JournalUtil.getPreviewPlid(article, themeDisplay) %>" var="previewArticleContentURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="mvcPath" value="/preview_article_content.jsp" />
			<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
			<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
			<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
		</liferay-portlet:renderURL>

		<%
		String taglibOnClick = "Liferay.fire('previewArticle', {title: '" + HtmlUtil.escapeJS(article.getTitle(locale)) + "', uri: '" + HtmlUtil.escapeJS(previewArticleContentURL.toString()) + "'});";
		%>

		<liferay-ui:icon
			message="preview"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>

		<c:if test="<%= JournalFolderPermission.contains(permissionChecker, scopeGroupId, article.getFolderId(), ActionKeys.ADD_ARTICLE) %>">
			<portlet:renderURL var="copyURL">
				<portlet:param name="mvcPath" value="/copy_article.jsp" />
				<portlet:param name="redirect" value="<%= currentURL %>" />
				<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
				<portlet:param name="oldArticleId" value="<%= article.getArticleId() %>" />
				<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
			</portlet:renderURL>

			<liferay-ui:icon
				message="copy"
				url="<%= copyURL.toString() %>"
			/>
		</c:if>
	</c:if>

	<c:if test="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.EXPIRE) && (article.getStatus() == WorkflowConstants.STATUS_APPROVED) %>">
		<portlet:actionURL name="expireArticles" var="expireURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
			<portlet:param name="articleId" value="<%= article.getArticleId() + JournalPortlet.VERSION_SEPARATOR + article.getVersion() %>" />
		</portlet:actionURL>

		<liferay-ui:icon
			message="expire"
			url="<%= expireURL %>"
		/>
	</c:if>

	<portlet:renderURL var="compareVersionURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcPath" value="/select_version.jsp" />
		<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
		<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
		<portlet:param name="sourceVersion" value="<%= String.valueOf(article.getVersion()) %>" />
	</portlet:renderURL>

	<%
	Map<String, Object> data = new HashMap<String, Object>();

	data.put("uri", compareVersionURL.toString());
	%>

	<liferay-ui:icon
		cssClass="compare-to-link"
		data="<%= data %>"
		message="compare-to"
		url="javascript:;"
	/>

	<c:if test="<%= JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) %>">
		<portlet:actionURL name="deleteArticle" var="deleteURL">
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="referringPortletResource" value="<%= referringPortletResource %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
			<portlet:param name="articleId" value="<%= article.getArticleId() + JournalPortlet.VERSION_SEPARATOR + article.getVersion() %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>