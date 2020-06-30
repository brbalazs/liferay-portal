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

package com.liferay.osb.faro.admin.web.internal.portlet;

import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.WorkspaceEngineClient;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.engine.client.model.WorkspaceService;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.util.UpgradeUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = UpgradeExecutor.class)
public class UpgradeExecutor {

	public Map<String, String> getProgress() {
		if (_isUpgradeDone()) {
			_shutdown();
		}

		return _upgradeProgress;
	}

	@Deactivate
	public void stop() {
		for (FutureTask futureTask : _futureTasks.values()) {
			futureTask.cancel(true);
		}

		_shutdown();
	}

	public void stop(long faroProjectId) throws Exception {
		FaroProject faroProject = _faroProjectLocalService.getFaroProject(
			faroProjectId);

		FutureTask futureTask = _futureTasks.get(faroProject.getWeDeployKey());

		if (futureTask != null) {
			futureTask.cancel(true);
		}

		_upgradeProgress.put(_getKey(faroProject), "Stopped");
	}

	public void upgrade(
			boolean refreshLiferay, int threadCount, boolean waitForHealthy)
		throws Exception {

		for (FutureTask futureTask : _futureTasks.values()) {
			if (!futureTask.isDone()) {
				throw new Exception("Upgrade currently in progress");
			}
		}

		_futureTasks.clear();
		_upgradeProgress.clear();

		String version = UpgradeUtil.getLatestVersion();

		for (FaroProject faroProject :
				ListUtil.sort(
					_faroProjectLocalService.getFaroProjects(
						QueryUtil.ALL_POS, QueryUtil.ALL_POS),
					(faroProject1, faroProject2) -> {
						String key1 = _getKey(faroProject1);

						return key1.compareTo(_getKey(faroProject2));
					})) {

			_addUpgradeTask(
				faroProject, version, refreshLiferay, threadCount,
				waitForHealthy);
		}
	}

	public void upgrade(
			long faroProjectId, boolean refreshLiferay, boolean waitForHealthy)
		throws Exception {

		FaroProject faroProject = _faroProjectLocalService.getFaroProject(
			faroProjectId);

		FutureTask futureTask = _futureTasks.get(faroProject.getWeDeployKey());

		if ((futureTask != null) && !futureTask.isDone()) {
			throw new Exception("Upgrade currently in progress");
		}

		_addUpgradeTask(
			faroProject, UpgradeUtil.getLatestVersion(), refreshLiferay, 1,
			waitForHealthy);
	}

	private void _addUpgradeTask(
		FaroProject faroProject, String version, boolean refreshLiferay,
		int threadCount, boolean waitForHealthy) {

		_upgradeProgress.put(_getKey(faroProject), "Queued");

		FutureTask futureTask = new FutureTask<>(
			() -> {
				_upgrade(faroProject, version, refreshLiferay, waitForHealthy);

				return null;
			});

		_futureTasks.put(faroProject.getWeDeployKey(), futureTask);

		ExecutorService executorService = _getExecutorService(
			faroProject.getServerLocation(), threadCount);

		executorService.execute(futureTask);
	}

	private void _buildServices(
			FaroProject faroProject, String version,
			String[] expectedServiceIds, boolean waitForHealthy)
		throws Exception {

		long startTime = System.currentTimeMillis();

		_upgradeProgress.put(_getKey(faroProject), "Building services");

		_workspaceEngineClient.updateWorkspace(
			faroProject.getWeDeployKey(), version, faroProject.isTrial());

		_upgradeProgress.put(_getKey(faroProject), "Waiting for services");

		if (!waitForHealthy) {
			return;
		}

		while ((System.currentTimeMillis() - startTime) < (Time.MINUTE * 30)) {
			_checkInterrupted(faroProject.getWeDeployKey());

			if (_checkHealth(
					faroProject,
					_workspaceEngineClient.getWorkspaceServices(
						faroProject.getWeDeployKey()),
					expectedServiceIds, version)) {

				return;
			}

			Thread.sleep(Time.SECOND * 30);
		}

		throw new Exception("Unable to update workspace");
	}

	private boolean _checkHealth(
		FaroProject faroProject, List<WorkspaceService> workspaceServices,
		String[] expectedServiceIds, String version) {

		List<String> serviceIds = new ArrayList<>(
			Arrays.asList(expectedServiceIds));

		for (WorkspaceService workspaceService : workspaceServices) {
			if (!serviceIds.contains(workspaceService.getServiceId()) ||
				(StringUtil.contains(
					workspaceService.getImageHint(),
					"com-liferay-osb-asah-private") &&
				 !StringUtil.endsWith(
					 workspaceService.getImageHint(), version))) {

				continue;
			}

			if (!workspaceService.isReady()) {
				return false;
			}

			WorkspaceService.LoadBalancer loadBalancer =
				workspaceService.getLoadBalancer();

			if (Validator.isNotNull(loadBalancer.getTargetPort()) &&
				!StringUtil.equals(
					workspaceService.getHealth(),
					Workspace.Health.healthy.name())) {

				return false;
			}

			serviceIds.remove(workspaceService.getServiceId());
		}

		if (!serviceIds.isEmpty()) {
			return false;
		}

		try {
			if (!_contactsEngineClient.isLatestVersion(faroProject)) {
				return false;
			}
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	private void _checkInterrupted(String weDeployKey)
		throws InterruptedException {

		FutureTask futureTask = _futureTasks.get(weDeployKey);

		if ((futureTask == null) || futureTask.isCancelled()) {
			throw new InterruptedException("Upgrade interrupted");
		}
	}

	private void _deleteServices(
			FaroProject faroProject, String[] expectedServiceIds)
		throws Exception {

		if (ArrayUtil.contains(
				_DELETE_SERVICES_WHITELIST, faroProject.getWeDeployKey())) {

			return;
		}

		long startTime = System.currentTimeMillis();

		_upgradeProgress.put(_getKey(faroProject), "Deleting services");

		for (WorkspaceService workspaceService :
				_workspaceEngineClient.getWorkspaceServices(
					faroProject.getWeDeployKey())) {

			if (!ArrayUtil.contains(
					expectedServiceIds, workspaceService.getServiceId())) {

				_workspaceEngineClient.deleteWorkspaceService(
					faroProject.getWeDeployKey(),
					workspaceService.getServiceId());
			}
		}

		_upgradeProgress.put(
			_getKey(faroProject), "Waiting for deleted services");

		while ((System.currentTimeMillis() - startTime) < (Time.MINUTE * 5)) {
			_checkInterrupted(faroProject.getWeDeployKey());

			if (_isDeleted(
					_workspaceEngineClient.getWorkspaceServices(
						faroProject.getWeDeployKey()),
					expectedServiceIds)) {

				return;
			}

			Thread.sleep(Time.SECOND * 30);
		}

		throw new Exception("Unable to delete services");
	}

	private ExecutorService _getExecutorService(String key, int threadCount) {
		ExecutorService executorService = _executorServices.get(key);

		if ((executorService != null) && !executorService.isShutdown()) {
			return executorService;
		}

		if (threadCount <= 0) {
			threadCount = 1;
		}

		executorService = Executors.newFixedThreadPool(threadCount);

		_executorServices.put(key, executorService);

		return executorService;
	}

	private String _getKey(FaroProject faroProject) {
		return faroProject.getServerLocation() + StringPool.DASH +
			faroProject.getWeDeployKey();
	}

	private boolean _isDeleted(
		List<WorkspaceService> workspaceServices, String[] expectedServiceIds) {

		for (WorkspaceService workspaceService : workspaceServices) {
			if (!ArrayUtil.contains(
					expectedServiceIds, workspaceService.getServiceId())) {

				return false;
			}
		}

		return true;
	}

	private boolean _isUpgradeDone() {
		for (FutureTask futureTask : _futureTasks.values()) {
			if (!futureTask.isDone()) {
				return false;
			}
		}

		return true;
	}

	private void _refreshLiferay(FaroProject faroProject) {
		ExecutorService executorService = _getExecutorService(
			"refreshLiferay", 1);

		executorService.execute(
			new FutureTask<>(
				() -> {
					try {
						List<String> dataSourceIds = new ArrayList<>();

						for (Map<String, Object> status :
								_contactsEngineClient.refreshLiferay(
									faroProject)) {

							if (!Objects.equals(
									status.get("statusCode"),
									Response.Status.OK.getStatusCode())) {

								dataSourceIds.add(
									(String)status.get("dataSourceId"));
							}
						}

						if (!dataSourceIds.isEmpty()) {
							_upgradeProgress.put(
								_getKey(faroProject),
								"Complete - Failed Refresh: " +
									ListUtil.toString(
										dataSourceIds, (String)null));
						}
						else {
							_upgradeProgress.put(
								_getKey(faroProject), "Complete");
						}
					}
					catch (Exception e) {
						_upgradeProgress.put(
							_getKey(faroProject), "Complete - Failed Refresh");
					}

					return null;
				}));
	}

	private void _shutdown() {
		for (ExecutorService executorService : _executorServices.values()) {
			executorService.shutdown();
		}

		_futureTasks.clear();
	}

	private void _upgrade(
		FaroProject faroProject, String version, boolean refreshLiferay,
		boolean waitForHealthy) {

		_upgradeProgress.put(_getKey(faroProject), "started");

		if (_log.isInfoEnabled()) {
			_log.info("Upgraded started for " + faroProject.getWeDeployKey());
		}

		try {
			String[] expectedServiceIds = null;

			if (faroProject.isTrial()) {
				expectedServiceIds = _EXPECTED_SERVICE_IDS_TRIAL;
			}
			else {
				expectedServiceIds = _EXPECTED_SERVICE_IDS_PAID;
			}

			_deleteServices(faroProject, expectedServiceIds);

			_buildServices(
				faroProject, version, expectedServiceIds, waitForHealthy);

			if (refreshLiferay) {
				_upgradeProgress.put(_getKey(faroProject), "ASAH Complete");

				_refreshLiferay(faroProject);
			}
			else {
				_upgradeProgress.put(_getKey(faroProject), "Complete");
			}

			Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(
				FaroProject.class);

			indexer.reindex(faroProject);

			if (_log.isInfoEnabled()) {
				_log.info("Finished upgrading " + faroProject.getWeDeployKey());
			}
		}
		catch (InterruptedException ie) {
			_upgradeProgress.put(_getKey(faroProject), "Stopped");
		}
		catch (Exception e) {
			_log.error("Failed to upgrade " + faroProject.getWeDeployKey(), e);

			_upgradeProgress.put(_getKey(faroProject), "Failed");
		}
	}

	private static final String[] _DELETE_SERVICES_WHITELIST = {
		"asah652a6babdba143d086a19db542781bc2.lfr.cloud"
	};

	private static final String[] _EXPECTED_SERVICE_IDS_PAID = {
		"osbasahbackend", "osbasahbatchcurator", "osbasahdxpextractor",
		"osbasahextractor", "osbasahpublisher", "osbasahqueue", "osbasahredis",
		"osbasahsalesforceextractor", "osbasahstreamcurator", "osbasahupgrade"
	};

	private static final String[] _EXPECTED_SERVICE_IDS_TRIAL = {
		"osbasahmonolith"
	};

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeExecutor.class);

	private static final Map<String, FutureTask> _futureTasks =
		new ConcurrentHashMap<>();
	private static final Map<String, String> _upgradeProgress =
		new ConcurrentSkipListMap<>(Comparator.naturalOrder());

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ContactsEngineClient _contactsEngineClient;

	private final Map<String, ExecutorService> _executorServices =
		new ConcurrentHashMap<>();

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile WorkspaceEngineClient _workspaceEngineClient;

}