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

import com.liferay.osb.faro.engine.client.model.LCPBuildService;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.LCPService;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.model.FaroProject;

import java.util.List;

/**
 * @author Matthew Kong
 */
public interface WorkspaceEngineClient {

	public Workspace createWorkspace(String region, boolean trial);

	public void deleteWorkspaceService(String weDeployKey, String serviceId);

	public String getBranch(String weDeployKey);

	public LCPProject getLCPProject(String weDeployKey);

	public List<LCPService> getLCPServices(String weDeployKey);

	public List<String> getLoadBalancerIPs(String weDeployKey);

	public Workspace getWorkspace(String weDeployKey) throws Exception;

	public void updateSecrets(FaroProject faroProject);

	public void updateServices(String weDeployKey, String operation)
		throws Exception;

	public void updateServices(
			String weDeployKey, String operation, List<String> serviceIds)
		throws Exception;

	public List<LCPBuildService> updateWorkspace(
		String weDeployKey, String sha, boolean trial);

}