/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.membershippolicy.bundle.rolemembershippolicyfactoryimpl;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicy;

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
	service = RoleMembershipPolicy.class
)
public class TestRoleMembershipPolicy implements RoleMembershipPolicy {

	@Override
	public void checkRoles(
		long[] userIds, long[] addRoleIds, long[] removeRoleIds) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public boolean isRoleAllowed(long userId, long roleId) {
		if (userId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isRoleRequired(long userId, long roleId) {
		if (userId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public void propagateRoles(
		long[] userIds, long[] addRoleIds, long[] removeRoleIds) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy() {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy(Role role) {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void verifyPolicy(
		Role role, Role oldRole,
		Map<String, Serializable> oldExpandoAttributes) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}