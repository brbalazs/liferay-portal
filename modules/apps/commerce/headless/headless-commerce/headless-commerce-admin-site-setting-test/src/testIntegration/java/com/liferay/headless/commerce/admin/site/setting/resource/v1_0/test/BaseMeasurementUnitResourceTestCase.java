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

package com.liferay.headless.commerce.admin.site.setting.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.site.setting.client.dto.v1_0.MeasurementUnit;
import com.liferay.headless.commerce.admin.site.setting.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Page;
import com.liferay.headless.commerce.admin.site.setting.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.site.setting.client.resource.v1_0.MeasurementUnitResource;
import com.liferay.headless.commerce.admin.site.setting.client.serdes.v1_0.MeasurementUnitSerDes;
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
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.beanutils.BeanUtilsBean;
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
public abstract class BaseMeasurementUnitResourceTestCase {

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

		_measurementUnitResource.setContextCompany(testCompany);

		MeasurementUnitResource.Builder builder =
			MeasurementUnitResource.builder();

		measurementUnitResource = builder.locale(
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

		MeasurementUnit measurementUnit1 = randomMeasurementUnit();

		String json = objectMapper.writeValueAsString(measurementUnit1);

		MeasurementUnit measurementUnit2 = MeasurementUnitSerDes.toDTO(json);

		Assert.assertTrue(equals(measurementUnit1, measurementUnit2));
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

		MeasurementUnit measurementUnit = randomMeasurementUnit();

		String json1 = objectMapper.writeValueAsString(measurementUnit);
		String json2 = MeasurementUnitSerDes.toJSON(measurementUnit);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		MeasurementUnit measurementUnit = randomMeasurementUnit();

		measurementUnit.setKey(regex);

		String json = MeasurementUnitSerDes.toJSON(measurementUnit);

		Assert.assertFalse(json.contains(regex));

		measurementUnit = MeasurementUnitSerDes.toDTO(json);

		Assert.assertEquals(regex, measurementUnit.getKey());
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupMeasurementUnitPage()
		throws Exception {

		Page<MeasurementUnit> page =
			measurementUnitResource.
				getCommerceAdminSiteSettingGroupMeasurementUnitPage(
					testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getGroupId(),
					null, Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());

		Long groupId =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getGroupId();
		Long irrelevantGroupId =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getIrrelevantGroupId();

		if ((irrelevantGroupId != null)) {
			MeasurementUnit irrelevantMeasurementUnit =
				testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
					irrelevantGroupId, randomIrrelevantMeasurementUnit());

			page =
				measurementUnitResource.
					getCommerceAdminSiteSettingGroupMeasurementUnitPage(
						irrelevantGroupId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantMeasurementUnit),
				(List<MeasurementUnit>)page.getItems());
			assertValid(page);
		}

		MeasurementUnit measurementUnit1 =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				groupId, randomMeasurementUnit());

		MeasurementUnit measurementUnit2 =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				groupId, randomMeasurementUnit());

		page =
			measurementUnitResource.
				getCommerceAdminSiteSettingGroupMeasurementUnitPage(
					groupId, null, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(measurementUnit1, measurementUnit2),
			(List<MeasurementUnit>)page.getItems());
		assertValid(page);

		measurementUnitResource.deleteMeasurementUnit(null);

		measurementUnitResource.deleteMeasurementUnit(null);
	}

	@Test
	public void testGetCommerceAdminSiteSettingGroupMeasurementUnitPageWithPagination()
		throws Exception {

		Long groupId =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getGroupId();

		MeasurementUnit measurementUnit1 =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				groupId, randomMeasurementUnit());

		MeasurementUnit measurementUnit2 =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				groupId, randomMeasurementUnit());

		MeasurementUnit measurementUnit3 =
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				groupId, randomMeasurementUnit());

		Page<MeasurementUnit> page1 =
			measurementUnitResource.
				getCommerceAdminSiteSettingGroupMeasurementUnitPage(
					groupId, null, Pagination.of(1, 2));

		List<MeasurementUnit> measurementUnits1 =
			(List<MeasurementUnit>)page1.getItems();

		Assert.assertEquals(
			measurementUnits1.toString(), 2, measurementUnits1.size());

		Page<MeasurementUnit> page2 =
			measurementUnitResource.
				getCommerceAdminSiteSettingGroupMeasurementUnitPage(
					groupId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<MeasurementUnit> measurementUnits2 =
			(List<MeasurementUnit>)page2.getItems();

		Assert.assertEquals(
			measurementUnits2.toString(), 1, measurementUnits2.size());

		Page<MeasurementUnit> page3 =
			measurementUnitResource.
				getCommerceAdminSiteSettingGroupMeasurementUnitPage(
					groupId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(measurementUnit1, measurementUnit2, measurementUnit3),
			(List<MeasurementUnit>)page3.getItems());
	}

	protected MeasurementUnit
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_addMeasurementUnit(
				Long groupId, MeasurementUnit measurementUnit)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getGroupId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetCommerceAdminSiteSettingGroupMeasurementUnitPage_getIrrelevantGroupId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostCommerceAdminSiteSettingGroupMeasurementUnit()
		throws Exception {

		MeasurementUnit randomMeasurementUnit = randomMeasurementUnit();

		MeasurementUnit postMeasurementUnit =
			testPostCommerceAdminSiteSettingGroupMeasurementUnit_addMeasurementUnit(
				randomMeasurementUnit);

		assertEquals(randomMeasurementUnit, postMeasurementUnit);
		assertValid(postMeasurementUnit);
	}

	protected MeasurementUnit
			testPostCommerceAdminSiteSettingGroupMeasurementUnit_addMeasurementUnit(
				MeasurementUnit measurementUnit)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteMeasurementUnit() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		MeasurementUnit measurementUnit =
			testDeleteMeasurementUnit_addMeasurementUnit();

		assertHttpResponseStatusCode(
			204,
			measurementUnitResource.deleteMeasurementUnitHttpResponse(
				measurementUnit.getId()));

		assertHttpResponseStatusCode(
			404,
			measurementUnitResource.getMeasurementUnitHttpResponse(
				measurementUnit.getId()));

		assertHttpResponseStatusCode(
			404,
			measurementUnitResource.getMeasurementUnitHttpResponse(
				measurementUnit.getId()));
	}

	protected MeasurementUnit testDeleteMeasurementUnit_addMeasurementUnit()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteMeasurementUnit() throws Exception {
		MeasurementUnit measurementUnit =
			testGraphQLMeasurementUnit_addMeasurementUnit();

		GraphQLField graphQLField = new GraphQLField(
			"mutation",
			new GraphQLField(
				"deleteMeasurementUnit",
				new HashMap<String, Object>() {
					{
						put("measurementUnitId", measurementUnit.getId());
					}
				}));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(dataJSONObject.getBoolean("deleteMeasurementUnit"));

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"graphql.execution.SimpleDataFetcherExceptionHandler",
					Level.WARN)) {

			graphQLField = new GraphQLField(
				"query",
				new GraphQLField(
					"measurementUnit",
					new HashMap<String, Object>() {
						{
							put("measurementUnitId", measurementUnit.getId());
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
	public void testGetMeasurementUnit() throws Exception {
		MeasurementUnit postMeasurementUnit =
			testGetMeasurementUnit_addMeasurementUnit();

		MeasurementUnit getMeasurementUnit =
			measurementUnitResource.getMeasurementUnit(
				postMeasurementUnit.getId());

		assertEquals(postMeasurementUnit, getMeasurementUnit);
		assertValid(getMeasurementUnit);
	}

	protected MeasurementUnit testGetMeasurementUnit_addMeasurementUnit()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetMeasurementUnit() throws Exception {
		MeasurementUnit measurementUnit =
			testGraphQLMeasurementUnit_addMeasurementUnit();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"measurementUnit",
				new HashMap<String, Object>() {
					{
						put("id", measurementUnit.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				measurementUnit,
				dataJSONObject.getJSONObject("measurementUnit")));
	}

	@Test
	public void testPutMeasurementUnit() throws Exception {
		Assert.assertTrue(false);
	}

	protected MeasurementUnit testGraphQLMeasurementUnit_addMeasurementUnit()
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
		MeasurementUnit measurementUnit1, MeasurementUnit measurementUnit2) {

		Assert.assertTrue(
			measurementUnit1 + " does not equal " + measurementUnit2,
			equals(measurementUnit1, measurementUnit2));
	}

	protected void assertEquals(
		List<MeasurementUnit> measurementUnits1,
		List<MeasurementUnit> measurementUnits2) {

		Assert.assertEquals(measurementUnits1.size(), measurementUnits2.size());

		for (int i = 0; i < measurementUnits1.size(); i++) {
			MeasurementUnit measurementUnit1 = measurementUnits1.get(i);
			MeasurementUnit measurementUnit2 = measurementUnits2.get(i);

			assertEquals(measurementUnit1, measurementUnit2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<MeasurementUnit> measurementUnits1,
		List<MeasurementUnit> measurementUnits2) {

		Assert.assertEquals(measurementUnits1.size(), measurementUnits2.size());

		for (MeasurementUnit measurementUnit1 : measurementUnits1) {
			boolean contains = false;

			for (MeasurementUnit measurementUnit2 : measurementUnits2) {
				if (equals(measurementUnit1, measurementUnit2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				measurementUnits2 + " does not contain " + measurementUnit1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<MeasurementUnit> measurementUnits, JSONArray jsonArray) {

		for (MeasurementUnit measurementUnit : measurementUnits) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(measurementUnit, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + measurementUnit, contains);
		}
	}

	protected void assertValid(MeasurementUnit measurementUnit) {
		boolean valid = true;

		if (measurementUnit.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (measurementUnit.getGroupId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (measurementUnit.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (measurementUnit.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (measurementUnit.getPrimary() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (measurementUnit.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("rate", additionalAssertFieldName)) {
				if (measurementUnit.getRate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (measurementUnit.getType() == null) {
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

	protected void assertValid(Page<MeasurementUnit> page) {
		boolean valid = false;

		java.util.Collection<MeasurementUnit> measurementUnits =
			page.getItems();

		int size = measurementUnits.size();

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
		MeasurementUnit measurementUnit1, MeasurementUnit measurementUnit2) {

		if (measurementUnit1 == measurementUnit2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("groupId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getGroupId(),
						measurementUnit2.getGroupId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getId(), measurementUnit2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getKey(), measurementUnit2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getName(),
						measurementUnit2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("primary", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getPrimary(),
						measurementUnit2.getPrimary())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getPriority(),
						measurementUnit2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("rate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getRate(),
						measurementUnit2.getRate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						measurementUnit1.getType(),
						measurementUnit2.getType())) {

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
		MeasurementUnit measurementUnit, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("groupId", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getGroupId(),
						jsonObject.getLong("groupId"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getKey(),
						jsonObject.getString("key"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("primary", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getPrimary(),
						jsonObject.getBoolean("primary"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getPriority(),
						jsonObject.getDouble("priority"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("rate", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getRate(),
						jsonObject.getDouble("rate"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", fieldName)) {
				if (!Objects.deepEquals(
						measurementUnit.getType(), jsonObject.getInt("type"))) {

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

		if (!(_measurementUnitResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_measurementUnitResource;

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
		EntityField entityField, String operator,
		MeasurementUnit measurementUnit) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("groupId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(measurementUnit.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("primary")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("rate")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
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

	protected MeasurementUnit randomMeasurementUnit() throws Exception {
		return new MeasurementUnit() {
			{
				groupId = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
				key = RandomTestUtil.randomString();
				primary = RandomTestUtil.randomBoolean();
				priority = RandomTestUtil.randomDouble();
				rate = RandomTestUtil.randomDouble();
				type = RandomTestUtil.randomInt();
			}
		};
	}

	protected MeasurementUnit randomIrrelevantMeasurementUnit()
		throws Exception {

		MeasurementUnit randomIrrelevantMeasurementUnit =
			randomMeasurementUnit();

		return randomIrrelevantMeasurementUnit;
	}

	protected MeasurementUnit randomPatchMeasurementUnit() throws Exception {
		return randomMeasurementUnit();
	}

	protected MeasurementUnitResource measurementUnitResource;
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
		BaseMeasurementUnitResourceTestCase.class);

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
	private com.liferay.headless.commerce.admin.site.setting.resource.v1_0.
		MeasurementUnitResource _measurementUnitResource;

}