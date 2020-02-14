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
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.test.util.AssetTestUtil;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommercePriceListLocalServiceUtil;
import com.liferay.commerce.pricing.constants.CommercePriceModifierTargetConstants;
import com.liferay.commerce.pricing.constants.CommercePriceModifierTypeConstants;
import com.liferay.commerce.pricing.discovery.modifier.CommercePriceModifierDiscovery;
import com.liferay.commerce.pricing.exception.CommercePriceModifierAmountException;
import com.liferay.commerce.pricing.exception.CommercePriceModifierTargetException;
import com.liferay.commerce.pricing.exception.CommercePriceModifierTitleException;
import com.liferay.commerce.pricing.exception.CommercePriceModifierTypeException;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.model.CommercePriceModifierRel;
import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.commerce.pricing.service.CommercePriceModifierLocalService;
import com.liferay.commerce.pricing.service.CommercePriceModifierRelLocalService;
import com.liferay.commerce.pricing.service.CommercePricingClassLocalService;
import com.liferay.commerce.pricing.service.CommercePricingClassRelLocalService;
import com.liferay.commerce.pricing.test.util.CommercePriceModifierTestUtil;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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
public class CommercePriceModifierTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_user = UserTestUtil.addUser(_company);

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency();

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			_user.getCompanyId(), _user.getGroupId(), _user.getUserId());
	}

	@Test
	public void testAbsoluteModifierTargetPricingClass() throws Exception {
		frutillaRule.scenario(
			"A type absolute price modifier modifies the price of the items " +
				"in a pricing class"
		).given(
			"A catalog with at least two product and one price list"
		).and(
			"A pricing class containing the two products"
		).and(
			"A type absolute price modifier targeting the pricing class"
		).when(
			"The price modifier is applied to the products"
		).then(
			"The original price of the two products is modified"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance1 = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition1 = cpInstance1.getCPDefinition();

		CPInstance cpInstance2 = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition2 = cpInstance2.getCPDefinition();

		CommercePricingClass commercePricingClass =
			_commercePricingClassLocalService.addCommercePricingClass(
				_user.getUserId(), _user.getGroupId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), _serviceContext);

		_commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClass.getCommercePricingClassId(),
			CPDefinition.class.getName(), cpDefinition1.getCPDefinitionId(),
			_serviceContext);

		_commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClass.getCommercePricingClassId(),
			CPDefinition.class.getName(), cpDefinition2.getCPDefinitionId(),
			_serviceContext);

		BigDecimal price1 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		BigDecimal price2 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry1 = _addCommercePriceEntry(
			cpDefinition1.getCProductId(), cpInstance1.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price1);

		CommercePriceEntry commercePriceEntry2 = _addCommercePriceEntry(
			cpDefinition2.getCProductId(), cpInstance2.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price2);

		CommercePriceModifier commercePriceModifier =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.ABSOLUTE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier.getGroupId(),
			commercePriceModifier.getCommercePriceModifierId(),
			CommercePricingClass.class.getName(),
			commercePricingClass.getCommercePricingClassId());

		CommerceMoney priceMoney1 = commercePriceEntry1.getPriceMoney(
			_commerceCurrency.getCommerceCurrencyId());

		CommerceMoney modifiedMoney1 =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceList.getCommercePriceListId(),
				cpInstance1.getCPDefinitionId(), priceMoney1,
				_commerceCurrency);

		CommerceMoney priceMoney2 = commercePriceEntry2.getPriceMoney(
			_commerceCurrency.getCommerceCurrencyId());

		CommerceMoney modifiedMoney2 =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceList.getCommercePriceListId(),
				cpInstance2.getCPDefinitionId(), priceMoney2,
				_commerceCurrency);

		CommerceMoney finalMoney1 = _commerceMoneyFactory.create(
			_commerceCurrency, price1.add(amount));
		CommerceMoney finalMoney2 = _commerceMoneyFactory.create(
			_commerceCurrency, price2.add(amount));

		Assert.assertEquals(finalMoney1.getPrice(), modifiedMoney1.getPrice());
		Assert.assertEquals(finalMoney2.getPrice(), modifiedMoney2.getPrice());
	}

	@Test
	public void testCreatePriceModifierWithCategoryTarget() throws Exception {
		frutillaRule.scenario(
			"A price modifier with category target is created for a price list"
		).given(
			"A catalog with at least a product and a price list"
		).and(
			"A category containing at least a product"
		).when(
			"The price modifier is created"
		).then(
			"The price modifiers has the category as a target"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		AssetVocabulary assetVocabulary = AssetTestUtil.addVocabulary(
			_user.getGroupId());

		AssetCategory assetCategory = AssetTestUtil.addCategory(
			_user.getGroupId(), assetVocabulary.getVocabularyId());

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_CATEGORIES,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			AssetCategory.class.getName(), assetCategory.getCategoryId());

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierLocalService.getCommercePriceModifiers(
				commercePriceList.getCommercePriceListId());

		CommercePriceModifier commercePriceModifier2 =
			commercePriceModifiers.get(0);

		Assert.assertEquals(
			commercePriceModifier1.getCommercePriceModifierId(),
			commercePriceModifier2.getCommercePriceModifierId());

		Assert.assertEquals(
			CommercePriceModifierTargetConstants.TARGET_CATEGORIES,
			commercePriceModifier2.getTarget());

		List<CommercePriceModifierRel> commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier2.getCommercePriceModifierId(),
				AssetCategory.class.getName());

		CommercePriceModifierRel commercePriceModifierRel =
			commercePriceModifierRels.get(0);

		Assert.assertEquals(
			assetCategory.getCategoryId(),
			commercePriceModifierRel.getClassPK());
	}

	@Test(expected = CommercePriceModifierAmountException.class)
	public void testCreatePriceModifierWithInvalidAmount() throws Exception {
		frutillaRule.scenario(
			"Creating a price modifier with invalid amount will raise an " +
				"exception"
		).given(
			"A catalog and a price list"
		).when(
			"I try to create a price modifier with invalid amount"
		).then(
			"An exception shall be raised"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CommercePriceModifierTestUtil.addCommercePriceModifier(
			catalog.getGroupId(),
			CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
			commercePriceList.getCommercePriceListId(),
			CommercePriceModifierTypeConstants.PERCENTAGE, null, true);
	}

	@Test(expected = CommercePriceModifierTargetException.class)
	public void testCreatePriceModifierWithInvalidTarget() throws Exception {
		frutillaRule.scenario(
			"Creating a price modifier with invalid target will raise an " +
				"exception"
		).given(
			"A catalog and a price list"
		).when(
			"I try to create a price modifier with invalid target"
		).then(
			"An exception shall be raised"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifierTestUtil.addCommercePriceModifier(
			catalog.getGroupId(), RandomTestUtil.randomString(),
			commercePriceList.getCommercePriceListId(),
			CommercePriceModifierTypeConstants.OVERRIDE, amount, true);
	}

	@Test(expected = CommercePriceModifierTypeException.class)
	public void testCreatePriceModifierWithInvalidType() throws Exception {
		frutillaRule.scenario(
			"Creating a price modifier with invalid type will raise an " +
				"exception"
		).given(
			"A catalog and a price list"
		).when(
			"I try to create a price modifier with invalid type"
		).then(
			"An exception shall be raised"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifierTestUtil.addCommercePriceModifier(
			catalog.getGroupId(),
			CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
			commercePriceList.getCommercePriceListId(),
			RandomTestUtil.randomString(), amount, true);
	}

	@Test
	public void testCreatePriceModifierWithNoTarget() throws Exception {
		frutillaRule.scenario(
			"A price modifier with no targets is created for a price list"
		).given(
			"A catalog and a price list"
		).when(
			"The price modifier is created with no target"
		).then(
			"The price modifier has target catalog"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierLocalService.getCommercePriceModifiers(
				commercePriceList.getCommercePriceListId());

		CommercePriceModifier commercePriceModifier2 =
			commercePriceModifiers.get(0);

		Assert.assertEquals(
			commercePriceModifier1.getCommercePriceModifierId(),
			commercePriceModifier2.getCommercePriceModifierId());

		Assert.assertEquals(
			CommercePriceModifierTargetConstants.TARGET_CATALOG,
			commercePriceModifier2.getTarget());
	}

	@Test(expected = CommercePriceModifierTitleException.class)
	public void testCreatePriceModifierWithNullTitle() throws Exception {
		frutillaRule.scenario(
			"Creating a price modifier with null of empty title will raise " +
				"an exception"
		).given(
			"A catalog and a price list"
		).when(
			"I try to create a price modifier with no title"
		).then(
			"An exception shall be raised"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifierTestUtil.addCommercePriceModifier(
			catalog.getGroupId(), null,
			CommercePriceModifierTargetConstants.TARGET_PRODUCT,
			commercePriceList.getCommercePriceListId(),
			CommercePriceModifierTypeConstants.OVERRIDE, amount, true);
	}

	@Test
	public void testCreatePriceModifierWithPricingClassTarget()
		throws Exception {

		frutillaRule.scenario(
			"A price modifier with pricing class target is created for a " +
				"price list"
		).given(
			"A catalog with at least a product and a price list"
		).and(
			"A pricing class containing at least a product"
		).when(
			"The price modifier is created"
		).then(
			"The price modifiers has the pricing class as a target"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommercePricingClass commercePricingClass =
			_commercePricingClassLocalService.addCommercePricingClass(
				_user.getUserId(), _user.getGroupId(),
				RandomTestUtil.randomString(), RandomTestUtil.randomString(),
				RandomTestUtil.randomString(), _serviceContext);

		_commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClass.getCommercePricingClassId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId(),
			_serviceContext);

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			CommercePricingClass.class.getName(),
			commercePricingClass.getCommercePricingClassId());

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierLocalService.getCommercePriceModifiers(
				commercePriceList.getCommercePriceListId());

		CommercePriceModifier commercePriceModifier2 =
			commercePriceModifiers.get(0);

		Assert.assertEquals(
			commercePriceModifier1.getCommercePriceModifierId(),
			commercePriceModifier2.getCommercePriceModifierId());

		Assert.assertEquals(
			CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS,
			commercePriceModifier2.getTarget());

		List<CommercePriceModifierRel> commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier2.getCommercePriceModifierId(),
				CommercePricingClass.class.getName());

		CommercePriceModifierRel commercePriceModifierRel =
			commercePriceModifierRels.get(0);

		Assert.assertEquals(
			commercePricingClass.getCommercePricingClassId(),
			commercePriceModifierRel.getClassPK());

		List<CommercePricingClassRel> commercePricingClassRels =
			_commercePricingClassRelLocalService.getCommercePricingClassRels(
				commercePricingClass.getCommercePricingClassId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePricingClassRels.toString(), 1,
			commercePricingClassRels.size());

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRels.get(0);

		Assert.assertEquals(
			cpDefinition.getCPDefinitionId(),
			commercePricingClassRel.getClassPK());
	}

	@Test
	public void testCreatePriceModifierWithProductTarget() throws Exception {
		frutillaRule.scenario(
			"A price modifier with product target is created for a price list"
		).given(
			"A catalog with at least a product and a price list"
		).when(
			"The price modifier is created"
		).then(
			"The price modifiers has the catalog product as a target"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRODUCT,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierLocalService.getCommercePriceModifiers(
				commercePriceList.getCommercePriceListId());

		CommercePriceModifier commercePriceModifier2 =
			commercePriceModifiers.get(0);

		Assert.assertEquals(
			commercePriceModifier1.getCommercePriceModifierId(),
			commercePriceModifier2.getCommercePriceModifierId());

		Assert.assertEquals(
			CommercePriceModifierTargetConstants.TARGET_PRODUCT,
			commercePriceModifier2.getTarget());

		List<CommercePriceModifierRel> commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier2.getCommercePriceModifierId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePriceModifierRels.toString(), 1,
			commercePriceModifierRels.size());

		CommercePriceModifierRel commercePriceModifierRel =
			commercePriceModifierRels.get(0);

		Assert.assertEquals(
			cpDefinition.getCPDefinitionId(),
			commercePriceModifierRel.getClassPK());
	}

	@Test
	public void testDeletePriceModifier() throws Exception {
		frutillaRule.scenario(
			"When a price modifier is deleted also its rels are deleted"
		).given(
			"A price modifier with a specific target"
		).when(
			"I delete all modifiers attached to a price list"
		).then(
			"The price modifier is deleted and the old rels are deleted"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRODUCT,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

		List<CommercePriceModifierRel> commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier1.getCommercePriceModifierId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePriceModifierRels.toString(), 1,
			commercePriceModifierRels.size());

		_commercePriceModifierLocalService.
			deleteCommercePriceModifiersByCommercePriceListId(
				commercePriceList.getCommercePriceListId());

		CommercePriceModifier commercePriceModifier2 =
			_commercePriceModifierLocalService.fetchCommercePriceModifier(
				commercePriceModifier1.getCommercePriceModifierId());

		Assert.assertNull(commercePriceModifier2);

		commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier1.getCommercePriceModifierId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePriceModifierRels.toString(), 0,
			commercePriceModifierRels.size());
	}

	@Test
	public void testMultiplePercentageModifierTargetCategory()
		throws Exception {

		frutillaRule.scenario(
			"When multiple type percentage price modifiers are defined the " +
				"one that produces the lowest price is applied to the targets"
		).given(
			"A catalog with at least two product and one price list"
		).and(
			"A category containing the two products"
		).and(
			"Two type percentage price modifiers targeting the category"
		).when(
			"The price modifier is applied to the products in the category"
		).then(
			"The original price of the two products is modified"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0,
				_commerceCurrency.getCommerceCurrencyId());

		AssetVocabulary assetVocabulary = AssetTestUtil.addVocabulary(
			_user.getGroupId());

		AssetCategory assetCategory = AssetTestUtil.addCategory(
			_user.getGroupId(), assetVocabulary.getVocabularyId());

		long[] assetCategoryIds = {assetCategory.getCategoryId()};

		CPInstance cpInstance1 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId(), assetCategoryIds);

		CPDefinition cpDefinition1 = cpInstance1.getCPDefinition();

		CPInstance cpInstance2 = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition2 = cpInstance2.getCPDefinition();

		BigDecimal price1 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		BigDecimal price2 = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry1 = _addCommercePriceEntry(
			cpDefinition1.getCProductId(), cpInstance1.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price1);

		CommercePriceEntry commercePriceEntry2 = _addCommercePriceEntry(
			cpDefinition2.getCProductId(), cpInstance2.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price2);

		BigDecimal amount1 = BigDecimal.valueOf(-10);

		BigDecimal amount2 = BigDecimal.valueOf(-1);

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_CATEGORIES,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.PERCENTAGE, amount1, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			AssetCategory.class.getName(), assetCategory.getCategoryId());

		CommercePriceModifier commercePriceModifier2 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_CATEGORIES,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.PERCENTAGE, amount2, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier2.getGroupId(),
			commercePriceModifier2.getCommercePriceModifierId(),
			AssetCategory.class.getName(), assetCategory.getCategoryId());

		CommerceMoney priceMoney1 = commercePriceEntry1.getPriceMoney(
			_commerceCurrency.getCommerceCurrencyId());

		CommerceMoney priceMoney2 = commercePriceEntry2.getPriceMoney(
			_commerceCurrency.getCommerceCurrencyId());

		CommerceMoney modifiedMoney1 =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceList.getCommercePriceListId(),
				cpInstance1.getCPDefinitionId(), priceMoney1,
				_commerceCurrency);

		CommerceMoney modifiedMoney2 =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceList.getCommercePriceListId(),
				cpInstance2.getCPDefinitionId(), priceMoney2,
				_commerceCurrency);

		BigDecimal modifiedPrice1 = modifiedMoney1.getPrice();

		RoundingMode roundingMode = RoundingMode.valueOf(
			_commerceCurrency.getRoundingMode());

		MathContext mathContext = new MathContext(
			modifiedPrice1.precision(), roundingMode);

		BigDecimal finalPrice1 = price1.multiply(
			BigDecimal.valueOf(0.9), mathContext);

		CommerceMoney finalMoney1 = _commerceMoneyFactory.create(
			_commerceCurrency, finalPrice1);

		Assert.assertEquals(finalMoney1.getPrice(), modifiedPrice1);

		Assert.assertEquals(priceMoney2.getPrice(), modifiedMoney2.getPrice());
	}

	@Test
	public void testOverridePriceTargetProduct() throws Exception {
		frutillaRule.scenario(
			"A type override price modifier overrides the price of a product"
		).given(
			"A catalog with at least one product and one price list"
		).and(
			"A type override price modifier targeting the product"
		).when(
			"The price modifier is applied to the product"
		).then(
			"The original price is overridden by the modifier"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal price = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceEntry commercePriceEntry = _addCommercePriceEntry(
			cpDefinition.getCProductId(), cpInstance.getCPInstanceUuid(),
			commercePriceList.getCommercePriceListId(), "", price);

		CommercePriceModifier commercePriceModifier =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRODUCT,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier.getGroupId(),
			commercePriceModifier.getCommercePriceModifierId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

		CommerceMoney priceMoney = commercePriceEntry.getPriceMoney(
			_commerceCurrency.getCommerceCurrencyId());

		CommerceMoney modifiedMoney =
			_commercePriceModifierDiscovery.applyCommercePriceModifier(
				commercePriceList.getCommercePriceListId(),
				cpInstance.getCPDefinitionId(), priceMoney, _commerceCurrency);

		RoundingMode roundingMode = RoundingMode.valueOf(
			_commerceCurrency.getRoundingMode());

		BigDecimal finalPrice = modifiedMoney.getPrice();

		MathContext mathContext = new MathContext(
			finalPrice.precision(), roundingMode);

		Assert.assertEquals(
			amount.round(mathContext), modifiedMoney.getPrice());
	}

	@Test
	public void testUpdatePriceModifierTarget() throws Exception {
		frutillaRule.scenario(
			"It is possible to update the target of a price modifier"
		).given(
			"A price modifier with a specific target"
		).when(
			"I change the target of the modifier"
		).then(
			"The price modifier target is updated and the old rels are deleted"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceModifierTestUtil.addCommercePriceList(
				catalog.getGroupId(), 0.0);

		CPInstance cpInstance = CPTestUtil.addCPInstance(catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		BigDecimal amount = BigDecimal.valueOf(RandomTestUtil.randomDouble());

		CommercePriceModifier commercePriceModifier1 =
			CommercePriceModifierTestUtil.addCommercePriceModifier(
				catalog.getGroupId(),
				CommercePriceModifierTargetConstants.TARGET_PRODUCT,
				commercePriceList.getCommercePriceListId(),
				CommercePriceModifierTypeConstants.OVERRIDE, amount, true);

		CommercePriceModifierTestUtil.addCommercePriceModifierRel(
			commercePriceModifier1.getGroupId(),
			commercePriceModifier1.getCommercePriceModifierId(),
			CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

		List<CommercePriceModifierRel> commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier1.getCommercePriceModifierId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePriceModifierRels.toString(), 1,
			commercePriceModifierRels.size());

		CommercePriceModifierRel commercePriceModifierRel =
			commercePriceModifierRels.get(0);

		Assert.assertEquals(
			cpDefinition.getCPDefinitionId(),
			commercePriceModifierRel.getClassPK());

		CommercePriceModifier commercePriceModifier2 =
			CommercePriceModifierTestUtil.updateCommercePriceModifier(
				catalog.getGroupId(),
				commercePriceModifier1.getCommercePriceModifierId(),
				CommercePriceModifierTargetConstants.TARGET_PRICING_CLASS);

		commercePriceModifierRels =
			_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
				commercePriceModifier2.getCommercePriceModifierId(),
				CPDefinition.class.getName());

		Assert.assertEquals(
			commercePriceModifierRels.toString(), 0,
			commercePriceModifierRels.size());
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

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

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Inject
	private CommercePriceModifierDiscovery _commercePriceModifierDiscovery;

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

	@DeleteAfterTestRun
	private Company _company;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}