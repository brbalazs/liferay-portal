/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.repository.CustomBQCSVUserRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.util.BQSQLUtil;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author Marcellus Tavares
 */
public class BQCSVUserRepositoryImpl implements CustomBQCSVUserRepository {

	public BQCSVUserRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public long countByDataSourceId(Long dataSourceId) {
		return 0;
	}

	@Override
	public void deleteAll() {
		_queryExecutor.queryExecute(
			_dslContext.deleteFrom(
				DSL.table("BQCSVUser")
			).where(
				DSL.trueCondition()
			));
	}

	@Override
	public void deleteByDataSourceId(Long dataSourceId) {
	}

	@Override
	public void deleteByDataSourceIdAndDataSourceUserPKIn(
		Long dataSourceId, List<String> dataSourceUserPKs) {
	}

	@Override
	public List<BQCSVUser> findAll() {
		return _queryExecutor.queryForList(
			BQCSVUser::new,
			_dslContext.select(
			).from(
				DSL.table("BQCSVUser")
			));
	}

	@Override
	public List<BQCSVUser> findByDataSourceId(
		Long dataSourceId, Pageable pageable) {

		return null;
	}

	@Override
	public void insert(BQCSVUser bqCSVUser) {
		_queryExecutor.queryExecute(BQSQLUtil.createInsertStatement(bqCSVUser));
	}

	@Override
	public void insertAll(List<BQCSVUser> bqCSVUsers) {
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}