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

package com.liferay.portal.license.enterprise.app.internal;

import com.liferay.portal.kernel.license.messaging.LCSPortletState;
import com.liferay.portal.kernel.license.util.LicenseManager;

import java.util.Map;
import java.util.Objects;

/**
 * @author Tina Tian
 */
public class PortalLicenseEnterpriseAppLicenseUtil {

	public static int getPortalLicenseState(LicenseManager licenseManager) {
		int portalLicenseState = licenseManager.getLicenseState(
			_PRODUCT_ID_PORTAL);

		if (portalLicenseState != LicenseManager.STATE_GOOD) {
			licenseManager.checkLicense(_PRODUCT_ID_PORTAL);

			portalLicenseState = licenseManager.getLicenseState(
				_PRODUCT_ID_PORTAL);
		}

		if ((portalLicenseState != LicenseManager.STATE_GOOD) &&
			!Objects.equals(
				PortalLicenseEnterpriseAppGateKeeper.lcsPortletState,
				LCSPortletState.GOOD)) {

			return LicenseManager.STATE_ABSENT;
		}

		return LicenseManager.STATE_GOOD;
	}

	public static void verify(LicenseManager licenseManager, String productId)
		throws Exception {

		int productLicenseState = licenseManager.getLicenseState(productId);

		if (productLicenseState != LicenseManager.STATE_GOOD) {
			licenseManager.checkLicense(productId);

			productLicenseState = licenseManager.getLicenseState(productId);
		}

		if (productLicenseState != LicenseManager.STATE_GOOD) {
			throw new Exception("Unable to find a valid license");
		}

		Map<String, String> portalLicenseProperties =
			licenseManager.getLicenseProperties("Portal");

		String portalLicenseType = portalLicenseProperties.get("type");

		if (portalLicenseType == null) {
			portalLicenseType = "production";
		}

		Map<String, String> appLicenseProperties =
			licenseManager.getLicenseProperties(productId);

		String appLicenseType = appLicenseProperties.get("type");

		if (Objects.equals(appLicenseType, portalLicenseType)) {
			if (appLicenseType.equals("trial") &&
				!Objects.equals(
					appLicenseProperties.get("lifetime"),
					portalLicenseProperties.get("lifetime"))) {

				throw new Exception(
					"Trial license lifetime is not same as Liferay DXP");
			}

			return;
		}

		if (appLicenseType.startsWith("developer")) {
			throw new Exception(
				"Developer license is not allowed because Liferay DXP " +
					"license is not a developer license");
		}

		if (portalLicenseType.startsWith("developer")) {
			throw new Exception(
				"Developer license is required because Liferay DXP license " +
					"is a developer license");
		}
	}

	private static final String _PRODUCT_ID_PORTAL = "Portal";

}