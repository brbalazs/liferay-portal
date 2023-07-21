/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.Supplier;

import java.util.Dictionary;

/**
 * @author Carlos Sierra Andrés
 */
public final class FactoryConfigurationDescription
	implements ConfigurationDescription {

	public FactoryConfigurationDescription(
		String factoryPid, String pid,
		Supplier<Dictionary<String, Object>> propertiesSupplier) {

		_factoryPid = factoryPid;
		_pid = pid;
		_propertiesSupplier = propertiesSupplier;
	}

	@Override
	public String getFactoryPid() {
		return _factoryPid;
	}

	@Override
	public String getPid() {
		return _pid;
	}

	@Override
	public Supplier<Dictionary<String, Object>> getPropertiesSupplier() {
		return _propertiesSupplier;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(7);

		sb.append("{factoryPid=");
		sb.append(_factoryPid);
		sb.append(", pid=");
		sb.append(_pid);
		sb.append(", propertiesSupplier=");
		sb.append(_propertiesSupplier);
		sb.append("}");

		return sb.toString();
	}

	private final String _factoryPid;
	private final String _pid;
	private final Supplier<Dictionary<String, Object>> _propertiesSupplier;

}