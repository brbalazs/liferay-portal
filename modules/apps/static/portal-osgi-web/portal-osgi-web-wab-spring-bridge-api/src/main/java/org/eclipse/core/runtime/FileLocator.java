/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package org.eclipse.core.runtime;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.io.IOException;

import java.net.URL;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.osgi.service.urlconversion.URLConverter;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * This is a dummy copy of what's needed to make spring annotation processing
 * work in OSGi.
 *
 * @author Raymond Augé
 */
@Component(immediate = true, service = {})
public class FileLocator {

	public static URL resolve(URL url) throws IOException {
		String protocol = url.getProtocol();

		_lock.lock();

		try {
			if (_serviceTrackerMap == null) {
				Bundle bundle = FrameworkUtil.getBundle(FileLocator.class);

				_serviceTrackerMap =
					ServiceTrackerMapFactory.openSingleValueMap(
						bundle.getBundleContext(), URLConverter.class,
						"protocol");
			}

			URLConverter converter = _serviceTrackerMap.getService(protocol);

			if (converter == null) {
				return url;
			}

			return converter.resolve(url);
		}
		finally {
			_lock.unlock();
		}
	}

	@Deactivate
	protected void deactivate() {
		_lock.lock();

		try {
			if (_serviceTrackerMap != null) {
				_serviceTrackerMap.close();

				_serviceTrackerMap = null;
			}
		}
		finally {
			_lock.unlock();
		}
	}

	private static final ReentrantLock _lock = new ReentrantLock(true);
	private static volatile ServiceTrackerMap<String, URLConverter>
		_serviceTrackerMap;

}