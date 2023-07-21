/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.internal.render;

import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRendererRegistry;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Pablo Carvalho
 */
public class DDMFormFieldRendererRegistryImpl
	implements DDMFormFieldRendererRegistry {

	public DDMFormFieldRendererRegistryImpl() {
		Class<?> clazz = getClass();

		Bundle bundle = FrameworkUtil.getBundle(clazz);

		_bundleContext = bundle.getBundleContext();

		_serviceTracker = ServiceTrackerFactory.open(
			_bundleContext,
			StringBundler.concat(
				"(&(objectClass=", DDMFormFieldRenderer.class.getName(),
				")(!(objectClass=", clazz.getName(), ")))"),
			new DDMFormFieldRendererServiceTrackerCustomizer());
	}

	@Override
	public DDMFormFieldRenderer getDDMFormFieldRenderer(
		String ddmFormFieldType) {

		List<DDMFormFieldRenderer> ddmFormFieldRenders =
			_ddmFormFieldRenderersMap.get(ddmFormFieldType);

		if ((ddmFormFieldRenders == null) || ddmFormFieldRenders.isEmpty()) {
			return null;
		}

		return ddmFormFieldRenders.get(ddmFormFieldRenders.size() - 1);
	}

	public void setDefaultDDMFormFieldRenderer(
		DDMFormFieldRenderer ddmFormFieldRenderer) {

		ServiceRegistration<DDMFormFieldRenderer> serviceRegistration =
			_bundleContext.registerService(
				DDMFormFieldRenderer.class, ddmFormFieldRenderer, null);

		_serviceRegistrations.put(ddmFormFieldRenderer, serviceRegistration);
	}

	private final BundleContext _bundleContext;
	private final Map<String, List<DDMFormFieldRenderer>>
		_ddmFormFieldRenderersMap = new ConcurrentHashMap<>();
	private final Map
		<DDMFormFieldRenderer, ServiceRegistration<DDMFormFieldRenderer>>
			_serviceRegistrations = new ConcurrentHashMap<>();
	private final ServiceTracker<DDMFormFieldRenderer, DDMFormFieldRenderer>
		_serviceTracker;

	private class DDMFormFieldRendererServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<DDMFormFieldRenderer, DDMFormFieldRenderer> {

		@Override
		public DDMFormFieldRenderer addingService(
			ServiceReference<DDMFormFieldRenderer> serviceReference) {

			DDMFormFieldRenderer ddmFormFieldRenderer =
				_bundleContext.getService(serviceReference);

			for (String supportedDDMFormFieldType :
					ddmFormFieldRenderer.getSupportedDDMFormFieldTypes()) {

				List<DDMFormFieldRenderer> ddmFormFieldRenderers =
					_ddmFormFieldRenderersMap.get(supportedDDMFormFieldType);

				if (ddmFormFieldRenderers == null) {
					ddmFormFieldRenderers = new ArrayList<>();

					_ddmFormFieldRenderersMap.put(
						supportedDDMFormFieldType, ddmFormFieldRenderers);
				}

				ddmFormFieldRenderers.add(ddmFormFieldRenderer);
			}

			return ddmFormFieldRenderer;
		}

		@Override
		public void modifiedService(
			ServiceReference<DDMFormFieldRenderer> serviceReference,
			DDMFormFieldRenderer ddmFormFieldRenderer) {
		}

		@Override
		public void removedService(
			ServiceReference<DDMFormFieldRenderer> serviceReference,
			DDMFormFieldRenderer ddmFormFieldRenderer) {

			_bundleContext.ungetService(serviceReference);

			for (String supportedDDMFormFieldType :
					ddmFormFieldRenderer.getSupportedDDMFormFieldTypes()) {

				List<DDMFormFieldRenderer> ddmFormFieldRenderers =
					_ddmFormFieldRenderersMap.get(supportedDDMFormFieldType);

				if (ddmFormFieldRenderers == null) {
					return;
				}

				ddmFormFieldRenderers.remove(ddmFormFieldRenderer);
			}
		}

	}

}