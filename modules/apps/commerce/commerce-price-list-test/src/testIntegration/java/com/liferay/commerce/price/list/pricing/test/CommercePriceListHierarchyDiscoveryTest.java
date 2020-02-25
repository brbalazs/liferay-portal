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

package com.liferay.commerce.price.list.pricing.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupCommerceAccountRelLocalServiceUtil;
import com.liferay.commerce.account.service.CommerceAccountGroupLocalService;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.price.list.constants.CommercePriceListTypeKeys;
import com.liferay.commerce.price.list.discovery.CommercePriceListDiscovery;
import com.liferay.commerce.price.list.model.CommercePriceEntry;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.model.CommercePriceListAccountRel;
import com.liferay.commerce.price.list.model.CommercePriceListChannelRel;
import com.liferay.commerce.price.list.model.CommercePriceListCommerceAccountGroupRel;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalServiceUtil;
import com.liferay.commerce.price.list.service.CommercePriceListAccountRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListChannelRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListCommerceAccountGroupRelLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalServiceUtil;
import com.liferay.commerce.price.list.test.util.CommercePriceListTestUtil;
import com.liferay.commerce.pricing.configuration.CommercePricingConfiguration;
import com.liferay.commerce.pricing.constants.CommercePricingConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import org.frutilla.FrutillaRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Riccardo Alberti
 */
@RunWith(Arquillian.class)
public class CommercePriceListHierarchyDiscoveryTest {

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

		_commerceAccount =
			_commerceAccountLocalService.getPersonalCommerceAccount(
				_user.getUserId());

		_commerceAccountGroup =
			_commerceAccountGroupLocalService.addCommerceAccountGroup(
				_company.getCompanyId(), RandomTestUtil.randomString(), 0,
				false, null, _serviceContext);

		CommerceAccountGroupCommerceAccountRelLocalServiceUtil.
			addCommerceAccountGroupCommerceAccountRel(
				_commerceAccountGroup.getCommerceAccountGroupId(),
				_commerceAccount.getCommerceAccountId(), _serviceContext);

		_commerceChannel = CommerceTestUtil.addCommerceChannel();
	}

	@Test
	public void testCreateCatalogBasePriceList() throws Exception {
		frutillaRule.scenario(
			"A price list is created and set as base pricelist for a catalog"
		).given(
			"A catalog"
		).when(
			"A price list is created with flag catalog base price list true"
		).then(
			"The price list is the default price list of the catalog"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				catalog.getGroupId(), true, _TYPE, 0.0);

		CommercePriceList discoveredPriceList =
			_commercePriceListDiscovery.getCommercePriceList(
				catalog.getGroupId(), 0, 0, null, _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());
	}

	@Test
	public void testRetrieveAccountAndChannelPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account and a channel as qualifier i " +
				"shall be able to retrieve it"
		).given(
			"A catalog with a price list qualified for an account and a channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account and channel"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList = _addAccountAndChannelPriceList(
			catalog.getGroupId());

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountAndChannelId(
					catalog.getGroupId(), _TYPE,
					_commerceAccount.getCommerceAccountId(),
					_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		CommercePriceListAccountRel commercePriceListAccountRel =
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNotNull(commercePriceListAccountRel);

		CommercePriceListChannelRel commercePriceListChannelRel =
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNotNull(commercePriceListChannelRel);
	}

	@Test
	public void testRetrieveAccountGroupsAndChannelPriceList()
		throws Exception {

		frutillaRule.scenario(
			"When a price list has account groups and a channel as qualifier " +
				"i shall be able to retrieve it"
		).given(
			"A catalog with a price list qualified for a account groups and " +
				"a channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account groups and channel"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			_addAccountGroupAndChannelPriceList(catalog.getGroupId());

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountGroupsAndChannelId(
					catalog.getGroupId(), _TYPE, _getCommerceAccoutGroupIds(),
					_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			CommercePriceListCommerceAccountGroupRel
				commercePriceListCommerceAccountGroupRel =
					_commercePriceListCommerceAccountGroupRelLocalService.
						fetchCommercePriceListCommerceAccountGroupRel(
							retrievedPriceList.getCommercePriceListId(),
							commerceAccountGroupId);

			Assert.assertNotNull(commercePriceListCommerceAccountGroupRel);
		}

		CommercePriceListChannelRel commercePriceListChannelRel =
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNotNull(commercePriceListChannelRel);
	}

	@Test
	public void testRetrieveAccountGroupsPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account group as qualifier i shall be " +
				"able to retrieve it"
		).given(
			"A catalog with a price list qualified for an account group"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account group"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		long[] commerceAccountGroupIds = {
			_commerceAccountGroup.getCommerceAccountGroupId()
		};

		CommercePriceList commercePriceList = _addAccountGroupPriceList(
			catalog.getGroupId());

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.
				getCommercePriceListByAccountGroupIds(
					catalog.getGroupId(), _TYPE, commerceAccountGroupIds);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		CommercePriceListCommerceAccountGroupRel
			commercePriceListCommerceAccountGroupRel =
				_commercePriceListCommerceAccountGroupRelLocalService.
					fetchCommercePriceListCommerceAccountGroupRel(
						retrievedPriceList.getCommercePriceListId(),
						_commerceAccountGroup.getCommerceAccountGroupId());

		Assert.assertNotNull(commercePriceListCommerceAccountGroupRel);
	}

	@Test
	public void testRetrieveAccountPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has an account as qualifier i shall be able " +
				"to retrieve it"
		).given(
			"A catalog with a price list qualified for an account"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the account"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList = _addAccountPriceList(
			catalog.getGroupId());

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByAccountId(
				catalog.getGroupId(), _TYPE,
				_commerceAccount.getCommerceAccountId());

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		CommercePriceListAccountRel commercePriceListAccountRel =
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNotNull(commercePriceListAccountRel);
	}

	@Test
	public void testRetrieveChannelPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has a channel as qualifier i shall be able to " +
				"retrieve it"
		).given(
			"A catalog with a price list qualified for an channel"
		).when(
			"The price list is discovered"
		).then(
			"The price list is qualified for the channel"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList = _addChannelPriceList(
			catalog.getGroupId());

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByChannelId(
				catalog.getGroupId(), _TYPE,
				_commerceChannel.getCommerceChannelId());

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		CommercePriceListChannelRel commercePriceListChannelRel =
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNotNull(commercePriceListChannelRel);
	}

	@Test
	public void testRetrieveCorrectPriceListByHierarchy() throws Exception {
		frutillaRule.scenario(
			"When multiple price list are defined for the same catalog the " +
				"highest in the hierarchy shall be taken"
		).given(
			"A catalog with multiple price lists"
		).when(
			"The price list is discovered"
		).then(
			"The price list with highest rank is retrieved"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commerceUnqualifiedPriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				catalog.getGroupId(), false, _TYPE, 1.0);

		CommercePriceList discoveredPriceList =
			_commercePriceListDiscovery.getCommercePriceList(
				catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
				_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceUnqualifiedPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		CommercePriceList commerceChannelPriceList = _addChannelPriceList(
			catalog.getGroupId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceChannelPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		CommercePriceList commerceAccountGroupPriceList =
			_addAccountGroupPriceList(catalog.getGroupId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceAccountGroupPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		CommercePriceList commerceAccountGroupAndChannelPriceList =
			_addAccountGroupAndChannelPriceList(catalog.getGroupId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceAccountGroupAndChannelPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		CommercePriceList commerceAccountPriceList = _addAccountPriceList(
			catalog.getGroupId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceAccountPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());

		CommercePriceList commerceAccountAndChannelPriceList =
			_addAccountAndChannelPriceList(catalog.getGroupId());

		discoveredPriceList = _commercePriceListDiscovery.getCommercePriceList(
			catalog.getGroupId(), _commerceAccount.getCommerceAccountId(),
			_commerceChannel.getCommerceChannelId(), null, _TYPE);

		Assert.assertEquals(
			commerceAccountAndChannelPriceList.getCommercePriceListId(),
			discoveredPriceList.getCommercePriceListId());
	}

	@Test
	public void testRetrieveUnqualifiedPriceList() throws Exception {
		frutillaRule.scenario(
			"When a price list has no qualifiers i shall be able to retrieve it"
		).given(
			"A catalog with a price list with no qualifiers"
		).when(
			"The price list is discovered"
		).then(
			"The price list has no qualifiers"
		);

		CommerceCatalog catalog =
			_commerceCatalogLocalService.addCommerceCatalog(
				RandomTestUtil.randomString(), _commerceCurrency.getCode(),
				LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				catalog.getGroupId(), false, _TYPE, 1.0);

		CommercePriceList retrievedPriceList =
			_commercePriceListLocalService.getCommercePriceListByUnqualified(
				catalog.getGroupId(), _TYPE);

		Assert.assertEquals(
			commercePriceList.getCommercePriceListId(),
			retrievedPriceList.getCommercePriceListId());

		CommercePriceListAccountRel commercePriceListAccountRel =
			_commercePriceListAccountRelLocalService.
				fetchCommercePriceListAccountRel(
					_commerceAccount.getCommerceAccountId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNull(commercePriceListAccountRel);

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			CommercePriceListCommerceAccountGroupRel
				commercePriceListCommerceAccountGroupRel =
					_commercePriceListCommerceAccountGroupRelLocalService.
						fetchCommercePriceListCommerceAccountGroupRel(
							retrievedPriceList.getCommercePriceListId(),
							_commerceAccountGroup.getCommerceAccountGroupId());

			Assert.assertNull(commercePriceListCommerceAccountGroupRel);
		}

		CommercePriceListChannelRel commercePriceListChannelRel =
			_commercePriceListChannelRelLocalService.
				fetchCommercePriceListChannelRel(
					_commerceChannel.getCommerceChannelId(),
					retrievedPriceList.getCommercePriceListId());

		Assert.assertNull(commercePriceListChannelRel);
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();


	private CommercePriceList _addAccountAndChannelPriceList(long groupId)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				groupId, false, _TYPE, 1.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0, _serviceContext);

		_commercePriceListChannelRelLocalService.addCommercePriceListChannelRel(
			commercePriceList.getCommercePriceListId(),
			_commerceChannel.getCommerceChannelId(), 0, _serviceContext);

		return commercePriceList;
	}

	private CommercePriceList _addAccountGroupAndChannelPriceList(long groupId)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				groupId, false, _TYPE, 1.0);

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			_commercePriceListCommerceAccountGroupRelLocalService.
				addCommercePriceListCommerceAccountGroupRel(
					commercePriceList.getCommercePriceListId(),
					commerceAccountGroupId, 0, _serviceContext);
		}

		_commercePriceListChannelRelLocalService.addCommercePriceListChannelRel(
			commercePriceList.getCommercePriceListId(),
			_commerceChannel.getCommerceChannelId(), 0, _serviceContext);

		return commercePriceList;
	}

	private CommercePriceList _addAccountGroupPriceList(long groupId)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				groupId, false, _TYPE, 1.0);

		long[] commerceAccountGroupIds = _getCommerceAccoutGroupIds();

		for (long commerceAccountGroupId : commerceAccountGroupIds) {
			_commercePriceListCommerceAccountGroupRelLocalService.
				addCommercePriceListCommerceAccountGroupRel(
					commercePriceList.getCommercePriceListId(),
					commerceAccountGroupId, 0, _serviceContext);
		}

		return commercePriceList;
	}

	private CommercePriceList _addAccountPriceList(long groupId)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				groupId, false, _TYPE, 1.0);

		_commercePriceListAccountRelLocalService.addCommercePriceListAccountRel(
			commercePriceList.getCommercePriceListId(),
			_commerceAccount.getCommerceAccountId(), 0, _serviceContext);

		return commercePriceList;
	}

	private CommercePriceList _addChannelPriceList(long groupId)
		throws Exception {

		CommercePriceList commercePriceList =
			CommercePriceListTestUtil.addCommercePriceList(
				groupId, false, _TYPE, 1.0);

		_commercePriceListChannelRelLocalService.addCommercePriceListChannelRel(
			commercePriceList.getCommercePriceListId(),
			_commerceChannel.getCommerceChannelId(), 0, _serviceContext);

		return commercePriceList;
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

	private static final String _TYPE =
		CommercePriceListTypeKeys.TYPE_PRICE_LIST;

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

	private CommerceCurrency _commerceCurrency;

	@Inject
	private CommercePriceListAccountRelLocalService
		_commercePriceListAccountRelLocalService;

	@Inject
	private CommercePriceListChannelRelLocalService
		_commercePriceListChannelRelLocalService;

	@Inject
	private CommercePriceListCommerceAccountGroupRelLocalService
		_commercePriceListCommerceAccountGroupRelLocalService;

	@Inject(filter = "commerce.price.list.discovery.key=" + CommercePricingConstants.ORDER_BY_HIERARCHY)
	private CommercePriceListDiscovery _commercePriceListDiscovery;

	@Inject
	private CommercePriceListLocalService _commercePriceListLocalService;

	@DeleteAfterTestRun
	private Company _company;

	private ServiceContext _serviceContext;

	@DeleteAfterTestRun
	private User _user;

}