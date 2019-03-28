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

package com.liferay.commerce.cloud.client.internal.configuration.category;

import com.liferay.configuration.admin.category.ConfigurationCategory;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(service = ConfigurationCategory.class)
public class CommerceInsightsConfigurationCategory
	implements ConfigurationCategory {

	@Override
	public String getBundleSymbolicName() {
		return "com.liferay.commerce.cloud.client.service";
	}

	@Override
	public String getCategoryIcon() {
		return "api-web";
	}

	@Override
	public String getCategoryKey() {
		return _CATEGORY_KEY;
	}

	@Override
	public String getCategorySection() {
		return _CATEGORY_SECTION;
	}

	private static final String _CATEGORY_KEY = "commerce-insights";

	private static final String _CATEGORY_SECTION = "commerce";

}