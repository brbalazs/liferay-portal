/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.kernel.controller;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;

import java.io.File;

/**
 * @author Daniel Kocsis
 */
@ProviderType
public interface ExportController extends ExportImportController {

	public File export(ExportImportConfiguration exportImportConfiguration)
		throws Exception;

}