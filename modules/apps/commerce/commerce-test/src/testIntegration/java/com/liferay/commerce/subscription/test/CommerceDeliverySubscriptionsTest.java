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

package com.liferay.commerce.subscription.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceSubscriptionEntryConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseItemLocalService;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceShipment;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.order.engine.CommerceOrderEngine;
import com.liferay.commerce.payment.engine.CommerceSubscriptionEngine;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelConstants;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceShipmentLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.commerce.subscription.CommerceSubscriptionEntryHelper;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Luca Pellizzon
 */
@RunWith(Arquillian.class)
public class CommerceDeliverySubscriptionsTest {

	@ClassRule
	@Rule
	public static AggregateTestRule aggregateTestRule = new AggregateTestRule(
		new LiferayIntegrationTestRule(), PermissionCheckerTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_user = UserTestUtil.addUser(_company);

		_group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(), 0);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		_commerceChannel = _commerceChannelLocalService.addCommerceChannel(
			_group.getGroupId(),
			_group.getName(serviceContext.getLanguageId()) + " Portal",
			CommerceChannelConstants.CHANNEL_TYPE_SITE, null, StringPool.BLANK,
			StringPool.BLANK, serviceContext);

		CommerceCurrency commerceCurrency =
			CommerceCurrencyTestUtil.addCommerceCurrency();

		_commerceOrder = CommerceTestUtil.addB2CCommerceOrder(
			_group.getGroupId(), _user.getUserId(),
			commerceCurrency.getCommerceCurrencyId());
	}

	@After
	public void tearDown() {
		_commerceInventoryWarehouseItemLocalService.
			deleteCommerceInventoryWarehouseItemsByCompanyId(
				_company.getCompanyId());
	}

	@Test
	public void testDeliveryAndPaymentSubscription() throws Exception {
		_commerceOrder = CommerceTestUtil.addCheckoutDetailsToUserOrder(
			_commerceOrder, _user.getUserId(), true, true);

		_commerceOrder = _commerceOrderEngine.checkoutCommerceOrder(
			_commerceOrder, _user.getUserId());

		_commerceOrderEngine.transitionCommerceOrder(
			_commerceOrder, CommerceOrderConstants.ORDER_STATUS_PENDING,
			_commerceOrder.getUserId());

		Thread.sleep(1000);

		List<CommerceSubscriptionEntry> commerceSubscriptionEntries =
			_commerceSubscriptionEntryLocalService.
				getCommerceSubscriptionEntries(
					_company.getCompanyId(), _commerceChannel.getGroupId(),
					_user.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null);

		Assert.assertEquals(
			commerceSubscriptionEntries.toString(), 1,
			commerceSubscriptionEntries.size());

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			commerceSubscriptionEntries.get(0);

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_ACTIVE,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.suspendRecurringPayment(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		_commerceSubscriptionEngine.suspendRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_SUSPENDED,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.activateRecurringPayment(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		_commerceSubscriptionEngine.activateRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_ACTIVE,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.cancelRecurringPayment(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		_commerceSubscriptionEngine.cancelRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_CANCELLED,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());
	}

	@Test
	public void testDeliverySubscription() throws Exception {
		_commerceOrder = CommerceTestUtil.addCheckoutDetailsToUserOrder(
			_commerceOrder, _user.getUserId(), false, true);

		_commerceOrder = _commerceOrderEngine.checkoutCommerceOrder(
			_commerceOrder, _user.getUserId());

		_commerceOrderEngine.transitionCommerceOrder(
			_commerceOrder, CommerceOrderConstants.ORDER_STATUS_PENDING,
			_commerceOrder.getUserId());

		Thread.sleep(1000);

		List<CommerceSubscriptionEntry> commerceSubscriptionEntries =
			_commerceSubscriptionEntryLocalService.
				getCommerceSubscriptionEntries(
					_company.getCompanyId(), _commerceChannel.getGroupId(),
					_user.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null);

		Assert.assertEquals(
			commerceSubscriptionEntries.toString(), 1,
			commerceSubscriptionEntries.size());

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			commerceSubscriptionEntries.get(0);

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_ACTIVE,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.suspendRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_SUSPENDED,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.activateRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_ACTIVE,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());

		_commerceSubscriptionEngine.cancelRecurringDelivery(
			commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		commerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUS_CANCELLED,
			commerceSubscriptionEntry.getDeliverySubscriptionStatus());
	}

	@Test
	public void testDeliverySubscriptionRenew() throws Exception {
		_commerceOrder = CommerceTestUtil.addCheckoutDetailsToUserOrder(
			_commerceOrder, _user.getUserId(), false, true);

		_commerceOrder = _commerceOrderEngine.checkoutCommerceOrder(
			_commerceOrder, _user.getUserId());

		_commerceOrderEngine.transitionCommerceOrder(
			_commerceOrder, CommerceOrderConstants.ORDER_STATUS_PENDING,
			_commerceOrder.getUserId());

		Thread.sleep(1000);

		List<CommerceSubscriptionEntry> commerceSubscriptionEntries =
			_commerceSubscriptionEntryLocalService.
				getCommerceSubscriptionEntries(
					_company.getCompanyId(), _commerceChannel.getGroupId(),
					_user.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null);

		Assert.assertEquals(
			commerceSubscriptionEntries.toString(), 1,
			commerceSubscriptionEntries.size());

		CommerceSubscriptionEntry commerceSubscriptionEntry =
			commerceSubscriptionEntries.get(0);

		// Set subscription entry to be renewable

		commerceSubscriptionEntry.setNextIterationDate(new Date());

		Thread.sleep(1000);

		_commerceSubscriptionEntryHelper.checkDeliverySubscriptionStatus(
			commerceSubscriptionEntry);

		CommerceSubscriptionEntry renewedCommerceSubscriptionEntry =
			_commerceSubscriptionEntryLocalService.getCommerceSubscriptionEntry(
				commerceSubscriptionEntry.getCommerceSubscriptionEntryId());

		Assert.assertEquals(
			commerceSubscriptionEntry.getDeliveryCurrentCycle() + 1,
			renewedCommerceSubscriptionEntry.getDeliveryCurrentCycle());

		long[] groupIds = {_commerceChannel.getGroupId()};

		List<CommerceShipment> commerceShipments =
			_commerceShipmentLocalService.getCommerceShipments(
				groupIds, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(
			commerceShipments.toString(), 2, commerceShipments.size());
	}

	private CommerceChannel _commerceChannel;

	@Inject
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Inject
	private CommerceInventoryWarehouseItemLocalService
		_commerceInventoryWarehouseItemLocalService;

	@DeleteAfterTestRun
	private CommerceOrder _commerceOrder;

	@Inject
	private CommerceOrderEngine _commerceOrderEngine;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Inject
	private CommerceShipmentLocalService _commerceShipmentLocalService;

	@Inject
	private CommerceSubscriptionEngine _commerceSubscriptionEngine;

	@Inject
	private CommerceSubscriptionEntryHelper _commerceSubscriptionEntryHelper;

	@Inject
	private CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;

	@DeleteAfterTestRun
	private Company _company;

	private Group _group;

	@DeleteAfterTestRun
	private User _user;

}