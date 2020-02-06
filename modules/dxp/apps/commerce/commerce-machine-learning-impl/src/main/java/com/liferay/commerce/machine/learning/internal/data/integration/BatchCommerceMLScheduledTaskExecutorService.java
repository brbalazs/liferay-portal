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

package com.liferay.commerce.machine.learning.internal.data.integration;

import com.liferay.batch.engine.BatchEngineExportTaskExecutor;
import com.liferay.batch.engine.BatchEngineImportTaskExecutor;
import com.liferay.batch.engine.BatchEngineTaskContentType;
import com.liferay.batch.engine.BatchEngineTaskExecuteStatus;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.model.BatchEngineExportTask;
import com.liferay.batch.engine.model.BatchEngineImportTask;
import com.liferay.batch.engine.service.BatchEngineExportTaskLocalService;
import com.liferay.batch.engine.service.BatchEngineImportTaskLocalService;
import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcessLog;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLocalService;
import com.liferay.commerce.data.integration.service.CommerceDataIntegrationProcessLogLocalService;
import com.liferay.commerce.machine.learning.internal.gateway.CommerceMLGatewayClient;
import com.liferay.commerce.machine.learning.internal.gateway.CommerceMLJobState;
import com.liferay.commerce.machine.learning.internal.gateway.CommerceMLJobStateConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.File;
import java.io.InputStream;

import java.nio.file.Files;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(
	immediate = true,
	service = BatchCommerceMLScheduledTaskExecutorService.class
)
public class BatchCommerceMLScheduledTaskExecutorService {

	public void executeScheduledTask(
			long commerceDataIntegrationProcessId,
			BatchEngineTaskItemDelegateResourceMapper[] exportResourceNameList,
			Map<String, String> contextProperties,
			BatchEngineTaskItemDelegateResourceMapper[] importResources)
		throws PortalException {

		CommerceDataIntegrationProcess commerceDataIntegrationProcess =
			_commerceDataIntegrationProcessLocalService.
				getCommerceDataIntegrationProcess(
					commerceDataIntegrationProcessId);

		Date startDate = new Date();

		CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog =
			_commerceDataIntegrationProcessLogLocalService.
				addCommerceDataIntegrationProcessLog(
					commerceDataIntegrationProcess.getUserId(),
					commerceDataIntegrationProcess.
						getCommerceDataIntegrationProcessId(),
					null, null, BackgroundTaskConstants.STATUS_IN_PROGRESS,
					startDate, null);

		try {
			for (BatchEngineTaskItemDelegateResourceMapper exportResourceName :
					exportResourceNameList) {

				commerceDataIntegrationProcessLog = runExportTask(
					commerceDataIntegrationProcess,
					commerceDataIntegrationProcessLog, exportResourceName);
			}

			UnicodeProperties typeSettingsProperties =
				commerceDataIntegrationProcess.getTypeSettingsProperties();

			typeSettingsProperties.putAll(contextProperties);

			CommerceMLJobState commerceMLJobState =
				_commerceMLGatewayClient.startCommerceMLJob(
					typeSettingsProperties);

			commerceDataIntegrationProcessLog = _appendToLogOutput(
				commerceDataIntegrationProcessLog,
				"Starting job: " + commerceMLJobState.getApplicationId());

			if (commerceMLJobState.getApplicationId() != null) {
				_pollAndWait(
					commerceMLJobState.getApplicationId(),
					commerceDataIntegrationProcess.getTypeSettingsProperties());

				commerceDataIntegrationProcessLog = _appendToLogOutput(
					commerceDataIntegrationProcessLog,
					"Completed job: " + commerceMLJobState.getApplicationId());

				for (BatchEngineTaskItemDelegateResourceMapper importResource :
						importResources) {

					File file =
						_commerceMLGatewayClient.downloadCommerceMLJobResult(
							commerceMLJobState.getApplicationId(),
							importResource.getResourceName(),
							typeSettingsProperties);

					commerceDataIntegrationProcessLog = runImportTask(
						commerceDataIntegrationProcess,
						commerceDataIntegrationProcessLog, importResource,
						file);
				}
			}

			commerceDataIntegrationProcessLog.setEndDate(new Date());

			commerceDataIntegrationProcessLog.setStatus(
				BackgroundTaskConstants.STATUS_SUCCESSFUL);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}

			commerceDataIntegrationProcessLog.setError(e.getMessage());

			commerceDataIntegrationProcessLog.setEndDate(new Date());

			commerceDataIntegrationProcessLog.setStatus(
				BackgroundTaskConstants.STATUS_FAILED);
		}

		_commerceDataIntegrationProcessLogLocalService.
			updateCommerceDataIntegrationProcessLog(
				commerceDataIntegrationProcessLog);
	}

	protected CommerceDataIntegrationProcessLog runExportTask(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess,
			CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog,
			BatchEngineTaskItemDelegateResourceMapper
				batchEnginetaskItemDelegateResourceMapper)
		throws Exception {

		_appendToLogOutput(
			commerceDataIntegrationProcessLog,
			"Start exporting: " +
				batchEnginetaskItemDelegateResourceMapper.getResourceName());

		BatchEngineExportTask batchEngineExportTask =
			_batchEngineExportTaskLocalService.addBatchEngineExportTask(
				commerceDataIntegrationProcess.getCompanyId(),
				commerceDataIntegrationProcess.getUserId(), null,
				batchEnginetaskItemDelegateResourceMapper.getResourceName(),
				BatchEngineTaskContentType.JSONL.name(),
				BatchEngineTaskExecuteStatus.INITIAL.name(), null,
				new HashMap<>(),
				batchEnginetaskItemDelegateResourceMapper.
					getBatchEngineTaskItemDelegate());

		_batchEngineExportTaskExecutor.execute(batchEngineExportTask);

		BatchEngineTaskExecuteStatus batchEngineTaskExecuteStatus =
			BatchEngineTaskExecuteStatus.valueOf(
				batchEngineExportTask.getExecuteStatus());

		if (batchEngineTaskExecuteStatus.equals(
				BatchEngineTaskExecuteStatus.COMPLETED)) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Batch Export process completed, uploading: " +
						batchEngineExportTask.getClassName());
			}

			_appendToLogOutput(
				commerceDataIntegrationProcessLog,
				"Start uploading: " +
					batchEnginetaskItemDelegateResourceMapper.
						getResourceName());

			_uploadExport(
				batchEngineExportTask, commerceDataIntegrationProcess);

			_batchEngineExportTaskLocalService.deleteBatchEngineExportTask(
				batchEngineExportTask);
		}
		else {
			throw new PortalException(
				"Error exporting: " +
					batchEnginetaskItemDelegateResourceMapper.
						getResourceName());
		}

		return commerceDataIntegrationProcessLog;
	}

	protected CommerceDataIntegrationProcessLog runImportTask(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess,
			CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog,
			BatchEngineTaskItemDelegateResourceMapper
				batchEnginetaskItemDelegateResourceMapper,
			File resourceFile)
		throws Exception {

		_appendToLogOutput(
			commerceDataIntegrationProcessLog,
			"Start import task: " +
				batchEnginetaskItemDelegateResourceMapper.getResourceName());

		BatchEngineImportTask batchEngineImportTask =
			_batchEngineImportTaskLocalService.addBatchEngineImportTask(
				commerceDataIntegrationProcess.getCompanyId(),
				commerceDataIntegrationProcess.getUserId(), 20, null,
				batchEnginetaskItemDelegateResourceMapper.getResourceName(),
				Files.readAllBytes(resourceFile.toPath()),
				BatchEngineTaskContentType.JSONL.name(),
				BatchEngineTaskExecuteStatus.INITIAL.name(),
				batchEnginetaskItemDelegateResourceMapper.getFieldMapping(),
				BatchEngineTaskOperation.CREATE.name(), null,
				batchEnginetaskItemDelegateResourceMapper.
					getBatchEngineTaskItemDelegate());

		_batchEngineImportTaskExecutor.execute(batchEngineImportTask);

		BatchEngineTaskExecuteStatus batchEngineTaskExecuteStatus =
			BatchEngineTaskExecuteStatus.valueOf(
				batchEngineImportTask.getExecuteStatus());

		if (batchEngineTaskExecuteStatus.equals(
				BatchEngineTaskExecuteStatus.COMPLETED)) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Batch Import process completed for entity: " +
						batchEngineImportTask.getClassName());
			}

			_batchEngineImportTaskLocalService.deleteBatchEngineImportTask(
				batchEngineImportTask);

			_appendToLogOutput(
				commerceDataIntegrationProcessLog,
				"Completed import task: " +
					batchEnginetaskItemDelegateResourceMapper.
						getResourceName());
		}
		else {
			throw new PortalException(
				"Error importing resource: " +
					batchEnginetaskItemDelegateResourceMapper.
						getResourceName());
		}

		return commerceDataIntegrationProcessLog;
	}

	protected static final DateFormat dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private CommerceDataIntegrationProcessLog _appendToLogOutput(
		CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog,
		String message) {

		StringBundler sb = new StringBundler(5);

		sb.append(commerceDataIntegrationProcessLog.getOutput());
		sb.append(dateFormat.format(new Date()));
		sb.append(StringPool.SPACE);
		sb.append(message);
		sb.append(StringPool.NEW_LINE);

		commerceDataIntegrationProcessLog.setOutput(sb.toString());

		commerceDataIntegrationProcessLog.setEndDate(new Date());

		return _commerceDataIntegrationProcessLogLocalService.
			updateCommerceDataIntegrationProcessLog(
				commerceDataIntegrationProcessLog);
	}

	private void _pollAndWait(
			String applicationId, UnicodeProperties unicodeProperties)
		throws Exception {

		int pollCount = 0;

		while (pollCount < _MAX_POLL_COUNT) {
			CommerceMLJobState commerceMLJobState =
				_commerceMLGatewayClient.getCommerceMLJobState(
					applicationId, unicodeProperties);

			String state = commerceMLJobState.getState();

			if (state.equalsIgnoreCase(CommerceMLJobStateConstants.COMPLETE)) {
				return;
			}
			else if (state.equalsIgnoreCase(
						CommerceMLJobStateConstants.ERROR)) {

				_log.error("Application failed");

				throw new Exception("ML Job failed with an error");
			}
			else {
				if (_log.isDebugEnabled()) {
					_log.debug("Remote application status: " + state);
				}
			}

			pollCount++;

			Thread.sleep(60 * 1000);
		}

		throw new Exception("Timeout waiting for ML Job completion");
	}

	private void _uploadExport(
			BatchEngineExportTask batchEngineExportTask,
			CommerceDataIntegrationProcess commerceDataIntegrationProcess)
		throws Exception {

		InputStream inputStream =
			_batchEngineExportTaskLocalService.openContentInputStream(
				batchEngineExportTask.getBatchEngineExportTaskId());

		_commerceMLGatewayClient.uploadCommerceMLJobResource(
			batchEngineExportTask.getClassName(), inputStream,
			commerceDataIntegrationProcess.getTypeSettingsProperties());

		inputStream.close();
	}

	private static final int _MAX_POLL_COUNT = 180;

	private static final Log _log = LogFactoryUtil.getLog(
		BatchCommerceMLScheduledTaskExecutorService.class);

	@Reference
	private BatchEngineExportTaskExecutor _batchEngineExportTaskExecutor;

	@Reference
	private BatchEngineExportTaskLocalService
		_batchEngineExportTaskLocalService;

	@Reference
	private BatchEngineImportTaskExecutor _batchEngineImportTaskExecutor;

	@Reference
	private BatchEngineImportTaskLocalService
		_batchEngineImportTaskLocalService;

	@Reference
	private CommerceDataIntegrationProcessLocalService
		_commerceDataIntegrationProcessLocalService;

	@Reference
	private CommerceDataIntegrationProcessLogLocalService
		_commerceDataIntegrationProcessLogLocalService;

	@Reference
	private CommerceMLGatewayClient _commerceMLGatewayClient;

}