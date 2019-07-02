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

package com.liferay.commerce.recommend.internal.instance.lifecycle;

import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;
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
public class CommerceRecommendIndexPortalInstanceLifecycleListener
	extends BasePortalInstanceLifecycleListener {

	@Override
	public void portalInstanceRegistered(Company company) throws Exception {
		try {
			for (CommerceRecommendIndexer commerceRecommendIndexer :
					_commerceRecommendIndexers) {

				commerceRecommendIndexer.createIndex(company.getCompanyId());
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
			for (CommerceRecommendIndexer commerceRecommendIndexer :
					_commerceRecommendIndexers) {

				commerceRecommendIndexer.dropIndex(company.getCompanyId());
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
		service = CommerceRecommendIndexer.class
	)
	protected void setCommerceRecommendIndexer(
		CommerceRecommendIndexer commerceRecommendIndexer) {

		_commerceRecommendIndexers.add(commerceRecommendIndexer);

		if (_companyLocalService == null) {
			_queuedCommerceRecommendIndexers.add(commerceRecommendIndexer);

			return;
		}

		verifyCompanies(commerceRecommendIndexer);
	}

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;

		for (CommerceRecommendIndexer queuedCommerceRecommendIndexer :
				_queuedCommerceRecommendIndexers) {

			verifyCompanies(queuedCommerceRecommendIndexer);
		}

		_queuedCommerceRecommendIndexers.clear();
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	protected void unsetCommerceRecommendIndexer(
		CommerceRecommendIndexer commerceRecommendIndexer) {

		_commerceRecommendIndexers.remove(commerceRecommendIndexer);
	}

	protected void verifyCompanies(
		CommerceRecommendIndexer commerceRecommendIndexer) {

		for (Company company : _companyLocalService.getCompanies()) {
			commerceRecommendIndexer.createIndex(company.getCompanyId());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceRecommendIndexPortalInstanceLifecycleListener.class);

	private final List<CommerceRecommendIndexer> _commerceRecommendIndexers =
		new CopyOnWriteArrayList<>();
	private CompanyLocalService _companyLocalService;
	private final Set<CommerceRecommendIndexer>
		_queuedCommerceRecommendIndexers = Collections.newSetFromMap(
			new ConcurrentHashMap<>());

}