/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQRole;
import com.liferay.osb.asah.common.repository.CustomBQRoleRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.util.ConditionUtil;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author Marcos Martins
 */
public class BQRoleRepositoryImpl
	extends BaseRepository implements CustomBQRoleRepository {

	public BQRoleRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQRole")
			));
	}

	@Override
	public long countByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQRole"
			).where(
				ConditionUtil.toConditions(
					dataSourceIds, keywords, new String[] {"name"})
			));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQRole")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public List<BQRole> findByIdIn(Collection<String> ids) {
		return _queryExecutor.queryForList(
			BQRole::new,
			_dslContext.selectFrom(
				"BQRole"
			).where(
				DSL.field(
					"id"
				).in(
					ids
				)
			));
	}

	@Override
	public BQRole insert(BQRole bqRole) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQRole")
			).columns(
				DSL.field("dataSourceId", Long.class), DSL.field("id"),
				DSL.field("modifiedDate", Date.class), DSL.field("name"),
				DSL.field("roleId", Long.class)
			).values(
				bqRole.getDataSourceId(), bqRole.getId(),
				bqRole.getModifiedDate(), bqRole.getName(), bqRole.getRoleId()
			));

		return bqRole;
	}

	@Override
	public List<BQRole> searchByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQRole::new,
			selectSelectStep.from(
				"BQRole"
			).where(
				ConditionUtil.toConditions(
					dataSourceIds, keywords, new String[] {"name"})
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}