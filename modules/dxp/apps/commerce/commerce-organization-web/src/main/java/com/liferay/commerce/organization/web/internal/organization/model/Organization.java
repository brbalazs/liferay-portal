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

import java.util.List;

/**
 * @author Alessio Antonio Rendina
 */
public class Organization {

	public Organization(
		long organizationId, long parentOrganizationId,
		List<Organization> suborganizations, AccountList accountList,
		UserList userList) {

		_organizationId = organizationId;
		_parentOrganizationId = parentOrganizationId;
		_suborganizations = suborganizations;
		_accountList = accountList;
		_userList = userList;

		if ((_suborganizations != null) && (_suborganizations.size() > 0)) {
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

	public long getOrganizationId() {
		return _organizationId;
	}

	public long getParentOrganizationId() {
		return _parentOrganizationId;
	}

	public List<Organization> getSuborganizations() {
		return _suborganizations;
	}

	public UserList getUserList() {
		return _userList;
	}

	private final AccountList _accountList;
	private final boolean _lastLevel;
	private final long _organizationId;
	private final long _parentOrganizationId;
	private final List<Organization> _suborganizations;
	private final UserList _userList;

}