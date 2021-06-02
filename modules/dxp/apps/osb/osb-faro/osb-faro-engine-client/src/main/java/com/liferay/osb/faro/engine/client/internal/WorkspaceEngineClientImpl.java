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

import com.liferay.osb.faro.constants.FaroProjectConstants;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.WorkspaceEngineClient;
import com.liferay.osb.faro.engine.client.model.LCPBuildService;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.LCPService;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.util.UpgradeUtil;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
					"/activities/builds-deployments?limit=1&shouldGroup=true",
					"&type=BUILD_SUCCEEDED"),
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
		catch (Exception exception) {
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

	@Override
	public List<LCPService> getLCPServices(String weDeployKey) {
		ResponseEntity<List<LCPService>> response = getRestTemplate().exchange(
			StringBundler.concat(
				_PROJECT_API_URL, getProjectId(weDeployKey), "/services"),
			HttpMethod.GET, null,
			new ParameterizedTypeReference<List<LCPService>>() {
			});

		return response.getBody();
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
	public void updateSecrets(FaroProject faroProject) {
		List<String> envVarSecretNames = new ArrayList<>();

		try {
			for (String secretKey : _secretKeys) {
				String secretValue = System.getenv(secretKey);

				if (Validator.isNull(secretValue)) {
					continue;
				}

				String secretName = getSecretName(secretKey);

				if (hasSecret(faroProject.getWeDeployKey(), secretName)) {
					updateSecret(
						faroProject.getWeDeployKey(), secretName, secretValue);
				}
				else {
					createSecret(
						faroProject.getWeDeployKey(), secretName, secretValue);
				}

				envVarSecretNames.add(secretKey);
				envVarSecretNames.add(secretName);
			}

			for (LCPService lcpService :
					getLCPServices(faroProject.getWeDeployKey())) {

				attachSecrets(
					faroProject.getWeDeployKey(), lcpService.getServiceId(),
					envVarSecretNames.toArray(new String[0]));
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Successfully updated secrets to " +
						faroProject.getWeDeployKey());
			}
		}
		catch (Exception exception) {
			_log.error(
				"Unable to update secrets to " + faroProject.getWeDeployKey(),
				exception);
		}
	}

	@Override
	public void updateServices(String weDeployKey, String operation)
		throws Exception {

		List<String> serviceIds = new ArrayList<>();

		for (LCPService lcpService : getLCPServices(weDeployKey)) {
			serviceIds.add(lcpService.getServiceId());
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
	public List<LCPBuildService> updateWorkspace(
		String weDeployKey, String sha, boolean trial) {

		return buildWorkspace(getProjectId(weDeployKey), sha, trial, true);
	}

	protected void attachSecrets(
		String weDeployKey, String serviceId, String... envVarSecretNames) {

		if ((envVarSecretNames.length % 2) != 0) {
			return;
		}

		getRestTemplate().exchange(
			StringBundler.concat(
				_PROJECT_API_URL, getProjectId(weDeployKey), "/services/",
				serviceId, "/secrets"),
			HttpMethod.POST,
			new HttpEntity<Object>(
				new HashMap<String, List<Map<String, String>>>() {
					{
						put(
							"attachments",
							new ArrayList<Map<String, String>>() {
								{
									for (int i = 0;
										 i < envVarSecretNames.length; i += 2) {

										String envVarName =
											envVarSecretNames[i];
										String secretName =
											envVarSecretNames[i + 1];

										add(
											new HashMap<String, String>() {
												{
													put(
														"envVarName",
														envVarName);
													put(
														"secretName",
														secretName);
												}
											});
									}
								}
							});
					}
				}),
			Void.class);
	}

	protected List<LCPBuildService> buildWorkspace(
		String projectId, String sha, boolean trial, boolean upgrade) {

		ResponseEntity<List<LCPBuildService>> responseEntity =
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
								sb.append(UpgradeUtil.getLatestVersion());
							}
							else {
								sb.append(sha);
							}

							sb.append(StringPool.SLASH);
							sb.append(".wedeploy_profiles/customer");

							if (trial) {
								sb.append("-trial");
							}
							else if (upgrade) {
								sb.append("-upgrade");
							}

							put("repository", sb.toString());
						}
					}),
				new ParameterizedTypeReference<List<LCPBuildService>>() {
				});

		return responseEntity.getBody();
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

	protected void createSecret(String weDeployKey, String name, String value) {
		getRestTemplate().exchange(
			StringBundler.concat(
				_PROJECT_API_URL, getProjectId(weDeployKey), "/secrets"),
			HttpMethod.POST,
			new HttpEntity<Object>(
				new HashMap<String, String>() {
					{
						put("name", name);
						put("value", value);
					}
				}),
			Void.class);
	}

	protected Workspace createWorkspace(LCPProject lcpProject, boolean trial) {
		createElasticSearchLink(lcpProject);

		for (String secretKey : _secretKeys) {
			String secretValue = System.getenv(secretKey);

			if (Validator.isNull(secretValue)) {
				continue;
			}

			createSecret(
				lcpProject.getProjectId(), getSecretName(secretKey),
				secretValue);
		}

		Workspace workspace = new Workspace();

		buildWorkspace(lcpProject.getProjectId(), null, trial, false);

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

	protected String getSecretName(String secretKey) {
		secretKey = StringUtil.toLowerCase(secretKey);

		return StringUtil.removeSubstring(secretKey, StringPool.UNDERLINE);
	}

	protected boolean hasSecret(String weDeployKey, String name) {
		try {
			getRestTemplate().exchange(
				StringBundler.concat(
					_PROJECT_API_URL + getProjectId(weDeployKey), "/secrets/",
					name),
				HttpMethod.GET, null, Object.class);

			return true;
		}
		catch (Exception exception) {
			return false;
		}
	}

	protected boolean isReady(FaroProject faroProject) {
		try {
			_contactsEngineClient.getIndividuals(
				faroProject, (String)null, false, 1, 0, null);

			return true;
		}
		catch (Exception exception) {
			_log.error(
				String.format(
					"Failed to check if workspace %s is ready",
					faroProject.getWeDeployKey()),
				exception);

			return false;
		}
	}

	protected void updateSecret(String weDeployKey, String name, String value) {
		getRestTemplate().exchange(
			StringBundler.concat(
				_PROJECT_API_URL, getProjectId(weDeployKey), "/secrets/", name),
			HttpMethod.PUT,
			new HttpEntity<Object>(
				new HashMap<String, String>() {
					{
						put("name", name);
						put("value", value);
					}
				}),
			Void.class);
	}

	protected List<LCPService> waitForLCPServices(String weDeployKey)
		throws Exception {

		long startTime = System.currentTimeMillis();

		List<LCPService> lcpServices = getLCPServices(weDeployKey);

		while (lcpServices.isEmpty()) {
			Thread.sleep(10 * Time.SECOND);

			lcpServices = getLCPServices(weDeployKey);

			if ((System.currentTimeMillis() - startTime) > Time.HOUR) {
				_log.error("Unable to deploy services to " + weDeployKey);

				return Collections.emptyList();
			}
		}

		return lcpServices;
	}

	private static final String _PROJECT_API_URL = GetterUtil.getString(
		System.getenv("FARO_DXP_CLOUD_API_URL"),
		"https://api.liferay.cloud/projects/");

	private static final String _REPOSITORY_TOKEN = System.getenv(
		"FARO_REPOSITORY_TOKEN");

	private static final String _WEDEPLOY_EMAIL_ADDRESS = System.getenv(
		"FARO_WEDEPLOY_EMAIL_ADDRESS");

	private static final String _WEDEPLOY_PASSWORD = System.getenv(
		"FARO_WEDEPLOY_PASSWORD");

	private static final Log _log = LogFactoryUtil.getLog(
		WorkspaceEngineClientImpl.class);

	private static final List<String> _secretKeys = Arrays.asList(
		"ELASTICSEARCH_PASSWORD", "ELASTICSEARCH_USER",
		"OSB_ASAH_SECURITY_TOKEN");

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
			catch (Exception exception) {
				_log.error(exception, exception);
			}
		}

		protected void doRun() throws Exception {
			List<String> envVarSecretNames = new ArrayList<>();

			for (String secretKey : _secretKeys) {
				String secretValue = System.getenv(secretKey);

				if (Validator.isNull(secretValue)) {
					continue;
				}

				envVarSecretNames.add(secretKey);
				envVarSecretNames.add(getSecretName(secretKey));
			}

			for (LCPService lcpService :
					waitForLCPServices(_workspace.getWeDeployKey())) {

				attachSecrets(
					_workspace.getWeDeployKey(), lcpService.getServiceId(),
					envVarSecretNames.toArray(new String[0]));
			}

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
				faroProject.setState(
					FaroProjectConstants.STATE_AUTO_REDEPLOY_FAILED);

				_faroProjectLocalService.updateFaroProject(faroProject);
			}

			_log.error("Unable to deploy to " + _workspace.getWeDeployKey());
		}

		private final String _sha;
		private final boolean _trial;
		private final Workspace _workspace;

	}

}