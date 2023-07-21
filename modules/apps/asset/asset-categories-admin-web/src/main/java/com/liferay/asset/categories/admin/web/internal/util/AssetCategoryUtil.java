/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.categories.admin.web.internal.util;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class AssetCategoryUtil {

	public static void addPortletBreadcrumbEntry(
			AssetVocabulary vocabulary, AssetCategory category,
			HttpServletRequest request, RenderResponse renderResponse)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletURL portletURL = renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view.jsp");

		PortalUtil.addPortletBreadcrumbEntry(
			request, LanguageUtil.get(request, "vocabularies"),
			portletURL.toString());

		if (category == null) {
			PortalUtil.addPortletBreadcrumbEntry(
				request, vocabulary.getTitle(themeDisplay.getLocale()), null);

			return;
		}

		portletURL.setParameter("mvcPath", "/view_categories.jsp");

		String navigation = ParamUtil.getString(request, "navigation");

		if (Validator.isNotNull(navigation)) {
			portletURL.setParameter("navigation", navigation);
		}

		portletURL.setParameter(
			"vocabularyId", String.valueOf(vocabulary.getVocabularyId()));

		PortalUtil.addPortletBreadcrumbEntry(
			request, vocabulary.getTitle(themeDisplay.getLocale()),
			portletURL.toString());

		List<AssetCategory> ancestorsCategories = category.getAncestors();

		Collections.reverse(ancestorsCategories);

		for (AssetCategory curCategory : ancestorsCategories) {
			portletURL.setParameter(
				"categoryId", String.valueOf(curCategory.getCategoryId()));

			PortalUtil.addPortletBreadcrumbEntry(
				request, curCategory.getTitle(themeDisplay.getLocale()),
				portletURL.toString());
		}

		PortalUtil.addPortletBreadcrumbEntry(
			request, category.getTitle(themeDisplay.getLocale()), null);
	}

}