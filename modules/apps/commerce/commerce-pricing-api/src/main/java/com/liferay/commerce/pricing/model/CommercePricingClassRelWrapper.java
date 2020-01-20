/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.pricing.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link CommercePricingClassRel}.
 * </p>
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRel
 * @generated
 */
public class CommercePricingClassRelWrapper
	implements CommercePricingClassRel, ModelWrapper<CommercePricingClassRel> {

	public CommercePricingClassRelWrapper(
		CommercePricingClassRel commercePricingClassRel) {

		_commercePricingClassRel = commercePricingClassRel;
	}

	@Override
	public Class<?> getModelClass() {
		return CommercePricingClassRel.class;
	}

	@Override
	public String getModelClassName() {
		return CommercePricingClassRel.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put(
			"commercePricingClassRelId", getCommercePricingClassRelId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("commercePricingClassId", getCommercePricingClassId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long commercePricingClassRelId = (Long)attributes.get(
			"commercePricingClassRelId");

		if (commercePricingClassRelId != null) {
			setCommercePricingClassRelId(commercePricingClassRelId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long commercePricingClassId = (Long)attributes.get(
			"commercePricingClassId");

		if (commercePricingClassId != null) {
			setCommercePricingClassId(commercePricingClassId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}
	}

	@Override
	public Object clone() {
		return new CommercePricingClassRelWrapper(
			(CommercePricingClassRel)_commercePricingClassRel.clone());
	}

	@Override
	public int compareTo(CommercePricingClassRel commercePricingClassRel) {
		return _commercePricingClassRel.compareTo(commercePricingClassRel);
	}

	/**
	 * Returns the fully qualified class name of this commerce pricing class rel.
	 *
	 * @return the fully qualified class name of this commerce pricing class rel
	 */
	@Override
	public String getClassName() {
		return _commercePricingClassRel.getClassName();
	}

	/**
	 * Returns the class name ID of this commerce pricing class rel.
	 *
	 * @return the class name ID of this commerce pricing class rel
	 */
	@Override
	public long getClassNameId() {
		return _commercePricingClassRel.getClassNameId();
	}

	/**
	 * Returns the class pk of this commerce pricing class rel.
	 *
	 * @return the class pk of this commerce pricing class rel
	 */
	@Override
	public long getClassPK() {
		return _commercePricingClassRel.getClassPK();
	}

	/**
	 * Returns the commerce pricing class ID of this commerce pricing class rel.
	 *
	 * @return the commerce pricing class ID of this commerce pricing class rel
	 */
	@Override
	public long getCommercePricingClassId() {
		return _commercePricingClassRel.getCommercePricingClassId();
	}

	/**
	 * Returns the commerce pricing class rel ID of this commerce pricing class rel.
	 *
	 * @return the commerce pricing class rel ID of this commerce pricing class rel
	 */
	@Override
	public long getCommercePricingClassRelId() {
		return _commercePricingClassRel.getCommercePricingClassRelId();
	}

	/**
	 * Returns the company ID of this commerce pricing class rel.
	 *
	 * @return the company ID of this commerce pricing class rel
	 */
	@Override
	public long getCompanyId() {
		return _commercePricingClassRel.getCompanyId();
	}

	/**
	 * Returns the create date of this commerce pricing class rel.
	 *
	 * @return the create date of this commerce pricing class rel
	 */
	@Override
	public Date getCreateDate() {
		return _commercePricingClassRel.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _commercePricingClassRel.getExpandoBridge();
	}

	/**
	 * Returns the modified date of this commerce pricing class rel.
	 *
	 * @return the modified date of this commerce pricing class rel
	 */
	@Override
	public Date getModifiedDate() {
		return _commercePricingClassRel.getModifiedDate();
	}

	/**
	 * Returns the primary key of this commerce pricing class rel.
	 *
	 * @return the primary key of this commerce pricing class rel
	 */
	@Override
	public long getPrimaryKey() {
		return _commercePricingClassRel.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commercePricingClassRel.getPrimaryKeyObj();
	}

	/**
	 * Returns the user ID of this commerce pricing class rel.
	 *
	 * @return the user ID of this commerce pricing class rel
	 */
	@Override
	public long getUserId() {
		return _commercePricingClassRel.getUserId();
	}

	/**
	 * Returns the user name of this commerce pricing class rel.
	 *
	 * @return the user name of this commerce pricing class rel
	 */
	@Override
	public String getUserName() {
		return _commercePricingClassRel.getUserName();
	}

	/**
	 * Returns the user uuid of this commerce pricing class rel.
	 *
	 * @return the user uuid of this commerce pricing class rel
	 */
	@Override
	public String getUserUuid() {
		return _commercePricingClassRel.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _commercePricingClassRel.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _commercePricingClassRel.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _commercePricingClassRel.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _commercePricingClassRel.isNew();
	}

	@Override
	public void persist() {
		_commercePricingClassRel.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_commercePricingClassRel.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_commercePricingClassRel.setClassName(className);
	}

	/**
	 * Sets the class name ID of this commerce pricing class rel.
	 *
	 * @param classNameId the class name ID of this commerce pricing class rel
	 */
	@Override
	public void setClassNameId(long classNameId) {
		_commercePricingClassRel.setClassNameId(classNameId);
	}

	/**
	 * Sets the class pk of this commerce pricing class rel.
	 *
	 * @param classPK the class pk of this commerce pricing class rel
	 */
	@Override
	public void setClassPK(long classPK) {
		_commercePricingClassRel.setClassPK(classPK);
	}

	/**
	 * Sets the commerce pricing class ID of this commerce pricing class rel.
	 *
	 * @param commercePricingClassId the commerce pricing class ID of this commerce pricing class rel
	 */
	@Override
	public void setCommercePricingClassId(long commercePricingClassId) {
		_commercePricingClassRel.setCommercePricingClassId(
			commercePricingClassId);
	}

	/**
	 * Sets the commerce pricing class rel ID of this commerce pricing class rel.
	 *
	 * @param commercePricingClassRelId the commerce pricing class rel ID of this commerce pricing class rel
	 */
	@Override
	public void setCommercePricingClassRelId(long commercePricingClassRelId) {
		_commercePricingClassRel.setCommercePricingClassRelId(
			commercePricingClassRelId);
	}

	/**
	 * Sets the company ID of this commerce pricing class rel.
	 *
	 * @param companyId the company ID of this commerce pricing class rel
	 */
	@Override
	public void setCompanyId(long companyId) {
		_commercePricingClassRel.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this commerce pricing class rel.
	 *
	 * @param createDate the create date of this commerce pricing class rel
	 */
	@Override
	public void setCreateDate(Date createDate) {
		_commercePricingClassRel.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_commercePricingClassRel.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_commercePricingClassRel.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_commercePricingClassRel.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	 * Sets the modified date of this commerce pricing class rel.
	 *
	 * @param modifiedDate the modified date of this commerce pricing class rel
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_commercePricingClassRel.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_commercePricingClassRel.setNew(n);
	}

	/**
	 * Sets the primary key of this commerce pricing class rel.
	 *
	 * @param primaryKey the primary key of this commerce pricing class rel
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_commercePricingClassRel.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_commercePricingClassRel.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the user ID of this commerce pricing class rel.
	 *
	 * @param userId the user ID of this commerce pricing class rel
	 */
	@Override
	public void setUserId(long userId) {
		_commercePricingClassRel.setUserId(userId);
	}

	/**
	 * Sets the user name of this commerce pricing class rel.
	 *
	 * @param userName the user name of this commerce pricing class rel
	 */
	@Override
	public void setUserName(String userName) {
		_commercePricingClassRel.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this commerce pricing class rel.
	 *
	 * @param userUuid the user uuid of this commerce pricing class rel
	 */
	@Override
	public void setUserUuid(String userUuid) {
		_commercePricingClassRel.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<CommercePricingClassRel>
		toCacheModel() {

		return _commercePricingClassRel.toCacheModel();
	}

	@Override
	public CommercePricingClassRel toEscapedModel() {
		return new CommercePricingClassRelWrapper(
			_commercePricingClassRel.toEscapedModel());
	}

	@Override
	public String toString() {
		return _commercePricingClassRel.toString();
	}

	@Override
	public CommercePricingClassRel toUnescapedModel() {
		return new CommercePricingClassRelWrapper(
			_commercePricingClassRel.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _commercePricingClassRel.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommercePricingClassRelWrapper)) {
			return false;
		}

		CommercePricingClassRelWrapper commercePricingClassRelWrapper =
			(CommercePricingClassRelWrapper)obj;

		if (Objects.equals(
				_commercePricingClassRel,
				commercePricingClassRelWrapper._commercePricingClassRel)) {

			return true;
		}

		return false;
	}

	@Override
	public CommercePricingClassRel getWrappedModel() {
		return _commercePricingClassRel;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _commercePricingClassRel.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _commercePricingClassRel.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_commercePricingClassRel.resetOriginalValues();
	}

	private final CommercePricingClassRel _commercePricingClassRel;

}