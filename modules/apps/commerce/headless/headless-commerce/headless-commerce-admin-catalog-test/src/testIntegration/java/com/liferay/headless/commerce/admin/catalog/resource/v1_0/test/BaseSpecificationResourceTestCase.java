/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.headless.commerce.admin.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.Specification;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.SpecificationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.SpecificationSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Level;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseSpecificationResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_specificationResource.setContextCompany(testCompany);

		SpecificationResource.Builder builder = SpecificationResource.builder();

		specificationResource = builder.locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		Specification specification1 = randomSpecification();

		String json = objectMapper.writeValueAsString(specification1);

		Specification specification2 = SpecificationSerDes.toDTO(json);

		Assert.assertTrue(equals(specification1, specification2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		Specification specification = randomSpecification();

		String json1 = objectMapper.writeValueAsString(specification);
		String json2 = SpecificationSerDes.toJSON(specification);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Specification specification = randomSpecification();

		specification.setKey(regex);

		String json = SpecificationSerDes.toJSON(specification);

		Assert.assertFalse(json.contains(regex));

		specification = SpecificationSerDes.toDTO(json);

		Assert.assertEquals(regex, specification.getKey());
	}

	@Test
	public void testGetSpecificationsPage() throws Exception {
		Page<Specification> page = specificationResource.getSpecificationsPage(
			RandomTestUtil.randomString(), null, Pagination.of(1, 2), null);

		Assert.assertEquals(0, page.getTotalCount());

		Specification specification1 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		Specification specification2 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		page = specificationResource.getSpecificationsPage(
			null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(specification1, specification2),
			(List<Specification>)page.getItems());
		assertValid(page);

		specificationResource.deleteSpecification(null);

		specificationResource.deleteSpecification(null);
	}

	@Test
	public void testGetSpecificationsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Specification specification1 = randomSpecification();

		specification1 = testGetSpecificationsPage_addSpecification(
			specification1);

		for (EntityField entityField : entityFields) {
			Page<Specification> page =
				specificationResource.getSpecificationsPage(
					null,
					getFilterString(entityField, "between", specification1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(specification1),
				(List<Specification>)page.getItems());
		}
	}

	@Test
	public void testGetSpecificationsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Specification specification1 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Specification specification2 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		for (EntityField entityField : entityFields) {
			Page<Specification> page =
				specificationResource.getSpecificationsPage(
					null, getFilterString(entityField, "eq", specification1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(specification1),
				(List<Specification>)page.getItems());
		}
	}

	@Test
	public void testGetSpecificationsPageWithPagination() throws Exception {
		Specification specification1 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		Specification specification2 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		Specification specification3 =
			testGetSpecificationsPage_addSpecification(randomSpecification());

		Page<Specification> page1 = specificationResource.getSpecificationsPage(
			null, null, Pagination.of(1, 2), null);

		List<Specification> specifications1 =
			(List<Specification>)page1.getItems();

		Assert.assertEquals(
			specifications1.toString(), 2, specifications1.size());

		Page<Specification> page2 = specificationResource.getSpecificationsPage(
			null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Specification> specifications2 =
			(List<Specification>)page2.getItems();

		Assert.assertEquals(
			specifications2.toString(), 1, specifications2.size());

		Page<Specification> page3 = specificationResource.getSpecificationsPage(
			null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(specification1, specification2, specification3),
			(List<Specification>)page3.getItems());
	}

	@Test
	public void testGetSpecificationsPageWithSortDateTime() throws Exception {
		testGetSpecificationsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, specification1, specification2) -> {
				BeanUtils.setProperty(
					specification1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetSpecificationsPageWithSortInteger() throws Exception {
		testGetSpecificationsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, specification1, specification2) -> {
				BeanUtils.setProperty(specification1, entityField.getName(), 0);
				BeanUtils.setProperty(specification2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetSpecificationsPageWithSortString() throws Exception {
		testGetSpecificationsPageWithSort(
			EntityField.Type.STRING,
			(entityField, specification1, specification2) -> {
				Class<?> clazz = specification1.getClass();

				Method method = clazz.getMethod(
					"get" +
						StringUtil.upperCaseFirstLetter(entityField.getName()));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanUtils.setProperty(
						specification1, entityField.getName(),
						Collections.singletonMap("Aaa", "Aaa"));
					BeanUtils.setProperty(
						specification2, entityField.getName(),
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else {
					BeanUtils.setProperty(
						specification1, entityField.getName(),
						"Aaa" + RandomTestUtil.randomString());
					BeanUtils.setProperty(
						specification2, entityField.getName(),
						"Bbb" + RandomTestUtil.randomString());
				}
			});
	}

	protected void testGetSpecificationsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, Specification, Specification, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Specification specification1 = randomSpecification();
		Specification specification2 = randomSpecification();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, specification1, specification2);
		}

		specification1 = testGetSpecificationsPage_addSpecification(
			specification1);

		specification2 = testGetSpecificationsPage_addSpecification(
			specification2);

		for (EntityField entityField : entityFields) {
			Page<Specification> ascPage =
				specificationResource.getSpecificationsPage(
					null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(specification1, specification2),
				(List<Specification>)ascPage.getItems());

			Page<Specification> descPage =
				specificationResource.getSpecificationsPage(
					null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(specification2, specification1),
				(List<Specification>)descPage.getItems());
		}
	}

	protected Specification testGetSpecificationsPage_addSpecification(
			Specification specification)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSpecificationsPage() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		List<GraphQLField> itemsGraphQLFields = getGraphQLFields();

		graphQLFields.add(
			new GraphQLField(
				"items", itemsGraphQLFields.toArray(new GraphQLField[0])));

		graphQLFields.add(new GraphQLField("page"));
		graphQLFields.add(new GraphQLField("totalCount"));

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"specifications",
				new HashMap<String, Object>() {
					{
						put("page", 1);
						put("pageSize", 2);
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		JSONObject specificationsJSONObject = dataJSONObject.getJSONObject(
			"specifications");

		Assert.assertEquals(0, specificationsJSONObject.get("totalCount"));

		Specification specification1 =
			testGraphQLSpecification_addSpecification();
		Specification specification2 =
			testGraphQLSpecification_addSpecification();

		jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		dataJSONObject = jsonObject.getJSONObject("data");

		specificationsJSONObject = dataJSONObject.getJSONObject(
			"specifications");

		Assert.assertEquals(2, specificationsJSONObject.get("totalCount"));

		assertEqualsJSONArray(
			Arrays.asList(specification1, specification2),
			specificationsJSONObject.getJSONArray("items"));
	}

	@Test
	public void testPostSpecification() throws Exception {
		Specification randomSpecification = randomSpecification();

		Specification postSpecification =
			testPostSpecification_addSpecification(randomSpecification);

		assertEquals(randomSpecification, postSpecification);
		assertValid(postSpecification);
	}

	protected Specification testPostSpecification_addSpecification(
			Specification specification)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteSpecification() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Specification specification =
			testDeleteSpecification_addSpecification();

		assertHttpResponseStatusCode(
			204,
			specificationResource.deleteSpecificationHttpResponse(
				specification.getId()));

		assertHttpResponseStatusCode(
			404,
			specificationResource.getSpecificationHttpResponse(
				specification.getId()));

		assertHttpResponseStatusCode(
			404,
			specificationResource.getSpecificationHttpResponse(
				specification.getId()));
	}

	protected Specification testDeleteSpecification_addSpecification()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteSpecification() throws Exception {
		Specification specification =
			testGraphQLSpecification_addSpecification();

		GraphQLField graphQLField = new GraphQLField(
			"mutation",
			new GraphQLField(
				"deleteSpecification",
				new HashMap<String, Object>() {
					{
						put("specificationId", specification.getId());
					}
				}));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(dataJSONObject.getBoolean("deleteSpecification"));

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"graphql.execution.SimpleDataFetcherExceptionHandler",
					Level.WARN)) {

			graphQLField = new GraphQLField(
				"query",
				new GraphQLField(
					"specification",
					new HashMap<String, Object>() {
						{
							put("specificationId", specification.getId());
						}
					},
					new GraphQLField("id")));

			jsonObject = JSONFactoryUtil.createJSONObject(
				invoke(graphQLField.toString()));

			JSONArray errorsJSONArray = jsonObject.getJSONArray("errors");

			Assert.assertTrue(errorsJSONArray.length() > 0);
		}
	}

	@Test
	public void testGetSpecification() throws Exception {
		Specification postSpecification =
			testGetSpecification_addSpecification();

		Specification getSpecification = specificationResource.getSpecification(
			postSpecification.getId());

		assertEquals(postSpecification, getSpecification);
		assertValid(getSpecification);
	}

	protected Specification testGetSpecification_addSpecification()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetSpecification() throws Exception {
		Specification specification =
			testGraphQLSpecification_addSpecification();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"specification",
				new HashMap<String, Object>() {
					{
						put("id", specification.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				specification, dataJSONObject.getJSONObject("specification")));
	}

	@Test
	public void testPatchSpecification() throws Exception {
		Assert.assertTrue(false);
	}

	protected Specification testGraphQLSpecification_addSpecification()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		Specification specification1, Specification specification2) {

		Assert.assertTrue(
			specification1 + " does not equal " + specification2,
			equals(specification1, specification2));
	}

	protected void assertEquals(
		List<Specification> specifications1,
		List<Specification> specifications2) {

		Assert.assertEquals(specifications1.size(), specifications2.size());

		for (int i = 0; i < specifications1.size(); i++) {
			Specification specification1 = specifications1.get(i);
			Specification specification2 = specifications2.get(i);

			assertEquals(specification1, specification2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Specification> specifications1,
		List<Specification> specifications2) {

		Assert.assertEquals(specifications1.size(), specifications2.size());

		for (Specification specification1 : specifications1) {
			boolean contains = false;

			for (Specification specification2 : specifications2) {
				if (equals(specification1, specification2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				specifications2 + " does not contain " + specification1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<Specification> specifications, JSONArray jsonArray) {

		for (Specification specification : specifications) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(specification, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + specification, contains);
		}
	}

	protected void assertValid(Specification specification) {
		boolean valid = true;

		if (specification.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (specification.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("facetable", additionalAssertFieldName)) {
				if (specification.getFacetable() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (specification.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("optionCategory", additionalAssertFieldName)) {
				if (specification.getOptionCategory() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (specification.getTitle() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<Specification> page) {
		boolean valid = false;

		java.util.Collection<Specification> specifications = page.getItems();

		int size = specifications.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			graphQLFields.add(new GraphQLField(additionalAssertFieldName));
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(
		Specification specification1, Specification specification2) {

		if (specification1 == specification2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getDescription(),
						specification2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("facetable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getFacetable(),
						specification2.getFacetable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getId(), specification2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getKey(), specification2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("optionCategory", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getOptionCategory(),
						specification2.getOptionCategory())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("title", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						specification1.getTitle(), specification2.getTitle())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equalsJSONObject(
		Specification specification, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("facetable", fieldName)) {
				if (!Objects.deepEquals(
						specification.getFacetable(),
						jsonObject.getBoolean("facetable"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						specification.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", fieldName)) {
				if (!Objects.deepEquals(
						specification.getKey(), jsonObject.getString("key"))) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid field name " + fieldName);
		}

		return true;
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_specificationResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_specificationResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField ->
				Objects.equals(entityField.getType(), type) &&
				!ArrayUtil.contains(
					getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator, Specification specification) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("description")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("facetable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(specification.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("optionCategory")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("title")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected Specification randomSpecification() throws Exception {
		return new Specification() {
			{
				facetable = RandomTestUtil.randomBoolean();
				id = RandomTestUtil.randomLong();
				key = RandomTestUtil.randomString();
			}
		};
	}

	protected Specification randomIrrelevantSpecification() throws Exception {
		Specification randomIrrelevantSpecification = randomSpecification();

		return randomIrrelevantSpecification;
	}

	protected Specification randomPatchSpecification() throws Exception {
		return randomSpecification();
	}

	protected SpecificationResource specificationResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(":");
					sb.append(entry.getValue());
					sb.append(",");
				}

				sb.setLength(sb.length() - 1);

				sb.append(")");
			}

			if (_graphQLFields.length > 0) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(",");
				}

				sb.setLength(sb.length() - 1);

				sb.append("}");
			}

			return sb.toString();
		}

		private final GraphQLField[] _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseSpecificationResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		SpecificationResource _specificationResource;

}