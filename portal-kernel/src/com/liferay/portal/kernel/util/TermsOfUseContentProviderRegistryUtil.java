/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.collections.ServiceRegistrationMap;
import com.liferay.registry.collections.ServiceRegistrationMapImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eduardo García
 */
public class TermsOfUseContentProviderRegistryUtil {

	public static String[] getClassNames() {
		return _instance._getClassNames();
	}

	public static TermsOfUseContentProvider getTermsOfUseContentProvider() {
		return _instance._getTermsOfUseContentProvider();
	}

	public static TermsOfUseContentProvider getTermsOfUseContentProvider(
		String className) {

		return _instance._getTermsOfUseContentProvider(className);
	}

	public static List<TermsOfUseContentProvider>
		getTermsOfUseContentProviders() {

		return _instance._getTermsOfUseContentProviders();
	}

	public static void register(
		TermsOfUseContentProvider termsOfUseContentProvider) {

		_instance._register(termsOfUseContentProvider);
	}

	public static void unregister(
		TermsOfUseContentProvider termsOfUseContentProvider) {

		_instance._unregister(termsOfUseContentProvider);
	}

	private TermsOfUseContentProviderRegistryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			TermsOfUseContentProvider.class,
			new TermsOfUseContentProviderServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private String[] _getClassNames() {
		Set<String> classNames = _termsOfUseContentProviders.keySet();

		return classNames.toArray(new String[0]);
	}

	private TermsOfUseContentProvider _getTermsOfUseContentProvider() {
		List<TermsOfUseContentProvider> termsOfUseContentProviders =
			_getTermsOfUseContentProviders();

		if (termsOfUseContentProviders.isEmpty()) {
			return null;
		}

		return termsOfUseContentProviders.get(0);
	}

	private TermsOfUseContentProvider _getTermsOfUseContentProvider(
		String className) {

		return _termsOfUseContentProviders.get(className);
	}

	private List<TermsOfUseContentProvider> _getTermsOfUseContentProviders() {
		return ListUtil.fromMapValues(_termsOfUseContentProviders);
	}

	private void _register(
		TermsOfUseContentProvider termsOfUseContentProvider) {

		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<TermsOfUseContentProvider> serviceRegistration =
			registry.registerService(
				TermsOfUseContentProvider.class, termsOfUseContentProvider);

		_serviceRegistrations.put(
			termsOfUseContentProvider, serviceRegistration);
	}

	private void _unregister(
		TermsOfUseContentProvider termsOfUseContentProvider) {

		ServiceRegistration<TermsOfUseContentProvider> serviceRegistration =
			_serviceRegistrations.remove(termsOfUseContentProvider);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	private static final TermsOfUseContentProviderRegistryUtil _instance =
		new TermsOfUseContentProviderRegistryUtil();

	private final ServiceRegistrationMap<TermsOfUseContentProvider>
		_serviceRegistrations = new ServiceRegistrationMapImpl<>();
	private final ServiceTracker
		<TermsOfUseContentProvider, TermsOfUseContentProvider> _serviceTracker;
	private final Map<String, TermsOfUseContentProvider>
		_termsOfUseContentProviders = new ConcurrentHashMap<>();

	private class TermsOfUseContentProviderServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<TermsOfUseContentProvider, TermsOfUseContentProvider> {

		@Override
		public TermsOfUseContentProvider addingService(
			ServiceReference<TermsOfUseContentProvider> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			TermsOfUseContentProvider termsOfUseContentProvider =
				registry.getService(serviceReference);

			_termsOfUseContentProviders.put(
				termsOfUseContentProvider.getClassName(),
				termsOfUseContentProvider);

			return termsOfUseContentProvider;
		}

		@Override
		public void modifiedService(
			ServiceReference<TermsOfUseContentProvider> serviceReference,
			TermsOfUseContentProvider termsOfUseContentProvider) {
		}

		@Override
		public void removedService(
			ServiceReference<TermsOfUseContentProvider> serviceReference,
			TermsOfUseContentProvider termsOfUseContentProvider) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			_termsOfUseContentProviders.remove(
				termsOfUseContentProvider.getClassName());
		}

	}

}