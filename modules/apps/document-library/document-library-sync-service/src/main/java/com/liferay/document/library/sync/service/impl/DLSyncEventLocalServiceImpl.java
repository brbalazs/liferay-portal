/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.sync.service.impl;

import com.liferay.document.library.sync.model.DLSyncEvent;
import com.liferay.document.library.sync.service.base.DLSyncEventLocalServiceBaseImpl;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;

import java.util.List;

/**
 * @author Dennis Ju
 */
public class DLSyncEventLocalServiceImpl
	extends DLSyncEventLocalServiceBaseImpl {

	@Override
	public DLSyncEvent addDLSyncEvent(String event, String type, long typePK) {
		DLSyncEvent dlSyncEvent = dlSyncEventPersistence.fetchByTypePK(typePK);

		if (dlSyncEvent == null) {
			long dlSyncEventId = counterLocalService.increment();

			dlSyncEvent = dlSyncEventPersistence.create(dlSyncEventId);

			dlSyncEvent.setType(type);
			dlSyncEvent.setTypePK(typePK);
		}

		dlSyncEvent.setModifiedTime(System.currentTimeMillis());
		dlSyncEvent.setEvent(event);

		return dlSyncEventPersistence.update(dlSyncEvent);
	}

	@Override
	public void deleteDLSyncEvents() {
		dlSyncEventPersistence.removeAll();
	}

	@Override
	public List<DLSyncEvent> getDLSyncEvents(long modifiedTime) {
		return dlSyncEventPersistence.findByModifiedTime(modifiedTime);
	}

	@Override
	public List<DLSyncEvent> getLatestDLSyncEvents() {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			DLSyncEvent.class);

		Property property = PropertyFactoryUtil.forName("modifiedTime");

		DynamicQuery modifiedTimeDynamicQuery =
			DynamicQueryFactoryUtil.forClass(DLSyncEvent.class);

		Projection projection = ProjectionFactoryUtil.max("modifiedTime");

		modifiedTimeDynamicQuery.setProjection(projection);

		dynamicQuery.add(property.eq(modifiedTimeDynamicQuery));

		return dlSyncEventPersistence.findWithDynamicQuery(dynamicQuery);
	}

}