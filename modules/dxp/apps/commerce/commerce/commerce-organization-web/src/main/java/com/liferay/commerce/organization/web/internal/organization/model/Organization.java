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

package com.liferay.commerce.organization.web.internal.organization.model;

/**
 * @author Alessio Antonio Rendina
 */
public class Organization {

	public Organization(
		long organizationId, long parentOrganizationId, String name,
		OrganizationList organizations, AccountList accountList,
		UserList userList) {

		_organizationId = organizationId;
		_parentOrganizationId = parentOrganizationId;
		_name = name;
		_organizations = organizations;
		_accountList = accountList;
		_userList = userList;

		if ((_organizations != null) && (_organizations.getTotal() > 0)) {
			_lastLevel = false;
		}
		else {
			_lastLevel = true;
		}
	}

	public AccountList getAccountList() {
		return _accountList;
	}

	public boolean getLastLevel() {
		return _lastLevel;
	}

	public String getName() {
		return _name;
	}

	public long getOrganizationId() {
		return _organizationId;
	}

	public OrganizationList getOrganizations() {
		return _organizations;
	}

	public long getParentOrganizationId() {
		return _parentOrganizationId;
	}

	public UserList getUserList() {
		return _userList;
	}

	private final AccountList _accountList;
	private final boolean _lastLevel;
	private final String _name;
	private final long _organizationId;
	private final OrganizationList _organizations;
	private final long _parentOrganizationId;
	private final UserList _userList;

}