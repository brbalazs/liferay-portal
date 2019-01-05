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

package com.liferay.commerce.account.web.internal.model;

/**
 * @author Alessio Antonio Rendina
 */
public class Address {

	public Address(
		long addressId, String address, String referent, String phoneNumber) {

		_addressId = addressId;
		_address = address;
		_referent = referent;
		_phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return _address;
	}

	public long getAddressId() {
		return _addressId;
	}

	public String getPhoneNumber() {
		return _phoneNumber;
	}

	public String getReferent() {
		return _referent;
	}

	private final String _address;
	private final long _addressId;
	private final String _phoneNumber;
	private final String _referent;

}