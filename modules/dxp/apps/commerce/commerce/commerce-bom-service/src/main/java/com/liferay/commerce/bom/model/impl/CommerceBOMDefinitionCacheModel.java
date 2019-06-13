/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.bom.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.bom.model.CommerceBOMDefinition;

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing CommerceBOMDefinition in entity cache.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMDefinition
 * @generated
 */
@ProviderType
public class CommerceBOMDefinitionCacheModel implements CacheModel<CommerceBOMDefinition>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CommerceBOMDefinitionCacheModel)) {
			return false;
		}

		CommerceBOMDefinitionCacheModel commerceBOMDefinitionCacheModel = (CommerceBOMDefinitionCacheModel)obj;

		if (commerceBOMDefinitionId == commerceBOMDefinitionCacheModel.commerceBOMDefinitionId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, commerceBOMDefinitionId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{commerceBOMDefinitionId=");
		sb.append(commerceBOMDefinitionId);
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
		sb.append(", name=");
		sb.append(name);
		sb.append(", imageId=");
		sb.append(imageId);
		sb.append(", friendlyUrl=");
		sb.append(friendlyUrl);
		sb.append(", commerceBOMFolderId=");
		sb.append(commerceBOMFolderId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public CommerceBOMDefinition toEntityModel() {
		CommerceBOMDefinitionImpl commerceBOMDefinitionImpl = new CommerceBOMDefinitionImpl();

		commerceBOMDefinitionImpl.setCommerceBOMDefinitionId(commerceBOMDefinitionId);
		commerceBOMDefinitionImpl.setCompanyId(companyId);
		commerceBOMDefinitionImpl.setUserId(userId);

		if (userName == null) {
			commerceBOMDefinitionImpl.setUserName("");
		}
		else {
			commerceBOMDefinitionImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			commerceBOMDefinitionImpl.setCreateDate(null);
		}
		else {
			commerceBOMDefinitionImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			commerceBOMDefinitionImpl.setModifiedDate(null);
		}
		else {
			commerceBOMDefinitionImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (name == null) {
			commerceBOMDefinitionImpl.setName("");
		}
		else {
			commerceBOMDefinitionImpl.setName(name);
		}

		commerceBOMDefinitionImpl.setImageId(imageId);

		if (friendlyUrl == null) {
			commerceBOMDefinitionImpl.setFriendlyUrl("");
		}
		else {
			commerceBOMDefinitionImpl.setFriendlyUrl(friendlyUrl);
		}

		commerceBOMDefinitionImpl.setCommerceBOMFolderId(commerceBOMFolderId);

		commerceBOMDefinitionImpl.resetOriginalValues();

		return commerceBOMDefinitionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		commerceBOMDefinitionId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		name = objectInput.readUTF();

		imageId = objectInput.readLong();
		friendlyUrl = objectInput.readUTF();

		commerceBOMFolderId = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(commerceBOMDefinitionId);

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

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		objectOutput.writeLong(imageId);

		if (friendlyUrl == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(friendlyUrl);
		}

		objectOutput.writeLong(commerceBOMFolderId);
	}

	public long commerceBOMDefinitionId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String name;
	public long imageId;
	public String friendlyUrl;
	public long commerceBOMFolderId;
}