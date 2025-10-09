/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.cms.internal.resource.v1_0;

import com.liferay.depot.model.DepotEntry;
import com.liferay.headless.cms.dto.v1_0.AssetPermissionAction;
import com.liferay.headless.cms.dto.v1_0.ResetAssetPermissionAction;
import com.liferay.headless.cms.resource.v1_0.AssetPermissionActionResource;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.site.cms.site.initializer.util.CMSDefaultPermissionUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.rmi.NoSuchObjectException;

/**
 * @author Crescenzo Rega
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/asset-permission-action.properties",
	scope = ServiceScope.PROTOTYPE,
	service = AssetPermissionActionResource.class
)
public class AssetPermissionActionResourceImpl
	extends BaseAssetPermissionActionResourceImpl {

	@Override
	public AssetPermissionAction postAssetPermission(
		AssetPermissionAction assetPermissionAction)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled(
			contextCompany.getCompanyId(), "LPD-17564")) {

			throw new UnsupportedOperationException();
		}

		AssetPermissionAction.Type type = assetPermissionAction.getType();

		if (AssetPermissionAction.Type.RESET_ACTION.equals(type)) {
			// we will do the reset logic

			String className = assetPermissionAction.getClassName();

			if (className.equals(ObjectEntry.class.getName())) {
				// logic for content and files
			} else if (className.equals(ObjectEntryFolder.class.getName())) {
				// logic for folder
			} else {
				throw new UnsupportedOperationException();
			}
		}

		throw new UnsupportedOperationException();
	}

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	private JSONObject _getCMSDefaultPermissionJSONObject(
		ObjectEntry objectEntry)
		throws Exception {

		return _getCMSDefaultPermissionJSONObject(objectEntry.getGroupId(), objectEntry.getObjectEntryFolderId());
	}

	private JSONObject _getCMSDefaultPermissionJSONObject(long groupId, long objectEntryFolderId)
		throws PortalException {
		if (objectEntryFolderId != 0) {
			ObjectEntryFolder objectEntryFolder =
				_objectEntryFolderLocalService.getObjectEntryFolder(objectEntryFolderId);

			JSONObject jsonObject = CMSDefaultPermissionUtil.getJSONObject(
				objectEntryFolder.getCompanyId(),
				objectEntryFolder.getUserId(),
				objectEntryFolder.getExternalReferenceCode(),
				objectEntryFolder.getModelClassName(), _filterFactory);

			if ((jsonObject != null) && !JSONUtil.isEmpty(jsonObject)) {
				return jsonObject;
			}
		}

		Group group = _groupLocalService.getGroup(groupId);

		return CMSDefaultPermissionUtil.getJSONObject(
			group.getCompanyId(), group.getCreatorUserId(),
			group.getExternalReferenceCode(), DepotEntry.class.getName(),
			_filterFactory);
	}

	private JSONObject _getCMSDefaultPermissionJSONObject(
		ObjectEntryFolder objectEntryFolder)
		throws Exception {

		return _getCMSDefaultPermissionJSONObject(objectEntryFolder.getGroupId(), objectEntryFolder.getParentObjectEntryFolderId());
	}
}