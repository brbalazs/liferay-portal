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

package com.liferay.osb.faro.mock.engine.client.internal;

import com.liferay.osb.faro.engine.client.WorkspaceEngineClient;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true, property = "service.ranking:Integer=100",
	service = WorkspaceEngineClient.class
)
public class MockWorkspaceEngineClientImpl
	extends BaseMockWorkspaceEngineClientImpl {

	@Override
	public Workspace createWorkspace(String region, boolean trial)
		throws Exception {

		if (Validator.isNotNull(_WEDEPLOY_EMAIL_ADDRESS)) {
			return workspaceEngineClient.createWorkspace(region, trial);
		}

		return new Workspace();
	}

	private static final String _WEDEPLOY_EMAIL_ADDRESS = System.getenv(
		"FARO_WEDEPLOY_EMAIL_ADDRESS");

}