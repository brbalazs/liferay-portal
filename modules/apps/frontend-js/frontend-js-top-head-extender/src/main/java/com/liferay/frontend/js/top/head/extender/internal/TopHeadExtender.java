/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.top.head.extender.internal;

import com.liferay.osgi.felix.util.AbstractExtender;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Iván Zaera Avellón
 */
@Component(immediate = true, service = {})
public class TopHeadExtender extends AbstractExtender {

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
		Dictionary<String, String> headers = bundle.getHeaders(
			StringPool.BLANK);

		String liferayJsResourcesTopHead = headers.get(
			"Liferay-JS-Resources-Top-Head");

		String liferayJsResourcesTopHeadAuthenticated = headers.get(
			"Liferay-JS-Resources-Top-Head-Authenticated");

		if (Validator.isBlank(liferayJsResourcesTopHead) &&
			Validator.isBlank(liferayJsResourcesTopHeadAuthenticated)) {

			return null;
		}

		List<String> jsResourcePaths = null;

		if (Validator.isNull(liferayJsResourcesTopHead)) {
			jsResourcePaths = Collections.emptyList();
		}
		else {
			jsResourcePaths = Arrays.asList(
				liferayJsResourcesTopHead.split(StringPool.COMMA));
		}

		List<String> authenticatedJsResourcePaths = null;

		if (Validator.isNull(liferayJsResourcesTopHeadAuthenticated)) {
			authenticatedJsResourcePaths = Collections.emptyList();
		}
		else {
			authenticatedJsResourcePaths = Arrays.asList(
				liferayJsResourcesTopHeadAuthenticated.split(StringPool.COMMA));
		}

		TopHeadResourcesImpl topHeadResourcesImpl = new TopHeadResourcesImpl(
			jsResourcePaths, authenticatedJsResourcePaths);

		int liferayTopHeadWeight = GetterUtil.getInteger(
			headers.get("Liferay-Top-Head-Weight"));

		return new TopHeadExtension(
			bundle, topHeadResourcesImpl, liferayTopHeadWeight);
	}

	@Override
	protected void error(String s, Throwable t) {
		_logger.log(Logger.LOG_ERROR, s, t);
	}

	@Override
	protected void warn(Bundle bundle, String s, Throwable t) {
		_logger.log(
			Logger.LOG_WARNING, StringBundler.concat("[", bundle, "] ", s), t);
	}

	private Logger _logger;

}