/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.service.impl;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskForm;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskFormInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoTaskInstanceToken;
import com.liferay.portal.workflow.kaleo.runtime.form.FormValueProcessor;
import com.liferay.portal.workflow.kaleo.service.base.KaleoTaskFormInstanceLocalServiceBaseImpl;

import java.util.Date;
import java.util.List;

import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Michael C. Han
 */
public class KaleoTaskFormInstanceLocalServiceImpl
	extends KaleoTaskFormInstanceLocalServiceBaseImpl {

	@Override
	public KaleoTaskFormInstance addKaleoTaskFormInstance(
			long groupId, long kaleoTaskFormId, String formValues,
			KaleoTaskInstanceToken kaleoTaskInstanceToken,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(serviceContext.getGuestOrUserId());
		Date now = new Date();

		long kaleoTaskFormInstanceId = counterLocalService.increment();

		KaleoTaskFormInstance kaleoTaskFormInstance =
			kaleoTaskFormInstancePersistence.create(kaleoTaskFormInstanceId);

		kaleoTaskFormInstance.setGroupId(groupId);
		kaleoTaskFormInstance.setCompanyId(user.getCompanyId());
		kaleoTaskFormInstance.setUserId(user.getUserId());
		kaleoTaskFormInstance.setUserName(user.getFullName());
		kaleoTaskFormInstance.setCreateDate(now);
		kaleoTaskFormInstance.setModifiedDate(now);
		kaleoTaskFormInstance.setKaleoDefinitionVersionId(
			kaleoTaskInstanceToken.getKaleoDefinitionVersionId());
		kaleoTaskFormInstance.setKaleoInstanceId(
			kaleoTaskInstanceToken.getKaleoInstanceId());
		kaleoTaskFormInstance.setKaleoTaskId(
			kaleoTaskInstanceToken.getKaleoTaskId());
		kaleoTaskFormInstance.setKaleoTaskInstanceTokenId(
			kaleoTaskInstanceToken.getKaleoTaskInstanceTokenId());
		kaleoTaskFormInstance.setKaleoTaskFormId(kaleoTaskFormId);

		KaleoTaskForm kaleoTaskForm =
			kaleoTaskFormLocalService.getKaleoTaskForm(kaleoTaskFormId);

		if (Validator.isNotNull(kaleoTaskForm.getFormDefinition())) {
			kaleoTaskFormInstance.setFormValues(formValues);
		}
		else {
			FormValueProcessor formValueProcessor = getFormValueProcessor();

			if (formValueProcessor != null) {
				kaleoTaskFormInstance = formValueProcessor.processFormValues(
					kaleoTaskForm, kaleoTaskFormInstance, formValues,
					serviceContext);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn(
						StringBundler.concat(
							"No form value processor defined to for form: ",
							kaleoTaskForm.getKaleoTaskFormId(), " and values: ",
							formValues));
				}
			}
		}

		return kaleoTaskFormInstancePersistence.update(kaleoTaskFormInstance);
	}

	@Override
	public int countKaleoTaskFormInstanceByKaleoTaskId(long kaleoTaskId) {
		return kaleoTaskFormInstancePersistence.countByKaleoTaskId(kaleoTaskId);
	}

	@Override
	public void deleteCompanyKaleoTaskFormInstances(long companyId) {
		kaleoTaskFormInstancePersistence.removeByCompanyId(companyId);
	}

	@Override
	public void deleteKaleoDefinitionVersionKaleoTaskFormInstances(
		long kaleoDefinitionVersionId) {

		kaleoTaskFormInstancePersistence.removeByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId);
	}

	@Override
	public void deleteKaleoInstanceKaleoTaskFormInstances(
		long kaleoInstanceId) {

		kaleoTaskFormInstancePersistence.removeByKaleoInstanceId(
			kaleoInstanceId);
	}

	@Override
	public KaleoTaskFormInstance fetchKaleoTaskFormKaleoTaskFormInstance(
		long kaleoTaskFormId) {

		return kaleoTaskFormInstancePersistence.fetchByKaleoTaskFormId(
			kaleoTaskFormId);
	}

	@Override
	public KaleoTaskFormInstance getKaleoTaskFormKaleoTaskFormInstance(
			long kaleoTaskFormId)
		throws PortalException {

		return kaleoTaskFormInstancePersistence.findByKaleoTaskFormId(
			kaleoTaskFormId);
	}

	@Override
	public List<KaleoTaskFormInstance> getKaleoTaskKaleoTaskFormInstances(
		long kaleoTaskId) {

		return kaleoTaskFormInstancePersistence.findByKaleoTaskId(kaleoTaskId);
	}

	protected FormValueProcessor getFormValueProcessor() {
		return _serviceTracker.getService();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoTaskFormInstanceLocalServiceImpl.class);

	private static final ServiceTracker<FormValueProcessor, FormValueProcessor>
		_serviceTracker = ServiceTrackerFactory.open(FormValueProcessor.class);

}