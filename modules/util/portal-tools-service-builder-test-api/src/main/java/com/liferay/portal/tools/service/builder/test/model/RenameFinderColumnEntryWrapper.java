/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.tools.service.builder.test.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link RenameFinderColumnEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RenameFinderColumnEntry
 * @generated
 */
public class RenameFinderColumnEntryWrapper
	implements ModelWrapper<RenameFinderColumnEntry>, RenameFinderColumnEntry {

	public RenameFinderColumnEntryWrapper(
		RenameFinderColumnEntry renameFinderColumnEntry) {

		_renameFinderColumnEntry = renameFinderColumnEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return RenameFinderColumnEntry.class;
	}

	@Override
	public String getModelClassName() {
		return RenameFinderColumnEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(
			"renameFinderColumnEntryId", getRenameFinderColumnEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("renamedColumn", getRenamedColumn());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long renameFinderColumnEntryId = (Long)attributes.get(
			"renameFinderColumnEntryId");

		if (renameFinderColumnEntryId != null) {
			setRenameFinderColumnEntryId(renameFinderColumnEntryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		String renamedColumn = (String)attributes.get("renamedColumn");

		if (renamedColumn != null) {
			setRenamedColumn(renamedColumn);
		}
	}

	@Override
	public Object clone() {
		return new RenameFinderColumnEntryWrapper(
			(RenameFinderColumnEntry)_renameFinderColumnEntry.clone());
	}

	@Override
	public int compareTo(RenameFinderColumnEntry renameFinderColumnEntry) {
		return _renameFinderColumnEntry.compareTo(renameFinderColumnEntry);
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _renameFinderColumnEntry.getExpandoBridge();
	}

	/**
	 * Returns the group ID of this rename finder column entry.
	 *
	 * @return the group ID of this rename finder column entry
	 */
	@Override
	public long getGroupId() {
		return _renameFinderColumnEntry.getGroupId();
	}

	/**
	 * Returns the primary key of this rename finder column entry.
	 *
	 * @return the primary key of this rename finder column entry
	 */
	@Override
	public long getPrimaryKey() {
		return _renameFinderColumnEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _renameFinderColumnEntry.getPrimaryKeyObj();
	}

	/**
	 * Returns the renamed column of this rename finder column entry.
	 *
	 * @return the renamed column of this rename finder column entry
	 */
	@Override
	public String getRenamedColumn() {
		return _renameFinderColumnEntry.getRenamedColumn();
	}

	/**
	 * Returns the rename finder column entry ID of this rename finder column entry.
	 *
	 * @return the rename finder column entry ID of this rename finder column entry
	 */
	@Override
	public long getRenameFinderColumnEntryId() {
		return _renameFinderColumnEntry.getRenameFinderColumnEntryId();
	}

	@Override
	public int hashCode() {
		return _renameFinderColumnEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _renameFinderColumnEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _renameFinderColumnEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _renameFinderColumnEntry.isNew();
	}

	@Override
	public void persist() {
		_renameFinderColumnEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_renameFinderColumnEntry.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_renameFinderColumnEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_renameFinderColumnEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_renameFinderColumnEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the group ID of this rename finder column entry.
	 *
	 * @param groupId the group ID of this rename finder column entry
	 */
	@Override
	public void setGroupId(long groupId) {
		_renameFinderColumnEntry.setGroupId(groupId);
	}

	@Override
	public void setNew(boolean n) {
		_renameFinderColumnEntry.setNew(n);
	}

	/**
	 * Sets the primary key of this rename finder column entry.
	 *
	 * @param primaryKey the primary key of this rename finder column entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_renameFinderColumnEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_renameFinderColumnEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the renamed column of this rename finder column entry.
	 *
	 * @param renamedColumn the renamed column of this rename finder column entry
	 */
	@Override
	public void setRenamedColumn(String renamedColumn) {
		_renameFinderColumnEntry.setRenamedColumn(renamedColumn);
	}

	/**
	 * Sets the rename finder column entry ID of this rename finder column entry.
	 *
	 * @param renameFinderColumnEntryId the rename finder column entry ID of this rename finder column entry
	 */
	@Override
	public void setRenameFinderColumnEntryId(long renameFinderColumnEntryId) {
		_renameFinderColumnEntry.setRenameFinderColumnEntryId(
			renameFinderColumnEntryId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<RenameFinderColumnEntry>
		toCacheModel() {

		return _renameFinderColumnEntry.toCacheModel();
	}

	@Override
	public RenameFinderColumnEntry toEscapedModel() {
		return new RenameFinderColumnEntryWrapper(
			_renameFinderColumnEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _renameFinderColumnEntry.toString();
	}

	@Override
	public RenameFinderColumnEntry toUnescapedModel() {
		return new RenameFinderColumnEntryWrapper(
			_renameFinderColumnEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _renameFinderColumnEntry.toXmlString();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof RenameFinderColumnEntryWrapper)) {
			return false;
		}

		RenameFinderColumnEntryWrapper renameFinderColumnEntryWrapper =
			(RenameFinderColumnEntryWrapper)object;

		if (Objects.equals(
				_renameFinderColumnEntry,
				renameFinderColumnEntryWrapper._renameFinderColumnEntry)) {

			return true;
		}

		return false;
	}

	@Override
	public RenameFinderColumnEntry getWrappedModel() {
		return _renameFinderColumnEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _renameFinderColumnEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _renameFinderColumnEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_renameFinderColumnEntry.resetOriginalValues();
	}

	private final RenameFinderColumnEntry _renameFinderColumnEntry;

}