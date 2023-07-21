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
 * This class is a wrapper for {@link CacheFieldEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CacheFieldEntry
 * @generated
 */
public class CacheFieldEntryWrapper
	implements CacheFieldEntry, ModelWrapper<CacheFieldEntry> {

	public CacheFieldEntryWrapper(CacheFieldEntry cacheFieldEntry) {
		_cacheFieldEntry = cacheFieldEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return CacheFieldEntry.class;
	}

	@Override
	public String getModelClassName() {
		return CacheFieldEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("cacheFieldEntryId", getCacheFieldEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("name", getName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long cacheFieldEntryId = (Long)attributes.get("cacheFieldEntryId");

		if (cacheFieldEntryId != null) {
			setCacheFieldEntryId(cacheFieldEntryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}
	}

	@Override
	public Object clone() {
		return new CacheFieldEntryWrapper(
			(CacheFieldEntry)_cacheFieldEntry.clone());
	}

	@Override
	public int compareTo(CacheFieldEntry cacheFieldEntry) {
		return _cacheFieldEntry.compareTo(cacheFieldEntry);
	}

	/**
	 * Returns the cache field entry ID of this cache field entry.
	 *
	 * @return the cache field entry ID of this cache field entry
	 */
	@Override
	public long getCacheFieldEntryId() {
		return _cacheFieldEntry.getCacheFieldEntryId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _cacheFieldEntry.getExpandoBridge();
	}

	/**
	 * Returns the group ID of this cache field entry.
	 *
	 * @return the group ID of this cache field entry
	 */
	@Override
	public long getGroupId() {
		return _cacheFieldEntry.getGroupId();
	}

	/**
	 * Returns the name of this cache field entry.
	 *
	 * @return the name of this cache field entry
	 */
	@Override
	public String getName() {
		return _cacheFieldEntry.getName();
	}

	@Override
	public String getNickname() {
		return _cacheFieldEntry.getNickname();
	}

	/**
	 * Returns the primary key of this cache field entry.
	 *
	 * @return the primary key of this cache field entry
	 */
	@Override
	public long getPrimaryKey() {
		return _cacheFieldEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _cacheFieldEntry.getPrimaryKeyObj();
	}

	@Override
	public int hashCode() {
		return _cacheFieldEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _cacheFieldEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _cacheFieldEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _cacheFieldEntry.isNew();
	}

	@Override
	public void persist() {
		_cacheFieldEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_cacheFieldEntry.setCachedModel(cachedModel);
	}

	/**
	 * Sets the cache field entry ID of this cache field entry.
	 *
	 * @param cacheFieldEntryId the cache field entry ID of this cache field entry
	 */
	@Override
	public void setCacheFieldEntryId(long cacheFieldEntryId) {
		_cacheFieldEntry.setCacheFieldEntryId(cacheFieldEntryId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_cacheFieldEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_cacheFieldEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_cacheFieldEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the group ID of this cache field entry.
	 *
	 * @param groupId the group ID of this cache field entry
	 */
	@Override
	public void setGroupId(long groupId) {
		_cacheFieldEntry.setGroupId(groupId);
	}

	/**
	 * Sets the name of this cache field entry.
	 *
	 * @param name the name of this cache field entry
	 */
	@Override
	public void setName(String name) {
		_cacheFieldEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_cacheFieldEntry.setNew(n);
	}

	@Override
	public void setNickname(String nickname) {
		_cacheFieldEntry.setNickname(nickname);
	}

	/**
	 * Sets the primary key of this cache field entry.
	 *
	 * @param primaryKey the primary key of this cache field entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_cacheFieldEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_cacheFieldEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<CacheFieldEntry>
		toCacheModel() {

		return _cacheFieldEntry.toCacheModel();
	}

	@Override
	public CacheFieldEntry toEscapedModel() {
		return new CacheFieldEntryWrapper(_cacheFieldEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _cacheFieldEntry.toString();
	}

	@Override
	public CacheFieldEntry toUnescapedModel() {
		return new CacheFieldEntryWrapper(_cacheFieldEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _cacheFieldEntry.toXmlString();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CacheFieldEntryWrapper)) {
			return false;
		}

		CacheFieldEntryWrapper cacheFieldEntryWrapper =
			(CacheFieldEntryWrapper)object;

		if (Objects.equals(
				_cacheFieldEntry, cacheFieldEntryWrapper._cacheFieldEntry)) {

			return true;
		}

		return false;
	}

	@Override
	public CacheFieldEntry getWrappedModel() {
		return _cacheFieldEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _cacheFieldEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _cacheFieldEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_cacheFieldEntry.resetOriginalValues();
	}

	private final CacheFieldEntry _cacheFieldEntry;

}