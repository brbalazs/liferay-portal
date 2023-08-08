/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@Table
public class Channel implements Persistable<Long> {

	public Channel() {
	}

	public Channel(String name) {
		_name = name;
	}

	public void addChannelDataSource(ChannelDataSource channelDataSource) {
		_channelDataSources.add(channelDataSource);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Channel)) {
			return false;
		}

		Channel channel = (Channel)obj;

		if (Objects.equals(_channelDataSources, channel._channelDataSources) &&
			Objects.equals(_createDate, channel._createDate) &&
			Objects.equals(_id, channel._id) &&
			Objects.equals(_name, channel._name) &&
			Objects.equals(_state, channel._state)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dataSources")
	@MappedCollection(idColumn = "channelid")
	public Set<ChannelDataSource> getChannelDataSources() {
		return _channelDataSources;
	}

	@AccessType(AccessType.Type.PROPERTY)
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

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getDefaultChannel() {
		return _defaultChannel;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getState() {
		return _state;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelDataSources, _createDate, _id, _name, _state);
	}

	public Boolean isDefaultChannel() {
		return getDefaultChannel();
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setChannelDataSources(
		Set<ChannelDataSource> channelDataSources) {

		_channelDataSources = channelDataSources;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDefaultChannel(Boolean defaultChannel) {
		_defaultChannel = defaultChannel;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setState(String state) {
		_state = state;
	}

	@Transient
	private Set<ChannelDataSource> _channelDataSources = new HashSet<>();

	@Transient
	private Date _createDate = new Date();

	@Transient
	private Boolean _defaultChannel;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _name;

	@Transient
	private String _state = "READY";

}