/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.theme.minium.site.initializer.internal;

import com.liferay.commerce.product.importer.CPFileImporter;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(service = MiniumLayoutsInitializer.class)
public class MiniumLayoutsInitializer {

	public void initialize(ServiceContext serviceContext) throws Exception {
		_cpFileImporter.cleanLayouts(serviceContext);

		_createLayouts(serviceContext);
	}

	private void _createLayouts(ServiceContext serviceContext)
		throws Exception {

		ClassLoader classLoader =
			MiniumLayoutsInitializer.class.getClassLoader();

		String json = StringUtil.read(
			classLoader,
			MiniumSiteInitializer.DEPENDENCIES_PATH + "layouts.json", false);

		JSONArray jsonArray = _jsonFactory.createJSONArray(json);

		_cpFileImporter.createLayouts(
			jsonArray, classLoader, MiniumSiteInitializer.DEPENDENCIES_PATH,
			serviceContext);
	}

	@Reference
	private CPFileImporter _cpFileImporter;

	@Reference
	private JSONFactory _jsonFactory;

}