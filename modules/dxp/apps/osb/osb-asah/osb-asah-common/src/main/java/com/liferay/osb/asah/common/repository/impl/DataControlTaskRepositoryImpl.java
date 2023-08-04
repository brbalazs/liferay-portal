/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.DataControlTaskType;
import com.liferay.osb.asah.common.repository.CustomDataControlTaskRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public class DataControlTaskRepositoryImpl
	extends BaseRepository implements CustomDataControlTaskRepository {

	public DataControlTaskRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countDataControlTasks(
		Long batchId, String emailAddress, Date startCreateDate,
		List<String> statuses, List<String> types) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"DataControlTask"
		).where(
			_getConditions(
				batchId, emailAddress, null, startCreateDate, statuses, types)
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public Boolean existsByBatchIdAndStatusIn(
		@Nullable Long batchId, @Nullable List<String> statuses) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return _dslContext.fetchExists(
			selectSelectStep.from(
				"DataControlTask"
			).where(
				_getConditions(batchId, null, null, null, statuses, null)
			));
	}

	@Override
	public Set<String> findSuppressedEmailAddresses() {
		Field<String> emailAddressField = DSL.field(
			"emailAddress", String.class);

		return _dslContext.selectDistinct(
			emailAddressField
		).from(
			"DataControlTask"
		).where(
			DSL.field(
				"status"
			).eq(
				DataControlTaskStatus.COMPLETED.toString()
			),
			DSL.field(
				"type"
			).in(
				DataControlTaskType.DELETE.toString(),
				DataControlTaskType.SUPPRESS.toString()
			)
		).fetchSet(
			emailAddressField
		);
	}

	@Override
	public List<DataControlTask> searchDataControlTasks(
		@Nullable Date endCompleteDate, @Nullable List<String> statuses,
		@Nullable List<String> types) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DataControlTask"
		).where(
			_getConditions(null, null, endCompleteDate, null, statuses, types)
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public List<DataControlTask> searchDataControlTasks(
		FilterHelper filterHelper, @Nullable String status) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(filterHelper.getCondition());

		if ((status != null) && !status.isEmpty()) {
			conditions.add(
				DSL.field(
					"status"
				).in(
					status
				));
		}

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DataControlTask"
		).where(
			conditions
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public List<DataControlTask> searchDataControlTasks(
		@Nullable Long batchId, @Nullable String emailAddress,
		@Nullable Date startCreateDate, @Nullable List<String> statuses,
		@Nullable List<String> types, Pageable pageable) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DataControlTask"
		).where(
			_getConditions(
				batchId, emailAddress, null, startCreateDate, statuses, types)
		).orderBy(
			getSortFields(pageable.getSort(), null)
		).limit(
			pageable.getPageSize()
		).offset(
			pageable.getOffset()
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	private List<Condition> _getConditions(
		@Nullable Long batchId, @Nullable String emailAddress,
		@Nullable Date endCompleteDate, @Nullable Date startCreateDate,
		@Nullable List<String> statuses, @Nullable List<String> types) {

		List<Condition> conditions = new ArrayList<>();

		if (batchId != null) {
			conditions.add(
				DSL.field(
					"batchId"
				).eq(
					batchId
				));
		}

		if (!StringUtils.isBlank(emailAddress)) {
			conditions.add(
				DSL.field(
					"emailAddress"
				).containsIgnoreCase(
					emailAddress
				));
		}

		if (endCompleteDate != null) {
			conditions.add(
				DSL.field(
					"completeDate"
				).lessThan(
					endCompleteDate
				));
		}

		if (startCreateDate != null) {
			conditions.add(
				DSL.field(
					"createDate"
				).greaterOrEqual(
					startCreateDate
				));
		}

		if ((statuses != null) && !statuses.isEmpty()) {
			conditions.add(
				DSL.field(
					"status"
				).in(
					statuses
				));
		}

		if ((types != null) && !types.isEmpty()) {
			conditions.add(
				DSL.field(
					"type"
				).in(
					types
				));
		}

		return conditions;
	}

	private final DSLContext _dslContext;

}