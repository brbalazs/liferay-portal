/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Iván Zaera
 */
public abstract class BaseRSSStrutsAction extends BaseStrutsAction {

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		if (!isRSSFeedsEnabled(request)) {
			PortalUtil.sendRSSFeedsDisabledError(request, response);

			return null;
		}

		try {
			ServletResponseUtil.sendFile(
				request, response, null, getRSS(request),
				ContentTypes.TEXT_XML_UTF8);

			return null;
		}
		catch (Exception e) {
			PortalUtil.sendError(e, request, response);

			return null;
		}
	}

	protected abstract byte[] getRSS(HttpServletRequest request)
		throws Exception;

	protected boolean isRSSFeedsEnabled(HttpServletRequest request)
		throws Exception {

		return PortalUtil.isRSSFeedsEnabled();
	}

}