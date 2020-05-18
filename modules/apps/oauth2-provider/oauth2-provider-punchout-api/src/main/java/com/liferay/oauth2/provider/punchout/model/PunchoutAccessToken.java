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

	public long getExpiresIn() {
		return _expiresIn;
	}

	public long getIssuedAt() {
		return _issuedAt;
	}

	public byte[] getToken() {
		return _token;
	}

	public String getUserEmailAddress() {
		return _userEmailAddress;
	}

	public void setExpiresIn(long expiresIn) {
		_expiresIn = expiresIn;
	}

	public void setIssuedAt(long issuedAt) {
		_issuedAt = issuedAt;
	}

	public void setToken(byte[] token) {
		_token = token;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		_userEmailAddress = userEmailAddress;
	}

	private long _expiresIn;
	private long _issuedAt;
	private byte[] _token;
	private String _userEmailAddress;

}