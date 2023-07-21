/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.internal.service;

import com.liferay.message.boards.kernel.model.MBBan;
import com.liferay.message.boards.kernel.service.MBBanService;
import com.liferay.message.boards.kernel.service.MBBanServiceWrapper;
import com.liferay.petra.model.adapter.util.ModelAdapterUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(immediate = true, service = ServiceWrapper.class)
public class ModularMBBanServiceWrapper extends MBBanServiceWrapper {

	public ModularMBBanServiceWrapper() {
		super(null);
	}

	public ModularMBBanServiceWrapper(MBBanService mbBanService) {
		super(mbBanService);
	}

	@Override
	public MBBan addBan(long banUserId, ServiceContext serviceContext)
		throws PortalException {

		return ModelAdapterUtil.adapt(
			MBBan.class, _mbBanService.addBan(banUserId, serviceContext));
	}

	@Override
	public void deleteBan(long banUserId, ServiceContext serviceContext)
		throws PortalException {

		_mbBanService.deleteBan(banUserId, serviceContext);
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return _mbBanService.getOSGiServiceIdentifier();
	}

	@Override
	public MBBanService getWrappedService() {
		return super.getWrappedService();
	}

	@Override
	public void setWrappedService(MBBanService mbBanService) {
		super.setWrappedService(mbBanService);
	}

	@Reference
	private com.liferay.message.boards.service.MBBanService _mbBanService;

}