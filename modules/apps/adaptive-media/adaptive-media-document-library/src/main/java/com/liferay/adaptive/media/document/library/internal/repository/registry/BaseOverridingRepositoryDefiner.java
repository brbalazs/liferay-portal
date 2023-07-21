/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.document.library.internal.repository.registry;

import com.liferay.adaptive.media.processor.AMAsyncProcessor;
import com.liferay.adaptive.media.processor.AMAsyncProcessorLocator;
import com.liferay.document.library.security.io.InputStreamSanitizer;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.repository.DocumentRepository;
import com.liferay.portal.kernel.repository.RepositoryConfiguration;
import com.liferay.portal.kernel.repository.RepositoryException;
import com.liferay.portal.kernel.repository.capabilities.Capability;
import com.liferay.portal.kernel.repository.event.RepositoryEventAware;
import com.liferay.portal.kernel.repository.event.RepositoryEventType;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileEntryWrapper;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.repository.model.FileVersionWrapper;
import com.liferay.portal.kernel.repository.registry.CapabilityRegistry;
import com.liferay.portal.kernel.repository.registry.RepositoryDefiner;
import com.liferay.portal.kernel.repository.registry.RepositoryEventRegistry;
import com.liferay.portal.kernel.repository.registry.RepositoryFactoryRegistry;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.repository.registry.RepositoryClassDefinition;
import com.liferay.portal.repository.registry.RepositoryClassDefinitionCatalog;
import com.liferay.portal.repository.registry.RepositoryClassDefinitionCatalogUtil;

import java.io.InputStream;

import java.lang.reflect.Field;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 * @author Sergio González
 */
public abstract class BaseOverridingRepositoryDefiner
	implements RepositoryDefiner {

	@Override
	public String getClassName() {
		return _overridenRepositoryDefiner.getClassName();
	}

	@Override
	public RepositoryConfiguration getRepositoryConfiguration() {
		return _overridenRepositoryDefiner.getRepositoryConfiguration();
	}

	@Override
	public String getRepositoryTypeLabel(Locale locale) {
		return _overridenRepositoryDefiner.getRepositoryTypeLabel(locale);
	}

	@Override
	public boolean isExternalRepository() {
		return _overridenRepositoryDefiner.isExternalRepository();
	}

	@Override
	public void registerCapabilities(
		CapabilityRegistry<DocumentRepository> capabilityRegistry) {

		_overridenRepositoryDefiner.registerCapabilities(capabilityRegistry);

		capabilityRegistry.addSupportedCapability(
			AdaptiveMediaCapabiliy.class, new AdaptiveMediaCapabiliy());
	}

	@Override
	public void registerRepositoryEventListeners(
		RepositoryEventRegistry repositoryEventRegistry) {

		_overridenRepositoryDefiner.registerRepositoryEventListeners(
			repositoryEventRegistry);
	}

	@Override
	public void registerRepositoryFactory(
		RepositoryFactoryRegistry repositoryFactoryRegistry) {

		_overridenRepositoryDefiner.registerRepositoryFactory(
			repositoryFactoryRegistry);
	}

	@Reference(unbind = "-")
	public void setAMAsyncProcessorLocator(
		AMAsyncProcessorLocator amAsyncProcessorLocator) {

		_amAsyncProcessorLocator = amAsyncProcessorLocator;
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	public void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	protected void initializeOverridenRepositoryDefiner(
		RepositoryDefiner repositoryDefiner) {

		_overridenRepositoryDefiner = repositoryDefiner;
	}

	protected void restoreOverridenRepositoryDefiner(String className) {
		Map<String, RepositoryClassDefinition> repositoryClassDefinitions =
			_getFieldValue("_repositoryClassDefinitions");

		RepositoryClassDefinition repositoryClassDefinition =
			RepositoryClassDefinition.fromRepositoryDefiner(
				_overridenRepositoryDefiner);

		repositoryClassDefinitions.put(className, repositoryClassDefinition);
	}

	@Reference
	protected InputStreamSanitizer inputStreamSanitizer;

	private void _deleteAdaptiveMedia(FileEntry fileEntry) {
		if (ExportImportThreadLocal.isImportInProcess()) {
			return;
		}

		try {
			AMAsyncProcessor<FileVersion, ?> amAsyncProcessor =
				_amAsyncProcessorLocator.locateForClass(FileVersion.class);

			List<FileVersion> fileVersions = fileEntry.getFileVersions(
				WorkflowConstants.STATUS_ANY);

			for (FileVersion fileVersion : fileVersions) {
				amAsyncProcessor.triggerCleanUp(
					fileVersion,
					String.valueOf(fileVersion.getFileVersionId()));
			}
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

	private <T> T _getFieldValue(String fieldName) {
		try {
			RepositoryClassDefinitionCatalog repositoryClassDefinitionCatalog =
				RepositoryClassDefinitionCatalogUtil.
					getRepositoryClassDefinitionCatalog();

			Class<? extends RepositoryClassDefinitionCatalog> clazz =
				repositoryClassDefinitionCatalog.getClass();

			Field field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			return (T)field.get(repositoryClassDefinitionCatalog);
		}
		catch (IllegalAccessException | NoSuchFieldException | SecurityException
					e) {

			throw new RepositoryException(e);
		}
	}

	private void _updateAdaptiveMedia(FileEntry fileEntry) {
		if (ExportImportThreadLocal.isImportInProcess()) {
			return;
		}

		try {
			AMAsyncProcessor<FileVersion, ?> amAsyncProcessor =
				_amAsyncProcessorLocator.locateForClass(FileVersion.class);

			FileVersion latestFileVersion = fileEntry.getLatestFileVersion(
				true);

			amAsyncProcessor.triggerProcess(
				_wrap(latestFileVersion),
				String.valueOf(latestFileVersion.getFileVersionId()));
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

	private FileVersion _wrap(FileVersion fileVersion) {
		if (fileVersion == null) {
			return null;
		}

		return new SafeFileVersion(fileVersion);
	}

	private AMAsyncProcessorLocator _amAsyncProcessorLocator;
	private RepositoryDefiner _overridenRepositoryDefiner;

	private class AdaptiveMediaCapabiliy
		implements Capability, RepositoryEventAware {

		public void registerRepositoryEventListeners(
			RepositoryEventRegistry repositoryEventRegistry) {

			repositoryEventRegistry.registerRepositoryEventListener(
				RepositoryEventType.Add.class, FileEntry.class,
				BaseOverridingRepositoryDefiner.this::_updateAdaptiveMedia);
			repositoryEventRegistry.registerRepositoryEventListener(
				RepositoryEventType.Delete.class, FileEntry.class,
				BaseOverridingRepositoryDefiner.this::_deleteAdaptiveMedia);
			repositoryEventRegistry.registerRepositoryEventListener(
				RepositoryEventType.Update.class, FileEntry.class,
				BaseOverridingRepositoryDefiner.this::_updateAdaptiveMedia);
		}

	}

	private class SafeFileEntry extends FileEntryWrapper {

		public SafeFileEntry(FileEntry fileEntry) {
			super(fileEntry);
		}

		@Override
		public InputStream getContentStream() throws PortalException {
			return inputStreamSanitizer.sanitize(super.getContentStream());
		}

		@Override
		public InputStream getContentStream(String version)
			throws PortalException {

			return inputStreamSanitizer.sanitize(
				super.getContentStream(version));
		}

		@Override
		public FileVersion getFileVersion() throws PortalException {
			return new SafeFileVersion(super.getFileVersion());
		}

		@Override
		public FileVersion getFileVersion(String version)
			throws PortalException {

			return new SafeFileVersion(super.getFileVersion(version));
		}

		@Override
		public FileVersion getLatestFileVersion() throws PortalException {
			return new SafeFileVersion(super.getLatestFileVersion());
		}

		@Override
		public FileVersion getLatestFileVersion(boolean trusted)
			throws PortalException {

			return new SafeFileVersion(super.getLatestFileVersion(trusted));
		}

	}

	private class SafeFileVersion extends FileVersionWrapper {

		public SafeFileVersion(FileVersion fileVersion) {
			super(fileVersion);
		}

		@Override
		public InputStream getContentStream(boolean incrementCounter)
			throws PortalException {

			return inputStreamSanitizer.sanitize(
				super.getContentStream(incrementCounter));
		}

		@Override
		public FileEntry getFileEntry() throws PortalException {
			return new SafeFileEntry(super.getFileEntry());
		}

	}

}