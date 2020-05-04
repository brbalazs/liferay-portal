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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductConfiguration;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductConfigurationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductConfigurationSerDes;
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
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseProductConfigurationResourceTestCase {

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

		_productConfigurationResource.setContextCompany(testCompany);

		ProductConfigurationResource.Builder builder =
			ProductConfigurationResource.builder();

		productConfigurationResource = builder.locale(
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

		ProductConfiguration productConfiguration1 =
			randomProductConfiguration();

		String json = objectMapper.writeValueAsString(productConfiguration1);

		ProductConfiguration productConfiguration2 =
			ProductConfigurationSerDes.toDTO(json);

		Assert.assertTrue(equals(productConfiguration1, productConfiguration2));
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

		ProductConfiguration productConfiguration =
			randomProductConfiguration();

		String json1 = objectMapper.writeValueAsString(productConfiguration);
		String json2 = ProductConfigurationSerDes.toJSON(productConfiguration);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductConfiguration productConfiguration =
			randomProductConfiguration();

		productConfiguration.setInventoryEngine(regex);
		productConfiguration.setLowStockAction(regex);

		String json = ProductConfigurationSerDes.toJSON(productConfiguration);

		Assert.assertFalse(json.contains(regex));

		productConfiguration = ProductConfigurationSerDes.toDTO(json);

		Assert.assertEquals(regex, productConfiguration.getInventoryEngine());
		Assert.assertEquals(regex, productConfiguration.getLowStockAction());
	}

	@Test
	public void testGetProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductByExternalReferenceCodeConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetProductIdConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetProductIdConfiguration() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testPatchProductIdConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductConfiguration productConfiguration1,
		ProductConfiguration productConfiguration2) {

		Assert.assertTrue(
			productConfiguration1 + " does not equal " + productConfiguration2,
			equals(productConfiguration1, productConfiguration2));
	}

	protected void assertEquals(
		List<ProductConfiguration> productConfigurations1,
		List<ProductConfiguration> productConfigurations2) {

		Assert.assertEquals(
			productConfigurations1.size(), productConfigurations2.size());

		for (int i = 0; i < productConfigurations1.size(); i++) {
			ProductConfiguration productConfiguration1 =
				productConfigurations1.get(i);
			ProductConfiguration productConfiguration2 =
				productConfigurations2.get(i);

			assertEquals(productConfiguration1, productConfiguration2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductConfiguration> productConfigurations1,
		List<ProductConfiguration> productConfigurations2) {

		Assert.assertEquals(
			productConfigurations1.size(), productConfigurations2.size());

		for (ProductConfiguration productConfiguration1 :
				productConfigurations1) {

			boolean contains = false;

			for (ProductConfiguration productConfiguration2 :
					productConfigurations2) {

				if (equals(productConfiguration1, productConfiguration2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productConfigurations2 + " does not contain " +
					productConfiguration1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<ProductConfiguration> productConfigurations, JSONArray jsonArray) {

		for (ProductConfiguration productConfiguration :
				productConfigurations) {

			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(
						productConfiguration, (JSONObject)object)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + productConfiguration,
				contains);
		}
	}

	protected void assertValid(ProductConfiguration productConfiguration) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("allowBackOrder", additionalAssertFieldName)) {
				if (productConfiguration.getAllowBackOrder() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (productConfiguration.getAllowedOrderQuantities() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"displayAvailability", additionalAssertFieldName)) {

				if (productConfiguration.getDisplayAvailability() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"displayStockQuantity", additionalAssertFieldName)) {

				if (productConfiguration.getDisplayStockQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("inventoryEngine", additionalAssertFieldName)) {
				if (productConfiguration.getInventoryEngine() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("lowStockAction", additionalAssertFieldName)) {
				if (productConfiguration.getLowStockAction() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMaxOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMinOrderQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("minStockQuantity", additionalAssertFieldName)) {
				if (productConfiguration.getMinStockQuantity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"multipleOrderQuantity", additionalAssertFieldName)) {

				if (productConfiguration.getMultipleOrderQuantity() == null) {
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

	protected void assertValid(Page<ProductConfiguration> page) {
		boolean valid = false;

		java.util.Collection<ProductConfiguration> productConfigurations =
			page.getItems();

		int size = productConfigurations.size();

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
		ProductConfiguration productConfiguration1,
		ProductConfiguration productConfiguration2) {

		if (productConfiguration1 == productConfiguration2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("allowBackOrder", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getAllowBackOrder(),
						productConfiguration2.getAllowBackOrder())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"allowedOrderQuantities", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getAllowedOrderQuantities(),
						productConfiguration2.getAllowedOrderQuantities())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"displayAvailability", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getDisplayAvailability(),
						productConfiguration2.getDisplayAvailability())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"displayStockQuantity", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getDisplayStockQuantity(),
						productConfiguration2.getDisplayStockQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("inventoryEngine", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getInventoryEngine(),
						productConfiguration2.getInventoryEngine())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("lowStockAction", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getLowStockAction(),
						productConfiguration2.getLowStockAction())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMaxOrderQuantity(),
						productConfiguration2.getMaxOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMinOrderQuantity(),
						productConfiguration2.getMinOrderQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minStockQuantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productConfiguration1.getMinStockQuantity(),
						productConfiguration2.getMinStockQuantity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"multipleOrderQuantity", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						productConfiguration1.getMultipleOrderQuantity(),
						productConfiguration2.getMultipleOrderQuantity())) {

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
		ProductConfiguration productConfiguration, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("allowBackOrder", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getAllowBackOrder(),
						jsonObject.getBoolean("allowBackOrder"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("displayAvailability", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getDisplayAvailability(),
						jsonObject.getBoolean("displayAvailability"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("displayStockQuantity", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getDisplayStockQuantity(),
						jsonObject.getBoolean("displayStockQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("inventoryEngine", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getInventoryEngine(),
						jsonObject.getString("inventoryEngine"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("lowStockAction", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getLowStockAction(),
						jsonObject.getString("lowStockAction"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxOrderQuantity", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getMaxOrderQuantity(),
						jsonObject.getInt("maxOrderQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minOrderQuantity", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getMinOrderQuantity(),
						jsonObject.getInt("minOrderQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("minStockQuantity", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getMinStockQuantity(),
						jsonObject.getInt("minStockQuantity"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("multipleOrderQuantity", fieldName)) {
				if (!Objects.deepEquals(
						productConfiguration.getMultipleOrderQuantity(),
						jsonObject.getInt("multipleOrderQuantity"))) {

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

		if (!(_productConfigurationResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productConfigurationResource;

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
		ProductConfiguration productConfiguration) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("allowBackOrder")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("allowedOrderQuantities")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("displayAvailability")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("displayStockQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("inventoryEngine")) {
			sb.append("'");
			sb.append(
				String.valueOf(productConfiguration.getInventoryEngine()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("lowStockAction")) {
			sb.append("'");
			sb.append(String.valueOf(productConfiguration.getLowStockAction()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("maxOrderQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("minOrderQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("minStockQuantity")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("multipleOrderQuantity")) {
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

	protected ProductConfiguration randomProductConfiguration()
		throws Exception {

		return new ProductConfiguration() {
			{
				allowBackOrder = RandomTestUtil.randomBoolean();
				displayAvailability = RandomTestUtil.randomBoolean();
				displayStockQuantity = RandomTestUtil.randomBoolean();
				inventoryEngine = RandomTestUtil.randomString();
				lowStockAction = RandomTestUtil.randomString();
				maxOrderQuantity = RandomTestUtil.randomInt();
				minOrderQuantity = RandomTestUtil.randomInt();
				minStockQuantity = RandomTestUtil.randomInt();
				multipleOrderQuantity = RandomTestUtil.randomInt();
			}
		};
	}

	protected ProductConfiguration randomIrrelevantProductConfiguration()
		throws Exception {

		ProductConfiguration randomIrrelevantProductConfiguration =
			randomProductConfiguration();

		return randomIrrelevantProductConfiguration;
	}

	protected ProductConfiguration randomPatchProductConfiguration()
		throws Exception {

		return randomProductConfiguration();
	}

	protected ProductConfigurationResource productConfigurationResource;
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
		BaseProductConfigurationResourceTestCase.class);

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
		ProductConfigurationResource _productConfigurationResource;

}