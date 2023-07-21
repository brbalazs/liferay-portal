/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.image.internal.mime.type;

import com.liferay.adaptive.media.image.internal.configuration.AMImageConfiguration;
import com.liferay.adaptive.media.image.mime.type.AMImageMimeTypeProvider;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Sergio González
 */
@Component(
	configurationPid = "com.liferay.adaptive.media.image.internal.configuration.AMImageConfiguration",
	service = AMImageMimeTypeProvider.class
)
public class AMImageMimeTypeProviderImpl implements AMImageMimeTypeProvider {

	@Override
	public String[] getSupportedMimeTypes() {
		return _amImageConfiguration.supportedMimeTypes();
	}

	@Override
	public boolean isMimeTypeSupported(String mimeType) {
		return ArrayUtil.contains(getSupportedMimeTypes(), mimeType);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_amImageConfiguration = ConfigurableUtil.createConfigurable(
			AMImageConfiguration.class, properties);
	}

	private volatile AMImageConfiguration _amImageConfiguration;

}