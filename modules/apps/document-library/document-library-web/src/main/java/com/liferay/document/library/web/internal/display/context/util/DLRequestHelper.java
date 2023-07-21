/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.display.context.util;

import com.liferay.document.library.web.internal.constants.DLWebKeys;
import com.liferay.document.library.web.internal.settings.DLPortletInstanceSettings;
import com.liferay.portal.kernel.display.context.util.BaseRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.documentlibrary.DLGroupServiceSettings;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Iván Zaera
 */
public class DLRequestHelper extends BaseRequestHelper {

	public DLRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public DLGroupServiceSettings getDLGroupServiceSettings() {
		if (_dlGroupServiceSettings != null) {
			return _dlGroupServiceSettings;
		}

		HttpServletRequest request = getRequest();

		_dlGroupServiceSettings = (DLGroupServiceSettings)request.getAttribute(
			DLWebKeys.DOCUMENT_LIBRARY_GROUP_SERVICE_SETTINGS);

		if (_dlGroupServiceSettings != null) {
			return _dlGroupServiceSettings;
		}

		String portletResource = getPortletResource();

		try {
			if (Validator.isNotNull(portletResource)) {
				_dlGroupServiceSettings = DLGroupServiceSettings.getInstance(
					getScopeGroupId(), request.getParameterMap());
			}
			else {
				_dlGroupServiceSettings = DLGroupServiceSettings.getInstance(
					getScopeGroupId());
			}
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}

		request.setAttribute(
			DLWebKeys.DOCUMENT_LIBRARY_GROUP_SERVICE_SETTINGS,
			_dlGroupServiceSettings);

		return _dlGroupServiceSettings;
	}

	public DLPortletInstanceSettings getDLPortletInstanceSettings() {
		if (_dlPortletInstanceSettings != null) {
			return _dlPortletInstanceSettings;
		}

		HttpServletRequest request = getRequest();

		_dlPortletInstanceSettings =
			(DLPortletInstanceSettings)request.getAttribute(
				DLWebKeys.DOCUMENT_LIBRARY_PORTLET_INSTANCE_SETTINGS);

		if (_dlPortletInstanceSettings != null) {
			return _dlPortletInstanceSettings;
		}

		String portletResource = getPortletResource();

		try {
			if (Validator.isNotNull(portletResource)) {
				_dlPortletInstanceSettings =
					DLPortletInstanceSettings.getInstance(
						getLayout(), getResourcePortletId(),
						request.getParameterMap());
			}
			else {
				_dlPortletInstanceSettings =
					DLPortletInstanceSettings.getInstance(
						getLayout(), getPortletId());
			}
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}

		request.setAttribute(
			DLWebKeys.DOCUMENT_LIBRARY_PORTLET_INSTANCE_SETTINGS,
			_dlPortletInstanceSettings);

		return _dlPortletInstanceSettings;
	}

	private DLGroupServiceSettings _dlGroupServiceSettings;
	private DLPortletInstanceSettings _dlPortletInstanceSettings;

}