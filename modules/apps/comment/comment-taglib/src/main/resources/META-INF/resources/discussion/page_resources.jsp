<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/discussion/init.jsp" %>

<%
int index = GetterUtil.getInteger(request.getAttribute("liferay-comment:discussion:index"));
int initialIndex = GetterUtil.getInteger(request.getAttribute("liferay-comment:discussion:index"));
int rootIndexPage = GetterUtil.getInteger(request.getAttribute("liferay-comment:discussion:rootIndexPage"));

DiscussionRequestHelper discussionRequestHelper = new DiscussionRequestHelper(request);
DiscussionTaglibHelper discussionTaglibHelper = new DiscussionTaglibHelper(request);

Discussion discussion = CommentManagerUtil.getDiscussion(discussionTaglibHelper.getUserId(), discussionRequestHelper.getScopeGroupId(), discussionTaglibHelper.getClassName(), discussionTaglibHelper.getClassPK(), new ServiceContextFunction(request));

DiscussionComment rootDiscussionComment = (discussion == null) ? null : discussion.getRootDiscussionComment();

DiscussionCommentIterator discussionCommentIterator = (rootDiscussionComment == null) ? null : rootDiscussionComment.getThreadDiscussionCommentIterator(rootIndexPage - 1);
%>

<c:if test="<%= discussionCommentIterator != null %>">

	<%
	while (discussionCommentIterator.hasNext()) {
		rootIndexPage = discussionCommentIterator.getIndexPage();

		if (index >= (initialIndex + PropsValues.DISCUSSION_COMMENTS_DELTA_VALUE)) {
			break;
		}

		request.setAttribute("liferay-comment:discussion:depth", 0);
		request.setAttribute("liferay-comment:discussion:discussion", discussion);
		request.setAttribute("liferay-comment:discussion:discussionComment", discussionCommentIterator.next());
	%>

		<liferay-util:include page="/discussion/view_message_thread.jsp" servletContext="<%= application %>" />

	<%
		index = GetterUtil.getInteger(request.getAttribute("liferay-comment:discussion:index"));
	}
	%>

</c:if>

<aui:script sandbox="<%= true %>">
	var rootIndexPage = $('#<%= namespace %>rootIndexPage');
	var index = $('#<%= namespace %>index');

	rootIndexPage.val('<%= String.valueOf(rootIndexPage) %>');
	index.val('<%= String.valueOf(index) %>');

	<c:if test="<%= (rootDiscussionComment != null) && (discussion.getDiscussionCommentsCount() <= index) %>">
		var moreCommentsLink = $('#<%= namespace %>moreComments');

		moreCommentsLink.hide();
	</c:if>
</aui:script>