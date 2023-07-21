/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.comment.display.context;

import com.liferay.portal.comment.display.context.util.DiscussionRequestHelper;
import com.liferay.portal.comment.display.context.util.DiscussionTaglibHelper;
import com.liferay.portal.kernel.comment.Discussion;
import com.liferay.portal.kernel.comment.DiscussionComment;
import com.liferay.portal.kernel.comment.DiscussionPermission;
import com.liferay.portal.kernel.comment.display.context.CommentDisplayContextFactory;
import com.liferay.portal.kernel.comment.display.context.CommentSectionDisplayContext;
import com.liferay.portal.kernel.comment.display.context.CommentTreeDisplayContext;
import com.liferay.portal.kernel.display.context.BaseDisplayContextProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Adolfo Pérez
 */
public class CommentDisplayContextProviderImpl
	extends BaseDisplayContextProvider<CommentDisplayContextFactory>
	implements CommentDisplayContextProvider {

	public CommentDisplayContextProviderImpl() {
		super(CommentDisplayContextFactory.class);
	}

	@Override
	public CommentSectionDisplayContext getCommentSectionDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		DiscussionPermission discussionPermission, Discussion discussion) {

		DiscussionRequestHelper discussionRequestHelper =
			new DiscussionRequestHelper(request);
		DiscussionTaglibHelper discussionTaglibHelper =
			new DiscussionTaglibHelper(request);

		CommentSectionDisplayContext commentSectionDisplayContext =
			new DefaultCommentSectionDisplayContext(
				discussionRequestHelper, discussionTaglibHelper,
				discussionPermission, discussion);

		for (CommentDisplayContextFactory commentDisplayContextFactory :
				getDisplayContextFactories()) {

			commentSectionDisplayContext =
				commentDisplayContextFactory.getCommentSectionDisplayContext(
					commentSectionDisplayContext, request, response,
					discussionPermission, discussion);
		}

		return commentSectionDisplayContext;
	}

	@Override
	public CommentTreeDisplayContext getCommentTreeDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		DiscussionPermission discussionPermission,
		DiscussionComment discussionComment) {

		DiscussionRequestHelper discussionRequestHelper =
			new DiscussionRequestHelper(request);
		DiscussionTaglibHelper discussionTaglibHelper =
			new DiscussionTaglibHelper(request);

		CommentTreeDisplayContext commentTreeDisplayContext =
			new DefaultCommentTreeDisplayContext(
				discussionRequestHelper, discussionTaglibHelper,
				discussionPermission, discussionComment);

		for (CommentDisplayContextFactory commentDisplayContextFactory :
				getDisplayContextFactories()) {

			commentTreeDisplayContext =
				commentDisplayContextFactory.getCommentTreeDisplayContext(
					commentTreeDisplayContext, request, response,
					discussionPermission, discussionComment);
		}

		return commentTreeDisplayContext;
	}

}