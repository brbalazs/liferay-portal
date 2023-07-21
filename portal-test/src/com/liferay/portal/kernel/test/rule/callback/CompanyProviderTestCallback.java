/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.rule.callback;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.test.util.TestPropsValues;

import org.junit.runner.Description;

/**
 * @author Cristina González
 */
public class CompanyProviderTestCallback extends BaseTestCallback<Long, Void> {

	public static final CompanyProviderTestCallback INSTANCE =
		new CompanyProviderTestCallback();

	@Override
	public void afterClass(Description description, Long previousCompanyId) {
		CompanyThreadLocal.setCompanyId(previousCompanyId);
	}

	@Override
	public Long beforeClass(Description description) throws PortalException {
		Long companyId = CompanyThreadLocal.getCompanyId();

		CompanyThreadLocal.setCompanyId(TestPropsValues.getCompanyId());

		return companyId;
	}

	private CompanyProviderTestCallback() {
	}

}