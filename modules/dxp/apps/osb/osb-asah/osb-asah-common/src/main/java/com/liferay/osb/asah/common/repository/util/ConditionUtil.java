/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.util;

import com.liferay.osb.asah.common.converter.helper.DefaultFilterStringConverterHelper;
import com.liferay.osb.asah.common.converter.helper.FilterStringConverterHelper;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 * @author Rachael Koestartyo
 */
public class ConditionUtil {

	public static Condition toCondition(String filterString) {
		return toCondition(
			filterString, new DefaultFilterStringConverterHelper());
	}

	public static Condition toCondition(
		String filterString,
		FilterStringConverterHelper filterStringConverterHelper) {

		if (StringUtils.isEmpty(filterString)) {
			return DSL.noCondition();
		}

		FilterExpression filterExpression = new FilterExpression(
			filterString,
			FilterExpression.FilterType.of(
				filterStringConverterHelper.getFilterType()));

		Condition condition = filterExpression.getCondition();

		if (condition == null) {
			return DSL.noCondition();
		}

		return condition;
	}

	public static List<Condition> toConditions(
		List<Long> dataSourceIds, String keywords,
		String[] keywordsFieldNames) {

		List<Condition> conditions = new ArrayList<>();

		if (!dataSourceIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"dataSourceId"
				).in(
					dataSourceIds
				));
		}

		if (StringUtils.isNotBlank(keywords)) {
			List<Condition> orConditions = new ArrayList<>();

			for (String keywordsFieldName : keywordsFieldNames) {
				orConditions.add(
					DSL.condition(
						String.format(
							"lower(%s) like '%s'", keywordsFieldName,
							"%" + StringUtils.lowerCase(keywords) + "%")));
			}

			conditions.add(DSL.or(orConditions));
		}

		return conditions;
	}

	public static List<Condition> toConditions(Map<String, Object> fields) {
		List<Condition> conditions = new ArrayList<>();

		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			Field<Object> field = DSL.field(
				StringUtils.lowerCase(entry.getKey()));

			conditions.add(field.eq(entry.getValue()));
		}

		return conditions;
	}

}