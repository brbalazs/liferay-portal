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
	scope = ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE
)
@Meta.OCD(
	id = "com.liferay.commerce.dashboard.web.internal.configuration.CommerceDashboardForecastsChartPortletInstanceConfiguration",
	localization = "content/Language",
	name = "commerce-dashboard-forecasts-chart-portlet-instance-configuration-name"
)
public interface CommerceDashboardForecastsChartPortletInstanceConfiguration {

	@Meta.AD(name = "filter-by-sku", required = false)
	public boolean filterBySKU();

	@Meta.AD(name = "period", required = false)
	public int period();

	@Meta.AD(deflt = "1", name = "target", required = false)
	public int target();

}