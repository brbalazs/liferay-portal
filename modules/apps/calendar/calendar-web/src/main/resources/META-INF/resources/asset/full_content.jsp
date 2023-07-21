<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<div class="calendar-asset-full-content">
	<c:if test="<%= Validator.isNotNull(calendarBooking.getDescription(locale)) %>">
		<div>
			<%= calendarBooking.getDescription(locale) %>
		</div>
	</c:if>

	<p>
		<liferay-ui:icon
			iconCssClass="icon-user"
			message="owner"
		/>

		<%
		Calendar calendar = calendarBooking.getCalendar();
		%>

		<strong><%= HtmlUtil.escape(calendar.getName(locale)) %></strong>

		<%
		List<CalendarBooking> childCalendarBookings = calendarBooking.getChildCalendarBookings();
		%>

		<c:if test="<%= !childCalendarBookings.isEmpty() %>">
			<br />

			<liferay-ui:icon
				iconCssClass="icon-globe"
				message="resources"
			/>

			<liferay-ui:message key="resources" />:

			<%
			List<String> calendarResourcesNames = new ArrayList<String>();

			for (CalendarBooking childCalendarBooking : childCalendarBookings) {
				CalendarResource calendarResource = childCalendarBooking.getCalendarResource();

				calendarResourcesNames.add(calendarResource.getName(locale));
			}
			%>

			<%= HtmlUtil.escape(StringUtil.merge(calendarResourcesNames, ", ")) %>
		</c:if>

		<c:if test="<%= calendarBooking.isRecurring() %>">
			<br /><br />

			<liferay-ui:icon
				iconCssClass="icon-list-alt"
				message="recurring"
			/>

			<liferay-ui:message key="recurring" />
		</c:if>

		<br /><br />

		<liferay-ui:icon
			iconCssClass="icon-calendar"
			message="starts"
		/>

		<%
		java.util.Calendar startTimeJCalendar = JCalendarUtil.getJCalendar(calendarBooking.getStartTime(), user.getTimeZone());
		%>

		<liferay-ui:message key="starts" />: <%= dateFormatLongDate.format(startTimeJCalendar.getTime()) %>, <%= dateFormatTime.format(startTimeJCalendar.getTime()) %>

		<br />

		<liferay-ui:icon
			iconCssClass="icon-calendar"
			message="ends"
		/>

		<%
		java.util.Calendar endTimeJCalendar = JCalendarUtil.getJCalendar(calendarBooking.getEndTime(), user.getTimeZone());
		%>

		<liferay-ui:message key="ends" />: <%= dateFormatLongDate.format(endTimeJCalendar.getTime()) %>, <%= dateFormatTime.format(endTimeJCalendar.getTime()) %>

		<%
		java.util.Calendar nowJCalendar = CalendarFactoryUtil.getCalendar(timeZone);

		CalendarBooking nextCalendarBooking = RecurrenceUtil.getCalendarBookingInstance(calendarBooking, RecurrenceUtil.getIndexOfInstance(calendarBooking.getRecurrence(), startTimeJCalendar.getTimeInMillis(), nowJCalendar.getTimeInMillis()));
		%>

		<c:if test="<%= nextCalendarBooking != null %>">
			<br /><br />

			<liferay-ui:icon
				icon="calendar"
				markupView="lexicon"
				message="next-event-starts"
			/>

			<%
			java.util.Calendar nextEventStartTimeJCalendar = JCalendarUtil.getJCalendar(nextCalendarBooking.getStartTime(), user.getTimeZone());
			%>

			<liferay-ui:message key="next-event-starts" />: <%= dateFormatLongDate.format(nextEventStartTimeJCalendar.getTime()) %>, <%= dateFormatTime.format(nextEventStartTimeJCalendar.getTime()) %>

			<br />

			<liferay-ui:icon
				icon="calendar"
				markupView="lexicon"
				message="next-event-ends"
			/>

			<%
			java.util.Calendar nextEventEndTimeJCalendar = JCalendarUtil.getJCalendar(nextCalendarBooking.getEndTime(), user.getTimeZone());
			%>

			<liferay-ui:message key="next-event-ends" />: <%= dateFormatLongDate.format(nextEventEndTimeJCalendar.getTime()) %>, <%= dateFormatTime.format(nextEventEndTimeJCalendar.getTime()) %>
		</c:if>

		<c:if test="<%= Validator.isNotNull(calendarBooking.getLocation()) %>">
			<br /><br />

			<liferay-ui:icon
				iconCssClass="icon-location-arrow"
				message="location"
			/>

			<liferay-ui:message key="location" />: <a href="https://maps.google.com.br/maps?q=<%= HtmlUtil.escapeHREF(calendarBooking.getLocation()) %>" target="_blank"><%= HtmlUtil.escape(calendarBooking.getLocation()) %></a>
		</c:if>

		<liferay-expando:custom-attributes-available
			className="<%= CalendarBooking.class.getName() %>"
		>
			<liferay-expando:custom-attribute-list
				className="<%= CalendarBooking.class.getName() %>"
				classPK="<%= (calendarBooking != null) ? calendarBooking.getCalendarBookingId() : 0 %>"
				editable="<%= false %>"
				label="<%= true %>"
			/>
		</liferay-expando:custom-attributes-available>
	</p>
</div>

<br />