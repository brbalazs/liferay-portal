/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.internal.verify;

import com.liferay.blogs.linkback.LinkbackConsumer;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.message.boards.model.MBDiscussion;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.service.MBDiscussionLocalService;
import com.liferay.message.boards.service.MBMessageLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.verify.VerifyProcess;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * <p>
 * This class looks at every blog comment to see if it is a trackback and
 * verifies that the source URL is a valid URL. Do not run this unless you want
 * to do this.
 * </p>
 *
 * @author Alexander Chow
 */
@Component(
	immediate = true,
	property = "verify.process.name=com.liferay.blogs.trackbacks",
	service = VerifyProcess.class
)
public class VerifyBlogsTrackbacks extends VerifyProcess {

	@Override
	protected void doVerify() throws Exception {
		verifyMBDiscussions();
	}

	@Reference(unbind = "-")
	protected void setBlogsEntryLocalService(
		BlogsEntryLocalService blogsEntryLocalService) {

		_blogsEntryLocalService = blogsEntryLocalService;
	}

	@Reference(unbind = "-")
	protected void setMBDiscussionLocalService(
		MBDiscussionLocalService mbDiscussionLocalService) {

		_mbDiscussionLocalService = mbDiscussionLocalService;
	}

	@Reference(unbind = "-")
	protected void setMBMessageLocalService(
		MBMessageLocalService mbMessageLocalService) {

		_mbMessageLocalService = mbMessageLocalService;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	protected void verifyMBDiscussions() {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			List<MBDiscussion> mbDiscussions =
				_mbDiscussionLocalService.getDiscussions(
					BlogsEntry.class.getName());

			for (MBDiscussion mbDiscussion : mbDiscussions) {
				try {
					BlogsEntry entry = _blogsEntryLocalService.getEntry(
						mbDiscussion.getClassPK());

					List<MBMessage> mbMessages =
						_mbMessageLocalService.getThreadMessages(
							mbDiscussion.getThreadId(),
							WorkflowConstants.STATUS_APPROVED);

					for (MBMessage mbMessage : mbMessages) {
						_verifyPost(entry, mbMessage);
					}
				}
				catch (Exception e) {
					_log.error(e, e);
				}
			}
		}
	}

	private void _verifyPost(BlogsEntry entry, MBMessage mbMessage)
		throws PortalException {

		String body = mbMessage.getBody();
		String url = null;

		int start = body.indexOf("[url=");

		if (start > -1) {
			start += "[url=".length();

			int end = body.indexOf("]", start);

			if (end > -1) {
				url = body.substring(start, end);
			}
		}

		if (Validator.isNotNull(url)) {
			long defaultUserId = _userLocalService.getDefaultUserId(
				mbMessage.getCompanyId());

			if (mbMessage.getUserId() == defaultUserId) {
				String entryURL =
					Portal.FRIENDLY_URL_SEPARATOR + "blogs/" +
						entry.getUrlTitle();

				_linkbackConsumer.verifyTrackback(
					mbMessage.getMessageId(), url, entryURL);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyBlogsTrackbacks.class);

	private BlogsEntryLocalService _blogsEntryLocalService;

	@Reference
	private LinkbackConsumer _linkbackConsumer;

	private MBDiscussionLocalService _mbDiscussionLocalService;
	private MBMessageLocalService _mbMessageLocalService;
	private UserLocalService _userLocalService;

}