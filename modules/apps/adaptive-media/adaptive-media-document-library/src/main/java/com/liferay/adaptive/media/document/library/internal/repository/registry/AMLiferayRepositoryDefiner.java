/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.document.library.internal.repository.registry;

import com.liferay.portal.kernel.repository.registry.RepositoryDefiner;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(immediate = true, service = RepositoryDefiner.class)
public class AMLiferayRepositoryDefiner
	extends BaseOverridingRepositoryDefiner {

	@Activate
	protected void activate() {
		initializeOverridenRepositoryDefiner(_repositoryDefiner);
	}

	@Deactivate
	protected void deactivate() {
		restoreOverridenRepositoryDefiner(_CLASS_NAME);
	}

	private static final String _CLASS_NAME =
		"com.liferay.portal.repository.liferayrepository.LiferayRepository";

	@Reference(target = "(class.name=" + _CLASS_NAME + ")")
	private RepositoryDefiner _repositoryDefiner;

}