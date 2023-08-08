/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Marcellus Tavares
 */
public class FilterOperators {

	public static FilterOperator of(
		EventAttributeDefinition.DataType dataType, DSLHelper dslHelper,
		String name, List<String> values) {

		FilterOperatorSupplier filterOperatorSupplier = Optional.ofNullable(
			_filterOperatorSuppliers.get(name)
		).orElseThrow(
			() -> new IllegalArgumentException("Invalid operator: " + name)
		);

		return filterOperatorSupplier.get(dataType, dslHelper, values);
	}

	private static final Map<String, FilterOperatorSupplier>
		_filterOperatorSuppliers =
			new HashMap<String, FilterOperatorSupplier>() {
				{
					put("between", BetweenFilterOperator::new);
					put("bin", BinFilterOperator::new);
					put("contains", ContainsFilterOperator::new);
					put("dateGrouping", DateGroupingFilterOperator::new);
					put("endsWith", EndsWithFilterOperator::new);
					put("eq", EqualsFilterOperator::new);
					put("ge", GreaterThanEqualsFilterOperator::new);
					put("gt", GreaterThanFilterOperator::new);
					put("le", LessThanEqualsFilterOperator::new);
					put("lt", LessThanFilterOperator::new);
					put("ne", NotEqualsFilterOperator::new);
					put("notContains", NotContainsFilterOperator::new);
					put("similarTo", SimilarToFilterOperator::new);
					put("startsWith", StartsWithFilterOperator::new);
				}
			};

}