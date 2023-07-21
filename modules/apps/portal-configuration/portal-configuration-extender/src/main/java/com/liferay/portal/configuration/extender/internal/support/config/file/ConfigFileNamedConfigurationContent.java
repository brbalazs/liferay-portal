/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.extender.internal.support.config.file;

import com.liferay.portal.configuration.extender.internal.NamedConfigurationContent;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

/**
 * @author Carlos Sierra Andrés
 */
public final class ConfigFileNamedConfigurationContent
	implements NamedConfigurationContent {

	public ConfigFileNamedConfigurationContent(
		String name, InputStream inputStream) {

		_name = name;
		_inputStream = inputStream;
	}

	public ConfigFileNamedConfigurationContent(URL url) {
		String name = url.getFile();

		if (name.startsWith("/")) {
			name = name.substring(1);
		}

		int lastIndexOfSlash = name.lastIndexOf('/');

		if (lastIndexOfSlash > 0) {
			name = name.substring(lastIndexOfSlash + 1);
		}

		if (!name.endsWith(".config")) {
			throw new IllegalArgumentException(
				"File name does not end with .config");
		}

		_name = name.substring(0, name.length() - 7);

		try {
			_inputStream = url.openStream();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public InputStream getInputStream() {
		return _inputStream;
	}

	@Override
	public String getName() {
		return _name;
	}

	private final InputStream _inputStream;
	private final String _name;

}