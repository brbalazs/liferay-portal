/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.internal.servlet;

import com.liferay.commerce.constants.CommerceOrderAttachmentConstants;
import com.liferay.commerce.exception.NoSuchOrderAttachmentException;
import com.liferay.commerce.model.CommerceOrderAttachment;
import com.liferay.commerce.service.CommerceOrderAttachmentService;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Balazs Breier
 */
@Component(
	property = {
		"osgi.http.whiteboard.context.path=/" + CommerceOrderAttachmentConstants.SERVLET_PATH,
		"osgi.http.whiteboard.servlet.name=com.liferay.commerce.internal.servlet.CommerceOrderAttachmentServlet",
		"osgi.http.whiteboard.servlet.pattern=/" + CommerceOrderAttachmentConstants.SERVLET_PATH + "/*"
	},
	service = Servlet.class
)
public class CommerceOrderAttachmentServlet extends HttpServlet {

	@Override
	protected void doGet(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		if (PortalSessionThreadLocal.getHttpSession() == null) {
			PortalSessionThreadLocal.setHttpSession(
				httpServletRequest.getSession());
		}

		long commerceOrderAttachmentId = _getCommerceOrderAttachmentId(
			httpServletRequest);

		if (commerceOrderAttachmentId == 0) {
			_sendError(httpServletResponse, HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();
		String originalName = PrincipalThreadLocal.getName();
		User user = null;

		try {
			user = _portal.getUser(httpServletRequest);

			if (user == null) {
				user = _userLocalService.getGuestUser(
					_portal.getCompanyId(httpServletRequest));
			}

			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(user));
			PrincipalThreadLocal.setName(user.getUserId());

			CommerceOrderAttachment commerceOrderAttachment =
				_commerceOrderAttachmentService.getCommerceOrderAttachment(
					commerceOrderAttachmentId);

			FileEntry fileEntry = _dlAppLocalService.getFileEntry(
				commerceOrderAttachment.getFileEntryId());

			ServletResponseUtil.sendFile(
				httpServletRequest, httpServletResponse,
				fileEntry.getFileName(), fileEntry.getContentStream(),
				fileEntry.getSize(), fileEntry.getMimeType(),
				HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT);
		}
		catch (PrincipalException principalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(principalException);
			}

			// A user without permission and a nonexistent attachment both end
			// up here because the model resource permission denies both. Do not
			// distinguish them, otherwise the response tells the caller which
			// attachment IDs exist.

			if ((user == null) || user.isGuestUser()) {
				_sendError(
					httpServletResponse, HttpServletResponse.SC_UNAUTHORIZED);
			}
			else {
				_sendError(
					httpServletResponse, HttpServletResponse.SC_FORBIDDEN);
			}
		}
		catch (NoSuchOrderAttachmentException noSuchOrderAttachmentException) {
			if (_log.isDebugEnabled()) {
				_log.debug(noSuchOrderAttachmentException);
			}

			_sendError(httpServletResponse, HttpServletResponse.SC_NOT_FOUND);
		}
		catch (Exception exception) {
			_log.error(exception);

			_sendError(
				httpServletResponse,
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
			PrincipalThreadLocal.setName(originalName);
		}
	}

	private long _getCommerceOrderAttachmentId(
		HttpServletRequest httpServletRequest) {

		String[] pathArray = StringUtil.split(
			HttpComponentsUtil.fixPath(httpServletRequest.getPathInfo()),
			CharPool.SLASH);

		if (pathArray.length != 1) {
			return 0;
		}

		return GetterUtil.getLong(pathArray[0]);
	}

	private void _sendError(HttpServletResponse httpServletResponse, int status)
		throws IOException {

		// The response is already committed when the file transfer fails part
		// way through, because sendFile flushes the buffer before it writes

		if (!httpServletResponse.isCommitted()) {
			httpServletResponse.sendError(status);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderAttachmentServlet.class);

	@Reference
	private CommerceOrderAttachmentService _commerceOrderAttachmentService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}