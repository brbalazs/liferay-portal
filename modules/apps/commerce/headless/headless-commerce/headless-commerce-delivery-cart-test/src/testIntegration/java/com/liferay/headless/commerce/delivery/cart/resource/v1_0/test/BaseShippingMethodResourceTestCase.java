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

package com.liferay.headless.commerce.delivery.cart.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.ShippingMethod;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.ShippingMethodResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.ShippingMethodSerDes;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BaseShippingMethodResourceTestCase {

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

		_shippingMethodResource.setContextCompany(testCompany);

		ShippingMethodResource.Builder builder =
			ShippingMethodResource.builder();

		shippingMethodResource = builder.locale(
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

		ShippingMethod shippingMethod1 = randomShippingMethod();

		String json = objectMapper.writeValueAsString(shippingMethod1);

		ShippingMethod shippingMethod2 = ShippingMethodSerDes.toDTO(json);

		Assert.assertTrue(equals(shippingMethod1, shippingMethod2));
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

		ShippingMethod shippingMethod = randomShippingMethod();

		String json1 = objectMapper.writeValueAsString(shippingMethod);
		String json2 = ShippingMethodSerDes.toJSON(shippingMethod);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ShippingMethod shippingMethod = randomShippingMethod();

		shippingMethod.setDescription(regex);
		shippingMethod.setName(regex);

		String json = ShippingMethodSerDes.toJSON(shippingMethod);

		Assert.assertFalse(json.contains(regex));

		shippingMethod = ShippingMethodSerDes.toDTO(json);

		Assert.assertEquals(regex, shippingMethod.getDescription());
		Assert.assertEquals(regex, shippingMethod.getName());
	}

	@Test
	public void testGetChannelCartShippingMethodsPage() throws Exception {
		Page<ShippingMethod> page =
			shippingMethodResource.getChannelCartShippingMethodsPage(
				testGetChannelCartShippingMethodsPage_getChannelId(),
				testGetChannelCartShippingMethodsPage_getCartId());

		Assert.assertEquals(0, page.getTotalCount());

		Long channelId = testGetChannelCartShippingMethodsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelCartShippingMethodsPage_getIrrelevantChannelId();
		Long cartId = testGetChannelCartShippingMethodsPage_getCartId();
		Long irrelevantCartId =
			testGetChannelCartShippingMethodsPage_getIrrelevantCartId();

		if ((irrelevantChannelId != null) && (irrelevantCartId != null)) {
			ShippingMethod irrelevantShippingMethod =
				testGetChannelCartShippingMethodsPage_addShippingMethod(
					irrelevantChannelId, irrelevantCartId,
					randomIrrelevantShippingMethod());

			page = shippingMethodResource.getChannelCartShippingMethodsPage(
				irrelevantChannelId, irrelevantCartId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantShippingMethod),
				(List<ShippingMethod>)page.getItems());
			assertValid(page);
		}

		ShippingMethod shippingMethod1 =
			testGetChannelCartShippingMethodsPage_addShippingMethod(
				channelId, cartId, randomShippingMethod());

		ShippingMethod shippingMethod2 =
			testGetChannelCartShippingMethodsPage_addShippingMethod(
				channelId, cartId, randomShippingMethod());

		page = shippingMethodResource.getChannelCartShippingMethodsPage(
			channelId, cartId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(shippingMethod1, shippingMethod2),
			(List<ShippingMethod>)page.getItems());
		assertValid(page);
	}

	protected ShippingMethod
			testGetChannelCartShippingMethodsPage_addShippingMethod(
				Long channelId, Long cartId, ShippingMethod shippingMethod)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartShippingMethodsPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetChannelCartShippingMethodsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelCartShippingMethodsPage_getCartId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartShippingMethodsPage_getIrrelevantCartId()
		throws Exception {

		return null;
	}

	protected ShippingMethod testGraphQLShippingMethod_addShippingMethod()
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
		ShippingMethod shippingMethod1, ShippingMethod shippingMethod2) {

		Assert.assertTrue(
			shippingMethod1 + " does not equal " + shippingMethod2,
			equals(shippingMethod1, shippingMethod2));
	}

	protected void assertEquals(
		List<ShippingMethod> shippingMethods1,
		List<ShippingMethod> shippingMethods2) {

		Assert.assertEquals(shippingMethods1.size(), shippingMethods2.size());

		for (int i = 0; i < shippingMethods1.size(); i++) {
			ShippingMethod shippingMethod1 = shippingMethods1.get(i);
			ShippingMethod shippingMethod2 = shippingMethods2.get(i);

			assertEquals(shippingMethod1, shippingMethod2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ShippingMethod> shippingMethods1,
		List<ShippingMethod> shippingMethods2) {

		Assert.assertEquals(shippingMethods1.size(), shippingMethods2.size());

		for (ShippingMethod shippingMethod1 : shippingMethods1) {
			boolean contains = false;

			for (ShippingMethod shippingMethod2 : shippingMethods2) {
				if (equals(shippingMethod1, shippingMethod2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				shippingMethods2 + " does not contain " + shippingMethod1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<ShippingMethod> shippingMethods, JSONArray jsonArray) {

		for (ShippingMethod shippingMethod : shippingMethods) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(shippingMethod, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + shippingMethod, contains);
		}
	}

	protected void assertValid(ShippingMethod shippingMethod) {
		boolean valid = true;

		if (shippingMethod.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (shippingMethod.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (shippingMethod.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("shippingOptions", additionalAssertFieldName)) {
				if (shippingMethod.getShippingOptions() == null) {
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

	protected void assertValid(Page<ShippingMethod> page) {
		boolean valid = false;

		java.util.Collection<ShippingMethod> shippingMethods = page.getItems();

		int size = shippingMethods.size();

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
		ShippingMethod shippingMethod1, ShippingMethod shippingMethod2) {

		if (shippingMethod1 == shippingMethod2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingMethod1.getDescription(),
						shippingMethod2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingMethod1.getId(), shippingMethod2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingMethod1.getName(), shippingMethod2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("shippingOptions", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingMethod1.getShippingOptions(),
						shippingMethod2.getShippingOptions())) {

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
		ShippingMethod shippingMethod, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("description", fieldName)) {
				if (!Objects.deepEquals(
						shippingMethod.getDescription(),
						jsonObject.getString("description"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						shippingMethod.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						shippingMethod.getName(),
						jsonObject.getString("name"))) {

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

		if (!(_shippingMethodResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_shippingMethodResource;

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
		ShippingMethod shippingMethod) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(shippingMethod.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(shippingMethod.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("shippingOptions")) {
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

	protected ShippingMethod randomShippingMethod() throws Exception {
		return new ShippingMethod() {
			{
				description = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				name = RandomTestUtil.randomString();
			}
		};
	}

	protected ShippingMethod randomIrrelevantShippingMethod() throws Exception {
		ShippingMethod randomIrrelevantShippingMethod = randomShippingMethod();

		return randomIrrelevantShippingMethod;
	}

	protected ShippingMethod randomPatchShippingMethod() throws Exception {
		return randomShippingMethod();
	}

	protected ShippingMethodResource shippingMethodResource;
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
		BaseShippingMethodResourceTestCase.class);

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
	private com.liferay.headless.commerce.delivery.cart.resource.v1_0.
		ShippingMethodResource _shippingMethodResource;

}