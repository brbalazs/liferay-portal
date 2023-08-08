/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.repository.CustomBQUserGroupRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.util.ConditionUtil;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author Marcos Martins
 */
public class BQUserGroupRepositoryImpl
	extends BaseRepository implements CustomBQUserGroupRepository {

	public BQUserGroupRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long count() {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQUserGroup")
			));
	}

	@Override
	public long countByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQUserGroup"
			).where(
				ConditionUtil.toConditions(
					dataSourceIds, keywords, new String[] {"name"})
			));
	}

	@Override
	public void deleteById(String id) {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQUserGroup")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public Optional<BQUserGroup> findById(String id) {
		return _queryExecutor.queryForObject(
			BQUserGroup::new,
			_dslContext.select(
			).from(
				DSL.table("BQUserGroup")
			).where(
				DSL.field(
					"id"
				).eq(
					id
				)
			));
	}

	@Override
	public List<BQUserGroup> findByIdIn(Collection<String> ids) {
		return _queryExecutor.queryForList(
			BQUserGroup::new,
			_dslContext.selectFrom(
				"BQUserGroup"
			).where(
				DSL.field(
					"id"
				).in(
					ids
				)
			));
	}

	@Override
	public BQUserGroup insert(BQUserGroup bqUserGroup) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQUserGroup")
			).columns(
				DSL.field("dataSourceId", Long.class), DSL.field("id"),
				DSL.field("modifiedDate", Date.class), DSL.field("name"),
				DSL.field("userGroupId", Long.class)
			).values(
				bqUserGroup.getDataSourceId(), bqUserGroup.getId(),
				bqUserGroup.getModifiedDate(), bqUserGroup.getName(),
				bqUserGroup.getUserGroupId()
			));

		return bqUserGroup;
	}

	@Override
	public List<BQUserGroup> searchByDataSourceIdsAndKeywords(
		List<Long> dataSourceIds, String keywords, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _queryExecutor.queryForList(
			BQUserGroup::new,
			selectSelectStep.from(
				"BQUserGroup"
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