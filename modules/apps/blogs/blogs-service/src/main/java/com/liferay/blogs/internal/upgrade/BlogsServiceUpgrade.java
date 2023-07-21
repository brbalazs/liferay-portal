/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.internal.upgrade;

import com.liferay.blogs.internal.upgrade.v1_1_0.UpgradeClassNames;
import com.liferay.blogs.internal.upgrade.v1_1_0.UpgradeFriendlyURL;
import com.liferay.blogs.internal.upgrade.v1_1_1.UpgradeUrlTitle;
import com.liferay.blogs.internal.upgrade.v1_1_2.UpgradeBlogsImages;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.comment.upgrade.UpgradeDiscussionSubscriptionClassName;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.service.ImageLocalService;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.subscription.service.SubscriptionLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class BlogsServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "1.0.0", new UpgradeClassNames());

		registry.register(
			"1.0.0", "1.1.0",
			new UpgradeFriendlyURL(_friendlyURLEntryLocalService));

		registry.register("1.1.0", "1.1.1", new UpgradeUrlTitle());

		registry.register(
			"1.1.1", "1.1.2",
			new UpgradeBlogsImages(_imageLocalService, _portletFileRepository));

		registry.register(
			"1.1.2", "1.1.3",
			new UpgradeDiscussionSubscriptionClassName(
				_subscriptionLocalService, BlogsEntry.class.getName(),
				UpgradeDiscussionSubscriptionClassName.DeletionMode.ADD_NEW));
	}

	@Reference
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Reference
	private ImageLocalService _imageLocalService;

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	private ModuleServiceLifecycle _moduleServiceLifecycle;

	@Reference
	private PortletFileRepository _portletFileRepository;

	@Reference
	private SubscriptionLocalService _subscriptionLocalService;

}