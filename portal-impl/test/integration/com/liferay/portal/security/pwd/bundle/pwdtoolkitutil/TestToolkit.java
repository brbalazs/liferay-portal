/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.pwd.bundle.pwdtoolkitutil;

import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.security.pwd.Toolkit;

import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = Toolkit.class
)
public class TestToolkit implements Toolkit {

	public static String PASSWORD = "shibboleth";

	@Override
	public String generate(PasswordPolicy passwordPolicy) {
		return PASSWORD;
	}

	@Override
	public void validate(
		long userId, String password1, String password2,
		PasswordPolicy passwordPolicy) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void validate(
		String password1, String password2, PasswordPolicy passwordPolicy) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}