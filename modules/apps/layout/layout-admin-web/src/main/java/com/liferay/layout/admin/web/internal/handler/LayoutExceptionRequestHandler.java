/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.handler;

import com.liferay.asset.kernel.exception.AssetCategoryException;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.LayoutNameException;
import com.liferay.portal.kernel.exception.LayoutTypeException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.model.ModelHintsUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.LayoutTypeControllerTracker;

import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = LayoutExceptionRequestHandler.class)
public class LayoutExceptionRequestHandler {

	public void handlePortalException(
			ActionRequest actionRequest, ActionResponse actionResponse,
			PortalException pe)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String errorMessage = null;

		if (pe instanceof AssetCategoryException) {
			AssetCategoryException assetCategoryException =
				(AssetCategoryException)pe;

			AssetVocabulary assetVocabulary =
				assetCategoryException.getVocabulary();

			String assetVocabularyTitle = StringPool.BLANK;

			if (assetVocabulary != null) {
				assetVocabularyTitle = assetVocabulary.getTitle(
					themeDisplay.getLocale());
			}

			if (assetCategoryException.getType() ==
					AssetCategoryException.AT_LEAST_ONE_CATEGORY) {

				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"please-select-at-least-one-category-for-x",
					assetVocabularyTitle);
			}
			else if (assetCategoryException.getType() ==
						AssetCategoryException.TOO_MANY_CATEGORIES) {

				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"you-cannot-select-more-than-one-category-for-x",
					assetVocabularyTitle);
			}
		}
		else if (pe instanceof LayoutNameException) {
			LayoutNameException lne = (LayoutNameException)pe;

			if (lne.getType() == LayoutNameException.TOO_LONG) {
				errorMessage = LanguageUtil.format(
					themeDisplay.getRequest(),
					"page-name-cannot-exceed-x-characters",
					ModelHintsUtil.getMaxLength(
						Layout.class.getName(), "friendlyURL"));
			}
			else {
				errorMessage = LanguageUtil.get(
					themeDisplay.getRequest(),
					"please-enter-a-valid-name-for-the-page");
			}
		}
		else if (pe instanceof LayoutTypeException) {
			LayoutTypeException lte = (LayoutTypeException)pe;

			if ((lte.getType() == LayoutTypeException.FIRST_LAYOUT) ||
				(lte.getType() == LayoutTypeException.NOT_INSTANCEABLE)) {

				errorMessage = _handleLayoutTypeException(
					actionRequest, lte.getType());
			}
		}

		if (Validator.isNull(errorMessage)) {
			errorMessage = LanguageUtil.get(
				themeDisplay.getRequest(), "an-unexpected-error-occurred");
		}

		JSONObject jsonObject = JSONUtil.put("errorMessage", errorMessage);

		JSONPortletResponseUtil.writeJSON(
			actionRequest, actionResponse, jsonObject);
	}

	private String _handleLayoutTypeException(
		ActionRequest actionRequest, int exceptionType) {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String errorMessage = "pages-of-type-x-cannot-be-selected";

		if (exceptionType == LayoutTypeException.FIRST_LAYOUT) {
			errorMessage = "the-first-page-cannot-be-of-type-x";
		}

		String type = ParamUtil.getString(actionRequest, "type");

		LayoutTypeController layoutTypeController =
			LayoutTypeControllerTracker.getLayoutTypeController(type);

		ResourceBundle layoutTypeResourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", themeDisplay.getLocale(),
			layoutTypeController.getClass());

		String layoutTypeName = LanguageUtil.get(
			layoutTypeResourceBundle, "layout.types." + type);

		return LanguageUtil.format(
			themeDisplay.getRequest(), errorMessage, layoutTypeName);
	}

}