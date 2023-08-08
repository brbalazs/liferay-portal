/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQMembershipIndividual;
import com.liferay.osb.asah.common.repository.CustomBQMembershipIndividualRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Pageable;

/**
 * @author Marcellus Tavares
 */
public class BQMembershipIndividualRepositoryImpl
	extends BaseRepository implements CustomBQMembershipIndividualRepository {

	public BQMembershipIndividualRepositoryImpl(
		DSLContext dslContext, Environment environment,
		QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_environment = environment;
		_queryExecutor = queryExecutor;

		try {
			_membershipIndividualMergeStatement =
				ResourceUtil.readResourceToString(
					"membership_individual_merge_statement.sql", getClass());
		}
		catch (Exception exception) {
			throw new IllegalStateException(exception);
		}
	}

	@Override
	public long countMembershipIndividuals(Long segmentId) {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				"BQMembershipIndividual"
			).where(
				DSL.field(
					"segmentId"
				).eq(
					segmentId
				)
			));
	}

	@Override
	public List<BQMembershipIndividual> getMembershipIndividuals(
		Pageable pageable, Long segmentId) {

		return _queryExecutor.queryForList(
			BQMembershipIndividual::new,
			_dslContext.selectFrom(
				DSL.table("BQMembershipIndividual")
			).where(
				DSL.field(
					"segmentId"
				).eq(
					segmentId
				)
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public void updateMembershipIndividuals() {
		Select select = _getMembershipIndividualsSelect(null);

		if (_environment.acceptsProfiles(Profiles.of("prod"))) {
			_queryExecutor.queryExecute(
				StringUtils.replaceEach(
					_membershipIndividualMergeStatement,
					new String[] {
						"${ac_project_id}",
						"${membership_individuals_select_stmt}"
					},
					new String[] {
						ProjectIdThreadLocal.getProjectId(), select.toString()
					}));

			return;
		}

		_queryExecutor.queryExecute(
			_dslContext.deleteFrom(
				DSL.table("BQMembershipIndividual")
			).where(
				DSL.trueCondition()
			));

		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQMembershipIndividual")
			).columns(
				DSL.field("channelId"), DSL.field("dataSourceUUIDs"),
				DSL.field("individualId"), DSL.field("modifiedDate"),
				DSL.field("segmentId")
			).select(
				select
			));
	}

	@Override
	public void updateMembershipIndividuals(Long segmentId) {
		_queryExecutor.queryExecute(
			_dslContext.deleteFrom(
				DSL.table("BQMembershipIndividual")
			).where(
				DSL.field(
					"segmentId"
				).eq(
					segmentId
				)
			));

		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQMembershipIndividual")
			).columns(
				DSL.field("channelId"), DSL.field("dataSourceUUIDs"),
				DSL.field("individualId"), DSL.field("modifiedDate"),
				DSL.field("segmentId")
			).select(
				_getMembershipIndividualsSelect(segmentId)
			));
	}

	private Select _getMembershipIndividualsSelect(Long segmentId) {
		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"individualId"
			).isNotNull());

		if (segmentId != null) {
			conditions.add(
				DSL.field(
					"segmentId"
				).eq(
					segmentId
				));
		}

		return _dslContext.with(
			"MembershipIndividual"
		).as(
			_dslContext.select(
				DSL.field("channelId"), DSL.field("individualId"),
				DSL.field("modifiedDate"), DSL.field("segmentId")
			).from(
				"BQMembership"
			).where(
				conditions
			).groupBy(
				DSL.field("channelId"), DSL.field("individualId"),
				DSL.field("modifiedDate"), DSL.field("segmentId")
			)
		).select(
			DSL.field("MembershipIndividual.channelId"),
			DSL.field(
				"ARRAY_AGG((SELECT AS STRUCT User.dataSourceId AS " +
					"dataSourceId, User.uuid AS uuid))"
			).as(
				"dataSourceUUIDs"
			),
			DSL.field("MembershipIndividual.individualId"),
			DSL.max(
				DSL.field("MembershipIndividual.modifiedDate")
			).as(
				"modifiedDate"
			),
			DSL.field("MembershipIndividual.segmentId")
		).from(
			"MembershipIndividual"
		).join(
			DSL.table(
				"BQUser"
			).as(
				"User"
			)
		).on(
			DSL.field(
				"MembershipIndividual.individualId"
			).eq(
				DSL.field("User.individualId")
			)
		).where(
			DSL.field(
				"User.uuid"
			).notEqual(
				""
			)
		).groupBy(
			DSL.field("channelId"), DSL.field("individualId"),
			DSL.field("segmentId")
		);
	}

	private final DSLContext _dslContext;
	private final Environment _environment;
	private final String _membershipIndividualMergeStatement;
	private final QueryExecutor _queryExecutor;

}