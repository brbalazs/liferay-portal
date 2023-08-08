/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.constants.DataConstants;
import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class GeolocationTest extends BaseBeanTestCase<Geolocation> {

	@Test
	public void testCountry() {
		Geolocation geolocation = Geolocation.country(DataConstants.ANY);

		Assertions.assertEquals(Geolocation.any(), geolocation);
	}

	@Test
	public void testRegion1() {
		Geolocation geolocation = Geolocation.region(
			DataConstants.ANY, DataConstants.ANY);

		Assertions.assertEquals(Geolocation.any(), geolocation);
	}

	@Test
	public void testRegion2() {
		Geolocation geolocation = Geolocation.region(null, null);

		Assertions.assertEquals(Geolocation.any(), geolocation);
	}

	@Override
	protected Geolocation newInstance() {
		return Geolocation.any();
	}

}