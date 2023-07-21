/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.ignore.duplicates.wrapper.internal;

import com.liferay.document.library.kernel.store.Store;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"service.ranking:Integer=" + IgnoreDuplicatesStore.SERVICE_RANKING,
		"store.type=com.liferay.portal.store.file.system.FileSystemStore"
	},
	service = Store.class
)
public class IgnoreDuplicatesFileSystemStoreWrapper
	extends IgnoreDuplicatesStore {

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(&(service.ranking<=" + (IgnoreDuplicatesStore.SERVICE_RANKING - 1) + ")(store.type=com.liferay.portal.store.file.system.FileSystemStore)(!(current.store=*)))"
	)
	protected void setStore(Store store) {
		this.store = store;
	}

	protected void unsetStore(Store store) {
		if (this.store == store) {
			this.store = null;
		}
	}

}