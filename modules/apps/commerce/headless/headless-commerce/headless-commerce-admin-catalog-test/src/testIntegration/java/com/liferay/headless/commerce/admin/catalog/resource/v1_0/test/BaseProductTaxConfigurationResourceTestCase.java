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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductTaxConfiguration;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductTaxConfigurationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductTaxConfigurationSerDes;
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
public abstract class BaseProductTaxConfigurationResourceTestCase {

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

		_productTaxConfigurationResource.setContextCompany(testCompany);

		ProductTaxConfigurationResource.Builder builder =
			ProductTaxConfigurationResource.builder();

		productTaxConfigurationResource = builder.locale(
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

		ProductTaxConfiguration productTaxConfiguration1 =
			randomProductTaxConfiguration();

		String json = objectMapper.writeValueAsString(productTaxConfiguration1);

		ProductTaxConfiguration productTaxConfiguration2 =
			ProductTaxConfigurationSerDes.toDTO(json);

		Assert.assertTrue(
			equals(productTaxConfiguration1, productTaxConfiguration2));
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

		ProductTaxConfiguration productTaxConfiguration =
			randomProductTaxConfiguration();

		String json1 = objectMapper.writeValueAsString(productTaxConfiguration);
		String json2 = ProductTaxConfigurationSerDes.toJSON(
			productTaxConfiguration);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductTaxConfiguration productTaxConfiguration =
			randomProductTaxConfiguration();

		productTaxConfiguration.setTaxCategory(regex);

		String json = ProductTaxConfigurationSerDes.toJSON(
			productTaxConfiguration);

		Assert.assertFalse(json.contains(regex));

		productTaxConfiguration = ProductTaxConfigurationSerDes.toDTO(json);

		Assert.assertEquals(regex, productTaxConfiguration.getTaxCategory());
	}

	@Test
	public void testGetProductByExternalReferenceCodeTaxConfiguration()
		throws Exception {

		ProductTaxConfiguration postProductTaxConfiguration =
			testGetProductByExternalReferenceCodeTaxConfiguration_addProductTaxConfiguration();

		ProductTaxConfiguration getProductTaxConfiguration =
			productTaxConfigurationResource.
				getProductByExternalReferenceCodeTaxConfiguration(null);

		assertEquals(postProductTaxConfiguration, getProductTaxConfiguration);
		assertValid(getProductTaxConfiguration);
	}

	protected ProductTaxConfiguration
			testGetProductByExternalReferenceCodeTaxConfiguration_addProductTaxConfiguration()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductByExternalReferenceCodeTaxConfiguration()
		throws Exception {

		ProductTaxConfiguration productTaxConfiguration =
			testGraphQLProductTaxConfiguration_addProductTaxConfiguration();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"productByExternalReferenceCodeTaxConfiguration",
				new HashMap<String, Object>() {
					{
						put("externalReferenceCode", null);
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				productTaxConfiguration,
				dataJSONObject.getJSONObject(
					"productByExternalReferenceCodeTaxConfiguration")));
	}

	@Test
	public void testPatchProductByExternalReferenceCodeTaxConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetProductIdTaxConfiguration() throws Exception {
		ProductTaxConfiguration postProductTaxConfiguration =
			testGetProductIdTaxConfiguration_addProductTaxConfiguration();

		ProductTaxConfiguration getProductTaxConfiguration =
			productTaxConfigurationResource.getProductIdTaxConfiguration(
				postProductTaxConfiguration.getId());

		assertEquals(postProductTaxConfiguration, getProductTaxConfiguration);
		assertValid(getProductTaxConfiguration);
	}

	protected ProductTaxConfiguration
			testGetProductIdTaxConfiguration_addProductTaxConfiguration()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetProductIdTaxConfiguration() throws Exception {
		ProductTaxConfiguration productTaxConfiguration =
			testGraphQLProductTaxConfiguration_addProductTaxConfiguration();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"productIdTaxConfiguration",
				new HashMap<String, Object>() {
					{
						put("id", productTaxConfiguration.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				productTaxConfiguration,
				dataJSONObject.getJSONObject("productIdTaxConfiguration")));
	}

	@Test
	public void testPatchProductIdTaxConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	protected ProductTaxConfiguration
			testGraphQLProductTaxConfiguration_addProductTaxConfiguration()
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
		ProductTaxConfiguration productTaxConfiguration1,
		ProductTaxConfiguration productTaxConfiguration2) {

		Assert.assertTrue(
			productTaxConfiguration1 + " does not equal " +
				productTaxConfiguration2,
			equals(productTaxConfiguration1, productTaxConfiguration2));
	}

	protected void assertEquals(
		List<ProductTaxConfiguration> productTaxConfigurations1,
		List<ProductTaxConfiguration> productTaxConfigurations2) {

		Assert.assertEquals(
			productTaxConfigurations1.size(), productTaxConfigurations2.size());

		for (int i = 0; i < productTaxConfigurations1.size(); i++) {
			ProductTaxConfiguration productTaxConfiguration1 =
				productTaxConfigurations1.get(i);
			ProductTaxConfiguration productTaxConfiguration2 =
				productTaxConfigurations2.get(i);

			assertEquals(productTaxConfiguration1, productTaxConfiguration2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductTaxConfiguration> productTaxConfigurations1,
		List<ProductTaxConfiguration> productTaxConfigurations2) {

		Assert.assertEquals(
			productTaxConfigurations1.size(), productTaxConfigurations2.size());

		for (ProductTaxConfiguration productTaxConfiguration1 :
				productTaxConfigurations1) {

			boolean contains = false;

			for (ProductTaxConfiguration productTaxConfiguration2 :
					productTaxConfigurations2) {

				if (equals(
						productTaxConfiguration1, productTaxConfiguration2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productTaxConfigurations2 + " does not contain " +
					productTaxConfiguration1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<ProductTaxConfiguration> productTaxConfigurations,
		JSONArray jsonArray) {

		for (ProductTaxConfiguration productTaxConfiguration :
				productTaxConfigurations) {

			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(
						productTaxConfiguration, (JSONObject)object)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + productTaxConfiguration,
				contains);
		}
	}

	protected void assertValid(
		ProductTaxConfiguration productTaxConfiguration) {

		boolean valid = true;

		if (productTaxConfiguration.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("taxCategory", additionalAssertFieldName)) {
				if (productTaxConfiguration.getTaxCategory() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("taxable", additionalAssertFieldName)) {
				if (productTaxConfiguration.getTaxable() == null) {
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

	protected void assertValid(Page<ProductTaxConfiguration> page) {
		boolean valid = false;

		java.util.Collection<ProductTaxConfiguration> productTaxConfigurations =
			page.getItems();

		int size = productTaxConfigurations.size();

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
		ProductTaxConfiguration productTaxConfiguration1,
		ProductTaxConfiguration productTaxConfiguration2) {

		if (productTaxConfiguration1 == productTaxConfiguration2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration1.getId(),
						productTaxConfiguration2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("taxCategory", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration1.getTaxCategory(),
						productTaxConfiguration2.getTaxCategory())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("taxable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration1.getTaxable(),
						productTaxConfiguration2.getTaxable())) {

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
		ProductTaxConfiguration productTaxConfiguration,
		JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration.getId(),
						jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("taxCategory", fieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration.getTaxCategory(),
						jsonObject.getString("taxCategory"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("taxable", fieldName)) {
				if (!Objects.deepEquals(
						productTaxConfiguration.getTaxable(),
						jsonObject.getBoolean("taxable"))) {

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

		if (!(_productTaxConfigurationResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productTaxConfigurationResource;

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
		ProductTaxConfiguration productTaxConfiguration) {

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

		if (entityFieldName.equals("taxCategory")) {
			sb.append("'");
			sb.append(String.valueOf(productTaxConfiguration.getTaxCategory()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("taxable")) {
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

	protected ProductTaxConfiguration randomProductTaxConfiguration()
		throws Exception {

		return new ProductTaxConfiguration() {
			{
				id = RandomTestUtil.randomLong();
				taxCategory = RandomTestUtil.randomString();
				taxable = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected ProductTaxConfiguration randomIrrelevantProductTaxConfiguration()
		throws Exception {

		ProductTaxConfiguration randomIrrelevantProductTaxConfiguration =
			randomProductTaxConfiguration();

		return randomIrrelevantProductTaxConfiguration;
	}

	protected ProductTaxConfiguration randomPatchProductTaxConfiguration()
		throws Exception {

		return randomProductTaxConfiguration();
	}

	protected ProductTaxConfigurationResource productTaxConfigurationResource;
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
		BaseProductTaxConfigurationResourceTestCase.class);

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
		ProductTaxConfigurationResource _productTaxConfigurationResource;

}