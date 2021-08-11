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

package com.liferay.osb.faro.engine.client.internal;

import com.liferay.ip.geocoder.IPGeocoder;
import com.liferay.ip.geocoder.IPInfo;
import com.liferay.osb.faro.engine.client.HubSpotEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.FaroUser;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = HubSpotEngineClient.class)
public class HubSpotEngineClientImpl implements HubSpotEngineClient {

	@Override
	public void submitUsageForm(
		FaroProject faroProject, FaroUser faroUser, double usage) {

		String formId = null;

		if (usage >= 1) {
			formId = "cd685b23-ba61-46d2-bda2-ef2eac9ec1d6";
		}
		else if (usage >= .9) {
			formId = "5694c2e0-6dcb-44d4-a586-995b22640b33";
		}
		else if (usage >= .75) {
			formId = "c1ade6b2-2169-4877-ac3c-c60c605e2382";
		}
		else if (usage >= .5) {
			formId = "7a68b334-de8f-4479-82b0-cbeabd09be8c";
		}
		else if (usage >= .25) {
			formId = "5bcb30c3-5466-4dc4-8ce7-d2830fb5bd34";
		}
		else {
			return;
		}

		_getRestTemplate().postForEntity(
			_HUBSPOT_API_URL,
			_getRequestBody(
				"ac_usage", GetterUtil.getDouble(_decimalFormat.format(usage)),
				"ac_workspace_id", faroProject.getGroupId(),
				"ac_workspace_name", faroProject.getName(), "email",
				faroUser.getEmailAddress()),
			Void.class, _getURIVariables(formId));
	}

	@Override
	public void submitWorkspaceExpirationForm(
		FaroProject faroProject, FaroUser faroUser) {

		_getRestTemplate().postForEntity(
			_HUBSPOT_API_URL,
			_getRequestBody(
				"ac_workspace_id", faroProject.getGroupId(),
				"ac_workspace_name", faroProject.getName(),
				"ac_last_activity_date",
				_getNormalizedTime(faroProject.getLastAccessTime()), "email",
				faroUser.getEmailAddress()),
			Void.class,
			_getURIVariables("c01de9b3-b3c1-4401-a3e1-5b1a53802e1c"));
	}

	@Override
	public void submitWorkspaceUserForm(
		FaroProject faroProject, FaroUser faroUser, boolean primaryOwner) {

		User user = _userLocalService.fetchUser(faroUser.getLiveUserId());

		if (user == null) {
			return;
		}

		String country = null;

		if (_ipGeocoder != null) {
			IPInfo ipInfo = _ipGeocoder.getIPInfo(user.getLoginIP());

			country = ipInfo.getCountryName();
		}

		_getRestTemplate().postForEntity(
			_HUBSPOT_API_URL,
			_getRequestBody(
				"ac_primary_owner", primaryOwner, "ac_workspace_id",
				faroProject.getGroupId(), "ac_workspace_name",
				faroProject.getName(), "country", country, "email",
				faroUser.getEmailAddress(), "firstname", user.getFirstName(),
				"lastname", user.getLastName(), "recent_conversion_type",
				"Software Download"),
			Void.class,
			_getURIVariables("c0bc56bd-9f5e-479a-9de4-9db4fc5555d3"));
	}

	private long _getNormalizedTime(long time) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(time);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTimeInMillis();
	}

	private Map<String, Object> _getRequestBody(Object... params) {
		Map<String, Object> body = new HashMap<>();

		List<Map<String, Object>> fields = new ArrayList<>();

		for (int i = 0; i < params.length; i += 2) {
			Map<String, Object> field = new HashMap<>();

			field.put("name", String.valueOf(params[i]));
			field.put("value", String.valueOf(params[i + 1]));

			fields.add(field);
		}

		body.put("fields", fields);

		return body;
	}

	private RestTemplate _getRestTemplate() {
		return new RestTemplate() {

			@Override
			public <T> ResponseEntity<T> postForEntity(
				String url, Object request, Class<T> responseType,
				Map<String, ?> uriVariables) {

				if (!_HUBSPOT_ENABLED) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}

				return super.postForEntity(
					url, request, responseType, uriVariables);
			}

		};
	}

	private Map<String, Object> _getURIVariables(String formId) {
		Map<String, Object> uriVariables = new HashMap<>();

		uriVariables.put("formId", formId);

		return uriVariables;
	}

	private static final String _HUBSPOT_API_URL =
		"https://api.hsforms.com/submissions/v3/integration/submit/252686" +
			"/{formId}";

	private static final boolean _HUBSPOT_ENABLED = GetterUtil.getBoolean(
		System.getenv("HUBSPOT_ENABLED"));

	private static final DecimalFormat _decimalFormat = new DecimalFormat(
		"#.###");

	@Reference(cardinality = ReferenceCardinality.OPTIONAL)
	private IPGeocoder _ipGeocoder;

	@Reference
	private UserLocalService _userLocalService;

}