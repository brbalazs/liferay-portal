/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class BQMembershipChange {

	public BQMembershipChange() {
	}

	public BQMembershipChange(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQMembershipChange)) {
			return false;
		}

		BQMembershipChange bqMembershipChange = (BQMembershipChange)obj;

		if (Objects.equals(_createDate, bqMembershipChange._createDate) &&
			Objects.equals(
				_identitiesCount, bqMembershipChange._identitiesCount) &&
			Objects.equals(
				_individualsCount, bqMembershipChange._individualsCount) &&
			Objects.equals(_segmentId, bqMembershipChange._segmentId)) {

			return true;
		}

		return false;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public Long getIdentitiesCount() {
		return _identitiesCount;
	}

	public Long getIndividualsCount() {
		return _individualsCount;
	}

	public Long getSegmentId() {
		return _segmentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_identitiesCount, _individualsCount, _createDate, _segmentId);
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setIdentitiesCount(Long identitiesCount) {
		_identitiesCount = identitiesCount;
	}

	public void setIndividualsCount(Long individualsCount) {
		_individualsCount = individualsCount;
	}

	public void setSegmentId(Long segmentId) {
		_segmentId = segmentId;
	}

	private Date _createDate;
	private Long _identitiesCount;
	private Long _individualsCount;
	private Long _segmentId;

}