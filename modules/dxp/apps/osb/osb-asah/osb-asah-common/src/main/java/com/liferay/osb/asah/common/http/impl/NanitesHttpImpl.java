/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http.impl;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.spring.http.Http;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Shinn Lok
 */
@Component
public class NanitesHttpImpl implements NanitesHttp {

	@Override
	public void removeSchedule() {
		_http.exchangeIfUp(
			ServiceConstants.URL_BATCH_CURATOR, "/nanites/remove-schedule",
			HttpMethod.POST, null);
	}

	@Override
	public void rescheduleNanites() {
		_http.exchangeIfUp(
			ServiceConstants.URL_BATCH_CURATOR, "/nanites/reschedule",
			HttpMethod.POST, null);
	}

	@Override
	public void run(JSONArray jsonArray) {
		_http.exchangeIfUp(
			ServiceConstants.URL_BATCH_CURATOR, "/nanites/run", HttpMethod.POST,
			jsonArray.toString());
	}

	@Override
	public void scheduleAsahTask(Long asahTaskId) {
		_http.exchangeIfUp(
			ServiceConstants.URL_BATCH_CURATOR,
			String.format("/nanites/schedule/%d", asahTaskId), HttpMethod.POST,
			null);
	}

	@Override
	public void unscheduleAsahTask(Long asahTaskId) {
		_http.exchangeIfUp(
			ServiceConstants.URL_BATCH_CURATOR,
			String.format("/nanites/unschedule/%d", asahTaskId),
			HttpMethod.POST, null);
	}

	@Autowired
	private Http _http;

}