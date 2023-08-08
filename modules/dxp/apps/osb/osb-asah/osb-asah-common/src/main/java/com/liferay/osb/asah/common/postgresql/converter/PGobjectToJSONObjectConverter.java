/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter;

import org.json.JSONObject;

import org.postgresql.util.PGobject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author André Miranda
 */
@ReadingConverter
public class PGobjectToJSONObjectConverter
	implements Converter<PGobject, JSONObject> {

	@Override
	public JSONObject convert(PGobject pGobject) {
		return new JSONObject(pGobject.getValue());
	}

}