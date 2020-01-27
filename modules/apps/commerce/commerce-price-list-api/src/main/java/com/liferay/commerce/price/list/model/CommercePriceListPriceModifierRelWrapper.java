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

package com.liferay.commerce.price.list.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link CommercePriceListPriceModifierRel}.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceListPriceModifierRel
 * @generated
 */
public class CommercePriceListPriceModifierRelWrapper
	implements CommercePriceListPriceModifierRel,
			   ModelWrapper<CommercePriceListPriceModifierRel> {

	public CommercePriceListPriceModifierRelWrapper(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		_commercePriceListPriceModifierRel = commercePriceListPriceModifierRel;
	}

	@Override
	public Class<?> getModelClass() {
		return CommercePriceListPriceModifierRel.class;
	}

	@Override
	public String getModelClassName() {
		return CommercePriceListPriceModifierRel.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put(
			"commercePriceListPriceModifierRelId",
			getCommercePriceListPriceModifierRelId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("commercePriceModifierId", getCommercePriceModifierId());
		attributes.put("commercePriceListId", getCommercePriceListId());
		attributes.put("order", getOrder());
		attributes.put("lastPublishDate", getLastPublishDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long commercePriceListPriceModifierRelId = (Long)attributes.get(
			"commercePriceListPriceModifierRelId");

		if (commercePriceListPriceModifierRelId != null) {
			setCommercePriceListPriceModifierRelId(
				commercePriceListPriceModifierRelId);
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

		Long commercePriceModifierId = (Long)attributes.get(
			"commercePriceModifierId");

		if (commercePriceModifierId != null) {
			setCommercePriceModifierId(commercePriceModifierId);
		}

		Long commercePriceListId = (Long)attributes.get("commercePriceListId");

		if (commercePriceListId != null) {
			setCommercePriceListId(commercePriceListId);
		}

		Integer order = (Integer)attributes.get("order");

		if (order != null) {
			setOrder(order);
		}

		Date lastPublishDate = (Date)attributes.get("lastPublishDate");

		if (lastPublishDate != null) {
			setLastPublishDate(lastPublishDate);
		}
	}

	@Override
	public Object clone() {
		return new CommercePriceListPriceModifierRelWrapper(
			(CommercePriceListPriceModifierRel)
				_commercePriceListPriceModifierRel.clone());
	}

	@Override
	public int compareTo(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		return _commercePriceListPriceModifierRel.compareTo(
			commercePriceListPriceModifierRel);
	}

	/**
	 * Returns the commerce price list ID of this commerce price list price modifier rel.
	 *
	 * @return the commerce price list ID of this commerce price list price modifier rel
	 */
	@Override
	public long getCommercePriceListId() {
		return _commercePriceListPriceModifierRel.getCommercePriceListId();
	}

	/**
	 * Returns the commerce price list price modifier rel ID of this commerce price list price modifier rel.
	 *
	 * @return the commerce price list price modifier rel ID of this commerce price list price modifier rel
	 */
	@Override
	public long getCommercePriceListPriceModifierRelId() {
		return _commercePriceListPriceModifierRel.
			getCommercePriceListPriceModifierRelId();
	}

	/**
	 * Returns the commerce price modifier ID of this commerce price list price modifier rel.
	 *
	 * @return the commerce price modifier ID of this commerce price list price modifier rel
	 */
	@Override
	public long getCommercePriceModifierId() {
		return _commercePriceListPriceModifierRel.getCommercePriceModifierId();
	}

	/**
	 * Returns the company ID of this commerce price list price modifier rel.
	 *
	 * @return the company ID of this commerce price list price modifier rel
	 */
	@Override
	public long getCompanyId() {
		return _commercePriceListPriceModifierRel.getCompanyId();
	}

	/**
	 * Returns the create date of this commerce price list price modifier rel.
	 *
	 * @return the create date of this commerce price list price modifier rel
	 */
	@Override
	public Date getCreateDate() {
		return _commercePriceListPriceModifierRel.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _commercePriceListPriceModifierRel.getExpandoBridge();
	}

	/**
	 * Returns the last publish date of this commerce price list price modifier rel.
	 *
	 * @return the last publish date of this commerce price list price modifier rel
	 */
	@Override
	public Date getLastPublishDate() {
		return _commercePriceListPriceModifierRel.getLastPublishDate();
	}

	/**
	 * Returns the modified date of this commerce price list price modifier rel.
	 *
	 * @return the modified date of this commerce price list price modifier rel
	 */
	@Override
	public Date getModifiedDate() {
		return _commercePriceListPriceModifierRel.getModifiedDate();
	}

	/**
	 * Returns the order of this commerce price list price modifier rel.
	 *
	 * @return the order of this commerce price list price modifier rel
	 */
	@Override
	public int getOrder() {
		return _commercePriceListPriceModifierRel.getOrder();
	}

	/**
	 * Returns the primary key of this commerce price list price modifier rel.
	 *
	 * @return the primary key of this commerce price list price modifier rel
	 */
	@Override
	public long getPrimaryKey() {
		return _commercePriceListPriceModifierRel.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _commercePriceListPriceModifierRel.getPrimaryKeyObj();
	}

	/**
	 * Returns the user ID of this commerce price list price modifier rel.
	 *
	 * @return the user ID of this commerce price list price modifier rel
	 */
	@Override
	public long getUserId() {
		return _commercePriceListPriceModifierRel.getUserId();
	}

	/**
	 * Returns the user name of this commerce price list price modifier rel.
	 *
	 * @return the user name of this commerce price list price modifier rel
	 */
	@Override
	public String getUserName() {
		return _commercePriceListPriceModifierRel.getUserName();
	}

	/**
	 * Returns the user uuid of this commerce price list price modifier rel.
	 *
	 * @return the user uuid of this commerce price list price modifier rel
	 */
	@Override
	public String getUserUuid() {
		return _commercePriceListPriceModifierRel.getUserUuid();
	}

	/**
	 * Returns the uuid of this commerce price list price modifier rel.
	 *
	 * @return the uuid of this commerce price list price modifier rel
	 */
	@Override
	public String getUuid() {
		return _commercePriceListPriceModifierRel.getUuid();
	}

	@Override
	public int hashCode() {
		return _commercePriceListPriceModifierRel.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _commercePriceListPriceModifierRel.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _commercePriceListPriceModifierRel.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _commercePriceListPriceModifierRel.isNew();
	}

	@Override
	public void persist() {
		_commercePriceListPriceModifierRel.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_commercePriceListPriceModifierRel.setCachedModel(cachedModel);
	}

	/**
	 * Sets the commerce price list ID of this commerce price list price modifier rel.
	 *
	 * @param commercePriceListId the commerce price list ID of this commerce price list price modifier rel
	 */
	@Override
	public void setCommercePriceListId(long commercePriceListId) {
		_commercePriceListPriceModifierRel.setCommercePriceListId(
			commercePriceListId);
	}

	/**
	 * Sets the commerce price list price modifier rel ID of this commerce price list price modifier rel.
	 *
	 * @param commercePriceListPriceModifierRelId the commerce price list price modifier rel ID of this commerce price list price modifier rel
	 */
	@Override
	public void setCommercePriceListPriceModifierRelId(
		long commercePriceListPriceModifierRelId) {

		_commercePriceListPriceModifierRel.
			setCommercePriceListPriceModifierRelId(
				commercePriceListPriceModifierRelId);
	}

	/**
	 * Sets the commerce price modifier ID of this commerce price list price modifier rel.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID of this commerce price list price modifier rel
	 */
	@Override
	public void setCommercePriceModifierId(long commercePriceModifierId) {
		_commercePriceListPriceModifierRel.setCommercePriceModifierId(
			commercePriceModifierId);
	}

	/**
	 * Sets the company ID of this commerce price list price modifier rel.
	 *
	 * @param companyId the company ID of this commerce price list price modifier rel
	 */
	@Override
	public void setCompanyId(long companyId) {
		_commercePriceListPriceModifierRel.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this commerce price list price modifier rel.
	 *
	 * @param createDate the create date of this commerce price list price modifier rel
	 */
	@Override
	public void setCreateDate(Date createDate) {
		_commercePriceListPriceModifierRel.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {

		_commercePriceListPriceModifierRel.setExpandoBridgeAttributes(
			baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_commercePriceListPriceModifierRel.setExpandoBridgeAttributes(
			expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_commercePriceListPriceModifierRel.setExpandoBridgeAttributes(
			serviceContext);
	}

	/**
	 * Sets the last publish date of this commerce price list price modifier rel.
	 *
	 * @param lastPublishDate the last publish date of this commerce price list price modifier rel
	 */
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_commercePriceListPriceModifierRel.setLastPublishDate(lastPublishDate);
	}

	/**
	 * Sets the modified date of this commerce price list price modifier rel.
	 *
	 * @param modifiedDate the modified date of this commerce price list price modifier rel
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_commercePriceListPriceModifierRel.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_commercePriceListPriceModifierRel.setNew(n);
	}

	/**
	 * Sets the order of this commerce price list price modifier rel.
	 *
	 * @param order the order of this commerce price list price modifier rel
	 */
	@Override
	public void setOrder(int order) {
		_commercePriceListPriceModifierRel.setOrder(order);
	}

	/**
	 * Sets the primary key of this commerce price list price modifier rel.
	 *
	 * @param primaryKey the primary key of this commerce price list price modifier rel
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		_commercePriceListPriceModifierRel.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_commercePriceListPriceModifierRel.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	 * Sets the user ID of this commerce price list price modifier rel.
	 *
	 * @param userId the user ID of this commerce price list price modifier rel
	 */
	@Override
	public void setUserId(long userId) {
		_commercePriceListPriceModifierRel.setUserId(userId);
	}

	/**
	 * Sets the user name of this commerce price list price modifier rel.
	 *
	 * @param userName the user name of this commerce price list price modifier rel
	 */
	@Override
	public void setUserName(String userName) {
		_commercePriceListPriceModifierRel.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this commerce price list price modifier rel.
	 *
	 * @param userUuid the user uuid of this commerce price list price modifier rel
	 */
	@Override
	public void setUserUuid(String userUuid) {
		_commercePriceListPriceModifierRel.setUserUuid(userUuid);
	}

	/**
	 * Sets the uuid of this commerce price list price modifier rel.
	 *
	 * @param uuid the uuid of this commerce price list price modifier rel
	 */
	@Override
	public void setUuid(String uuid) {
		_commercePriceListPriceModifierRel.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel
		<CommercePriceListPriceModifierRel> toCacheModel() {

		return _commercePriceListPriceModifierRel.toCacheModel();
	}

	@Override
	public CommercePriceListPriceModifierRel toEscapedModel() {
		return new CommercePriceListPriceModifierRelWrapper(
			_commercePriceListPriceModifierRel.toEscapedModel());
	}

	@Override
	public String toString() {
		return _commercePriceListPriceModifierRel.toString();
	}

	@Override
	public CommercePriceListPriceModifierRel toUnescapedModel() {
		return new CommercePriceListPriceModifierRelWrapper(
			_commercePriceListPriceModifierRel.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _commercePriceListPriceModifierRel.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommercePriceListPriceModifierRelWrapper)) {
			return false;
		}

		CommercePriceListPriceModifierRelWrapper
			commercePriceListPriceModifierRelWrapper =
				(CommercePriceListPriceModifierRelWrapper)obj;

		if (Objects.equals(
				_commercePriceListPriceModifierRel,
				commercePriceListPriceModifierRelWrapper.
					_commercePriceListPriceModifierRel)) {

			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _commercePriceListPriceModifierRel.getStagedModelType();
	}

	@Override
	public CommercePriceListPriceModifierRel getWrappedModel() {
		return _commercePriceListPriceModifierRel;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _commercePriceListPriceModifierRel.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _commercePriceListPriceModifierRel.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_commercePriceListPriceModifierRel.resetOriginalValues();
	}

	private final CommercePriceListPriceModifierRel
		_commercePriceListPriceModifierRel;

}