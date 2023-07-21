<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/admin/common/init.jsp" %>

<%
KBSuggestionListDisplayContext kbSuggestionListDisplayContext = (KBSuggestionListDisplayContext)request.getAttribute(KBWebKeys.KNOWLEDGE_BASE_KB_SUGGESTION_LIST_DISPLAY_CONTEXT);

SearchContainer kbCommentsSearchContainer = (SearchContainer)request.getAttribute("view_suggestions.jsp-searchContainer");

KBCommentResultRowSplitter resultRowSplitter = (KBCommentResultRowSplitter)request.getAttribute("view_suggestions.jsp-resultRowSplitter");
%>

<liferay-portlet:actionURL name="deleteKBComments" varImpl="deleteKBCommentsURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</liferay-portlet:actionURL>

<aui:form action="<%= deleteKBCommentsURL %>" name="fm">
	<liferay-ui:search-container
		id="kbComments"
		searchContainer="<%= kbCommentsSearchContainer %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.knowledge.base.model.KBComment"
			keyProperty="kbCommentId"
			modelVar="kbComment"
		>
			<liferay-ui:search-container-column-user
				showDetails="<%= false %>"
				userId="<%= kbComment.getUserId() %>"
			/>

			<liferay-ui:search-container-column-text
				colspan="<%= 2 %>"
			>

				<%
				Date modifiedDate = kbComment.getModifiedDate();

				String modifiedDateDescription = LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true);
				%>

				<h5 class="text-default">
					<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(kbComment.getUserName()), modifiedDateDescription} %>" key="x-suggested-x-ago" />
				</h5>

				<h4>
					<liferay-portlet:renderURL varImpl="rowURL">
						<portlet:param name="mvcPath" value='<%= templatePath + "view_suggestion.jsp" %>' />
						<portlet:param name="kbCommentId" value="<%= String.valueOf(kbComment.getKbCommentId()) %>" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
					</liferay-portlet:renderURL>

					<aui:a href="<%= rowURL.toString() %>">
						<%= StringUtil.shorten(HtmlUtil.escape(kbComment.getContent()), 100) %>
					</aui:a>
				</h4>

				<h5>
					<span class="kb-comment-status text-default">
						<liferay-ui:message key="<%= KBUtil.getStatusLabel(kbComment.getStatus()) %>" />
					</span>

					<%
					KBArticle kbArticle = KBArticleServiceUtil.getLatestKBArticle(kbComment.getClassPK(), WorkflowConstants.STATUS_ANY);

					request.setAttribute(KBWebKeys.KNOWLEDGE_BASE_KB_ARTICLE, kbArticle);

					KBArticleURLHelper kbArticleURLHelper = new KBArticleURLHelper(renderRequest, renderResponse, templatePath);

					PortletURL viewKBArticleURL = kbArticleURLHelper.createViewWithRedirectURL(kbArticle, currentURL);
					%>

					<c:if test="<%= kbSuggestionListDisplayContext.isShowKBArticleTitle() %>">
						<a class="kb-article-link text-default" href="<%= viewKBArticleURL.toString() %>"><%= HtmlUtil.escape(kbArticle.getTitle()) %></a>
					</c:if>
				</h5>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-jsp
				path="/admin/common/suggestion_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="descriptive"
			markupView="lexicon"
			resultRowSplitter="<%= resultRowSplitter %>"
		/>
	</liferay-ui:search-container>
</aui:form>