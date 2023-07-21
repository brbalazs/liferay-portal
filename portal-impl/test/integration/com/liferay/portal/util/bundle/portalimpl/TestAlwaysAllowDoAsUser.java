/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util.bundle.portalimpl;

import com.liferay.portal.kernel.security.auth.AlwaysAllowDoAsUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = AlwaysAllowDoAsUser.class
)
public class TestAlwaysAllowDoAsUser implements AlwaysAllowDoAsUser {

	public static final String ACTION_NAME =
		"/TestAlwaysAllowDoAsUser/action/name";

	public static final String MVC_RENDER_COMMMAND_NAME =
		"/TestAlwaysAllowDoAsUser/mvc/render/command/name";

	public static final String PATH = "/TestAlwaysAllowDoAsUser/";

	public static final String STRUTS_ACTION =
		"/TestAlwaysAllowDoAsUser/struts/action";

	@Override
	public Collection<String> getActionNames() {
		_atomicBoolean.set(Boolean.TRUE);

		Collection<String> actionNames = new ArrayList<>();

		actionNames.add(ACTION_NAME);

		return actionNames;
	}

	@Override
	public Collection<String> getMVCRenderCommandNames() {
		_atomicBoolean.set(Boolean.TRUE);

		Collection<String> mvcRenderCommandNames = new ArrayList<>();

		mvcRenderCommandNames.add(MVC_RENDER_COMMMAND_NAME);

		return mvcRenderCommandNames;
	}

	@Override
	public Collection<String> getPaths() {
		_atomicBoolean.set(Boolean.TRUE);

		Collection<String> paths = new ArrayList<>();

		paths.add(PATH);

		return paths;
	}

	@Override
	public Collection<String> getStrutsActions() {
		_atomicBoolean.set(Boolean.TRUE);

		Collection<String> strutsActions = new ArrayList<>();

		strutsActions.add(STRUTS_ACTION);

		return strutsActions;
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}