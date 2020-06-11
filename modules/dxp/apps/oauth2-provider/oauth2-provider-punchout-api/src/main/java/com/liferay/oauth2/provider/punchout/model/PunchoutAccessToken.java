/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.oauth2.provider.punchout.model;

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

	public String getPunchoutReturnURL() {
		return _punchoutReturnURL;
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

	public void setPunchoutReturnURL(String punchoutReturnURL) {
		_punchoutReturnURL = punchoutReturnURL;
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
	private String _punchoutReturnURL;
	private byte[] _token;
	private String _userEmailAddress;

}