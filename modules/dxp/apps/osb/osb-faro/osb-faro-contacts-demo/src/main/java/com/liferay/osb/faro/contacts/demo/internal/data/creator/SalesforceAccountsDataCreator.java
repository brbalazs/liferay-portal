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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class SalesforceAccountsDataCreator extends DataCreator {

	public SalesforceAccountsDataCreator(
		ContactsEngineClient contactsEngineClient, FaroProject faroProject,
		String dataSourceId) {

		super(
			contactsEngineClient, faroProject, "osbasahsalesforceraw",
			"Account");

		_dataSourceId = dataSourceId;

		_salesforceAuditEventsDataCreator =
			new SalesforceAuditEventsDataCreator(
				contactsEngineClient, faroProject, "Account");
	}

	@Override
	public void execute() {
		super.execute();

		_salesforceAuditEventsDataCreator.execute();
	}

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> salesforceAccount = new HashMap<>();

		salesforceAccount.put(
			"AnnualRevenue", number.numberBetween(0, 1000) * 1000);
		salesforceAccount.put("BillingCity", address.city());
		salesforceAccount.put("BillingCountry", address.country());
		salesforceAccount.put("BillingPostalCode", address.zipCode());
		salesforceAccount.put("BillingState", address.state());
		salesforceAccount.put("BillingStreet", address.streetAddress());
		salesforceAccount.put(
			"CurrencyIsoCode",
			_currencyIsoCodes.get(random.nextInt(_currencyIsoCodes.size())));
		salesforceAccount.put("id", internet.uuid());

		salesforceAccount.put("Description", company.catchPhrase());

		salesforceAccount.put("Fax", phoneNumber.phoneNumber());

		salesforceAccount.put("Industry", company.industry());
		salesforceAccount.put("LastModifiedDate", formatDate(new Date()));
		salesforceAccount.put("Name", company.name());
		salesforceAccount.put(
			"NumberOfEmployees", number.numberBetween(1, 100000));
		salesforceAccount.put("osbAsahDataSourceId", _dataSourceId);
		salesforceAccount.put("Ownership", "Private");
		salesforceAccount.put("Phone", phoneNumber.phoneNumber());
		salesforceAccount.put("ShippingCity", address.city());
		salesforceAccount.put("ShippingCountry", address.country());
		salesforceAccount.put("ShippingPostalCode", address.zipCode());
		salesforceAccount.put("ShippingState", address.state());
		salesforceAccount.put("ShippingStreet", address.streetAddress());
		salesforceAccount.put("Type", "Customer");
		salesforceAccount.put("Website", "https://" + internet.url());
		salesforceAccount.put("YearStarted", number.numberBetween(1900, 2019));

		_salesforceAuditEventsDataCreator.create(
			new Object[] {salesforceAccount});

		return salesforceAccount;
	}

	private static final List<String> _currencyIsoCodes = Arrays.asList(
		"CNY", "EUR", "GBP", "USD");

	private final String _dataSourceId;
	private final SalesforceAuditEventsDataCreator
		_salesforceAuditEventsDataCreator;

}