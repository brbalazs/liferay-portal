/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite.data.exporter;

import com.fasterxml.jackson.core.JsonFactory;

import com.liferay.osb.asah.common.entity.DataExportTask;
import com.liferay.osb.asah.common.json.JSONUtil;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.data.domain.Pageable;

/**
 * @author Thiago Buarque
 */
public class PostgreSQLDataExporter extends BaseDataExporter {

	public PostgreSQLDataExporter(
			DataExportTask dataExportTask, String dateFieldName,
			DSLContext dslContext, JsonFactory jsonFactory,
			OutputStream outputStream, String tableName)
		throws Exception {

		super(jsonFactory, outputStream);

		_dataExportTask = dataExportTask;
		_dateFieldName = dateFieldName;
		_dslContext = dslContext;
		_tableName = tableName;
	}

	@Override
	protected JSONObject doGetResultPageJSONObject(Pageable pageable) {
		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		Result<Record> records = selectSelectStep.from(
			_tableName
		).where(
			_getConditions()
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch();

		return JSONUtil.put(
			"results",
			new JSONArray(
				records.map(record -> new JSONObject(record.intoMap()))));
	}

	private List<Condition> _getConditions() {
		List<Condition> conditions = new ArrayList<>();

		if (_dataExportTask.getFromDate() != null) {
			conditions.add(
				DSL.field(
					_dateFieldName
				).greaterOrEqual(
					_dataExportTask.getFromDate()
				));
		}

		if (_dataExportTask.getToDate() != null) {
			conditions.add(
				DSL.field(
					_dateFieldName
				).lessOrEqual(
					_dataExportTask.getToDate()
				));
		}

		return conditions;
	}

	private final DataExportTask _dataExportTask;
	private final String _dateFieldName;
	private final DSLContext _dslContext;
	private final String _tableName;

}