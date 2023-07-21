/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.util.comparator;

import com.liferay.osb.faro.engine.client.util.OrderByField;
import com.liferay.osb.faro.model.FaroUser;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Matthew Kong
 */
public class FaroUserComparator extends OrderByComparator<FaroUser> {

	public FaroUserComparator(List<OrderByField> orderByFields) {
		_orderByFields = orderByFields;
	}

	@Override
	public int compare(FaroUser faroUser1, FaroUser faroUser2) {
		return 0;
	}

	@Override
	public String getOrderBy() {
		Stream<OrderByField> stream = _orderByFields.stream();

		return stream.map(
			orderByField -> {
				String format = null;

				if (StringUtil.equals(orderByField.getFieldName(), "status")) {
					format = "%s %s";
				}
				else {
					format = "lower(%s) %s";
				}

				return String.format(
					format, _fieldNames.get(orderByField.getFieldName()),
					orderByField.getOrderBy());
			}
		).collect(
			Collectors.joining(StringPool.COMMA)
		);
	}

	private static final Map<String, String> _fieldNames =
		new HashMap<String, String>() {
			{
				put("emailAddress", "OSBFaro_FaroUser.emailAddress");
				put("firstName", "User_.firstName");
				put("lastLoginDate", "User_.lastLoginDate");
				put("lastName", "User_.lastName");
				put("roleName", "Role_.name");
				put("status", "OSBFaro_FaroUser.status");
			}
		};

	private final List<OrderByField> _orderByFields;

}