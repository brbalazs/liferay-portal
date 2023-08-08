/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.RunLog;
import com.liferay.osb.asah.common.repository.CustomRunLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public class RunLogRepositoryImpl
	extends BaseRepository implements CustomRunLogRepository {

	public RunLogRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public Optional<RunLog>
		findByDataSourceIdAndNaniteClassNameAndStatusOrderByDateLoggedDesc(
			@Nullable Long dataSourceId, String naniteClassName,
			@Nullable String status) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"RunLog"
		).where(
			_getConditions(dataSourceId, naniteClassName, status)
		).orderBy(
			DSL.field(
				"dateLogged"
			).desc()
		).limit(
			1
		).fetchOptional(
			record -> new RunLog(record.intoMap())
		);
	}

	private List<Condition> _getConditions(
		Long dataSourceId, String naniteClassName, String status) {

		List<Condition> conditions = new ArrayList<>();

		if (dataSourceId != null) {
			conditions.add(
				DSL.field(
					"dataSourceId"
				).eq(
					dataSourceId
				));
		}

		conditions.add(
			DSL.field(
				"naniteClassName"
			).eq(
				naniteClassName
			));

		if (StringUtils.isNotBlank(status)) {
			conditions.add(
				DSL.field(
					"status"
				).eq(
					status
				));
		}

		return conditions;
	}

	private final DSLContext _dslContext;

}