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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Matthew Kong
 */
public class SalesforceIndividualsDataCreator extends DataCreator {

	public SalesforceIndividualsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(
			contactsEngineClient, faroProject, "osbasahsalesforceraw",
			"individuals");

		_dataSourceId = dataSourceId;
		_salesforceAuditEventsDataCreator =
			new SalesforceAuditEventsDataCreator(
				contactsEngineClient, faroProject, "individuals");
	}

	@Override
	public void execute() {
		super.execute();

		_salesforceAuditEventsDataCreator.execute();
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> salesforceIndividual = new HashMap<>();

		Map<String, Object> liferayUser = new HashMap<>();
		Map<String, Object> salesforceAccount = new HashMap<>();

		if (params != null) {
			liferayUser = (Map<String, Object>)params[0];
			salesforceAccount = (Map<String, Object>)params[1];
		}

		Object accountPKs = salesforceAccount.get("id");

		if (accountPKs != null) {
			salesforceIndividual.put(
				"accountPKs", Collections.singletonList(accountPKs));
		}

		salesforceIndividual.put(
			"birthDate",
			liferayUser.getOrDefault(
				"birthday", dateAndTime.past(18250, TimeUnit.DAYS)));
		salesforceIndividual.put("city", address.city());
		salesforceIndividual.put(
			"company", salesforceAccount.getOrDefault("Name", company.name()));

		if (!salesforceAccount.isEmpty()) {
			salesforceIndividual.put(
				"contactId", number.randomNumber(8, false));
		}

		salesforceIndividual.put("country", address.country());
		salesforceIndividual.put("department", commerce.department());
		salesforceIndividual.put("description", company.buzzword());
		salesforceIndividual.put("doNotCall", bool.bool());

		String firstName = (String)liferayUser.getOrDefault(
			"firstName", name.firstName());
		String lastName = (String)liferayUser.getOrDefault(
			"lastName", name.lastName());

		salesforceIndividual.put(
			"email",
			liferayUser.getOrDefault(
				"emailAddress",
				internet.emailAddress(
					firstName + StringPool.PERIOD + lastName)));

		salesforceIndividual.put("fax", phoneNumber.phoneNumber());
		salesforceIndividual.put("firstName", firstName);
		salesforceIndividual.put(
			"fullName", firstName + StringPool.SPACE + lastName);
		salesforceIndividual.put("id", internet.uuid());
		salesforceIndividual.put(
			"industry",
			salesforceAccount.getOrDefault("Industry", company.industry()));
		salesforceIndividual.put("lastName", lastName);
		salesforceIndividual.put("middleName", name.firstName());
		salesforceIndividual.put("mobilePhone", phoneNumber.cellPhone());
		salesforceIndividual.put("modifiedDate", dateFormat.format(new Date()));
		salesforceIndividual.put("osbAsahDataSourceId", _dataSourceId);
		salesforceIndividual.put("phone", phoneNumber.phoneNumber());
		salesforceIndividual.put("postalCode", address.zipCode());
		salesforceIndividual.put("state", address.state());
		salesforceIndividual.put("street", address.streetAddress());
		salesforceIndividual.put("suffix", name.suffix());
		salesforceIndividual.put("title", name.title());

		_salesforceAuditEventsDataCreator.create(
			new Object[] {salesforceIndividual});

		return salesforceIndividual;
	}

	private final String _dataSourceId;
	private final SalesforceAuditEventsDataCreator
		_salesforceAuditEventsDataCreator;

}