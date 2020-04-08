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
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.test.util.CommerceDiscountTestUtil;
import com.liferay.commerce.price.CommerceOptionValue;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.test.util.CommercePriceEntryTestUtil;
import com.liferay.commerce.price.list.test.util.CommercePriceListTestUtil;
import com.liferay.commerce.pricing.test.util.TestCommerceOptionValue;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.test.util.TestCommerceContext;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.frutilla.FrutillaRule;

import org.junit.After;
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
public class CommerceProductBundlesTest {

	@ClassRule
	@Rule
	public static AggregateTestRule aggregateTestRule = new AggregateTestRule(
		new LiferayIntegrationTestRule(), PermissionCheckerTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_user = UserTestUtil.addUser();

		_group = GroupTestUtil.addGroup();

		_commerceAccount =
			_commerceAccountLocalService.getPersonalCommerceAccount(
				_user.getUserId());

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency(
			_group.getCompanyId());

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_user.getCompanyId(), _user.getGroupId(), _user.getUserId());
	}

	@After
	public void tearDown() throws Exception {
		_commerceAccountLocalService.deleteCommerceAccount(
			_commerceAccount.getCommerceAccountId());
	}

	@Test
	public void testCalculatePriceDynamicOptionSKU() throws Exception {
		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values linked to SKUs with price type " +
				"dynamic"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);

		cpInstance2.setPrice(cpInstancePrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(cpInstancePrice1);

		expectedPrice = expectedPrice.add(cpInstancePrice2);

		expectedPrice = expectedPrice.add(cpInstancePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceDynamicOptionSKUWithPromo() throws Exception {
		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values linked to SKUs with price type " +
				"dynamic"
		).and(
			"Some linked SKUs have a promo price"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);
		BigDecimal cpInstancePromoPrice2 = BigDecimal.valueOf(100);

		cpInstance2.setPrice(cpInstancePrice2);
		cpInstance2.setPromoPrice(cpInstancePromoPrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(cpInstancePrice1);

		expectedPrice = expectedPrice.add(cpInstancePromoPrice2);

		expectedPrice = expectedPrice.add(cpInstancePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceDynamicOptionSKUWithQuantities()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values linked to SKUs with price type " +
				"dynamic"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);

		cpInstance2.setPrice(cpInstancePrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		int quantity1 = 1;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC,
				quantity1));

		int quantity2 = 3;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC,
				quantity2));

		int quantity3 = 10;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC,
				quantity3));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(
			cpInstancePrice1.multiply(BigDecimal.valueOf(quantity1)));

		expectedPrice = expectedPrice.add(
			cpInstancePrice2.multiply(BigDecimal.valueOf(quantity2)));

		expectedPrice = expectedPrice.add(
			cpInstancePrice3.multiply(BigDecimal.valueOf(quantity3)));

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceMixedOptionSKUWithDiscount()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values some linked to SKU and some with " +
				"price type dynamic"
		).and(
			"The product has a discount applied on it"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		double discountAmount = 10;

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommerceDiscountTestUtil.addFixedCommerceDiscount(
			_group.getGroupId(), discountAmount,
			CommerceDiscountConstants.TARGET_PRODUCT,
			cpDefinition.getCPDefinitionId());

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);
		BigDecimal cpInstancePromoPrice2 = BigDecimal.valueOf(100);

		cpInstance2.setPrice(cpInstancePrice2);
		cpInstance2.setPromoPrice(cpInstancePromoPrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice3, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.subtract(
			BigDecimal.valueOf(discountAmount));

		expectedPrice = expectedPrice.add(cpInstancePromoPrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceMixedOptionSKUWithOptionDiscount()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values some linked to SKU and some with " +
				"price type dynamic"
		).and(
			"Some linked SKUs have discount applied on"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);
		BigDecimal cpInstancePromoPrice2 = BigDecimal.valueOf(100);

		cpInstance2.setPrice(cpInstancePrice2);
		cpInstance2.setPromoPrice(cpInstancePromoPrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPDefinition cpDefinition = cpInstance2.getCPDefinition();

		double discountAmount = 10;

		CommerceDiscountTestUtil.addFixedCommerceDiscount(
			_group.getGroupId(), discountAmount,
			CommerceDiscountConstants.TARGET_PRODUCT,
			cpDefinition.getCPDefinitionId());

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice3, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(cpInstancePromoPrice2);

		expectedPrice = expectedPrice.subtract(
			BigDecimal.valueOf(discountAmount));

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceMixedOptionSKUWithPriceList()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values some linked to SKU and some with " +
				"price type dynamic"
		).and(
			"Some linked SKUs have their price defined in a price list"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance1.getCPDefinition();

		CProduct cProduct = cpDefinition.getCProduct();

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(300);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		BigDecimal cpInstancePriceEntryPrice1 = BigDecimal.valueOf(100);

		CommercePriceEntryTestUtil.addCommercePriceEntry(
			cProduct.getCProductId(), cpInstance1.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "",
			cpInstancePriceEntryPrice1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);
		BigDecimal cpInstancePromoPrice2 = BigDecimal.valueOf(100);

		cpInstance2.setPrice(cpInstancePrice2);
		cpInstance2.setPromoPrice(cpInstancePromoPrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice3, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(
			cpInstancePriceEntryPrice1);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceMixedOptionSKUWithPromo() throws Exception {
		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values some linked to SKU and some with " +
				"price type dynamic"
		).and(
			"Some linked SKUs have a promo price"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);
		BigDecimal cpInstancePromoPrice2 = BigDecimal.valueOf(100);

		cpInstance2.setPrice(cpInstancePrice2);
		cpInstance2.setPromoPrice(cpInstancePromoPrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				null, CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC, 1));

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice3, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(cpInstancePromoPrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceStaticOptionNoSKU() throws Exception {
		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values not linked to SKUs with price " +
				"type static"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		BigDecimal optionValuePrice1 = BigDecimal.valueOf(10);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice1,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		BigDecimal optionValuePrice2 = BigDecimal.valueOf(15);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice2,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice3,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, 1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(optionValuePrice1);

		expectedPrice = expectedPrice.add(optionValuePrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceStaticOptionNoSKUWithQuantities()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values not linked to SKUs with price " +
				"type static"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		BigDecimal optionValuePrice1 = BigDecimal.valueOf(10);

		int quantity1 = 1;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice1,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, quantity1));

		BigDecimal optionValuePrice2 = BigDecimal.valueOf(15);

		int quantity2 = 11;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice2,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, quantity2));

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		int quantity3 = 10;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice3,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, quantity3));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(optionValuePrice1);

		expectedPrice = expectedPrice.add(optionValuePrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceStaticOptionSKU() throws Exception {
		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values linked to SKUs with price type " +
				"static"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned given the quantity"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance1.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice2 = BigDecimal.valueOf(150);

		cpInstance2.setPrice(cpInstancePrice2);

		_cpInstanceLocalService.updateCPInstance(cpInstance2);

		CPInstance cpInstance3 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice3 = BigDecimal.valueOf(200);

		cpInstance3.setPrice(cpInstancePrice3);

		_cpInstanceLocalService.updateCPInstance(cpInstance3);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		BigDecimal optionValuePrice1 = BigDecimal.valueOf(10);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice1, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		BigDecimal optionValuePrice2 = BigDecimal.valueOf(15);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance2.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice2, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance3.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice3, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				1));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(optionValuePrice1);

		expectedPrice = expectedPrice.add(optionValuePrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Test
	public void testCalculatePriceStaticOptionWithSKUWithQuantities()
		throws Exception {

		frutillaRule.scenario(
			"The price of a product with 3 option values selected is calculated"
		).given(
			"A product with 3 option values with price type static"
		).and(
			"an option value linked to a cpInstance"
		).when(
			"The price of the product is calculated"
		).then(
			"The correct price is returned and the quantity of the linked " +
				"option is taken into account"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice = BigDecimal.valueOf(35);

		cpInstance.setPrice(cpInstancePrice);

		_cpInstanceLocalService.updateCPInstance(cpInstance);

		List<CommerceOptionValue> commerceOptionValues = new ArrayList<>();

		BigDecimal optionValuePrice1 = BigDecimal.valueOf(10);

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		BigDecimal cpInstancePrice1 = BigDecimal.valueOf(100);

		cpInstance.setPrice(cpInstancePrice1);

		_cpInstanceLocalService.updateCPInstance(cpInstance1);

		int quantity1 = 10;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				cpInstance1.getCPInstanceId(), RandomTestUtil.randomString(),
				optionValuePrice1, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				quantity1));

		BigDecimal optionValuePrice2 = BigDecimal.valueOf(15);

		int quantity2 = 11;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice2,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, quantity2));

		BigDecimal optionValuePrice3 = BigDecimal.valueOf(20);

		int quantity3 = 10;

		commerceOptionValues.add(
			new TestCommerceOptionValue(
				0, RandomTestUtil.randomString(), optionValuePrice3,
				CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC, quantity3));

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 1, true, commerceContext,
				commerceOptionValues);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		BigDecimal expectedPrice = cpInstancePrice.add(
			optionValuePrice1.multiply(BigDecimal.valueOf(quantity1)));

		expectedPrice = expectedPrice.add(optionValuePrice2);

		expectedPrice = expectedPrice.add(optionValuePrice3);

		Assert.assertEquals(
			expectedPrice.stripTrailingZeros(),
			finalPrice.stripTrailingZeros());
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	private CommerceAccount _commerceAccount;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	private CommerceCurrency _commerceCurrency;

	@Inject(filter = "commerce.price.calculation.key=v1.0")
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Inject
	private CPInstanceLocalService _cpInstanceLocalService;

	@DeleteAfterTestRun
	private Group _group;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}