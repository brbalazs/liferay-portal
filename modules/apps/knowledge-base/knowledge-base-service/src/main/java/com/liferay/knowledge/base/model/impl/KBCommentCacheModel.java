/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.knowledge.base.model.impl;

import com.liferay.knowledge.base.model.KBComment;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing KBComment in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class KBCommentCacheModel
	implements CacheModel<KBComment>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof KBCommentCacheModel)) {
			return false;
		}

		KBCommentCacheModel kbCommentCacheModel = (KBCommentCacheModel)object;

		if (kbCommentId == kbCommentCacheModel.kbCommentId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, kbCommentId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(29);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", kbCommentId=");
		sb.append(kbCommentId);
		sb.append(", groupId=");
		sb.append(groupId);
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
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", content=");
		sb.append(content);
		sb.append(", userRating=");
		sb.append(userRating);
		sb.append(", lastPublishDate=");
		sb.append(lastPublishDate);
		sb.append(", status=");
		sb.append(status);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public KBComment toEntityModel() {
		KBCommentImpl kbCommentImpl = new KBCommentImpl();

		if (uuid == null) {
			kbCommentImpl.setUuid("");
		}
		else {
			kbCommentImpl.setUuid(uuid);
		}

		kbCommentImpl.setKbCommentId(kbCommentId);
		kbCommentImpl.setGroupId(groupId);
		kbCommentImpl.setCompanyId(companyId);
		kbCommentImpl.setUserId(userId);

		if (userName == null) {
			kbCommentImpl.setUserName("");
		}
		else {
			kbCommentImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			kbCommentImpl.setCreateDate(null);
		}
		else {
			kbCommentImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			kbCommentImpl.setModifiedDate(null);
		}
		else {
			kbCommentImpl.setModifiedDate(new Date(modifiedDate));
		}

		kbCommentImpl.setClassNameId(classNameId);
		kbCommentImpl.setClassPK(classPK);

		if (content == null) {
			kbCommentImpl.setContent("");
		}
		else {
			kbCommentImpl.setContent(content);
		}

		kbCommentImpl.setUserRating(userRating);

		if (lastPublishDate == Long.MIN_VALUE) {
			kbCommentImpl.setLastPublishDate(null);
		}
		else {
			kbCommentImpl.setLastPublishDate(new Date(lastPublishDate));
		}

		kbCommentImpl.setStatus(status);

		kbCommentImpl.resetOriginalValues();

		return kbCommentImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		kbCommentId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		classNameId = objectInput.readLong();

		classPK = objectInput.readLong();
		content = objectInput.readUTF();

		userRating = objectInput.readInt();
		lastPublishDate = objectInput.readLong();

		status = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(kbCommentId);

		objectOutput.writeLong(groupId);

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

		objectOutput.writeLong(classNameId);

		objectOutput.writeLong(classPK);

		if (content == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(content);
		}

		objectOutput.writeInt(userRating);
		objectOutput.writeLong(lastPublishDate);

		objectOutput.writeInt(status);
	}

	public String uuid;
	public long kbCommentId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long classNameId;
	public long classPK;
	public String content;
	public int userRating;
	public long lastPublishDate;
	public int status;

}