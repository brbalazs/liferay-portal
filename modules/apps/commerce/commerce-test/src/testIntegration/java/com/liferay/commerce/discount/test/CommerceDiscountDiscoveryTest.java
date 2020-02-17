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

package com.liferay.commerce.discount.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelLocalServiceUtil;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.account.test.util.CommerceAccountTestUtil;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.discovery.CommerceDiscountDiscovery;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.service.CommerceDiscountAccountRelLocalService;
import com.liferay.commerce.discount.service.CommerceDiscountCommerceAccountGroupRelLocalService;
import com.liferay.commerce.discount.test.util.CommerceDiscountTestUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.pricing.configuration.CommercePricingConfiguration;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceChannelRelLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.TestCommerceContext;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

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
public class CommerceDiscountDiscoveryTest {

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

		_commerceAccount = CommerceAccountTestUtil.addBusinessCommerceAccount(
			_user.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			new long[] {_user.getUserId()}, null, _serviceContext);

		_commerceAccountGroup =
			_commerceAccountGroupLocalService.addCommerceAccountGroup(
				_company.getCompanyId(), RandomTestUtil.randomString(), 0,
				false, null, _serviceContext);

		CommerceAccountGroupCommerceAccountRelLocalServiceUtil.
			addCommerceAccountGroupCommerceAccountRel(
				_commerceAccountGroup.getCommerceAccountGroupId(),
				_commerceAccount.getCommerceAccountId(), _serviceContext);

		_commerceChannel = CommerceTestUtil.addCommerceChannel();

		_commercePricingConfiguration =
			_configurationProvider.getSystemConfiguration(
				CommercePricingConfiguration.class);

		_commerceOrders = new ArrayList<>();

		_updateProperties(
			"commerceDiscountApplicationMethod",
			CommercePricingConstants.DISCOUNT_CHAIN_METHOD);
	}

	@After
	public void tearDown() throws Exception {
		for (CommerceOrder commerceOrder : _commerceOrders) {
			_commerceOrderLocalService.deleteCommerceOrder(commerceOrder);
		}
	}

	@Test
	public void testCreateFixedDiscountWithTargetProduct() throws Exception {
		frutillaRule.scenario(
			"When a fixed amount discount is targeting a product in a " +
				"catalog it shall be possible to retrieve it with the " +
					"discount discovery"
		).given(
			"A catalog a product and a discount targeting that product"
		).when(
			"The discount is discovered"
		).then(
			"The discount is matching the created one"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				_group.getGroupId(), RandomTestUtil.nextDouble(),
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		Assert.assertEquals(
			commerceDiscounts.toString(), 1, commerceDiscounts.size());

		Assert.assertEquals(commerceDiscount, commerceDiscounts.get(0));
	}

	@Test
	public void testCreatePercentageDiscountWithTargetProduct()
		throws Exception {

		frutillaRule.scenario(
			"When a percentage amount discount is targeting a product in a " +
				"catalog it shall be possible to retrieve it with the " +
					"discount discovery"
		).given(
			"A catalog a product and a discount targeting that product"
		).when(
			"The discount is discovered"
		).then(
			"The discount is matching the created one"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				_group.getGroupId(),
				BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				CommerceDiscountConstants.LEVEL1,
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount, null);

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		Assert.assertEquals(
			commerceDiscounts.toString(), 1, commerceDiscounts.size());

		Assert.assertEquals(commerceDiscount, commerceDiscounts.get(0));
	}

	@Test
	public void testRetrieveCorrectDiscountByHierarchy() throws Exception {
		frutillaRule.scenario(
			"When multiple discounts are defined for the same target the " +
				"highest in the hierarchy shall be taken"
		).given(
			"A catalog with multiple discounts"
		).when(
			"The discount is discovered"
		).then(
			"The discount with highest rank is retrieved"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommerceDiscount commerceUnqualifiedDiscount =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				_group.getGroupId(),
				BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				CommerceDiscountConstants.LEVEL2,
				CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinition.getCPDefinitionId());

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, _commerceChannel, _user, _group,
			_commerceAccount, null);

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		CommerceDiscount commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceUnqualifiedDiscount.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());

		CommerceDiscount commerceChannelDiscount = _addChannelDiscount(
			_group.getGroupId(), CommerceDiscountConstants.LEVEL1,
			cpDefinition.getCPDefinitionId());

		commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceChannelDiscount.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());

		CommerceDiscount commerceAccountGroupsDiscount =
			_addAccountGroupDiscount(
				_group.getGroupId(), CommerceDiscountConstants.LEVEL3,
				cpDefinition.getCPDefinitionId());

		commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceAccountGroupsDiscount.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());

		CommerceDiscount commerceAccountDiscount = _addAccountDiscount(
			_group.getGroupId(), CommerceDiscountConstants.LEVEL4,
			cpDefinition.getCPDefinitionId());

		commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscountByHierarchy(
				commerceContext, cpDefinition.getCPDefinitionId());

		commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceAccountDiscount.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());
	}

	@Test
	public void testRetrieveDiscountsWithoutHierarchy() throws Exception {
		frutillaRule.scenario(
			"When multiple discounts are defined for the same target the " +
				"full list shall be returned"
		).given(
			"A catalog with multiple discounts"
		).when(
			"The discount is discovered"
		).then(
			"The full list of discounts is retrieved"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			catalog.getGroupId());

		CPDefinition cpDefinition = cpInstance.getCPDefinition();

		CommerceDiscount commerceChannelDiscount = _addChannelDiscount(
			_group.getGroupId(), CommerceDiscountConstants.LEVEL1,
			cpDefinition.getCPDefinitionId());

		CommerceDiscount commerceAccountGroupsDiscount =
			_addAccountGroupDiscount(
				_group.getGroupId(), CommerceDiscountConstants.LEVEL3,
				cpDefinition.getCPDefinitionId());

		CommerceDiscount commerceAccountDiscount = _addAccountDiscount(
			_group.getGroupId(), CommerceDiscountConstants.LEVEL4,
			cpDefinition.getCPDefinitionId());

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getProductCommerceDiscount(
				_commerceAccount.getCommerceAccountId(),
				_getCommerceAccoutGroupIds(),
				_commerceChannel.getCommerceChannelId(),
				cpDefinition.getCPDefinitionId());

		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceChannelDiscount));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceAccountGroupsDiscount));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceAccountDiscount));
	}

	@Test
	public void testRetrieveOrderDiscountByHierarchy() throws Exception {
		frutillaRule.scenario(
			"When multiple discounts are defined for the same target the " +
				"highest in the hierarchy shall be taken"
		).given(
			"A catalog with multiple discounts"
		).when(
			"The discount is discovered"
		).then(
			"The discount with highest rank is retrieved"
		);

		CommerceOrder commerceOrder = CommerceTestUtil.addB2CCommerceOrder(
			_user.getUserId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getSiteGroupId(), _commerceCurrency);

		_commerceOrders.add(commerceOrder);

		CommerceContext commerceContext = new TestCommerceContext(
			_commerceCurrency, null, _user, _group, _commerceAccount,
			commerceOrder);

		_addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);
		_addAccountGroupOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);
		CommerceDiscount commerceDiscountTotal3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getOrderCommerceDiscountByHierarchy(
				commerceContext, CommerceDiscountConstants.TARGET_TOTAL);

		CommerceDiscount commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceDiscountTotal3.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());

		_addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);
		_addAccountGroupOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);
		CommerceDiscount commerceDiscountShipping3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);

		commerceDiscounts =
			_commerceDiscountDiscovery.getOrderCommerceDiscountByHierarchy(
				commerceContext, CommerceDiscountConstants.TARGET_SHIPPING);

		commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceDiscountShipping3.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());

		_addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);
		_addAccountGroupOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);
		CommerceDiscount commerceDiscountSubtotal3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);

		commerceDiscounts =
			_commerceDiscountDiscovery.getOrderCommerceDiscountByHierarchy(
				commerceContext, CommerceDiscountConstants.TARGET_SUBTOTAL);

		commerceDiscount = commerceDiscounts.get(0);

		Assert.assertEquals(
			commerceDiscountSubtotal3.getCommerceDiscountId(),
			commerceDiscount.getCommerceDiscountId());
	}

	@Test
	public void testRetrieveOrderDiscountWithNoHierarchy() throws Exception {
		frutillaRule.scenario(
			"When multiple discounts are defined for the same target the " +
				"highest in the hierarchy shall be taken"
		).given(
			"A catalog with multiple discounts"
		).when(
			"The discount is discovered"
		).then(
			"The discount with highest rank is retrieved"
		);

		CommerceOrder commerceOrder = CommerceTestUtil.addB2CCommerceOrder(
			_user.getUserId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getSiteGroupId(), _commerceCurrency);

		_commerceOrders.add(commerceOrder);

		CommerceDiscount commerceDiscountTotal1 = _addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);
		CommerceDiscount commerceDiscountTotal2 = _addAccountGroupOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);
		CommerceDiscount commerceDiscountTotal3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_TOTAL);

		List<CommerceDiscount> commerceDiscounts =
			_commerceDiscountDiscovery.getOrderCommerceDiscount(
				_commerceAccount.getCommerceAccountId(),
				_getCommerceAccoutGroupIds(),
				_commerceChannel.getCommerceChannelId(),
				CommerceDiscountConstants.TARGET_TOTAL);

		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountTotal1));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountTotal2));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountTotal3));

		CommerceDiscount commerceDiscountShipping1 = _addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);
		CommerceDiscount commerceDiscountShipping2 =
			_addAccountGroupOrderDiscount(
				_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);
		CommerceDiscount commerceDiscountShipping3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SHIPPING);

		commerceDiscounts = _commerceDiscountDiscovery.getOrderCommerceDiscount(
			_commerceAccount.getCommerceAccountId(),
			_getCommerceAccoutGroupIds(),
			_commerceChannel.getCommerceChannelId(),
			CommerceDiscountConstants.TARGET_SHIPPING);

		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountShipping1));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountShipping2));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountShipping3));

		CommerceDiscount commerceDiscountSubtotal1 = _addChannelOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);
		CommerceDiscount commerceDiscountSubtotal2 =
			_addAccountGroupOrderDiscount(
				_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);
		CommerceDiscount commerceDiscountSubtotal3 = _addAccountOrderDiscount(
			_group.getGroupId(), CommerceDiscountConstants.TARGET_SUBTOTAL);

		commerceDiscounts = _commerceDiscountDiscovery.getOrderCommerceDiscount(
			_commerceAccount.getCommerceAccountId(),
			_getCommerceAccoutGroupIds(),
			_commerceChannel.getCommerceChannelId(),
			CommerceDiscountConstants.TARGET_SUBTOTAL);

		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountSubtotal1));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountSubtotal2));
		Assert.assertEquals(
			true, commerceDiscounts.contains(commerceDiscountSubtotal3));
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	private CommerceDiscount _addAccountDiscount(
			long groupId, String level, long cpDefinitionId)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				groupId, BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				level, CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinitionId);

		_commerceDiscountAccountRelLocalService.addCommerceDiscountAccountRel(
			commerceDiscount.getCommerceDiscountId(),
			_commerceAccount.getCommerceAccountId(), _serviceContext);

		return commerceDiscount;
	}

	private CommerceDiscount _addAccountGroupDiscount(
			long groupId, String level, long cpDefinitionId)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				groupId, BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				level, CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinitionId);

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			_commerceDiscountCommerceAccountGroupRelLocalService.
				addCommerceDiscountCommerceAccountGroupRel(
					commerceDiscount.getCommerceDiscountId(),
					commerceAccountGroupId, _serviceContext);
		}

		return commerceDiscount;
	}

	private CommerceDiscount _addAccountGroupOrderDiscount(
			long groupId, String type)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				groupId, RandomTestUtil.randomDouble(), type, null);

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			_commerceDiscountCommerceAccountGroupRelLocalService.
				addCommerceDiscountCommerceAccountGroupRel(
					commerceDiscount.getCommerceDiscountId(),
					commerceAccountGroupId, _serviceContext);
		}

		return commerceDiscount;
	}

	private CommerceDiscount _addAccountOrderDiscount(long groupId, String type)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				groupId, RandomTestUtil.randomDouble(), type, null);

		_commerceDiscountAccountRelLocalService.addCommerceDiscountAccountRel(
			commerceDiscount.getCommerceDiscountId(),
			_commerceAccount.getCommerceAccountId(), _serviceContext);

		return commerceDiscount;
	}

	private CommerceDiscount _addChannelDiscount(
			long groupId, String level, long cpDefinitionId)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addPercentageCommerceDiscount(
				groupId, BigDecimal.valueOf(RandomTestUtil.randomDouble()),
				level, CommerceDiscountConstants.TARGET_PRODUCT,
				cpDefinitionId);

		_commerceChannelRelLocalService.addCommerceChannelRel(
			CommerceDiscount.class.getName(),
			commerceDiscount.getCommerceDiscountId(),
			_commerceChannel.getCommerceChannelId(), _serviceContext);

		return commerceDiscount;
	}

	private CommerceDiscount _addChannelOrderDiscount(long groupId, String type)
		throws Exception {

		CommerceDiscount commerceDiscount =
			CommerceDiscountTestUtil.addFixedCommerceDiscount(
				groupId, RandomTestUtil.randomDouble(), type, null);

		_commerceChannelRelLocalService.addCommerceChannelRel(
			CommerceDiscount.class.getName(),
			commerceDiscount.getCommerceDiscountId(),
			_commerceChannel.getCommerceChannelId(), _serviceContext);

		return commerceDiscount;
	}

	private long[] _getCommerceAccoutGroupIds() {
		List<CommerceAccountGroup> commerceAccountGroups =
			_commerceAccountGroupLocalService.
				getCommerceAccountGroupsByCommerceAccountId(
					_commerceAccount.getCommerceAccountId());

		Stream<CommerceAccountGroup> stream = commerceAccountGroups.stream();

		return stream.mapToLong(
			CommerceAccountGroup::getCommerceAccountGroupId
		).toArray();
	}

	private void _updateProperties(String key, int value)
		throws ConfigurationException {

		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(key, value);

		_configurationProvider.saveSystemConfiguration(
			CommercePricingConfiguration.class, properties);
	}

	private CommerceAccount _commerceAccount;
	private CommerceAccountGroup _commerceAccountGroup;

	@Inject
	private CommerceAccountGroupLocalService _commerceAccountGroupLocalService;

	@Inject
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@DeleteAfterTestRun
	private CommerceChannel _commerceChannel;

	@Inject
	private CommerceChannelRelLocalService _commerceChannelRelLocalService;

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommerceDiscountAccountRelLocalService
		_commerceDiscountAccountRelLocalService;

	@Inject
	private CommerceDiscountCommerceAccountGroupRelLocalService
		_commerceDiscountCommerceAccountGroupRelLocalService;

	@Inject
	private CommerceDiscountDiscovery _commerceDiscountDiscovery;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	private List<CommerceOrder> _commerceOrders;
	private CommercePricingConfiguration _commercePricingConfiguration;

	@DeleteAfterTestRun
	private Company _company;

	@Inject
	private ConfigurationProvider _configurationProvider;

	@DeleteAfterTestRun
	private Group _group;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}