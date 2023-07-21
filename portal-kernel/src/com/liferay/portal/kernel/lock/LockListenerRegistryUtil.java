/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.lock;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Chow
 * @author Peter Fellwock
 */
public class LockListenerRegistryUtil {

	public static LockListener getLockListener(String className) {
		return _instance._lockListeners.get(className);
	}

	private LockListenerRegistryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			LockListener.class, new LockListenerServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static final LockListenerRegistryUtil _instance =
		new LockListenerRegistryUtil();

	private final Map<String, LockListener> _lockListeners = new HashMap<>();
	private final ServiceTracker<?, LockListener> _serviceTracker;

	private class LockListenerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<LockListener, LockListener> {

		@Override
		public LockListener addingService(
			ServiceReference<LockListener> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			LockListener lockListener = registry.getService(serviceReference);

			_lockListeners.put(lockListener.getClassName(), lockListener);

			return lockListener;
		}

		@Override
		public void modifiedService(
			ServiceReference<LockListener> serviceReference,
			LockListener lockListener) {
		}

		@Override
		public void removedService(
			ServiceReference<LockListener> serviceReference,
			LockListener lockListener) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			_lockListeners.remove(lockListener);
		}

	}

}