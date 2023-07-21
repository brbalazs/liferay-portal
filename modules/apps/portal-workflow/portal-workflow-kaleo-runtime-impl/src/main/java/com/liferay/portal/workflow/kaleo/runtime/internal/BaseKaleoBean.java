/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.runtime.internal;

import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.workflow.kaleo.service.KaleoActionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoConditionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionVersionLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoInstanceLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoInstanceTokenLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoLogLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoNodeLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoNotificationLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoNotificationRecipientLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskAssignmentLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskFormInstanceLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskFormLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskInstanceTokenLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTaskLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTimerInstanceTokenLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTimerLocalService;
import com.liferay.portal.workflow.kaleo.service.KaleoTransitionLocalService;

/**
 * @author Brian Wing Shun Chan
 */
public class BaseKaleoBean {

	@ServiceReference(type = KaleoActionLocalService.class)
	protected KaleoActionLocalService kaleoActionLocalService;

	@ServiceReference(type = KaleoConditionLocalService.class)
	protected KaleoConditionLocalService kaleoConditionLocalService;

	@ServiceReference(type = KaleoDefinitionLocalService.class)
	protected KaleoDefinitionLocalService kaleoDefinitionLocalService;

	@ServiceReference(type = KaleoDefinitionVersionLocalService.class)
	protected KaleoDefinitionVersionLocalService
		kaleoDefinitionVersionLocalService;

	@ServiceReference(type = KaleoInstanceLocalService.class)
	protected KaleoInstanceLocalService kaleoInstanceLocalService;

	@ServiceReference(type = KaleoInstanceTokenLocalService.class)
	protected KaleoInstanceTokenLocalService kaleoInstanceTokenLocalService;

	@ServiceReference(type = KaleoLogLocalService.class)
	protected KaleoLogLocalService kaleoLogLocalService;

	@ServiceReference(type = KaleoNodeLocalService.class)
	protected KaleoNodeLocalService kaleoNodeLocalService;

	@ServiceReference(type = KaleoNotificationLocalService.class)
	protected KaleoNotificationLocalService kaleoNotificationLocalService;

	@ServiceReference(type = KaleoNotificationRecipientLocalService.class)
	protected KaleoNotificationRecipientLocalService
		kaleoNotificationRecipientLocalService;

	@ServiceReference(type = KaleoTaskAssignmentLocalService.class)
	protected KaleoTaskAssignmentLocalService kaleoTaskAssignmentLocalService;

	@ServiceReference(type = KaleoTaskFormInstanceLocalService.class)
	protected KaleoTaskFormInstanceLocalService
		kaleoTaskFormInstanceLocalService;

	@ServiceReference(type = KaleoTaskFormLocalService.class)
	protected KaleoTaskFormLocalService kaleoTaskFormLocalService;

	@ServiceReference(type = KaleoTaskInstanceTokenLocalService.class)
	protected KaleoTaskInstanceTokenLocalService
		kaleoTaskInstanceTokenLocalService;

	@ServiceReference(type = KaleoTaskLocalService.class)
	protected KaleoTaskLocalService kaleoTaskLocalService;

	@ServiceReference(type = KaleoTimerInstanceTokenLocalService.class)
	protected KaleoTimerInstanceTokenLocalService
		kaleoTimerInstanceTokenLocalService;

	@ServiceReference(type = KaleoTimerLocalService.class)
	protected KaleoTimerLocalService kaleoTimerLocalService;

	@ServiceReference(type = KaleoTransitionLocalService.class)
	protected KaleoTransitionLocalService kaleoTransitionLocalService;

	@ServiceReference(type = WorkflowDefinitionLinkLocalService.class)
	protected WorkflowDefinitionLinkLocalService
		workflowDefinitionLinkLocalService;

}