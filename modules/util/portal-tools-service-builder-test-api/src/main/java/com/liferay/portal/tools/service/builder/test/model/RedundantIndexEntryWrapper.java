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
 * This class is a wrapper for {@link RedundantIndexEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RedundantIndexEntry
 * @generated
 */
public class RedundantIndexEntryWrapper
	implements ModelWrapper<RedundantIndexEntry>, RedundantIndexEntry {

	public RedundantIndexEntryWrapper(RedundantIndexEntry redundantIndexEntry) {
		_redundantIndexEntry = redundantIndexEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return RedundantIndexEntry.class;
	}

	@Override
	public String getModelClassName() {
		return RedundantIndexEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("redundantIndexEntryId", getRedundantIndexEntryId());
		attributes.put("companyId", getCompanyId());
		attributes.put("name", getName());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long redundantIndexEntryId = (Long)attributes.get(
			"redundantIndexEntryId");

		if (redundantIndexEntryId != null) {
			setRedundantIndexEntryId(redundantIndexEntryId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}
	}

	@Override
	public Object clone() {
		return new RedundantIndexEntryWrapper(
			(RedundantIndexEntry)_redundantIndexEntry.clone());
	}

	@Override
	public int compareTo(RedundantIndexEntry redundantIndexEntry) {
		return _redundantIndexEntry.compareTo(redundantIndexEntry);
	}

	/**
	 * Returns the company ID of this redundant index entry.
	 *
	 * @return the company ID of this redundant index entry
	 */
	@Override
	public long getCompanyId() {
		return _redundantIndexEntry.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _redundantIndexEntry.getExpandoBridge();
	}

	/**
	 * Returns the name of this redundant index entry.
	 *
	 * @return the name of this redundant index entry
	 */
	@Override
	public String getName() {
		return _redundantIndexEntry.getName();
	}

	/**
	 * Returns the primary key of this redundant index entry.
	 *
	 * @return the primary key of this redundant index entry
	 */
	@Override
	public long getPrimaryKey() {
		return _redundantIndexEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _redundantIndexEntry.getPrimaryKeyObj();
	}

	/**
	 * Returns the redundant index entry ID of this redundant index entry.
	 *
	 * @return the redundant index entry ID of this redundant index entry
	 */
	@Override
	public long getRedundantIndexEntryId() {
		return _redundantIndexEntry.getRedundantIndexEntryId();
	}

	@Override
	public int hashCode() {
		return _redundantIndexEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _redundantIndexEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _redundantIndexEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _redundantIndexEntry.isNew();
	}

	@Override
	public void persist() {
		_redundantIndexEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_redundantIndexEntry.setCachedModel(cachedModel);
	}

	/**
	 * Sets the company ID of this redundant index entry.
	 *
	 * @param companyId the company ID of this redundant index entry
	 */
	@Override
	public void setCompanyId(long companyId) {
		_redundantIndexEntry.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_redundantIndexEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_redundantIndexEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_redundantIndexEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the name of this redundant index entry.
	 *
	 * @param name the name of this redundant index entry
	 */
	@Override
	public void setName(String name) {
		_redundantIndexEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_redundantIndexEntry.setNew(n);
	}

	/**
	 * Sets the primary key of this redundant index entry.
	 *
	 * @param primaryKey the primary key of this redundant index entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_redundantIndexEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_redundantIndexEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the redundant index entry ID of this redundant index entry.
	 *
	 * @param redundantIndexEntryId the redundant index entry ID of this redundant index entry
	 */
	@Override
	public void setRedundantIndexEntryId(long redundantIndexEntryId) {
		_redundantIndexEntry.setRedundantIndexEntryId(redundantIndexEntryId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<RedundantIndexEntry>
		toCacheModel() {

		return _redundantIndexEntry.toCacheModel();
	}

	@Override
	public RedundantIndexEntry toEscapedModel() {
		return new RedundantIndexEntryWrapper(
			_redundantIndexEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _redundantIndexEntry.toString();
	}

	@Override
	public RedundantIndexEntry toUnescapedModel() {
		return new RedundantIndexEntryWrapper(
			_redundantIndexEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _redundantIndexEntry.toXmlString();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof RedundantIndexEntryWrapper)) {
			return false;
		}

		RedundantIndexEntryWrapper redundantIndexEntryWrapper =
			(RedundantIndexEntryWrapper)object;

		if (Objects.equals(
				_redundantIndexEntry,
				redundantIndexEntryWrapper._redundantIndexEntry)) {

			return true;
		}

		return false;
	}

	@Override
	public RedundantIndexEntry getWrappedModel() {
		return _redundantIndexEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _redundantIndexEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _redundantIndexEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_redundantIndexEntry.resetOriginalValues();
	}

	private final RedundantIndexEntry _redundantIndexEntry;

}