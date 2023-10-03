/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQIndividualDog;
import com.liferay.osb.asah.common.dog.BQMembershipDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.faro.info.util.FaroInfoIndividualUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.DXPEntityType;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.BQOrganizationRepository;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.common.repository.DXPEntityRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

	@BeforeEach
	public void setUp() {
		_liferayDataSource = FaroInfoTestUtil.buildLiferayDataSource();

		_liferayDataSource.setId(
			Long.parseLong(RandomStringUtils.randomNumeric(4)));

		// TODO Save BQFieldMapping with _FIELD_NAMES

	}

	@Disabled
	@Test
	public void testAddAndUpdateLiferayIndividual() throws Exception {
		String userId = RandomTestUtil.randomId();

		Individual individual = _addIndividual(
			"3",
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"email", "john.doe@liferay.com"
				).put(
					"firstName", "John"
				).put(
					"lastName", "Doe"
				).put(
					"middleName", ""
				).put(
					"modifiedDate", System.currentTimeMillis()
				).put(
					"userId", userId
				)
			).put(
				"email", "john.doe@liferay.com"
			).put(
				"id", RandomTestUtil.randomId()
			).put(
				"middleName", "James"
			).put(
				"modifiedDate", System.currentTimeMillis()
			).put(
				"osbAsahDataSourceId",
				String.valueOf(_liferayDataSource.getId())
			).put(
				"userId", userId
			).put(
				"uuid", RandomTestUtil.randomUUID()
			),
			_liferayDataSource);

		_assertIndividualMiddleName("James", individual);

		individual = _updateIndividual(
			"3",
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"email", "john.doe@liferay.com"
				).put(
					"middleName", "Joseph"
				)
			).put(
				"modifiedDate", System.currentTimeMillis()
			),
			_liferayDataSource, individual);

		_assertIndividualMiddleName("Joseph", individual);
	}

	@Disabled
	@Test
	public void testAddAndUpdateLiferayIndividualCustomFields()
		throws Exception {

		// TODO Add BQFieldMapping "address", "Text"
		// TODO Add BQFieldMapping "spokenLanguages", "Text"
		// TODO Add BQFieldMapping "favoriteNumber", "Number"

		JSONObject userJSONObject = JSONUtil.put(
			"contact",
			JSONUtil.put(
				"email", "john.doe@liferay.com"
			).put(
				"userId", 12345
			)
		).put(
			"email", "john.doe@liferay.com"
		).put(
			"expando",
			JSONUtil.put(
				"address", "1400 Montefino Ave"
			).put(
				"favoriteNumber", 42
			).put(
				"spokenLanguages", JSONUtil.put("english")
			)
		).put(
			"modifiedDate", System.currentTimeMillis()
		).put(
			"osbAsahDataSourceId", String.valueOf(_liferayDataSource.getId())
		).put(
			"userId", 12345
		);

		Individual individual = _addIndividual(
			"1", userJSONObject, _liferayDataSource);

		Set<Field> customFields = individual.getCustomFields();

		_assertCustomFields(customFields, "1400 Montefino Ave", "address");
		_assertCustomFields(customFields, 42, "favoriteNumber");
		_assertCustomFields(customFields, "[english]", "spokenLanguages");

		userJSONObject.remove("expando");

		individual = _updateIndividual(
			"1", userJSONObject, _liferayDataSource, individual);

		_assertCustomFields(customFields, "1400 Montefino Ave", "address");
		_assertCustomFields(customFields, 42, "favoriteNumber");
		_assertCustomFields(customFields, "[english]", "spokenLanguages");

		individual = _updateIndividual(
			"1",
			userJSONObject.put(
				"expando",
				JSONUtil.put(
					"favoriteNumber", 8
				).put(
					"spokenLanguages", JSONUtil.put("german")
				)),
			_liferayDataSource, individual);

		customFields = individual.getCustomFields();

		_assertCustomFields(customFields, null, "address");
		_assertCustomFields(customFields, 8, "favoriteNumber");
		_assertCustomFields(customFields, "[german]", "spokenLanguages");
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_associations.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@RepositoryResource(
		repositoryClass = BQOrganizationRepository.class,
		resourcePath = "osbasahfaroinfo/organizations.json"
	)
	@Test
	public void testAddIndividualAssociation() {
		Individual individual = _addIndividualAssociation(
			33134, 405201047787757795L,
			DXPEntity.Type.of(DXPEntityType.CLASS_NAME_ORGANIZATION), null);

		Set<Long> organizationIds = individual.getOrganizationIds();

		MatcherAssert.assertThat(
			new Long[] {402139267512234420L, 402139268847589064L},
			Matchers.arrayContainingInAnyOrder(
				organizationIds.toArray(new Long[0])));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_2.json"
	)
	@Test
	public void testCountActiveIndividualsFromLast30DaysBySegment() {
		Segment segment = new Segment();

		segment.setChannelId(100L);
		segment.setId(123L);

		Assertions.assertEquals(
			3, _countActiveIndividualsFromLast30DaysBySegment(segment));
	}

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

	@Disabled
	@RepositoryResource(
		repositoryClass = BQOrganizationRepository.class,
		resourcePath = "osbasahfaroinfo/organizations.json"
	)
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_associations.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@RepositoryResource(
		repositoryClass = BQOrganizationRepository.class,
		resourcePath = "osbasahfaroinfo/organizations.json"
	)
	@Test
	public void testDeleteIndividualAssociation() {
		Individual individual = _deleteIndividualAssociation(
			33134, 402139209179557944L,
			DXPEntity.Type.of(DXPEntityType.CLASS_NAME_ORGANIZATION), null);

		Set<Long> organizationIds = individual.getOrganizationIds();

		MatcherAssert.assertThat(
			new Long[] {402139267512234420L},
			Matchers.arrayContainingInAnyOrder(
				organizationIds.toArray(new Long[0])));
	}

	@BQSQLResource(resourcePath = "test_bq_identity_activities.sql")
	@Test
	public void testFetchBQIndividual() {
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

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_info.json"
	)
	@Test
	public void testGetIndividualPage1() {

		// TODO Add BQFieldMapping "client_id", "Text"
		// TODO Add BQFieldMapping "favoritePokemon", "Text"

		Page<Individual> individualPage = _getIndividualPage("", null, 0, 10);

		Assertions.assertEquals(5, individualPage.getTotalElements());

		Assertions.assertEquals(
			SetUtil.of("123", "124", "125", "126", "127"),
			Collections.emptySet());
		Assertions.assertEquals(
			SetUtil.of("Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Mew"),
			Collections.emptySet());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_info.json"
	)
	@Test
	public void testGetIndividualPage2() {

		// TODO Add BQFieldMapping "favoritePokemon", "Text"

		Page<Individual> individualPage = _getIndividualPage(
			"mander", null, 0, 10);

		Assertions.assertEquals(1, individualPage.getTotalElements());

		Assertions.assertEquals(
			SetUtil.of("Charmander"), Collections.emptySet());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_4.json"
	)
	@Test
	public void testGetIndividualPage3() {
		Page<Individual> individualPage = _getIndividualPage(
			DateUtil.toUTCDate("2019-02-10T10:00:00.000Z"), -1L, 2,
			Sort.by(Sort.Order.asc("id")),
			DateUtil.toUTCDate("2019-02-12T14:00:00.000Z"));

		Assertions.assertEquals(3, individualPage.getTotalElements());

		List<Individual> individuals = individualPage.getContent();

		Assertions.assertEquals(2, individuals.size());

		Individual individual = individuals.get(0);

		Assertions.assertEquals(4L, Long.valueOf(individual.getId()));

		individual = individuals.get(1);

		Assertions.assertEquals(6L, Long.valueOf(individual.getId()));
	}

	@Disabled
	@Test
	public void testNoDuplicateIndividualPKs() throws Exception {
		String userId = RandomTestUtil.randomId();
		String uuid1 = RandomTestUtil.randomUUID();

		JSONObject dataJSONObject = JSONUtil.put(
			"contact",
			JSONUtil.put(
				"email", "dummy.test@test.com"
			).put(
				"userId", userId
			)
		).put(
			"email", "dummy.test@test.com"
		).put(
			"id", RandomTestUtil.randomId()
		).put(
			"modifiedDate", System.currentTimeMillis()
		).put(
			"osbAsahDataSourceId", String.valueOf(_liferayDataSource.getId())
		).put(
			"userId", userId
		).put(
			"uuid", uuid1
		);

		Individual individual = _addIndividual(
			uuid1, dataJSONObject, _liferayDataSource);

		_assertDataSourceUserPKs(new String[] {uuid1}, individual);

		String uuid2 = RandomTestUtil.randomUUID();

		individual = _updateIndividual(
			uuid2, dataJSONObject.put("uuid", uuid2), _liferayDataSource,
			individual);

		_assertDataSourceUserPKs(new Object[] {uuid1, uuid2}, individual);

		individual = _updateIndividual(
			uuid2, dataJSONObject.put("test", "test"), _liferayDataSource,
			individual);

		_assertDataSourceUserPKs(new Object[] {uuid1, uuid2}, individual);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@Test
	public void testRemoveIndividualSegmentId() {
		Individual individual = _fetchIndividual("338486041327913341");

		Set<Long> segmentIds = individual.getSegmentIds();

		Assertions.assertEquals(1, segmentIds.size(), segmentIds.toString());

		// TODO Remove Individual from segment

		individual = _fetchIndividual("338486041327913341");

		segmentIds = individual.getSegmentIds();

		Assertions.assertEquals(0, segmentIds.size(), segmentIds.toString());
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

	@Disabled
	@RepositoryResource(
		repositoryClass = BQEventRepository.class,
		resourcePath = "osbasahfaroinfo/events.json"
	)
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@Test
	public void testSearchBQIndividuals2() {
		StringBuilder sb = new StringBuilder();

		sb.append("(channelIds eq '100' and (activities.filterByCount(");
		sb.append("filter='(activityKey eq ");
		sb.append("''Form#formViewed#511687236573013375'' and day gt ");
		sb.append("''last24Hours'')',operator='%s',value=%s)))");

		List<Individual> individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "eq", 2), true, 0, 10, null);

		Assertions.assertEquals(1, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "ge", 2), true, 0, 10, null);

		Assertions.assertEquals(1, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "ge", 3), true, 0, 10, null);

		Assertions.assertEquals(0, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "gt", 0), true, 0, 10, null);

		Assertions.assertEquals(1, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "le", 3), true, 0, 10, null);

		Assertions.assertEquals(5, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "lt", 2), true, 0, 10, null);

		Assertions.assertEquals(4, individuals.size(), individuals.toString());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQSessionRepository.class,
		resourcePath = "osbasahcerebroinfo/user_sessions.json"
	)
	@Test
	public void testSearchBQIndividuals3() {
		StringBuilder sb = new StringBuilder();

		sb.append("(channelIds eq '100' and (sessions.filter(filter=");
		sb.append(
			"'(context/country %s ''%s'' and context/city %s ''%s'')')))");

		List<Individual> individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "eq", "Japan", "eq", "Tokyo"),
			true, 0, 10, null);

		Assertions.assertEquals(1, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "ne", "Japan", "ne", "Tokyo"),
			true, 0, 10, null);

		Assertions.assertEquals(0, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "eq", "japan", "eq", "tokyo"),
			true, 0, 10, null);

		Assertions.assertEquals(1, individuals.size(), individuals.toString());

		individuals = _searchIndividuals(
			100L, String.format(sb.toString(), "ne", "japan", "ne", "tokyo"),
			true, 0, 10, null);

		Assertions.assertEquals(0, individuals.size(), individuals.toString());
	}

	@Disabled
	@Test
	public void testUpdateDynamicAddMemberships() {
		Individual individual = new Individual();

		individual.setId("123");
		individual.setSegmentIds(Collections.emptySet());

		// TODO Add Individual

		Segment segment = new Segment();

		segment.setFilter("");
		segment.setId(234L);
		segment.setIncludeAnonymousUsers(Boolean.TRUE);
		segment.setIsNew(Boolean.TRUE);
		segment.setStatus("ACTIVE");

		_segmentDog.addSegment(segment);

		// TODO Update Dynamic Memberships

		individual = _fetchIndividual(individual.getId());

		Set<Long> segmentIds = individual.getSegmentIds();

		Assertions.assertArrayEquals(
			new Long[] {234L}, segmentIds.toArray(new Long[0]));
	}

	@Disabled
	@Test
	public void testUpdateDynamicMemberships() {
		Segment segment1 = new Segment();

		segment1.setFilter("");
		segment1.setFilterMetadata("0");
		segment1.setId(338511398116723458L);
		segment1.setIsNew(Boolean.TRUE);
		segment1.setModifiedDate(
			DateUtil.toUTCDate("2019-02-11T20:27:36.603Z"));
		segment1.setName("Test Segment 1");
		segment1.setScope("PROJECT");
		segment1.setType(Segment.Type.STATIC);
		segment1.setState("READY");
		segment1.setStatus("ACTIVE");

		_segmentDog.addSegment(segment1);

		Segment segment2 = new Segment();

		segment2.setFilter("");
		segment2.setFilterMetadata("0");
		segment2.setId(338511451975440187L);
		segment2.setIsNew(Boolean.TRUE);
		segment2.setModifiedDate(
			DateUtil.toUTCDate("2019-02-11T20:27:47.622Z"));
		segment2.setName("Test Segment 2");
		segment2.setScope("PROJECT");
		segment2.setType(Segment.Type.STATIC);
		segment2.setState("READY");
		segment2.setStatus("ACTIVE");

		_segmentDog.addSegment(segment2);

		Segment segment3 = new Segment();

		segment3.setFilter("");
		segment3.setFilterMetadata("0");
		segment3.setId(338511451975440190L);
		segment3.setIsNew(Boolean.TRUE);
		segment3.setModifiedDate(
			DateUtil.toUTCDate("2022-01-06T20:27:47.622Z"));
		segment3.setName("Test Segment 3");
		segment3.setScope("PROJECT");
		segment3.setType(Segment.Type.DYNAMIC);
		segment3.setFilter(
			"(sessions.filter(filter='(context/country eq ''Germany'' and " +
				"completeDate gt ''last24Hours'')'))");
		segment3.setState("READY");
		segment3.setStatus("ACTIVE");

		_segmentDog.addSegment(segment3);

		Individual individual = new Individual();

		individual.setId("338486037253283140");
		individual.setSegmentIds(Collections.singleton(338511398116723458L));

		// TODO Add Individual

		Field field = new Field();

		field.setContext("demographics");
		field.setDataSourceId(338486009405470846L);
		field.setDataSourceName("Test Data Source");
		field.setFieldType("Text");
		field.setModifiedDate(DateUtil.toUTCDate("2019-02-11T17:05:06.814Z"));
		field.setName("email");
		field.setOwnerId(338486037253283140L);
		field.setOwnerType("individual");
		field.setSourceName("email");
		field.setValue("test1@liferay.com");

		individual.setFields(Collections.singleton(field));

		// TODO Update Individual

		BQMembership bqMembership = new BQMembership();

		bqMembership.setCreateDate(
			DateUtil.toUTCDate("2019-02-11T20:26:53.218Z"));
		bqMembership.setIdentityId("338486037253283140");
		bqMembership.setSegmentId(338511451975440187L);
		bqMembership.setStatus("ACTIVE");

		_bqMembershipDog.addBQMembership(bqMembership);

		// TODO Update Dynamic Memberships

		individual = _fetchIndividual("338486037253283140");

		Set<Long> segmentIds = individual.getSegmentIds();

		MatcherAssert.assertThat(
			new Long[] {338511398116723458L, 338511451975440187L},
			Matchers.arrayContainingInAnyOrder(
				segmentIds.toArray(new Long[0])));

		// TODO Delete Individual
		// TODO Update Dynamic Memberships

		Assertions.assertEquals(
			0,
			_bqMembershipRepository.countByIdentityIdAndSegmentId(
				"338486037253283140", 338511398116723458L));

		// TODO Update Dynamic Memberships

		Assertions.assertEquals(
			0, _bqMembershipRepository.countBySegmentId(338511451975440190L));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQMembershipChangeRepository.class,
		resourcePath = "osbasahfaroinfo/bq_membership_changes.json"
	)
	@RepositoryResource(
		repositoryClass = BQMembershipRepository.class,
		resourcePath = "osbasahfaroinfo/bq_memberships.json"
	)
	@RepositoryResource(
		repositoryClass = SegmentRepository.class,
		resourcePath = "osbasahfaroinfo/individual_segments.json"
	)
	@Test
	public void testUpdateDynamicRemoveMemberships() {
		Assertions.assertEquals(
			3,
			_bqMembershipRepository.countBySegmentIdAndStatus(
				338511398116723458L, "INACTIVE"));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@RepositoryResource(
		repositoryClass = DXPEntityRepository.class,
		resourcePath = "osbasahdxpraw/users.json"
	)
	@Test
	public void testUpdateIndividualAssociation() throws Exception {
		DataSource dataSource = FaroInfoTestUtil.buildLiferayDataSource();

		dataSource.setId(402139209179557944L);

		_dataSourceRepository.save(dataSource);

		BQOrganization bqOrganization1 = new BQOrganization();

		bqOrganization1.setDataSourceId(402139209179557944L);
		bqOrganization1.setId("402139267512234420");
		bqOrganization1.setName("engineering");
		bqOrganization1.setOrganizationId(33120L);

		_bqOrganizationRepository.insert(bqOrganization1);

		BQOrganization bqOrganization2 = new BQOrganization();

		bqOrganization2.setDataSourceId(402139209179557944L);
		bqOrganization2.setId("402139268847589064");
		bqOrganization2.setName("marketing");
		bqOrganization2.setOrganizationId(33134L);

		_bqOrganizationRepository.insert(bqOrganization2);

		Individual individual = new Individual();

		individual.setBQDataSourceUsers(
			Collections.singleton(
				new BQDataSourceUser(
					Collections.emptySet(), 402139209179557944L,
					402139280465582637L,
					Collections.singleton(
						"86ada3db-d8f9-c59f-7985-5c8fbdebb169"))));
		individual.setId("402139280465582637");
		individual.setOrganizationIds(
			Collections.singleton(402139267512234420L));

		// TODO Add Individual

		Field field = new Field();

		field.setContext("demographics");
		field.setDataSourceId(402139209179557944L);
		field.setDataSourceName("Test Data Source");
		field.setFieldType("Text");
		field.setModifiedDate(DateUtil.toUTCDate("2020-01-30T17:59:38.729Z"));
		field.setName("email");
		field.setOwnerId(402139280465582637L);
		field.setOwnerType("individual");
		field.setSourceName("email");
		field.setValue("test1@liferay.com");

		individual.setFields(Collections.singleton(field));

		// TODO Update Individual

		// TODO Add BQFieldMapping "email", "Text"

		individual = _updateIndividual(
			"402139280465582637",
			JSONUtil.put(
				"createDate", System.currentTimeMillis()
			).put(
				"emailAddress", "test1@liferay.com"
			).put(
				"memberships",
				JSONUtil.put(
					DXPEntityType.CLASS_NAME_ORGANIZATION,
					JSONUtil.putAll(33120, 33134))
			).put(
				"osbAsahDataSourceId", String.valueOf(dataSource.getId())
			).put(
				"userId", 36016
			),
			dataSource, individual);

		Set<Long> organizationIds = individual.getOrganizationIds();

		MatcherAssert.assertThat(
			new Long[] {402139267512234420L, 402139268847589064L},
			Matchers.arrayContainingInAnyOrder(
				organizationIds.toArray(new Long[0])));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals_3.json"
	)
	@Test
	public void testUpdateIndividualFirstEnrichmentDate() throws Exception {
		Individual individual = _updateIndividual(
			null,
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"email", "john.doe@liferay.com"
				).put(
					"middleName", "Joseph"
				)
			).put(
				"modifiedDate", System.currentTimeMillis()
			),
			_liferayDataSource, _fetchIndividual("523130384134494521"));

		Assertions.assertNotNull(individual.getFirstEnrichmentDate());
	}

	@Disabled
	@Test
	public void testUpdateIndividualFromDifferentDataSourceIgnoresNullValue()
		throws Exception {

		Individual individual = _addIndividual(
			"2",
			JSONUtil.put(
				"country", "United States"
			).put(
				"email", "dummy.test@test.com"
			).put(
				"modifiedDate", DateUtil.newDateString()
			),
			null);

		individual = _updateIndividual(
			String.valueOf(individual.getId()),
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"country", ""
				).put(
					"email", "dummy.test@test.com"
				)
			).put(
				"modifiedDate", System.currentTimeMillis()
			),
			_liferayDataSource, individual);

		Set<Field> fields = individual.getFields();

		Stream<Field> stream = fields.stream();

		Field countryField = stream.filter(
			field -> Objects.equals(field.getName(), "country")
		).findFirst(
		).orElse(
			null
		);

		Assertions.assertEquals("United States", countryField.getValue());
	}

	@Disabled
	@Test
	public void testUpdateIndividualUpdatesPagesAndAssets() throws Exception {
		Date date = DateUtil.toUTCDate(DateUtil.newDayDateString());

		Individual individual = new Individual();

		//individual.setActivitiesCounts(Collections.emptySet());

		BQDataSourceUser bqDataSourceUser = new BQDataSourceUser();

		bqDataSourceUser.setUserPKs(Collections.singleton("12345"));
		bqDataSourceUser.setDataSourceId(_liferayDataSource.getId());

		individual.setBQDataSourceUsers(
			Collections.singleton(bqDataSourceUser));

		individual.setChannelIds(Collections.singleton(1L));
		individual.setCreateDate(date);

		individual.setEmailAddressHashed(
			"47ff64395860b1d498241d907069f649b98c198a95b3ba5303b87094058590");
		individual.setModifiedDate(date);
		individual.setSegmentIds(Collections.emptySet());

		// TODO Add Individual
		// TODO Add page record associated with individual

		_updateIndividualAsync(
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"country", ""
				).put(
					"email", "dummy.test@test.com"
				)
			).put(
				"modifiedDate", System.currentTimeMillis()
			),
			individual);

		// TODO Assert page record is now associated to a known individual

	}

	private Individual _addIndividual(
		String dataId, JSONObject dataJSONObject,
		DataSource liferayDataSource) {

		return null;
	}

	private Individual _addIndividualAssociation(
		int classPK, long dataSourceId, DXPEntity.Type dxpEntityType,
		Individual individual) {

		return null;
	}

	private void _assertCustomFields(
		Set<Field> customFields, Object expectedValue, String fieldName) {

		Stream<Field> stream = customFields.stream();

		List<Object> values = stream.filter(
			field -> Objects.equals(fieldName, field.getName())
		).map(
			Field::getValue
		).collect(
			Collectors.toList()
		);

		if (values.isEmpty()) {
			Assertions.assertNull(expectedValue);
		}
		else if (values.size() == 1) {
			Assertions.assertEquals(
				String.valueOf(expectedValue), String.valueOf(values.get(0)));
		}
		else {
			MatcherAssert.assertThat(
				(Object[])expectedValue,
				Matchers.arrayContainingInAnyOrder(
					values.toArray(new Object[0])));
		}
	}

	private void _assertDataSourceUserPKs(
		Object[] expectedValue, Individual individual) {

		Set<Individual.DataSourceUserPK> dataSourceUserPKs =
			individual.getDataSourceUserPKs();

		Iterator<Individual.DataSourceUserPK> iterator =
			dataSourceUserPKs.iterator();

		Individual.DataSourceUserPK dataSourceUserPK = iterator.next();

		Set<String> userPKs = dataSourceUserPK.getUserPKs();

		MatcherAssert.assertThat(
			expectedValue,
			Matchers.arrayContainingInAnyOrder(userPKs.toArray(new Object[0])));
	}

	private void _assertIndividualMiddleName(
		String expectedMiddleName, Individual individual) {

		Set<Field> fields = individual.getFields();

		Stream<Field> stream = fields.stream();

		Field middleNameField = stream.filter(
			field -> Objects.equals(field.getName(), "middleName")
		).findFirst(
		).orElse(
			null
		);

		Assertions.assertEquals(expectedMiddleName, middleNameField.getValue());
	}

	private int _countActiveIndividualsFromLast30DaysBySegment(
		Segment segment) {

		return 0;
	}

	private Individual _deleteIndividualAssociation(
		int classPK, long dataSourceId, DXPEntity.Type dxpEntityType,
		Individual individual) {

		return new Individual();
	}

	private Individual _fetchIndividual(String individualId) {
		return new Individual();
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

	private Page<Individual> _getIndividualPage(
		Date fromCreateDate, Long individualId, int size,
		org.springframework.data.domain.Sort sort, Date toCreateDate) {

		return Page.empty();
	}

	private Page<Individual> _getIndividualPage(
		String query, Long segmentId, int page, int size) {

		return Page.empty();
	}

	private List<Individual> _searchIndividuals(
		Long channelId, String filterString, Boolean includeAnonymousUsers,
		int page, int size, String[] sorts) {

		return Collections.emptyList();
	}

	private Individual _updateIndividual(
		String dataId, JSONObject dataJSONObject, DataSource liferayDataSource,
		Individual individual) {

		return new Individual();
	}

	private void _updateIndividualAsync(
			JSONObject dataJSONObject, Individual individual)
		throws Exception {

		String projectId = ProjectIdThreadLocal.getProjectId();

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		Future<?> future = executorService.submit(
			() -> {
				try {
					ProjectIdThreadLocal.setProjectId(projectId);

					// TODO Update Individual

				}
				catch (Exception exception) {
				}
			});

		try {
			future.get(3, TimeUnit.SECONDS);
		}
		catch (TimeoutException timeoutException) {
			future.cancel(true);
		}
		finally {
			executorService.shutdownNow();
		}
	}

	@Autowired
	private BQIndividualDog _bqIndividualDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

	@Autowired
	private BQOrganizationRepository _bqOrganizationRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	private DataSource _liferayDataSource;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SegmentDog _segmentDog;

}