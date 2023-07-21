/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.instance;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderContext;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.dynamic.data.mapping.storage.StorageAdapter;
import com.liferay.dynamic.data.mapping.storage.StorageAdapterRegistry;
import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "ddm.data.provider.instance.id=ddm-storage-types",
	service = DDMDataProvider.class
)
public class DDMStorageTypesDataProvider implements DDMDataProvider {

	@Override
	public List<KeyValuePair> getData(
			DDMDataProviderContext ddmDataProviderContext)
		throws DDMDataProviderException {

		return Collections.emptyList();
	}

	@Override
	public DDMDataProviderResponse getData(
			DDMDataProviderRequest ddmDataProviderRequest)
		throws DDMDataProviderException {

		List<KeyValuePair> data = new ArrayList<>();

		StorageAdapter storageAdapter =
			_storageAdapterRegistry.getDefaultStorageAdapter();

		String storageTypeDefault = storageAdapter.getStorageType();

		data.add(new KeyValuePair(storageTypeDefault, storageTypeDefault));

		Set<String> storageTypes = _storageAdapterRegistry.getStorageTypes();

		for (String storageType : storageTypes) {
			if (storageType.equals(storageTypeDefault)) {
				continue;
			}

			data.add(new KeyValuePair(storageType, storageType));
		}

		return DDMDataProviderResponse.of(
			DDMDataProviderResponseOutput.of("Default-Output", "list", data));
	}

	@Override
	public Class<?> getSettings() {
		throw new UnsupportedOperationException();
	}

	@Reference
	private StorageAdapterRegistry _storageAdapterRegistry;

}