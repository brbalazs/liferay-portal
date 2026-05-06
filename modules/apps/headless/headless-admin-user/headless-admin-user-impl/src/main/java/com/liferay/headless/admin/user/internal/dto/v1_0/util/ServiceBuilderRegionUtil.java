/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.user.internal.dto.v1_0.util;

import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.RegionLocalServiceUtil;
import com.liferay.portal.kernel.service.RegionServiceUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

/**
 * @author Drew Brokke
 */
public class ServiceBuilderRegionUtil {

	public static long getServiceBuilderRegionId(
		String addressRegion, long countryId) {

		return getServiceBuilderRegionId(addressRegion, null, countryId);
	}

	public static long getServiceBuilderRegionId(
		String addressRegion, String addressRegionExternalReferenceCode,
		long countryId) {

		if (Validator.isNotNull(addressRegionExternalReferenceCode)) {
			Region region =
				RegionLocalServiceUtil.fetchRegionByExternalReferenceCode(
					addressRegionExternalReferenceCode,
					CompanyThreadLocal.getCompanyId());

			if (region != null) {
				return region.getRegionId();
			}
		}

		if (Validator.isNull(addressRegion) || (countryId <= 0)) {
			return 0;
		}

		Region region = RegionServiceUtil.fetchRegion(countryId, addressRegion);

		if (region != null) {
			return region.getRegionId();
		}

		List<Region> regions = RegionServiceUtil.getRegions(countryId);

		for (Region curRegion : regions) {
			if (StringUtil.equalsIgnoreCase(
					addressRegion, curRegion.getName())) {

				return curRegion.getRegionId();
			}
		}

		return 0;
	}

}