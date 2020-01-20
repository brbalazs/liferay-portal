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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.CartResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.CartSerDes;
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
import org.apache.commons.lang.time.DateUtils;

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
public abstract class BaseCartResourceTestCase {

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

		_cartResource.setContextCompany(testCompany);

		CartResource.Builder builder = CartResource.builder();

		cartResource = builder.locale(
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

		Cart cart1 = randomCart();

		String json = objectMapper.writeValueAsString(cart1);

		Cart cart2 = CartSerDes.toDTO(json);

		Assert.assertTrue(equals(cart1, cart2));
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

		Cart cart = randomCart();

		String json1 = objectMapper.writeValueAsString(cart);
		String json2 = CartSerDes.toJSON(cart);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Cart cart = randomCart();

		cart.setAccount(regex);
		cart.setAuthor(regex);
		cart.setStatus(regex);

		String json = CartSerDes.toJSON(cart);

		Assert.assertFalse(json.contains(regex));

		cart = CartSerDes.toDTO(json);

		Assert.assertEquals(regex, cart.getAccount());
		Assert.assertEquals(regex, cart.getAuthor());
		Assert.assertEquals(regex, cart.getStatus());
	}

	@Test
	public void testPostChannelCartCartItem() throws Exception {
		Cart randomCart = randomCart();

		Cart postCart = testPostChannelCartCartItem_addCart(randomCart);

		assertEquals(randomCart, postCart);
		assertValid(postCart);
	}

	protected Cart testPostChannelCartCartItem_addCart(Cart cart)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetChannelCart() throws Exception {
		Cart postCart = testGetChannelCart_addCart();

		Cart getCart = cartResource.getChannelCart(
			postCart.getId(), postCart.getChannelId());

		assertEquals(postCart, getCart);
		assertValid(getCart);
	}

	protected Cart testGetChannelCart_addCart() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetChannelCart() throws Exception {
		Cart cart = testGraphQLCart_addCart();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"channelCart",
				new HashMap<String, Object>() {
					{
						put("cartId", cart.getId());
						put("channelId", cart.getChannelId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				cart, dataJSONObject.getJSONObject("channelCart")));
	}

	@Test
	public void testPutChannelCart() throws Exception {
		Cart postCart = testPutChannelCart_addCart();

		Cart randomCart = randomCart();

		Cart putCart = cartResource.putCart(postCart.getId(), randomCart);

		assertEquals(randomCart, putCart);
		assertValid(putCart);

		Cart getCart = cartResource.getCart(putCart.getId());

		assertEquals(randomCart, getCart);
		assertValid(getCart);
	}

	protected Cart testPutChannelCart_addCart() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetChannelCartsPage() throws Exception {
		Page<Cart> page = cartResource.getChannelCartsPage(
			testGetChannelCartsPage_getChannelId());

		Assert.assertEquals(0, page.getTotalCount());

		Long channelId = testGetChannelCartsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelCartsPage_getIrrelevantChannelId();

		if ((irrelevantChannelId != null)) {
			Cart irrelevantCart = testGetChannelCartsPage_addCart(
				irrelevantChannelId, randomIrrelevantCart());

			page = cartResource.getChannelCartsPage(irrelevantChannelId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantCart), (List<Cart>)page.getItems());
			assertValid(page);
		}

		Cart cart1 = testGetChannelCartsPage_addCart(channelId, randomCart());

		Cart cart2 = testGetChannelCartsPage_addCart(channelId, randomCart());

		page = cartResource.getChannelCartsPage(channelId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(cart1, cart2), (List<Cart>)page.getItems());
		assertValid(page);
	}

	protected Cart testGetChannelCartsPage_addCart(Long channelId, Cart cart)
		throws Exception {

		return cartResource.postChannelCart(channelId, cart);
	}

	protected Long testGetChannelCartsPage_getChannelId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostChannelCart() throws Exception {
		Cart randomCart = randomCart();

		Cart postCart = testPostChannelCart_addCart(randomCart);

		assertEquals(randomCart, postCart);
		assertValid(postCart);
	}

	protected Cart testPostChannelCart_addCart(Cart cart) throws Exception {
		return cartResource.postChannelCart(
			testGetChannelCartsPage_getChannelId(), cart);
	}

	protected Cart testGraphQLCart_addCart() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Cart cart1, Cart cart2) {
		Assert.assertTrue(
			cart1 + " does not equal " + cart2, equals(cart1, cart2));
	}

	protected void assertEquals(List<Cart> carts1, List<Cart> carts2) {
		Assert.assertEquals(carts1.size(), carts2.size());

		for (int i = 0; i < carts1.size(); i++) {
			Cart cart1 = carts1.get(i);
			Cart cart2 = carts2.get(i);

			assertEquals(cart1, cart2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Cart> carts1, List<Cart> carts2) {

		Assert.assertEquals(carts1.size(), carts2.size());

		for (Cart cart1 : carts1) {
			boolean contains = false;

			for (Cart cart2 : carts2) {
				if (equals(cart1, cart2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(carts2 + " does not contain " + cart1, contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<Cart> carts, JSONArray jsonArray) {

		for (Cart cart : carts) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(cart, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + cart, contains);
		}
	}

	protected void assertValid(Cart cart) {
		boolean valid = true;

		if (cart.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("account", additionalAssertFieldName)) {
				if (cart.getAccount() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (cart.getAccountId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (cart.getAuthor() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("cartItems", additionalAssertFieldName)) {
				if (cart.getCartItems() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("createDate", additionalAssertFieldName)) {
				if (cart.getCreateDate() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (cart.getStatus() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("summary", additionalAssertFieldName)) {
				if (cart.getSummary() == null) {
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

	protected void assertValid(Page<Cart> page) {
		boolean valid = false;

		java.util.Collection<Cart> carts = page.getItems();

		int size = carts.size();

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

	protected boolean equals(Cart cart1, Cart cart2) {
		if (cart1 == cart2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("account", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cart1.getAccount(), cart2.getAccount())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cart1.getAccountId(), cart2.getAccountId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (!Objects.deepEquals(cart1.getAuthor(), cart2.getAuthor())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("cartItems", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cart1.getCartItems(), cart2.getCartItems())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("createDate", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cart1.getCreateDate(), cart2.getCreateDate())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(cart1.getId(), cart2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("status", additionalAssertFieldName)) {
				if (!Objects.deepEquals(cart1.getStatus(), cart2.getStatus())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("summary", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cart1.getSummary(), cart2.getSummary())) {

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

	protected boolean equalsJSONObject(Cart cart, JSONObject jsonObject) {
		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("account", fieldName)) {
				if (!Objects.deepEquals(
						cart.getAccount(), jsonObject.getString("account"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("accountId", fieldName)) {
				if (!Objects.deepEquals(
						cart.getAccountId(), jsonObject.getLong("accountId"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("author", fieldName)) {
				if (!Objects.deepEquals(
						cart.getAuthor(), jsonObject.getString("author"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						cart.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("status", fieldName)) {
				if (!Objects.deepEquals(
						cart.getStatus(), jsonObject.getString("status"))) {

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

		if (!(_cartResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_cartResource;

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
		EntityField entityField, String operator, Cart cart) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("account")) {
			sb.append("'");
			sb.append(String.valueOf(cart.getAccount()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("accountId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("author")) {
			sb.append("'");
			sb.append(String.valueOf(cart.getAuthor()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("cartItems")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("createDate")) {
			if (operator.equals("between")) {
				sb = new StringBundler();

				sb.append("(");
				sb.append(entityFieldName);
				sb.append(" gt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(cart.getCreateDate(), -2)));
				sb.append(" and ");
				sb.append(entityFieldName);
				sb.append(" lt ");
				sb.append(
					_dateFormat.format(
						DateUtils.addSeconds(cart.getCreateDate(), 2)));
				sb.append(")");
			}
			else {
				sb.append(entityFieldName);

				sb.append(" ");
				sb.append(operator);
				sb.append(" ");

				sb.append(_dateFormat.format(cart.getCreateDate()));
			}

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("status")) {
			sb.append("'");
			sb.append(String.valueOf(cart.getStatus()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("summary")) {
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

	protected Cart randomCart() throws Exception {
		return new Cart() {
			{
				account = RandomTestUtil.randomString();
				accountId = RandomTestUtil.randomLong();
				author = RandomTestUtil.randomString();
				createDate = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				status = RandomTestUtil.randomString();
			}
		};
	}

	protected Cart randomIrrelevantCart() throws Exception {
		Cart randomIrrelevantCart = randomCart();

		return randomIrrelevantCart;
	}

	protected Cart randomPatchCart() throws Exception {
		return randomCart();
	}

	protected CartResource cartResource;
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
		BaseCartResourceTestCase.class);

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
		com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource
			_cartResource;

}