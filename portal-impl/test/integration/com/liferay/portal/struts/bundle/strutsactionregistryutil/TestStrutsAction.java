/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts.bundle.strutsactionregistryutil;

import com.liferay.portal.kernel.struts.StrutsAction;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"path=TestStrutsAction", "service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = StrutsAction.class
)
public class TestStrutsAction implements StrutsAction {

	@Override
	public String execute(
		HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@Override
	public String execute(
		StrutsAction originalStrutsAction, HttpServletRequest request,
		HttpServletResponse response) {

		_atomicBoolean.set(Boolean.TRUE);

		return null;
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}