/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.util;

import com.liferay.asset.publisher.util.AssetPublisherHelper;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author  Jürgen Kappler
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class AssetPublisherHelperUtil {

	public static AssetPublisherHelper getAssetPublisherHelper() {
		return _serviceTracker.getService();
	}

	private static final ServiceTracker
		<AssetPublisherHelper, AssetPublisherHelper> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(AssetPublisherHelper.class);

		ServiceTracker<AssetPublisherHelper, AssetPublisherHelper>
			serviceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), AssetPublisherHelper.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}