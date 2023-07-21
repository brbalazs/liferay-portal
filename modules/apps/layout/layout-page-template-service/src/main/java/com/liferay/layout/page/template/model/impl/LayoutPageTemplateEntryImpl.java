/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.model.impl;

import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

/**
 * @author Jürgen Kappler
 */
public class LayoutPageTemplateEntryImpl
	extends LayoutPageTemplateEntryBaseImpl {

	@Override
	public String getContent() throws PortalException {
		List<FragmentEntryLink> fragmentEntryLinks =
			FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(
				getGroupId(),
				PortalUtil.getClassNameId(
					LayoutPageTemplateEntry.class.getName()),
				getLayoutPageTemplateEntryId());

		StringBundler cssSB = new StringBundler(fragmentEntryLinks.size());
		StringBundler htmlSB = new StringBundler(fragmentEntryLinks.size());
		StringBundler jsSB = new StringBundler(fragmentEntryLinks.size());

		for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
			cssSB.append(fragmentEntryLink.getCss());
			htmlSB.append(fragmentEntryLink.getHtml());
			jsSB.append(fragmentEntryLink.getJs());
		}

		StringBundler sb = new StringBundler(7);

		sb.append("<html><head><style>");
		sb.append(cssSB.toString());
		sb.append("</style><script>");
		sb.append(jsSB.toString());
		sb.append("</script></head><body>");
		sb.append(htmlSB.toString());
		sb.append("</body></html>");

		return sb.toString();
	}

	@Override
	public String getImagePreviewURL(ThemeDisplay themeDisplay) {
		if (getPreviewFileEntryId() <= 0) {
			return StringPool.BLANK;
		}

		try {
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
				getPreviewFileEntryId());

			if (fileEntry == null) {
				return StringPool.BLANK;
			}

			return DLUtil.getImagePreviewURL(fileEntry, themeDisplay);
		}
		catch (Exception e) {
			_log.error("Unable to get preview entry image URL", e);
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutPageTemplateEntryImpl.class);

}