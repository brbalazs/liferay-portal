/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osgi.service.tracker.collections.map;

import org.osgi.framework.ServiceReference;

/**
 * @author Carlos Sierra Andrés
 */
public class PropertyServiceReferenceMapper<T, S>
	implements ServiceReferenceMapper<T, S> {

	public PropertyServiceReferenceMapper(String propertyKey) {
		_propertyKey = propertyKey;
	}

	@Override
	public void map(ServiceReference<S> serviceReference, Emitter<T> emitter) {
		Object propertyValue = serviceReference.getProperty(_propertyKey);

		if (propertyValue == null) {
			return;
		}

		if (propertyValue instanceof Object[]) {
			for (T t : (T[])propertyValue) {
				emitter.emit(t);
			}
		}
		else {
			emitter.emit((T)propertyValue);
		}
	}

	private final String _propertyKey;

}