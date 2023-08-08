/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Inácio Nery
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("channels")
public class ChannelDTO {

	public ChannelDTO() {
	}

	public ChannelDTO(Channel channel) {
		_channelDataSourceDTOs = SetUtil.map(
			channel.getChannelDataSources(), ChannelDataSourceDTO::new);
		_createDate = channel.getCreateDate();
		_id = StringUtil.get(channel.getId(), null);
		_name = channel.getName();
	}

	public ChannelDTO(
		Channel channel, Map<String, Set<DataSourceDTO>> dataSourceDTOs) {

		this(channel);

		_dataSourceDTOs = dataSourceDTOs;
	}

	public ChannelDTO(Channel channel, Set<Long> removedGroupIds) {
		_channelDTO = new ChannelDTO(channel);

		_removedGroupIds = SetUtil.map(removedGroupIds, String::valueOf);
	}

	public ChannelDTO(List<Channel> channels) {
		_channelDTOs = SetUtil.map(channels, ChannelDTO::new);
	}

	@JsonProperty("dataSources")
	public Set<ChannelDataSourceDTO> getChannelDataSourceDTOs() {
		return _channelDataSourceDTOs;
	}

	@JsonProperty("channel")
	public ChannelDTO getChannelDTO() {
		return _channelDTO;
	}

	@JsonProperty("channels")
	public Set<ChannelDTO> getChannelDTOs() {
		return _channelDTOs;
	}

	@JsonAlias("dateCreated")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createDate")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonProperty("_embedded")
	public Map<String, Set<DataSourceDTO>> getDataSourceDTOs() {
		return _dataSourceDTOs;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("removedGroupIds")
	public Set<String> getRemovedGroupIds() {
		return _removedGroupIds;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ChannelDataSourceDTO {

		public ChannelDataSourceDTO() {
		}

		public ChannelDataSourceDTO(ChannelDataSource channelDataSource) {
			_commerceChannelIds = SetUtil.map(
				channelDataSource.getCommerceChannelIds(), String::valueOf);
			_dataSourceId = StringUtil.get(
				channelDataSource.getDataSourceId(), null);
			_groupIds = SetUtil.map(
				channelDataSource.getGroupIds(), String::valueOf);
		}

		@JsonProperty("commerceChannelIds")
		public Set<String> getCommerceChannelIds() {
			return _commerceChannelIds;
		}

		@JsonProperty("id")
		public String getDataSourceId() {
			return _dataSourceId;
		}

		@JsonProperty("groupIds")
		public Set<String> getGroupIds() {
			return _groupIds;
		}

		private Set<String> _commerceChannelIds;
		private String _dataSourceId;
		private Set<String> _groupIds;

	}

	private Set<ChannelDataSourceDTO> _channelDataSourceDTOs;
	private ChannelDTO _channelDTO;
	private Set<ChannelDTO> _channelDTOs;
	private Date _createDate;
	private Map<String, Set<DataSourceDTO>> _dataSourceDTOs;
	private String _id;
	private String _name;
	private Set<String> _removedGroupIds;

}