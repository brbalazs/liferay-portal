/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.internal.model.listener;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.workflow.kaleo.runtime.manager.PortalKaleoManager;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.springframework.context.ApplicationContext;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = ModelListener.class)
public class CompanyModelListener extends BaseModelListener<Company> {

	@Override
	public void onAfterRemove(Company company) throws ModelListenerException {
		try {
			_portalKaleoManager.deleteKaleoData(company.getCompanyId());
		}
		catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference(
		target = "(org.springframework.context.service.name=com.liferay.portal.workflow.kaleo.service)",
		unbind = "-"
	)
	protected void setApplicationContext(
		ApplicationContext applicationContext) {
	}

	@Reference(unbind = "-")
	protected void setPortalKaleoManager(
		PortalKaleoManager portalKaleoManager) {

		_portalKaleoManager = portalKaleoManager;
	}

	@Reference
	private PortalKaleoManager _portalKaleoManager;

}