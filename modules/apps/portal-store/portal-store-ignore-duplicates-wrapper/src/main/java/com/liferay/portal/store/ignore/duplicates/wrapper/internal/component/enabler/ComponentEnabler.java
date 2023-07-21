/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.ignore.duplicates.wrapper.internal.component.enabler;

import com.liferay.document.library.kernel.store.Store;
import com.liferay.osgi.util.ComponentUtil;
import com.liferay.portal.store.ignore.duplicates.wrapper.internal.IgnoreDuplicatesAdvancedFileSystemStoreWrapper;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Tina Tian
 */
@Component(immediate = true, service = {})
public class ComponentEnabler {

	@Activate
	protected void activate(ComponentContext componentContext) {
		ComponentUtil.enableComponents(
			Store.class,
			"(store.type=com.liferay.portal.store.file.system." +
				"AdvancedFileSystemStore)",
			componentContext,
			IgnoreDuplicatesAdvancedFileSystemStoreWrapper.class);
	}

}