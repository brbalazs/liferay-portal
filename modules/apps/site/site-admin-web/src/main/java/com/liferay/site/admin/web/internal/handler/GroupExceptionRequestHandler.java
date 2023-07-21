/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.admin.web.internal.handler;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.DuplicateGroupException;
import com.liferay.portal.kernel.exception.GroupInheritContentException;
import com.liferay.portal.kernel.exception.GroupKeyException;
import com.liferay.portal.kernel.exception.GroupParentException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.model.SiteConstants;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = GroupExceptionRequestHandler.class)
public class GroupExceptionRequestHandler {

	public void handlePortalException(
			ActionRequest actionRequest, ActionResponse actionResponse,
			PortalException pe)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		String errorMessage = null;

		if (pe instanceof DuplicateGroupException) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(), "please-enter-a-unique-name");
		}
		else if (pe instanceof GroupInheritContentException) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(),
				"this-site-cannot-inherit-content-from-its-parent-site");
		}
		else if (pe instanceof GroupKeyException) {
			errorMessage = _handleGroupKeyException(actionRequest);
		}
		else if (pe instanceof GroupParentException.MustNotBeOwnParent) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(),
				"this-site-cannot-inherit-content-from-its-parent-site");
		}

		if (Validator.isNull(errorMessage)) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(), "an-unexpected-error-occurred");
		}

		jsonObject.put("error", errorMessage);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private String _handleGroupKeyException(ActionRequest actionRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		StringBundler sb = new StringBundler(5);

		sb.append(
			LanguageUtil.format(
				themeDisplay.getRequest(),
				"the-x-cannot-be-x-or-a-reserved-word-such-as-x",
				new String[] {
					SiteConstants.NAME_LABEL,
					SiteConstants.getNameGeneralRestrictions(
						themeDisplay.getLocale()),
					SiteConstants.NAME_RESERVED_WORDS
				}));

		sb.append(StringPool.SPACE);

		sb.append(
			LanguageUtil.format(
				themeDisplay.getRequest(),
				"the-x-cannot-contain-the-following-invalid-characters-x",
				new String[] {
					SiteConstants.NAME_LABEL,
					SiteConstants.NAME_INVALID_CHARACTERS
				}));

		sb.append(StringPool.SPACE);

		int groupKeyMaxLength = ModelHintsUtil.getMaxLength(
			Group.class.getName(), "groupKey");

		sb.append(
			LanguageUtil.format(
				themeDisplay.getRequest(),
				"the-x-cannot-contain-more-than-x-characters",
				new String[] {
					SiteConstants.NAME_LABEL, String.valueOf(groupKeyMaxLength)
				}));

		return sb.toString();
	}

}