/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.page.service.impl;

import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.service.base.AssetDisplayPageEntryLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;

import java.util.Date;
import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public class AssetDisplayPageEntryLocalServiceImpl
	extends AssetDisplayPageEntryLocalServiceBaseImpl {

	@Override
	public AssetDisplayPageEntry addAssetDisplayPageEntry(
			long userId, long groupId, long classNameId, long classPK,
			long layoutPageTemplateEntryId, int type,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long assetDisplayPageEntryId = counterLocalService.increment();

		AssetDisplayPageEntry assetDisplayPageEntry =
			assetDisplayPageEntryPersistence.create(assetDisplayPageEntryId);

		assetDisplayPageEntry.setUuid(serviceContext.getUuid());
		assetDisplayPageEntry.setGroupId(groupId);
		assetDisplayPageEntry.setCompanyId(user.getCompanyId());
		assetDisplayPageEntry.setUserId(user.getUserId());
		assetDisplayPageEntry.setUserName(user.getFullName());
		assetDisplayPageEntry.setCreateDate(
			serviceContext.getCreateDate(new Date()));
		assetDisplayPageEntry.setModifiedDate(
			serviceContext.getModifiedDate(new Date()));
		assetDisplayPageEntry.setClassNameId(classNameId);
		assetDisplayPageEntry.setClassPK(classPK);
		assetDisplayPageEntry.setLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
		assetDisplayPageEntry.setType(type);

		return assetDisplayPageEntryPersistence.update(assetDisplayPageEntry);
	}

	@Override
	public AssetDisplayPageEntry addAssetDisplayPageEntry(
			long userId, long groupId, long classNameId, long classPK,
			long layoutPageTemplateEntryId, ServiceContext serviceContext)
		throws PortalException {

		return addAssetDisplayPageEntry(
			userId, groupId, classNameId, classPK, layoutPageTemplateEntryId,
			AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public void deleteAssetDisplayPageEntry(
			long groupId, long classNameId, long classPK)
		throws PortalException {

		assetDisplayPageEntryPersistence.removeByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public AssetDisplayPageEntry fetchAssetDisplayPageEntry(
		long groupId, long classNameId, long classPK) {

		return assetDisplayPageEntryPersistence.fetchByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public List<AssetDisplayPageEntry>
		getAssetDisplayPageEntriesByLayoutPageTemplateEntryId(
			long layoutPageTemplateEntryId) {

		return assetDisplayPageEntryPersistence.findByLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
	}

	@Override
	public int getAssetDisplayPageEntriesCountByLayoutPageTemplateEntryId(
		long layoutPageTemplateEntryId) {

		return assetDisplayPageEntryPersistence.
			countByLayoutPageTemplateEntryId(layoutPageTemplateEntryId);
	}

	@Override
	public AssetDisplayPageEntry updateAssetDisplayPageEntry(
			long assetDisplayPageEntryId, long layoutPageTemplateEntryId,
			int type)
		throws PortalException {

		AssetDisplayPageEntry assetDisplayPageEntry =
			assetDisplayPageEntryPersistence.findByPrimaryKey(
				assetDisplayPageEntryId);

		assetDisplayPageEntry.setModifiedDate(new Date());
		assetDisplayPageEntry.setLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
		assetDisplayPageEntry.setType(type);

		return assetDisplayPageEntryPersistence.update(assetDisplayPageEntry);
	}

}