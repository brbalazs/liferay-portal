/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_0;

import com.liferay.osb.asah.common.dog.AsahMarkerDog;
import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class AsahMarkerUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		AsahMarker asahMarker = _asahMarkerDog.fetchAsahMarker(
			"DXPEntityNanite");

		if (asahMarker == null) {
			return;
		}

		_asahMarkerDog.deleteAsahMarker("DXPEntitiesNanite");

		asahMarker.setId("DXPEntitiesNanite");
		asahMarker.setIsNew(Boolean.TRUE);

		_asahMarkerDog.addAsahMarker(asahMarker);

		_asahMarkerDog.deleteAsahMarker("DXPEntityNanite");

		if (_log.isInfoEnabled()) {
			_log.info("Asah Marker has successfully upgraded");
		}
	}

	private static final Log _log = LogFactory.getLog(
		AsahMarkerUpgradeStep.class);

	@Autowired
	private AsahMarkerDog _asahMarkerDog;

}