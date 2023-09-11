/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.filter.expression;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.findbugs.SuppressFBWarnings;
import com.liferay.osb.asah.common.util.StringUtil;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.core.io.ClassPathResource;

/**
 * @author Marcellus Tavares
 */
public class FilterExpressionTest {

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
	public void testBetweenFunction() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"Session.deviceType"
				).eq(
					"app"
				),
				DSL.field(
					"Session.sessionEnd"
				).between(
					"2022-05-15", "2022-05-20"
				),
				DSL.field(
					"cast(Session.sessionEnd as date)"
				).gt(
					DSL.function("DATE", Date.class, DSL.val("2022-05-15"))
				)),
			"(sessions.filter(filter='(context/deviceType eq ''app'' and " +
				"between(completeDate,''2022-05-15'',''2022-05-20'') and " +
					"completeDate gt ''2022-05-15'')'))");
	}

	@Test
	public void testBetweenFunctionWithZoneId() {
		TimeZoneDog timeZoneDog = Mockito.mock(TimeZoneDog.class);

		Mockito.when(
			timeZoneDog.getZoneId()
		).thenReturn(
			ZoneId.of("Japan")
		);

		TimeZoneDogUtil.setTimeZoneDog(timeZoneDog);

		_assertEquals(
			DSL.and(
				DSL.field(
					"Session.deviceType"
				).eq(
					"app"
				),
				DSL.field(
					"Session.sessionEnd"
				).between(
					"2022-05-14", "2022-05-19"
				),
				DSL.field(
					"cast(Session.sessionEnd as date)"
				).gt(
					DSL.function("DATE", Date.class, DSL.val("2022-05-14"))
				)),
			"(sessions.filter(filter='(context/deviceType eq ''app'' and " +
				"between(completeDate,''2022-05-15'',''2022-05-20'') and " +
					"completeDate gt ''2022-05-15'')'))");
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
	public void testCastOperatorBoolean() {
		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.defaultUser AS BOOLEAN)"
			).eq(
				true
			),
			"cast(demographics/defaultUser/value, 'BOOLEAN') eq true", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.defaultUser AS BOOLEAN)"
			).eq(
				false
			),
			"cast(demographics/defaultUser/value, 'BOOLEAN') eq false", true);
	}

	@Test
	public void testCastOperatorDate() {
		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.birthday AS DATE)"
			).eq(
				"2023-02-16"
			),
			"cast(demographics/birthDate/value, 'DATE') eq '2023-02-16'", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.birthday AS DATE)"
			).gt(
				"2023-02-16"
			),
			"cast(demographics/birthDate/value, 'DATE') gt '2023-02-16'", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.birthday AS DATE)"
			).lt(
				"2023-02-16"
			),
			"cast(demographics/birthDate/value, 'DATE') lt '2023-02-16'", true);
	}

	@Test
	public void testCastOperatorNumeric() {
		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.contactId AS NUMERIC)"
			).eq(
				0L
			),
			"cast(demographics/contactId/value, 'NUMBER') eq 0", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.contactId AS NUMERIC)"
			).ne(
				0L
			),
			"cast(demographics/contactId/value, 'NUMBER') ne 0", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.contactId AS NUMERIC)"
			).gt(
				0L
			),
			"cast(demographics/contactId/value, 'NUMBER') gt 0", true);

		_assertEquals(
			DSL.field(
				"SAFE_CAST(Individual.contactId AS NUMERIC)"
			).lt(
				0L
			),
			"cast(demographics/contactId/value, 'NUMBER') lt 0", true);

		_assertEquals(
			DSL.or(
				DSL.field(
					"Individual.contactId"
				).isNull(),
				DSL.field(
					"Individual.contactId"
				).eq(
					""
				)),
			"demographics/contactId/value eq null", true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"Individual.contactId"
				).isNotNull(),
				DSL.field(
					"Individual.contactId"
				).ne(
					""
				)),
			"demographics/contactId/value ne null", true);
	}

	@Test
	public void testContainsOperator() {
		_assertEquals(
			DSL.lower(
				DSL.field("column1", String.class)
			).like(
				DSL.inline("%value1%")
			),
			"contains(column1, 'value1')");
	}

	@Test
	public void testContainsOperatorCustomField() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(",
						"IndividualFields_custom_field.value, '[') AND ",
						"ENDS_WITH(IndividualFields_custom_field.value, ']') ",
						"THEN (EXISTS (SELECT value FROM UNNEST(",
						"JSON_EXTRACT_STRING_ARRAY(",
						"IndividualFields_custom_field.value,'$')) AS value ",
						"WHERE LOWER(value) LIKE '%value1%')) ELSE ",
						"LOWER(IndividualFields_custom_field.value) ",
						"LIKE '%value1%' END"))),
			"contains(custom/custom_field/value, 'value1')", true);
	}

	@Test
	public void testDanglingLogicalOperatorThrowsException() {
		_assertThrowsException(
			"column1 eq 'value1' and",
			"Parsed 'and' as a logical operator, but no operand was found " +
				"after it");
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
	public void testEqualOperatorCustomField() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"ExpandoValue_Boolean_Field.fieldName"
				).eq(
					"Boolean_Field"
				),
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(",
						"ExpandoValue_Boolean_Field.value, '[') AND ENDS_WITH(",
						"ExpandoValue_Boolean_Field.value, ']') THEN ( EXISTS ",
						"(SELECT value FROM UNNEST(JSON_EXTRACT_STRING_ARRAY(",
						"ExpandoValue_Boolean_Field.value,'$')) AS value ",
						"WHERE LOWER(value) = 'false')) ELSE LOWER(",
						"ExpandoValue_Boolean_Field.value) = 'false' END"))),
			"(custom/Boolean_Field/value eq 'false')");

		_assertEquals(
			DSL.and(
				DSL.field(
					"ExpandoValue_05_IsHeadQuater.fieldName"
				).eq(
					"05_IsHeadQuater"
				),
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(",
						"ExpandoValue_05_IsHeadQuater.value, '[') AND ",
						"ENDS_WITH(ExpandoValue_05_IsHeadQuater.value, ']') ",
						"THEN ( EXISTS (SELECT value FROM UNNEST(",
						"JSON_EXTRACT_STRING_ARRAY(",
						"ExpandoValue_05_IsHeadQuater.value,'$')) AS value",
						" WHERE LOWER(value) = 'false')) ELSE LOWER(",
						"ExpandoValue_05_IsHeadQuater.value) = 'false' END"))),
			"(custom/05_IsHeadQuater/value eq 'false')");
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
			DSL.or(
				DSL.field(
					"column1", Long.class
				).gt(
					42L
				),
				DSL.and(
					DSL.or(
						DSL.lower(
							DSL.field("column2", String.class)
						).like(
							DSL.inline("%escaped'quote)%")
						),
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
							))),
					DSL.field(
						"column5"
					).isNotNull())),
			_testFilters.get("testFreestyle1"));
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
							"column6", Long.class
						).le(
							-8192L
						)))),
			_testFilters.get("testFreestyle2"));
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
			_testFilters.get("testFreestyle3"));
	}

	@Test
	public void testFreestyle5() {
		_assertEquals(
			DSL.lower(
				DSL.field("Individual.jobTitle", String.class)
			).like(
				DSL.inline("%manager%")
			),
			"contains(demographics/jobTitle/value, 'manager')", true);
	}

	@Test
	public void testFreestyle6() {
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		_assertEquals(
			DSL.and(
				DSL.lower(
					DSL.field("Individual.addresses", String.class)
				).eq(
					"address1"
				),
				DSL.field(
					"Session.browserName"
				).eq(
					"browser1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.lower(
					DSL.field("Individual.addresses", String.class)
				).eq(
					"address2"
				)),
			_testFilters.get("testFreestyle6"),
			new HashSet<>(Arrays.asList("Individual", "Session")), true);
	}

	@Test
	public void testFreestyle7() {
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		_assertEquals(
			DSL.or(
				DSL.and(
					DSL.field(
						"Session.country"
					).eq(
						"country1"
					),
					DSL.field(
						"Session.sessionEnd"
					).gt(
						localDateTime.minusHours(24)
					)),
				DSL.and(
					DSL.condition("'url1' IN UNNEST(Session.urls)"),
					DSL.field(
						"Session.sessionEnd"
					).gt(
						localDateTime.minusHours(24)
					))),
			_testFilters.get("testFreestyle7"),
			new HashSet<>(Arrays.asList("Session")));
	}

	@Test
	public void testFreestyle8() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Individual.id", String.class)
					).from(
						DSL.table(
							"BQIndividual"
						).as(
							"Individual"
						)
					).crossJoin(
						DSL.table(
							"UNNEST(Individual.memberships) AS " +
								"IndividualMemberships")
					).join(
						DSL.table(
							"BQOrganization"
						).as(
							"Organization"
						)
					).on(
						DSL.condition(
							"Organization.id IN UNNEST(" +
								"IndividualMemberships.ids)")
					).where(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"organizationIds"
						),
						DSL.field(
							"Organization.name"
						).eq(
							"name1"
						)
					)
				),
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Individual.id", String.class)
					).from(
						DSL.table(
							"BQIndividual"
						).as(
							"Individual"
						)
					).crossJoin(
						DSL.table(
							"UNNEST(Individual.memberships) AS " +
								"IndividualMemberships")
					).join(
						DSL.table(
							"BQOrganization"
						).as(
							"Organization"
						)
					).on(
						DSL.condition(
							"Organization.id IN UNNEST(" +
								"IndividualMemberships.ids)")
					).where(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"organizationIds"
						),
						DSL.field(
							"Organization.type"
						).eq(
							"type1"
						)
					)
				)),
			_testFilters.get("testFreestyle8"),
			new HashSet<>(Arrays.asList("Individual", "Organization")));
	}

	@Test
	public void testFreestyle9() {
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		_assertEquals(
			DSL.and(
				DSL.lower(
					DSL.field("Individual.addresses", String.class)
				).eq(
					"address1"
				),
				DSL.field(
					"Session.country"
				).eq(
					"country1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.lower(
					DSL.field("Individual.emailAddress", String.class)
				).eq(
					"emailaddress1"
				),
				DSL.condition("'url1' IN UNNEST(Session.urls)"),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.field(
					"Session.sessionEnd"
				).eq(
					"2022-12-20T16:56:05.761Z"
				),
				DSL.field(
					"Session.deviceType"
				).eq(
					"deviceType1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.field(
					"Session.browserName"
				).eq(
					"browserName1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Individual.id", String.class)
					).from(
						DSL.table(
							"BQIndividual"
						).as(
							"Individual"
						)
					).crossJoin(
						DSL.table(
							"UNNEST(Individual.memberships) AS " +
								"IndividualMemberships")
					).join(
						DSL.table(
							"BQOrganization"
						).as(
							"Organization"
						)
					).on(
						DSL.condition(
							"Organization.id IN UNNEST(" +
								"IndividualMemberships.ids)")
					).where(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"organizationIds"
						),
						DSL.field(
							"Organization.name"
						).eq(
							"name1"
						)
					)
				)),
			_testFilters.get("testFreestyle9"),
			new HashSet<>(
				Arrays.asList("Individual", "Organization", "Session")),
			true);
	}

	@Test
	public void testFreestyle10() {
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		_assertEquals(
			DSL.and(
				DSL.or(
					DSL.lower(
						DSL.field("Individual.addresses", String.class)
					).eq(
						"address1"
					),
					DSL.and(
						DSL.field(
							"Session.country"
						).eq(
							"country1"
						),
						DSL.field(
							"Session.sessionEnd"
						).gt(
							localDateTime.minusHours(24)
						),
						DSL.lower(
							DSL.field("Individual.emailAddress", String.class)
						).eq(
							"emailaddress1"
						),
						DSL.condition("'url1' IN UNNEST(Session.urls)"),
						DSL.field(
							"Session.sessionEnd"
						).gt(
							localDateTime.minusHours(24)
						),
						DSL.field(
							"Session.sessionEnd"
						).eq(
							"2022-12-20T16:56:05.761Z"
						))),
				DSL.field(
					"Session.deviceType"
				).eq(
					"deviceType1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.field(
					"Session.browserName"
				).eq(
					"browserName1"
				),
				DSL.field(
					"Session.sessionEnd"
				).gt(
					localDateTime.minusHours(24)
				),
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Individual.id", String.class)
					).from(
						DSL.table(
							"BQIndividual"
						).as(
							"Individual"
						)
					).crossJoin(
						DSL.table(
							"UNNEST(Individual.memberships) AS " +
								"IndividualMemberships")
					).join(
						DSL.table(
							"BQOrganization"
						).as(
							"Organization"
						)
					).on(
						DSL.condition(
							"Organization.id IN UNNEST(" +
								"IndividualMemberships.ids)")
					).where(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"organizationIds"
						),
						DSL.field(
							"Organization.name"
						).eq(
							"name1"
						)
					)
				)),
			_testFilters.get("testFreestyle10"),
			new HashSet<>(
				Arrays.asList("Individual", "Organization", "Session")),
			true);
	}

	@Test
	public void testFreestyle11() {
		_assertEquals(
			DSL.field(
				"accountPKs"
			).eq(
				"ae549a89-a2f4-4167-858c-e6f23f51beee"
			),
			_testFilters.get("testFreestyle11"));
	}

	@Test
	public void testFreestyle12() {
		Field userIdField = DSL.field("Event.userId");
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		Field identityIdField = DSL.field("Identity.id");
		Field individualIdField = DSL.field("Individual.id");

		_assertEquals(
			DSL.or(
				identityIdField.in(
					DSL.select(
						userIdField
					).from(
						DSL.table(
							"BQEvent"
						).as(
							"Event"
						)
					).where(
						DSL.field(
							"Event.applicationId"
						).eq(
							"Blog"
						).and(
							DSL.field(
								"Event.eventId"
							).eq(
								"blogViewed"
							)
						).and(
							DSL.field(
								"Event.id"
							).eq(
								"370994124094927024"
							)
						).and(
							DSL.field(
								"Event.eventDate"
							).gt(
								localDateTime.minus(1, ChronoUnit.DAYS)
							)
						)
					).groupBy(
						userIdField
					).having(
						DSL.count(
							userIdField
						).ge(
							1
						)
					)),
				individualIdField.in(
					DSL.selectDistinct(
						DSL.field("Identity.individualId")
					).from(
						DSL.table(
							"BQEvent"
						).as(
							"Event"
						).join(
							DSL.table(
								"BQIdentity"
							).as(
								"Identity"
							)
						).on(
							DSL.field(
								"Event.userId"
							).eq(
								DSL.field("Identity.id")
							)
						)
					).where(
						DSL.field(
							"Event.applicationId"
						).eq(
							"Blog"
						).and(
							DSL.field(
								"Event.eventId"
							).eq(
								"blogViewed"
							)
						).and(
							DSL.field(
								"Event.id"
							).eq(
								"370994124094927024"
							)
						).and(
							DSL.field(
								"Event.eventDate"
							).gt(
								localDateTime.minus(1, ChronoUnit.DAYS)
							)
						).and(
							DSL.field(
								"Identity.individualId"
							).isNotNull()
						)
					).groupBy(
						userIdField, DSL.field("Identity.individualId")
					).having(
						DSL.count(
							userIdField
						).ge(
							1
						)
					))),
			_testFilters.get("testFreestyle12"),
			new HashSet<>(Arrays.asList("Event")));
	}

	@Test
	public void testFreestyle13() {
		Field userIdField = DSL.field("Event.userId");
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		Field identityIdField = DSL.field("Identity.id");
		Field individualIdField = DSL.field("Individual.id");

		_assertEquals(
			DSL.and(
				DSL.or(
					identityIdField.in(
						DSL.select(
							userIdField
						).from(
							DSL.table(
								"BQEvent"
							).as(
								"Event"
							)
						).where(
							DSL.field(
								"Event.applicationId"
							).eq(
								"Form"
							).and(
								DSL.field(
									"Event.eventId"
								).eq(
									"formSubmitted"
								)
							).and(
								DSL.field(
									"Event.id"
								).eq(
									"519356017509996745"
								)
							).and(
								DSL.field(
									"Event.eventDate"
								).gt(
									localDateTime.minus(1, ChronoUnit.DAYS)
								)
							)
						).groupBy(
							userIdField
						).having(
							DSL.count(
								userIdField
							).ge(
								1
							)
						)),
					individualIdField.in(
						DSL.selectDistinct(
							DSL.field("Identity.individualId")
						).from(
							DSL.table(
								"BQEvent"
							).as(
								"Event"
							).join(
								DSL.table(
									"BQIdentity"
								).as(
									"Identity"
								)
							).on(
								DSL.field(
									"Event.userId"
								).eq(
									DSL.field("Identity.id")
								)
							)
						).where(
							DSL.field(
								"Event.applicationId"
							).eq(
								"Form"
							).and(
								DSL.field(
									"Event.eventId"
								).eq(
									"formSubmitted"
								)
							).and(
								DSL.field(
									"Event.id"
								).eq(
									"519356017509996745"
								)
							).and(
								DSL.field(
									"Event.eventDate"
								).gt(
									localDateTime.minus(1, ChronoUnit.DAYS)
								)
							).and(
								DSL.field(
									"Identity.individualId"
								).isNotNull()
							)
						).groupBy(
							userIdField, DSL.field("Identity.individualId")
						).having(
							DSL.count(
								userIdField
							).ge(
								1
							)
						))),
				DSL.or(
					identityIdField.in(
						DSL.select(
							userIdField
						).from(
							DSL.table(
								"BQEvent"
							).as(
								"Event"
							)
						).where(
							DSL.field(
								"Event.applicationId"
							).eq(
								"Form"
							).and(
								DSL.field(
									"Event.eventId"
								).eq(
									"formSubmitted"
								)
							).and(
								DSL.field(
									"Event.id"
								).eq(
									"499704442662154327"
								)
							).and(
								DSL.field(
									"Event.eventDate"
								).gt(
									localDateTime.minus(1, ChronoUnit.DAYS)
								)
							)
						).groupBy(
							userIdField
						).having(
							DSL.count(
								userIdField
							).ge(
								1
							)
						)),
					individualIdField.in(
						DSL.selectDistinct(
							DSL.field("Identity.individualId")
						).from(
							DSL.table(
								"BQEvent"
							).as(
								"Event"
							).join(
								DSL.table(
									"BQIdentity"
								).as(
									"Identity"
								)
							).on(
								DSL.field(
									"Event.userId"
								).eq(
									DSL.field("Identity.id")
								)
							)
						).where(
							DSL.field(
								"Event.applicationId"
							).eq(
								"Form"
							).and(
								DSL.field(
									"Event.eventId"
								).eq(
									"formSubmitted"
								)
							).and(
								DSL.field(
									"Event.id"
								).eq(
									"499704442662154327"
								)
							).and(
								DSL.field(
									"Event.eventDate"
								).gt(
									localDateTime.minus(1, ChronoUnit.DAYS)
								)
							).and(
								DSL.field(
									"Identity.individualId"
								).isNotNull()
							)
						).groupBy(
							userIdField, DSL.field("Identity.individualId")
						).having(
							DSL.count(
								userIdField
							).ge(
								1
							)
						)))),
			_testFilters.get("testFreestyle13"),
			new HashSet<>(Arrays.asList("Event")));
	}

	@Test
	public void testFreestyle14() {
		Field userIdField = DSL.field("Event.userId");
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		Field identityIdField = DSL.field("Identity.id");
		Field individualIdField = DSL.field("Individual.id");

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.addresses", String.class)
			).eq(
				"address"
			).and(
				DSL.or(
					DSL.lower(
						DSL.field("Individual.jobTitle", String.class)
					).eq(
						"jobtitle"
					),
					DSL.or(
						identityIdField.in(
							DSL.select(
								userIdField
							).from(
								DSL.table(
									"BQEvent"
								).as(
									"Event"
								)
							).where(
								DSL.field(
									"Event.applicationId"
								).eq(
									"Page"
								).and(
									DSL.field(
										"Event.eventId"
									).eq(
										"pageViewed"
									)
								).and(
									DSL.field(
										"Event.id"
									).eq(
										"370983685501627145"
									)
								).and(
									DSL.field(
										"Event.eventDate"
									).gt(
										localDateTime.minus(1, ChronoUnit.DAYS)
									)
								)
							).groupBy(
								userIdField
							).having(
								DSL.count(
									userIdField
								).ge(
									1
								)
							))),
					individualIdField.in(
						DSL.selectDistinct(
							DSL.field("Identity.individualId")
						).from(
							DSL.table(
								"BQEvent"
							).as(
								"Event"
							).join(
								DSL.table(
									"BQIdentity"
								).as(
									"Identity"
								)
							).on(
								DSL.field(
									"Event.userId"
								).eq(
									DSL.field("Identity.id")
								)
							)
						).where(
							DSL.field(
								"Event.applicationId"
							).eq(
								"Page"
							).and(
								DSL.field(
									"Event.eventId"
								).eq(
									"pageViewed"
								)
							).and(
								DSL.field(
									"Event.id"
								).eq(
									"370983685501627145"
								)
							).and(
								DSL.field(
									"Event.eventDate"
								).gt(
									localDateTime.minus(1, ChronoUnit.DAYS)
								)
							).and(
								DSL.field(
									"Identity.individualId"
								).isNotNull()
							)
						).groupBy(
							userIdField, DSL.field("Identity.individualId")
						).having(
							DSL.count(
								userIdField
							).ge(
								1
							)
						)))
			).and(
				DSL.lower(
					DSL.field("Individual.emailAddress", String.class)
				).eq(
					"email@test.com"
				)
			),
			_testFilters.get("testFreestyle14"),
			new HashSet<>(Arrays.asList("Event", "Individual")), true);
	}

	@Test
	public void testFreestyle15() {
		Field userIdField = DSL.field("Event.userId");
		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(TimeZoneDogUtil.getZoneId()), LocalTime.MIDNIGHT);

		Field identityIdField = DSL.field("Identity.id");
		Field individualIdField = DSL.field("Individual.id");

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.addresses", String.class)
			).eq(
				"address"
			).and(
				DSL.or(
					DSL.lower(
						DSL.field("Individual.jobTitle", String.class)
					).eq(
						"jobtitle"
					),
					DSL.not(
						DSL.or(
							identityIdField.in(
								DSL.select(
									userIdField
								).from(
									DSL.table(
										"BQEvent"
									).as(
										"Event"
									)
								).where(
									DSL.field(
										"Event.applicationId"
									).eq(
										"Page"
									).and(
										DSL.field(
											"Event.eventId"
										).eq(
											"pageViewed"
										)
									).and(
										DSL.field(
											"Event.id"
										).eq(
											"370983685501627145"
										)
									).and(
										DSL.field(
											"Event.eventDate"
										).gt(
											localDateTime.minus(
												1, ChronoUnit.DAYS)
										)
									)
								).groupBy(
									userIdField
								).having(
									DSL.count(
										userIdField
									).ge(
										1
									)
								)),
							individualIdField.in(
								DSL.selectDistinct(
									DSL.field("Identity.individualId")
								).from(
									DSL.table(
										"BQEvent"
									).as(
										"Event"
									).join(
										DSL.table(
											"BQIdentity"
										).as(
											"Identity"
										)
									).on(
										DSL.field(
											"Event.userId"
										).eq(
											DSL.field("Identity.id")
										)
									)
								).where(
									DSL.field(
										"Event.applicationId"
									).eq(
										"Page"
									).and(
										DSL.field(
											"Event.eventId"
										).eq(
											"pageViewed"
										)
									).and(
										DSL.field(
											"Event.id"
										).eq(
											"370983685501627145"
										)
									).and(
										DSL.field(
											"Event.eventDate"
										).gt(
											localDateTime.minus(
												1, ChronoUnit.DAYS)
										)
									).and(
										DSL.field(
											"Identity.individualId"
										).isNotNull()
									)
								).groupBy(
									userIdField,
									DSL.field("Identity.individualId")
								).having(
									DSL.count(
										userIdField
									).ge(
										1
									)
								)))))
			).and(
				DSL.lower(
					DSL.field("Individual.emailAddress", String.class)
				).eq(
					"email@test.com"
				)
			),
			_testFilters.get("testFreestyle15"),
			new HashSet<>(Arrays.asList("Event", "Individual")), true);
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
	public void testIdentifierDemographicsDate() {
		_assertEquals(
			DSL.field(
				"cast(Individual.birthday as date)"
			).eq(
				DSL.date("2023-01-01")
			),
			"(demographics/birthDate/value eq '2023-01-01')", true);

		_assertEquals(
			DSL.field(
				"cast(Individual.birthday as date)"
			).gt(
				DSL.date("2023-01-01")
			),
			"(demographics/birthDate/value gt '2023-01-01')", true);

		_assertEquals(
			DSL.field(
				"cast(Individual.birthday as date)"
			).lt(
				DSL.date("2023-01-01")
			),
			"(demographics/birthDate/value lt '2023-01-01')", true);
	}

	@Test
	public void testIdentifierDemographicsFieldMappings() {
		_assertEquals(
			DSL.lower(
				DSL.field("Individual.addresses", String.class)
			).eq(
				"test"
			),
			"(demographics/address/value eq 'Test')", true);

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.emailAddress", String.class)
			).ne(
				"test"
			),
			"(demographics/email/value ne 'Test')", true);

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.lastName", String.class)
			).ne(
				"test"
			),
			"(demographics/familyName/value ne 'Test')", true);

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.firstName", String.class)
			).ne(
				"test"
			),
			"(demographics/givenName/value ne 'Test')", true);
	}

	@Test
	public void testIdentifierDemographicsText() {
		_assertEquals(
			DSL.lower(
				DSL.field("Individual.firstName", String.class)
			).eq(
				"test"
			),
			"(demographics/givenName/value eq 'Test')", true);

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.firstName", String.class)
			).ne(
				"test"
			),
			"(demographics/givenName/value ne 'Test')", true);

		_assertEquals(
			DSL.lower(
				DSL.field("Individual.firstName", String.class)
			).like(
				DSL.inline("%liferay.com%")
			),
			"contains(demographics/givenName/value, 'liferay.com')", true);

		_assertEquals(
			DSL.not(
				DSL.condition(
					"LOWER(Individual.firstName) LIKE '%liferay.com%'")),
			"not contains(demographics/givenName/value, 'liferay.com')", true);

		_assertEquals(
			DSL.or(
				DSL.field(
					"Individual.firstName"
				).isNull(),
				DSL.field(
					"Individual.firstName"
				).eq(
					""
				)),
			"demographics/givenName/value eq null", true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"Individual.firstName"
				).isNotNull(),
				DSL.field(
					"Individual.firstName"
				).ne(
					""
				)),
			"demographics/givenName/value ne null", true);
	}

	@Test
	public void testIncompleteExpressionThrowsException() {
		_assertThrowsException(
			"column1 eq ", "Expression terminated unexpectedly: column1 eq ");
	}

	@Test
	public void testIndividualsCustomFieldFilterExpression() {
		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(",
						"IndividualFields_custom_field.value, '[') AND ",
						"ENDS_WITH(IndividualFields_custom_field.value, ']') ",
						"THEN ( EXISTS (SELECT value FROM UNNEST(",
						"JSON_EXTRACT_STRING_ARRAY(",
						"IndividualFields_custom_field.value,'$')) AS value ",
						"WHERE LOWER(value) = 'test')) ELSE LOWER(",
						"IndividualFields_custom_field.value) = 'test' END"))),
			"(custom/custom_field/value eq 'test')",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.or(
					DSL.field(
						"IndividualFields_custom_field.value"
					).isNull(),
					DSL.field(
						"IndividualFields_custom_field.value"
					).eq(
						""
					),
					DSL.field(
						"IndividualFields_custom_field.value"
					).eq(
						"[]"
					),
					DSL.field(
						"IndividualFields_custom_field.value"
					).eq(
						"[\"\"]"
					))),
			"(custom/custom_field/value eq null)",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					StringUtil.replace(
						_QUERY, new String[] {"{0}", "{1}", "{2}"},
						new String[] {
							"IndividualFields_custom_field", ">=", "50"
						}))),
			"(custom/custom_field/value ge 50)",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					StringUtil.replace(
						_QUERY, new String[] {"{0}", "{1}", "{2}"},
						new String[] {
							"IndividualFields_custom_field", ">", "50"
						}))),
			"(custom/custom_field/value gt 50)",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					StringUtil.replace(
						_QUERY, new String[] {"{0}", "{1}", "{2}"},
						new String[] {
							"IndividualFields_custom_field", "<=", "50.03"
						}))),
			"(custom/custom_field/value le 50.03)",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					StringUtil.replace(
						_QUERY, new String[] {"{0}", "{1}", "{2}"},
						new String[] {
							"IndividualFields_custom_field", "<", "500.2344"
						}))),
			"(custom/custom_field/value lt '500.2344')",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.and(
					DSL.field(
						"IndividualFields_custom_field.value"
					).isNotNull()),
				DSL.field(
					"IndividualFields_custom_field.value"
				).ne(
					""
				),
				DSL.field(
					"IndividualFields_custom_field.value"
				).ne(
					"[]"
				),
				DSL.field(
					"IndividualFields_custom_field.value"
				).ne(
					"[\"\"]"
				)),
			"(custom/custom_field/value ne null)",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);

		_assertEquals(
			DSL.and(
				DSL.field(
					"IndividualFields_custom_field.name"
				).eq(
					"custom_field"
				),
				DSL.condition(
					String.join(
						"", "CASE WHEN STARTS_WITH(",
						"IndividualFields_custom_field.value, '[') ",
						"AND ENDS_WITH(IndividualFields_custom_field.value, ",
						"']') THEN (EXISTS (SELECT value FROM UNNEST(",
						"JSON_EXTRACT_STRING_ARRAY(",
						"IndividualFields_custom_field.value,'$')) AS value ",
						"WHERE LOWER(value) LIKE '%test%')) ELSE LOWER(",
						"IndividualFields_custom_field.value) LIKE '%test%' ",
						"END"))),
			"contains(custom/custom_field/value, 'test')",
			new HashSet<>(
				Arrays.asList(
					"ExpandoValue", "Individual",
					"IndividualFields_custom_field")),
			true);
	}

	@Test
	public void testIndividualSegmentIdsFilterExpression() {
		FilterExpression filterExpression = new FilterExpression(
			"individualSegmentIds eq '1'", true);

		Assertions.assertEquals(
			DSL.field(
				"Membership.segmentId"
			).eq(
				1
			),
			filterExpression.getCondition());

		Set<String> referencedTableNames =
			filterExpression.getReferencedTableNames();

		Assertions.assertTrue(referencedTableNames.contains("Membership"));
	}

	@Test
	public void testIndividualsEqAndNull() {
		Condition expectedCondition = DSL.and(
			DSL.field(
				"IdentityActivity.channelId"
			).eq(
				506297979389450553L
			),
			DSL.field(
				"Individual.emailAddress"
			).isNotNull(),
			DSL.field(
				"Individual.emailAddress"
			).ne(
				""
			));

		FilterExpression filterExpression = new FilterExpression(
			"channelIds eq '506297979389450553' and (demographics/email" +
				"/value ne null)",
			true);

		Condition actualCondition = filterExpression.getCondition();

		Assertions.assertEquals(
			expectedCondition.toString(), actualCondition.toString());
	}

	@Test
	public void testIntegerValue() {
		_assertEquals(
			DSL.field(
				"column1", Long.class
			).eq(
				123L
			),
			"column1 eq 123");
	}

	@Test
	public void testInterestFilterExpressionFalse() {
		_assertEquals(
			DSL.not(
				DSL.or(
					DSL.field(
						"Identity.id", String.class
					).in(
						DSL.selectDistinct(
							DSL.field("Identity.id", String.class)
						).from(
							DSL.table(
								"BQIdentity"
							).as(
								"Identity"
							)
						).join(
							DSL.table(
								"BQIdentityInterestScore"
							).as(
								"Interest"
							)
						).on(
							DSL.field(
								"Identity.id", String.class
							).eq(
								DSL.field("Interest.identityId", String.class)
							)
						).where(
							DSL.and(
								DSL.field(
									"Interest.keyword", String.class
								).eq(
									"analytics"
								),
								DSL.field(
									"Interest.interested", Boolean.class
								).eq(
									true
								))
						)
					),
					DSL.field(
						"Individual.id", String.class
					).in(
						DSL.selectDistinct(
							DSL.field("Identity.individualId", String.class)
						).from(
							DSL.table(
								"BQIdentity"
							).as(
								"Identity"
							)
						).join(
							DSL.table(
								"BQIdentityInterestScore"
							).as(
								"Interest"
							)
						).on(
							DSL.field(
								"Identity.id", String.class
							).eq(
								DSL.field("Interest.identityId", String.class)
							)
						).where(
							DSL.and(
								DSL.field(
									"Interest.keyword", String.class
								).eq(
									"analytics"
								),
								DSL.field(
									"Interest.interested", Boolean.class
								).eq(
									true
								),
								DSL.field(
									"Identity.individualId"
								).isNotNull())
						)
					))),
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''false'')'))");
	}

	@Test
	public void testInterestFilterExpressionTrue1() {
		_assertEquals(
			DSL.or(
				DSL.field(
					"Identity.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Identity.id", String.class)
					).from(
						DSL.table(
							"BQIdentity"
						).as(
							"Identity"
						)
					).join(
						DSL.table(
							"BQIdentityInterestScore"
						).as(
							"Interest"
						)
					).on(
						DSL.field(
							"Identity.id", String.class
						).eq(
							DSL.field("Interest.identityId", String.class)
						)
					).where(
						DSL.and(
							DSL.field(
								"Interest.keyword", String.class
							).eq(
								"analytics"
							),
							DSL.field(
								"Interest.interested", Boolean.class
							).eq(
								true
							))
					)
				),
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Identity.individualId", String.class)
					).from(
						DSL.table(
							"BQIdentity"
						).as(
							"Identity"
						)
					).join(
						DSL.table(
							"BQIdentityInterestScore"
						).as(
							"Interest"
						)
					).on(
						DSL.field(
							"Identity.id", String.class
						).eq(
							DSL.field("Interest.identityId", String.class)
						)
					).where(
						DSL.and(
							DSL.field(
								"Interest.keyword", String.class
							).eq(
								"analytics"
							),
							DSL.field(
								"Interest.interested", Boolean.class
							).eq(
								true
							),
							DSL.field(
								"Identity.individualId"
							).isNotNull())
					)
				)),
			"(interests.filter(filter='(name eq ''analytics'' and score eq " +
				"''true'')'))");
	}

	@Test
	public void testInterestFilterExpressionTrue2() {
		_assertEquals(
			DSL.or(
				DSL.field(
					"Identity.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Identity.id", String.class)
					).from(
						DSL.table(
							"BQIdentity"
						).as(
							"Identity"
						)
					).join(
						DSL.table(
							"BQIdentityInterestScore"
						).as(
							"Interest"
						)
					).on(
						DSL.field(
							"Identity.id", String.class
						).eq(
							DSL.field("Interest.identityId", String.class)
						)
					).where(
						DSL.and(
							DSL.field(
								"Interest.keyword", String.class
							).eq(
								"analytics test"
							),
							DSL.field(
								"Interest.interested", Boolean.class
							).eq(
								true
							))
					)
				),
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Identity.individualId", String.class)
					).from(
						DSL.table(
							"BQIdentity"
						).as(
							"Identity"
						)
					).join(
						DSL.table(
							"BQIdentityInterestScore"
						).as(
							"Interest"
						)
					).on(
						DSL.field(
							"Identity.id", String.class
						).eq(
							DSL.field("Interest.identityId", String.class)
						)
					).where(
						DSL.and(
							DSL.field(
								"Interest.keyword", String.class
							).eq(
								"analytics test"
							),
							DSL.field(
								"Interest.interested", Boolean.class
							).eq(
								true
							),
							DSL.field(
								"Identity.individualId"
							).isNotNull())
					)
				)),
			"(interests.filter(filter='(name eq ''analytics test'' and score " +
				"eq ''true'')'))");
	}

	@Test
	public void testInvalidLogicalOperatorThrowsException() {
		_assertThrowsException(
			"column1 eq 'value1' but column2 eq 'value2'",
			"Expected logical operator \"and\" or \"or\", but got \"but\" " +
				"instead");
	}

	@Test
	public void testInvalidOperatorThrowsException() {
		_assertThrowsException("column1 is 'value1'", "Invalid operator: is");
	}

	@Test
	public void testInvalidStringFunctionArgumentThrowsException() {
		_assertThrowsException(
			"matches(column1, 'value1')", "Invalid string function: matches");
	}

	@Test
	public void testIsMemberFilterExpressionEq() {
		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).where(
					DSL.and(
						DSL.field(
							"IndividualMemberships.name"
						).eq(
							"userGroupIds"
						),
						DSL.condition(
							"'1234' IN UNNEST(IndividualMemberships.ids)"))
				)
			),
			"userGroupIds eq '1234'");
	}

	@Test
	public void testIsMemberFilterExpressionNe() {
		_assertEquals(
			DSL.not(
				DSL.field(
					"Individual.id", String.class
				).in(
					DSL.selectDistinct(
						DSL.field("Individual.id", String.class)
					).from(
						DSL.table(
							"BQIndividual"
						).as(
							"Individual"
						)
					).crossJoin(
						DSL.table(
							"UNNEST(Individual.memberships) AS " +
								"IndividualMemberships")
					).where(
						DSL.and(
							DSL.field(
								"IndividualMemberships.name"
							).eq(
								"userGroupIds"
							),
							DSL.condition(
								"'1234' IN UNNEST(IndividualMemberships.ids)"))
					)
				)),
			"userGroupIds ne '1234'");
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
	public void testOrganizationFilterExpression() {
		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.condition("'1234' IN UNNEST(IndividualMemberships.ids)")
				)
			),
			"organizations.filter(filter='(id eq ''1234'')')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"ParentOrganization"
					)
				).on(
					DSL.and(
						DSL.field(
							"Organization.parentOrganizationId"
						).eq(
							DSL.field("ParentOrganization.organizationId")
						),
						DSL.field(
							"Organization.dataSourceId"
						).eq(
							DSL.field("ParentOrganization.dataSourceId")
						))
				).where(
					DSL.field(
						"ParentOrganization.id"
					).eq(
						"1234"
					)
				)
			),
			"organizations.filter(filter='(parentId eq ''1234'')')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"Organization.type"
					).eq(
						"org"
					)
				)
			),
			"organizations.filter(filter='(type eq ''org'')')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).notIn(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"Organization.type"
					).isNotNull()
				)
			),
			"organizations.filter(filter='(type eq null)')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"Organization.type"
					).ne(
						"org"
					)
				)
			),
			"organizations.filter(filter='(type ne ''org'')')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"Organization.type"
					).isNotNull()
				)
			),
			"organizations.filter(filter='(type ne null)')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.lower(
						DSL.field("Organization.treePath", String.class)
					).like(
						DSL.inline("%test%")
					)
				)
			),
			"organizations.filter(filter='(contains(hierarchyPath, ''test''))" +
				"')");

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).join(
					DSL.table(
						"BQExpandoValue"
					).as(
						"ExpandoValue_custom_field"
					)
				).on(
					DSL.and(
						DSL.field(
							"ExpandoValue_custom_field.classPK"
						).eq(
							DSL.field(
								"SAFE_CAST(Organization.organizationId AS " +
									"STRING)")
						),
						DSL.field(
							"ExpandoValue_custom_field.classType"
						).eq(
							"com.liferay.portal.kernel.model.Organization"
						),
						DSL.field(
							"ExpandoValue_custom_field.dataSourceId"
						).eq(
							DSL.field("Organization.dataSourceId")
						))
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"ExpandoValue_custom_field.fieldName"
					).eq(
						"custom_field"
					),
					DSL.condition(
						String.join(
							"", "CASE WHEN STARTS_WITH(",
							"ExpandoValue_custom_field.value, '[') AND ",
							"ENDS_WITH(ExpandoValue_custom_field.value, ",
							"']') THEN ( EXISTS (SELECT value FROM UNNEST(",
							"JSON_EXTRACT_STRING_ARRAY(",
							"ExpandoValue_custom_field.value,'$')) AS ",
							"value WHERE LOWER(value) = 'test')) ELSE ",
							"LOWER(ExpandoValue_custom_field.value) = ",
							"'test' END"))
				)
			),
			"organizations.filter(filter='(custom/custom_field/value eq " +
				"''test'')')",
			new HashSet<>(Arrays.asList("Individual", "Organization")));

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).join(
					DSL.table(
						"BQExpandoValue"
					).as(
						"ExpandoValue_custom_field"
					)
				).on(
					DSL.and(
						DSL.field(
							"ExpandoValue_custom_field.classPK"
						).eq(
							DSL.field(
								"SAFE_CAST(Organization.organizationId AS " +
									"STRING)")
						),
						DSL.field(
							"ExpandoValue_custom_field.classType"
						).eq(
							"com.liferay.portal.kernel.model.Organization"
						),
						DSL.field(
							"ExpandoValue_custom_field.dataSourceId"
						).eq(
							DSL.field("Organization.dataSourceId")
						))
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"ExpandoValue_custom_field.fieldName"
					).eq(
						"custom_field"
					),
					DSL.condition(
						StringUtil.replace(
							_QUERY, new String[] {"{0}", "{1}", "{2}"},
							new String[] {
								"ExpandoValue_custom_field", ">=", "123"
							}))
				)
			),
			"organizations.filter(filter='(custom/custom_field/value ge 123)')",
			new HashSet<>(Arrays.asList("Individual", "Organization")));

		_assertEquals(
			DSL.field(
				"Individual.id", String.class
			).in(
				DSL.selectDistinct(
					DSL.field("Individual.id", String.class)
				).from(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).crossJoin(
					DSL.table(
						"UNNEST(Individual.memberships) AS " +
							"IndividualMemberships")
				).join(
					DSL.table(
						"BQOrganization"
					).as(
						"Organization"
					)
				).on(
					DSL.condition(
						"Organization.id IN UNNEST(IndividualMemberships.ids)")
				).join(
					DSL.table(
						"BQExpandoValue"
					).as(
						"ExpandoValue_custom_field"
					)
				).on(
					DSL.and(
						DSL.field(
							"ExpandoValue_custom_field.classPK"
						).eq(
							DSL.field(
								"SAFE_CAST(Organization.organizationId AS " +
									"STRING)")
						),
						DSL.field(
							"ExpandoValue_custom_field.classType"
						).eq(
							"com.liferay.portal.kernel.model.Organization"
						),
						DSL.field(
							"ExpandoValue_custom_field.dataSourceId"
						).eq(
							DSL.field("Organization.dataSourceId")
						))
				).where(
					DSL.field(
						"IndividualMemberships.name"
					).eq(
						"organizationIds"
					),
					DSL.field(
						"ExpandoValue_custom_field.fieldName"
					).eq(
						"custom_field"
					),
					DSL.condition(
						String.join(
							"", "CASE WHEN STARTS_WITH(",
							"ExpandoValue_custom_field.value, '[') AND ",
							"ENDS_WITH(ExpandoValue_custom_field.value, ",
							"']') THEN (EXISTS (SELECT value FROM UNNEST(",
							"JSON_EXTRACT_STRING_ARRAY(",
							"ExpandoValue_custom_field.value,'$')) AS ",
							"value WHERE LOWER(value) LIKE '%test%')) ",
							"ELSE LOWER(ExpandoValue_custom_field.value) ",
							"LIKE '%test%' END"))
				)
			),
			"organizations.filter(filter='(contains(custom/custom_field" +
				"/value, ''test''))')",
			new HashSet<>(Arrays.asList("Individual", "Organization")));
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

	@Disabled
	@Test
	public void testTooManyStringFunctionArgumentsThrowsException() {
		_assertThrowsException(
			"contains(column1, 'value1', 'value2')",
			"Expected 2 arguments for contains function, got 3 instead: " +
				"[column1, 'value1', 'value2']");
	}

	@Test
	public void testUnclosedParenthesisThrowsException() {
		_assertThrowsException(
			"(column1 eq ')'",
			"Unclosed parenthetical statement: (column1 eq ')'");
	}

	@Test
	public void testUnclosedStringLiteralThrowsException() {
		_assertThrowsException(
			"column1 eq 'escaped quote: ''",
			"Unclosed string literal: 'escaped quote: ''");
	}

	private void _assertEquals(
		Condition expectedCondition, String actualFilterExpressionString) {

		_assertEquals(expectedCondition, actualFilterExpressionString, false);
	}

	private void _assertEquals(
		Condition expectedCondition, String actualFilterExpressionString,
		boolean segment) {

		FilterExpression filterExpression = new FilterExpression(
			actualFilterExpressionString, segment);

		Assertions.assertEquals(
			expectedCondition, filterExpression.getCondition());
	}

	private void _assertEquals(
		Condition expectedCondition, String actualFilterExpressionString,
		Set<String> includedTableNames) {

		_assertEquals(
			expectedCondition, actualFilterExpressionString, includedTableNames,
			false);
	}

	private void _assertEquals(
		Condition expectedCondition, String actualFilterExpressionString,
		Set<String> includedTableNames, boolean segment) {

		FilterExpression filterExpression = new FilterExpression(
			actualFilterExpressionString, segment);

		Assertions.assertEquals(
			expectedCondition, filterExpression.getCondition());

		Assertions.assertEquals(
			includedTableNames, filterExpression.getReferencedTableNames());
	}

	private void _assertThrowsException(
		String filterExpressionString, String message) {

		Assertions.assertThrows(
			FilterExpressionParserException.class,
			() -> new FilterExpression(filterExpressionString), message);
	}

	@SuppressFBWarnings
	private Map<String, String> _getTestFilters() {
		ClassPathResource classPathResource = new ClassPathResource(
			"filters.txt", getClass());

		try {
			File file = classPathResource.getFile();

			List<String> filters = Files.readAllLines(
				Paths.get(file.getAbsolutePath()));

			Stream<String> stream = filters.stream();

			return stream.map(
				filter -> filter.split("=", 2)
			).collect(
				Collectors.toMap(filter -> filter[0], filter -> filter[1])
			);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static final String _QUERY = String.join(
		"", "CASE WHEN STARTS_WITH({0}.value, '[') AND ENDS_WITH(",
		"{0}.value, ']') THEN (EXISTS (SELECT numeric_value FROM UNNEST(",
		"JSON_EXTRACT_ARRAY({0}.value,'$')) AS numeric_value WHERE ",
		"SAFE_CAST(numeric_value AS NUMERIC) {1} SAFE_CAST('{2}' AS ",
		"NUMERIC))) WHEN SAFE_CAST({0}.value AS NUMERIC) IS NULL THEN ",
		"false ELSE SAFE_CAST({0}.value AS NUMERIC) {1} SAFE_CAST('{2}' ",
		"AS NUMERIC) END");

	private final Map<String, String> _testFilters = _getTestFilters();

}