/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.membershippolicy.bundle.usergroupmembershippolicyfactoryimpl;

import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.security.membershippolicy.UserGroupMembershipPolicy;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = UserGroupMembershipPolicy.class
)
public class TestUserGroupMembershipPolicy
	implements UserGroupMembershipPolicy {

	@Override
	public void checkMembership(
		long[] userIds, long[] addUserGroupIds, long[] removeUserGroupIds) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public boolean isMembershipAllowed(long userId, long userGroupId) {
		if (userId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isMembershipRequired(long userId, long userGroupId) {
		if (userId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public void propagateMembership(
		long[] userIds, long[] addUserGroupIds, long[] removeUserGroupIds) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy() {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy(UserGroup userGroup) {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy(
		UserGroup userGroup, UserGroup oldUserGroup,
		Map<String, Serializable> oldExpandoAttributes) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}