/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.constants.EventPropertyConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.repository.CustomBQEventPropertyRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.util.BQSQLUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

/**
 * @author Alejo Ceballos
 */
public class BQEventPropertyRepositoryImpl
	extends BaseRepository implements CustomBQEventPropertyRepository {

	public BQEventPropertyRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countValues(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, String keywords) {

		SelectSelectStep<Record1<Integer>> selectCount =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectCount.from(
				_getEventPropertySelectStep(
					channelId, eventAttributeDefinitionName,
					eventDefinitionName,
					DSL.lower(DSL.field("BQEventProperty.value", String.class)),
					keywords)));
	}

	@Override
	public Map<String, Date>
		findBQEventPropertyValuesByEventAttributeDefinitionName(
			String eventAttributeDefinitionName, int size) {

		return _queryExecutor.queryForMap(
			key -> (String)key,
			_dslContext.select(
				DSL.field("BQEventProperty.value", String.class),
				DSL.max(
					DSL.field("BQEventProperty.eventDate", Date.class)
				).as(
					"lastSeenDate"
				)
			).from(
				"BQEventProperty"
			).where(
				DSL.and(
					DSL.field(
						"BQEventProperty.eventDate"
					).ge(
						_dslHelper.getDateParam(
							DateUtil.addDays(DateUtil.newDate(), -7))
					),
					DSL.field(
						"BQEventProperty.name"
					).eq(
						eventAttributeDefinitionName.replace("'", "\\'")
					))
			).groupBy(
				DSL.field("BQEventProperty.value")
			).orderBy(
				DSL.field(
					"lastSeenDate"
				).desc()
			).limit(
				size
			),
			value -> (Date)value);
	}

	@Override
	public BQEventProperty insert(BQEventProperty bqEventProperty) {
		_queryExecutor.queryExecute(
			BQSQLUtil.createInsertStatement(bqEventProperty));

		return bqEventProperty;
	}

	@Override
	public List<String> searchValues(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, String keywords, Pageable pageable) {

		Field<String> selectField = null;

		if (_isGlobalEventAttributeDefinition(eventAttributeDefinitionName)) {
			selectField = DSL.lower(
				DSL.field(
					String.format(
						"BQEvent.%s",
						EventPropertyConstants.globalEventPropertyNames.get(
							eventAttributeDefinitionName)),
					String.class)
			).as(
				"temp"
			);
		}
		else {
			selectField = DSL.lower(
				DSL.field("BQEventProperty.value", String.class)
			).as(
				"temp"
			);
		}

		SelectConditionStep<Record1<String>> eventPropertySelectStep =
			_getEventPropertySelectStep(
				channelId, eventAttributeDefinitionName, eventDefinitionName,
				selectField, keywords);

		return _queryExecutor.queryForList(
			rowMap -> (String)rowMap.get("temp"),
			eventPropertySelectStep.orderBy(
				selectField.asc()
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private Condition _getCondition(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, String keywords) {

		Condition condition = DSL.field(
			"BQEvent.channelId"
		).eq(
			channelId
		).and(
			DSL.field(
				"BQEvent.eventId"
			).eq(
				eventDefinitionName
			)
		);

		if (_isGlobalEventAttributeDefinition(eventAttributeDefinitionName)) {
			condition = condition.and(
				DSL.lower(
					DSL.field(
						String.format(
							"BQEvent.%s",
							EventPropertyConstants.globalEventPropertyNames.get(
								eventAttributeDefinitionName)),
						String.class)
				).like(
					"%" + _getSanitizedKeywords(keywords) + "%"
				));
		}
		else {
			condition = DSL.and(
				condition,
				DSL.field(
					"BQEventProperty.name"
				).eq(
					eventAttributeDefinitionName.replace("'", "\\'")
				),
				DSL.lower(
					DSL.field("BQEventProperty.value", String.class)
				).like(
					"%" + _getSanitizedKeywords(keywords) + "%"
				));
		}

		return condition;
	}

	private SelectConditionStep<Record1<String>> _getEventPropertySelectStep(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, Field<String> selectField,
		String keywords) {

		SelectSelectStep<Record1<String>> selectSelectStep =
			_dslContext.selectDistinct(selectField);

		return selectSelectStep.from(
			"BQEventProperty"
		).join(
			"BQEvent"
		).on(
			DSL.field(
				"BQEvent.id"
			).eq(
				DSL.field("BQEventProperty.id")
			)
		).where(
			_getCondition(
				channelId, eventAttributeDefinitionName, eventDefinitionName,
				keywords)
		);
	}

	private String _getSanitizedKeywords(String keywords) {
		keywords = StringUtil.unquoteAndDecodeInnerQuotes(keywords);

		return StringUtils.lowerCase(keywords.replace("'", "\\'"));
	}

	private boolean _isGlobalEventAttributeDefinition(
		String eventAttributeDefinitionName) {

		return EventPropertyConstants.globalEventPropertyNames.containsKey(
			eventAttributeDefinitionName);
	}

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private QueryExecutor _queryExecutor;

}