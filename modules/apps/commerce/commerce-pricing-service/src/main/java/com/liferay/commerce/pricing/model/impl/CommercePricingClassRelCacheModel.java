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

package com.liferay.commerce.pricing.model.impl;

import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing CommercePricingClassRel in entity cache.
 *
 * @author Riccardo Alberti
 * @generated
 */
public class CommercePricingClassRelCacheModel
	implements CacheModel<CommercePricingClassRel>, Externalizable {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommercePricingClassRelCacheModel)) {
			return false;
		}

		CommercePricingClassRelCacheModel commercePricingClassRelCacheModel =
			(CommercePricingClassRelCacheModel)obj;

		if (commercePricingClassRelId ==
				commercePricingClassRelCacheModel.commercePricingClassRelId) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, commercePricingClassRelId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{commercePricingClassRelId=");
		sb.append(commercePricingClassRelId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", commercePricingClassId=");
		sb.append(commercePricingClassId);
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public CommercePricingClassRel toEntityModel() {
		CommercePricingClassRelImpl commercePricingClassRelImpl =
			new CommercePricingClassRelImpl();

		commercePricingClassRelImpl.setCommercePricingClassRelId(
			commercePricingClassRelId);
		commercePricingClassRelImpl.setCompanyId(companyId);
		commercePricingClassRelImpl.setUserId(userId);

		if (userName == null) {
			commercePricingClassRelImpl.setUserName("");
		}
		else {
			commercePricingClassRelImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commercePricingClassRelImpl.setCreateDate(null);
		}
		else {
			commercePricingClassRelImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commercePricingClassRelImpl.setModifiedDate(null);
		}
		else {
			commercePricingClassRelImpl.setModifiedDate(new Date(modifiedDate));
		}

		commercePricingClassRelImpl.setCommercePricingClassId(
			commercePricingClassId);
		commercePricingClassRelImpl.setClassNameId(classNameId);
		commercePricingClassRelImpl.setClassPK(classPK);

		commercePricingClassRelImpl.resetOriginalValues();

		return commercePricingClassRelImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		commercePricingClassRelId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		commercePricingClassId = objectInput.readLong();

		classNameId = objectInput.readLong();

		classPK = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(commercePricingClassRelId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(commercePricingClassId);

		objectOutput.writeLong(classNameId);

		objectOutput.writeLong(classPK);
	}

	public long commercePricingClassRelId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long commercePricingClassId;
	public long classNameId;
	public long classPK;

}