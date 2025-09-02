/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.internal.info.item.provider.test;

import com.liferay.account.model.AccountEntry;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.test.util.CommerceAccountTestUtil;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.ERCInfoItemIdentifier;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

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
public class CommerceOrderInfoItemObjectProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		AccountEntry accountEntry =
			CommerceAccountTestUtil.getPersonAccountEntry(
				TestPropsValues.getUserId());

		CommerceCurrency commerceCurrency =
			CommerceCurrencyTestUtil.addCommerceCurrency(_group.getCompanyId());

		CommerceChannel commerceChannel = CommerceTestUtil.addCommerceChannel(
			_group.getGroupId(), commerceCurrency.getCode());

		_commerceOrder = _commerceOrderLocalService.addCommerceOrder(
			TestPropsValues.getUserId(), commerceChannel.getGroupId(),
			accountEntry.getAccountEntryId(), commerceCurrency.getCode(), 0);
	}

	@Test
	public void testGetInfoItem() throws Exception {
		InfoItemObjectProvider<CommerceOrder> infoItemObjectProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemObjectProvider.class, CommerceOrder.class.getName());

		Group group = _groupLocalService.fetchGroup(
			_commerceOrder.getGroupId());

		Assert.assertEquals(
			_commerceOrder,
			infoItemObjectProvider.getInfoItem(
				group.getGroupId(),
				new ClassPKInfoItemIdentifier(
					_commerceOrder.getCommerceOrderId())));

		Assert.assertEquals(
			_commerceOrder,
			infoItemObjectProvider.getInfoItem(
				_group.getGroupId(),
				new ERCInfoItemIdentifier(
					_commerceOrder.getExternalReferenceCode())));
		Assert.assertEquals(
			_commerceOrder,
			infoItemObjectProvider.getInfoItem(
				RandomTestUtil.randomLong(),
				new ERCInfoItemIdentifier(
					_commerceOrder.getExternalReferenceCode(),
					_group.getExternalReferenceCode())));
	}

	private CommerceOrder _commerceOrder;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private InfoItemServiceRegistry _infoItemServiceRegistry;

}