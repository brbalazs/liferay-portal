/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter.helper;

import com.liferay.osb.asah.common.converter.helper.DefaultFilterStringConverterHelper;
import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Robson Pastor
 */
public class SegmentFilterStringConverterHelper
	extends DefaultFilterStringConverterHelper {

	public SegmentFilterStringConverterHelper(
		BQMembershipChangeDog bqMembershipChangeDog) {

		_bqMembershipChangeDog = bqMembershipChangeDog;
	}

	@Override
	public Condition getLogicFunctionCondition(
		String fieldName, String operator, boolean processString,
		String valueString) {

		if (fieldName.equals("individualCount")) {
			return DSL.field(
				"id"
			).in(
				_bqMembershipChangeDog.findSegmentIdByFilterString(
					"identitiesCount " + operator + " " + valueString)
			);
		}

		return null;
	}

	@Autowired
	private final BQMembershipChangeDog _bqMembershipChangeDog;

}