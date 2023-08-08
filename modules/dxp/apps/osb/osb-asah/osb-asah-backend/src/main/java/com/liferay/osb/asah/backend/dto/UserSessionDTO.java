/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@GraphQLType("UserSession")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("userSessions")
public class UserSessionDTO {

	public UserSessionDTO(List<BQEvent> bqEvents, BQSession bqSession) {
		Comparator<BQEvent> comparator = Comparator.comparing(
			BQEvent::getEventDate);

		Collections.sort(bqEvents, comparator.reversed());

		BQEvent firstBQEvent = bqEvents.get(bqEvents.size() - 1);

		JSONObject contextJSONObject = new JSONObject(
			firstBQEvent.getContext());

		_browserName = firstBQEvent.getBrowserName();

		_completeDate = bqSession.getSessionEnd();
		_contentLanguageId = firstBQEvent.getContentLanguageId();
		_createDate = bqSession.getSessionStart();
		_devicePixelRatio = contextJSONObject.optString("devicePixelRatio", "");
		_deviceType = firstBQEvent.getDeviceType();
		_languageId = firstBQEvent.getLanguageId();
		_screenHeight = contextJSONObject.optString("screenHeight", "");
		_screenWidth = contextJSONObject.optString("screenWidth", "");
		_timezoneOffset = firstBQEvent.getTimezoneOffset();
		_userAgent = contextJSONObject.optString("userAgent", "");

		for (BQEvent bqEvent : bqEvents) {
			_bqEventDTOs.add(new BQEventDTO(bqEvent));
		}
	}

	@GraphQLProperty("events")
	@JsonProperty("events")
	public List<BQEventDTO> getBQEventDTOs() {
		return _bqEventDTOs;
	}

	public String getBrowserName() {
		return _browserName;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCompleteDate() {
		if (_completeDate == null) {
			return null;
		}

		return new Date(_completeDate.getTime());
	}

	public String getContentLanguageId() {
		return _contentLanguageId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getDevicePixelRatio() {
		return _devicePixelRatio;
	}

	public String getDeviceType() {
		return _deviceType;
	}

	public String getLanguageId() {
		return _languageId;
	}

	public String getScreenHeight() {
		return _screenHeight;
	}

	public String getScreenWidth() {
		return _screenWidth;
	}

	public String getTimezoneOffset() {
		return _timezoneOffset;
	}

	public String getUserAgent() {
		return _userAgent;
	}

	private final List<BQEventDTO> _bqEventDTOs = new ArrayList<>();
	private final String _browserName;
	private final Date _completeDate;
	private final String _contentLanguageId;
	private final Date _createDate;
	private final String _devicePixelRatio;
	private final String _deviceType;
	private final String _languageId;
	private final String _screenHeight;
	private final String _screenWidth;
	private final String _timezoneOffset;
	private final String _userAgent;

}