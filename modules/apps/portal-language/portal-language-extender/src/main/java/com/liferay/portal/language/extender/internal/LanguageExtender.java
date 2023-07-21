/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.language.extender.internal;

import com.liferay.osgi.felix.util.AbstractExtender;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.language.LanguageResources;

import java.util.List;

import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = {})
public class LanguageExtender extends AbstractExtender {

	@Activate
	protected void activate(BundleContext bundleContext) throws Exception {
		_logger = new Logger(bundleContext);

		start(bundleContext);
	}

	@Deactivate
	protected void deactivate(BundleContext bundleContext) throws Exception {
		stop(bundleContext);
	}

	@Override
	protected void debug(Bundle bundle, String s) {
		_logger.log(
			Logger.LOG_DEBUG, StringBundler.concat("[", bundle, "] ", s));
	}

	@Override
	protected Extension doCreateExtension(Bundle bundle) throws Exception {
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		List<BundleCapability> bundleCapabilities =
			bundleWiring.getCapabilities("liferay.resource.bundle");

		if (bundleCapabilities == null) {
			return null;
		}

		return new LanguageExtension(
			getBundleContext(), bundle, bundleCapabilities, _logger);
	}

	@Override
	protected void error(String s, Throwable throwable) {
		_logger.log(Logger.LOG_ERROR, s, throwable);
	}

	@Override
	protected void warn(Bundle bundle, String s, Throwable throwable) {
		_logger.log(
			Logger.LOG_WARNING, StringBundler.concat("[", bundle, "] ", s));
	}

	@Reference
	private LanguageResources _languageResources;

	private Logger _logger;

}