/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.shrinkwrap.osgi.api;

import java.io.File;

import org.jboss.shrinkwrap.api.Assignable;

/**
 * @author Carlos Sierra Andrés
 */
public interface BndProjectBuilder extends Assignable {

	public BndProjectBuilder addClassPath(File classPathFile);

	public BndProjectBuilder addProjectPropertiesFile(
		File projectPropertiesFile);

	public BndProjectBuilder addWorkspacePropertiesFile(
		File workspacePropertiesFile);

	public BndArchive asBndJar();

	public BndProjectBuilder generateManifest(boolean generateManifest);

	public BndProjectBuilder setBaseDir(File baseDir);

	public BndProjectBuilder setBndFile(File bndFile);

	public BndProjectBuilder setProjectDir(File projectDir);

	public BndProjectBuilder setWorkspaceDir(File workspaceDir);

}