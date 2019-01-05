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
public class Member {

	public Member(long memberId, String name, String email, String[] roles) {
		_memberId = memberId;
		_name = name;
		_email = email;
		_roles = roles;
	}

	public String getEmail() {
		return _email;
	}

	public long getMemberId() {
		return _memberId;
	}

	public String getName() {
		return _name;
	}

	public String[] getRoles() {
		return _roles;
	}

	private final String _email;
	private final long _memberId;
	private final String _name;
	private final String[] _roles;

}