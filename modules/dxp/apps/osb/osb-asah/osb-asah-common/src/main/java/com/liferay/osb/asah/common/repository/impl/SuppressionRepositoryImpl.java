/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.repository.CustomSuppressionRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep5;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public class SuppressionRepositoryImpl
	extends BaseRepository implements CustomSuppressionRepository {

	public SuppressionRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countSuppressions(@Nullable String emailAddress) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				DSL.table("Suppression")
			).where(
				_getCondition(emailAddress)
			));
	}

	@Override
	public void deleteByEmailAddress(String emailAddress) {
		_queryExecutor.queryExecute(
			_dslContext.deleteFrom(
				DSL.table("Suppression")
			).where(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddress
				)
			));
	}

	@Override
	public List<Suppression> findAll() {
		return _queryExecutor.queryForList(
			Suppression::new, _dslContext.selectFrom(DSL.table("Suppression")));
	}

	@Override
	public Optional<Suppression> findByEmailAddress(String emailAddress) {
		return _queryExecutor.queryForObject(
			Suppression::new,
			_dslContext.select(
			).from(
				DSL.table("Suppression")
			).where(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddress
				)
			));
	}

	@Override
	public List<Suppression> getSuppressions(@Nullable String filterString) {
		Condition condition = DSL.noCondition();

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				filterString);

			condition = filterExpression.getCondition();
		}

		return _queryExecutor.queryForList(
			Suppression::new,
			_dslContext.selectFrom(
				DSL.table("Suppression")
			).where(
				condition
			).orderBy(
				DSL.field(
					"createDate"
				).desc()
			));
	}

	@Override
	public List<Suppression> getSuppressions(
		@Nullable String emailAddress, Pageable pageable) {

		return _queryExecutor.queryForList(
			Suppression::new,
			_dslContext.selectFrom(
				DSL.table("Suppression")
			).where(
				_getCondition(emailAddress)
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public void hideSuppression(String emailAddress) {
		_queryExecutor.queryExecute(
			_dslContext.update(
				DSL.table("Suppression")
			).set(
				DSL.field("hidden", Boolean.class), true
			).where(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddress
				)
			));
	}

	@Override
	public Suppression insert(Suppression suppression) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("Suppression")
			).columns(
				DSL.field("createDate", Object.class),
				DSL.field("dataControlTaskBatchId", Long.class),
				DSL.field("dataControlTaskCreateDate", Object.class),
				DSL.field("emailAddress", String.class),
				DSL.field("hidden", Boolean.class)
			).values(
				_dslHelper.getDateParam(suppression.getCreateDate()),
				suppression.getDataControlTaskBatchId(),
				DateUtil.toUTCString(
					suppression.getDataControlTaskCreateDate(),
					DateUtil.PATTERN_SHORT),
				suppression.getEmailAddress(), suppression.getHidden()
			));

		return suppression;
	}

	@Override
	public void insertAll(List<Suppression> suppressions) {
		InsertValuesStep5<Record, Object, Long, Object, String, Boolean>
			insertValuesStep5 = _dslContext.insertInto(
				DSL.table("Suppression")
			).columns(
				DSL.field("createDate", Object.class),
				DSL.field("dataControlTaskBatchId", Long.class),
				DSL.field("dataControlTaskCreateDate", Object.class),
				DSL.field("emailAddress", String.class),
				DSL.field("hidden", Boolean.class)
			);

		for (Suppression suppression : suppressions) {
			insertValuesStep5 = insertValuesStep5.values(
				DateUtil.toUTCString(
					suppression.getCreateDate(), DateUtil.PATTERN_SHORT),
				suppression.getDataControlTaskBatchId(),
				DateUtil.toUTCString(
					suppression.getDataControlTaskCreateDate(),
					DateUtil.PATTERN_SHORT),
				suppression.getEmailAddress(), suppression.getHidden());
		}

		_queryExecutor.queryExecute(insertValuesStep5);
	}

	@Override
	public void unhideSuppression(String emailAddress) {
		_queryExecutor.queryExecute(
			_dslContext.update(
				DSL.table("Suppression")
			).set(
				DSL.field("hidden", Boolean.class), false
			).where(
				DSL.field(
					"emailAddress"
				).eq(
					emailAddress
				)
			));
	}

	private Condition _getCondition(String emailAddress) {
		Condition condition = DSL.or(
			DSL.field(
				"hidden", Boolean.class
			).isNull(),
			DSL.field(
				"hidden", Boolean.class
			).ne(
				DSL.val(Boolean.TRUE)
			));

		if (StringUtils.isNotBlank(emailAddress)) {
			condition = condition.and(
				DSL.field(
					"emailAddress", String.class
				).like(
					DSL.lower(StringUtils.wrap(emailAddress, "%"))
				));
		}

		return condition;
	}

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private QueryExecutor _queryExecutor;

}