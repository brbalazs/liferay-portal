/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class InterestTopic implements Persistable<Long> {

	public InterestTopic() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof InterestTopic)) {
			return false;
		}

		InterestTopic interestTopic = (InterestTopic)obj;

		if (Objects.equals(_id, interestTopic._id) &&
			Objects.equals(_term, interestTopic._term) &&
			Objects.equals(_termType, interestTopic._termType) &&
			Objects.equals(_termWeight, interestTopic._termWeight) &&
			Objects.equals(_topic, interestTopic._topic) &&
			Objects.equals(_topicWeight, interestTopic._topicWeight)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTerm() {
		return _term;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTermType() {
		return _termType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getTermWeight() {
		return _termWeight;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Integer getTopic() {
		return _topic;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getTopicWeight() {
		return _topicWeight;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_id, _term, _termType, _termWeight, _topic, _topicWeight);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setTerm(String term) {
		_term = term;
	}

	public void setTermType(String termType) {
		_termType = termType;
	}

	public void setTermWeight(Double termWeight) {
		_termWeight = termWeight;
	}

	public void setTopic(Integer topic) {
		_topic = topic;
	}

	public void setTopicWeight(Double topicWeight) {
		_topicWeight = topicWeight;
	}

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _term;

	@Transient
	private String _termType;

	@Transient
	private Double _termWeight;

	@Transient
	private Integer _topic;

	@Transient
	private Double _topicWeight;

}