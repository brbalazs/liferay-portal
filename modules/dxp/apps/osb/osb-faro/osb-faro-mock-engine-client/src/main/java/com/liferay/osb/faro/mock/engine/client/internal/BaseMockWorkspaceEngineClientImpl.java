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
import com.liferay.osb.faro.engine.client.model.LCPBuildService;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.LCPService;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.model.FaroProject;

import java.util.List;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
public abstract class BaseMockWorkspaceEngineClientImpl
	implements WorkspaceEngineClient {

	@Override
	public Workspace createWorkspace(String region, boolean trial)
		throws Exception {

		return workspaceEngineClient.createWorkspace(region, trial);
	}

	@Override
	public void deleteWorkspaceService(String weDeployKey, String serviceId) {
		workspaceEngineClient.deleteWorkspaceService(weDeployKey, serviceId);
	}

	@Override
	public String getBranch(String weDeployKey) {
		return workspaceEngineClient.getBranch(weDeployKey);
	}

	@Override
	public LCPProject getLCPProject(String weDeployKey) {
		return workspaceEngineClient.getLCPProject(weDeployKey);
	}

	@Override
	public List<LCPService> getLCPServices(String weDeployKey) {
		return workspaceEngineClient.getLCPServices(weDeployKey);
	}

	@Override
	public List<String> getLoadBalancerIPs(String weDeployKey) {
		return workspaceEngineClient.getLoadBalancerIPs(weDeployKey);
	}

	@Override
	public Workspace getWorkspace(String weDeployKey) throws Exception {
		return workspaceEngineClient.getWorkspace(weDeployKey);
	}

	@Override
	public void updateSecrets(FaroProject faroProject) {
		workspaceEngineClient.updateSecrets(faroProject);
	}

	@Override
	public void updateServices(String weDeployKey, String operation)
		throws Exception {

		workspaceEngineClient.updateServices(weDeployKey, operation);
	}

	@Override
	public void updateServices(
			String weDeployKey, String operation, List<String> serviceIds)
		throws Exception {

		workspaceEngineClient.updateServices(
			weDeployKey, operation, serviceIds);
	}

	@Override
	public List<LCPBuildService> updateWorkspace(
		String weDeployKey, String sha, boolean trial) {

		return workspaceEngineClient.updateWorkspace(weDeployKey, sha, trial);
	}

	@Reference(
		target = "(component.name=com.liferay.osb.faro.engine.client.internal.WorkspaceEngineClientImpl)"
	)
	protected WorkspaceEngineClient workspaceEngineClient;

}