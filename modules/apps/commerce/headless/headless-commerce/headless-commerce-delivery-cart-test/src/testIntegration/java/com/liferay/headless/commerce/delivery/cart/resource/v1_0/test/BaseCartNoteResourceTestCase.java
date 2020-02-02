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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartNote;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.CartNoteResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.CartNoteSerDes;
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

import org.apache.commons.beanutils.BeanUtils;
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
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BaseCartNoteResourceTestCase {

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

		_cartNoteResource.setContextCompany(testCompany);

		CartNoteResource.Builder builder = CartNoteResource.builder();

		cartNoteResource = builder.locale(
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

		CartNote cartNote1 = randomCartNote();

		String json = objectMapper.writeValueAsString(cartNote1);

		CartNote cartNote2 = CartNoteSerDes.toDTO(json);

		Assert.assertTrue(equals(cartNote1, cartNote2));
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

		CartNote cartNote = randomCartNote();

		String json1 = objectMapper.writeValueAsString(cartNote);
		String json2 = CartNoteSerDes.toJSON(cartNote);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		CartNote cartNote = randomCartNote();

		cartNote.setAuthor(regex);
		cartNote.setContent(regex);

		String json = CartNoteSerDes.toJSON(cartNote);

		Assert.assertFalse(json.contains(regex));

		cartNote = CartNoteSerDes.toDTO(json);

		Assert.assertEquals(regex, cartNote.getAuthor());
		Assert.assertEquals(regex, cartNote.getContent());
	}

	@Test
	public void testGetCartNotesPage() throws Exception {
		Page<CartNote> page = cartNoteResource.getCartNotesPage(
			testGetCartNotesPage_getCartId(), Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());

		Long cartId = testGetCartNotesPage_getCartId();
		Long irrelevantCartId = testGetCartNotesPage_getIrrelevantCartId();

		if ((irrelevantCartId != null)) {
			CartNote irrelevantCartNote = testGetCartNotesPage_addCartNote(
				irrelevantCartId, randomIrrelevantCartNote());

			page = cartNoteResource.getCartNotesPage(
				irrelevantCartId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantCartNote),
				(List<CartNote>)page.getItems());
			assertValid(page);
		}

		CartNote cartNote1 = testGetCartNotesPage_addCartNote(
			cartId, randomCartNote());

		CartNote cartNote2 = testGetCartNotesPage_addCartNote(
			cartId, randomCartNote());

		page = cartNoteResource.getCartNotesPage(cartId, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(cartNote1, cartNote2),
			(List<CartNote>)page.getItems());
		assertValid(page);

		cartNoteResource.deleteCartNote(null);

		cartNoteResource.deleteCartNote(null);
	}

	@Test
	public void testGetCartNotesPageWithPagination() throws Exception {
		Long cartId = testGetCartNotesPage_getCartId();

		CartNote cartNote1 = testGetCartNotesPage_addCartNote(
			cartId, randomCartNote());

		CartNote cartNote2 = testGetCartNotesPage_addCartNote(
			cartId, randomCartNote());

		CartNote cartNote3 = testGetCartNotesPage_addCartNote(
			cartId, randomCartNote());

		Page<CartNote> page1 = cartNoteResource.getCartNotesPage(
			cartId, Pagination.of(1, 2));

		List<CartNote> cartNotes1 = (List<CartNote>)page1.getItems();

		Assert.assertEquals(cartNotes1.toString(), 2, cartNotes1.size());

		Page<CartNote> page2 = cartNoteResource.getCartNotesPage(
			cartId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<CartNote> cartNotes2 = (List<CartNote>)page2.getItems();

		Assert.assertEquals(cartNotes2.toString(), 1, cartNotes2.size());

		Page<CartNote> page3 = cartNoteResource.getCartNotesPage(
			cartId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(cartNote1, cartNote2, cartNote3),
			(List<CartNote>)page3.getItems());
	}

	protected CartNote testGetCartNotesPage_addCartNote(
			Long cartId, CartNote cartNote)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCartNotesPage_getCartId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCartNotesPage_getIrrelevantCartId() throws Exception {
		return null;
	}

	@Test
	public void testGraphQLGetCartNotesPage() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		List<GraphQLField> itemsGraphQLFields = getGraphQLFields();

		graphQLFields.add(
			new GraphQLField(
				"items", itemsGraphQLFields.toArray(new GraphQLField[0])));

		graphQLFields.add(new GraphQLField("page"));
		graphQLFields.add(new GraphQLField("totalCount"));

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"cartNotes",
				new HashMap<String, Object>() {
					{
						put("page", 1);
						put("pageSize", 2);
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		JSONObject cartNotesJSONObject = dataJSONObject.getJSONObject(
			"cartNotes");

		Assert.assertEquals(0, cartNotesJSONObject.get("totalCount"));

		CartNote cartNote1 = testGraphQLCartNote_addCartNote();
		CartNote cartNote2 = testGraphQLCartNote_addCartNote();

		jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		dataJSONObject = jsonObject.getJSONObject("data");

		cartNotesJSONObject = dataJSONObject.getJSONObject("cartNotes");

		Assert.assertEquals(2, cartNotesJSONObject.get("totalCount"));

		assertEqualsJSONArray(
			Arrays.asList(cartNote1, cartNote2),
			cartNotesJSONObject.getJSONArray("items"));
	}

	@Test
	public void testPostCartNote() throws Exception {
		CartNote randomCartNote = randomCartNote();

		CartNote postCartNote = testPostCartNote_addCartNote(randomCartNote);

		assertEquals(randomCartNote, postCartNote);
		assertValid(postCartNote);
	}

	protected CartNote testPostCartNote_addCartNote(CartNote cartNote)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteCartNote() throws Exception {
		CartNote cartNote = testDeleteCartNote_addCartNote();

		assertHttpResponseStatusCode(
			204, cartNoteResource.deleteCartNoteHttpResponse(null));

		assertHttpResponseStatusCode(
			404, cartNoteResource.getCartNoteHttpResponse(null));

		assertHttpResponseStatusCode(
			404, cartNoteResource.getCartNoteHttpResponse(null));
	}

	protected CartNote testDeleteCartNote_addCartNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteCartNote() throws Exception {
		CartNote cartNote = testGraphQLCartNote_addCartNote();

		GraphQLField graphQLField = new GraphQLField(
			"mutation",
			new GraphQLField(
				"deleteCartNote",
				new HashMap<String, Object>() {
					{
						put("cartNoteId", cartNote.getId());
					}
				}));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(dataJSONObject.getBoolean("deleteCartNote"));

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"graphql.execution.SimpleDataFetcherExceptionHandler",
					Level.WARN)) {

			graphQLField = new GraphQLField(
				"query",
				new GraphQLField(
					"cartNote",
					new HashMap<String, Object>() {
						{
							put("cartNoteId", cartNote.getId());
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
	public void testGetCartNote() throws Exception {
		CartNote postCartNote = testGetCartNote_addCartNote();

		CartNote getCartNote = cartNoteResource.getCartNote(null);

		assertEquals(postCartNote, getCartNote);
		assertValid(getCartNote);
	}

	protected CartNote testGetCartNote_addCartNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetCartNote() throws Exception {
		CartNote cartNote = testGraphQLCartNote_addCartNote();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"cartNote",
				new HashMap<String, Object>() {
					{
						put("noteId", null);
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				cartNote, dataJSONObject.getJSONObject("cartNote")));
	}

	@Test
	public void testPatchCartNote() throws Exception {
		CartNote postCartNote = testPatchCartNote_addCartNote();

		CartNote randomPatchCartNote = randomPatchCartNote();

		CartNote patchCartNote = cartNoteResource.patchCartNote(
			postCartNote.getId(), randomPatchCartNote);

		CartNote expectedPatchCartNote = (CartNote)BeanUtils.cloneBean(
			postCartNote);

		_beanUtilsBean.copyProperties(
			expectedPatchCartNote, randomPatchCartNote);

		CartNote getCartNote = cartNoteResource.getCartNote(
			patchCartNote.getId());

		assertEquals(expectedPatchCartNote, getCartNote);
		assertValid(getCartNote);
	}

	protected CartNote testPatchCartNote_addCartNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutCartNote() throws Exception {
		CartNote postCartNote = testPutCartNote_addCartNote();

		CartNote randomCartNote = randomCartNote();

		CartNote putCartNote = cartNoteResource.putCartNote(
			postCartNote.getId(), randomCartNote);

		assertEquals(randomCartNote, putCartNote);
		assertValid(putCartNote);

		CartNote getCartNote = cartNoteResource.getCartNote(
			putCartNote.getId());

		assertEquals(randomCartNote, getCartNote);
		assertValid(getCartNote);
	}

	protected CartNote testPutCartNote_addCartNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected CartNote testGraphQLCartNote_addCartNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(CartNote cartNote1, CartNote cartNote2) {
		Assert.assertTrue(
			cartNote1 + " does not equal " + cartNote2,
			equals(cartNote1, cartNote2));
	}

	protected void assertEquals(
		List<CartNote> cartNotes1, List<CartNote> cartNotes2) {

		Assert.assertEquals(cartNotes1.size(), cartNotes2.size());

		for (int i = 0; i < cartNotes1.size(); i++) {
			CartNote cartNote1 = cartNotes1.get(i);
			CartNote cartNote2 = cartNotes2.get(i);

			assertEquals(cartNote1, cartNote2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<CartNote> cartNotes1, List<CartNote> cartNotes2) {

		Assert.assertEquals(cartNotes1.size(), cartNotes2.size());

		for (CartNote cartNote1 : cartNotes1) {
			boolean contains = false;

			for (CartNote cartNote2 : cartNotes2) {
				if (equals(cartNote1, cartNote2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				cartNotes2 + " does not contain " + cartNote1, contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<CartNote> cartNotes, JSONArray jsonArray) {

		for (CartNote cartNote : cartNotes) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(cartNote, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + cartNote, contains);
		}
	}

	protected void assertValid(CartNote cartNote) {
		boolean valid = true;

		if (cartNote.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (cartNote.getAuthor() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (cartNote.getContent() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (cartNote.getOrderId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("restricted", additionalAssertFieldName)) {
				if (cartNote.getRestricted() == null) {
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

	protected void assertValid(Page<CartNote> page) {
		boolean valid = false;

		java.util.Collection<CartNote> cartNotes = page.getItems();

		int size = cartNotes.size();

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

	protected boolean equals(CartNote cartNote1, CartNote cartNote2) {
		if (cartNote1 == cartNote2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cartNote1.getAuthor(), cartNote2.getAuthor())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cartNote1.getContent(), cartNote2.getContent())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(cartNote1.getId(), cartNote2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cartNote1.getOrderId(), cartNote2.getOrderId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("restricted", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						cartNote1.getRestricted(), cartNote2.getRestricted())) {

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
		CartNote cartNote, JSONObject jsonObject) {

		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("author", fieldName)) {
				if (!Objects.deepEquals(
						cartNote.getAuthor(), jsonObject.getString("author"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("content", fieldName)) {
				if (!Objects.deepEquals(
						cartNote.getContent(),
						jsonObject.getString("content"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						cartNote.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderId", fieldName)) {
				if (!Objects.deepEquals(
						cartNote.getOrderId(), jsonObject.getLong("orderId"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("restricted", fieldName)) {
				if (!Objects.deepEquals(
						cartNote.getRestricted(),
						jsonObject.getBoolean("restricted"))) {

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

		if (!(_cartNoteResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_cartNoteResource;

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
		EntityField entityField, String operator, CartNote cartNote) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("author")) {
			sb.append("'");
			sb.append(String.valueOf(cartNote.getAuthor()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("content")) {
			sb.append("'");
			sb.append(String.valueOf(cartNote.getContent()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("restricted")) {
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

	protected CartNote randomCartNote() throws Exception {
		return new CartNote() {
			{
				author = RandomTestUtil.randomString();
				content = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				orderId = RandomTestUtil.randomLong();
				restricted = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected CartNote randomIrrelevantCartNote() throws Exception {
		CartNote randomIrrelevantCartNote = randomCartNote();

		return randomIrrelevantCartNote;
	}

	protected CartNote randomPatchCartNote() throws Exception {
		return randomCartNote();
	}

	protected CartNoteResource cartNoteResource;
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
		BaseCartNoteResourceTestCase.class);

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
			CartNoteResource _cartNoteResource;

}