/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.cloud.server.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Di Giorgi
 */
@DataObject(generateConverter = true, publicConverter = false)
public class Project implements JsonSerializable {

	public Project(JsonObject jsonObject) {
		ProjectConverter.fromJson(jsonObject, this);
	}

	public String getApiKey() {
		return _apiKey;
	}

	public String getCallbackHost() {
		return _callbackHost;
	}

	public int getForecastingHistorySize() {
		return _forecastingHistorySize;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	public void setApiKey(String apiKey) {
		_apiKey = apiKey;
	}

	public void setCallbackHost(String callbackHost) {
		_callbackHost = callbackHost;
	}

	public void setForecastingHistorySize(int forecastingHistorySize) {
		_forecastingHistorySize = forecastingHistorySize;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public JsonObject toClientJson() {
		JsonObject jsonObject = toJson();

		Iterator<Map.Entry<String, Object>> iterator = jsonObject.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();

			if (!_clientPropertyKeys.contains(entry.getKey())) {
				iterator.remove();
			}
		}

		return jsonObject;
	}

	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();

		ProjectConverter.toJson(this, jsonObject);

		return jsonObject;
	}

	private static final Set<String> _clientPropertyKeys;

	static {
		_clientPropertyKeys = new HashSet<>();

		_clientPropertyKeys.add("callbackHost");
		_clientPropertyKeys.add("forecastingHistorySize");
	}

	private boolean _active;
	private String _apiKey;
	private String _callbackHost;
	private int _forecastingHistorySize;
	private String _id;
	private String _name;

}