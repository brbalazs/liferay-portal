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
 * This class is a wrapper for {@link NullConvertibleEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NullConvertibleEntry
 * @generated
 */
public class NullConvertibleEntryWrapper
	implements ModelWrapper<NullConvertibleEntry>, NullConvertibleEntry {

	public NullConvertibleEntryWrapper(
		NullConvertibleEntry nullConvertibleEntry) {

		_nullConvertibleEntry = nullConvertibleEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return NullConvertibleEntry.class;
	}

	@Override
	public String getModelClassName() {
		return NullConvertibleEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("nullConvertibleEntryId", getNullConvertibleEntryId());
		attributes.put("name", getName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long nullConvertibleEntryId = (Long)attributes.get(
			"nullConvertibleEntryId");

		if (nullConvertibleEntryId != null) {
			setNullConvertibleEntryId(nullConvertibleEntryId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}
	}

	@Override
	public Object clone() {
		return new NullConvertibleEntryWrapper(
			(NullConvertibleEntry)_nullConvertibleEntry.clone());
	}

	@Override
	public int compareTo(NullConvertibleEntry nullConvertibleEntry) {
		return _nullConvertibleEntry.compareTo(nullConvertibleEntry);
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _nullConvertibleEntry.getExpandoBridge();
	}

	/**
	 * Returns the name of this null convertible entry.
	 *
	 * @return the name of this null convertible entry
	 */
	@Override
	public String getName() {
		return _nullConvertibleEntry.getName();
	}

	/**
	 * Returns the null convertible entry ID of this null convertible entry.
	 *
	 * @return the null convertible entry ID of this null convertible entry
	 */
	@Override
	public long getNullConvertibleEntryId() {
		return _nullConvertibleEntry.getNullConvertibleEntryId();
	}

	/**
	 * Returns the primary key of this null convertible entry.
	 *
	 * @return the primary key of this null convertible entry
	 */
	@Override
	public long getPrimaryKey() {
		return _nullConvertibleEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _nullConvertibleEntry.getPrimaryKeyObj();
	}

	@Override
	public int hashCode() {
		return _nullConvertibleEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _nullConvertibleEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _nullConvertibleEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _nullConvertibleEntry.isNew();
	}

	@Override
	public void persist() {
		_nullConvertibleEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_nullConvertibleEntry.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_nullConvertibleEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_nullConvertibleEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_nullConvertibleEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the name of this null convertible entry.
	 *
	 * @param name the name of this null convertible entry
	 */
	@Override
	public void setName(String name) {
		_nullConvertibleEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_nullConvertibleEntry.setNew(n);
	}

	/**
	 * Sets the null convertible entry ID of this null convertible entry.
	 *
	 * @param nullConvertibleEntryId the null convertible entry ID of this null convertible entry
	 */
	@Override
	public void setNullConvertibleEntryId(long nullConvertibleEntryId) {
		_nullConvertibleEntry.setNullConvertibleEntryId(nullConvertibleEntryId);
	}

	/**
	 * Sets the primary key of this null convertible entry.
	 *
	 * @param primaryKey the primary key of this null convertible entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_nullConvertibleEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_nullConvertibleEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<NullConvertibleEntry>
		toCacheModel() {

		return _nullConvertibleEntry.toCacheModel();
	}

	@Override
	public NullConvertibleEntry toEscapedModel() {
		return new NullConvertibleEntryWrapper(
			_nullConvertibleEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _nullConvertibleEntry.toString();
	}

	@Override
	public NullConvertibleEntry toUnescapedModel() {
		return new NullConvertibleEntryWrapper(
			_nullConvertibleEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _nullConvertibleEntry.toXmlString();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof NullConvertibleEntryWrapper)) {
			return false;
		}

		NullConvertibleEntryWrapper nullConvertibleEntryWrapper =
			(NullConvertibleEntryWrapper)object;

		if (Objects.equals(
				_nullConvertibleEntry,
				nullConvertibleEntryWrapper._nullConvertibleEntry)) {

			return true;
		}

		return false;
	}

	@Override
	public NullConvertibleEntry getWrappedModel() {
		return _nullConvertibleEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _nullConvertibleEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _nullConvertibleEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_nullConvertibleEntry.resetOriginalValues();
	}

	private final NullConvertibleEntry _nullConvertibleEntry;

}