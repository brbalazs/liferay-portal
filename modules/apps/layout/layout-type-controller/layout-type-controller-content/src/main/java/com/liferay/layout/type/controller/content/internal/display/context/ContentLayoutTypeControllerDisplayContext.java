/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.type.controller.content.internal.display.context;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.util.FragmentEntryRenderUtil;
import com.liferay.layout.type.controller.content.internal.constants.ContentLayoutTypeControllerWebKeys;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Juergen Kappler
 */
public class ContentLayoutTypeControllerDisplayContext {

	public ContentLayoutTypeControllerDisplayContext(
		HttpServletRequest request, HttpServletResponse response) {

		_request = request;
		_response = response;
	}

	public String getRenderedContent() throws PortalException {
		List<FragmentEntryLink> fragmentEntryLinks =
			(List<FragmentEntryLink>)_request.getAttribute(
				ContentLayoutTypeControllerWebKeys.LAYOUT_FRAGMENTS);

		if ((fragmentEntryLinks == null) || fragmentEntryLinks.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(fragmentEntryLinks.size());

		for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
			sb.append(
				FragmentEntryRenderUtil.renderFragmentEntryLink(
					fragmentEntryLink, FragmentEntryLinkConstants.VIEW,
					_request, _response));
		}

		String renderedContent = sb.toString();

		if (Validator.isNull(renderedContent)) {
			return StringPool.BLANK;
		}

		return renderedContent;
	}

	private final HttpServletRequest _request;
	private final HttpServletResponse _response;

}