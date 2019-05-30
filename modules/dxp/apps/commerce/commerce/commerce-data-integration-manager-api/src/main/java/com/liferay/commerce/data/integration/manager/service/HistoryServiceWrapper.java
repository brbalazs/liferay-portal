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

package com.liferay.commerce.data.integration.manager.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link HistoryService}.
 *
 * @author Marco Leo
 * @see HistoryService
 * @generated
 */
@ProviderType
public class HistoryServiceWrapper
	implements HistoryService, ServiceWrapper<HistoryService> {

	public HistoryServiceWrapper(HistoryService historyService) {
		_historyService = historyService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _historyService.getOSGiServiceIdentifier();
	}

	@Override
	public HistoryService getWrappedService() {
		return _historyService;
	}

	@Override
	public void setWrappedService(HistoryService historyService) {
		_historyService = historyService;
	}

	private HistoryService _historyService;

}