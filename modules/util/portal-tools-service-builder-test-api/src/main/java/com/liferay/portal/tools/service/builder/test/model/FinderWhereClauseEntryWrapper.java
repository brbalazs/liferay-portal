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
 * This class is a wrapper for {@link FinderWhereClauseEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FinderWhereClauseEntry
 * @generated
 */
public class FinderWhereClauseEntryWrapper
	implements FinderWhereClauseEntry, ModelWrapper<FinderWhereClauseEntry> {

	public FinderWhereClauseEntryWrapper(
		FinderWhereClauseEntry finderWhereClauseEntry) {

		_finderWhereClauseEntry = finderWhereClauseEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return FinderWhereClauseEntry.class;
	}

	@Override
	public String getModelClassName() {
		return FinderWhereClauseEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(
			"finderWhereClauseEntryId", getFinderWhereClauseEntryId());
		attributes.put("name", getName());
		attributes.put("nickname", getNickname());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long finderWhereClauseEntryId = (Long)attributes.get(
			"finderWhereClauseEntryId");

		if (finderWhereClauseEntryId != null) {
			setFinderWhereClauseEntryId(finderWhereClauseEntryId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String nickname = (String)attributes.get("nickname");

		if (nickname != null) {
			setNickname(nickname);
		}
	}

	@Override
	public Object clone() {
		return new FinderWhereClauseEntryWrapper(
			(FinderWhereClauseEntry)_finderWhereClauseEntry.clone());
	}

	@Override
	public int compareTo(FinderWhereClauseEntry finderWhereClauseEntry) {
		return _finderWhereClauseEntry.compareTo(finderWhereClauseEntry);
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _finderWhereClauseEntry.getExpandoBridge();
	}

	/**
	 * Returns the finder where clause entry ID of this finder where clause entry.
	 *
	 * @return the finder where clause entry ID of this finder where clause entry
	 */
	@Override
	public long getFinderWhereClauseEntryId() {
		return _finderWhereClauseEntry.getFinderWhereClauseEntryId();
	}

	/**
	 * Returns the name of this finder where clause entry.
	 *
	 * @return the name of this finder where clause entry
	 */
	@Override
	public String getName() {
		return _finderWhereClauseEntry.getName();
	}

	/**
	 * Returns the nickname of this finder where clause entry.
	 *
	 * @return the nickname of this finder where clause entry
	 */
	@Override
	public String getNickname() {
		return _finderWhereClauseEntry.getNickname();
	}

	/**
	 * Returns the primary key of this finder where clause entry.
	 *
	 * @return the primary key of this finder where clause entry
	 */
	@Override
	public long getPrimaryKey() {
		return _finderWhereClauseEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _finderWhereClauseEntry.getPrimaryKeyObj();
	}

	@Override
	public int hashCode() {
		return _finderWhereClauseEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _finderWhereClauseEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _finderWhereClauseEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _finderWhereClauseEntry.isNew();
	}

	@Override
	public void persist() {
		_finderWhereClauseEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_finderWhereClauseEntry.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_finderWhereClauseEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_finderWhereClauseEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_finderWhereClauseEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the finder where clause entry ID of this finder where clause entry.
	 *
	 * @param finderWhereClauseEntryId the finder where clause entry ID of this finder where clause entry
	 */
	@Override
	public void setFinderWhereClauseEntryId(long finderWhereClauseEntryId) {
		_finderWhereClauseEntry.setFinderWhereClauseEntryId(
			finderWhereClauseEntryId);
	}

	/**
	 * Sets the name of this finder where clause entry.
	 *
	 * @param name the name of this finder where clause entry
	 */
	@Override
	public void setName(String name) {
		_finderWhereClauseEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_finderWhereClauseEntry.setNew(n);
	}

	/**
	 * Sets the nickname of this finder where clause entry.
	 *
	 * @param nickname the nickname of this finder where clause entry
	 */
	@Override
	public void setNickname(String nickname) {
		_finderWhereClauseEntry.setNickname(nickname);
	}

	/**
	 * Sets the primary key of this finder where clause entry.
	 *
	 * @param primaryKey the primary key of this finder where clause entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_finderWhereClauseEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_finderWhereClauseEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<FinderWhereClauseEntry>
		toCacheModel() {

		return _finderWhereClauseEntry.toCacheModel();
	}

	@Override
	public FinderWhereClauseEntry toEscapedModel() {
		return new FinderWhereClauseEntryWrapper(
			_finderWhereClauseEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _finderWhereClauseEntry.toString();
	}

	@Override
	public FinderWhereClauseEntry toUnescapedModel() {
		return new FinderWhereClauseEntryWrapper(
			_finderWhereClauseEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _finderWhereClauseEntry.toXmlString();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof FinderWhereClauseEntryWrapper)) {
			return false;
		}

		FinderWhereClauseEntryWrapper finderWhereClauseEntryWrapper =
			(FinderWhereClauseEntryWrapper)object;

		if (Objects.equals(
				_finderWhereClauseEntry,
				finderWhereClauseEntryWrapper._finderWhereClauseEntry)) {

			return true;
		}

		return false;
	}

	@Override
	public FinderWhereClauseEntry getWrappedModel() {
		return _finderWhereClauseEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _finderWhereClauseEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderWhereClauseEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_finderWhereClauseEntry.resetOriginalValues();
	}

	private final FinderWhereClauseEntry _finderWhereClauseEntry;

}