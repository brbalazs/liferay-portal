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

package com.liferay.headless.commerce.admin.channel.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.channel.client.dto.v1_0.Channel;
import com.liferay.headless.commerce.admin.channel.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.channel.client.pagination.Page;
import com.liferay.headless.commerce.admin.channel.client.resource.v1_0.ChannelResource;
import com.liferay.headless.commerce.admin.channel.client.serdes.v1_0.ChannelSerDes;
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
public abstract class BaseChannelResourceTestCase {

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

		_channelResource.setContextCompany(testCompany);

		ChannelResource.Builder builder = ChannelResource.builder();

		channelResource = builder.locale(
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

		Channel channel1 = randomChannel();

		String json = objectMapper.writeValueAsString(channel1);

		Channel channel2 = ChannelSerDes.toDTO(json);

		Assert.assertTrue(equals(channel1, channel2));
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

		Channel channel = randomChannel();

		String json1 = objectMapper.writeValueAsString(channel);
		String json2 = ChannelSerDes.toJSON(channel);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Channel channel = randomChannel();

		channel.setCurrency(regex);
		channel.setName(regex);
		channel.setType(regex);

		String json = ChannelSerDes.toJSON(channel);

		Assert.assertFalse(json.contains(regex));

		channel = ChannelSerDes.toDTO(json);

		Assert.assertEquals(regex, channel.getCurrency());
		Assert.assertEquals(regex, channel.getName());
		Assert.assertEquals(regex, channel.getType());
	}

	@Test
	public void testGetChannelsPage() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetChannelsPage() throws Exception {
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
				"channels",
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

		JSONObject channelsJSONObject = dataJSONObject.getJSONObject(
			"channels");

		Assert.assertEquals(0, channelsJSONObject.get("totalCount"));

		Channel channel1 = testGraphQLChannel_addChannel();
		Channel channel2 = testGraphQLChannel_addChannel();

		jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		dataJSONObject = jsonObject.getJSONObject("data");

		channelsJSONObject = dataJSONObject.getJSONObject("channels");

		Assert.assertEquals(2, channelsJSONObject.get("totalCount"));

		assertEqualsJSONArray(
			Arrays.asList(channel1, channel2),
			channelsJSONObject.getJSONArray("items"));
	}

	@Test
	public void testPostChannel() throws Exception {
		Channel randomChannel = randomChannel();

		Channel postChannel = testPostChannel_addChannel(randomChannel);

		assertEquals(randomChannel, postChannel);
		assertValid(postChannel);
	}

	protected Channel testPostChannel_addChannel(Channel channel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testNullBatch() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteChannel() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Channel channel = testDeleteChannel_addChannel();

		assertHttpResponseStatusCode(
			204, channelResource.deleteChannelHttpResponse(channel.getId()));

		assertHttpResponseStatusCode(
			404, channelResource.getChannelHttpResponse(channel.getId()));

		assertHttpResponseStatusCode(
			404, channelResource.getChannelHttpResponse(0L));
	}

	protected Channel testDeleteChannel_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteChannel() throws Exception {
		Channel channel = testGraphQLChannel_addChannel();

		GraphQLField graphQLField = new GraphQLField(
			"mutation",
			new GraphQLField(
				"deleteChannel",
				new HashMap<String, Object>() {
					{
						put("channelId", channel.getId());
					}
				}));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(dataJSONObject.getBoolean("deleteChannel"));

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"graphql.execution.SimpleDataFetcherExceptionHandler",
					Level.WARN)) {

			graphQLField = new GraphQLField(
				"query",
				new GraphQLField(
					"channel",
					new HashMap<String, Object>() {
						{
							put("channelId", channel.getId());
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
	public void testNullBatch() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		Channel channel = testNullBatch_addChannel();

		assertHttpResponseStatusCode(
			204, channelResource.nullBatchHttpResponse(null, null));
	}

	protected Channel testNullBatch_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetChannel() throws Exception {
		Channel postChannel = testGetChannel_addChannel();

		Channel getChannel = channelResource.getChannel(postChannel.getId());

		assertEquals(postChannel, getChannel);
		assertValid(getChannel);
	}

	protected Channel testGetChannel_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetChannel() throws Exception {
		Channel channel = testGraphQLChannel_addChannel();

		List<GraphQLField> graphQLFields = getGraphQLFields();

		GraphQLField graphQLField = new GraphQLField(
			"query",
			new GraphQLField(
				"channel",
				new HashMap<String, Object>() {
					{
						put("channelId", channel.getId());
					}
				},
				graphQLFields.toArray(new GraphQLField[0])));

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			invoke(graphQLField.toString()));

		JSONObject dataJSONObject = jsonObject.getJSONObject("data");

		Assert.assertTrue(
			equalsJSONObject(channel, dataJSONObject.getJSONObject("channel")));
	}

	@Test
	public void testPatchChannel() throws Exception {
		Channel postChannel = testPatchChannel_addChannel();

		Channel randomPatchChannel = randomPatchChannel();

		Channel patchChannel = channelResource.patchChannel(
			postChannel.getId(), randomPatchChannel);

		Channel expectedPatchChannel = postChannel.clone();

		_beanUtilsBean.copyProperties(expectedPatchChannel, randomPatchChannel);

		Channel getChannel = channelResource.getChannel(patchChannel.getId());

		assertEquals(expectedPatchChannel, getChannel);
		assertValid(getChannel);
	}

	protected Channel testPatchChannel_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutChannel() throws Exception {
		Channel postChannel = testPutChannel_addChannel();

		Channel randomChannel = randomChannel();

		Channel putChannel = channelResource.putChannel(
			postChannel.getId(), randomChannel);

		assertEquals(randomChannel, putChannel);
		assertValid(putChannel);

		Channel getChannel = channelResource.getChannel(putChannel.getId());

		assertEquals(randomChannel, getChannel);
		assertValid(getChannel);
	}

	protected Channel testPutChannel_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testNullBatch() throws Exception {
		Assert.assertTrue(false);
	}

	protected Channel testGraphQLChannel_addChannel() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Channel channel1, Channel channel2) {
		Assert.assertTrue(
			channel1 + " does not equal " + channel2,
			equals(channel1, channel2));
	}

	protected void assertEquals(
		List<Channel> channels1, List<Channel> channels2) {

		Assert.assertEquals(channels1.size(), channels2.size());

		for (int i = 0; i < channels1.size(); i++) {
			Channel channel1 = channels1.get(i);
			Channel channel2 = channels2.get(i);

			assertEquals(channel1, channel2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Channel> channels1, List<Channel> channels2) {

		Assert.assertEquals(channels1.size(), channels2.size());

		for (Channel channel1 : channels1) {
			boolean contains = false;

			for (Channel channel2 : channels2) {
				if (equals(channel1, channel2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				channels2 + " does not contain " + channel1, contains);
		}
	}

	protected void assertEqualsJSONArray(
		List<Channel> channels, JSONArray jsonArray) {

		for (Channel channel : channels) {
			boolean contains = false;

			for (Object object : jsonArray) {
				if (equalsJSONObject(channel, (JSONObject)object)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				jsonArray + " does not contain " + channel, contains);
		}
	}

	protected void assertValid(Channel channel) {
		boolean valid = true;

		if (channel.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("currency", additionalAssertFieldName)) {
				if (channel.getCurrency() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (channel.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (channel.getType() == null) {
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

	protected void assertValid(Page<Channel> page) {
		boolean valid = false;

		java.util.Collection<Channel> channels = page.getItems();

		int size = channels.size();

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

	protected boolean equals(Channel channel1, Channel channel2) {
		if (channel1 == channel2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("currency", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						channel1.getCurrency(), channel2.getCurrency())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(channel1.getId(), channel2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						channel1.getName(), channel2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						channel1.getType(), channel2.getType())) {

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

	protected boolean equalsJSONObject(Channel channel, JSONObject jsonObject) {
		for (String fieldName : getAdditionalAssertFieldNames()) {
			if (Objects.equals("currency", fieldName)) {
				if (!Objects.deepEquals(
						channel.getCurrency(),
						jsonObject.getString("currency"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", fieldName)) {
				if (!Objects.deepEquals(
						channel.getId(), jsonObject.getLong("id"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", fieldName)) {
				if (!Objects.deepEquals(
						channel.getName(), jsonObject.getString("name"))) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", fieldName)) {
				if (!Objects.deepEquals(
						channel.getType(), jsonObject.getString("type"))) {

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

		if (!(_channelResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_channelResource;

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
		EntityField entityField, String operator, Channel channel) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("currency")) {
			sb.append("'");
			sb.append(String.valueOf(channel.getCurrency()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(channel.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(channel.getType()));
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

	protected Channel randomChannel() throws Exception {
		return new Channel() {
			{
				currency = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				name = RandomTestUtil.randomString();
				type = RandomTestUtil.randomString();
			}
		};
	}

	protected Channel randomIrrelevantChannel() throws Exception {
		Channel randomIrrelevantChannel = randomChannel();

		return randomIrrelevantChannel;
	}

	protected Channel randomPatchChannel() throws Exception {
		return randomChannel();
	}

	protected ChannelResource channelResource;
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
		BaseChannelResourceTestCase.class);

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
		com.liferay.headless.commerce.admin.channel.resource.v1_0.
			ChannelResource _channelResource;

}