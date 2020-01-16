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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.OrderItemResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.OrderItemSerDes;
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
public abstract class BaseOrderItemResourceTestCase {

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

		_orderItemResource.setContextCompany(testCompany);

		OrderItemResource.Builder builder = OrderItemResource.builder();

		orderItemResource = builder.locale(
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

		OrderItem orderItem1 = randomOrderItem();

		String json = objectMapper.writeValueAsString(orderItem1);

		OrderItem orderItem2 = OrderItemSerDes.toDTO(json);

		Assert.assertTrue(equals(orderItem1, orderItem2));
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

		OrderItem orderItem = randomOrderItem();

		String json1 = objectMapper.writeValueAsString(orderItem);
		String json2 = OrderItemSerDes.toJSON(orderItem);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		OrderItem orderItem = randomOrderItem();

		orderItem.setName(regex);

		String json = OrderItemSerDes.toJSON(orderItem);

		Assert.assertFalse(json.contains(regex));

		orderItem = OrderItemSerDes.toDTO(json);

		Assert.assertEquals(regex, orderItem.getName());
	}

	@Test
	public void testGetChannelCartOrderItemsPage() throws Exception {
		Page<OrderItem> page = orderItemResource.getChannelCartOrderItemsPage(
			testGetChannelCartOrderItemsPage_getCartId(),
			testGetChannelCartOrderItemsPage_getChannelId());

		Assert.assertEquals(0, page.getTotalCount());

		Long cartId = testGetChannelCartOrderItemsPage_getCartId();
		Long irrelevantCartId =
			testGetChannelCartOrderItemsPage_getIrrelevantCartId();
		Long channelId = testGetChannelCartOrderItemsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelCartOrderItemsPage_getIrrelevantChannelId();

		if ((irrelevantCartId != null) && (irrelevantChannelId != null)) {
			OrderItem irrelevantOrderItem =
				testGetChannelCartOrderItemsPage_addOrderItem(
					irrelevantCartId, irrelevantChannelId,
					randomIrrelevantOrderItem());

			page = orderItemResource.getChannelCartOrderItemsPage(
				irrelevantCartId, irrelevantChannelId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantOrderItem),
				(List<OrderItem>)page.getItems());
			assertValid(page);
		}

		OrderItem orderItem1 = testGetChannelCartOrderItemsPage_addOrderItem(
			cartId, channelId, randomOrderItem());

		OrderItem orderItem2 = testGetChannelCartOrderItemsPage_addOrderItem(
			cartId, channelId, randomOrderItem());

		page = orderItemResource.getChannelCartOrderItemsPage(
			cartId, channelId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(orderItem1, orderItem2),
			(List<OrderItem>)page.getItems());
		assertValid(page);
	}

	protected OrderItem testGetChannelCartOrderItemsPage_addOrderItem(
			Long cartId, Long channelId, OrderItem orderItem)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartOrderItemsPage_getCartId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartOrderItemsPage_getIrrelevantCartId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelCartOrderItemsPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartOrderItemsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	@Test
	public void testGetChannelCartOrderItem() throws Exception {
		OrderItem postOrderItem = testGetChannelCartOrderItem_addOrderItem();

		OrderItem getOrderItem = orderItemResource.getChannelCartOrderItem(
			postOrderItem.getCartId(), postOrderItem.getChannelId(),
			postOrderItem.getId());

		assertEquals(postOrderItem, getOrderItem);
		assertValid(getOrderItem);
	}

	protected OrderItem testGetChannelCartOrderItem_addOrderItem()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetChannelCartOrderItem() throws Exception {
		OrderItem orderItem = testGraphQLOrderItem_addOrderItem();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"channelCartOrderItem",
				new HashMap<String, Object>() {
					{
						put("cartId", orderItem.getCartId());
						put("channelId", orderItem.getChannelId());
						put("orderItemId", orderItem.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				orderItem,
				dataJSONObject.getJSONObject("channelCartOrderItem")));
	}

	protected OrderItem testGraphQLOrderItem_addOrderItem() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(OrderItem orderItem1, OrderItem orderItem2) {
		Assert.assertTrue(
			orderItem1 + " does not equal " + orderItem2,
			equals(orderItem1, orderItem2));
	}

	protected void assertEquals(
		List<OrderItem> orderItems1, List<OrderItem> orderItems2) {

		Assert.assertEquals(orderItems1.size(), orderItems2.size());

		for (int i = 0; i < orderItems1.size(); i++) {
			OrderItem orderItem1 = orderItems1.get(i);
			OrderItem orderItem2 = orderItems2.get(i);

			assertEquals(orderItem1, orderItem2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<OrderItem> orderItems1, List<OrderItem> orderItems2) {

		Assert.assertEquals(orderItems1.size(), orderItems2.size());

		for (OrderItem orderItem1 : orderItems1) {
			boolean contains = false;

			for (OrderItem orderItem2 : orderItems2) {
				if (equals(orderItem1, orderItem2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				orderItems2 + " does not contain " + orderItem1, contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<OrderItem> orderItems, JSONArray jsonArray) {

		for (OrderItem orderItem : orderItems) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(orderItem, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + orderItem, contains);
		}
	}

	protected void assertValid(OrderItem orderItem) {
		boolean valid = true;

		if (orderItem.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (orderItem.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (orderItem.getQuantity() == null) {
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

	protected void assertValid(Page<OrderItem> page) {
		boolean valid = false;

		java.util.Collection<OrderItem> orderItems = page.getItems();

		int size = orderItems.size();

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

	protected boolean equals(OrderItem orderItem1, OrderItem orderItem2) {
		if (orderItem1 == orderItem2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderItem1.getId(), orderItem2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderItem1.getName(), orderItem2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						orderItem1.getQuantity(), orderItem2.getQuantity())) {

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
		OrderItem orderItem, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						orderItem.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						orderItem.getName(), jsonObject.getString("name"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("quantity", fieldName)) {
				if (!Objects.deepEquals(
						orderItem.getQuantity(),
						jsonObject.getInt("quantity"))) {

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

		if (!(_orderItemResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_orderItemResource;

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
		EntityField entityField, String operator, OrderItem orderItem) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(orderItem.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("quantity")) {
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

	protected OrderItem randomOrderItem() throws Exception {
		return new OrderItem() {
			{
				id = RandomTestUtil.randomLong();
				name = RandomTestUtil.randomString();
				quantity = RandomTestUtil.randomInt();
			}
		};
	}

	protected OrderItem randomIrrelevantOrderItem() throws Exception {
		OrderItem randomIrrelevantOrderItem = randomOrderItem();

		return randomIrrelevantOrderItem;
	}

	protected OrderItem randomPatchOrderItem() throws Exception {
		return randomOrderItem();
	}

	protected OrderItemResource orderItemResource;
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
		BaseOrderItemResourceTestCase.class);

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
	private
		com.liferay.headless.commerce.delivery.cart.resource.v1_0.
			OrderItemResource _orderItemResource;

}