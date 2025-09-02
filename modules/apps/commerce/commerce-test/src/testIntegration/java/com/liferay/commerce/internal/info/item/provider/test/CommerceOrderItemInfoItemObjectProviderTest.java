/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.internal.info.item.provider.test;

import com.liferay.account.model.AccountEntry;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.test.util.CommerceAccountTestUtil;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.test.util.CommerceInventoryTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.commerce.test.util.context.TestCommerceContext;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.math.BigDecimal;

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
public class CommerceOrderItemInfoItemObjectProviderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		User user = UserTestUtil.addUser();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), user.getUserId());

		AccountEntry accountEntry =
			CommerceAccountTestUtil.getPersonAccountEntry(user.getUserId());

		CommerceCurrency commerceCurrency =
			CommerceCurrencyTestUtil.addCommerceCurrency(_group.getCompanyId());

		CommerceChannel commerceChannel = CommerceTestUtil.addCommerceChannel(
			_group.getGroupId(), commerceCurrency.getCode());

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.addCommerceOrder(
				user.getUserId(), commerceChannel.getGroupId(),
				accountEntry.getAccountEntryId(), commerceCurrency.getCode(),
				0);

		CommerceContext commerceContext = new TestCommerceContext(
			null, commerceCurrency, commerceChannel, user, _group, null);

		CPInstance cpInstance = CPTestUtil.addCPInstance(_group.getGroupId());

		CommerceInventoryWarehouse commerceInventoryWarehouse =
			CommerceInventoryTestUtil.addCommerceInventoryWarehouse(
				ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		CommerceInventoryTestUtil.addCommerceInventoryWarehouseItem(
			user.getUserId(), commerceInventoryWarehouse, BigDecimal.valueOf(2),
			cpInstance.getSku(), StringPool.BLANK);

		CommerceTestUtil.addWarehouseCommerceChannelRel(
			commerceInventoryWarehouse.getCommerceInventoryWarehouseId(),
			commerceChannel.getCommerceChannelId());

		_commerceOrderItem =
			_commerceOrderItemLocalService.addCommerceOrderItem(
				user.getUserId(), commerceOrder.getCommerceOrderId(),
				cpInstance.getCPInstanceId(), null, BigDecimal.ONE, 0,
				BigDecimal.ZERO, StringPool.BLANK, commerceContext,
				serviceContext);
	}

	@Test
	public void testGetInfoItem() throws Exception {
		InfoItemObjectProvider<CommerceOrderItem> infoItemObjectProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemObjectProvider.class,
				CommerceOrderItem.class.getName());

		Group group = _groupLocalService.fetchGroup(
			_commerceOrderItem.getGroupId());

		Assert.assertEquals(
			_commerceOrderItem,
			infoItemObjectProvider.getInfoItem(
				group.getGroupId(),
				new ClassPKInfoItemIdentifier(
					_commerceOrderItem.getCommerceOrderItemId())));

		Assert.assertEquals(
			_commerceOrderItem,
			infoItemObjectProvider.getInfoItem(
				_group.getGroupId(),
				new ERCInfoItemIdentifier(
					_commerceOrderItem.getExternalReferenceCode())));
		Assert.assertEquals(
			_commerceOrderItem,
			infoItemObjectProvider.getInfoItem(
				RandomTestUtil.randomLong(),
				new ERCInfoItemIdentifier(
					_commerceOrderItem.getExternalReferenceCode(),
					_group.getExternalReferenceCode())));
	}

	private CommerceOrderItem _commerceOrderItem;

	@Inject
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private InfoItemServiceRegistry _infoItemServiceRegistry;

}