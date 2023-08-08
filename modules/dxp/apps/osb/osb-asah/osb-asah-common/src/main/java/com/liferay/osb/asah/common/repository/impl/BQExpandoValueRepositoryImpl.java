/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQExpandoValue;
import com.liferay.osb.asah.common.repository.CustomBQExpandoValueRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * @author Marcellus Tavares
 */
public class BQExpandoValueRepositoryImpl
	implements CustomBQExpandoValueRepository {

	public BQExpandoValueRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQExpandoValue")
			));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQExpandoValue")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public List<BQExpandoValue> findByClassPKAndClassTypeAndDataSourceId(
		Long classPK, String classType, Long dataSourceId) {

		return _queryExecutor.queryForList(
			BQExpandoValue::new,
			_dslContext.select(
			).from(
				DSL.table("BQExpandoValue")
			).where(
				DSL.and(
					DSL.field(
						"classPK"
					).eq(
						String.valueOf(classPK)
					),
					DSL.field(
						"classType"
					).eq(
						classType
					),
					DSL.field(
						"dataSourceId", Long.class
					).eq(
						dataSourceId
					))
			));
	}

	@Override
	public BQExpandoValue insert(BQExpandoValue bqExpandoValue) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQExpandoValue")
			).columns(
				DSL.field("classPK"), DSL.field("classType"),
				DSL.field("columnId"), DSL.field("dataSourceId", Long.class),
				DSL.field("fieldName"), DSL.field("id"),
				DSL.field("modifiedDate", Date.class), DSL.field("value")
			).values(
				bqExpandoValue.getClassPK(), bqExpandoValue.getClassType(),
				bqExpandoValue.getColumnId(), bqExpandoValue.getDataSourceId(),
				bqExpandoValue.getFieldName(), bqExpandoValue.getId(),
				bqExpandoValue.getModifiedDate(), bqExpandoValue.getValue()
			));

		return bqExpandoValue;
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}