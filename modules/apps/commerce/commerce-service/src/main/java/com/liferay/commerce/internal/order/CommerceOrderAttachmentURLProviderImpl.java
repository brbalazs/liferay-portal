/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.commerce.internal.order;

import com.liferay.commerce.constants.CommerceOrderAttachmentConstants;
import com.liferay.commerce.order.CommerceOrderAttachmentURLProvider;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.Portal;

import jakarta.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Balazs Breier
 */
@Component(service = CommerceOrderAttachmentURLProvider.class)
public class CommerceOrderAttachmentURLProviderImpl
	implements CommerceOrderAttachmentURLProvider {

	@Override
	public String getDownloadURL(
		HttpServletRequest httpServletRequest, long commerceOrderAttachmentId) {

		// Qualify the URL from the request rather than from the company, so
		// that a virtual host or a reverse proxy resolves to the host the
		// caller reached

		return StringBundler.concat(
			_portal.getPortalURL(httpServletRequest), _portal.getPathModule(),
			StringPool.SLASH, CommerceOrderAttachmentConstants.SERVLET_PATH,
			StringPool.SLASH, commerceOrderAttachmentId);
	}

	@Reference
	private Portal _portal;

}