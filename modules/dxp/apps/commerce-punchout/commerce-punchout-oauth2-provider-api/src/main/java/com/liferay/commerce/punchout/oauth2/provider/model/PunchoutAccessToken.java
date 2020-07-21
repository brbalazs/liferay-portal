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

package com.liferay.commerce.punchout.oauth2.provider.model;

import java.util.HashMap;

/**
 * @author Jaclyn Ong
 */
public class PunchoutAccessToken {

	public long getCommerceAccountId() {
		return _commerceAccountId;
	}

	public String getCommerceOrderUuid() {
		return _commerceOrderUuId;
	}

	public String getCurrencyCode() {
		return _currencyCode;
	}

	public long getExpiresIn() {
		return _expiresIn;
	}

	public long getGroupId() {
		return _groupId;
	}

	public long getIssuedAt() {
		return _issuedAt;
	}

	public Object getPunchoutSessionAttribute(String key) {
		return _punchoutSessionAttributes.get(key);
	}

	public HashMap<String, Object> getPunchoutSessionAttributes() {
		return _punchoutSessionAttributes;
	}

	public byte[] getToken() {
		return _token;
	}

	public String getUserEmailAddress() {
		return _userEmailAddress;
	}

	public void setCommerceAccountId(long commerceAccountId) {
		_commerceAccountId = commerceAccountId;
	}

	public void setCommerceOrderUuid(String commerceOrderUuid) {
		_commerceOrderUuId = commerceOrderUuid;
	}

	public void setCurrencyCode(String currencyCode) {
		_currencyCode = currencyCode;
	}

	public void setExpiresIn(long expiresIn) {
		_expiresIn = expiresIn;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setIssuedAt(long issuedAt) {
		_issuedAt = issuedAt;
	}

	public void setPunchoutSessionAttribute(String key, Object value) {
		_punchoutSessionAttributes.put(key, value);
	}

	public void setPunchoutSessionAttributes(
		HashMap<String, Object> punchoutSessionAttributes) {

		_punchoutSessionAttributes = punchoutSessionAttributes;
	}

	public void setToken(byte[] token) {
		_token = token;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		_userEmailAddress = userEmailAddress;
	}

	private long _commerceAccountId;
	private String _commerceOrderUuId;
	private String _currencyCode;
	private long _expiresIn;
	private long _groupId;
	private long _issuedAt;
	private HashMap<String, Object> _punchoutSessionAttributes =
		new HashMap<>();
	private byte[] _token;
	private String _userEmailAddress;

}