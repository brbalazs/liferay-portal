/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.filter.expression.FilterExpressionParserException;
import com.liferay.osb.asah.common.repository.util.ConditionUtil;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Rachael Koestartyo
 */
public class ConditionUtilTest {

	@Test
	public void testAndOperator() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"column1"
				).eq(
					"value1"
				),
				DSL.field(
					"column2"
				).ne(
					"value2"
				)),
			"column1 eq 'value1' and column2 ne 'value2'");
	}

	@Test
	public void testBooleanFalseValue() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				false
			),
			"column1 eq false");
	}

	@Test
	public void testBooleanTrueValue() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				true
			),
			"column1 eq true");
	}

	@Test
	public void testContainsOperator() {
		_assertEquals(
			DSL.condition(
				DSL.field(
					DSL.lower(
						DSL.field("column1", String.class)
					).like(
						DSL.inline("%value1%")
					))),
			"contains(column1, 'value1')");
	}

	@Test
	public void testDanglingLogicalOperatorThrowsException() {
		_assertThrowsException("column1 eq 'value1' and");
	}

	@Test
	public void testDoubleValue() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				12.34
			),
			"column1 eq 12.34");
	}

	@Test
	public void testEndsWithOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).similarTo(
				"%value1"
			),
			"endsWith(column1, 'value1')");
	}

	@Test
	public void testEqOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				"value1"
			),
			"column1 eq 'value1'");
	}

	@Test
	public void testEscapeOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				"value'1"
			),
			"column1 eq 'value''1'");
	}

	@Test
	public void testFreestyle1() {
		_assertEquals(
			DSL.and(
				DSL.or(
					DSL.field(
						"column1"
					).gt(
						42L
					),
					DSL.or(
						DSL.condition(
							DSL.field(
								DSL.lower(
									DSL.field("column2", String.class)
								).like(
									DSL.inline("%escaped'quote)%")
								))),
						DSL.and(
							DSL.field(
								"column3"
							).ne(
								true
							),
							DSL.field(
								"column4"
							).le(
								97531.8642
							)))),
				DSL.field(
					"column5"
				).isNotNull()),
			"(column1 gt 42 or contains(column2, 'escaped''quote)') or " +
				"(column3 ne true and column4 le 97531.8642)) and column5 ne " +
					"null");
	}

	@Test
	public void testFreestyle2() {
		_assertEquals(
			DSL.or(
				DSL.and(
					DSL.field(
						"column1"
					).ne(
						"null"
					),
					DSL.field(
						"column2"
					).isNotNull()),
				DSL.or(
					DSL.and(
						DSL.field(
							"column3"
						).eq(
							"true"
						),
						DSL.field(
							"column4"
						).eq(
							true
						)),
					DSL.and(
						DSL.field(
							"column5"
						).gt(
							-53.21
						),
						DSL.field(
							"column6"
						).le(
							-8192L
						)))),
			"((column1 ne 'null' and column2 ne null) or ((column3 eq 'true' " +
				"and column4 eq true) or (column5 gt -53.21 and column6 le " +
					"-8192)))");
	}

	@Test
	public void testFreestyle3() {
		_assertEquals(
			DSL.and(
				DSL.or(
					DSL.field(
						"column1"
					).eq(
						"value1"
					),
					DSL.field(
						"column2"
					).eq(
						"value2"
					)),
				DSL.field(
					"column3"
				).eq(
					"value3"
				)),
			"((column1 eq 'value1' or column2 eq 'value2') and column3 eq " +
				"'value3')");
	}

	@Test
	public void testGeOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).ge(
				"value1"
			),
			"column1 ge 'value1'");
	}

	@Test
	public void testGtOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).gt(
				"value1"
			),
			"column1 gt 'value1'");
	}

	@Test
	public void testIncompleteExpressionThrowsException() {
		_assertThrowsException("column1 eq ");
	}

	@Test
	public void testIntegerValue() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				123L
			),
			"column1 eq 123");
	}

	@Test
	public void testInvalidLogicalOperatorThrowsException() {
		_assertThrowsException("column1 eq 'value1' but column2 eq 'value2'");
	}

	@Disabled
	@Test
	public void testInvalidObjectThrowsException() {
		_assertThrowsException("column1 eq value1");
	}

	@Test
	public void testInvalidOperatorThrowsException() {
		_assertThrowsException("column1 is 'value1'");
	}

	@Test
	public void testInvalidStringFunctionArgumentThrowsException() {
		try {
			ConditionUtil.toCondition("matches(column1, 'value1')");

			Assertions.fail(
				"matches(column1, 'value1') did not throw an instance of " +
					"FilterExpressionParserException");
		}
		catch (Exception exception) {
			Assertions.assertTrue(
				exception instanceof FilterExpressionParserException);

			Assertions.assertEquals(
				exception.getMessage(), "Invalid string function: matches");
		}
	}

	@Test
	public void testLeOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).le(
				"value1"
			),
			"column1 le 'value1'");
	}

	@Test
	public void testLtOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).lt(
				"value1"
			),
			"column1 lt 'value1'");
	}

	@Test
	public void testNeNull() {
		_assertEquals(
			DSL.field(
				"column1"
			).isNotNull(),
			"column1 ne null");
	}

	@Test
	public void testNeOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).ne(
				"value1"
			),
			"column1 ne 'value1'");
	}

	@Test
	public void testNotContainsOperator() {
		_assertEquals(
			DSL.not(DSL.condition("LOWER(column1) LIKE '%value1%'")),
			"not contains(column1, 'value1')");
	}

	@Test
	public void testNotEndsWithOperator() {
		_assertEquals(
			DSL.not(
				DSL.field(
					"column1"
				).similarTo(
					"%value1"
				)),
			"not endsWith(column1, 'value1')");
	}

	@Test
	public void testNotStartsWithOperator() {
		_assertEquals(
			DSL.not(
				DSL.field(
					"column1"
				).similarTo(
					"value1%"
				)),
			"not startsWith(column1, 'value1')");
	}

	@Test
	public void testNullValue() {
		_assertEquals(
			DSL.field(
				"column1"
			).isNull(),
			"column1 eq null");
	}

	@Test
	public void testOrderOfOperations1() {
		_assertEquals(
			DSL.or(
				DSL.field(
					"column1"
				).eq(
					"value1"
				),
				DSL.and(
					DSL.field(
						"column2"
					).le(
						"value2"
					),
					DSL.field(
						"column3"
					).ge(
						"value3"
					))),
			"column1 eq 'value1' or (column2 le 'value2' and column3 ge " +
				"'value3')");
	}

	@Test
	public void testOrderOfOperations2() {
		_assertEquals(
			DSL.and(
				DSL.or(
					DSL.field(
						"column1"
					).lt(
						"value1"
					),
					DSL.field(
						"column2"
					).gt(
						"value2"
					)),
				DSL.field(
					"column3"
				).ne(
					"value3"
				)),
			"(column1 lt 'value1' or column2 gt 'value2') and column3 ne " +
				"'value3'");
	}

	@Test
	public void testOrOperator() {
		_assertEquals(
			DSL.or(
				DSL.field(
					"column1"
				).gt(
					"value1"
				),
				DSL.field(
					"column2"
				).lt(
					"value2"
				)),
			"column1 gt 'value1' or column2 lt 'value2'");
	}

	@Test
	public void testParentheses() {
		_assertEquals(
			DSL.field(
				"column1"
			).eq(
				"value)1"
			),
			"((column1 eq 'value)1'))");
	}

	@Test
	public void testStartsWithOperator() {
		_assertEquals(
			DSL.field(
				"column1"
			).similarTo(
				"value1%"
			),
			"startsWith(column1, 'value1')");
	}

	@Test
	public void testUnclosedParenthesisThrowsException() {
		_assertThrowsException("(column1 eq ')'");
	}

	@Test
	public void testUnclosedStringLiteralThrowsException() {
		_assertThrowsException("column1 eq 'escaped quote: ''");
	}

	private void _assertEquals(
		Condition expectedCondition, String actualFilterString) {

		Condition actualCondition = ConditionUtil.toCondition(
			actualFilterString);

		Assertions.assertEquals(
			expectedCondition.toString(), actualCondition.toString());
	}

	private void _assertThrowsException(String filterString) {
		try {
			ConditionUtil.toCondition(filterString);

			Assertions.fail(
				filterString + " did not throw an instance of " +
					"ParseCancellationException");
		}
		catch (Exception exception) {
			Throwable cause = exception.getCause();

			Assertions.assertTrue(cause instanceof ParseCancellationException);
		}
	}

}