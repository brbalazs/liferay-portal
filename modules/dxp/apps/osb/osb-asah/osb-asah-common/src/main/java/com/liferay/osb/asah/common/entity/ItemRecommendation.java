/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
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
public class ItemRecommendation implements Persistable<String> {

	public ItemRecommendation() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ItemRecommendation)) {
			return false;
		}

		ItemRecommendation itemRecommendation = (ItemRecommendation)obj;

		if (Objects.equals(_id, itemRecommendation._id) &&
			Objects.equals(_itemId, itemRecommendation._itemId) &&
			Objects.equals(_jobId, itemRecommendation._jobId) &&
			Objects.equals(
				_recommendedItemIds, itemRecommendation._recommendedItemIds)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public String getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getItemId() {
		return _itemId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonDeserialize(as = Long.class)
	public Long getJobId() {
		return _jobId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public List<String> getRecommendedItemIds() {
		return _recommendedItemIds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id, _itemId, _jobId, _recommendedItemIds);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setItemId(String itemId) {
		_itemId = itemId;
	}

	public void setJobId(Long jobId) {
		_jobId = jobId;
	}

	public void setRecommendedItemIds(List<String> recommendedItemIds) {
		_recommendedItemIds = recommendedItemIds;
	}

	@Transient
	private String _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _itemId;

	@Transient
	private Long _jobId;

	@Transient
	private List<String> _recommendedItemIds;

}