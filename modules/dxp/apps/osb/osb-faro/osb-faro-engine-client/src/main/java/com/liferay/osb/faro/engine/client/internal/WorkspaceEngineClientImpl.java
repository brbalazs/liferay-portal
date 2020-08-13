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

package com.liferay.osb.faro.engine.client.internal;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.WorkspaceEngineClient;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.engine.client.model.WorkspaceService;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * @author Shinn Lok
 */
@Component(immediate = true, service = WorkspaceEngineClient.class)
public class WorkspaceEngineClientImpl implements WorkspaceEngineClient {

	@Override
	public Workspace createWorkspace(String region, boolean trial) {
		String uuid = String.valueOf(UUID.randomUUID());

		final String projectId =
			"asah" + StringUtil.replace(uuid, CharPool.DASH, StringPool.BLANK);

		ResponseEntity<LCPProject> responseEntity = getRestTemplate().exchange(
			_PROJECT_API_URL, HttpMethod.POST,
			new HttpEntity<Object>(
				new HashMap<String, String>() {
					{
						put("cluster", region);
						put("projectId", projectId);
					}
				}),
			LCPProject.class);

		LCPProject lcpProject = responseEntity.getBody();

		Workspace workspace = createWorkspace(lcpProject, trial);

		_threadPoolTaskExecutor.execute(
			new CheckWorkspaceRunnable(workspace, null, trial));

		return workspace;
	}

	@Override
	public void deleteWorkspaceService(String weDeployKey, String serviceId) {
		getRestTemplate().exchange(
			StringBundler.concat(
				_PROJECT_API_URL, getProjectId(weDeployKey), "/services/",
				serviceId),
			HttpMethod.DELETE, null, Void.class);
	}

	@Override
	public String getBranch(String weDeployKey) {
		try {
			ResponseEntity<String> responseEntity = getRestTemplate().exchange(
				StringBundler.concat(
					_PROJECT_API_URL, getProjectId(weDeployKey),
					"/activities/builds-deployments?limit=1&shouldGroup=true"),
				HttpMethod.GET, null, String.class);

			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
				responseEntity.getBody());

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray innerJSONArray = jsonArray.getJSONArray(i);

				for (int j = 0; j < innerJSONArray.length(); j++) {
					JSONObject activityJSONObject =
						innerJSONArray.getJSONObject(j);

					JSONObject metadataJSONObject =
						activityJSONObject.getJSONObject("metadata");

					String branch = metadataJSONObject.getString(
						"branch", null);

					if (Validator.isNotNull(branch)) {
						return branch;
					}
				}
			}
		}
		catch (Exception e) {
		}

		return null;
	}

	@Override
	public LCPProject getLCPProject(String weDeployKey) {
		ResponseEntity<LCPProject> responseEntity = getRestTemplate().exchange(
			_PROJECT_API_URL + getProjectId(weDeployKey), HttpMethod.GET, null,
			LCPProject.class);

		return responseEntity.getBody();
	}

	public List<String> getLoadBalancerIPs(String weDeployKey) {
		if (Validator.isNull(weDeployKey)) {
			return Collections.emptyList();
		}

		List<String> loadBalancerIPs = new ArrayList<>();

		LCPProject lcpProject = getLCPProject(weDeployKey);

		loadBalancerIPs.add(lcpProject.getLoadBalancerIp());

		if (Validator.isNull(_faroLoadBalancerIP)) {
			LCPProject faroLCPProject = getLCPProject("ac-prd");

			_faroLoadBalancerIP = faroLCPProject.getLoadBalancerIp();
		}

		loadBalancerIPs.add(_faroLoadBalancerIP);

		return loadBalancerIPs;
	}

	@Override
	public Workspace getWorkspace(String weDeployKey) throws Exception {
		Workspace workspace = new Workspace();

		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByWeDeployKey(weDeployKey);

		if (faroProject == null) {
			throw new Exception("Could not find project " + weDeployKey);
		}

		workspace.setReady(isReady(faroProject));
		workspace.setWeDeployKey(weDeployKey);

		return workspace;
	}

	@Override
	public List<WorkspaceService> getWorkspaceServices(String weDeployKey) {
		ResponseEntity<List<WorkspaceService>> response =
			getRestTemplate().exchange(
				StringBundler.concat(
					_PROJECT_API_URL, getProjectId(weDeployKey), "/services"),
				HttpMethod.GET, null,
				new ParameterizedTypeReference<List<WorkspaceService>>() {
				});

		return response.getBody();
	}

	@Override
	public void updateServices(String weDeployKey, String operation)
		throws Exception {

		List<String> serviceIds = new ArrayList<>();

		for (WorkspaceService workspaceService :
				getWorkspaceServices(weDeployKey)) {

			serviceIds.add(workspaceService.getServiceId());
		}

		updateServices(weDeployKey, operation, serviceIds);
	}

	@Override
	public void updateServices(
			String weDeployKey, String operation, List<String> serviceIds)
		throws Exception {

		HttpMethod httpMethod = null;

		if (Objects.equals(operation, "restart")) {
			httpMethod = HttpMethod.POST;
		}
		else if (Objects.equals(operation, "stop")) {
			httpMethod = HttpMethod.PATCH;
		}
		else {
			throw new Exception("Invalid operation: " + operation);
		}

		for (String serviceId : serviceIds) {
			getRestTemplate().exchange(
				StringBundler.concat(
					_PROJECT_API_URL, getProjectId(weDeployKey), "/services/",
					serviceId, StringPool.SLASH, operation),
				httpMethod, null, Void.class);
		}
	}

	@Override
	public Workspace updateWorkspace(
		String weDeployKey, String sha, boolean trial) {

		Workspace workspace = new Workspace();

		buildWorkspace(getProjectId(weDeployKey), sha, trial);

		workspace.setWeDeployKey(weDeployKey);

		return workspace;
	}

	protected void buildWorkspace(String projectId, String sha, boolean trial) {
		getRestTemplate().exchange(
			StringBundler.concat(_PROJECT_API_URL, projectId, "/build"),
			HttpMethod.POST,
			new HttpEntity<Object>(
				new HashMap<String, String>() {
					{
						put("provider", "github");
						StringBundler sb = new StringBundler(10);

						sb.append("https://");
						sb.append(_REPOSITORY_TOKEN);
						sb.append(StringPool.AT);
						sb.append("github.com/liferay");
						sb.append("/com-liferay-osb-asah-private/tree");
						sb.append(StringPool.SLASH);

						if (Validator.isNull(sha)) {
							sb.append(_REPOSITORY_SHA);
						}
						else {
							sb.append(sha);
						}

						sb.append(StringPool.SLASH);
						sb.append(".wedeploy_profiles/customer");

						if (trial) {
							sb.append("-trial");
						}

						put("repository", sb.toString());
					}
				}),
			Void.class);
	}

	protected void createElasticSearchLink(LCPProject lcpProject) {
		getRestTemplate().exchange(
			_PROJECT_API_URL + lcpProject.getESProjectId() + "/link",
			HttpMethod.POST,
			new HttpEntity<Object>(
				new HashMap<String, String>() {
					{
						put("allowedProjectUid", lcpProject.getId());
					}
				}),
			Void.class);
	}

	protected Workspace createWorkspace(LCPProject lcpProject, boolean trial) {
		createElasticSearchLink(lcpProject);

		Workspace workspace = new Workspace();

		buildWorkspace(lcpProject.getProjectId(), null, trial);

		workspace.setWeDeployKey(lcpProject.getProjectId() + ".lfr.cloud");

		return workspace;
	}

	protected String getProjectId(String weDeployKey) {
		return StringUtil.removeSubstring(weDeployKey, ".lfr.cloud");
	}

	protected RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors =
			restTemplate.getInterceptors();

		interceptors.add(
			new BasicAuthorizationInterceptor(
				_WEDEPLOY_EMAIL_ADDRESS, _WEDEPLOY_PASSWORD));

		restTemplate.setRequestFactory(
			new HttpComponentsClientHttpRequestFactory());

		return restTemplate;
	}

	protected boolean isReady(FaroProject faroProject) {
		try {
			_contactsEngineClient.getIndividuals(
				faroProject, (String)null, false, 1, 0, null);

			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	private static final String _PROJECT_API_URL = GetterUtil.getString(
		System.getenv("FARO_DXP_CLOUD_API_URL"),
		"https://api.liferay.cloud/projects/");

	private static final String _REPOSITORY_SHA = System.getenv(
		"FARO_REPOSITORY_SHA");

	private static final String _REPOSITORY_TOKEN = System.getenv(
		"FARO_REPOSITORY_TOKEN");

	private static final String _WEDEPLOY_EMAIL_ADDRESS = System.getenv(
		"FARO_WEDEPLOY_EMAIL_ADDRESS");

	private static final String _WEDEPLOY_PASSWORD = System.getenv(
		"FARO_WEDEPLOY_PASSWORD");

	private static final Log _log = LogFactoryUtil.getLog(
		WorkspaceEngineClientImpl.class);

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ContactsEngineClient _contactsEngineClient;

	private String _faroLoadBalancerIP;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	private final ExecutorService _threadPoolTaskExecutor =
		Executors.newFixedThreadPool(10);

	private class CheckWorkspaceRunnable implements Runnable {

		public CheckWorkspaceRunnable(
			Workspace workspace, String sha, boolean trial) {

			_workspace = workspace;
			_sha = sha;
			_trial = trial;
		}

		@Override
		public void run() {
			try {
				doRun();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		protected void doRun() throws Exception {
			for (int i = 0; i < 3; i++) {
				Thread.sleep(Time.HOUR);

				Workspace workspace = getWorkspace(_workspace.getWeDeployKey());

				if (workspace.isReady()) {
					_faroProjectLocalService.sendCreatedWorkspaceEmail(
						workspace.getWeDeployKey());

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Successfully deployed to " +
								workspace.getWeDeployKey());
					}

					return;
				}

				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"Unable to deploy to ", workspace.getWeDeployKey(),
							". Retry ", i + 1, "."));
				}

				updateWorkspace(workspace.getWeDeployKey(), _sha, _trial);
			}

			FaroProject faroProject =
				_faroProjectLocalService.fetchFaroProjectByWeDeployKey(
					_workspace.getWeDeployKey());

			if (faroProject != null) {
				faroProject.setState(Workspace.AUTO_REDEPLOY_FAILED);

				_faroProjectLocalService.updateFaroProject(faroProject);
			}

			_log.error("Unable to deploy to " + _workspace.getWeDeployKey());
		}

		private final String _sha;
		private final boolean _trial;
		private final Workspace _workspace;

	}

}