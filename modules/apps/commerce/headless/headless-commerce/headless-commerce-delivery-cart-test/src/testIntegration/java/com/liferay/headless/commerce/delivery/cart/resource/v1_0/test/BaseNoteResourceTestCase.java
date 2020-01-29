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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Note;
import com.liferay.headless.commerce.delivery.cart.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Page;
import com.liferay.headless.commerce.delivery.cart.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.cart.client.resource.v1_0.NoteResource;
import com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0.NoteSerDes;
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
public abstract class BaseNoteResourceTestCase {

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

		_noteResource.setContextCompany(testCompany);

		NoteResource.Builder builder = NoteResource.builder();

		noteResource = builder.locale(
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

		Note note1 = randomNote();

		String json = objectMapper.writeValueAsString(note1);

		Note note2 = NoteSerDes.toDTO(json);

		Assert.assertTrue(equals(note1, note2));
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

		Note note = randomNote();

		String json1 = objectMapper.writeValueAsString(note);
		String json2 = NoteSerDes.toJSON(note);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Note note = randomNote();

		note.setAuthor(regex);
		note.setContent(regex);

		String json = NoteSerDes.toJSON(note);

		Assert.assertFalse(json.contains(regex));

		note = NoteSerDes.toDTO(json);

		Assert.assertEquals(regex, note.getAuthor());
		Assert.assertEquals(regex, note.getContent());
	}

	@Test
	public void testGetChannelCartNotesPage() throws Exception {
		Page<Note> page = noteResource.getChannelCartNotesPage(
			testGetChannelCartNotesPage_getChannelId(),
			testGetChannelCartNotesPage_getCartId(), Pagination.of(1, 2));

		Assert.assertEquals(0, page.getTotalCount());

		Long channelId = testGetChannelCartNotesPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelCartNotesPage_getIrrelevantChannelId();
		Long cartId = testGetChannelCartNotesPage_getCartId();
		Long irrelevantCartId =
			testGetChannelCartNotesPage_getIrrelevantCartId();

		if ((irrelevantChannelId != null) && (irrelevantCartId != null)) {
			Note irrelevantNote = testGetChannelCartNotesPage_addNote(
				irrelevantChannelId, irrelevantCartId, randomIrrelevantNote());

			page = noteResource.getChannelCartNotesPage(
				irrelevantChannelId, irrelevantCartId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantNote), (List<Note>)page.getItems());
			assertValid(page);
		}

		Note note1 = testGetChannelCartNotesPage_addNote(
			channelId, cartId, randomNote());

		Note note2 = testGetChannelCartNotesPage_addNote(
			channelId, cartId, randomNote());

		page = noteResource.getChannelCartNotesPage(
			channelId, cartId, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(note1, note2), (List<Note>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetChannelCartNotesPageWithPagination() throws Exception {
		Long channelId = testGetChannelCartNotesPage_getChannelId();
		Long cartId = testGetChannelCartNotesPage_getCartId();

		Note note1 = testGetChannelCartNotesPage_addNote(
			channelId, cartId, randomNote());

		Note note2 = testGetChannelCartNotesPage_addNote(
			channelId, cartId, randomNote());

		Note note3 = testGetChannelCartNotesPage_addNote(
			channelId, cartId, randomNote());

		Page<Note> page1 = noteResource.getChannelCartNotesPage(
			channelId, cartId, Pagination.of(1, 2));

		List<Note> notes1 = (List<Note>)page1.getItems();

		Assert.assertEquals(notes1.toString(), 2, notes1.size());

		Page<Note> page2 = noteResource.getChannelCartNotesPage(
			channelId, cartId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<Note> notes2 = (List<Note>)page2.getItems();

		Assert.assertEquals(notes2.toString(), 1, notes2.size());

		Page<Note> page3 = noteResource.getChannelCartNotesPage(
			channelId, cartId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(note1, note2, note3), (List<Note>)page3.getItems());
	}

	protected Note testGetChannelCartNotesPage_addNote(
			Long channelId, Long cartId, Note note)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartNotesPage_getChannelId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartNotesPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	protected Long testGetChannelCartNotesPage_getCartId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelCartNotesPage_getIrrelevantCartId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostChannelCartNote() throws Exception {
		Note randomNote = randomNote();

		Note postNote = testPostChannelCartNote_addNote(randomNote);

		assertEquals(randomNote, postNote);
		assertValid(postNote);
	}

	protected Note testPostChannelCartNote_addNote(Note note) throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteChannelCartNote() throws Exception {
		Note note = testDeleteChannelCartNote_addNote();

		assertHttpResponseStatusCode(
			204,
			noteResource.deleteChannelCartNoteHttpResponse(
				null, null, note.getId()));

		assertHttpResponseStatusCode(
			404,
			noteResource.getChannelCartNoteHttpResponse(
				null, null, note.getId()));

		assertHttpResponseStatusCode(
			404, noteResource.getChannelCartNoteHttpResponse(null, null, 0L));
	}

	protected Note testDeleteChannelCartNote_addNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetChannelCartNote() throws Exception {
		Note postNote = testGetChannelCartNote_addNote();

		Note getNote = noteResource.getChannelCartNote(
			postNote.getChannelId(), postNote.getCartId(), postNote.getId());

		assertEquals(postNote, getNote);
		assertValid(getNote);
	}

	protected Note testGetChannelCartNote_addNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetChannelCartNote() throws Exception {
		Note note = testGraphQLNote_addNote();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"channelCartNote",
				new HashMap<String, Object>() {
					{
						put("channelId", note.getChannelId());
						put("cartId", note.getCartId());
						put("noteId", note.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(
				note, dataJSONObject.getJSONObject("channelCartNote")));
	}

	@Test
	public void testPatchChannelCartNote() throws Exception {
		Assert.assertTrue(false);
	}

	protected Note testGraphQLNote_addNote() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Note note1, Note note2) {
		Assert.assertTrue(
			note1 + " does not equal " + note2, equals(note1, note2));
	}

	protected void assertEquals(List<Note> notes1, List<Note> notes2) {
		Assert.assertEquals(notes1.size(), notes2.size());

		for (int i = 0; i < notes1.size(); i++) {
			Note note1 = notes1.get(i);
			Note note2 = notes2.get(i);

			assertEquals(note1, note2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Note> notes1, List<Note> notes2) {

		Assert.assertEquals(notes1.size(), notes2.size());

		for (Note note1 : notes1) {
			boolean contains = false;

			for (Note note2 : notes2) {
				if (equals(note1, note2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(notes2 + " does not contain " + note1, contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<Note> notes, JSONArray jsonArray) {

		for (Note note : notes) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(note, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + note, contains);
		}
	}

	protected void assertValid(Note note) {
		boolean valid = true;

		if (note.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (note.getAuthor() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (note.getContent() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (note.getOrderId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("restricted", additionalAssertFieldName)) {
				if (note.getRestricted() == null) {
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

	protected void assertValid(Page<Note> page) {
		boolean valid = false;

		java.util.Collection<Note> notes = page.getItems();

		int size = notes.size();

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

	protected boolean equals(Note note1, Note note2) {
		if (note1 == note2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("author", additionalAssertFieldName)) {
				if (!Objects.deepEquals(note1.getAuthor(), note2.getAuthor())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("content", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						note1.getContent(), note2.getContent())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(note1.getId(), note2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("orderId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						note1.getOrderId(), note2.getOrderId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("restricted", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						note1.getRestricted(), note2.getRestricted())) {

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

	protected boolean equalsJSONObject(Note note, JSONObject jsonObject) {
		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("author", fieldName)) {
				if (!Objects.deepEquals(
						note.getAuthor(), jsonObject.getString("author"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("content", fieldName)) {
				if (!Objects.deepEquals(
						note.getContent(), jsonObject.getString("content"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						note.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderId", fieldName)) {
				if (!Objects.deepEquals(
						note.getOrderId(), jsonObject.getLong("orderId"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("restricted", fieldName)) {
				if (!Objects.deepEquals(
						note.getRestricted(),
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

		if (!(_noteResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_noteResource;

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
		EntityField entityField, String operator, Note note) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("author")) {
			sb.append("'");
			sb.append(String.valueOf(note.getAuthor()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("content")) {
			sb.append("'");
			sb.append(String.valueOf(note.getContent()));
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

	protected Note randomNote() throws Exception {
		return new Note() {
			{
				author = RandomTestUtil.randomString();
				content = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				orderId = RandomTestUtil.randomLong();
				restricted = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected Note randomIrrelevantNote() throws Exception {
		Note randomIrrelevantNote = randomNote();

		return randomIrrelevantNote;
	}

	protected Note randomPatchNote() throws Exception {
		return randomNote();
	}

	protected NoteResource noteResource;
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
		BaseNoteResourceTestCase.class);

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
		com.liferay.headless.commerce.delivery.cart.resource.v1_0.NoteResource
			_noteResource;

}