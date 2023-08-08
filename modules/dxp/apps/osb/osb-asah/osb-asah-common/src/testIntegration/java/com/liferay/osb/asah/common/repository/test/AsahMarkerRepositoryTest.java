/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.AsahMarkerRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Marcellus Tavares
 */
@Import(JDBCTestConfiguration.class)
public class AsahMarkerRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		_asahMarker1 = _asahMarkerRepository.save(
			new AsahMarker("SessionNanite"));
		_asahMarker2 = _asahMarkerRepository.save(
			new AsahMarker("Upgrade", JSONUtil.put("dataSourceId", "1")));
	}

	@Test
	public void testFindById() {
		String asahMarkerId = _asahMarker1.getId();

		Assertions.assertNotNull(asahMarkerId);

		Assertions.assertEquals(
			Optional.of(_asahMarker1),
			_asahMarkerRepository.findById(asahMarkerId));
	}

	private AsahMarker _asahMarker1;
	private AsahMarker _asahMarker2;

	@Autowired
	private AsahMarkerRepository _asahMarkerRepository;

}