/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_0.test;

import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.AsahMarkerRepository;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.upgrade.OSBAsahUpgradeSpringTestContext;
import com.liferay.osb.asah.upgrade.v4_3_0.AsahMarkerUpgradeStep;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class AsahMarkerUpgradeStepTest
	implements OSBAsahTestExecutionListenersContext,
			   OSBAsahUpgradeSpringTestContext {

	@SQLResource(resourcePath = "asahmarker.sql")
	@Test
	public void testUpgrade() {
		_asahMarkerUpgradeStep.upgrade(null);

		Assertions.assertFalse(
			_asahMarkerRepository.existsById("DXPEntityNanite"));

		Optional<AsahMarker> asahMarkerOptional =
			_asahMarkerRepository.findById("DXPEntitiesNanite");

		AsahMarker asahMarker = asahMarkerOptional.orElse(null);

		Assertions.assertNotNull(asahMarker);

		JSONAssert.assertEquals(
			JSONUtil.put(
				"lastSuccessfulDate", "2023-10-12T23:12:11.026Z"
			).put(
				"type", "nanite"
			),
			asahMarker.getContextJSONObject(), false);
	}

	@Autowired
	private AsahMarkerRepository _asahMarkerRepository;

	@Autowired
	private AsahMarkerUpgradeStep _asahMarkerUpgradeStep;

}