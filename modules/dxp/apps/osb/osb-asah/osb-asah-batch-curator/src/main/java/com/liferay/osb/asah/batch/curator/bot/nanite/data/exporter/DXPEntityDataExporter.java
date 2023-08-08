/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.dog.BQUserDog;
import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.ListUtil;

import java.io.OutputStream;

import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.data.domain.Pageable;

/**
 * @author Marcos Martins
 */
public class DXPEntityDataExporter extends BaseDataExporter {

	public DXPEntityDataExporter(
			BQUserDog bqUserDog, String fieldName, String fieldValue,
			JsonFactory jsonFactory, ObjectMapper objectMapper,
			OutputStream outputStream)
		throws Exception {

		super(jsonFactory, outputStream);

		_bqUserDog = bqUserDog;
		_fieldName = fieldName;
		_fieldValue = fieldValue;

		jsonGenerator.useDefaultPrettyPrinter();

		_objectMapper = objectMapper;
	}

	@Override
	protected JSONObject doGetResultPageJSONObject(Pageable pageable) {
		return JSONUtil.put(
			"results",
			new JSONArray(
				ListUtil.map(
					_bqUserDog.getBQUsers(
						Collections.singletonMap(_fieldName, _fieldValue),
						pageable),
					this::_toJSONObject)));
	}

	private JSONObject _toJSONObject(BQUser bqUser) {
		return _objectMapper.convertValue(bqUser, JSONObject.class);
	}

	private final BQUserDog _bqUserDog;
	private final String _fieldName;
	private final String _fieldValue;
	private final ObjectMapper _objectMapper;

}