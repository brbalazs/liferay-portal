/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.punchout.internal.model.listener;

import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.punchout.service.PunchoutAccountRoleHelper;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;

/**
 * @author Jaclyn Ong
 */
@Component(immediate = true, service = ModelListener.class)
public class CommerceOrderModelListener
	extends BaseModelListener<CommerceOrder> {

	@Override
	public void onAfterUpdate(CommerceOrder commerceOrder) {
		try {
			if ((commerceOrder.getStatus() == WorkflowConstants.STATUS_APPROVED) ||
				(!_punchoutAccountRoleHelper.hasPunchoutRole(
					commerceOrder.getCompanyId(), commerceOrder.getUserId(),
					commerceOrder.getCommerceAccountId()))) {

				return;
			}

			ServiceContext serviceContext = new ServiceContext();

			_commerceOrderLocalService.updateStatus(
				commerceOrder.getUserId(), commerceOrder.getCommerceOrderId(),
				WorkflowConstants.STATUS_APPROVED, serviceContext, Collections.emptyMap());
		}
		catch (PortalException e) {
			_log.error(
				"Failed to update workflow status to Approved on punchout " +
					"order (" + commerceOrder.getCommerceOrderId() + ")");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderModelListener.class);

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private PunchoutAccountRoleHelper _punchoutAccountRoleHelper;

}