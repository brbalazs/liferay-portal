/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.constants.DataConstants;
import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;
import com.liferay.petra.string.StringPool;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class TechnologyTest extends BaseBeanTestCase<Technology> {

	public TechnologyTest() {
		super(null, Arrays.asList("isMobile"));
	}

	@Test
	public void testBrowserName1() {
		Technology technology = Technology.browserName(DataConstants.ANY);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testBrowserName2() {
		Technology technology = Technology.browserName(StringPool.BLANK);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testBrowserName3() {
		Technology technology = Technology.browserName(null);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testDeviceType1() {
		Technology technology = Technology.deviceType(DataConstants.ANY);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testDeviceType2() {
		Technology technology = Technology.deviceType(StringPool.BLANK);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testDeviceType3() {
		Technology technology = Technology.deviceType(null);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testIsMobile1() {
		Technology technology = Technology.deviceType(DataConstants.ANY);

		Assertions.assertFalse(technology.isMobile());
	}

	@Test
	public void testIsMobile2() {
		Technology technology = Technology.deviceType(
			DataConstants.DEVICE_TYPE_MOBILE);

		Assertions.assertTrue(technology.isMobile());
	}

	@Test
	public void testIsMobile3() {
		Technology technology = Technology.deviceType(
			DataConstants.DEVICE_TYPE_SMART_PHONE);

		Assertions.assertTrue(technology.isMobile());
	}

	@Test
	public void testIsMobile4() {
		Technology technology = Technology.deviceType(
			DataConstants.DEVICE_TYPE_TABLET);

		Assertions.assertTrue(technology.isMobile());
	}

	@Test
	public void testPlatformName1() {
		Technology technology = Technology.platformName(DataConstants.ANY);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testPlatformName2() {
		Technology technology = Technology.platformName(StringPool.BLANK);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Test
	public void testPlatformName3() {
		Technology technology = Technology.platformName(null);

		Assertions.assertEquals(Technology.any(), technology);
	}

	@Override
	protected Technology newInstance() {
		return Technology.any();
	}

}