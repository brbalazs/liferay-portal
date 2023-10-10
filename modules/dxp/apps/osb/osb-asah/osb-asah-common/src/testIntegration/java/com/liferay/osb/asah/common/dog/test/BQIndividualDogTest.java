/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQIndividualDog;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.faro.info.util.FaroInfoIndividualUtil;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.yaml.snakeyaml.util.ArrayUtils;

/**
 * @author Rachael Koestartyo
 */
public class BQIndividualDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_count_bq_individuals.sql")
	@Test
	public void testCountBQIndividuals() {
		Assertions.assertEquals(
			7L,
			_bqIndividualDog.countBQIndividuals(
				null, 1L, null,
				"(sessions.filter(filter='(completeDate gt ''last90Days'')'))",
				true, null, null, null, null));
		Assertions.assertEquals(
			3L,
			_bqIndividualDog.countBQIndividuals(
				null, 1L, null,
				"(sessions.filter(filter='(completeDate gt ''last90Days'')'))",
				false, null, null, null, null));
		Assertions.assertEquals(
			1L,
			_bqIndividualDog.countBQIndividuals(
				null, 1L, null,
				"(sessions.filter(filter='(completeDate gt ''last90Days'')') " +
					"and demographics/familyName/value eq 'Test')",
				true, null, null, null, null));
		Assertions.assertEquals(
			1L,
			_bqIndividualDog.countBQIndividuals(
				null, 1L, null,
				"(sessions.filter(filter='(completeDate gt ''last90Days'')') " +
					"and demographics/familyName/value eq 'Test')",
				false, null, null, null, null));
	}

	@BQSQLResource(resourcePath = "test_bq_identity_activities_1.sql")
	@Test
	public void testFetchBQIndividual1() {
		Individual individual = _bqIndividualDog.fetchBQIndividual(
			1L,
			"05574696b257a38dc21009122d33550c299f822dc768984c95693e6d5c4ed006");

		Assertions.assertEquals(25L, individual.getActivitiesCount());
		Assertions.assertEquals(
			"2019-02-12T20:36:53.218Z",
			DateUtil.toString(individual.getLastActivityDate()));

		Set<Field> fields = individual.getFields();

		Assertions.assertEquals(4, fields.size());

		Set<Field> customFields = individual.getCustomFields();

		Assertions.assertEquals(1, customFields.size());

		Stream<Field> stream = customFields.stream();

		Field customField = stream.filter(
			field -> Objects.equals(field.getName(), "custom")
		).findFirst(
		).orElse(
			null
		);

		Assertions.assertNotNull(customField);

		Assertions.assertEquals(
			"joe.bloggs@liferay.com",
			FaroInfoIndividualUtil.getIndividualEmail(individual));
	}

	@BQSQLResource(resourcePath = "test_bq_identity_activities_2.sql")
	@Test
	public void testFetchBQIndividual2() {
		Assertions.assertNull(
			_bqIndividualDog.fetchBQIndividual(
				1L,
				"05574696b257a38dc21009122d33550c299f822dc768984c95693e6d5c4e" +
					"d006"));
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_individual_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQIndividualFieldValuePageCustom() {
		Page<String> fieldValuePage =
			_bqIndividualDog.getBQIndividualFieldValuePage(
				null, null, "custom/Custom_Greeting/value", 0, 5);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(5, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Aloha!");
				add("Good Day!");
				add("Greetings!");
				add("Hello!");
				add("Oh Hi There!");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(7, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_individual_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQIndividualFieldValuePageCustomCheckbox() {
		Page<String> fieldValuePage =
			_bqIndividualDog.getBQIndividualFieldValuePage(
				null, null, "custom/Preferred_Snacks/value", 0, 10);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(7, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Bread");
				add("Candy");
				add("Cheese String");
				add("Chips");
				add("Chocolate");
				add("Fruits");
				add("Peanuts");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(7, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_individual_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQIndividualFieldValuePageCustomSelectionList() {
		Page<String> fieldValuePage =
			_bqIndividualDog.getBQIndividualFieldValuePage(
				null, null, "custom/Department/value", 0, 5);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(4, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Customer Support");
				add("Marketing");
				add("Product");
				add("Sales");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(4, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_individual_field_value_page_demographics.sql"
	)
	@Test
	public void testGetBQIndividualFieldValuePageDemographics() {
		Page<String> fieldValuePage =
			_bqIndividualDog.getBQIndividualFieldValuePage(
				null, null, "demographics/givenName/value", 0, 7);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(7, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("test1");
				add("test10");
				add("test2");
				add("test3");
				add("test4");
				add("test5");
				add("test6");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(10, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(resourcePath = "test_search_bq_individual_page_1.sql")
	@Test
	public void testSearchBQIndividualPage1() {
		Page<Individual> bqIndividualPage =
			_bqIndividualDog.searchBQIndividualPage(null, 0, null, 10);

		Assertions.assertEquals(15, bqIndividualPage.getTotalElements());
		Assertions.assertEquals(2, bqIndividualPage.getTotalPages());

		Assertions.assertEquals(
			Arrays.asList(
				"1", "11", "12", "13", "14", "15", "16", "17", "18", "19"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));

		bqIndividualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1, null, 10);

		Assertions.assertEquals(
			Arrays.asList("20", "3", "5", "7", "9"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));
	}

	@BQSQLResource(resourcePath = "test_search_bq_individual_page_2.sql")
	@Test
	public void testSearchBQIndividualPage2() {
		Page<Individual> bqIndividualPage =
			_bqIndividualDog.searchBQIndividualPage(1L, 0, null, 2);

		Assertions.assertEquals(5, bqIndividualPage.getTotalElements());
		Assertions.assertEquals(3, bqIndividualPage.getTotalPages());

		Assertions.assertEquals(
			Arrays.asList("1", "3"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));

		bqIndividualPage = _bqIndividualDog.searchBQIndividualPage(
			1L, 1, null, 2);

		Assertions.assertEquals(
			Arrays.asList("5", "7"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));

		bqIndividualPage = _bqIndividualDog.searchBQIndividualPage(
			1L, 2, null, 2);

		Assertions.assertEquals(
			Arrays.asList("9"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));
	}

	@BQSQLResource(resourcePath = "test_search_bq_individual_page_3.sql")
	@Test
	public void testSearchBQIndividualPage3() {
		Page<Individual> bqIndividualPage =
			_bqIndividualDog.searchBQIndividualPage(1L, 0, "9", 20);

		Assertions.assertEquals(1, bqIndividualPage.getTotalElements());
		Assertions.assertEquals(1, bqIndividualPage.getTotalPages());

		Assertions.assertEquals(
			Arrays.asList("9"),
			ListUtil.map(bqIndividualPage.getContent(), Individual::getId));
	}

	@BQSQLResource(resourcePath = "test_get_bq_individual_page.sql")
	@Test
	public void testSearchBQIndividuals1() {
		Page<Individual> individualPage =
			_bqIndividualDog.searchBQIndividualPage(
				null, 1L, null, null, false, null, null, 0, null, null, 10,
				new String[] {"demographics/givenName/value,asc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"Adam", "alex", "Bonnie", "cedric", "Christina", "Daniel",
					"Eve", "fiona", "olivia", "Zinchenko"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, null, false, null, null, 0, null, null, 10,
			new String[] {"demographics/givenName/value,desc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"Zinchenko", "olivia", "fiona", "Eve", "Daniel",
					"Christina", "cedric", "Bonnie", "alex", "Adam"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, null, false, null, null, 0, null, null, 10,
			new String[] {"createDate,asc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"fiona", "Daniel", "olivia", "Zinchenko", "Christina",
					"cedric", "Bonnie", "alex", "Eve", "Adam"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, null, false, null, null, 0, null, null, 10,
			new String[] {"createDate,desc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"Adam", "Eve", "alex", "Bonnie", "cedric", "Christina",
					"Zinchenko", "olivia", "Daniel", "fiona"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, "(demographics/birthday/value lt '2023-04-29')",
			false, null, null, 0, null, null, 10,
			new String[] {"createDate,asc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"fiona", "Daniel", "olivia", "Zinchenko", "Christina",
					"cedric", "Bonnie", "alex", "Eve", "Adam"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, null, false, null, null, 0, null, null, 10,
			new String[] {"activitiesCount,asc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"Adam", "Eve", "alex", "fiona", "Bonnie", "cedric",
					"Zinchenko", "Daniel", "Christina", "olivia"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, null, false, null, null, 0, null, null, 10,
			new String[] {"activitiesCount,desc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"olivia", "Christina", "Daniel", "Zinchenko", "cedric",
					"Bonnie", "alex", "fiona", "Eve", "Adam"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, "(demographics/birthday/value lt '2023-04-29')",
			false, null, null, 0, null, null, 10,
			new String[] {"demographics/jobTitle/value,asc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"Zinchenko", "Daniel", "Bonnie", "cedric", "Adam", "olivia",
					"Eve", "alex", "Christina", "fiona"
				}),
			_getGivenNames(individualPage.getContent()));

		individualPage = _bqIndividualDog.searchBQIndividualPage(
			null, 1L, null, "(demographics/birthday/value lt '2023-04-29')",
			false, null, null, 0, null, null, 10,
			new String[] {"demographics/jobTitle/value,desc"});

		Assertions.assertEquals(
			ArrayUtils.toUnmodifiableList(
				new String[] {
					"fiona", "Christina", "alex", "Eve", "olivia", "Adam",
					"Bonnie", "cedric", "Daniel", "Zinchenko"
				}),
			_getGivenNames(individualPage.getContent()));
	}

	private List<String> _getGivenNames(List<Individual> individuals) {
		List<String> givenNames = new LinkedList<>();

		Stream<Individual> stream1 = individuals.stream();

		stream1.forEachOrdered(
			individual -> {
				Set<Field> fields = individual.getFields();

				Stream<Field> stream2 = fields.stream();

				Optional<Field> fieldOptional = stream2.filter(
					field -> Objects.equals(field.getName(), "givenName")
				).findFirst();

				fieldOptional.ifPresent(
					field -> givenNames.add(String.valueOf(field.getValue())));
			});

		return givenNames;
	}

	@Autowired
	private BQIndividualDog _bqIndividualDog;

}