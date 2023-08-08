/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter;

import org.json.JSONArray;

import org.postgresql.util.PGobject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author Rachael Koestartyo
 */
@ReadingConverter
public class PGobjectToJSONArrayConverter
	implements Converter<PGobject, JSONArray> {

	@Override
	public JSONArray convert(PGobject pGobject) {
		return new JSONArray(pGobject.getValue());
	}

}