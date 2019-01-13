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
import com.liferay.commerce.account.service.CommerceAccountService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider.Action;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

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

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				CommerceAccount commerceAccount = updateCommerceAccount(
					actionRequest);

				String redirect = getSaveAndContinueRedirect(
					actionRequest, commerceAccount);

				sendRedirect(actionRequest, actionResponse, redirect);
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

	protected String getSaveAndContinueRedirect(
			ActionRequest actionRequest, CommerceAccount commerceAccount)
		throws PortalException {

		PortletURL managePortletURL = PortletProviderUtil.getPortletURL(
			actionRequest, CommerceAccount.class.getName(), Action.MANAGE);

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			actionRequest, CommerceAccount.class.getName(), Action.VIEW);

		portletURL.setParameter(
			"commerceAccountId",
			String.valueOf(commerceAccount.getCommerceAccountId()));

		portletURL.setParameter(
			PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL",
			managePortletURL.toString());

		return portletURL.toString();
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
		long[] userIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "userIds"), 0L);
		String[] emailAddresses = ParamUtil.getStringValues(
			actionRequest, "emailAddresses");

		byte[] logoBytes = null;

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId > 0) {
			FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

			logoBytes = FileUtil.getBytes(fileEntry.getContentStream());
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAccount.class.getName(), actionRequest);

		if ((commerceAccountId > 0) &&
			(commerceAccountId != parentCommerceAccountId)) {

			return _commerceAccountService.updateCommerceAccount(
				commerceAccountId, name, !deleteLogo, logoBytes, email, taxId,
				active, serviceContext);
		}
		else {
			return _commerceAccountService.addCommerceAccount(
				name, parentCommerceAccountId, email, taxId, active,
				externalReferenceCode, userIds, emailAddresses, serviceContext);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditCommerceAccountMVCActionCommand.class);

	@Reference
	private CommerceAccountService _commerceAccountService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

}