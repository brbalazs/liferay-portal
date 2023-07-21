/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.my.subscriptions.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.subscription.service.SubscriptionLocalServiceUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class MySubscriptionsManagementToolbarDisplayContext {

	public MySubscriptionsManagementToolbarDisplayContext(
		HttpServletRequest request,
		LiferayPortletResponse liferayPortletResponse, User user) {

		_request = request;
		_liferayPortletResponse = liferayPortletResponse;
		_user = user;

		_totalItems = SubscriptionLocalServiceUtil.getUserSubscriptionsCount(
			user.getUserId());
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData("action", "unsubscribe");
						dropdownItem.setIcon("times");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "unsubscribe"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public int getTotalItems() {
		return _totalItems;
	}

	public boolean isDisabled() {
		if (_totalItems <= 0) {
			return true;
		}

		return false;
	}

	public boolean isSelectable() {
		return true;
	}

	public boolean isShowSearch() {
		return false;
	}

	private final LiferayPortletResponse _liferayPortletResponse;
	private final HttpServletRequest _request;
	private final int _totalItems;
	private final User _user;

}