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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.PaymentMethod;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.PaymentMethodResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.PaymentMethodSerDes;
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
public abstract class BasePaymentMethodResourceTestCase {

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

		_paymentMethodResource.setContextCompany(testCompany);

		PaymentMethodResource.Builder builder = PaymentMethodResource.builder();

		paymentMethodResource = builder.locale(
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

		PaymentMethod paymentMethod1 = randomPaymentMethod();

		String json = objectMapper.writeValueAsString(paymentMethod1);

		PaymentMethod paymentMethod2 = PaymentMethodSerDes.toDTO(json);

		Assert.assertTrue(equals(paymentMethod1, paymentMethod2));
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

		PaymentMethod paymentMethod = randomPaymentMethod();

		String json1 = objectMapper.writeValueAsString(paymentMethod);
		String json2 = PaymentMethodSerDes.toJSON(paymentMethod);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		PaymentMethod paymentMethod = randomPaymentMethod();

		paymentMethod.setDescription(regex);
		paymentMethod.setKey(regex);
		paymentMethod.setName(regex);

		String json = PaymentMethodSerDes.toJSON(paymentMethod);

		Assert.assertFalse(json.contains(regex));

		paymentMethod = PaymentMethodSerDes.toDTO(json);

		Assert.assertEquals(regex, paymentMethod.getDescription());
		Assert.assertEquals(regex, paymentMethod.getKey());
		Assert.assertEquals(regex, paymentMethod.getName());
	}

	@Test
	public void testGetChannelCartPaymentMethodsPage() throws Exception {
		Page<PaymentMethod> page =
			paymentMethodResource.getChannelCartPaymentMethodsPage(
				testGetChannelCartPaymentMethodsPage_getChannelId(),
				testGetChannelCartPaymentMethodsPage_getCartId());

		Assert.assertEquals(0, page.getTotalCount());

		Long channelId = testGetChannelCartPaymentMethodsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelCartPaymentMethodsPage_getIrrelevantChannelId();
		Long cartId = testGetChannelCartPaymentMethodsPage_getCartId();
		Long irrelevantCartId =
			testGetChannelCartPaymentMethodsPage_getIrrelevantCartId();

		if ((irrelevantChannelId != null) && (irrelevantCartId != null)) {
			PaymentMethod irrelevantPaymentMethod =
				testGetChannelCartPaymentMethodsPage_addPaymentMethod(
					irrelevantChannelId, irrelevantCartId,
					randomIrrelevantPaymentMethod());

			page = paymentMethodResource.getChannelCartPaymentMethodsPage(
				irrelevantChannelId, irrelevantCartId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantPaymentMethod),
				(List<PaymentMethod>)page.getItems());
			assertValid(page);
		}

		PaymentMethod paymentMethod1 =
			testGetChannelCartPaymentMethodsPage_addPaymentMethod(
				channelId, cartId, randomPaymentMethod());

		PaymentMethod paymentMethod2 =
			testGetChannelCartPaymentMethodsPage_addPaymentMethod(
				channelId, cartId, randomPaymentMethod());

		page = paymentMethodResource.getChannelCartPaymentMethodsPage(
			channelId, cartId);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(paymentMethod1, paymentMethod2),
			(List<PaymentMethod>)page.getItems());
		assertValid(page);
	}

	protected PaymentMethod
			testGetChannelCartPaymentMethodsPage_addPaymentMethod(
				Long channelId, Long cartId, PaymentMethod paymentMethod)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartPaymentMethodsPage_getChannelId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartPaymentMethodsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelCartPaymentMethodsPage_getCartId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartPaymentMethodsPage_getIrrelevantCartId()
		throws Exception {

		return null;
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		PaymentMethod paymentMethod1, PaymentMethod paymentMethod2) {

		Assert.assertTrue(
			paymentMethod1 + " does not equal " + paymentMethod2,
			equals(paymentMethod1, paymentMethod2));
	}

	protected void assertEquals(
		List<PaymentMethod> paymentMethods1,
		List<PaymentMethod> paymentMethods2) {

		Assert.assertEquals(paymentMethods1.size(), paymentMethods2.size());

		for (int i = 0; i < paymentMethods1.size(); i++) {
			PaymentMethod paymentMethod1 = paymentMethods1.get(i);
			PaymentMethod paymentMethod2 = paymentMethods2.get(i);

			assertEquals(paymentMethod1, paymentMethod2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<PaymentMethod> paymentMethods1,
		List<PaymentMethod> paymentMethods2) {

		Assert.assertEquals(paymentMethods1.size(), paymentMethods2.size());

		for (PaymentMethod paymentMethod1 : paymentMethods1) {
			boolean contains = false;

			for (PaymentMethod paymentMethod2 : paymentMethods2) {
				if (equals(paymentMethod1, paymentMethod2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				paymentMethods2 + " does not contain " + paymentMethod1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<PaymentMethod> paymentMethods, JSONArray jsonArray) {

		for (PaymentMethod paymentMethod : paymentMethods) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(paymentMethod, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + paymentMethod, contains);
		}
	}

	protected void assertValid(PaymentMethod paymentMethod) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (paymentMethod.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (paymentMethod.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (paymentMethod.getName() == null) {
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

	protected void assertValid(Page<PaymentMethod> page) {
		boolean valid = false;

		java.util.Collection<PaymentMethod> paymentMethods = page.getItems();

		int size = paymentMethods.size();

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
		PaymentMethod paymentMethod1, PaymentMethod paymentMethod2) {

		if (paymentMethod1 == paymentMethod2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethod1.getDescription(),
						paymentMethod2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethod1.getKey(), paymentMethod2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						paymentMethod1.getName(), paymentMethod2.getName())) {

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
		PaymentMethod paymentMethod, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("description", fieldName)) {
				if (!Objects.deepEquals(
						paymentMethod.getDescription(),
						jsonObject.getString("description"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", fieldName)) {
				if (!Objects.deepEquals(
						paymentMethod.getKey(), jsonObject.getString("key"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						paymentMethod.getName(),
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

		if (!(_paymentMethodResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_paymentMethodResource;

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
		EntityField entityField, String operator, PaymentMethod paymentMethod) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(paymentMethod.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(paymentMethod.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(paymentMethod.getName()));
			sb.append("'");

			return sb.toString();
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

	protected PaymentMethod randomPaymentMethod() throws Exception {
		return new PaymentMethod() {
			{
				description = RandomTestUtil.randomString();
				key = RandomTestUtil.randomString();
				name = RandomTestUtil.randomString();
			}
		};
	}

	protected PaymentMethod randomIrrelevantPaymentMethod() throws Exception {
		PaymentMethod randomIrrelevantPaymentMethod = randomPaymentMethod();

		return randomIrrelevantPaymentMethod;
	}

	protected PaymentMethod randomPatchPaymentMethod() throws Exception {
		return randomPaymentMethod();
	}

	protected PaymentMethodResource paymentMethodResource;
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
		BasePaymentMethodResourceTestCase.class);

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
		PaymentMethodResource _paymentMethodResource;

}