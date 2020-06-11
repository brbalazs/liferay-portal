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

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.petra.string.StringPool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Matthew Kong
 */
public class LiferayUsersDataCreator extends DataCreator {

	public LiferayUsersDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(contactsEngineClient, faroProject, "osbasahdxpraw", "users");

		_dataSourceId = dataSourceId;
	}

	public String getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> liferayUser = new HashMap<>();

		liferayUser.put("birthday", dateAndTime.past(36500, TimeUnit.DAYS));

		Map<String, Object> contact = new HashMap<>();

		String firstName = name.firstName();
		String lastName = name.lastName();

		String emailAddress = internet.emailAddress(
			firstName + StringPool.PERIOD + lastName);

		contact.put("emailAddress", emailAddress);

		contact.put("firstName", firstName);

		String jobTitle = company.profession();

		contact.put("jobTitle", jobTitle);

		String gender = "male";

		if (bool.bool()) {
			gender = "female";
		}

		contact.put("gender", gender);

		contact.put("lastName", lastName);

		contact.put("modifiedDate", System.currentTimeMillis());

		long userId = number.randomNumber(8, false);

		contact.put("userId", userId);

		liferayUser.put("contact", contact);

		liferayUser.put("emailAddress", emailAddress);
		liferayUser.put("firstName", firstName);
		liferayUser.put("jobTitle", jobTitle);
		liferayUser.put("lastName", lastName);
		liferayUser.put("modifiedDate", System.currentTimeMillis());
		liferayUser.put("osbAsahDataSourceId", _dataSourceId);
		liferayUser.put("screenName", firstName + StringPool.PERIOD + lastName);
		liferayUser.put("userId", userId);
		liferayUser.put("uuid", internet.uuid());

		return liferayUser;
	}

	private final String _dataSourceId;

}