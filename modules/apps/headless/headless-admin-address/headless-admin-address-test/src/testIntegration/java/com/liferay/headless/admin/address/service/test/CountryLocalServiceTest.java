/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.address.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.portal.kernel.exception.NoSuchCountryException;
import com.liferay.portal.kernel.lazy.referencing.LazyReferencingThreadLocal;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.service.CountryLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Balazs Breier
 */
@RunWith(Arquillian.class)
public class CountryLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetOrAddEmptyCountry() throws Exception {

		// Lazy referencing disabled

		try {
			_countryLocalService.getOrAddEmptyCountry(
				RandomTestUtil.randomString(), TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), RandomTestUtil.randomString());

			Assert.fail();
		}
		catch (NoSuchCountryException noSuchCountryException) {
			Assert.assertNotNull(noSuchCountryException);
		}

		// Lazy referencing enabled

		try (SafeCloseable safeCloseable =
				LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) {

			String externalReferenceCode = RandomTestUtil.randomString();

			Country country = _countryLocalService.getOrAddEmptyCountry(
				externalReferenceCode, TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), RandomTestUtil.randomString());

			Assert.assertEquals(
				externalReferenceCode, country.getExternalReferenceCode());
			Assert.assertEquals(
				country,
				_countryLocalService.fetchCountryByExternalReferenceCode(
					externalReferenceCode, TestPropsValues.getCompanyId()));

			_deleteCountry(country);
		}
	}

	@Test
	public void testUpdateCountryWithLazyReferencingEnabled() throws Exception {
		try (SafeCloseable safeCloseable =
				LazyReferencingThreadLocal.setEnabledWithSafeCloseable(true)) {

			String externalReferenceCode = RandomTestUtil.randomString();

			Country country = _countryLocalService.getOrAddEmptyCountry(
				externalReferenceCode, TestPropsValues.getCompanyId(),
				TestPropsValues.getUserId(), RandomTestUtil.randomString());

			String name = RandomTestUtil.randomString();
			String number = String.valueOf(RandomTestUtil.nextInt());

			country = _countryLocalService.updateCountry(
				country.getCountryId(), country.getA2(), country.getA3(), true,
				false, null, name, number, 0D, false, false);

			Assert.assertEquals(name, country.getName());
			Assert.assertEquals(number, country.getNumber());

			_deleteCountry(country);
		}
	}

	private void _deleteCountry(Country country) throws Exception {
		_countryLocalService.deleteCountry(country);
	}

	@Inject
	private CountryLocalService _countryLocalService;

	@SuppressWarnings("unused")
	private ServiceContext _serviceContext;

}