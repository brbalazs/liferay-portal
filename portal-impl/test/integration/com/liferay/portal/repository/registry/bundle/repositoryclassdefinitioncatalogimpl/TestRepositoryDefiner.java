/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.repository.registry.bundle.repositoryclassdefinitioncatalogimpl;

import com.liferay.portal.kernel.repository.DocumentRepository;
import com.liferay.portal.kernel.repository.RepositoryConfiguration;
import com.liferay.portal.kernel.repository.RepositoryConfigurationBuilder;
import com.liferay.portal.kernel.repository.registry.CapabilityRegistry;
import com.liferay.portal.kernel.repository.registry.RepositoryDefiner;
import com.liferay.portal.kernel.repository.registry.RepositoryEventRegistry;
import com.liferay.portal.kernel.repository.registry.RepositoryFactoryRegistry;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = RepositoryDefiner.class
)
public class TestRepositoryDefiner implements RepositoryDefiner {

	public TestRepositoryDefiner() {
		RepositoryConfigurationBuilder repositoryConfigurationBuilder =
			new RepositoryConfigurationBuilder();

		_repositoryConfiguration = repositoryConfigurationBuilder.build();
	}

	@Override
	public String getClassName() {
		return TestRepositoryDefiner.class.getName();
	}

	@Override
	public RepositoryConfiguration getRepositoryConfiguration() {
		return _repositoryConfiguration;
	}

	@Override
	public String getRepositoryTypeLabel(Locale locale) {
		return null;
	}

	@Override
	public boolean isExternalRepository() {
		return false;
	}

	@Override
	public void registerCapabilities(
		CapabilityRegistry<DocumentRepository> capabilityRegistry) {
	}

	@Override
	public void registerRepositoryEventListeners(
		RepositoryEventRegistry repositoryEventRegistry) {
	}

	@Override
	public void registerRepositoryFactory(
		RepositoryFactoryRegistry repositoryFactoryRegistry) {
	}

	private final RepositoryConfiguration _repositoryConfiguration;

}