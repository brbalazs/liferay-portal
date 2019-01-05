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

package com.liferay.commerce.account.web.internal.portlet.action;

import com.liferay.commerce.account.constants.CommerceAccountPortletKeys;
import com.liferay.commerce.account.exception.CommerceAccountNameException;
import com.liferay.commerce.account.exception.DuplicateCommerceAccountException;
import com.liferay.commerce.account.exception.NoSuchAccountException;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.model.CommerceAccountUserRel;
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.commerce.account.service.CommerceAccountUserRelService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.concurrent.Callable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CommerceAccountPortletKeys.COMMERCE_ACCOUNT,
		"mvc.command.name=editCommerceAccount"
	},
	service = MVCActionCommand.class
)
public class EditCommerceAccountMVCActionCommand extends BaseMVCActionCommand {

	protected void addAdminUsers(
			long commerceAccountId, ActionRequest actionRequest)
		throws PortalException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAccountUserRel.class.getName(), actionRequest);

		Role accountAdministratorRole = _roleLocalService.getRole(
			serviceContext.getCompanyId(), "Account Administrator");

		if (accountAdministratorRole == null) {
			return;
		}

		String[] emailAddresses = ParamUtil.getStringValues(
			actionRequest, "emailAddresses");

		_commerceAccountUserRelService.addCommerceAccountUserRels(
			commerceAccountId, emailAddresses,
			new long[] {accountAdministratorRole.getRoleId()}, serviceContext);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				Callable<CommerceAccount> commerceAccountCallable =
					new CommerceAccountCallable(actionRequest);

				TransactionInvokerUtil.invoke(
					_transactionConfig, commerceAccountCallable);
			}
		}
		catch (Throwable t) {
			if (t instanceof NoSuchAccountException ||
				t instanceof PrincipalException) {

				SessionErrors.add(actionRequest, t.getClass());

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");
			}
			else if (t instanceof CommerceAccountNameException ||
					 t instanceof DuplicateCommerceAccountException) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, t.getClass());
			}
			else {
				_log.error(t, t);
			}
		}

		hideDefaultSuccessMessage(actionRequest);
	}

	protected CommerceAccount updateCommerceAccount(ActionRequest actionRequest)
		throws Exception {

		long commerceAccountId = ParamUtil.getLong(
			actionRequest, "commerceAccountId");

		String name = ParamUtil.getString(actionRequest, "name");
		long parentCommerceAccountId = ParamUtil.getLong(
			actionRequest, "parentCommerceAccountId");
		boolean deleteLogo = ParamUtil.getBoolean(actionRequest, "deleteLogo");
		String email = ParamUtil.getString(actionRequest, "email");
		String taxId = ParamUtil.getString(actionRequest, "taxId");
		boolean active = ParamUtil.getBoolean(actionRequest, "active");
		String externalReferenceCode = ParamUtil.getString(
			actionRequest, "externalReferenceCode");

		byte[] logoBytes = null;

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId > 0) {
			FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

			logoBytes = FileUtil.getBytes(fileEntry.getContentStream());
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAccount.class.getName(), actionRequest);

		CommerceAccount commerceAccount;

		if ((commerceAccountId > 0) &&
			(commerceAccountId != parentCommerceAccountId)) {

			commerceAccount = _commerceAccountService.updateCommerceAccount(
				commerceAccountId, name, !deleteLogo, logoBytes, email, taxId,
				active, serviceContext);
		}
		else {
			commerceAccount = _commerceAccountService.addCommerceAccount(
				name, parentCommerceAccountId, email, taxId, active,
				externalReferenceCode, serviceContext);

			// Add administrator users

			addAdminUsers(
				commerceAccount.getCommerceAccountId(), actionRequest);
		}

		return commerceAccount;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditCommerceAccountMVCActionCommand.class);

	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private CommerceAccountUserRelService _commerceAccountUserRelService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	private class CommerceAccountCallable implements Callable<CommerceAccount> {

		@Override
		public CommerceAccount call() throws Exception {
			return updateCommerceAccount(_actionRequest);
		}

		private CommerceAccountCallable(ActionRequest actionRequest) {
			_actionRequest = actionRequest;
		}

		private final ActionRequest _actionRequest;

	}

}