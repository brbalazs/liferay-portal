/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.account.web.internal.display.context;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.users.admin.configuration.UserFileUploadsConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceAccountUserDisplayContext
	extends BaseCommerceAccountDisplayContext {

	public CommerceAccountUserDisplayContext(
		CommerceAccountHelper commerceAccountHelper,
		CommerceAccountService commerceAccountService,
		HttpServletRequest httpServletRequest,
		ModelResourcePermission<CommerceAccount> modelResourcePermission,
		Portal portal,
		UserFileUploadsConfiguration userFileUploadsConfiguration,
		UserLocalService userLocalService) {

		super(
			commerceAccountHelper, commerceAccountService, httpServletRequest,
			modelResourcePermission, portal);

		_userLocalService = userLocalService;
		_userFileUploadsConfiguration = userFileUploadsConfiguration;
	}

	public User getSelectedUser() throws PortalException {
		long userId = ParamUtil.getLong(
			commerceAccountRequestHelper.getRequest(), "userId");

		if (userId > 0) {
			return _userLocalService.getUser(userId);
		}

		return portal.getUser(commerceAccountRequestHelper.getRequest());
	}

	public UserFileUploadsConfiguration getUserFileUploadsConfiguration() {
		return _userFileUploadsConfiguration;
	}

	private final UserFileUploadsConfiguration _userFileUploadsConfiguration;
	private final UserLocalService _userLocalService;

}