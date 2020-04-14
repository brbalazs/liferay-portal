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

package com.liferay.headless.commerce.admin.order.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.admin.order.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.order.client.pagination.Page;
import com.liferay.headless.commerce.admin.order.client.resource.v1_0.ShippingAddressResource;
import com.liferay.headless.commerce.admin.order.client.serdes.v1_0.ShippingAddressSerDes;
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
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public abstract class BaseShippingAddressResourceTestCase {

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

		_shippingAddressResource.setContextCompany(testCompany);

		ShippingAddressResource.Builder builder =
			ShippingAddressResource.builder();

		shippingAddressResource = builder.locale(
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

		ShippingAddress shippingAddress1 = randomShippingAddress();

		String json = objectMapper.writeValueAsString(shippingAddress1);

		ShippingAddress shippingAddress2 = ShippingAddressSerDes.toDTO(json);

		Assert.assertTrue(equals(shippingAddress1, shippingAddress2));
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

		ShippingAddress shippingAddress = randomShippingAddress();

		String json1 = objectMapper.writeValueAsString(shippingAddress);
		String json2 = ShippingAddressSerDes.toJSON(shippingAddress);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ShippingAddress shippingAddress = randomShippingAddress();

		shippingAddress.setCity(regex);
		shippingAddress.setCountryISOCode(regex);
		shippingAddress.setDescription(regex);
		shippingAddress.setExternalReferenceCodeCopy(regex);
		shippingAddress.setName(regex);
		shippingAddress.setPhoneNumber(regex);
		shippingAddress.setRegionISOCode(regex);
		shippingAddress.setStreet1(regex);
		shippingAddress.setStreet2(regex);
		shippingAddress.setStreet3(regex);
		shippingAddress.setZip(regex);

		String json = ShippingAddressSerDes.toJSON(shippingAddress);

		Assert.assertFalse(json.contains(regex));

		shippingAddress = ShippingAddressSerDes.toDTO(json);

		Assert.assertEquals(regex, shippingAddress.getCity());
		Assert.assertEquals(regex, shippingAddress.getCountryISOCode());
		Assert.assertEquals(regex, shippingAddress.getDescription());
		Assert.assertEquals(
			regex, shippingAddress.getExternalReferenceCodeCopy());
		Assert.assertEquals(regex, shippingAddress.getName());
		Assert.assertEquals(regex, shippingAddress.getPhoneNumber());
		Assert.assertEquals(regex, shippingAddress.getRegionISOCode());
		Assert.assertEquals(regex, shippingAddress.getStreet1());
		Assert.assertEquals(regex, shippingAddress.getStreet2());
		Assert.assertEquals(regex, shippingAddress.getStreet3());
		Assert.assertEquals(regex, shippingAddress.getZip());
	}

	@Test
	public void testGetOrderByExternalReferenceCodeShippingAddress()
		throws Exception {

		ShippingAddress postShippingAddress =
			testGetOrderByExternalReferenceCodeShippingAddress_addShippingAddress();

		ShippingAddress getShippingAddress =
			shippingAddressResource.
				getOrderByExternalReferenceCodeShippingAddress(null);

		assertEquals(postShippingAddress, getShippingAddress);
		assertValid(getShippingAddress);
	}

	protected ShippingAddress
			testGetOrderByExternalReferenceCodeShippingAddress_addShippingAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOrderByExternalReferenceCodeShippingAddress()
		throws Exception {

		ShippingAddress shippingAddress =
			testGraphQLShippingAddress_addShippingAddress();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"orderByExternalReferenceCodeShippingAddress",
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
				shippingAddress,
				dataJSONObject.getJSONObject(
					"orderByExternalReferenceCodeShippingAddress")));
	}

	@Test
	public void testPatchOrderByExternalReferenceCodeShippingAddress()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetOrderIdShippingAddress() throws Exception {
		ShippingAddress postShippingAddress =
			testGetOrderIdShippingAddress_addShippingAddress();

		ShippingAddress getShippingAddress =
			shippingAddressResource.getOrderIdShippingAddress(
				postShippingAddress.getId());

		assertEquals(postShippingAddress, getShippingAddress);
		assertValid(getShippingAddress);
	}

	protected ShippingAddress testGetOrderIdShippingAddress_addShippingAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOrderIdShippingAddress() throws Exception {
		ShippingAddress shippingAddress =
			testGraphQLShippingAddress_addShippingAddress();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"orderIdShippingAddress",
				new HashMap<String, Object>() {
					{
						put("id", shippingAddress.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				shippingAddress,
				dataJSONObject.getJSONObject("orderIdShippingAddress")));
	}

	@Test
	public void testPatchOrderIdShippingAddress() throws Exception {
		Assert.assertTrue(false);
	}

	protected ShippingAddress testGraphQLShippingAddress_addShippingAddress()
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
		ShippingAddress shippingAddress1, ShippingAddress shippingAddress2) {

		Assert.assertTrue(
			shippingAddress1 + " does not equal " + shippingAddress2,
			equals(shippingAddress1, shippingAddress2));
	}

	protected void assertEquals(
		List<ShippingAddress> shippingAddresses1,
		List<ShippingAddress> shippingAddresses2) {

		Assert.assertEquals(
			shippingAddresses1.size(), shippingAddresses2.size());

		for (int i = 0; i < shippingAddresses1.size(); i++) {
			ShippingAddress shippingAddress1 = shippingAddresses1.get(i);
			ShippingAddress shippingAddress2 = shippingAddresses2.get(i);

			assertEquals(shippingAddress1, shippingAddress2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ShippingAddress> shippingAddresses1,
		List<ShippingAddress> shippingAddresses2) {

		Assert.assertEquals(
			shippingAddresses1.size(), shippingAddresses2.size());

		for (ShippingAddress shippingAddress1 : shippingAddresses1) {
			boolean contains = false;

			for (ShippingAddress shippingAddress2 : shippingAddresses2) {
				if (equals(shippingAddress1, shippingAddress2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				shippingAddresses2 + " does not contain " + shippingAddress1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<ShippingAddress> shippingAddresses, JSONArray jsonArray) {

		for (ShippingAddress shippingAddress : shippingAddresses) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(shippingAddress, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + shippingAddress, contains);
		}
	}

	protected void assertValid(ShippingAddress shippingAddress) {
		boolean valid = true;

		if (shippingAddress.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (shippingAddress.getCity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (shippingAddress.getCountryISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (shippingAddress.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCodeCopy", additionalAssertFieldName)) {

				if (shippingAddress.getExternalReferenceCodeCopy() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (shippingAddress.getLatitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (shippingAddress.getLongitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (shippingAddress.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (shippingAddress.getPhoneNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (shippingAddress.getRegionISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (shippingAddress.getStreet1() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (shippingAddress.getStreet2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (shippingAddress.getStreet3() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (shippingAddress.getZip() == null) {
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

	protected void assertValid(Page<ShippingAddress> page) {
		boolean valid = false;

		java.util.Collection<ShippingAddress> shippingAddresses =
			page.getItems();

		int size = shippingAddresses.size();

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
		ShippingAddress shippingAddress1, ShippingAddress shippingAddress2) {

		if (shippingAddress1 == shippingAddress2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getCity(),
						shippingAddress2.getCity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getCountryISOCode(),
						shippingAddress2.getCountryISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getDescription(),
						shippingAddress2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCodeCopy", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						shippingAddress1.getExternalReferenceCodeCopy(),
						shippingAddress2.getExternalReferenceCodeCopy())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getId(), shippingAddress2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getLatitude(),
						shippingAddress2.getLatitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getLongitude(),
						shippingAddress2.getLongitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getName(),
						shippingAddress2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getPhoneNumber(),
						shippingAddress2.getPhoneNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getRegionISOCode(),
						shippingAddress2.getRegionISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getStreet1(),
						shippingAddress2.getStreet1())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getStreet2(),
						shippingAddress2.getStreet2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getStreet3(),
						shippingAddress2.getStreet3())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						shippingAddress1.getZip(), shippingAddress2.getZip())) {

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
		ShippingAddress shippingAddress, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("city", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getCity(),
						jsonObject.getString("city"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getCountryISOCode(),
						jsonObject.getString("countryISOCode"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getDescription(),
						jsonObject.getString("description"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("externalReferenceCodeCopy", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getExternalReferenceCodeCopy(),
						jsonObject.getString("externalReferenceCodeCopy"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getLatitude(),
						jsonObject.getDouble("latitude"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getLongitude(),
						jsonObject.getDouble("longitude"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getName(),
						jsonObject.getString("name"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getPhoneNumber(),
						jsonObject.getString("phoneNumber"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getRegionISOCode(),
						jsonObject.getString("regionISOCode"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getStreet1(),
						jsonObject.getString("street1"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getStreet2(),
						jsonObject.getString("street2"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getStreet3(),
						jsonObject.getString("street3"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", fieldName)) {
				if (!Objects.deepEquals(
						shippingAddress.getZip(),
						jsonObject.getString("zip"))) {

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

		if (!(_shippingAddressResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_shippingAddressResource;

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
		ShippingAddress shippingAddress) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("city")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getCity()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("countryISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getCountryISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCodeCopy")) {
			sb.append("'");
			sb.append(
				String.valueOf(shippingAddress.getExternalReferenceCodeCopy()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("latitude")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("longitude")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("phoneNumber")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getPhoneNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("regionISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getRegionISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street1")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getStreet1()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street2")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getStreet2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street3")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getStreet3()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("zip")) {
			sb.append("'");
			sb.append(String.valueOf(shippingAddress.getZip()));
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

	protected ShippingAddress randomShippingAddress() throws Exception {
		return new ShippingAddress() {
			{
				city = RandomTestUtil.randomString();
				countryISOCode = RandomTestUtil.randomString();
				description = RandomTestUtil.randomString();
				externalReferenceCodeCopy = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				latitude = RandomTestUtil.randomDouble();
				longitude = RandomTestUtil.randomDouble();
				name = RandomTestUtil.randomString();
				phoneNumber = RandomTestUtil.randomString();
				regionISOCode = RandomTestUtil.randomString();
				street1 = RandomTestUtil.randomString();
				street2 = RandomTestUtil.randomString();
				street3 = RandomTestUtil.randomString();
				zip = RandomTestUtil.randomString();
			}
		};
	}

	protected ShippingAddress randomIrrelevantShippingAddress()
		throws Exception {

		ShippingAddress randomIrrelevantShippingAddress =
			randomShippingAddress();

		return randomIrrelevantShippingAddress;
	}

	protected ShippingAddress randomPatchShippingAddress() throws Exception {
		return randomShippingAddress();
	}

	protected ShippingAddressResource shippingAddressResource;
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
		BaseShippingAddressResourceTestCase.class);

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
	private com.liferay.headless.commerce.admin.order.resource.v1_0.
		ShippingAddressResource _shippingAddressResource;

}