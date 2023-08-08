/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.Collections;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rachael Koestartyo
 */
@CrossOrigin
@RequestMapping("/dxp-entities")
@RestController
public class DXPEntitiesRestController {

	@PostMapping
	public ResponseEntity<?> post(
		@RequestHeader(required = false, value = HeaderConstants.DATA_SOURCE_ID)
			String dataSourceId,
		@RequestBody String json) {

		JSONArray jsonArray = _processMessages(dataSourceId, json);

		_messageBus.sendMessage(
			Channel.DXP_ENTITIES_MESSAGE, jsonArray.toString(),
			Collections.singletonMap(
				"projectId", ProjectIdThreadLocal.getProjectId()));

		return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
	}

	private JSONObject _processGenderField(JSONObject userJSONObject) {
		if (userJSONObject == null) {
			return null;
		}

		if (userJSONObject.has("male")) {
			if (userJSONObject.getBoolean("male")) {
				userJSONObject.put("gender", "male");
			}
			else {
				userJSONObject.put("gender", "female");
			}
		}

		JSONObject contactJSONObject = userJSONObject.optJSONObject("contact");

		if (contactJSONObject == null) {
			return userJSONObject;
		}

		if (contactJSONObject.has("male")) {
			if (contactJSONObject.getBoolean("male")) {
				contactJSONObject.put("gender", "male");
			}
			else {
				contactJSONObject.put("gender", "female");
			}
		}

		return userJSONObject;
	}

	private JSONArray _processMessages(String dataSourceId, String json) {
		JSONArray jsonArray = new JSONArray();

		JSONArray messagesJSONArray = new JSONArray(json);

		for (int i = 0; i < messagesJSONArray.length(); i++) {
			JSONObject jsonObject = messagesJSONArray.getJSONObject(i);

			String action = jsonObject.getString("action");
			JSONObject objectJSONObject = jsonObject.getJSONObject(
				"objectJSONObject");
			String type = jsonObject.getString("type");

			if (type.equals(DXPEntity.Type.CLASS_NAME_CONTACT)) {
				Date date = DateUtil.newDate();

				JSONObject contactJSONObject = JSONUtil.put(
					"contact", objectJSONObject
				).put(
					"emailAddress", objectJSONObject.getString("emailAddress")
				).put(
					"modifiedDate",
					objectJSONObject.optLong("modifiedDate", date.getTime())
				).put(
					"userId", objectJSONObject.getInt("classPK")
				);

				if (action.equalsIgnoreCase("add")) {
					action = "update";

					contactJSONObject.put(
						"createDate",
						objectJSONObject.optLong("createDate", date.getTime()));
				}
				else if (objectJSONObject.has("createDate") &&
						 action.equalsIgnoreCase("update")) {

					contactJSONObject.put(
						"createDate", objectJSONObject.getLong("createDate"));
				}

				objectJSONObject = contactJSONObject;

				_processGenderField(objectJSONObject);

				type = DXPEntity.Type.CLASS_NAME_USER;
			}
			else if (type.equals(DXPEntity.Type.CLASS_NAME_USER)) {
				jsonObject.putOnce("expando", new JSONArray());
			}

			if (dataSourceId == null) {
				objectJSONObject.put(
					"osbAsahDataSourceId",
					jsonObject.getString("dataSourceId"));
			}
			else {
				objectJSONObject.put("osbAsahDataSourceId", dataSourceId);
			}

			jsonArray.put(
				JSONUtil.put(
					"context",
					JSONUtil.put(
						"action", action
					).put(
						"type", type
					)
				).put(
					"object", objectJSONObject
				));
		}

		return jsonArray;
	}

	@Autowired
	private MessageBus _messageBus;

}