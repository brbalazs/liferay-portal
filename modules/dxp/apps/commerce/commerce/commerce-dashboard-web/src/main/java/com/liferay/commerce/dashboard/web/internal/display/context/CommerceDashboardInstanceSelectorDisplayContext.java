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

package com.liferay.commerce.dashboard.web.internal.display.context;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.util.comparator.CPInstanceSkuComparator;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;

import javax.portlet.RenderRequest;

/**
 * @author Andrea Di Giorgi
 */
public class CommerceDashboardInstanceSelectorDisplayContext
	extends CommerceDashboardDisplayContext {

	public CommerceDashboardInstanceSelectorDisplayContext(
			ConfigurationProvider configurationProvider,
			CPInstanceLocalService cpInstanceLocalService,
			CPInstanceService cpInstanceService, RenderRequest renderRequest)
		throws PortalException {

		super(configurationProvider, renderRequest);

		_cpInstanceLocalService = cpInstanceLocalService;
		_cpInstanceService = cpInstanceService;
	}

	public CPInstance getCPInstance(long cpInstanceId) {
		try {
			return _cpInstanceService.getCPInstance(cpInstanceId);
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(pe, pe);
			}

			return null;
		}
	}

	public String getCPInstanceLabel(CPInstance cpInstance)
		throws PortalException {

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		String languageId = LanguageUtil.getLanguageId(
			commerceDashboardRequestHelper.getLocale());

		return cpInstance.getSku() + " - " + cpDefinition.getName(languageId);
	}

	public List<CPInstance> getCPInstances() throws PortalException {
		return _cpInstanceLocalService.getCPInstances(
			commerceDashboardRequestHelper.getScopeGroupId(),
			WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new CPInstanceSkuComparator(true));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceDashboardInstanceSelectorDisplayContext.class);

	private final CPInstanceLocalService _cpInstanceLocalService;
	private final CPInstanceService _cpInstanceService;

}