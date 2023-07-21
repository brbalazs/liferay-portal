/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.processor;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Locale;

/**
 * @author Lance Ji
 */
public interface FragmentEntryProcessorRegistry {

	public JSONObject getDefaultEditableValuesJSONObject(String html);

	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink)
		throws PortalException;

	public default String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, String mode)
		throws PortalException {

		return processFragmentEntryLinkHTML(
			fragmentEntryLink, mode, LocaleUtil.getMostRelevantLocale());
	}

	public String processFragmentEntryLinkHTML(
			FragmentEntryLink fragmentEntryLink, String mode, Locale locale)
		throws PortalException;

	public void validateFragmentEntryHTML(String html) throws PortalException;

}