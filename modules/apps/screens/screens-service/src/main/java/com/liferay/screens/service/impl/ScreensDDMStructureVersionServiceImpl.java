/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.screens.service.impl;

import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.screens.service.base.ScreensDDMStructureVersionServiceBaseImpl;

/**
 * @author Javier Gamarra
 */
public class ScreensDDMStructureVersionServiceImpl
	extends ScreensDDMStructureVersionServiceBaseImpl {

	@Override
	public JSONObject getDDMStructureVersion(long structureId)
		throws PortalException {

		JSONObject ddmStructureVersionJSONObject =
			JSONFactoryUtil.createJSONObject();

		DDMStructureVersion ddmStructureVersion =
			ddmStructureVersionService.getLatestStructureVersion(structureId);

		DDMFormLayout ddmFormLayout = ddmStructureVersion.getDDMFormLayout();

		JSONObject ddmFormLayoutJSONObject = JSONFactoryUtil.createJSONObject(
			JSONFactoryUtil.looseSerializeDeep(ddmFormLayout));

		ddmStructureVersionJSONObject.put(
			"ddmFormLayout", ddmFormLayoutJSONObject);

		JSONObject ddmStructureJSONObject = JSONFactoryUtil.createJSONObject(
			JSONFactoryUtil.looseSerialize(ddmStructureVersion.getStructure()));

		ddmStructureVersionJSONObject.put(
			"ddmStructure", ddmStructureJSONObject);

		return ddmStructureVersionJSONObject;
	}

}