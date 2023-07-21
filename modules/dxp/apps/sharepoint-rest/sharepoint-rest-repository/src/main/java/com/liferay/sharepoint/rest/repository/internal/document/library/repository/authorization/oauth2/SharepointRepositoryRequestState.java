/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.sharepoint.rest.repository.internal.document.library.repository.authorization.oauth2;

import com.liferay.document.library.repository.authorization.capability.AuthorizationException;
import com.liferay.document.library.repository.authorization.oauth2.OAuth2AuthorizationException;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.io.Serializable;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Adolfo Pérez
 */
public final class SharepointRepositoryRequestState implements Serializable {

	public static final SharepointRepositoryRequestState get(
		HttpServletRequest request) {

		HttpSession session = request.getSession();

		return (SharepointRepositoryRequestState)session.getAttribute(
			SharepointRepositoryRequestState.class.getName());
	}

	public static final void save(HttpServletRequest request, String state) {
		HttpSession session = request.getSession();

		PortletRequest portletRequest = (PortletRequest)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);

		session.setAttribute(
			SharepointRepositoryRequestState.class.getName(),
			new SharepointRepositoryRequestState(
				ParamUtil.getLong(portletRequest, "folderId"),
				PortalUtil.getCurrentCompleteURL(request), state));
	}

	public long getFolderId() {
		return _folderId;
	}

	public void restore(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		HttpSession session = request.getSession();

		session.removeAttribute(
			SharepointRepositoryRequestState.class.getName());

		response.sendRedirect(_url);
	}

	public void validate(String state) throws AuthorizationException {
		if (!state.equals(_state)) {
			throw new OAuth2AuthorizationException.InvalidState(
				String.format(
					"The Sharepoint server returned an invalid state %s that " +
						"does not match the expected state %s",
					_state, state));
		}
	}

	private SharepointRepositoryRequestState(
		long folderId, String url, String state) {

		_folderId = folderId;
		_url = url;
		_state = state;
	}

	private static final long serialVersionUID = 1L;

	private final long _folderId;
	private final String _state;
	private final String _url;

}