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

package com.liferay.headless.commerce.admin.pricing.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;

import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.model.CommerceDiscountCommerceAccountGroupRel;
import com.liferay.commerce.discount.service.CommerceDiscountCommerceAccountGroupRelService;
import com.liferay.commerce.discount.service.persistence.CommerceDiscountCommerceAccountGroupRelUtil;
import com.liferay.headless.commerce.admin.pricing.dto.v2_0.DiscountAccountGroup;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andrea Sbarra
 */
@Ignore
@RunWith(Arquillian.class)
public class DiscountAccountGroupResourceTest
	extends BaseDiscountAccountGroupResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_user = UserTestUtil.addUser(testCompany);

		_serviceContext = ServiceContextTestUtil.getServiceContext(
			testCompany.getCompanyId(), testGroup.getGroupId(),
			_user.getUserId());

		Iterator<CommerceDiscountCommerceAccountGroupRel> iterator = _commerceDiscountCommerceAccountGroupRel.iterator();

		while (iterator.hasNext()) {
			CommerceDiscountCommerceAccountGroupRel commerceDiscountCommerceAccountGroupRelIterator = iterator.next();

			CommerceDiscountCommerceAccountGroupRel commerceDiscountCommerceAccountGroupRel =
				_commerceDiscountCommerceAccountGroupRelService. fetchCommerceDiscountCommerceAccountGroupRel(
					commerceDiscountCommerceAccountGroupRelIterator.getCommerceDiscountCommerceAccountGroupRelId());

			if (commerceChannel1 != null) {
				CommerceChannelLocalServiceUtil.deleteCommerceChannel(
					commerceChannel1.getCommerceChannelId());
			}

			iterator.remove();
		}
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		Iterator<CommerceChannel> iterator = _commerceChannels.iterator();

		while (iterator.hasNext()) {
			CommerceChannel commerceChannel = iterator.next();

			CommerceChannel commerceChannel1 =
				CommerceChannelLocalServiceUtil.fetchCommerceChannel(
					commerceChannel.getCommerceChannelId());

			if (commerceChannel1 != null) {
				CommerceChannelLocalServiceUtil.deleteCommerceChannel(
					commerceChannel1.getCommerceChannelId());
			}

			iterator.remove();
		}
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"currency", "name", "siteGroupId", "type"};
	}

	protected Channel randomPatchChannel() throws Exception {
		return new Channel() {
			{
				currency = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	@Override
	protected Channel testDeleteChannel_addChannel() throws Exception {
		return _addChannel(randomChannel());
	}

	@Override
	protected Channel testGetChannel_addChannel() throws Exception {
		return _addChannel(randomChannel());
	}

	@Override
	protected Channel testGetChannelsPage_addChannel(Channel channel)
		throws Exception {

		return _addChannel(channel);
	}

	@Override
	protected Channel testGraphQLChannel_addChannel() throws Exception {
		return _addChannel(randomChannel());
	}

	@Override
	protected Channel testPatchChannel_addChannel() throws Exception {
		return _addChannel(randomChannel());
	}

	@Override
	protected Channel testPostChannel_addChannel(Channel channel)
		throws Exception {

		return _addChannel(channel);
	}

	@Override
	protected Channel testPutChannel_addChannel() throws Exception {
		return _addChannel(randomChannel());
	}

	private Channel _addChannel(Channel channel) throws PortalException {
		CommerceChannel commerceChannel =
			CommerceChannelLocalServiceUtil.addCommerceChannel(
				channel.getSiteGroupId(), channel.getName(), channel.getType(),
				null, channel.getCurrency(), StringPool.BLANK, _serviceContext);

		_commerceChannels.add(commerceChannel);

		return _toChannel(commerceChannel);
	}

	private DiscountAccountGroup _toDiscountAccountGroup(
		CommerceDiscountCommerceAccountGroupRel
			commerceDiscountCommerceAccountGroupRel) throws PortalException {

		CommerceAccountGroup commerceAccountGroup =
			commerceDiscountCommerceAccountGroupRel.getCommerceAccountGroup();
		CommerceDiscount commerceDiscount =
			commerceDiscountCommerceAccountGroupRel.getCommerceDiscount();

		return new DiscountAccountGroup() {
			{
				accountGroupExternalReferenceCode =
					commerceAccountGroup.getExternalReferenceCode();
				accountGroupId =
					commerceAccountGroup.getCommerceAccountGroupId();
				accountGroupName = commerceAccountGroup.getName();
				discountExternalReferenceCode =
					commerceDiscount.getExternalReferenceCode();
				discountId = commerceDiscount.getCommerceDiscountId();
				id =
					commerceDiscountCommerceAccountGroupRel.
						getCommerceDiscountCommerceAccountGroupRelId();
			}
		};
	}

	private final List<CommerceDiscountCommerceAccountGroupRel> _commerceDiscountCommerceAccountGroupRel = new ArrayList<>();
	private ServiceContext _serviceContext;
	private CommerceDiscountCommerceAccountGroupRelService
		_commerceDiscountCommerceAccountGroupRelService;
	private User _user;
}