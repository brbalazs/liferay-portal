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

package com.liferay.headless.commerce.punchout.internal.resource.v1_0;

import com.liferay.headless.commerce.punchout.resource.v1_0.PunchOutSessionResource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Jaclyn Ong
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/punch-out-session.properties",
	scope = ServiceScope.PROTOTYPE, service = PunchOutSessionResource.class
)
public class PunchOutSessionResourceImpl
	extends BasePunchOutSessionResourceImpl {
}