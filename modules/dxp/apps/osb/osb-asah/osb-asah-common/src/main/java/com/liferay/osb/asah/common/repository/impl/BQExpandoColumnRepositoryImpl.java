/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQExpandoColumn;
import com.liferay.osb.asah.common.repository.CustomBQExpandoColumnRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.util.Date;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * @author Marcellus Tavares
 */
public class BQExpandoColumnRepositoryImpl
	implements CustomBQExpandoColumnRepository {

	public BQExpandoColumnRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQExpandoColumn")
			));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQExpandoColumn")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public Optional<BQExpandoColumn> findByColumnIdAndDataSourceId(
		String expandoColumnId, Long dataSourceId) {

		return _queryExecutor.queryForObject(
			BQExpandoColumn::new,
			_dslContext.select(
			).from(
				DSL.table("BQExpandoColumn")
			).where(
				DSL.and(
					DSL.field(
						"columnId"
					).eq(
						expandoColumnId
					),
					DSL.field(
						"dataSourceId"
					).eq(
						dataSourceId
					))
			));
	}

	@Override
	public Optional<BQExpandoColumn> findById(String id) {
		return _queryExecutor.queryForObject(
			BQExpandoColumn::new,
			_dslContext.select(
			).from(
				DSL.table("BQExpandoColumn")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public BQExpandoColumn insert(BQExpandoColumn bqExpandoColumn) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQExpandoColumn")
			).columns(
				DSL.field("className"), DSL.field("columnId"),
				DSL.field("dataSourceId", Long.class), DSL.field("dataType"),
				DSL.field("displayType"), DSL.field("id"),
				DSL.field("modifiedDate", Date.class), DSL.field("name")
			).values(
				bqExpandoColumn.getClassName(), bqExpandoColumn.getColumnId(),
				bqExpandoColumn.getDataSourceId(),
				bqExpandoColumn.getDataType(), bqExpandoColumn.getDisplayType(),
				bqExpandoColumn.getId(), bqExpandoColumn.getModifiedDate(),
				bqExpandoColumn.getName()
			));

		return bqExpandoColumn;
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}