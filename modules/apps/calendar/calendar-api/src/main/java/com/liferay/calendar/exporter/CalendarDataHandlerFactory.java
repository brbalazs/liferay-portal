/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.calendar.exporter;

import com.liferay.portal.kernel.exception.PortalException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class CalendarDataHandlerFactory {

	public static CalendarDataHandler getCalendarDataHandler(
			CalendarDataFormat calendarDataFormat)
		throws PortalException {

		CalendarDataHandler calendarDataHandler = _calendarDataHandlers.get(
			calendarDataFormat);

		if (calendarDataHandler == null) {
			throw new PortalException(
				"Invalid format type " + calendarDataFormat);
		}

		return calendarDataHandler;
	}

	public void setCalendarDataHandlers(
		Map<String, CalendarDataHandler> calendarDataHandlers) {

		_calendarDataHandlers = new HashMap<>();

		for (Map.Entry<String, CalendarDataHandler> entry :
				calendarDataHandlers.entrySet()) {

			CalendarDataFormat calendarDataFormat = CalendarDataFormat.parse(
				entry.getKey());

			_calendarDataHandlers.put(calendarDataFormat, entry.getValue());
		}
	}

	private static Map<CalendarDataFormat, CalendarDataHandler>
		_calendarDataHandlers;

}