/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.image.content.transformer.backwards.compatibility.internal;

import com.liferay.adaptive.media.content.transformer.BaseRegexStringContentTransformer;
import com.liferay.adaptive.media.content.transformer.ContentTransformer;
import com.liferay.adaptive.media.content.transformer.ContentTransformerContentType;
import com.liferay.adaptive.media.content.transformer.constants.ContentTransformerContentTypes;
import com.liferay.adaptive.media.image.html.AMImageHTMLTagFactory;
import com.liferay.adaptive.media.image.html.constants.AMImageHTMLConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true, property = "content.transformer.content.type=html",
	service = ContentTransformer.class
)
public class AMBackwardsCompatibilityHtmlContentTransformer
	extends BaseRegexStringContentTransformer {

	@Override
	public ContentTransformerContentType<String>
		getContentTransformerContentType() {

		return ContentTransformerContentTypes.HTML;
	}

	@Override
	public String transform(String html) throws PortalException {
		if (html == null) {
			return null;
		}

		if (!html.contains("<img") || !html.contains("/documents/")) {
			return html;
		}

		return super.transform(html);
	}

	@Override
	protected FileEntry getFileEntry(Matcher matcher) throws PortalException {
		if (StringUtil.containsIgnoreCase(
				matcher.group(0),
				AMImageHTMLConstants.ATTRIBUTE_NAME_FILE_ENTRY_ID)) {

			return null;
		}

		if (matcher.group(4) != null) {
			long groupId = Long.valueOf(matcher.group(1));

			String uuid = matcher.group(4);

			return _dlAppLocalService.getFileEntryByUuidAndGroupId(
				uuid, groupId);
		}

		long groupId = Long.valueOf(matcher.group(1));
		long folderId = Long.valueOf(matcher.group(2));
		String title = matcher.group(3);

		return _dlAppLocalService.getFileEntry(groupId, folderId, title);
	}

	@Override
	protected Pattern getPattern() {
		return _pattern;
	}

	@Override
	protected String getReplacement(String originalImgTag, FileEntry fileEntry)
		throws PortalException {

		if (fileEntry == null) {
			return originalImgTag;
		}

		return _amImageHTMLTagFactory.create(originalImgTag, fileEntry);
	}

	private static final Pattern _pattern = Pattern.compile(
		"<img\\s+src=['\"]/documents/(\\d+)/(\\d+)/([^/?]+)" +
			"(?:/([-0-9a-fA-F]+))?(?:\\?t=\\d+)?['\"]\\s*/>");

	@Reference
	private AMImageHTMLTagFactory _amImageHTMLTagFactory;

	@Reference
	private DLAppLocalService _dlAppLocalService;

}