/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.webdav.methods;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.webdav.Status;
import com.liferay.portal.kernel.webdav.WebDAVException;
import com.liferay.portal.kernel.webdav.WebDAVRequest;
import com.liferay.portal.kernel.webdav.WebDAVStorage;
import com.liferay.portal.kernel.webdav.methods.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public class MkcolMethodImpl implements Method {

	@Override
	public int process(WebDAVRequest webDAVRequest) throws WebDAVException {
		long groupId = webDAVRequest.getGroupId();

		if (groupId != 0) {
			WebDAVStorage storage = webDAVRequest.getWebDAVStorage();

			Status status = storage.makeCollection(webDAVRequest);

			if (Validator.isNotNull(status.getObject())) {
				HttpServletRequest request =
					webDAVRequest.getHttpServletRequest();
				HttpServletResponse response =
					webDAVRequest.getHttpServletResponse();

				response.setHeader(
					HttpHeaders.LOCATION,
					StringBundler.concat(
						PortalUtil.getPortalURL(request),
						webDAVRequest.getRootPath(), StringPool.SLASH,
						String.valueOf(status.getObject())));
			}

			return status.getCode();
		}

		return HttpServletResponse.SC_FORBIDDEN;
	}

}