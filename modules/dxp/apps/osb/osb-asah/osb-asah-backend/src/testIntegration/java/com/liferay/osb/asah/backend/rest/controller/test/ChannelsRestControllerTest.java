/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.backend.rest.controller.DataSourcesRestController;
import com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.ChannelsRestController;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.http.ChannelHttp;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Geyson Silva
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ResetMocksTestExecutionListener.class
	}
)
public class ChannelsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testDuplicateChannelName() {
		JSONObject dataSourceJSONObject = new JSONObject(
			_dataSourcesRestController.postDataSource(
				new DataSourceDTO(
					FaroInfoTestUtil.buildLiferayDataSource(
						"Liferay", RandomTestUtil.randomURL()))));

		JSONObject channelJSONObject = FaroInfoTestUtil.buildChannelJSONObject(
			dataSourceJSONObject.getString("id"), "combined");

		for (int i = 0; i < 4; i++) {
			_channelsRestController.postChannels(
				String.valueOf(channelJSONObject));
		}

		JSONObject channelsJSONObject = _objectMapper.convertValue(
			_channelsRestController.getChannelDTOPageDTO(null, 0, 20, null),
			JSONObject.class);

		JSONArray channelsJSONArray = (JSONArray)channelsJSONObject.query(
			"/_embedded/channels");

		Assertions.assertEquals(5, channelsJSONArray.length());

		Set<String> channelNames = new HashSet<>();

		for (int i = 0; i < channelsJSONArray.length(); i++) {
			channelJSONObject = channelsJSONArray.getJSONObject(i);

			channelNames.add(channelJSONObject.getString("name"));
		}

		Assertions.assertTrue(
			channelNames.contains("Liferay Combined Property"));
		Assertions.assertTrue(
			channelNames.contains("Liferay Combined Property (1)"));
		Assertions.assertTrue(
			channelNames.contains("Liferay Combined Property (2)"));
		Assertions.assertTrue(
			channelNames.contains("Liferay Combined Property (3)"));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_1.json"
	)
	@Test
	public void testGetChannels() {
		JSONObject channelsJSONObject = _objectMapper.convertValue(
			_channelsRestController.getChannelDTOPageDTO(null, 0, 20, null),
			JSONObject.class);

		JSONArray channelsJSONArray = (JSONArray)channelsJSONObject.query(
			"/_embedded/channels");

		Assertions.assertEquals(3, channelsJSONArray.length());
	}

	@Test
	public void testHandleFaroFailure() {
		Mockito.doThrow(
			new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
		).when(
			_channelHttp
		).addChannel(
			ArgumentMatchers.any(Channel.class)
		);

		try {
			JSONObject dataSourceJSONObject = new JSONObject(
				_dataSourcesRestController.postDataSource(
					_objectMapper.convertValue(
						FaroInfoTestUtil.buildLiferayDataSource(),
						DataSourceDTO.class)));

			new JSONArray(
				_channelsRestController.postChannels(
					String.valueOf(
						FaroInfoTestUtil.buildChannelJSONObject(
							dataSourceJSONObject.getString("id"),
							"combined"))));
		}
		catch (OSBAsahException osbAsahException) {
			Assertions.assertEquals(
				"Unable to create channel", osbAsahException.getMessage());

			Assertions.assertEquals(0, _channelRepository.count());

			return;
		}

		Assertions.fail("postChannels did not throw an exception");
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_1.json"
	)
	@Test
	public void testPatchChannelExistingDataSource() {
		String dataSourceId = "351238757269547424";

		JSONArray channelsJSONArray = _objectMapper.convertValue(
			_channelsRestController.postChannels(
				String.valueOf(
					FaroInfoTestUtil.buildChannelJSONObject(
						dataSourceId, "combined"))),
			JSONArray.class);

		JSONObject channelJSONObject = channelsJSONArray.getJSONObject(0);

		String randomGroupId = RandomTestUtil.randomId();

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_channelsRestController.patchChannel(
				channelJSONObject.getLong("id"),
				JSONUtil.put(
					"dataSourceId", dataSourceId
				).put(
					"groups",
					Collections.singleton(
						JSONUtil.put(
							"id", randomGroupId
						).put(
							"name", RandomTestUtil.randomString()
						))
				).toString()),
			JSONObject.class);

		JSONObject actualChannelJSONObject = responseJSONObject.getJSONObject(
			"channel");

		JSONArray dataSourcesJSONArray = actualChannelJSONObject.getJSONArray(
			"dataSources");

		JSONObject dataSourceJSONObject = dataSourcesJSONArray.getJSONObject(0);

		JSONArray groupIdsJSONArray = dataSourceJSONObject.getJSONArray(
			"groupIds");

		Assertions.assertEquals(randomGroupId, groupIdsJSONArray.getString(0));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_1.json"
	)
	@Test
	public void testPatchChannelNewDataSource() {
		String dataSourceId = RandomTestUtil.randomId();
		String randomGroupId = RandomTestUtil.randomId();

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_channelsRestController.patchChannel(
				123456789L,
				JSONUtil.put(
					"dataSourceId", dataSourceId
				).put(
					"groups",
					Collections.singleton(
						JSONUtil.put(
							"id", randomGroupId
						).put(
							"name", RandomTestUtil.randomString()
						))
				).toString()),
			JSONObject.class);

		JSONObject actualChannelJSONObject = responseJSONObject.getJSONObject(
			"channel");

		JSONArray dataSourcesJSONArray = actualChannelJSONObject.getJSONArray(
			"dataSources");

		Assertions.assertEquals(2, dataSourcesJSONArray.length());

		Optional<JSONObject> dataSourceOptional = IntStream.range(
			0, dataSourcesJSONArray.length()
		).mapToObj(
			dataSourcesJSONArray::getJSONObject
		).filter(
			dataSourceJSONObject -> dataSourceId.equals(
				dataSourceJSONObject.getString("id"))
		).findFirst();

		Assertions.assertTrue(dataSourceOptional.isPresent());

		JSONObject dataSourceJSONObject = dataSourceOptional.get();

		JSONArray groupIdsJSONArray = dataSourceJSONObject.getJSONArray(
			"groupIds");

		Assertions.assertEquals(randomGroupId, groupIdsJSONArray.getString(0));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_1.json"
	)
	@Test
	public void testPatchChannelRename() {
		String randomName = RandomTestUtil.randomString();

		JSONObject inputChannelJSONObject = JSONUtil.put("name", randomName);

		JSONObject responseJSONObject = _objectMapper.convertValue(
			_channelsRestController.patchChannel(
				123456789L, inputChannelJSONObject.toString()),
			JSONObject.class);

		JSONObject actualChannelJSONObject = responseJSONObject.getJSONObject(
			"channel");

		Assertions.assertEquals(
			randomName, actualChannelJSONObject.getString("name"));
	}

	@Test
	public void testPostCombinedChannels() throws Exception {
		JSONObject dataSourceJSONObject = new JSONObject(
			_dataSourcesRestController.postDataSource(
				_objectMapper.convertValue(
					FaroInfoTestUtil.buildLiferayDataSource(),
					DataSourceDTO.class)));

		JSONArray channelsJSONArray = new JSONArray(
			_channelsRestController.postChannels(
				String.valueOf(
					FaroInfoTestUtil.buildChannelJSONObject(
						dataSourceJSONObject.getString("id"), "combined"))));

		Assertions.assertEquals(1, channelsJSONArray.length());

		Mockito.verify(
			_channelHttp, Mockito.times(2)
		).addChannel(
			ArgumentMatchers.any(Channel.class)
		);
	}

	@Test
	public void testPostMultipleChannels() {
		JSONObject dataSourceJSONObject = new JSONObject(
			_dataSourcesRestController.postDataSource(
				_objectMapper.convertValue(
					FaroInfoTestUtil.buildLiferayDataSource(),
					DataSourceDTO.class)));

		JSONArray channelsJSONArray = new JSONArray(
			_channelsRestController.postChannels(
				String.valueOf(
					FaroInfoTestUtil.buildChannelJSONObject(
						dataSourceJSONObject.getString("id"), "multiple"))));

		Assertions.assertEquals(2, channelsJSONArray.length());

		Mockito.verify(
			_channelHttp, Mockito.times(3)
		).addChannel(
			ArgumentMatchers.any(Channel.class)
		);
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels_1.json"
	)
	@Test
	public void testRemovedGroupIds() {
		JSONObject responseJSONObject = _objectMapper.convertValue(
			_channelsRestController.patchChannel(
				4324324324L,
				JSONUtil.put(
					"dataSourceId", "123456789"
				).put(
					"groups",
					Collections.singleton(
						JSONUtil.put(
							"id", RandomTestUtil.randomId()
						).put(
							"name", RandomTestUtil.randomString()
						))
				).toString()),
			JSONObject.class);

		JSONArray removedGroupIdsJSONArray = responseJSONObject.optJSONArray(
			"removedGroupIds");

		Assertions.assertEquals(2, removedGroupIdsJSONArray.length());

		Set<String> removedGroupIds = JSONUtil.toStringSet(
			removedGroupIdsJSONArray);

		Assertions.assertTrue(removedGroupIds.contains("123456"));
		Assertions.assertTrue(removedGroupIds.contains("654321"));
	}

	@MockBean
	private ChannelHttp _channelHttp;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private ChannelsRestController _channelsRestController;

	@Autowired
	private DataSourcesRestController _dataSourcesRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}