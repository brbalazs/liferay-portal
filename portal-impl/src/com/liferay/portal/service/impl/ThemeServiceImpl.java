/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.service.base.ThemeServiceBaseImpl;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class ThemeServiceImpl extends ThemeServiceBaseImpl {

	@Override
	public List<Theme> getThemes(long companyId) {
		return themeLocalService.getThemes(companyId);
	}

	@Override
	public JSONArray getWARThemes() {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<Theme> themes = themeLocalService.getWARThemes();

		for (Theme theme : themes) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put(
				"servlet_context_name", theme.getServletContextName());
			jsonObject.put("theme_id", theme.getThemeId());
			jsonObject.put("theme_name", theme.getName());

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

}