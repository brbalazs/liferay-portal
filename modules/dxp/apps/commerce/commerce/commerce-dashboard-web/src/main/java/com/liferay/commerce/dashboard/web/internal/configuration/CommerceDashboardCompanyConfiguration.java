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

package com.liferay.commerce.dashboard.web.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.commerce.dashboard.web.internal.configuration.category.DashboardConfigurationCategory;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Andrea Di Giorgi
 */
@ExtendedObjectClassDefinition(
	category = DashboardConfigurationCategory.CATEGORY_KEY,
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.commerce.dashboard.web.internal.configuration.CommerceDashboardCompanyConfiguration",
	localization = "content/Language",
	name = "commerce-dashboard-company-configuration-name"
)
public interface CommerceDashboardCompanyConfiguration {

	@Meta.AD(
		deflt = "#4B9BFF,#FFB46E,#FF5F5F,#50D2A0,#FF73C3,#9CE269,#AF78FF,#FFD76E,#5FC8FF",
		name = "chart-colors", required = false
	)
	public String[] chartColors();

}