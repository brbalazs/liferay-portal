/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter;

import com.fasterxml.jackson.core.JsonFactory;

import com.liferay.osb.asah.common.json.JSONUtil;

import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.data.domain.Pageable;

/**
 * @author Matthew Kong
 */
public class RawDataExporter extends BaseDataExporter {

	public RawDataExporter(
			String collectionName, JsonFactory jsonFactory,
			OutputStream outputStream)
		throws Exception {

		super(jsonFactory, outputStream);

		_collectionName = collectionName;

		jsonGenerator.useDefaultPrettyPrinter();
	}

	@Override
	protected JSONObject doGetResultPageJSONObject(Pageable pageable) {

		// TODO Implement Data Export

		return JSONUtil.put("results", new JSONArray());
	}

	private final String _collectionName;

}