/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.runtime.util.comparator;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorAdapter;
import com.liferay.portal.kernel.workflow.WorkflowInstance;
import com.liferay.portal.workflow.kaleo.KaleoWorkflowModelConverter;
import com.liferay.portal.workflow.kaleo.model.KaleoInstance;
import com.liferay.portal.workflow.kaleo.model.KaleoInstanceToken;

/**
 * @author William Newbury
 */
public class KaleoInstanceOrderByComparator
	extends OrderByComparatorAdapter<KaleoInstance, WorkflowInstance> {

	public static OrderByComparator<KaleoInstance> getOrderByComparator(
		OrderByComparator<WorkflowInstance> orderByComparator,
		KaleoWorkflowModelConverter kaleoWorkflowModelConverter,
		ServiceContext serviceContext) {

		if (orderByComparator == null) {
			return null;
		}

		return new KaleoInstanceOrderByComparator(
			orderByComparator, kaleoWorkflowModelConverter, serviceContext);
	}

	public WorkflowInstance adapt(KaleoInstance kaleoInstance) {
		try {
			KaleoInstanceToken rootKaleoInstanceToken =
				kaleoInstance.getRootKaleoInstanceToken(_serviceContext);

			return _kaleoWorkflowModelConverter.toWorkflowInstance(
				kaleoInstance, rootKaleoInstanceToken);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	private KaleoInstanceOrderByComparator(
		OrderByComparator<WorkflowInstance> orderByComparator,
		KaleoWorkflowModelConverter kaleoWorkflowModelConverter,
		ServiceContext serviceContext) {

		super(orderByComparator);

		_kaleoWorkflowModelConverter = kaleoWorkflowModelConverter;
		_serviceContext = serviceContext;
	}

	private final KaleoWorkflowModelConverter _kaleoWorkflowModelConverter;
	private final ServiceContext _serviceContext;

}