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

package com.liferay.osb.faro.provisioning.client.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Kong
 */
public class ProductConstants {

	public static final String BASIC_PRODUCT_ENTRY_ID = "KOR-36421";

	public static final String BASIC_PRODUCT_NAME = "Analytics Cloud Basic";

	public static final String BUSINESS_CONTACTS_PRODUCT_ENTRY_ID = "KOR-36431";

	public static final String BUSINESS_CONTACTS_PRODUCT_NAME =
		"Analytics Cloud Business Contacts";

	public static final String BUSINESS_PRODUCT_ENTRY_ID = "KOR-36425";

	public static final String BUSINESS_PRODUCT_NAME =
		"Analytics Cloud Business";

	public static final String BUSINESS_TRACKED_PAGES_PRODUCT_ENTRY_ID =
		"KOR-36434";

	public static final String BUSINESS_TRACKED_PAGES_PRODUCT_NAME =
		"Analytics Cloud Business Tracked Pages";

	public static final String ENTERPRISE_CONTACTS_PRODUCT_ENTRY_ID =
		"KOR-36437";

	public static final String ENTERPRISE_CONTACTS_PRODUCT_NAME =
		"Analytics Cloud Enterprise Contacts";

	public static final String ENTERPRISE_PRODUCT_ENTRY_ID = "KOR-36428";

	public static final String ENTERPRISE_PRODUCT_NAME =
		"Analytics Cloud Enterprise";

	public static final String ENTERPRISE_TRACKED_PAGES_PRODUCT_ENTRY_ID =
		"KOR-36440";

	public static final String ENTERPRISE_TRACKED_PAGES_PRODUCT_NAME =
		"Analytics Cloud Enterprise Tracked Pages";

	public static final int OSB_OFFERING_ENTRY_STATUS_ACTIVE = 1;

	public static String[] getProductEntryIds() {
		Set<String> keys = _productNames.keySet();

		return keys.toArray(new String[0]);
	}

	public static String getProductName(String productEntryId) {
		return _productNames.get(productEntryId);
	}

	private static final Map<String, String> _productNames =
		new HashMap<String, String>() {
			{
				put(BASIC_PRODUCT_ENTRY_ID, BASIC_PRODUCT_NAME);
				put(
					BUSINESS_CONTACTS_PRODUCT_ENTRY_ID,
					BUSINESS_CONTACTS_PRODUCT_NAME);
				put(BUSINESS_PRODUCT_ENTRY_ID, BUSINESS_PRODUCT_NAME);
				put(
					BUSINESS_TRACKED_PAGES_PRODUCT_ENTRY_ID,
					BUSINESS_TRACKED_PAGES_PRODUCT_NAME);
				put(
					ENTERPRISE_CONTACTS_PRODUCT_ENTRY_ID,
					ENTERPRISE_CONTACTS_PRODUCT_NAME);
				put(ENTERPRISE_PRODUCT_ENTRY_ID, ENTERPRISE_PRODUCT_NAME);
				put(
					ENTERPRISE_TRACKED_PAGES_PRODUCT_ENTRY_ID,
					ENTERPRISE_TRACKED_PAGES_PRODUCT_NAME);
			}
		};

}