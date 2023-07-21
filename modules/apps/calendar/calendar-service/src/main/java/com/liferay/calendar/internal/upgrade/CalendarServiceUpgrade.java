/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.calendar.internal.upgrade;

import com.liferay.calendar.internal.upgrade.v1_0_2.UpgradeCalendar;
import com.liferay.calendar.internal.upgrade.v1_0_4.UpgradeClassNames;
import com.liferay.calendar.internal.upgrade.v1_0_5.UpgradeCalendarResource;
import com.liferay.calendar.internal.upgrade.v1_0_5.UpgradeCompanyId;
import com.liferay.calendar.internal.upgrade.v1_0_5.UpgradeLastPublishDate;
import com.liferay.calendar.internal.upgrade.v1_0_6.UpgradeResourcePermission;
import com.liferay.calendar.internal.upgrade.v2_0_0.UpgradeSchema;
import com.liferay.calendar.internal.upgrade.v3_0_0.UpgradeCalendarBookingResourceBlock;
import com.liferay.calendar.internal.upgrade.v3_0_0.UpgradeCalendarResourceBlock;
import com.liferay.calendar.internal.upgrade.v3_0_0.UpgradeCalendarResourceResourceBlock;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.comment.upgrade.UpgradeDiscussionSubscriptionClassName;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.subscription.service.SubscriptionLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera
 * @author Manuel de la Peña
 */
@Component(
	immediate = true,
	service = {CalendarServiceUpgrade.class, UpgradeStepRegistrator.class}
)
public class CalendarServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.1", "1.0.0",
			new com.liferay.calendar.internal.upgrade.v1_0_0.
				UpgradeCalendarBooking());

		registry.register(
			"1.0.0", "1.0.1",
			new com.liferay.calendar.internal.upgrade.v1_0_1.
				UpgradeCalendarBooking());

		registry.register("1.0.1", "1.0.2", new UpgradeCalendar());

		registry.register("1.0.2", "1.0.3", new DummyUpgradeStep());

		registry.register("1.0.3", "1.0.4", new UpgradeClassNames());

		registry.register(
			"1.0.4", "1.0.5",
			new UpgradeCalendarResource(
				_classNameLocalService, _companyLocalService,
				_userLocalService),
			new UpgradeCompanyId(), new UpgradeLastPublishDate());

		registry.register(
			"1.0.5", "1.0.6",
			new UpgradeResourcePermission(
				_resourceActionLocalService, _resourcePermissionLocalService,
				_roleLocalService));

		registry.register("1.0.6", "1.0.7", new DummyUpgradeStep());

		registry.register("1.0.7", "2.0.0", new UpgradeSchema());

		registry.register(
			"2.0.0", "3.0.0", new UpgradeCalendarBookingResourceBlock(),
			new UpgradeCalendarResourceBlock(),
			new UpgradeCalendarResourceResourceBlock());

		registry.register(
			"3.0.0", "3.0.1",
			new UpgradeDiscussionSubscriptionClassName(
				_subscriptionLocalService, CalendarBooking.class.getName(),
				UpgradeDiscussionSubscriptionClassName.DeletionMode.ADD_NEW));
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ResourceActionLocalService _resourceActionLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private SubscriptionLocalService _subscriptionLocalService;

	@Reference
	private UserLocalService _userLocalService;

}