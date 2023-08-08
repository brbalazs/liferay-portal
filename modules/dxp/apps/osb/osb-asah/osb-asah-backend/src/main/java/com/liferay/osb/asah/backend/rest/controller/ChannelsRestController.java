/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.ChannelDTO;
import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Geyson Silva
 * @author André Miranda
 */
@RequestMapping("/channels")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.ChannelsRestController"
)
public class ChannelsRestController extends BaseRestController {

	@CacheEvict("getGraphQLExecutionResult")
	@PostMapping("/clear")
	public void clearChannel(@RequestBody List<String> ids) {
		_asahTaskDog.scheduleAsahTask(
			"ClearChannelsNanite", JSONUtil.put("channelIds", ids));
	}

	@DeleteMapping
	public void deleteChannels(@RequestBody List<String> ids) {
		_channelDog.updateState(
			ListUtil.map(ids, Long::valueOf), "IN_PROGRESS_DELETING");

		_asahTaskDog.scheduleAsahTask(
			"DeleteChannelsNanite", JSONUtil.put("channelIds", ids));
	}

	@GetMapping("/{id}")
	public ChannelDTO getChannelDTO(
		@PathVariable Long id, @RequestParam(required = false) String expand) {

		Channel channel = _channelDog.getChannel(id);

		if (StringUtils.isEmpty(expand)) {
			return new ChannelDTO(channel);
		}

		List<DataSource> dataSources = _dataSourceDog.getDataSources(
			ListUtil.map(
				channel.getChannelDataSources(),
				ChannelDataSource::getDataSourceId));

		return new ChannelDTO(
			channel,
			Collections.singletonMap(
				"data-sources", SetUtil.map(dataSources, DataSourceDTO::new)));
	}

	@GetMapping
	public PageDTO<ChannelDTO> getChannelDTOPageDTO(
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return _toPageDTO(
			_channelDog.getChannelPage(filterString, page, size, sorts));
	}

	@PatchMapping("/{id}")
	public ChannelDTO patchChannel(
		@PathVariable Long id, @RequestBody String json) {

		Set<Long> groupIds = new HashSet<>();

		JSONObject jsonObject = new JSONObject(json);

		JSONArray groupsJSONArray = jsonObject.optJSONArray("groups");

		if (groupsJSONArray != null) {
			for (int i = 0; i < groupsJSONArray.length(); i++) {
				JSONObject groupJSONObject = groupsJSONArray.getJSONObject(i);

				groupIds.add(Long.valueOf(groupJSONObject.getString("id")));
			}
		}

		Set<Long> removedGroupIds = _channelDog.getRemovedGroupIds(
			id,
			NumberUtils.createLong(jsonObject.optString("dataSourceId", null)),
			groupIds);

		return new ChannelDTO(
			_channelDog.patchChannel(
				id,
				NumberUtils.createLong(
					jsonObject.optString("dataSourceId", null)),
				groupIds, jsonObject.optString("name")),
			removedGroupIds);
	}

	@PostMapping
	public ChannelDTO postChannel(@RequestBody String json) {
		JSONObject jsonObject = new JSONObject(json);

		return new ChannelDTO(
			_channelDog.addChannel(jsonObject.getString("name")));
	}

	private PageDTO<ChannelDTO> _toPageDTO(Page<Channel> channelsPage) {
		return new PageDTO<>(
			"_embedded", new ChannelDTO(channelsPage.getContent()),
			channelsPage.getNumber(), channelsPage.getSize(),
			channelsPage.getTotalElements(), channelsPage.getTotalPages());
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

}