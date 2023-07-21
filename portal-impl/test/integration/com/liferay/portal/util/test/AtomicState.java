/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util.test;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Peter Fellwock
 */
public class AtomicState {

	public AtomicState() {
		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put("test", "AtomicState");

		_serviceRegistration = registry.registerService(
			AtomicBoolean.class, _atomicBoolean, properties);
	}

	public void close() {
		_serviceRegistration.unregister();
	}

	public Boolean get() {
		return _atomicBoolean.get();
	}

	public boolean isSet() {
		if (Boolean.TRUE.equals(_atomicBoolean.get())) {
			return true;
		}

		return false;
	}

	public void reset() {
		_atomicBoolean.set(Boolean.FALSE);
	}

	private final AtomicBoolean _atomicBoolean = new AtomicBoolean();
	private final ServiceRegistration<AtomicBoolean> _serviceRegistration;

}