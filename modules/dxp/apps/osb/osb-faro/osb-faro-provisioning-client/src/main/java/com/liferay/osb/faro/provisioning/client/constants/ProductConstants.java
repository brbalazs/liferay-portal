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

	public static final long BASIC_PRODUCT_ENTRY_ID = 110520630;

	public static final String BASIC_PRODUCT_NAME =
		"Liferay Analytics Cloud Basic";

	public static final long BUSINESS_CONTACTS_PRODUCT_ENTRY_ID = 110520673;

	public static final String BUSINESS_CONTACTS_PRODUCT_NAME =
		"Liferay Analytics Cloud Business Contacts";

	public static final long BUSINESS_PRODUCT_ENTRY_ID = 110520636;

	public static final String BUSINESS_PRODUCT_NAME =
		"Liferay Analytics Cloud Business";

	public static final long BUSINESS_TRACKED_PAGES_PRODUCT_ENTRY_ID =
		110520683;

	public static final String BUSINESS_TRACKED_PAGES_PRODUCT_NAME =
		"Liferay Analytics Cloud Business Tracked Pages";

	public static final long ENTERPRISE_CONTACTS_PRODUCT_ENTRY_ID = 110520692;

	public static final String ENTERPRISE_CONTACTS_PRODUCT_NAME =
		"Liferay Analytics Cloud Enterprise Contacts";

	public static final long ENTERPRISE_PRODUCT_ENTRY_ID = 110520665;

	public static final String ENTERPRISE_PRODUCT_NAME =
		"Liferay Analytics Cloud Enterprise";

	public static final long ENTERPRISE_TRACKED_PAGES_PRODUCT_ENTRY_ID =
		110520700;

	public static final String ENTERPRISE_TRACKED_PAGES_PRODUCT_NAME =
		"Liferay Analytics Cloud Enterprise Tracked Pages";

	public static final int OSB_OFFERING_ENTRY_STATUS_ACTIVE = 1;

	public static Long[] getProductEntryIds() {
		Set<Long> keys = _productNames.keySet();

		return keys.toArray(new Long[0]);
	}

	public static String getProductName(long productEntryId) {
		return _productNames.get(productEntryId);
	}

	private static final Map<Long, String> _productNames =
		new HashMap<Long, String>() {
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