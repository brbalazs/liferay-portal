/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.calendar.internal.model.listener;

import com.liferay.calendar.model.CalendarResource;
import com.liferay.calendar.service.CalendarResourceLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = ModelListener.class)
public class GroupModelListener extends BaseModelListener<Group> {

	@Override
	public void onAfterUpdate(Group group) throws ModelListenerException {
		try {
			long classNameId = _portal.getClassNameId(Group.class);

			CalendarResource calendarResource =
				_calendarResourceLocalService.fetchCalendarResource(
					classNameId, group.getGroupId());

			if (calendarResource == null) {
				return;
			}

			Map<Locale, String> nameMap = new HashMap<>();

			nameMap.put(
				LocaleUtil.getSiteDefault(), group.getDescriptiveName());

			calendarResource.setNameMap(
				LocalizationUtil.populateLocalizationMap(
					nameMap,
					LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault()),
					group.getGroupId()));

			_calendarResourceLocalService.updateCalendarResource(
				calendarResource);
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Override
	public void onBeforeRemove(Group group) throws ModelListenerException {
		try {

			// Global calendar resource

			long classNameId = _portal.getClassNameId(Group.class);

			CalendarResource calendarResource =
				_calendarResourceLocalService.fetchCalendarResource(
					classNameId, group.getGroupId());

			if (calendarResource != null) {
				_calendarResourceLocalService.deleteCalendarResource(
					calendarResource);
			}

			// Local calendar resources

			_calendarResourceLocalService.deleteCalendarResources(
				group.getGroupId());
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference(unbind = "-")
	protected void setCalendarResourceLocalService(
		CalendarResourceLocalService calendarResourceLocalService) {

		_calendarResourceLocalService = calendarResourceLocalService;
	}

	private CalendarResourceLocalService _calendarResourceLocalService;

	@Reference
	private Portal _portal;

}