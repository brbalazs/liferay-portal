/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.helper;

import com.liferay.osb.asah.common.date.DateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class DSLHelper {

	public Field concat(Field... fields) {
		if (isBigQueryDialect()) {
			StringBuffer stringBuffer = new StringBuffer("CONCAT(");

			Stream<Field> stream = Arrays.stream(fields);

			Iterator<Field> iterator = stream.iterator();

			while (iterator.hasNext()) {
				stringBuffer.append(String.valueOf(iterator.next()));

				if (iterator.hasNext()) {
					stringBuffer.append(",");
				}
			}

			stringBuffer.append(")");

			return DSL.field(stringBuffer.toString());
		}

		return DSL.concat(fields);
	}

	public Condition containsSubstring(String fieldName, String value) {
		if (isBigQueryDialect()) {
			String formattedValue = StringUtils.lowerCase(
				StringUtils.trim(StringUtils.replace(value, "\n", "")));

			return DSL.lower(
				DSL.trim(
					DSL.replace(
						DSL.coalesce(DSL.field(fieldName, String.class), ""),
						"\n", ""))
			).like(
				DSL.inline("%" + formattedValue + "%")
			);
		}

		return DSL.field(
			fieldName
		).containsIgnoreCase(
			value
		);
	}

	public Field<Integer> countIf(Condition condition) {
		if (isBigQueryDialect()) {
			return DSL.field("COUNTIF({0})", Integer.class, condition);
		}

		return DSL.field("COUNT(({0}) OR null)", Integer.class, condition);
	}

	public Field<OffsetDateTime> dateDiff(
		DatePart datePart, Field<OffsetDateTime> endDateField,
		Field<OffsetDateTime> startDateField) {

		if (isBigQueryDialect()) {
			return DSL.field(
				"DATE_DIFF({0}, {1}, {2})", endDateField.getDataType(),
				endDateField, startDateField, datePart.toName());
		}

		if (datePart == DatePart.WEEK) {
			return DSL.field(
				"CAST(EXTRACT('day' FROM (CAST({0} AS TIMESTAMP) - CAST({1} " +
					"AS TIMESTAMP))) / 7 AS INT)",
				endDateField.getDataType(), endDateField, startDateField);
		}

		return DSL.field(
			"DATE_PART({0}, AGE({1}, {2}))", endDateField.getDataType(),
			datePart.toSQL(), endDateField, startDateField);
	}

	public Field<OffsetDateTime> dateTrunc(
		DatePart datePart, Field<OffsetDateTime> field) {

		if (isBigQueryDialect()) {
			if ((datePart == DatePart.HOUR) || (datePart == DatePart.DAY)) {
				return DSL.field(
					"DATETIME_TRUNC({0}, {1})", field.getDataType(), field,
					datePart.toName());
			}

			return DSL.field(
				"DATE_TRUNC({0}, {1})", field.getDataType(), field,
				datePart.toName());
		}

		if (datePart == DatePart.WEEK) {
			return DSL.field(
				"DATE_TRUNC({0}, {1} + INTERVAL '1 DAY') - INTERVAL '1 DAY'",
				field.getDataType(), datePart.toSQL(), field);
		}

		return DSL.field(
			"DATE_TRUNC({0}, {1})", field.getDataType(), datePart.toSQL(),
			field);
	}

	public Field formatDate(String dateFormat, Field field) {
		return DSL.field(
			"format_date({0}, {1})", field.getDataType(), dateFormat, field);
	}

	public Field getCastBooleanField(Field field) {
		if (isBigQueryDialect()) {
			return DSL.field("SAFE_CAST({0} as BOOL)", field);
		}

		return DSL.function("try_cast_boolean", Boolean.class, field);
	}

	public Field getCastDurationField(Field field) {
		if (isBigQueryDialect()) {
			return DSL.field("SAFE_CAST({0} as INT64)", field);
		}

		return DSL.round(
			DSL.abs(DSL.function("try_cast_bigint", BigInteger.class, field)),
			-3);
	}

	public Field getCastNumberField(Field field) {
		if (isBigQueryDialect()) {
			return DSL.field("ROUND(SAFE_CAST({0} as BIGNUMERIC))", field);
		}

		return DSL.round(
			DSL.function("try_cast_float", BigDecimal.class, field));
	}

	public Field getCastStringField(Field field) {
		if (isBigQueryDialect()) {
			return DSL.field("SAFE_CAST({0} as STRING)", field);
		}

		return field.cast(String.class);
	}

	public Field getDateAtTimeZoneField(String fieldName, String timeZoneId) {
		if (isBigQueryDialect()) {
			return DSL.field(
				String.format("DATETIME(%s, '%s')", fieldName, timeZoneId),
				OffsetDateTime.class);
		}

		if (timeZoneId.equals("UTC")) {
			return DSL.field(fieldName, OffsetDateTime.class);
		}

		return DSL.field(
			String.format("%s AT TIME ZONE '%s'", fieldName, timeZoneId),
			OffsetDateTime.class);
	}

	public Object getDateParam(Date date) {
		if (isBigQueryDialect()) {
			return DSL.field("TIMESTAMP '" + DateUtil.toUTCString(date) + "'");
		}

		return date;
	}

	public Object getDateParam(LocalDateTime localDateTime, String timeZoneId) {
		localDateTime = DateUtil.toUTCLocalDateTime(
			localDateTime, ZoneId.of(timeZoneId));

		if (isBigQueryDialect()) {
			return DSL.field(
				"TIMESTAMP '" + DateUtil.toUTCString(localDateTime) + "'");
		}

		return localDateTime;
	}

	public Object getDateValue(Date date) {
		if (date == null) {
			return null;
		}

		return DSL.date(date);
	}

	public Field<OffsetDateTime> getDateValueField(
		Field field, String timeZoneId) {

		if (isBigQueryDialect()) {
			return DSL.field(
				"SAFE_CAST({0} as TIMESTAMP)", OffsetDateTime.class, field);
		}

		StringBuilder sb = new StringBuilder();

		sb.append(DSL.function("try_cast_timestamp", Object.class, field));
		sb.append(" AT TIME ZONE 'UTC'");

		if (!timeZoneId.equals("UTC")) {
			sb.append(" AT TIME ZONE '");
			sb.append(timeZoneId);
			sb.append("'");
		}

		return DSL.field(sb.toString(), OffsetDateTime.class);
	}

	public Field<?> getDayOfWeekField(Field field) {
		if (isBigQueryDialect()) {
			return DSL.field(
				String.format("EXTRACT(DAYOFWEEK FROM %s)", field));
		}

		return DSL.dayOfWeek(field);
	}

	public Field<?> getField(Field<?> bigQueryField, Field<?> postgresqlField) {
		if (isBigQueryDialect()) {
			return bigQueryField;
		}

		return postgresqlField;
	}

	public Field<String> getNestedField(
		String keyAttribute, String nestedField, String valueAttribute,
		String wrapperColumn) {

		if (isBigQueryDialect()) {
			return DSL.field(
				String.format(
					"(SELECT %s FROM UNNEST(%s) WHERE %s = {0})",
					valueAttribute, wrapperColumn, keyAttribute),
				String.class, nestedField);
		}

		String sql =
			"(SELECT %s FROM JSON_TO_RECORDSET(%s) AS (%s TEXT, %s TEXT) " +
				"WHERE %s = {0})";

		return DSL.field(
			String.format(
				sql, valueAttribute, wrapperColumn, keyAttribute,
				valueAttribute, keyAttribute),
			String.class, nestedField);
	}

	public Table<Record> getTimeSeriesTable(
		DatePart datePart, Timestamp timestamp1, Timestamp timestamp2) {

		if (isBigQueryDialect()) {
			return DSL.selectFrom(
				String.format(
					"UNNEST(GENERATE_DATE_ARRAY(DATE(%s), DATE(%s), INTERVAL " +
						"1 %s)) AS generatedDate",
					DSL.field(
						"TIMESTAMP '" + DateUtil.toUTCString(timestamp1) + "'"),
					DSL.field(
						"TIMESTAMP '" + DateUtil.toUTCString(timestamp2) + "'"),
					datePart.toSQL())
			).asTable();
		}

		return DSL.table(
			String.format(
				"GENERATE_SERIES({0}, {1}, '1 %s'::INTERVAL) AS generatedDate",
				datePart.toSQL()),
			timestamp1, timestamp2);
	}

	public boolean isBigQueryDialect() {
		return true;
	}

	public Field<Object> jsonExtractScalar(String fieldName, String key) {
		if (isBigQueryDialect()) {
			return DSL.field(
				String.format(
					"JSON_EXTRACT_SCALAR(%s, '$.%s')", fieldName, key));
		}

		return DSL.field(
			String.format(
				"JSON_EXTRACT_PATH_TEXT(%s::json, '%s')", fieldName, key));
	}

	public Condition regexpContains(String fieldName, String regexp) {
		if (isBigQueryDialect()) {
			return DSL.condition(
				String.format("REGEXP_CONTAINS(%s, r'%s')", fieldName, regexp));
		}

		return DSL.condition(
			String.format("%s ~ '^.*%s.*$'", fieldName, regexp));
	}

	public Field<String> regexpExtract(Field<String> field, String regexp) {
		if (isBigQueryDialect()) {
			return DSL.field(
				String.format("REGEXP_EXTRACT(%s, r'%s')", field, regexp),
				String.class);
		}

		return DSL.field(
			String.format("(REGEXP_MATCH(%s, '%s'))[1]", field, regexp),
			String.class);
	}

	public Field<String> regexpReplace(
		String fieldName, String regexp, String value) {

		if (isBigQueryDialect()) {
			return DSL.field(
				String.format(
					"REGEXP_REPLACE(%s, r'%s', '%s')", fieldName, regexp,
					value),
				String.class);
		}

		return DSL.field(
			String.format(
				"REGEXP_REPLACE(%s, '%s', '%s')", fieldName, regexp, value),
			String.class);
	}

	@Autowired
	private Environment _environment;

}