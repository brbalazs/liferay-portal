/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.target.platform.indexer;

import java.net.URI;

import java.util.List;

/**
 * @author Raymond Augé
 */
public interface IndexValidator {

	public List<String> validate(List<URI> indexURIs) throws Exception;

}