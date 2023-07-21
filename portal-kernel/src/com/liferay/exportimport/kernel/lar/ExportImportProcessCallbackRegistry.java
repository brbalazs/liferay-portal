/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.kernel.lar;

import aQute.bnd.annotation.ProviderType;

import java.util.concurrent.Callable;

/**
 * @author Daniel Kocsis
 */
@ProviderType
public interface ExportImportProcessCallbackRegistry {

	/**
	 * @deprecated As of Judson (7.1.x)
	 */
	@Deprecated
	public void registerCallback(Callable<?> callable);

	public void registerCallback(String processId, Callable<?> callable);

}