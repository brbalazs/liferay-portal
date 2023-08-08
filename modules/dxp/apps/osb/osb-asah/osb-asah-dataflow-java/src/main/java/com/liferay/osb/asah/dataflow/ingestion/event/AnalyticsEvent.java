/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/**
 * @author Marcellus Tavares
 */
@DefaultSchema(JavaFieldSchema.class)
public class AnalyticsEvent {

	public AnalyticsEvent() {
	}

	public AnalyticsEvent(AnalyticsEvent analyticsEvent) {
		applicationId = analyticsEvent.applicationId;
		channelId = analyticsEvent.channelId;
		clientIP = analyticsEvent.clientIP;
		context = new HashMap(analyticsEvent.context);
		createDate = analyticsEvent.createDate;
		dataSourceId = analyticsEvent.dataSourceId;
		emailAddressHashed = analyticsEvent.emailAddressHashed;
		eventDate = analyticsEvent.eventDate;
		eventId = analyticsEvent.eventId;
		eventProperties = new HashMap(analyticsEvent.eventProperties);
		id = analyticsEvent.id;
		projectId = analyticsEvent.projectId;
		projectTimeZoneId = analyticsEvent.projectTimeZoneId;
		userId = analyticsEvent.userId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof AnalyticsEvent)) {
			return false;
		}

		AnalyticsEvent analyticsEvent = (AnalyticsEvent)obj;

		if (Objects.equals(applicationId, analyticsEvent.applicationId) &&
			Objects.equals(channelId, analyticsEvent.channelId) &&
			Objects.equals(clientIP, analyticsEvent.clientIP) &&
			Objects.equals(context, analyticsEvent.context) &&
			Objects.equals(createDate, analyticsEvent.createDate) &&
			Objects.equals(dataSourceId, analyticsEvent.dataSourceId) &&
			Objects.equals(
				emailAddressHashed, analyticsEvent.emailAddressHashed) &&
			Objects.equals(eventDate, analyticsEvent.eventDate) &&
			Objects.equals(eventId, analyticsEvent.eventId) &&
			Objects.equals(eventProperties, analyticsEvent.eventProperties) &&
			Objects.equals(id, analyticsEvent.id) &&
			Objects.equals(projectId, analyticsEvent.projectId) &&
			Objects.equals(
				projectTimeZoneId, analyticsEvent.projectTimeZoneId) &&
			Objects.equals(userId, analyticsEvent.userId)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			applicationId, channelId, clientIP, context, createDate,
			dataSourceId, emailAddressHashed, eventDate, eventId,
			eventProperties, id, projectId, projectTimeZoneId, userId);
	}

	public String applicationId;
	public String channelId;

	@Nullable
	public String clientIP;

	public Map<String, String> context;
	public String createDate;
	public String dataSourceId;

	@Nullable
	public String emailAddressHashed;

	public String eventDate;
	public String eventId;
	public Map<String, String> eventProperties;
	public String id;
	public String projectId;
	public String projectTimeZoneId;
	public String userId;

}