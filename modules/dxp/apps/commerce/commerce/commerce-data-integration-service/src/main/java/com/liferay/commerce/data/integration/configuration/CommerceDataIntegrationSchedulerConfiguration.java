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

package com.liferay.commerce.data.integration.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author guywandji
 * @author Alessio Antonio Rendina
 */
@ExtendedObjectClassDefinition(
	category = "data-integration",
	scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.liferay.commerce.data.integration.configuration.CommerceDataIntegrationSchedulerConfiguration",
	localization = "content/Language",
	name = "commerce-data-integration-scheduler-configuration-name"
)
public interface CommerceDataIntegrationSchedulerConfiguration {

	@Meta.AD(deflt = "10", name = "check-interval", required = false)
	public int checkInterval();

	@Meta.AD(deflt = "20139", name = "default-user-id", required = false)
	public int userId();

}