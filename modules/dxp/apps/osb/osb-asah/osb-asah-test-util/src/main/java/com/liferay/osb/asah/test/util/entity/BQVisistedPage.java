/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.common.base.Objects;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Robson Pastor
 */
@Table
public class BQVisistedPage implements Persistable<Long> {

	public BQVisistedPage() {
	}

	public BQVisistedPage(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof BQVisistedPage)) {
			return false;
		}

		BQVisistedPage bqVisistedPage = (BQVisistedPage)obj;

		if (Objects.equal(_canonicalUrl, bqVisistedPage._canonicalUrl) &&
			Objects.equal(_dayDate, bqVisistedPage._dayDate) &&
			Objects.equal(_description, bqVisistedPage._description) &&
			Objects.equal(_interestName, bqVisistedPage._interestName) &&
			Objects.equal(_ownerId, bqVisistedPage._ownerId) &&
			Objects.equal(_ownerType, bqVisistedPage._ownerType) &&
			Objects.equal(_title, bqVisistedPage._title) &&
			Objects.equal(
				_uniqueVisitsCount, bqVisistedPage._uniqueVisitsCount) &&
			Objects.equal(_url, bqVisistedPage._url)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getDayDate() {
		if (_dayDate == null) {
			return null;
		}

		return new Date(_dayDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDescription() {
		return _description;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getInterestName() {
		return _interestName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getOwnerId() {
		return _ownerId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getOwnerType() {
		return _ownerType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTitle() {
		return _title;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getUniqueVisitsCount() {
		return _uniqueVisitsCount;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getUrl() {
		return _url;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			_canonicalUrl, _dayDate, _description, _interestName, _ownerId,
			_ownerType, _title, _uniqueVisitsCount, _url);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setDayDate(Date dayDate) {
		if (dayDate != null) {
			_dayDate = new Date(dayDate.getTime());
		}
		else {
			_dayDate = null;
		}
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setInterestName(String interestName) {
		_interestName = interestName;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setOwnerType(String ownerType) {
		_ownerType = ownerType;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUniqueVisitsCount(Long uniqueVisitsCount) {
		_uniqueVisitsCount = uniqueVisitsCount;
	}

	public void setUrl(String url) {
		_url = url;
	}

	@Transient
	private String _canonicalUrl;

	@Transient
	private Date _dayDate;

	@Transient
	private String _description;

	@Transient
	private Long _id;

	@Transient
	private String _interestName;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _ownerId;

	@Transient
	private String _ownerType;

	@Transient
	private String _title;

	@Transient
	private Long _uniqueVisitsCount;

	@Transient
	private String _url;

}