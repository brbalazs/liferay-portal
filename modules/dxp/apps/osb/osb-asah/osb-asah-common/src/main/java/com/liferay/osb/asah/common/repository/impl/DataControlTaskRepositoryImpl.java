/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.repository.CustomDataControlTaskRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectFinalStep;
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
		@Nullable Long batchId, @Nullable String emailAddress,
		@Nullable Date startCreateDate, @Nullable List<String> statuses,
		@Nullable List<DataControlTask.Type> types) {

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
	public Optional<DataControlTask> findLatestActiveSuppressionDataControlTask(
		String emailAddress) {

		Condition condition = DSL.and(
			DSL.field(
				"emailAddress"
			).eq(
				emailAddress
			),
			DSL.field(
				"status"
			).eq(
				DataControlTaskStatus.COMPLETED.toString()
			),
			DSL.field(
				"type"
			).eq(
				DataControlTask.Type.UNSUPPRESS.toString()
			));

		SelectFinalStep<Record1<Object>> latestUnsuppressDateSelectFinalStep =
			_dslContext.select(
				DSL.max(DSL.field("completeDate"))
			).from(
				"DataControlTask"
			).where(
				condition
			);

		return _dslContext.select(
		).from(
			"DataControlTask"
		).where(
			DSL.and(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddress
				),
				DSL.field(
					"type"
				).eq(
					DataControlTask.Type.SUPPRESS.toString()
				),
				DSL.or(
					DSL.field(
						"status"
					).eq(
						DataControlTaskStatus.COMPLETED.toString()
					),
					DSL.and(
						DSL.field(
							"status"
						).eq(
							DataControlTaskStatus.RUNNING.toString()
						),
						DSL.field(
							"continueDate"
						).isNotNull())),
				DSL.or(
					DSL.notExists(
						_dslContext.select(
						).from(
							"DataControlTask"
						).where(
							condition
						)),
					DSL.field(
						"completeDate"
					).gt(
						latestUnsuppressDateSelectFinalStep
					),
					DSL.field(
						"startDate"
					).gt(
						latestUnsuppressDateSelectFinalStep
					)))
		).orderBy(
			DSL.field(
				"completeDate"
			).desc(),
			DSL.field(
				"startDate"
			).desc()
		).limit(
			1
		).fetchOptional(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public Set<String> findSuppressedEmailAddresses() {
		Field<String> emailAddressField = DSL.field(
			"emailAddress", String.class);
		Field<String> typeField = DSL.field("type", String.class);

		return _dslContext.with(
			"LatestDataControlTasks"
		).as(
			_dslContext.select(
				emailAddressField, typeField,
				DSL.rowNumber(
				).over(
					DSL.partitionBy(
						emailAddressField
					).orderBy(
						DSL.field(
							"compareDate"
						).desc()
					)
				).as(
					"rowNumber"
				)
			).from(
				_dslContext.select(
					emailAddressField, typeField,
					DSL.when(
						DSL.field(
							"completeDate"
						).isNull(),
						DSL.field("startDate")
					).otherwise(
						DSL.field("completeDate")
					).as(
						"compareDate"
					)
				).from(
					"DataControlTask"
				).where(
					DSL.or(
						DSL.and(
							DSL.field(
								"status"
							).eq(
								DataControlTaskStatus.COMPLETED.toString()
							),
							typeField.in(
								DataControlTask.Type.SUPPRESS.toString(),
								DataControlTask.Type.UNSUPPRESS.toString())),
						DSL.and(
							DSL.field(
								"status"
							).eq(
								DataControlTaskStatus.RUNNING.toString()
							),
							typeField.eq(
								DataControlTask.Type.SUPPRESS.toString()),
							DSL.field(
								"continueDate"
							).isNotNull()))
				)
			)
		).selectDistinct(
			emailAddressField
		).from(
			"LatestDataControlTasks"
		).where(
			DSL.field(
				"rowNumber"
			).eq(
				1
			),
			typeField.eq(DataControlTask.Type.SUPPRESS.toString())
		).fetchSet(
			emailAddressField
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
		@Nullable List<DataControlTask.Type> types, Pageable pageable) {

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

	@Override
	public List<DataControlTask> searchDataControlTasks(
		@Nullable String emailAddress, @Nullable Date endCompleteDate,
		@Nullable List<String> statuses,
		@Nullable List<DataControlTask.Type> types) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		return selectSelectStep.from(
			"DataControlTask"
		).where(
			_getConditions(
				null, emailAddress, endCompleteDate, null, statuses, types)
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public List<DataControlTask> searchPendingAccessDataControlTasks() {
		return _dslContext.select(
		).from(
			"DataControlTask"
		).where(
			DSL.or(
				DSL.and(
					DSL.field(
						"status"
					).eq(
						DataControlTaskStatus.PENDING.toString()
					),
					DSL.field(
						"type"
					).in(
						Collections.singletonList(
							DataControlTask.Type.ACCESS.toString())
					)))
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public List<DataControlTask> searchPendingDeleteDataControlTasks() {
		return _dslContext.with(
			"DeleteDataControlTask"
		).as(
			_dslContext.select(
				DSL.field("DataControlTask.*")
			).from(
				"DataControlTask"
			).join(
				_dslContext.select(
				).from(
					"DataControlTask"
				).where(
					_getConditions(
						null, null, null, null,
						Arrays.asList(
							DataControlTaskStatus.COMPLETED.toString(),
							DataControlTaskStatus.ERROR.toString()),
						Collections.singletonList(
							DataControlTask.Type.SUPPRESS))
				).asTable(
					"SuppressDataControlTask"
				)
			).on(
				DSL.and(
					DSL.field(
						"DataControlTask.batchId"
					).eq(
						DSL.field("SuppressDataControlTask.batchId")
					),
					DSL.field(
						"DataControlTask.emailAddress"
					).eq(
						DSL.field("SuppressDataControlTask.emailAddress")
					))
			).where(
				DSL.and(
					DSL.field(
						"DataControlTask.status"
					).eq(
						DataControlTaskStatus.PENDING.toString()
					),
					DSL.field(
						"DataControlTask.type"
					).eq(
						DataControlTask.Type.DELETE.toString()
					))
			)
		).select(
		).from(
			"DeleteDataControlTask"
		).fetch(
			record -> new DataControlTask(record.intoMap())
		);
	}

	@Override
	public List<DataControlTask> searchPendingSuppressDataControlTasks() {
		SelectFinalStep selectFinalStep =
			_getAvailableDataControlTaskSelectFinalStep(
				DataControlTask.Type.SUPPRESS);

		return selectFinalStep.fetch(
			record -> new DataControlTask(record.intoMap()));
	}

	@Override
	public List<DataControlTask> searchPendingUnsuppressDataControlTasks() {
		SelectFinalStep selectFinalStep =
			_getAvailableDataControlTaskSelectFinalStep(
				DataControlTask.Type.UNSUPPRESS);

		return selectFinalStep.fetch(
			record -> new DataControlTask(record.intoMap()));
	}

	private SelectFinalStep _getAvailableDataControlTaskSelectFinalStep(
		DataControlTask.Type type) {

		List<Field> fields = Arrays.asList(
			DSL.field("id"), DSL.field("batchId"), DSL.field("completeDate"),
			DSL.field("continueDate"), DSL.field("createDate"),
			DSL.field("emailAddress"), DSL.field("ownerId"),
			DSL.field("startDate"), DSL.field("status"), DSL.field("type"),
			DSL.field("userId"), DSL.field("userName"));

		return _dslContext.select(
			fields
		).from(
			"DataControlTask"
		).where(
			DSL.field(
				"status"
			).eq(
				DataControlTaskStatus.RUNNING.toString()
			),
			DSL.field(
				"type"
			).eq(
				type.toString()
			),
			DSL.field(
				"continueDate"
			).lt(
				DSL.currentTimestamp()
			)
		).unionAll(
			_dslContext.select(
				fields
			).from(
				_dslContext.select(
					DSL.field("PendingDataControlTask.*"),
					DSL.rowNumber(
					).over(
						DSL.partitionBy(
							DSL.field("PendingDataControlTask.emailAddress"),
							DSL.field("PendingDataControlTask.type")
						).orderBy(
							DSL.field(
								"PendingDataControlTask.id"
							).asc()
						)
					).as(
						"rowNumber"
					)
				).from(
					DSL.table(
						"DataControlTask"
					).as(
						"PendingDataControlTask"
					)
				).leftOuterJoin(
					_dslContext.select(
						DSL.asterisk()
					).from(
						"DataControlTask"
					).where(
						DSL.field(
							"status"
						).eq(
							DataControlTaskStatus.RUNNING.toString()
						),
						DSL.field(
							"type"
						).eq(
							type.toString()
						)
					).asTable(
						"RunningDataControlTask"
					)
				).on(
					DSL.field(
						"PendingDataControlTask.emailAddress"
					).eq(
						DSL.field("RunningDataControlTask.emailAddress")
					)
				).where(
					DSL.field(
						"PendingDataControlTask.status"
					).eq(
						DataControlTaskStatus.PENDING.toString()
					),
					DSL.field(
						"PendingDataControlTask.type"
					).eq(
						type.toString()
					),
					DSL.field(
						"RunningDataControlTask.id"
					).isNull()
				).asTable()
			).where(
				DSL.field(
					"rowNumber"
				).eq(
					1
				)
			)
		);
	}

	private List<Condition> _getConditions(
		@Nullable Long batchId, @Nullable String emailAddress,
		@Nullable Date endCompleteDate, @Nullable Date startCreateDate,
		@Nullable List<String> statuses,
		@Nullable List<DataControlTask.Type> types) {

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
			Stream<DataControlTask.Type> typesStream = types.stream();

			conditions.add(
				DSL.field(
					"type"
				).in(
					typesStream.map(
						DataControlTask.Type::toString
					).collect(
						Collectors.toList()
					)
				));
		}

		return conditions;
	}

	private final DSLContext _dslContext;

}