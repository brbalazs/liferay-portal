/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.target.platform.indexer.internal;

import com.liferay.portal.target.platform.indexer.Indexer;
import com.liferay.portal.target.platform.indexer.IndexerFactory;

import java.io.File;

import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shuyang Zhou
 */
@Component(immediate = true, service = IndexerFactory.class)
public class IndexerFactoryImpl implements IndexerFactory {

	@Override
	public Indexer createLPKGIndexer(
		File lpkgFile, Set<String> excludedJarFileNames) {

		return new LPKGIndexer(lpkgFile, excludedJarFileNames);
	}

}