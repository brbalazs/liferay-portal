/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter.helper;

import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.Collections;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Robson Pastor
 */
public class SegmentFilterStringConverterHelperTest {

	@Test
	public void test() {
		Mockito.when(
			_bqMembershipChangeDog.findSegmentIdByFilterString(
				"identitiesCount eq 3")
		).thenReturn(
			Collections.singletonList(3L)
		);
		Mockito.when(
			_bqMembershipChangeDog.findSegmentIdByFilterString(
				"identitiesCount gt 3")
		).thenReturn(
			Collections.singletonList(5L)
		);
		Mockito.when(
			_bqMembershipChangeDog.findSegmentIdByFilterString(
				"identitiesCount lt 3")
		).thenReturn(
			Collections.singletonList(2L)
		);

		FilterHelper filterHelper1 = new FilterHelper(
			null, "((individualCount eq 3))",
			_segmentFilterStringConverterHelper);

		Condition condition1 = DSL.field(
			"identitiesCount"
		).eq(
			3L
		);

		Assertions.assertEquals(condition1, filterHelper1.getCondition());

		FilterHelper filterHelper2 = new FilterHelper(
			null, "((individualCount gt 3))",
			_segmentFilterStringConverterHelper);

		Condition condition2 = DSL.field(
			"identitiesCount"
		).gt(
			3L
		);

		Assertions.assertEquals(condition2, filterHelper2.getCondition());

		FilterHelper filterHelper3 = new FilterHelper(
			null, "((individualCount lt 3))",
			_segmentFilterStringConverterHelper);

		Condition condition3 = DSL.field(
			"identitiesCount"
		).lt(
			3L
		);

		Assertions.assertEquals(condition3, filterHelper3.getCondition());
	}

	@MockBean
	private BQMembershipChangeDog _bqMembershipChangeDog = Mockito.mock(
		BQMembershipChangeDog.class);

	private final SegmentFilterStringConverterHelper
		_segmentFilterStringConverterHelper =
			new SegmentFilterStringConverterHelper(_bqMembershipChangeDog);

}