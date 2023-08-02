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

package com.liferay.osb.asah.common.model.filter;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Collections;

import org.jooq.impl.DSL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Leslie Wong
 */
public class FilterOperatorTest {

	@BeforeEach
	public void setUp() {
		Environment environment = Mockito.mock(Environment.class);

		Mockito.when(
			environment.getProperty(
				Mockito.eq("GOOGLE_APPLICATION_CREDENTIALS"))
		).thenReturn(
			null
		);

		ReflectionTestUtils.setField(_dslHelper, "_environment", environment);
	}

	@Test
	public void testBetweenFilterOperatorDate() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DATE, _dslHelper, "between",
			new ArrayList<String>() {
				{
					add("2021-06-01");
					add("2021-06-23");
				}
			});

		LocalDate endLocalDate = LocalDate.parse("2021-06-23");
		LocalDate startLocalDate = LocalDate.parse("2021-06-01");

		Assertions.assertEquals(
			DSL.and(
				DSL.field(
					"testField"
				).ge(
					DSL.date(
						DateUtil.toDate(
							startLocalDate.atStartOfDay(), ZoneOffset.UTC))
				),
				DSL.field(
					"testField"
				).le(
					DSL.date(
						DateUtil.toDate(
							endLocalDate.atStartOfDay(), ZoneOffset.UTC))
				)),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testBetweenFilterOperatorDuration() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "between",
			new ArrayList<String>() {
				{
					add("3600");
					add("36000");
				}
			});

		Assertions.assertEquals(
			DSL.and(
				DSL.field(
					"testField"
				).ge(
					3600
				),
				DSL.field(
					"testField"
				).le(
					36000
				)),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testBetweenFilterOperatorNumber() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.NUMBER, _dslHelper, "between",
			new ArrayList<String>() {
				{
					add("20");
					add("27");
				}
			});

		Assertions.assertEquals(
			DSL.and(
				DSL.field(
					"testField"
				).ge(
					20.0
				),
				DSL.field(
					"testField"
				).le(
					27.0
				)),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testContainsFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "contains",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.condition(
				DSL.field(
					DSL.lower(
						DSL.trim(
							DSL.replace(
								DSL.coalesce(
									DSL.field("testField", String.class), ""),
								"\n", ""))
					).like(
						DSL.inline("%testvalue%")
					))),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testContainsFilterOperatorWithApostrophe() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "contains",
			Collections.singletonList("test'Value"));

		Assertions.assertEquals(
			DSL.condition(
				DSL.field(
					DSL.lower(
						DSL.trim(
							DSL.replace(
								DSL.coalesce(
									DSL.field("testField", String.class), ""),
								"\n", ""))
					).like(
						DSL.inline("%test\\'value%")
					))),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testEndsWithFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "endsWith",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).endsWithIgnoreCase(
				"testValue"
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testEqualsFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "eq",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).eq(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testEqualsFilterOperatorNull() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "eq",
			Collections.singletonList(null));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).isNull(),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testEqualsFilterOperatorString() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "eq",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.field(
				"testField", String.class
			).equalIgnoreCase(
				"testValue"
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testFilterOperatorBadArgumentsCount() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> FilterOperators.of(
				EventAttributeDefinition.DataType.STRING, _dslHelper,
				"contains",
				new ArrayList<String>() {
					{
						add("test1");
						add("test2");
					}
				}));
	}

	@Test
	public void testFilterOperatorUnsupportedDataType() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> FilterOperators.of(
				EventAttributeDefinition.DataType.STRING, _dslHelper, "gt",
				new ArrayList<String>() {
					{
						add("test");
					}
				}));
	}

	@Test
	public void testGreaterThanEqualsFilterOperatorDate() {
		String dateString = "2021-06-01";

		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DATE, _dslHelper, "ge",
			Collections.singletonList(dateString));

		LocalDate localDate = LocalDate.parse(dateString);

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).ge(
				DSL.date(
					DateUtil.toDate(localDate.atStartOfDay(), ZoneOffset.UTC))
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testGreaterThanEqualsFilterOperatorDuration() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "ge",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).ge(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testGreaterThanEqualsFilterOperatorNumber() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.NUMBER, _dslHelper, "ge",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).ge(
				123.0
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testGreaterThanFilterOperatorDate() {
		String dateString = "2021-06-01";

		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DATE, _dslHelper, "gt",
			Collections.singletonList(dateString));

		LocalDate localDate = LocalDate.parse(dateString);

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).gt(
				DSL.date(
					DateUtil.toDate(localDate.atStartOfDay(), ZoneOffset.UTC))
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testGreaterThanFilterOperatorDuration() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "gt",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).gt(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testGreaterThanFilterOperatorNumber() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.NUMBER, _dslHelper, "gt",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).gt(
				123.0
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanEqualsFilterOperatorDate() {
		String dateString = "2020-06-01";

		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DATE, _dslHelper, "le",
			Collections.singletonList(dateString));

		LocalDate localDate = LocalDate.parse(dateString);

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).le(
				DSL.date(
					DateUtil.toDate(localDate.atStartOfDay(), ZoneOffset.UTC))
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanEqualsFilterOperatorDuration() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "le",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).le(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanEqualsFilterOperatorNumber() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.NUMBER, _dslHelper, "le",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).le(
				123.0
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanFilterOperatorDate() {
		String dateString = "2021-06-01";

		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DATE, _dslHelper, "lt",
			Collections.singletonList(dateString));

		LocalDate localDate = LocalDate.parse(dateString);

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).lt(
				DSL.date(
					DateUtil.toDate(localDate.atStartOfDay(), ZoneOffset.UTC))
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanFilterOperatorDuration() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "lt",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).lt(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testLessThanFilterOperatorNumber() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.NUMBER, _dslHelper, "lt",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).lt(
				123.0
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testNotContainsFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "notContains",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.not(
				DSL.condition(
					DSL.field(
						DSL.lower(
							DSL.trim(
								DSL.replace(
									DSL.coalesce(
										DSL.field("testField", String.class),
										""),
									"\n", ""))
						).like(
							DSL.inline("%testvalue%")
						)))),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testNotEqualsFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.DURATION, _dslHelper, "ne",
			Collections.singletonList("123"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).ne(
				123
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testNotEqualsFilterOperatorNull() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "ne",
			Collections.singletonList(null));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).isNotNull(),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testNotEqualsFilterOperatorString() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "ne",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.field(
				"testField", String.class
			).notEqualIgnoreCase(
				"testValue"
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testSimilarToFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "similarTo",
			Collections.singletonList("test.*Value"));

		Assertions.assertEquals(
			DSL.lower(
				DSL.field("testField", String.class)
			).similarTo(
				"test_%value"
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	@Test
	public void testStartsWithFilterOperator() {
		FilterOperator filterOperator = FilterOperators.of(
			EventAttributeDefinition.DataType.STRING, _dslHelper, "startsWith",
			Collections.singletonList("testValue"));

		Assertions.assertEquals(
			DSL.field(
				"testField"
			).startsWithIgnoreCase(
				"testValue"
			),
			filterOperator.getCondition(DSL.field("testField")));
	}

	private final DSLHelper _dslHelper = new DSLHelper();

}