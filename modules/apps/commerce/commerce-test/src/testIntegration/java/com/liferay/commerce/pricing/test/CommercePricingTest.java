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
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelLocalService;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.service.CommerceDiscountLocalServiceUtil;
import com.liferay.commerce.discount.service.CommerceDiscountRelLocalServiceUtil;
import com.liferay.commerce.discount.test.util.CommerceDiscountTestUtil;
import com.liferay.commerce.price.CommercePriceCalculationRegistry;
import com.liferay.commerce.price.CommercePriceDiscovery;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.list.constants.CommercePriceListTypeKeys;
import com.liferay.commerce.price.list.discovery.CommercePriceListDiscovery;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommerceTierPriceEntry;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommercePriceListAccountRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListChannelRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListCommerceAccountGroupRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommercePriceListRelLocalService;
import com.liferay.commerce.price.list.service.CommerceTierPriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.test.util.CommercePriceListTestUtil;
import com.liferay.commerce.pricing.configuration.CommercePricingConfiguration;
import com.liferay.commerce.pricing.constants.CommercePriceModifierTargetConstants;
import com.liferay.commerce.pricing.constants.CommercePriceModifierTypeConstants;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.pricing.exception.CommerceUndefinedBasePriceListException;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.service.CommercePriceModifierLocalService;
import com.liferay.commerce.pricing.service.CommercePriceModifierRelLocalService;
import com.liferay.commerce.pricing.service.CommercePricingClassLocalService;
import com.liferay.commerce.pricing.service.CommercePricingClassRelLocalService;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.TestCommerceContext;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
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

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Riccardo Alberti
 */
@RunWith(Arquillian.class)
public class CommercePricingTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addUser();

		_commerceAccount =
			_commerceAccountLocalService.getPersonalCommerceAccount(
				_user.getUserId());

		_commerceAccountGroup =
			_commerceAccountGroupLocalService.addCommerceAccountGroup(
				_user.getCompanyId(), RandomTestUtil.randomString(), 0, false,
				"", ServiceContextTestUtil.getServiceContext());

		_commerceChannel = CommerceTestUtil.addCommerceChannel();

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency(
			_group.getGroupId());

		_commercePricingConfiguration =
			_configurationProvider.getSystemConfiguration(
				CommercePricingConfiguration.class);

		_updateProperties(
			"commercePriceListDiscovery",
			CommercePricingConstants.ORDER_BY_HIERARCHY);
	}

	@After
	public void tearDown() throws Exception {
		_commerceAccountLocalService.deleteCommerceAccount(
			_commerceAccount.getCommerceAccountId());

		_commerceAccountGroupCommerceAccountRelLocalService.
			deleteCommerceAccountGroupCommerceAccountRelByCAccountGroupId(
				_commerceAccount.getCommerceAccountId());

		_commerceAccountGroupLocalService.deleteCommerceAccountGroup(
			_commerceAccountGroup.getCommerceAccountGroupId());
	}

	@Test
	public void testCreateCatalogWithBasePriceList() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList2 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			commercePriceList2.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(8.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList2.getCommercePriceListId(), "", price);

		CommercePriceList discoveredPriceList =
			_commercePriceListDiscovery.getCommercePriceList(
				CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
				_commerceAccount.getCommerceAccountId(), null, 0);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues =
			_commercePriceDiscovery.getCommercePriceValue(
				discoveredPriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		CommercePriceValue commercePriceValue = commercePriceValues.get(0);

		CommerceMoney commerceMoney = commercePriceValue.getCommerceMoney();

		BigDecimal finalPrice = commerceMoney.getPrice();

		Assert.assertEquals(0, price.compareTo(finalPrice));
	}

	@Test
	public void testDiscountInBulkTierPriceEntryDiscoveryFalse()
		throws Exception {

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, false,
			BigDecimal.valueOf(10), BigDecimal.valueOf(15),
			BigDecimal.valueOf(5), BigDecimal.valueOf(10), true, true);

		BigDecimal price5 = BigDecimal.valueOf(18.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price5, 5, true,
			false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price10 = BigDecimal.valueOf(15.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price10, 10, true,
			false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price15 = BigDecimal.valueOf(10.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price15, 15, true,
			false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price20 = BigDecimal.valueOf(5.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price20, 20, true,
			false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPrice.compareTo(BigDecimal.valueOf(98.415)));
	}

	@Test
	public void testDiscountInPriceEntryDiscoveryFalse() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, false,
			BigDecimal.valueOf(10), BigDecimal.valueOf(15),
			BigDecimal.valueOf(5), BigDecimal.valueOf(10), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPrice.compareTo(BigDecimal.valueOf(130.815)));
	}

	@Test
	public void testDiscountInPriceEntryDiscoveryTrue() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, true,
			BigDecimal.valueOf(10), BigDecimal.valueOf(15),
			BigDecimal.valueOf(5), BigDecimal.valueOf(10), true, true);

		CommerceDiscountTestUtil.addFixedCommerceDiscount(
			_group.getGroupId(), 10, CommerceDiscountConstants.TARGET_PRODUCT,
			cpDefinition.getCPDefinitionId());

		BigDecimal percentage1 = BigDecimal.valueOf(5);

		_addPercentageCommerceDiscount(
			_group.getGroupId(), percentage1, CommerceDiscountConstants.LEVEL1,
			CommerceDiscountConstants.TARGET_PRODUCT,
			cpDefinition.getCPDefinitionId());

		BigDecimal percentage3 = BigDecimal.valueOf(70);

		_addPercentageCommerceDiscount(
			_group.getGroupId(), percentage3, CommerceDiscountConstants.LEVEL3,
			CommerceDiscountConstants.TARGET_PRODUCT,
			cpDefinition.getCPDefinitionId());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(30)));
	}

	@Test
	public void testDiscountInTierPriceEntryDiscoveryFalse() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, false,
			BigDecimal.valueOf(10), BigDecimal.valueOf(15),
			BigDecimal.valueOf(5), BigDecimal.valueOf(10), true, true);

		BigDecimal price5 = BigDecimal.valueOf(18.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price5, 5, false,
			false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price10 = BigDecimal.valueOf(15.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price10, 10,
			false, false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price15 = BigDecimal.valueOf(10.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price15, 15,
			false, false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		BigDecimal price20 = BigDecimal.valueOf(5.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), "", price20, 20,
			false, false, BigDecimal.valueOf(10), BigDecimal.valueOf(10),
			BigDecimal.valueOf(10), BigDecimal.valueOf(10), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPrice.compareTo(BigDecimal.valueOf(121.2165)));
	}

	@Test
	public void testDiscountInTierPriceEntryDiscoveryTrue() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, false,
			BigDecimal.valueOf(10), BigDecimal.valueOf(15),
			BigDecimal.valueOf(5), BigDecimal.valueOf(10), true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPrice.compareTo(BigDecimal.valueOf(130.815)));
	}

	@Test
	public void testEmptyDiscountInPriceEntryDiscoveryFalse() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1, false,
			null, null, null, null, true, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(200)));
	}

	@Test
	public void testPriceModifiersOnPricingClass() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CommercePriceList basePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			basePriceList.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CommercePricingClass commercePricingClass =
			_commercePricingClassLocalService.addCommercePricingClass(
				_user.getUserId(), _user.getGroupId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(),
				ServiceContextTestUtil.getServiceContext());

		List<AssetCategory> assetCategories =
			_assetCategoryLocalService.getAssetCategories(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		AssetCategory assetCategory = assetCategories.get(0);

		long[] assetCategoryIds = {assetCategory.getCategoryId()};

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId(), assetCategoryIds);

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		_commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClass.getCommercePricingClassId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId(),
			ServiceContextTestUtil.getServiceContext());

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			basePriceList.getCommercePriceListId(), "", price1);

		CommercePriceModifier commercePriceModifier = _addCommercePriceModifier(
			commercePriceList1.getGroupId(),
			CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
			commercePriceList1.getCommercePriceListId(),
			CommercePriceModifierTypeConstants.PERCENTAGE,
			BigDecimal.valueOf(-10), true);

		_commercePriceModifierRelLocalService.addCommercePriceModifierRel(
			commercePriceModifier.getCommercePriceModifierId(),
			CommercePricingClass.class.getName(),
			commercePricingClass.getCommercePricingClassId(),
			ServiceContextTestUtil.getServiceContext());

		CommercePriceModifier commercePriceModifier1 =
			_addCommercePriceModifier(
				commercePriceList1.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_CATEGORIES,
				commercePriceList1.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE,
				BigDecimal.valueOf(19), true);

		_commercePriceModifierRelLocalService.addCommercePriceModifierRel(
			commercePriceModifier1.getCommercePriceModifierId(),
			AssetCategory.class.getName(), assetCategory.getCategoryId(),
			ServiceContextTestUtil.getServiceContext());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(180)));
	}

	@Test
	public void testPriceModifiersOnProduct() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CommercePriceList basePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			basePriceList.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			basePriceList.getCommercePriceListId(), "", price1);

		CommercePriceModifier commercePriceModifier = _addCommercePriceModifier(
			commercePriceList1.getGroupId(),
			CommercePriceModifierTargetConstants.TARGET_PRODUCT,
			commercePriceList1.getCommercePriceListId(),
			CommercePriceModifierTypeConstants.ABSOLUTE,
			BigDecimal.valueOf(-10), true);

		_commercePriceModifierRelLocalService.addCommercePriceModifierRel(
			commercePriceModifier.getCommercePriceModifierId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId(),
			ServiceContextTestUtil.getServiceContext());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(100)));
	}

	@Test
	public void testRetrieveCorrectPriceList() throws Exception {
		CommercePriceList commercePriceListAccount =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 1.0);

		CommercePriceList commercePriceListAccountGroup =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 2.0);

		CommercePriceList commercePriceListChannel =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 100.0);

		commercePriceListChannel.setName("ZZZ" + System.currentTimeMillis());

		_commercePriceListLocalService.updateCommercePriceList(
			commercePriceListChannel);

		CommercePriceList discoveredPriceList =
			_commercePriceListDiscovery.getCommercePriceList(
				CommercePriceListTypeKeys.TYPE_PRICE_LIST, null, 0, null, 0);

		Assert.assertEquals(
			commercePriceListChannel.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceListAccount.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
			_commerceAccount.getCommerceAccountId(), null, 0);

		Assert.assertEquals(
			commercePriceListAccount.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_commerceAccountGroupCommerceAccountRelLocalService.
			addCommerceAccountGroupCommerceAccountRel(
				_commerceAccountGroup.getCommerceAccountGroupId(),
				_commerceAccount.getCommerceAccountId(),
				ServiceContextTestUtil.getServiceContext());

		_commercePriceListCommerceAccountGroupRelLocalService.
			addCommercePriceListCommerceAccountGroupRel(
				commercePriceListAccountGroup.getCommercePriceListId(),
				_commerceAccountGroup.getCommerceAccountGroupId(), 0,
				ServiceContextTestUtil.getServiceContext());

		long[] commerceAccountGroupIds = {
			_commerceAccountGroup.getCommerceAccountGroupId()
		};

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
			_commerceAccount.getCommerceAccountId(), commerceAccountGroupIds,
			0);

		Assert.assertEquals(
			commercePriceListAccount.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_commercePriceListChannelRelLocalService.addCommercePriceListChannelRel(
			commercePriceListChannel.getCommercePriceListId(),
			_commerceChannel.getCommerceChannelId(), 0,
			ServiceContextTestUtil.getServiceContext());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
			_commerceAccount.getCommerceAccountId(), commerceAccountGroupIds,
			_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceListAccount.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_commercePriceListLocalService.deleteCommercePriceList(
			commercePriceListAccount.getCommercePriceListId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
			_commerceAccount.getCommerceAccountId(), commerceAccountGroupIds,
			_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceListAccountGroup.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_commercePriceListLocalService.deleteCommercePriceList(
			commercePriceListAccountGroup.getCommercePriceListId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST, null,
			_commerceAccount.getCommerceAccountId(), commerceAccountGroupIds,
			_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceListChannel.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		_updateProperties(
			"commercePriceListDiscovery",
			CommercePricingConstants.ORDER_BY_LOWEST_ENTRY);

		CommercePriceList commercePriceListEntryAccount =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 1.0);

		CommercePriceList commercePriceListEntryChannel =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 2.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance();

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal value = BigDecimal.valueOf(8.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceListEntryAccount.getCommercePriceListId(), "", value);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceListEntryChannel.getCommercePriceListId(), "",
			BigDecimal.valueOf(10.0));

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			CommercePriceListTypeKeys.TYPE_PRICE_LIST,
			cpInstance.getCPInstanceUuid(),
			_commerceAccount.getCommerceAccountId(), commerceAccountGroupIds,
			_commerceChannel.getCommerceChannelId());

		CommercePriceEntry commercePriceEntry =
			_commercePriceEntryLocalService.fetchCommercePriceEntry(
				discoveredPriceList.getCommercePriceListId(),
				cpInstance.getCPInstanceUuid());

		Assert.assertEquals(0, value.compareTo(commercePriceEntry.getPrice()));
	}

	@Test
	public void testUseBulkTierPriceEntry() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			commercePriceList1.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1);

		BigDecimal price5 = BigDecimal.valueOf(18.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price5, 5, true);

		BigDecimal price10 = BigDecimal.valueOf(15.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price10, 10, true);

		BigDecimal price15 = BigDecimal.valueOf(10.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price15, 15, true);

		BigDecimal price20 = BigDecimal.valueOf(5.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price20, 20, true);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommercePriceValue> commercePriceValues1 =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList1.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 1, _commerceCurrency,
				commerceContext);

		Assert.assertEquals(
			commercePriceValues1.toString(), 1, commercePriceValues1.size());

		CommercePriceValue commercePriceValue1 = commercePriceValues1.get(0);

		CommerceMoney commerceMoney1 = commercePriceValue1.getCommerceMoney();

		Assert.assertEquals(0, price1.compareTo(commerceMoney1.getPrice()));

		List<CommercePriceValue> commercePriceValues10 =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList1.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 10, _commerceCurrency,
				commerceContext);

		Assert.assertEquals(
			commercePriceValues10.toString(), 1, commercePriceValues10.size());

		CommercePriceValue commercePriceValue10 = commercePriceValues10.get(0);

		CommerceMoney commerceMoney10 = commercePriceValue10.getCommerceMoney();

		Assert.assertEquals(0, price10.compareTo(commerceMoney10.getPrice()));

		List<CommercePriceValue> commercePriceValues18 =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList1.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 18, _commerceCurrency,
				commerceContext);

		Assert.assertEquals(
			commercePriceValues18.toString(), 1, commercePriceValues18.size());

		CommercePriceValue commercePriceValue18 = commercePriceValues18.get(0);

		CommerceMoney commerceMoney15 = commercePriceValue18.getCommerceMoney();

		Assert.assertEquals(0, price15.compareTo(commerceMoney15.getPrice()));

		List<CommercePriceValue> commercePriceValues25 =
			_commercePriceDiscovery.getCommercePriceValue(
				commercePriceList1.getCommercePriceListId(),
				cpInstance.getCPInstanceId(), 25, _commerceCurrency,
				commerceContext);

		Assert.assertEquals(
			commercePriceValues25.toString(), 1, commercePriceValues25.size());

		CommercePriceValue commercePriceValue25 = commercePriceValues25.get(0);

		CommerceMoney commerceMoney20 = commercePriceValue25.getCommerceMoney();

		Assert.assertEquals(0, price20.compareTo(commerceMoney20.getPrice()));
	}

	@Ignore
	@Test
	public void testUseExpiredPriceEntry() throws Exception {
		_getCommercePriceValues(true, false);
	}

	@Ignore
	@Test
	public void testUseNonpublishedPriceEntry() throws Exception {
		_getCommercePriceValues(false, false);
	}

	@Test
	public void testUseTierPriceEntryWithPromotion() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			commercePriceList1.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1);

		BigDecimal price5 = BigDecimal.valueOf(18.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price5, 5, false);

		BigDecimal price10 = BigDecimal.valueOf(15.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price10, 10, false);

		BigDecimal price15 = BigDecimal.valueOf(10.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price15, 15, false);

		BigDecimal price20 = BigDecimal.valueOf(5.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price20, 20, false);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(185)));

		CommercePriceList commercePromotion = _addPromotion(
			_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePromotion.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		BigDecimal promoPrice = BigDecimal.valueOf(10);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePromotion.getCommercePriceListId(), "", promoPrice);

		commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPromoPriceMoney =
			commerceProductPrice.getFinalPrice();

		BigDecimal finalPromoPrice = finalPromoPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPromoPrice.compareTo(BigDecimal.valueOf(100)));
	}

	@Test
	public void testUseTierPriceEntryWithTierPromotion() throws Exception {
		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				LocaleUtil.US.getDisplayLanguage(), null,
				ServiceContextTestUtil.getServiceContext());

		CommercePriceList commercePriceList1 =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 0.0);

		_commercePriceListRelLocalService.addCommercePriceListRel(
			commercePriceList1.getCommercePriceListId(),
			CommerceCatalog.class.getName(), catalog.getCommerceCatalogId(), 0,
			ServiceContextTestUtil.getServiceContext());

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList1.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(20.0);

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList1.getCommercePriceListId(), "", price1);

		BigDecimal price5 = BigDecimal.valueOf(18.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price5, 5, false);

		BigDecimal price10 = BigDecimal.valueOf(15.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price10, 10, false);

		BigDecimal price15 = BigDecimal.valueOf(10.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price15, 15, false);

		BigDecimal price20 = BigDecimal.valueOf(5.0);

		_addCommerceTierPriceEntry(
			commercePriceEntry.getCommercePriceEntryId(), price20, 20, false);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		CommerceProductPriceCalculation commerceProductPriceCalculation =
			_commercePriceCalculationRegistry.
				getCommerceProductPriceCalculation("v2.0");

		CommerceProductPrice commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPriceMoney = commerceProductPrice.getFinalPrice();

		BigDecimal finalPrice = finalPriceMoney.getPrice();

		Assert.assertEquals(0, finalPrice.compareTo(BigDecimal.valueOf(185)));

		CommercePriceList commercePromotion = _addPromotion(
			_group.getGroupId(), 0.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePromotion.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0,
			ServiceContextTestUtil.getServiceContext());

		BigDecimal promoPrice = BigDecimal.valueOf(10);

		CommercePriceEntry commercePromotionEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePromotion.getCommercePriceListId(), "", promoPrice);

		BigDecimal price3 = BigDecimal.valueOf(8);

		_addCommerceTierPriceEntry(
			commercePromotionEntry.getCommercePriceEntryId(), price3, 3, false);

		BigDecimal price7 = BigDecimal.valueOf(5);

		_addCommerceTierPriceEntry(
			commercePromotionEntry.getCommercePriceEntryId(), price7, 7, false);

		commerceProductPrice =
			commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstance.getCPInstanceId(), 10, false, commerceContext);

		CommerceMoney finalPromoPriceMoney =
			commerceProductPrice.getFinalPrice();

		BigDecimal finalPromoPrice = finalPromoPriceMoney.getPrice();

		Assert.assertEquals(
			0, finalPromoPrice.compareTo(BigDecimal.valueOf(72)));
	}

	@Test(expected = CommerceUndefinedBasePriceListException.class)
	public void testWithoutBasePricelist() throws Exception {
		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 1.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance();

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		_commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 1, _commerceCurrency,
			commerceContext);
	}

	@Test
	public void testGetOrderLevelDiscounts() throws Exception {

	}

	private static CommercePriceEntry _addCommercePriceEntry(
			long cpProductId, String cpInstanceUuid, long commercePriceListId,
			String externalReferenceCode, BigDecimal price)
		throws PortalException {

		CommercePriceList commercePriceList =
			CommercePriceListLocalServiceUtil.getCommercePriceList(
				commercePriceListId);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				commercePriceList.getGroupId());

		return CommercePriceEntryLocalServiceUtil.addCommercePriceEntry(
			cpProductId, cpInstanceUuid, commercePriceListId,
			externalReferenceCode, price, BigDecimal.ZERO, serviceContext);
	}

	private static void _addDiscountProductRel(
			CommerceDiscount commerceDiscount, long... targetIds)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		for (long id : targetIds) {
			CommerceDiscountRelLocalServiceUtil.addCommerceDiscountRel(
				commerceDiscount.getCommerceDiscountId(),
				CPDefinition.class.getName(), id, serviceContext);
		}
	}

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

	private CommercePriceModifier _addCommercePriceModifier(
			long groupId, String target, long commercePriceListId, String type,
			BigDecimal amount, boolean neverExpire)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(groupId);

		Calendar calendar = CalendarFactoryUtil.getCalendar(
			_user.getTimeZone());

		return _commercePriceModifierLocalService.addCommercePriceModifier(
			groupId, RandomTestUtil.randomString(), target, commercePriceListId,
			type, amount, 0.0, true, calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR),
			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE), neverExpire, serviceContext);
	}

	private CommerceTierPriceEntry _addCommerceTierPriceEntry(
			long commercePriceEntryId, BigDecimal price, int minQuantity,
			boolean bulkPricing)
		throws PortalException {

		CommercePriceEntry commercePriceEntry =
			CommercePriceEntryLocalServiceUtil.getCommercePriceEntry(
				commercePriceEntryId);

		CommercePriceList commercePriceList =
			commercePriceEntry.getCommercePriceList();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				commercePriceList.getGroupId());

		return CommerceTierPriceEntryLocalServiceUtil.addCommerceTierPriceEntry(
			commercePriceEntryId, price, null, bulkPricing, minQuantity,
			serviceContext);
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

	private CommerceDiscount _addPercentageCommerceDiscount(
			long groupId, BigDecimal percentage, String level, String target,
			long... targetIds)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(groupId);

		User user = UserLocalServiceUtil.getDefaultUser(
			serviceContext.getCompanyId());

		Calendar calendar = CalendarFactoryUtil.getCalendar(user.getTimeZone());

		CommerceDiscount commerceDiscount =
			CommerceDiscountLocalServiceUtil.addCommerceDiscount(
				user.getUserId(), RandomTestUtil.randomString(), target, false,
				null, true, BigDecimal.valueOf(10000), level, percentage,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				CommerceDiscountConstants.LIMITATION_TYPE_UNLIMITED, 0, true,
				true, calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true, serviceContext);

		_addDiscountProductRel(commerceDiscount, targetIds);

		return commerceDiscount;
	}

	private List<CommercePriceValue> _getCommercePriceValues(
			boolean publish, boolean neverExpire)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				_group.getGroupId(), 1.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance();

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(10.0);

		_addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price, false,
			BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			publish, neverExpire);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		return _commercePriceDiscovery.getCommercePriceValue(
			commercePriceList.getCommercePriceListId(),
			cpInstance.getCPInstanceId(), 1, _commerceCurrency,
			commerceContext);
	}

	private void _updateProperties(String key, int value)
		throws ConfigurationException {

		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(key, value);

		_configurationProvider.saveSystemConfiguration(
			CommercePricingConfiguration.class, properties);
	}

	@Inject
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Inject
	private AssetEntryLocalService _assetEntryLocalService;

	private CommerceAccount _commerceAccount;
	private CommerceAccountGroup _commerceAccountGroup;

	@Inject
	private CommerceAccountGroupCommerceAccountRelLocalService
		_commerceAccountGroupCommerceAccountRelLocalService;

	@Inject
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@DeleteAfterTestRun
	private CommerceChannel _commerceChannel;

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommercePriceCalculationRegistry _commercePriceCalculationRegistry;

	@Inject
	private CommercePriceDiscovery _commercePriceDiscovery;

	@Inject
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Inject
	private CommercePriceListAccountRelLocalService
		_commercePriceListAccountRelLocalService;

	@Inject
	private CommercePriceListChannelRelLocalService
		_commercePriceListChannelRelLocalService;

	@Inject
	private CommercePriceListCommerceAccountGroupRelLocalService
		_commercePriceListCommerceAccountGroupRelLocalService;

	@Inject
	private CommercePriceListDiscovery _commercePriceListDiscovery;

	@Inject
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Inject
	private CommercePriceListRelLocalService _commercePriceListRelLocalService;

	@Inject
	private CommercePriceModifierLocalService
		_commercePriceModifierLocalService;

	@Inject
	private CommercePriceModifierRelLocalService
		_commercePriceModifierRelLocalService;

	@Inject
	private CommercePricingClassLocalService _commercePricingClassLocalService;

	@Inject
	private CommercePricingClassRelLocalService
		_commercePricingClassRelLocalService;

	private CommercePricingConfiguration _commercePricingConfiguration;

	@Inject
	private ConfigurationProvider _configurationProvider;

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User _user;

}