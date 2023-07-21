/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal;

import com.liferay.portal.kernel.util.Supplier;

import java.util.Dictionary;

/**
 * @author Carlos Sierra Andrés
 */
public interface ConfigurationDescription {

	public String getFactoryPid();

	public String getPid();

	public Supplier<Dictionary<String, Object>> getPropertiesSupplier();

}