/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mail.reader.web.portlet.action;

import com.liferay.mail.reader.model.Account;
import com.liferay.mail.reader.service.AccountLocalService;
import com.liferay.mail.reader.web.util.MailManager;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Scott Lee
 * @author Peter Fellwock
 */
@Component(
	enabled = false, immediate = true, property = "key=login.events.post",
	service = LifecycleAction.class
)
public class LoginPostAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			initiateSynchronization(request);
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	protected void initiateSynchronization(HttpServletRequest request)
		throws PortalException {

		long userId = _portal.getUserId(request);

		if (userId <= 0) {
			return;
		}

		List<Account> accounts = _accountLocalService.getAccounts(userId);

		if (accounts.isEmpty()) {
			return;
		}

		MailManager mailManager = MailManager.getInstance(request);

		for (Account account : accounts) {
			mailManager.synchronizeAccount(account.getAccountId());
		}
	}

	@Reference
	private AccountLocalService _accountLocalService;

	@Reference
	private Portal _portal;

}