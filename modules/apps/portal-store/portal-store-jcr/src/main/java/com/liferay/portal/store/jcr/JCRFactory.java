/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.store.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @author Michael Young
 * @author Manuel de la Peña
 */
public interface JCRFactory {

	public Session createSession(String workspaceName)
		throws RepositoryException;

	public void initialize() throws RepositoryException;

	public void prepare() throws RepositoryException;

	public void shutdown();

}