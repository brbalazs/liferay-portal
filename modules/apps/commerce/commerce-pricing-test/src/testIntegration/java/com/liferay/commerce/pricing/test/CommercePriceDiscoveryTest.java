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

package com.liferay.commerce.pricing.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.discount.CommerceDiscountLevel;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.price.CommercePriceDiscovery;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.commerce.price.list.constants.CommercePriceListTypeKeys;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommercePriceListLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalServiceUtil;
import com.liferay.commerce.pricing.test.util.CommerceDiscountTestUtil;
import com.liferay.commerce.pricing.test.util.CommercePriceModifierTestUtil;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.TestCommerceContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Calendar;
import java.util.List;

import org.frutilla.FrutillaRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Riccardo Alberti
 */
@RunWith(Arquillian.class)
public class CommercePriceDiscoveryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addUser(_company);

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_user.getCompanyId(), _user.getGroupId(), _user.getUserId());

		_commerceAccount =
			_commerceAccountLocalService.getPersonalCommerceAccount(
				_user.getUserId());

		_commerceChannel = CommerceTestUtil.addCommerceChannel();
	}

	@Test
	public void testBulkTierPriceEntryNoPromoNoDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when no promotion nor " +
				"discounts are defined"
		).given(
			"A catalog with a product and a price list with a bulk tier " +
				"price entry with the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false, null,
			null, null, null, true, true);

		BigDecimal price5 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price5, 5, true,
			false, null, null, null, null, true, true);

		BigDecimal price10 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price10, 10, true,
			false, null, null, null, null, true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValues = _commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 100, _commerceCurrency,
			commerceContext);

		commercePriceValue = commercePriceValues.get(0);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price10.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));
	}

	@Test
	public void testPriceEntryNoPromoNoDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when no promotion nor " +
				"discounts are defined"
		).given(
			"A catalog with a product and a price list with a price entry " +
				"with the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false, null,
			null, null, null, true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		Assert.assertEquals(price, finalPriceMoney.getPrice());
	}

	@Test
	public void testPriceEntryWithDiscountsNoDiscovery() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when price entry level " +
				"discounts are defined"
		).given(
			"A catalog with a product and a price list with a price entry " +
				"with the product with discovery flag = false"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned and the discounts taken from the " +
				"price entry"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false,
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		Assert.assertEquals(price, finalPriceMoney.getPrice());

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePriceValue.getDiscountLevels();

		Assert.assertEquals(
			commercePriceEntry.getDiscountLevel1(),
			commerceDiscountLevels[0].getDiscountValue());
		Assert.assertEquals(
			commercePriceEntry.getDiscountLevel2(),
			commerceDiscountLevels[1].getDiscountValue());
		Assert.assertEquals(
			commercePriceEntry.getDiscountLevel3(),
			commerceDiscountLevels[2].getDiscountValue());
		Assert.assertEquals(
			commercePriceEntry.getDiscountLevel4(),
			commerceDiscountLevels[3].getDiscountValue());
	}

	@Test
	public void testPriceEntryWithDiscountsWithDiscovery() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when price entry level " +
				"discounts are defined and no system discounts are defined"
		).given(
			"A catalog with a product and a price list with a price entry " +
				"with the product with discovery flag = true"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned and the discounts shall be empty"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, true,
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		Assert.assertEquals(price, finalPriceMoney.getPrice());

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePriceValue.getDiscountLevels();

		Assert.assertNull(commerceDiscountLevels[0]);
		Assert.assertNull(commerceDiscountLevels[1]);
		Assert.assertNull(commerceDiscountLevels[2]);
		Assert.assertNull(commerceDiscountLevels[3]);
	}

	@Test
	public void testPriceEntryWithPromoNoDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price and the promo price of a product is retrieved " +
				"when no discounts are defined"
		).given(
			"A catalog with a product a price list with a price entry with " +
				"the product and a promo on the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price and the promo is returned "
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CommercePriceList commercePromotion = _addPromotion(
			catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());
		BigDecimal promoPrice = BigDecimal.valueOf(
			RandomTestUtil.randomDouble());

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false, null,
			null, null, null, true, true);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePromotion.getCommercePriceListId(), "", promoPrice, false,
			null, null, null, null, true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		List<CommercePriceValue> commercePromoValues =
			_commercePriceDiscovery.getCommercePromoPriceValue(
				commercePromotion.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, commercePriceValues,
				_commerceCurrency, commerceContext);

		CommercePriceValue commercePromoValue = commercePromoValues.get(0);

		CommerceMoney finalPromoMoney = commercePromoValue.getCommerceMoney();

		BigDecimal finalPromoPrice = finalPromoMoney.getPrice();

		Assert.assertEquals(
			promoPrice.setScale(_SCALE, RoundingMode.FLOOR),
			finalPromoPrice.setScale(_SCALE, RoundingMode.FLOOR));
	}

	@Test
	public void testPriceEntryWithSystemDiscountsWithDiscovery()
		throws Exception {

		frutillaRule.scenario(
			"The unit price of a product is retrieved when price entry level " +
				"discounts are defined and system discount are defined"
		).given(
			"A catalog with a product and a price list with a price entry " +
				"with the product with discovery flag = true"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned and the discounts shall contain " +
				"an entry for each found system discount"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, true,
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceDiscount commerceDiscount1 =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				_group.getGroupId(), RandomTestUtil.randomDouble(),
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		CommerceDiscount commerceDiscount2 =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				_group.getGroupId(),
				BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				CommerceDiscountConstants.LEVEL2,
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePriceValue.getDiscountLevels();

		Assert.assertEquals(false, commerceDiscountLevels[0].isUsePercentage());

		BigDecimal expectedDiscountLevel1 = commerceDiscount1.getLevel1();

		BigDecimal expectedDiscountLevel2 = commerceDiscount2.getLevel2();

		BigDecimal discoveredDiscount1 =
			commerceDiscountLevels[0].getDiscountValue();

		BigDecimal discoveredDiscount2 =
			commerceDiscountLevels[1].getDiscountValue();

		Assert.assertEquals(
			expectedDiscountLevel1.setScale(_SCALE, RoundingMode.FLOOR),
			discoveredDiscount1.setScale(_SCALE, RoundingMode.FLOOR));

		Assert.assertEquals(true, commerceDiscountLevels[1].isUsePercentage());

		Assert.assertEquals(
			expectedDiscountLevel2.setScale(_SCALE, RoundingMode.FLOOR),
			discoveredDiscount2.setScale(_SCALE, RoundingMode.FLOOR));

		Assert.assertNull(commerceDiscountLevels[2]);
		Assert.assertNull(commerceDiscountLevels[3]);
	}

	@Test
	public void testTierPriceEntryEntryAndSystemDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when discounts are " +
				"both on price entry level and system discounts are defined"
		).given(
			"A catalog with a product and a price list with a tier price " +
				"entry with the product with entry discounts on one tier"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price and discounts is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, true,
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		BigDecimal price5 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price5, 5, false,
			true, BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		BigDecimal price10 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommerceTierPriceEntry commerceTierPriceEntry2 =
			_addCommerceTierPriceEntry(
				commercePriceEntry.getCommercePriceEntryId(), "", price10, 10,
				false, false, BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceDiscount commerceDiscount1 =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				_group.getGroupId(), RandomTestUtil.randomDouble(),
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePriceValue.getDiscountLevels();

		BigDecimal discountLevel = commerceDiscount1.getLevel1();

		BigDecimal discoveredDiscountLevel =
			commerceDiscountLevels[0].getDiscountValue();

		Assert.assertEquals(
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR),
			discoveredDiscountLevel.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValues = _commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 100, _commerceCurrency,
			commerceContext);

		commercePriceValue = commercePriceValues.get(0);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValue = commercePriceValues.get(1);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price5.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePriceValue.getDiscountLevels();

		discountLevel = commerceDiscount1.getLevel1();

		discoveredDiscountLevel = commerceDiscountLevels[0].getDiscountValue();

		Assert.assertEquals(
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR),
			discoveredDiscountLevel.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValue = commercePriceValues.get(2);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price10.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePriceValue.getDiscountLevels();

		_checkDiscountLevels(
			commerceTierPriceEntry2.getDiscountLevel1(),
			commerceTierPriceEntry2.getDiscountLevel2(),
			commerceTierPriceEntry2.getDiscountLevel3(),
			commerceTierPriceEntry2.getDiscountLevel4(),
			commerceDiscountLevels);
	}

	@Test
	public void testTierPriceEntryNoPromoEntryDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when no promotion nor " +
				"discounts are defined"
		).given(
			"A catalog with a product and a price list with a tier price " +
				"entry with the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false,
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		BigDecimal price5 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommerceTierPriceEntry commerceTierPriceEntry1 =
			_addCommerceTierPriceEntry(
				commercePriceEntry.getCommercePriceEntryId(), "", price5, 5,
				false, false, BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		BigDecimal price10 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommerceTierPriceEntry commerceTierPriceEntry2 =
			_addCommerceTierPriceEntry(
				commercePriceEntry.getCommercePriceEntryId(), "", price10, 10,
				false, false, BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePriceValue.getDiscountLevels();

		_checkDiscountLevels(
			commercePriceEntry.getDiscountLevel1(),
			commercePriceEntry.getDiscountLevel2(),
			commercePriceEntry.getDiscountLevel3(),
			commercePriceEntry.getDiscountLevel4(), commerceDiscountLevels);

		commercePriceValues = _commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 100, _commerceCurrency,
			commerceContext);

		commercePriceValue = commercePriceValues.get(0);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValue = commercePriceValues.get(1);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price5.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePriceValue.getDiscountLevels();

		_checkDiscountLevels(
			commerceTierPriceEntry1.getDiscountLevel1(),
			commerceTierPriceEntry1.getDiscountLevel2(),
			commerceTierPriceEntry1.getDiscountLevel3(),
			commerceTierPriceEntry1.getDiscountLevel4(),
			commerceDiscountLevels);

		commercePriceValue = commercePriceValues.get(2);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price10.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePriceValue.getDiscountLevels();

		_checkDiscountLevels(
			commerceTierPriceEntry2.getDiscountLevel1(),
			commerceTierPriceEntry2.getDiscountLevel2(),
			commerceTierPriceEntry2.getDiscountLevel3(),
			commerceTierPriceEntry2.getDiscountLevel4(),
			commerceDiscountLevels);
	}

	@Test
	public void testTierPriceEntryNoPromoNoDiscounts() throws Exception {
		frutillaRule.scenario(
			"The unit price of a product is retrieved when no promotion nor " +
				"discounts are defined"
		).given(
			"A catalog with a product and a price list with a tier price " +
				"entry with the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false, null,
			null, null, null, true, true);

		BigDecimal price5 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price5, 5, false,
			false, null, null, null, null, true, true);

		BigDecimal price10 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price10, 10,
			false, false, null, null, null, null, true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValues = _commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 100, _commerceCurrency,
			commerceContext);

		commercePriceValue = commercePriceValues.get(0);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValue = commercePriceValues.get(1);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price5.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePriceValue = commercePriceValues.get(2);

		finalPriceMoney = commercePriceValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price10.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));
	}

	@Test
	public void testTierPriceEntryWithPromo() throws Exception {
		frutillaRule.scenario(
			"The unit price and the promo price of a product is retrieved " +
				"when no discounts are defined"
		).given(
			"A catalog with a product a price list with a price entry with " +
				"the product and a promo on the product"
		).when(
			"The price of the product is discovered"
		).then(
			"The correct price and the promo is returned "
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CommercePriceList commercePromotion = _addPromotion(
			catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());
		BigDecimal promoPrice = BigDecimal.valueOf(
			RandomTestUtil.randomDouble());

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false, null,
			null, null, null, true, true);

		CommercePriceEntry commercePromoEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePromotion.getCommercePriceListId(), "", promoPrice, false,
			null, null, null, null, true, true);

		BigDecimal price5 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		_addCommerceTierPriceEntry(
			commercePromoEntry.getCommercePriceEntryId(), "", price5, 5, false,
			true, BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()),
			BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		BigDecimal price10 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommerceTierPriceEntry commerceTierPriceEntry2 =
			_addCommerceTierPriceEntry(
				commercePromoEntry.getCommercePriceEntryId(), "", price10, 10,
				false, false, BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()),
				BigDecimal.valueOf(RandomTestUtil.randomInt()), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney finalPriceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		List<CommercePriceValue> commercePromoValues =
			_commercePriceDiscovery.getCommercePromoPriceValue(
				commercePromotion.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 100, commercePriceValues,
				_commerceCurrency, commerceContext);

		CommercePriceValue commercePromoValue = commercePromoValues.get(0);

		CommerceDiscountLevel[] commerceDiscountLevels =
			commercePromoValue.getDiscountLevels();

		Assert.assertNull(commerceDiscountLevels[0].getDiscountValue());
		Assert.assertNull(commerceDiscountLevels[1].getDiscountValue());
		Assert.assertNull(commerceDiscountLevels[2].getDiscountValue());
		Assert.assertNull(commerceDiscountLevels[3].getDiscountValue());

		finalPriceMoney = commercePromoValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			promoPrice.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commercePromoValue = commercePromoValues.get(1);

		finalPriceMoney = commercePromoValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price5.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePromoValue.getDiscountLevels();

		Assert.assertNull(commerceDiscountLevels[0]);
		Assert.assertNull(commerceDiscountLevels[1]);
		Assert.assertNull(commerceDiscountLevels[2]);
		Assert.assertNull(commerceDiscountLevels[3]);

		commercePromoValue = commercePromoValues.get(2);

		finalPriceMoney = commercePromoValue.getCommerceMoney();

		finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			price10.setScale(_SCALE, RoundingMode.FLOOR),
			finalPrice.setScale(_SCALE, RoundingMode.FLOOR));

		commerceDiscountLevels = commercePromoValue.getDiscountLevels();

		_checkDiscountLevels(
			commerceTierPriceEntry2.getDiscountLevel1(),
			commerceTierPriceEntry2.getDiscountLevel2(),
			commerceTierPriceEntry2.getDiscountLevel3(),
			commerceTierPriceEntry2.getDiscountLevel4(),
			commerceDiscountLevels);
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	private static CommercePriceList _addPromotion(
			long groupId, double priority)
		throws Exception {

		CommerceCurrency commerceCurrency =
			CommerceCurrencyTestUtil.addCommerceCurrency(groupId);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(groupId);

		User user = UserLocalServiceUtil.getDefaultUser(
			serviceContext.getCompanyId());

		Calendar calendar = CalendarFactoryUtil.getCalendar(user.getTimeZone());

		serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);

		return CommercePriceListLocalServiceUtil.addCommercePriceList(
			groupId, user.getUserId(), commerceCurrency.getCommerceCurrencyId(),
			CommercePriceListTypeKeys.TYPE_PROMOTION,
			RandomTestUtil.randomString(), priority,
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR),
			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
			true, serviceContext);
	}

	private CommercePriceEntry _addCommercePriceEntry(
			long cpProductId, String cpInstanceUuid, long commercePriceListId,
			String externalReferenceCode, BigDecimal price,
			boolean discountDiscovery, BigDecimal discountLevel1,
			BigDecimal discountLevel2, BigDecimal discountLevel3,
			BigDecimal discountLevel4, boolean publish, boolean neverExpire)
		throws PortalException {

		CommercePriceList commercePriceList =
			CommercePriceListLocalServiceUtil.getCommercePriceList(
				commercePriceListId);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				commercePriceList.getGroupId());

		Calendar calendar = CalendarFactoryUtil.getCalendar(
			_user.getTimeZone());

		if (publish) {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}

		return CommercePriceEntryLocalServiceUtil.addCommercePriceEntry(
			cpProductId, cpInstanceUuid, commercePriceListId,
			externalReferenceCode, price, discountDiscovery, discountLevel1,
			discountLevel2, discountLevel3, discountLevel4,
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR),
			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
			neverExpire, serviceContext);
	}

	private CommerceTierPriceEntry _addCommerceTierPriceEntry(
			long commercePriceEntryId, String externalReferenceCode,
			BigDecimal price, int minQuantity, boolean bulkPricing,
			boolean discountDiscovery, BigDecimal discountLevel1,
			BigDecimal discountLevel2, BigDecimal discountLevel3,
			BigDecimal discountLevel4, boolean publish, boolean neverExpire)
		throws PortalException {

		CommercePriceEntry commercePriceEntry =
			CommercePriceEntryLocalServiceUtil.getCommercePriceEntry(
				commercePriceEntryId);

		CommercePriceList commercePriceList =
			commercePriceEntry.getCommercePriceList();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				commercePriceList.getGroupId());

		Calendar calendar = CalendarFactoryUtil.getCalendar(
			_user.getTimeZone());

		if (publish) {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}

		return CommerceTierPriceEntryLocalServiceUtil.addCommerceTierPriceEntry(
			commercePriceEntryId, externalReferenceCode, price, minQuantity,
			bulkPricing, discountDiscovery, discountLevel1, discountLevel2,
			discountLevel3, discountLevel4, calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR),
			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), neverExpire, serviceContext);
	}

	private void _checkDiscountLevels(
		BigDecimal discountLevel1, BigDecimal discountLevel2,
		BigDecimal discountLevel3, BigDecimal discountLevel4,
		CommerceDiscountLevel[] commerceDiscountLevels) {

		BigDecimal discountLevel = commerceDiscountLevels[0].getDiscountValue();

		Assert.assertEquals(
			discountLevel1.setScale(_SCALE, RoundingMode.FLOOR),
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR));

		discountLevel = commerceDiscountLevels[1].getDiscountValue();

		Assert.assertEquals(
			discountLevel2.setScale(_SCALE, RoundingMode.FLOOR),
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR));

		discountLevel = commerceDiscountLevels[2].getDiscountValue();

		Assert.assertEquals(
			discountLevel3.setScale(_SCALE, RoundingMode.FLOOR),
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR));

		discountLevel = commerceDiscountLevels[3].getDiscountValue();

		Assert.assertEquals(
			discountLevel4.setScale(_SCALE, RoundingMode.FLOOR),
			discountLevel.setScale(_SCALE, RoundingMode.FLOOR));
	}

	private static final int _SCALE = 10;

	private CommerceAccount _commerceAccount;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@DeleteAfterTestRun
	private CommerceChannel _commerceChannel;

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommercePriceDiscovery _commercePriceDiscovery;

	@DeleteAfterTestRun
	private Company _company;

	@DeleteAfterTestRun
	private Group _group;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}