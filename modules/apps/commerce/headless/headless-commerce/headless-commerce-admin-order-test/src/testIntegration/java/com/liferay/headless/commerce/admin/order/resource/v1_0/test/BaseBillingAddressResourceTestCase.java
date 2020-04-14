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

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.BillingAddress;
import com.liferay.headless.commerce.admin.order.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.order.client.pagination.Page;
import com.liferay.headless.commerce.admin.order.client.resource.v1_0.BillingAddressResource;
import com.liferay.headless.commerce.admin.order.client.serdes.v1_0.BillingAddressSerDes;
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
public abstract class BaseBillingAddressResourceTestCase {

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

		_billingAddressResource.setContextCompany(testCompany);

		BillingAddressResource.Builder builder =
			BillingAddressResource.builder();

		billingAddressResource = builder.locale(
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

		BillingAddress billingAddress1 = randomBillingAddress();

		String json = objectMapper.writeValueAsString(billingAddress1);

		BillingAddress billingAddress2 = BillingAddressSerDes.toDTO(json);

		Assert.assertTrue(equals(billingAddress1, billingAddress2));
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

		BillingAddress billingAddress = randomBillingAddress();

		String json1 = objectMapper.writeValueAsString(billingAddress);
		String json2 = BillingAddressSerDes.toJSON(billingAddress);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		BillingAddress billingAddress = randomBillingAddress();

		billingAddress.setCity(regex);
		billingAddress.setCountryISOCode(regex);
		billingAddress.setDescription(regex);
		billingAddress.setExternalReferenceCodeCopy(regex);
		billingAddress.setName(regex);
		billingAddress.setPhoneNumber(regex);
		billingAddress.setRegionISOCode(regex);
		billingAddress.setStreet1(regex);
		billingAddress.setStreet2(regex);
		billingAddress.setStreet3(regex);
		billingAddress.setVatNumber(regex);
		billingAddress.setZip(regex);

		String json = BillingAddressSerDes.toJSON(billingAddress);

		Assert.assertFalse(json.contains(regex));

		billingAddress = BillingAddressSerDes.toDTO(json);

		Assert.assertEquals(regex, billingAddress.getCity());
		Assert.assertEquals(regex, billingAddress.getCountryISOCode());
		Assert.assertEquals(regex, billingAddress.getDescription());
		Assert.assertEquals(
			regex, billingAddress.getExternalReferenceCodeCopy());
		Assert.assertEquals(regex, billingAddress.getName());
		Assert.assertEquals(regex, billingAddress.getPhoneNumber());
		Assert.assertEquals(regex, billingAddress.getRegionISOCode());
		Assert.assertEquals(regex, billingAddress.getStreet1());
		Assert.assertEquals(regex, billingAddress.getStreet2());
		Assert.assertEquals(regex, billingAddress.getStreet3());
		Assert.assertEquals(regex, billingAddress.getVatNumber());
		Assert.assertEquals(regex, billingAddress.getZip());
	}

	@Test
	public void testGetOrderByExternalReferenceCodeBillingAddress()
		throws Exception {

		BillingAddress postBillingAddress =
			testGetOrderByExternalReferenceCodeBillingAddress_addBillingAddress();

		BillingAddress getBillingAddress =
			billingAddressResource.
				getOrderByExternalReferenceCodeBillingAddress(null);

		assertEquals(postBillingAddress, getBillingAddress);
		assertValid(getBillingAddress);
	}

	protected BillingAddress
			testGetOrderByExternalReferenceCodeBillingAddress_addBillingAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOrderByExternalReferenceCodeBillingAddress()
		throws Exception {

		BillingAddress billingAddress =
			testGraphQLBillingAddress_addBillingAddress();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"orderByExternalReferenceCodeBillingAddress",
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
				billingAddress,
				dataJSONObject.getJSONObject(
					"orderByExternalReferenceCodeBillingAddress")));
	}

	@Test
	public void testPatchOrderByExternalReferenceCodeBillingAddress()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGetOrderIdBillingAddress() throws Exception {
		BillingAddress postBillingAddress =
			testGetOrderIdBillingAddress_addBillingAddress();

		BillingAddress getBillingAddress =
			billingAddressResource.getOrderIdBillingAddress(
				postBillingAddress.getId());

		assertEquals(postBillingAddress, getBillingAddress);
		assertValid(getBillingAddress);
	}

	protected BillingAddress testGetOrderIdBillingAddress_addBillingAddress()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetOrderIdBillingAddress() throws Exception {
		BillingAddress billingAddress =
			testGraphQLBillingAddress_addBillingAddress();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"orderIdBillingAddress",
				new HashMap<String, Object>() {
					{
						put("id", billingAddress.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				billingAddress,
				dataJSONObject.getJSONObject("orderIdBillingAddress")));
	}

	@Test
	public void testPatchOrderIdBillingAddress() throws Exception {
		Assert.assertTrue(false);
	}

	protected BillingAddress testGraphQLBillingAddress_addBillingAddress()
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
		BillingAddress billingAddress1, BillingAddress billingAddress2) {

		Assert.assertTrue(
			billingAddress1 + " does not equal " + billingAddress2,
			equals(billingAddress1, billingAddress2));
	}

	protected void assertEquals(
		List<BillingAddress> billingAddresses1,
		List<BillingAddress> billingAddresses2) {

		Assert.assertEquals(billingAddresses1.size(), billingAddresses2.size());

		for (int i = 0; i < billingAddresses1.size(); i++) {
			BillingAddress billingAddress1 = billingAddresses1.get(i);
			BillingAddress billingAddress2 = billingAddresses2.get(i);

			assertEquals(billingAddress1, billingAddress2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<BillingAddress> billingAddresses1,
		List<BillingAddress> billingAddresses2) {

		Assert.assertEquals(billingAddresses1.size(), billingAddresses2.size());

		for (BillingAddress billingAddress1 : billingAddresses1) {
			boolean contains = false;

			for (BillingAddress billingAddress2 : billingAddresses2) {
				if (equals(billingAddress1, billingAddress2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				billingAddresses2 + " does not contain " + billingAddress1,
				contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<BillingAddress> billingAddresses, JSONArray jsonArray) {

		for (BillingAddress billingAddress : billingAddresses) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(billingAddress, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + billingAddress, contains);
		}
	}

	protected void assertValid(BillingAddress billingAddress) {
		boolean valid = true;

		if (billingAddress.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (billingAddress.getCity() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (billingAddress.getCountryISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (billingAddress.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCodeCopy", additionalAssertFieldName)) {

				if (billingAddress.getExternalReferenceCodeCopy() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (billingAddress.getLatitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (billingAddress.getLongitude() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (billingAddress.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (billingAddress.getPhoneNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (billingAddress.getRegionISOCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (billingAddress.getStreet1() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (billingAddress.getStreet2() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (billingAddress.getStreet3() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("vatNumber", additionalAssertFieldName)) {
				if (billingAddress.getVatNumber() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (billingAddress.getZip() == null) {
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

	protected void assertValid(Page<BillingAddress> page) {
		boolean valid = false;

		java.util.Collection<BillingAddress> billingAddresses = page.getItems();

		int size = billingAddresses.size();

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
		BillingAddress billingAddress1, BillingAddress billingAddress2) {

		if (billingAddress1 == billingAddress2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("city", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getCity(), billingAddress2.getCity())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getCountryISOCode(),
						billingAddress2.getCountryISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getDescription(),
						billingAddress2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCodeCopy", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						billingAddress1.getExternalReferenceCodeCopy(),
						billingAddress2.getExternalReferenceCodeCopy())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getId(), billingAddress2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getLatitude(),
						billingAddress2.getLatitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getLongitude(),
						billingAddress2.getLongitude())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getName(), billingAddress2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getPhoneNumber(),
						billingAddress2.getPhoneNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getRegionISOCode(),
						billingAddress2.getRegionISOCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getStreet1(),
						billingAddress2.getStreet1())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getStreet2(),
						billingAddress2.getStreet2())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getStreet3(),
						billingAddress2.getStreet3())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("vatNumber", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getVatNumber(),
						billingAddress2.getVatNumber())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						billingAddress1.getZip(), billingAddress2.getZip())) {

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
		BillingAddress billingAddress, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("city", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getCity(),
						jsonObject.getString("city"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("countryISOCode", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getCountryISOCode(),
						jsonObject.getString("countryISOCode"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getDescription(),
						jsonObject.getString("description"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("externalReferenceCodeCopy", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getExternalReferenceCodeCopy(),
						jsonObject.getString("externalReferenceCodeCopy"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("latitude", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getLatitude(),
						jsonObject.getDouble("latitude"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("longitude", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getLongitude(),
						jsonObject.getDouble("longitude"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getName(),
						jsonObject.getString("name"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("phoneNumber", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getPhoneNumber(),
						jsonObject.getString("phoneNumber"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("regionISOCode", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getRegionISOCode(),
						jsonObject.getString("regionISOCode"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street1", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getStreet1(),
						jsonObject.getString("street1"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street2", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getStreet2(),
						jsonObject.getString("street2"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("street3", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getStreet3(),
						jsonObject.getString("street3"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("vatNumber", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getVatNumber(),
						jsonObject.getString("vatNumber"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("zip", fieldName)) {
				if (!Objects.deepEquals(
						billingAddress.getZip(), jsonObject.getString("zip"))) {

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

		if (!(_billingAddressResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_billingAddressResource;

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
		BillingAddress billingAddress) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("city")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getCity()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("countryISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getCountryISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCodeCopy")) {
			sb.append("'");
			sb.append(
				String.valueOf(billingAddress.getExternalReferenceCodeCopy()));
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
			sb.append(String.valueOf(billingAddress.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("phoneNumber")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getPhoneNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("regionISOCode")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getRegionISOCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street1")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getStreet1()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street2")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getStreet2()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("street3")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getStreet3()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("vatNumber")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getVatNumber()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("zip")) {
			sb.append("'");
			sb.append(String.valueOf(billingAddress.getZip()));
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

	protected BillingAddress randomBillingAddress() throws Exception {
		return new BillingAddress() {
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
				vatNumber = RandomTestUtil.randomString();
				zip = RandomTestUtil.randomString();
			}
		};
	}

	protected BillingAddress randomIrrelevantBillingAddress() throws Exception {
		BillingAddress randomIrrelevantBillingAddress = randomBillingAddress();

		return randomIrrelevantBillingAddress;
	}

	protected BillingAddress randomPatchBillingAddress() throws Exception {
		return randomBillingAddress();
	}

	protected BillingAddressResource billingAddressResource;
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
		BaseBillingAddressResourceTestCase.class);

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
		BillingAddressResource _billingAddressResource;

}