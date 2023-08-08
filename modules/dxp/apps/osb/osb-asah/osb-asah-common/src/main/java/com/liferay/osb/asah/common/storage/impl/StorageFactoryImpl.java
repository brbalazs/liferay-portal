/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import com.liferay.osb.asah.common.storage.Storage;
import com.liferay.osb.asah.common.storage.StorageConfiguration;
import com.liferay.osb.asah.common.storage.StorageFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class StorageFactoryImpl implements StorageFactory {

	@Override
	public Storage getStorage(StorageConfiguration storageConfiguration) {
		LocalStorage localStorage = new LocalStorage(
			_jsonAvroTransformer, storageConfiguration);

		localStorage.setGoogleStorageArchiver(_googleStorageArchiver);

		return localStorage;
	}

	@Autowired(required = false)
	private GoogleStorageArchiver _googleStorageArchiver;

	@Autowired
	private JSONAvroTransformer _jsonAvroTransformer;

}