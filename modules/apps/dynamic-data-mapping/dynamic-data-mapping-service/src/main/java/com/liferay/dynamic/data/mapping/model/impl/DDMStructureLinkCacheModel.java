/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.model.impl;

import com.liferay.dynamic.data.mapping.model.DDMStructureLink;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing DDMStructureLink in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class DDMStructureLinkCacheModel
	implements CacheModel<DDMStructureLink>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DDMStructureLinkCacheModel)) {
			return false;
		}

		DDMStructureLinkCacheModel ddmStructureLinkCacheModel =
			(DDMStructureLinkCacheModel)object;

		if (structureLinkId == ddmStructureLinkCacheModel.structureLinkId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, structureLinkId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(11);

		sb.append("{structureLinkId=");
		sb.append(structureLinkId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", structureId=");
		sb.append(structureId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DDMStructureLink toEntityModel() {
		DDMStructureLinkImpl ddmStructureLinkImpl = new DDMStructureLinkImpl();

		ddmStructureLinkImpl.setStructureLinkId(structureLinkId);
		ddmStructureLinkImpl.setCompanyId(companyId);
		ddmStructureLinkImpl.setClassNameId(classNameId);
		ddmStructureLinkImpl.setClassPK(classPK);
		ddmStructureLinkImpl.setStructureId(structureId);

		ddmStructureLinkImpl.resetOriginalValues();

		return ddmStructureLinkImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		structureLinkId = objectInput.readLong();

		companyId = objectInput.readLong();

		classNameId = objectInput.readLong();

		classPK = objectInput.readLong();

		structureId = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(structureLinkId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(classNameId);

		objectOutput.writeLong(classPK);

		objectOutput.writeLong(structureId);
	}

	public long structureLinkId;
	public long companyId;
	public long classNameId;
	public long classPK;
	public long structureId;

}