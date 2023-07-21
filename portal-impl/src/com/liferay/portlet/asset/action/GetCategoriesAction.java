/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.asset.action;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetCategoryConstants;
import com.liferay.asset.kernel.service.AssetCategoryServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.JSONAction;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Eduardo Lundgren
 */
public class GetCategoriesAction extends JSONAction {

	@Override
	public String getJSON(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<AssetCategory> categories = getCategories(request);

		for (AssetCategory category : categories) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			List<AssetCategory> childCategories =
				AssetCategoryServiceUtil.getChildCategories(
					category.getCategoryId());

			jsonObject.put("categoryId", category.getCategoryId());
			jsonObject.put("childrenCount", childCategories.size());
			jsonObject.put("hasChildren", !childCategories.isEmpty());
			jsonObject.put("name", category.getName());
			jsonObject.put("parentCategoryId", category.getParentCategoryId());
			jsonObject.put(
				"titleCurrentValue", category.getTitleCurrentValue());

			jsonArray.put(jsonObject);
		}

		return jsonArray.toString();
	}

	protected List<AssetCategory> getCategories(HttpServletRequest request)
		throws Exception {

		long scopeGroupId = ParamUtil.getLong(request, "scopeGroupId");
		long categoryId = ParamUtil.getLong(request, "categoryId");
		long vocabularyId = ParamUtil.getLong(request, "vocabularyId");
		int start = ParamUtil.getInteger(request, "start", QueryUtil.ALL_POS);
		int end = ParamUtil.getInteger(request, "end", QueryUtil.ALL_POS);

		List<AssetCategory> categories = Collections.emptyList();

		if (categoryId > 0) {
			if (scopeGroupId > 0) {
				categories = AssetCategoryServiceUtil.getVocabularyCategories(
					scopeGroupId, categoryId, vocabularyId, start, end, null);
			}
			else {
				categories = AssetCategoryServiceUtil.getChildCategories(
					categoryId, start, end, null);
			}
		}
		else if (vocabularyId > 0) {
			long parentCategoryId = ParamUtil.getLong(
				request, "parentCategoryId",
				AssetCategoryConstants.DEFAULT_PARENT_CATEGORY_ID);

			if (scopeGroupId > 0) {
				categories = AssetCategoryServiceUtil.getVocabularyCategories(
					scopeGroupId, parentCategoryId, vocabularyId, start, end,
					null);
			}
			else {
				categories = AssetCategoryServiceUtil.getVocabularyCategories(
					parentCategoryId, vocabularyId, start, end, null);
			}
		}

		return categories;
	}

}