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

package com.liferay.commerce.machine.learning.internal.search.instance.lifecycle;

import com.liferay.commerce.machine.learning.internal.search.api.CommerceIndexer;
import com.liferay.portal.instance.lifecycle.BasePortalInstanceLifecycleListener;
import com.liferay.portal.instance.lifecycle.PortalInstanceLifecycleListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.CompanyLocalService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Marco Leo
 */
@Component(immediate = true, service = PortalInstanceLifecycleListener.class)
public class CommerceIndexPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		try {
			for (CommerceIndexer commerceIndexer : _commerceIndexers) {
				commerceIndexer.createIndex(company.getCompanyId());
			}
		}
		catch (Exception e) {
			_log.error(
				"Unable to add commerce recommend index for company " + company,
				e);
		}
	}

	@Override
	public void portalInstanceUnregistered(Company company) throws Exception {
		try {
			for (CommerceIndexer commerceIndexer : _commerceIndexers) {
				commerceIndexer.dropIndex(company.getCompanyId());
			}
		}
		catch (Exception e) {
			_log.error(
				"Unable to remove commerce recommend index for company " +
					company,
				e);
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		service = CommerceIndexer.class
	)
	protected void setCommerceMachineLearningIndexer(
		CommerceIndexer commerceIndexer) {

		_commerceIndexers.add(commerceIndexer);

		if (_companyLocalService == null) {
			_queuedCommerceIndexers.add(commerceIndexer);

			return;
		}

		verifyCompanies(commerceIndexer);
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;

		for (CommerceIndexer queuedCommerceIndexer : _queuedCommerceIndexers) {
			verifyCompanies(queuedCommerceIndexer);
		}

		_queuedCommerceIndexers.clear();
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	protected void unsetCommerceMachineLearningIndexer(
		CommerceIndexer commerceIndexer) {

		_commerceIndexers.remove(commerceIndexer);
	}

	protected void verifyCompanies(CommerceIndexer commerceIndexer) {
		for (Company company : _companyLocalService.getCompanies()) {
			commerceIndexer.createIndex(company.getCompanyId());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceIndexPortalInstanceLifecycleListener.class);

	private final List<CommerceIndexer> _commerceIndexers =
		new CopyOnWriteArrayList<>();
	private CompanyLocalService _companyLocalService;
	private final Set<CommerceIndexer> _queuedCommerceIndexers =
		Collections.newSetFromMap(new ConcurrentHashMap<>());

}