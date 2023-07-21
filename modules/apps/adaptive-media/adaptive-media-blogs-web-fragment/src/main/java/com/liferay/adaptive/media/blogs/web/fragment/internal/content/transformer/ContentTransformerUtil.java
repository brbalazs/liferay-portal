/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.blogs.web.fragment.internal.content.transformer;

import com.liferay.adaptive.media.content.transformer.ContentTransformerHandler;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;

import java.util.Iterator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Alejandro Tardín
 */
public class ContentTransformerUtil {

	public static ContentTransformerHandler getContentTransformerHandler() {
		Iterator<ContentTransformerHandler> iterator =
			_instance._contentTransformerHandlers.iterator();

		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}

	private ContentTransformerUtil() {
		Bundle bundle = FrameworkUtil.getBundle(ContentTransformerUtil.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_contentTransformerHandlers = ServiceTrackerListFactory.open(
			bundleContext, ContentTransformerHandler.class);
	}

	private static final ContentTransformerUtil _instance =
		new ContentTransformerUtil();

	private final ServiceTrackerList
		<ContentTransformerHandler, ContentTransformerHandler>
			_contentTransformerHandlers;

}