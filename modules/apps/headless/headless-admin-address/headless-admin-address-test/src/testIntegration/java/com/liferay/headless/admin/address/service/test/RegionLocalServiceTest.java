/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.address.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.portal.kernel.exception.NoSuchRegionException;
import com.liferay.portal.kernel.lazy.referencing.LazyReferencingThreadLocal;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.RegionLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Balazs Breier
 */
@RunWith(Arquillian.class)
public class RegionLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_country = _countryLocalService.addCountry(
			"a1", "a11", true, false, null, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), 0D, false, false, false,
			ServiceContextTestUtil.getServiceContext());
	}

	@After
	public void tearDown() throws Exception {
		if (_country != null) {
			_countryLocalService.deleteCountry(_country);
		}
	}

	@Test
	public void testGetOrAddEmptyRegion() throws Exception {

		// Lazy referencing disabled

		try {
			_regionLocalService.getOrAddEmptyRegion(
				RandomTestUtil.randomString(), TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), _country.getCountryId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

			Assert.fail();
		}
		catch (NoSuchRegionException noSuchRegionException) {
			Assert.assertNotNull(noSuchRegionException);
		}

		// Lazy referencing enabled

		try (SafeCloseable safeCloseable =
				LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) {

			String externalReferenceCode = RandomTestUtil.randomString();

			Region region = _regionLocalService.getOrAddEmptyRegion(
				externalReferenceCode, TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), _country.getCountryId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

			Assert.assertEquals(
				externalReferenceCode, region.getExternalReferenceCode());
			Assert.assertEquals(
				region,
				_regionLocalService.fetchRegionByExternalReferenceCode(
					externalReferenceCode, TestPropsValues.getCompanyId()));
		}
	}

	@Test
	public void testGetOrAddEmptyRegionRegionCodeCollisionFallback()
		throws Exception {

		Region existingRegion = _regionLocalService.addRegion(
			_country.getCountryId(), true, RandomTestUtil.randomString(), 0D,
			"R1", ServiceContextTestUtil.getServiceContext());

		try (SafeCloseable safeCloseable =
				LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) {

			String externalReferenceCode = RandomTestUtil.randomString();

			Region region = _regionLocalService.getOrAddEmptyRegion(
				externalReferenceCode, TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), _country.getCountryId(),
				existingRegion.getRegionCode(), RandomTestUtil.randomString());

			Assert.assertNotEquals(
				existingRegion.getRegionCode(), region.getRegionCode());
			Assert.assertEquals(
				externalReferenceCode, region.getExternalReferenceCode());
		}
	}

	@Test
	public void testUpdateRegionWithLazyReferencingEnabled() throws Exception {
		try (SafeCloseable safeCloseable =
				LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) {

			String externalReferenceCode = RandomTestUtil.randomString();

			Region region = _regionLocalService.getOrAddEmptyRegion(
				externalReferenceCode, TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), _country.getCountryId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString());

			String name = RandomTestUtil.randomString();
			String regionCode = RandomTestUtil.randomString();

			region = _regionLocalService.updateRegion(
				region.getRegionId(), true, name, 0D, regionCode);

			Assert.assertEquals(name, region.getName());
			Assert.assertEquals(regionCode, region.getRegionCode());
		}
	}

	private Country _country;

	@Inject
	private CountryLocalService _countryLocalService;

	@Inject
	private RegionLocalService _regionLocalService;

}