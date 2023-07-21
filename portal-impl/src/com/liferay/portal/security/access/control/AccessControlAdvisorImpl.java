/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.access.control;

import com.liferay.portal.kernel.security.access.control.AccessControlPolicy;
import com.liferay.portal.kernel.security.access.control.AccessControlThreadLocal;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Tomas Polesovsky
 * @author Igor Spasic
 * @author Michael C. Han
 * @author Raymond Augé
 */
public class AccessControlAdvisorImpl implements AccessControlAdvisor {

	@Override
	public void accept(
			MethodInvocation methodInvocation,
			AccessControlled accessControlled)
		throws SecurityException {

		if (AccessControlThreadLocal.isRemoteAccess()) {
			for (AccessControlPolicy accessControlPolicy :
					_accessControlPolicies) {

				accessControlPolicy.onServiceRemoteAccess(
					methodInvocation.getMethod(),
					methodInvocation.getArguments(), accessControlled);
			}
		}
		else {
			for (AccessControlPolicy accessControlPolicy :
					_accessControlPolicies) {

				accessControlPolicy.onServiceAccess(
					methodInvocation.getMethod(),
					methodInvocation.getArguments(), accessControlled);
			}
		}
	}

	private static final List<AccessControlPolicy> _accessControlPolicies =
		new CopyOnWriteArrayList<>();
	private static final ServiceTracker<?, ?> _serviceTracker;

	private static class AccessControlPolicyTrackerCustomizer
		implements ServiceTrackerCustomizer
			<AccessControlPolicy, AccessControlPolicy> {

		@Override
		public AccessControlPolicy addingService(
			ServiceReference<AccessControlPolicy> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			AccessControlPolicy accessControlPolicy = registry.getService(
				serviceReference);

			_accessControlPolicies.add(accessControlPolicy);

			return accessControlPolicy;
		}

		@Override
		public void modifiedService(
			ServiceReference<AccessControlPolicy> serviceReference,
			AccessControlPolicy accessControlPolicy) {
		}

		@Override
		public void removedService(
			ServiceReference<AccessControlPolicy> serviceReference,
			AccessControlPolicy accessControlPolicy) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			_accessControlPolicies.remove(accessControlPolicy);
		}

	}

	static {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			AccessControlPolicy.class,
			new AccessControlPolicyTrackerCustomizer());

		_serviceTracker.open();
	}

}