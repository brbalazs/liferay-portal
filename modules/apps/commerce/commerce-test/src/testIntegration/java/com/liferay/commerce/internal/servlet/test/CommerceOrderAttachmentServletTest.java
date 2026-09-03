/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.internal.servlet.test;

import com.liferay.account.model.AccountEntry;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.account.test.util.CommerceAccountTestUtil;
import com.liferay.commerce.constants.CommerceOrderActionKeys;
import com.liferay.commerce.constants.CommerceOrderAttachmentConstants;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.test.util.CommerceCurrencyTestUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderAttachment;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.service.CommerceOrderAttachmentLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.test.util.CommerceOrderAttachmentTestUtil;
import com.liferay.commerce.test.util.CommerceTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Balazs Breier
 */
@FeatureFlag("LPD-6252")
@RunWith(Arquillian.class)
public class CommerceOrderAttachmentServletTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		CommerceOrderAttachmentTestUtil.initialize(getClass());

		_group = GroupTestUtil.addGroup();

		_commerceCurrency = CommerceCurrencyTestUtil.addCommerceCurrency(
			_group.getCompanyId());

		_commerceChannel = CommerceTestUtil.addCommerceChannel(
			_group.getGroupId(), _commerceCurrency.getCode());

		_accountEntry = CommerceAccountTestUtil.addPersonAccountEntry(
			TestPropsValues.getUserId(),
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		_commerceOrder = _commerceOrderLocalService.addCommerceOrder(
			TestPropsValues.getUserId(), _commerceChannel.getGroupId(),
			_accountEntry.getAccountEntryId(), _commerceCurrency.getCode(), 0);

		_role = _roleLocalService.addRole(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(), null, 0,
			RandomTestUtil.randomString(), null, null,
			RoleConstants.TYPE_REGULAR, null,
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId()));

		_user = UserTestUtil.addUser();

		_roleLocalService.addUserRole(_user.getUserId(), _role);
	}

	@Test
	public void testDoGet() throws Exception {

		// A path that holds no attachment ID

		Assert.assertEquals(
			HttpServletResponse.SC_NOT_FOUND,
			_getStatus(RandomTestUtil.randomString(), _user));
		Assert.assertEquals(
			HttpServletResponse.SC_NOT_FOUND,
			_getStatus(
				RandomTestUtil.randomLong() + "/" + RandomTestUtil.randomLong(),
				_user));

		CommerceOrderAttachment commerceOrderAttachment =
			_commerceOrderAttachmentLocalService.addCommerceOrderAttachment(
				RandomTestUtil.randomString(), _user.getUserId(),
				_commerceOrder.getCommerceOrderId(),
				RandomTestUtil.nextDouble(), false,
				RandomTestUtil.randomString(), "invoice",
				RandomTestUtil.randomString(),
				new ByteArrayInputStream(_CONTENT.getBytes()));

		long commerceOrderAttachmentId =
			commerceOrderAttachment.getCommerceOrderAttachmentId();

		// A user who cannot view the order, and an attachment that does not
		// exist, are answered alike so that the response does not tell the
		// caller which attachment IDs exist

		Assert.assertEquals(
			HttpServletResponse.SC_FORBIDDEN,
			_getStatus(String.valueOf(commerceOrderAttachmentId), _user));

		Assert.assertEquals(
			HttpServletResponse.SC_FORBIDDEN,
			_getStatus(String.valueOf(RandomTestUtil.randomLong()), _user));

		// A user who can view the order

		_setResourcePermissions(CommerceOrderActionKeys.MANAGE_COMMERCE_ORDERS);

		MockHttpServletResponse mockHttpServletResponse = _service(
			String.valueOf(commerceOrderAttachmentId), _user);

		Assert.assertEquals(
			HttpServletResponse.SC_OK, mockHttpServletResponse.getStatus());
		Assert.assertEquals(
			_CONTENT, mockHttpServletResponse.getContentAsString());
		Assert.assertTrue(
			StringUtil.startsWith(
				mockHttpServletResponse.getHeader(
					HttpHeaders.CONTENT_DISPOSITION),
				HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT));

		// A restricted attachment, which the document library URL this servlet
		// replaced handed out on the order permission alone

		commerceOrderAttachment.setRestricted(true);

		_commerceOrderAttachmentLocalService.updateCommerceOrderAttachment(
			commerceOrderAttachment);

		Assert.assertEquals(
			HttpServletResponse.SC_FORBIDDEN,
			_getStatus(String.valueOf(commerceOrderAttachmentId), _user));

		_setResourcePermissions(
			CommerceOrderActionKeys.MANAGE_COMMERCE_ORDERS,
			CommerceOrderActionKeys.VIEW_RESTRICTED_COMMERCE_ORDER_ATTACHMENTS);

		Assert.assertEquals(
			HttpServletResponse.SC_OK,
			_getStatus(String.valueOf(commerceOrderAttachmentId), _user));

		// A request that carries no credentials

		Assert.assertEquals(
			HttpServletResponse.SC_UNAUTHORIZED,
			_getStatus(String.valueOf(commerceOrderAttachmentId), null));
	}

	private int _getStatus(String pathInfo, User user) throws Exception {
		MockHttpServletResponse mockHttpServletResponse = _service(
			pathInfo, user);

		return mockHttpServletResponse.getStatus();
	}

	private MockHttpServletResponse _service(String pathInfo, User user)
		throws Exception {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(
				"GET",
				StringBundler.concat(
					_portal.getPathModule(), StringPool.SLASH,
					CommerceOrderAttachmentConstants.SERVLET_PATH,
					StringPool.SLASH, pathInfo));

		mockHttpServletRequest.setAttribute(
			WebKeys.COMPANY_ID, _group.getCompanyId());

		if (user != null) {
			mockHttpServletRequest.setAttribute(WebKeys.USER, user);
		}

		mockHttpServletRequest.setPathInfo(StringPool.SLASH + pathInfo);

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_servlet.service(mockHttpServletRequest, mockHttpServletResponse);

		return mockHttpServletResponse;
	}

	private void _setResourcePermissions(String... actionIds) throws Exception {
		_resourcePermissionLocalService.setResourcePermissions(
			_commerceOrder.getCompanyId(), CommerceOrderConstants.RESOURCE_NAME,
			ResourceConstants.SCOPE_GROUP,
			String.valueOf(_accountEntry.getAccountEntryGroupId()),
			_role.getRoleId(), actionIds);
	}

	private static final String _CONTENT = "Liferay";

	private AccountEntry _accountEntry;
	private CommerceChannel _commerceChannel;
	private CommerceCurrency _commerceCurrency;
	private CommerceOrder _commerceOrder;

	@Inject
	private CommerceOrderAttachmentLocalService
		_commerceOrderAttachmentLocalService;

	@Inject
	private CommerceOrderLocalService _commerceOrderLocalService;

	private Group _group;

	@Inject
	private Portal _portal;

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	private Role _role;

	@Inject
	private RoleLocalService _roleLocalService;

	@Inject(
		filter = "osgi.http.whiteboard.servlet.name=com.liferay.commerce.internal.servlet.CommerceOrderAttachmentServlet"
	)
	private Servlet _servlet;

	private User _user;

}