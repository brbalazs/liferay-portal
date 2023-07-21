/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.service.impl;

import com.liferay.journal.constants.JournalConstants;
import com.liferay.journal.model.JournalFeed;
import com.liferay.journal.service.base.JournalFeedServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author Raymond Augé
 */
public class JournalFeedServiceImpl extends JournalFeedServiceBaseImpl {

	@Override
	public JournalFeed addFeed(
			long groupId, String feedId, boolean autoFeedId, String name,
			String description, String ddmStructureKey, String ddmTemplateKey,
			String ddmRendererTemplateKey, int delta, String orderByCol,
			String orderByType, String targetLayoutFriendlyUrl,
			String targetPortletId, String contentField, String feedType,
			double feedVersion, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId, ActionKeys.ADD_FEED);

		return journalFeedLocalService.addFeed(
			getUserId(), groupId, feedId, autoFeedId, name, description,
			ddmStructureKey, ddmTemplateKey, ddmRendererTemplateKey, delta,
			orderByCol, orderByType, targetLayoutFriendlyUrl, targetPortletId,
			contentField, feedType, feedVersion, serviceContext);
	}

	@Override
	public void deleteFeed(long feedId) throws PortalException {
		_journalFeedModelResourcePermission.check(
			getPermissionChecker(), feedId, ActionKeys.DELETE);

		journalFeedLocalService.deleteFeed(feedId);
	}

	@Override
	public void deleteFeed(long groupId, String feedId) throws PortalException {
		JournalFeed feed = journalFeedPersistence.findByG_F(groupId, feedId);

		_journalFeedModelResourcePermission.check(
			getPermissionChecker(), feed, ActionKeys.DELETE);

		journalFeedLocalService.deleteFeed(feed);
	}

	@Override
	public JournalFeed getFeed(long feedId) throws PortalException {
		JournalFeed feed = journalFeedLocalService.getFeed(feedId);

		_journalFeedModelResourcePermission.check(
			getPermissionChecker(), feed, ActionKeys.VIEW);

		return feed;
	}

	@Override
	public JournalFeed getFeed(long groupId, String feedId)
		throws PortalException {

		JournalFeed feed = journalFeedPersistence.findByG_F(groupId, feedId);

		_journalFeedModelResourcePermission.check(
			getPermissionChecker(), feed, ActionKeys.VIEW);

		return feed;
	}

	@Override
	public JournalFeed updateFeed(
			long groupId, String feedId, String name, String description,
			String ddmStructureKey, String ddmTemplateKey,
			String ddmRendererTemplateKey, int delta, String orderByCol,
			String orderByType, String targetLayoutFriendlyUrl,
			String targetPortletId, String contentField, String feedType,
			double feedVersion, ServiceContext serviceContext)
		throws PortalException {

		JournalFeed feed = journalFeedPersistence.findByG_F(groupId, feedId);

		_journalFeedModelResourcePermission.check(
			getPermissionChecker(), feed, ActionKeys.UPDATE);

		return journalFeedLocalService.updateFeed(
			groupId, feedId, name, description, ddmStructureKey, ddmTemplateKey,
			ddmRendererTemplateKey, delta, orderByCol, orderByType,
			targetLayoutFriendlyUrl, targetPortletId, contentField, feedType,
			feedVersion, serviceContext);
	}

	private static volatile ModelResourcePermission<JournalFeed>
		_journalFeedModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				JournalFeedServiceImpl.class,
				"_journalFeedModelResourcePermission", JournalFeed.class);
	private static volatile PortletResourcePermission
		_portletResourcePermission =
			PortletResourcePermissionFactory.getInstance(
				JournalFeedServiceImpl.class, "_portletResourcePermission",
				JournalConstants.RESOURCE_NAME);

}