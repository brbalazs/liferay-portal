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

package com.liferay.osb.faro.engine.client;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.FaroUser;

/**
 * @author Matthew Kong
 */
public interface HubSpotEngineClient {

	public void submitUsageForm(
		FaroProject faroProject, FaroUser faroUser, double usage);

	public void submitWorkspaceExpirationForm(
		FaroProject faroProject, FaroUser faroUser);

	public void submitWorkspaceUserForm(
		FaroProject faroProject, FaroUser faroUser, boolean primaryOwner);

}