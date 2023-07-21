/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.target.platform.indexer.internal;

import com.liferay.portal.target.platform.indexer.IndexValidator;
import com.liferay.portal.target.platform.indexer.IndexValidatorFactory;

import java.net.URI;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Shuyang Zhou
 */
@Component(immediate = true, service = IndexValidatorFactory.class)
public class IndexValidatorFactoryImpl implements IndexValidatorFactory {

	@Override
	public IndexValidator create(List<URI> targetPlatformIndexURIs) {
		return new DefaultIndexValidator(targetPlatformIndexURIs);
	}

}