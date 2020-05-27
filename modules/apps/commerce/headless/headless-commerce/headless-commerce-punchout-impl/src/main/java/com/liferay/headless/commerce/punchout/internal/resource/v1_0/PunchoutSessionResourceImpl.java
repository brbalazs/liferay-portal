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

package com.liferay.headless.commerce.punchout.internal.resource.v1_0;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountUserRel;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.account.service.CommerceAccountUserRelLocalService;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.commerce.punchout.dto.v1_0.Cart;
import com.liferay.headless.commerce.punchout.dto.v1_0.CartItem;
import com.liferay.headless.commerce.punchout.dto.v1_0.Group;
import com.liferay.headless.commerce.punchout.dto.v1_0.PunchoutSession;
import com.liferay.headless.commerce.punchout.dto.v1_0.User;
import com.liferay.headless.commerce.punchout.internal.configuration.PunchoutConfiguration;
import com.liferay.headless.commerce.punchout.internal.configuration.PunchoutConstants;
import com.liferay.headless.commerce.punchout.resource.v1_0.PunchoutSessionResource;
import com.liferay.oauth2.provider.punchout.PunchoutAccessTokenProvider;
import com.liferay.oauth2.provider.punchout.model.PunchoutAccessToken;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URLEncoder;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Jaclyn Ong
 */
@Component(
	configurationPid = "com.liferay.headless.commerce.punchout.internal.configuration.PunchoutConfiguration",
	properties = "OSGI-INF/liferay/rest/v1_0/punchout-session.properties",
	scope = ServiceScope.PROTOTYPE, service = PunchoutSessionResource.class
)
public class PunchoutSessionResourceImpl
	extends BasePunchoutSessionResourceImpl {

	@Override
	public PunchoutSession postPunchoutSessionRequest(
			@NotNull PunchoutSession punchoutSession)
		throws Exception {

		com.liferay.portal.kernel.model.Group buyerGroup = _fetchGroup(
			punchoutSession.getBuyerGroup());

		if (buyerGroup == null) {
			throw new NoSuchGroupException("No such group exists");
		}

		//todo: check if punchout enabled for the channel

		User buyerUser = punchoutSession.getBuyerUser();

		com.liferay.portal.kernel.model.User buyerUserInLiferay =
			_fetchOrCreateBuyerUser(buyerUser, buyerGroup.getGroupId());

		if (buyerUserInLiferay == null) {
			throw new InternalServerErrorException();
		}

		if (!_userBelongsToGroup(buyerGroup.getGroupId(), buyerUserInLiferay)) {
			throw new BadRequestException();
		}

		CommerceAccount businessCommerceAccount =
			_fetchOrCreateBusinessCommerceAccount(
				buyerUserInLiferay, buyerGroup.getGroupId());

		String punchoutSessionType = punchoutSession.getPunchoutSessionType();

		Cart cart = punchoutSession.getCart();

		if (punchoutSessionType.equalsIgnoreCase(_EDIT_REQUEST_TYPE) ||
			punchoutSessionType.equalsIgnoreCase(_INSPECT_REQUEST_TYPE)) {

			if (!_userBelongsToCart(
					buyerUserInLiferay.getUserId(), cart.getId())) {

				throw new BadRequestException();
			}

			_mergeCartItems(punchoutSession.getCart(), buyerGroup.getGroupId());
		}

		String punchoutStartURL = _getPunchoutStartURL();

		CommerceChannel commerceChannel = _fetchChannel(
			buyerGroup.getGroupId());

		PunchoutAccessToken punchoutAccessToken =
			_punchoutAccessTokenProvider.generatePunchoutAccessToken(
				buyerGroup.getGroupId(), commerceChannel.getCommerceChannelId(),
				businessCommerceAccount.getCommerceAccountId(),
				cart.getCurrencyCode(), buyerUserInLiferay.getEmailAddress(),
				punchoutSession.getPunchoutReturnURL());

		byte[] token = punchoutAccessToken.getToken();

		String tokenString = token.toString();

		punchoutStartURL +=
			StringPool.QUESTION + _PUNCHOUT_ACCESS_TOKEN_PARAMETER +
				URLEncoder.encode(tokenString, "UTF-8");

		punchoutSession.setPunchoutStartURL(punchoutStartURL);

		return punchoutSession;
	}

	@Reference(unbind = "-")
	protected void setConfigurationProvider(
		ConfigurationProvider configurationProvider) {

		_configurationProvider = configurationProvider;
	}

	private com.liferay.portal.kernel.model.User _addBuyerUser(
			long companyId, long groupId, String email, String firstName,
			String middleName, String lastName)
		throws Exception {

		if (Validator.isBlank(firstName) && Validator.isBlank(lastName)) {
			throw new BadRequestException(
				"User first and last name are required");
		}

		_checkAllowUserCreation(companyId, email);

		long creatorUserId = 0;
		boolean autoPassword = true;
		String password1 = StringPool.BLANK;
		String password2 = StringPool.BLANK;
		boolean autoScreenName = true;
		String screenName = StringPool.BLANK;
		String openId = StringPool.BLANK;
		Locale locale = LocaleUtil.getDefault();
		long prefixId = 0;
		long suffixId = 0;
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;
		String jobTitle = StringPool.BLANK;
		long[] groupIds = {groupId};
		long[] organizationIds = null;
		long[] userGroupIds = null;
		boolean sendEmail = true;
		long[] roleIds = null;

		com.liferay.portal.kernel.model.User user = _userLocalService.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, email, 0, openId, locale, firstName,
			middleName, lastName, prefixId, suffixId, false, birthdayMonth,
			birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds,
			roleIds, userGroupIds, sendEmail,
			_serviceContextHelper.getServiceContext(groupId));

		user = _userLocalService.updateLastLogin(
			user.getUserId(), user.getLoginIP());

		return _userLocalService.updatePasswordReset(user.getUserId(), false);
	}

	private void _checkAllowUserCreation(long companyId, String email)
		throws PortalException {

		Company company = _companyLocalService.getCompany(companyId);

		if (!company.isStrangers()) {
			throw new InternalServerErrorException();
		}

		if (company.hasCompanyMx(email) && !company.isStrangersWithMx()) {
			throw new UserEmailAddressException.MustNotUseCompanyMx(email);
		}
	}

	private CommerceAccount _fetchBusinessCommerceAccount(long userId)
		throws PortalException {

		List<CommerceAccount> businessCommerceAccounts =
			_commerceAccountLocalService.getUserCommerceAccounts(
				userId, 0L, CommerceAccountConstants.ACCOUNT_TYPE_BUSINESS,
				null, -1, -1);

		for (CommerceAccount commerceAccount : businessCommerceAccounts) {
			List<CommerceAccountUserRel> commerceAccountUserRels =
				commerceAccount.getCommerceAccountUserRels();

			for (CommerceAccountUserRel commerceAccountUserRel :
					commerceAccountUserRels) {

				com.liferay.portal.kernel.model.User commerceAccountUser =
					commerceAccountUserRel.getUser();

				if (commerceAccountUser.getUserId() == userId) {
					return commerceAccount;
				}
			}
		}

		return null;
	}

	private CommerceChannel _fetchChannel(long groupId) {
		return _commerceChannelLocalService.fetchCommerceChannelBySiteGroupId(
			groupId);
	}

	private com.liferay.portal.kernel.model.Group _fetchGroup(Group group) {
		return _groupLocalService.fetchGroup(
			contextCompany.getCompanyId(), group.getName());
	}

	private CommerceAccount _fetchOrCreateBusinessCommerceAccount(
			com.liferay.portal.kernel.model.User user, long groupId)
		throws PortalException {

		CommerceAccount businessCommerceAccount = _fetchBusinessCommerceAccount(
			user.getUserId());

		if (businessCommerceAccount != null) {
			return businessCommerceAccount;
		}

		businessCommerceAccount =
			_commerceAccountLocalService.addBusinessCommerceAccount(
				_BUSINESS_ACCOUNT_NAME_PREFIX + user.getFullName(),
				CommerceAccountConstants.DEFAULT_PARENT_ACCOUNT_ID,
				user.getEmailAddress(), StringPool.BLANK, true, null,
				new long[] {user.getUserId()}, null,
				_serviceContextHelper.getServiceContext(groupId));

		Role role = _roleLocalService.fetchRole(
			contextCompany.getCompanyId(),
			CommerceAccountConstants.ROLE_NAME_ACCOUNT_BUYER);

		long[] roleIds = {role.getRoleId()};

		_commerceAccountUserRelLocalService.addCommerceAccountUserRels(
			businessCommerceAccount.getCommerceAccountId(),
			new long[] {user.getUserId()}, null, roleIds,
			_serviceContextHelper.getServiceContext(groupId));

		return businessCommerceAccount;
	}

	private com.liferay.portal.kernel.model.User _fetchOrCreateBuyerUser(
			User user, long groupId)
		throws Exception {

		if (Validator.isBlank(user.getEmail())) {
			throw new BadRequestException("User email is required");
		}

		com.liferay.portal.kernel.model.User liferayUser =
			_userLocalService.fetchUserByEmailAddress(
				contextCompany.getCompanyId(), user.getEmail());

		if (liferayUser != null) {
			return liferayUser;
		}

		return _addBuyerUser(
			contextCompany.getCompanyId(), groupId, user.getEmail(),
			user.getFirstName(), user.getMiddleName(), user.getLastName());
	}

	private PunchoutConfiguration _getPunchoutConfiguration(long companyId) {
		try {
			return _configurationProvider.getConfiguration(
				PunchoutConfiguration.class,
				new CompanyServiceSettingsLocator(
					companyId, PunchoutConstants.SERVICE_NAME));
		}
		catch (ConfigurationException ce) {
			_log.error(
				"Unable to get punchout access token auto login configuration",
				ce);
		}

		return null;
	}

	private String _getPunchoutStartURL() {
		PunchoutConfiguration punchoutConfiguration = _getPunchoutConfiguration(
			contextCompany.getCompanyId());

		if (punchoutConfiguration == null) {
			return null;
		}

		return punchoutConfiguration.punchoutStartURL();
	}

	private void _mergeCartItems(Cart cart, long groupId)
		throws PortalException {

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.fetchCommerceOrder(cart.getId());

		if (commerceOrder == null) {
			return;
		}

		CartItem[] cartItems = cart.getCartItems();

		List<CommerceOrderItem> commerceOrderItems =
			_commerceOrderItemLocalService.getCommerceOrderItems(
				commerceOrder.getCommerceOrderId(), -1, -1);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceOrder.getGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		for (CartItem cartItem : cartItems) {
			if (!commerceOrderItems.isEmpty()) {
				boolean found = false;

				for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
					if (cartItem.getId() ==
							commerceOrderItem.getCommerceOrderItemId()) {

						found = true;

						_commerceOrderItemLocalService.updateCommerceOrderItem(
							commerceOrderItem.getCommerceOrderItemId(),
							cartItem.getQuantity(), commerceContext,
							_serviceContextHelper.getServiceContext(groupId));

						break;
					}
				}

				if (found) {
					continue;
				}
			}

			_commerceOrderItemLocalService.addCommerceOrderItem(
				commerceOrder.getCommerceOrderId(), cartItem.getSkuId(),
				cartItem.getQuantity(), cartItem.getShippedQuantity(), null,
				commerceContext,
				_serviceContextHelper.getServiceContext(groupId));
		}

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			if (cartItems.length > 0) {
				boolean found = false;

				for (CartItem cartItem : cartItems) {
					if (cartItem.getId() ==
							commerceOrderItem.getCommerceOrderItemId()) {

						found = true;

						break;
					}
				}

				if (found) {
					continue;
				}
			}

			_commerceOrderItemLocalService.deleteCommerceOrderItem(
				commerceOrderItem.getCommerceOrderItemId());
		}
	}

	private boolean _userBelongsToCart(long userId, long cartId) {
		CommerceOrder commerceOrder =
			_commerceOrderLocalService.fetchCommerceOrder(cartId);

		if (userId == commerceOrder.getUserId()) {
			return true;
		}

		return false;
	}

	private boolean _userBelongsToGroup(
		long groupId, com.liferay.portal.kernel.model.User user) {

		return ArrayUtil.contains(user.getGroupIds(), groupId);
	}

	private static final String _BUSINESS_ACCOUNT_NAME_PREFIX =
		"Business Account ";

	private static final String _EDIT_REQUEST_TYPE = "edit";

	private static final String _INSPECT_REQUEST_TYPE = "inspect";

	private static final String _PUNCHOUT_ACCESS_TOKEN_PARAMETER =
		"punchoutAccessToken=";

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutSessionResourceImpl.class);

	@Reference
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Reference
	private CommerceAccountUserRelLocalService
		_commerceAccountUserRelLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderItemLocalService _commerceOrderItemLocalService;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	private ConfigurationProvider _configurationProvider;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private PunchoutAccessTokenProvider _punchoutAccessTokenProvider;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UserLocalService _userLocalService;

}