/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQAsset;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.repository.CustomBQAssetRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author Ivica Cardic
 */
public class BQAssetRepositoryImpl
	extends BaseRepository implements CustomBQAssetRepository {

	public BQAssetRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long countBQAssets(String filterString) {
		FilterExpression filterExpression = new FilterExpression(
			filterString, FilterExpression.FilterType.ASSETS);

		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table(
					"BQAsset"
				).as(
					"Asset"
				)
			).where(
				filterExpression.getCondition()
			));
	}

	@Override
	public List<BQAsset> findByIdIn(Collection<String> ids) {
		return _queryExecutor.queryForList(
			BQAsset::new,
			_dslContext.selectFrom(
				"BQAsset"
			).where(
				DSL.field(
					"id"
				).in(
					ids
				)
			));
	}

	@Override
	public List<BQAsset> searchBQAssets(
		String filterString, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		FilterExpression filterExpression = new FilterExpression(
			filterString, FilterExpression.FilterType.ASSETS);

		return _queryExecutor.queryForList(
			BQAsset::new,
			selectSelectStep.from(
				DSL.table(
					"BQAsset"
				).as(
					"Asset"
				)
			).where(
				filterExpression.getCondition()
			).orderBy(
				getSortFields(
					new HashMap<String, String>() {
						{
							put("desc", "assetTitle");
						}
					},
					pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}