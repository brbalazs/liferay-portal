/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter;

import org.json.JSONObject;

import org.postgresql.util.PGobject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * @author André Miranda
 */
@WritingConverter
public class JSONObjectToPGobjectConverter
	implements Converter<JSONObject, PGobject> {

	@Override
	public PGobject convert(JSONObject jsonObject) {
		try {
			PGobject pGobject = new PGobject();

			pGobject.setType("json");
			pGobject.setValue(jsonObject.toString());

			return pGobject;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

}