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

package com.liferay.headless.commerce.delivery.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Sku;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.SkuResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.SkuSerDes;
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
public abstract class BaseSkuResourceTestCase {

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

		_skuResource.setContextCompany(testCompany);

		SkuResource.Builder builder = SkuResource.builder();

		skuResource = builder.locale(
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

		Sku sku1 = randomSku();

		String json = objectMapper.writeValueAsString(sku1);

		Sku sku2 = SkuSerDes.toDTO(json);

		Assert.assertTrue(equals(sku1, sku2));
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

		Sku sku = randomSku();

		String json1 = objectMapper.writeValueAsString(sku);
		String json2 = SkuSerDes.toJSON(sku);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Sku sku = randomSku();

		sku.setSku(regex);

		String json = SkuSerDes.toJSON(sku);

		Assert.assertFalse(json.contains(regex));

		sku = SkuSerDes.toDTO(json);

		Assert.assertEquals(regex, sku.getSku());
	}

	@Test
	public void testGetProductIdSkusPage() throws Exception {
		Page<Sku> page = skuResource.getProductIdSkusPage(
			testGetProductIdSkusPage_getId());

		Assert.assertEquals(0, page.getTotalCount());

		Long id = testGetProductIdSkusPage_getId();
		Long irrelevantId = testGetProductIdSkusPage_getIrrelevantId();

		if ((irrelevantId != null)) {
			Sku irrelevantSku = testGetProductIdSkusPage_addSku(
				irrelevantId, randomIrrelevantSku());

			page = skuResource.getProductIdSkusPage(irrelevantId);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantSku), (List<Sku>)page.getItems());
			assertValid(page);
		}

		Sku sku1 = testGetProductIdSkusPage_addSku(id, randomSku());

		Sku sku2 = testGetProductIdSkusPage_addSku(id, randomSku());

		page = skuResource.getProductIdSkusPage(id);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(sku1, sku2), (List<Sku>)page.getItems());
		assertValid(page);
	}

	protected Sku testGetProductIdSkusPage_addSku(Long id, Sku sku)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdSkusPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdSkusPage_getIrrelevantId() throws Exception {
		return null;
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Sku sku1, Sku sku2) {
		Assert.assertTrue(sku1 + " does not equal " + sku2, equals(sku1, sku2));
	}

	protected void assertEquals(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (int i = 0; i < skus1.size(); i++) {
			Sku sku1 = skus1.get(i);
			Sku sku2 = skus2.get(i);

			assertEquals(sku1, sku2);
		}
	}

	protected void assertEqualsIgnoringOrder(List<Sku> skus1, List<Sku> skus2) {
		Assert.assertEquals(skus1.size(), skus2.size());

		for (Sku sku1 : skus1) {
			boolean contains = false;

			for (Sku sku2 : skus2) {
				if (equals(sku1, sku2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(skus2 + " does not contain " + sku1, contains);
		}
	}

	protected void assertEqualsJSONArray(List<Sku> skus, JSONArray jsonArray) {
		for (Sku sku : skus) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(sku, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(jsonArray + " does not contain " + sku, contains);
		}
	}

	protected void assertValid(Sku sku) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (sku.getAllowedOrderQuantities() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("availability", additionalAssertFieldName)) {
				if (sku.getAvailability() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (sku.getMaxOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (sku.getMinOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (sku.getPrice() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (sku.getSku() == null) {
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

	protected void assertValid(Page<Sku> page) {
		boolean valid = false;

		java.util.Collection<Sku> skus = page.getItems();

		int size = skus.size();

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

	protected boolean equals(Sku sku1, Sku sku2) {
		if (sku1 == sku2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						sku1.getAllowedOrderQuantities(),
						sku2.getAllowedOrderQuantities())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("availability", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getAvailability(), sku2.getAvailability())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getMaxOrderQuantity(),
						sku2.getMaxOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						sku1.getMinOrderQuantity(),
						sku2.getMinOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("price", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getPrice(), sku2.getPrice())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("sku", additionalAssertFieldName)) {
				if (!Objects.deepEquals(sku1.getSku(), sku2.getSku())) {
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

	protected boolean equalsJSONObject(Sku sku, JSONObject jsonObject) {
		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("maxOrderQuantity", fieldName)) {
				if (!Objects.deepEquals(
						sku.getMaxOrderQuantity(),
						jsonObject.getInt("maxOrderQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", fieldName)) {
				if (!Objects.deepEquals(
						sku.getMinOrderQuantity(),
						jsonObject.getInt("minOrderQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sku", fieldName)) {
				if (!Objects.deepEquals(
						sku.getSku(), jsonObject.getString("sku"))) {

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

		if (!(_skuResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_skuResource;

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
		EntityField entityField, String operator, Sku sku) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("allowedOrderQuantities")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("availability")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("maxOrderQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("minOrderQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("price")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("sku")) {
			sb.append("'");
			sb.append(String.valueOf(sku.getSku()));
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

	protected Sku randomSku() throws Exception {
		return new Sku() {
			{
				maxOrderQuantity = RandomTestUtil.randomInt();
				minOrderQuantity = RandomTestUtil.randomInt();
				sku = RandomTestUtil.randomString();
			}
		};
	}

	protected Sku randomIrrelevantSku() throws Exception {
		Sku randomIrrelevantSku = randomSku();

		return randomIrrelevantSku;
	}

	protected Sku randomPatchSku() throws Exception {
		return randomSku();
	}

	protected SkuResource skuResource;
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
		BaseSkuResourceTestCase.class);

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
		com.liferay.headless.commerce.delivery.catalog.resource.v1_0.SkuResource
			_skuResource;

}