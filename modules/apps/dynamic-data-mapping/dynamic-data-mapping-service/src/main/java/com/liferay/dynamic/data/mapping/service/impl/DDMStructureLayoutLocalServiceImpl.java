/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.service.impl;

import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONSerializer;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
import com.liferay.dynamic.data.mapping.service.base.DDMStructureLayoutLocalServiceBaseImpl;
import com.liferay.dynamic.data.mapping.validator.DDMFormLayoutValidator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.spring.extender.service.ServiceReference;

/**
 * @author Marcellus Tavares
 */
public class DDMStructureLayoutLocalServiceImpl
	extends DDMStructureLayoutLocalServiceBaseImpl {

	@Override
	public DDMStructureLayout addStructureLayout(
			long userId, long groupId, long structureVersionId,
			DDMFormLayout ddmFormLayout, ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		validate(ddmFormLayout);

		long structureLayoutId = counterLocalService.increment();

		DDMStructureLayout structureLayout =
			ddmStructureLayoutPersistence.create(structureLayoutId);

		structureLayout.setUuid(serviceContext.getUuid());
		structureLayout.setGroupId(groupId);
		structureLayout.setCompanyId(user.getCompanyId());
		structureLayout.setUserId(user.getUserId());
		structureLayout.setUserName(user.getFullName());
		structureLayout.setStructureVersionId(structureVersionId);
		structureLayout.setDefinition(
			ddmFormLayoutJSONSerializer.serialize(ddmFormLayout));

		return ddmStructureLayoutPersistence.update(structureLayout);
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public void deleteStructureLayout(DDMStructureLayout structureLayout) {
		ddmStructureLayoutPersistence.remove(structureLayout);
	}

	@Override
	public void deleteStructureLayout(long structureLayoutId)
		throws PortalException {

		DDMStructureLayout structureLayout =
			ddmStructureLayoutPersistence.findByPrimaryKey(structureLayoutId);

		ddmStructureLayoutLocalService.deleteStructureLayout(structureLayout);
	}

	@Override
	public DDMStructureLayout getStructureLayout(long structureLayoutId)
		throws PortalException {

		return ddmStructureLayoutPersistence.findByPrimaryKey(
			structureLayoutId);
	}

	@Override
	public DDMStructureLayout getStructureLayoutByStructureVersionId(
			long structureVersionId)
		throws PortalException {

		return ddmStructureLayoutPersistence.findByStructureVersionId(
			structureVersionId);
	}

	@Override
	public DDMFormLayout getStructureLayoutDDMFormLayout(
			DDMStructureLayout structureLayout)
		throws PortalException {

		return ddmFormLayoutJSONDeserializer.deserialize(
			structureLayout.getDefinition());
	}

	@Override
	public DDMStructureLayout updateStructureLayout(
			long structureLayoutId, DDMFormLayout ddmFormLayout,
			ServiceContext serviceContext)
		throws PortalException {

		DDMStructureLayout structureLayout =
			ddmStructureLayoutPersistence.findByPrimaryKey(structureLayoutId);

		validate(ddmFormLayout);

		structureLayout.setDefinition(
			ddmFormLayoutJSONSerializer.serialize(ddmFormLayout));

		return ddmStructureLayoutPersistence.update(structureLayout);
	}

	protected void validate(DDMFormLayout ddmFormLayout)
		throws PortalException {

		ddmFormLayoutValidator.validate(ddmFormLayout);
	}

	@ServiceReference(type = DDMFormLayoutJSONDeserializer.class)
	protected DDMFormLayoutJSONDeserializer ddmFormLayoutJSONDeserializer;

	@ServiceReference(type = DDMFormLayoutJSONSerializer.class)
	protected DDMFormLayoutJSONSerializer ddmFormLayoutJSONSerializer;

	@ServiceReference(type = DDMFormLayoutValidator.class)
	protected DDMFormLayoutValidator ddmFormLayoutValidator;

}