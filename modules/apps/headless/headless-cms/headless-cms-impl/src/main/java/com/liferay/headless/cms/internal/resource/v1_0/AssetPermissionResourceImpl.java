/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.cms.internal.resource.v1_0;

import com.liferay.depot.model.DepotEntry;
import com.liferay.headless.cms.dto.v1_0.AssetPermission;
import com.liferay.headless.cms.resource.v1_0.AssetPermissionResource;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.rest.filter.factory.FilterFactory;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.site.cms.site.initializer.util.CMSDefaultPermissionUtil;

import java.rmi.NoSuchObjectException;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Balazs Breier
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/asset-permission.properties",
	scope = ServiceScope.PROTOTYPE, service = AssetPermissionResource.class
)
public class AssetPermissionResourceImpl
	extends BaseAssetPermissionResourceImpl {

	@Override
	public AssetPermission putAssetPermissionReset(Long assetId)
		throws Exception {

		ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
			assetId);

		JSONObject defaultPermissionsJSONObject;
		JSONObject jsonObject;

		if (objectEntry == null) {
			ObjectEntryFolder objectEntryFolder =
				_objectEntryFolderLocalService.fetchObjectEntryFolder(assetId);

			if (objectEntryFolder == null) {
				throw new NoSuchObjectException(
					"No ObjectEntry or ObjectEntryFolder found with primary " +
						"key " + assetId);
			}

			defaultPermissionsJSONObject = _getCMSDefaultPermissionJSONObject(
				objectEntryFolder);

			jsonObject = defaultPermissionsJSONObject.getJSONObject(
				"OBJECT_ENTRY_FOLDERS");

			_setResourcePermissions(objectEntryFolder, jsonObject);

			return new AssetPermission() {
				{
					setClassName(objectEntryFolder::getModelClassName);
					setExternalReferenceCode(
						objectEntryFolder::getExternalReferenceCode);
					setId(objectEntryFolder::getObjectEntryFolderId);
				}
			};
		}

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.getObjectDefinition(
				objectEntry.getObjectDefinitionId());

		defaultPermissionsJSONObject = _getCMSDefaultPermissionJSONObject(
			objectEntry);

		jsonObject = _getJSONObject(
			objectDefinition, defaultPermissionsJSONObject);

		_setResourcePermissions(objectEntry, jsonObject);

		return new AssetPermission() {
			{
				setClassName(objectEntry::getModelClassName);
				setExternalReferenceCode(objectEntry::getExternalReferenceCode);
				setId(objectEntry::getObjectEntryId);
			}
		};
	}

	private JSONObject _getCMSDefaultPermissionJSONObject(
			ObjectEntry objectEntry)
		throws Exception {

		ObjectDefinition cmsDefaultPermissionObjectDefinition =
			_objectDefinitionLocalService.
				fetchObjectDefinitionByExternalReferenceCode(
					"L_CMS_DEFAULT_PERMISSION", objectEntry.getCompanyId());

		if (cmsDefaultPermissionObjectDefinition == null) {
			return null;
		}

		if (objectEntry.getObjectEntryFolderId() != 0) {
			ObjectEntryFolder parentObjectEntryFolder =
				_objectEntryFolderLocalService.getObjectEntryFolder(
					objectEntry.getObjectEntryFolderId());

			JSONObject jsonObject = CMSDefaultPermissionUtil.getJSONObject(
				parentObjectEntryFolder.getCompanyId(),
				parentObjectEntryFolder.getUserId(),
				parentObjectEntryFolder.getExternalReferenceCode(),
				parentObjectEntryFolder.getModelClassName(), _filterFactory);

			if ((jsonObject != null) && !JSONUtil.isEmpty(jsonObject)) {
				return jsonObject;
			}
		}

		Group group = _groupLocalService.getGroup(objectEntry.getGroupId());

		return CMSDefaultPermissionUtil.getJSONObject(
			group.getCompanyId(), group.getCreatorUserId(),
			group.getExternalReferenceCode(), DepotEntry.class.getName(),
			_filterFactory);
	}

	private JSONObject _getCMSDefaultPermissionJSONObject(
			ObjectEntryFolder objectEntryFolder)
		throws Exception {

		ObjectDefinition cmsDefaultPermissionObjectDefinition =
			_objectDefinitionLocalService.
				fetchObjectDefinitionByExternalReferenceCode(
					"L_CMS_DEFAULT_PERMISSION",
					objectEntryFolder.getCompanyId());

		if (cmsDefaultPermissionObjectDefinition == null) {
			return null;
		}

		if (objectEntryFolder.getObjectEntryFolderId() != 0) {
			ObjectEntryFolder parentObjectEntryFolder =
				_objectEntryFolderLocalService.getObjectEntryFolder(
					objectEntryFolder.getParentObjectEntryFolderId());

			JSONObject jsonObject = CMSDefaultPermissionUtil.getJSONObject(
				parentObjectEntryFolder.getCompanyId(),
				parentObjectEntryFolder.getUserId(),
				parentObjectEntryFolder.getExternalReferenceCode(),
				parentObjectEntryFolder.getModelClassName(), _filterFactory);

			if ((jsonObject != null) && !JSONUtil.isEmpty(jsonObject)) {
				return jsonObject;
			}
		}

		Group group = _groupLocalService.getGroup(
			objectEntryFolder.getGroupId());

		return CMSDefaultPermissionUtil.getJSONObject(
			group.getCompanyId(), group.getCreatorUserId(),
			group.getExternalReferenceCode(), DepotEntry.class.getName(),
			_filterFactory);
	}

	private JSONObject _getJSONObject(
		ObjectDefinition objectDefinition, JSONObject objectEntryJSONObject) {

		JSONObject jsonObject = null;

		if (Objects.equals(
				objectDefinition.getExternalReferenceCode(),
				"L_BASIC_WEB_CONTENT") || Objects.equals(
			objectDefinition.getExternalReferenceCode(),
			"L_BLOG")) {

			jsonObject = objectEntryJSONObject.getJSONObject(
				ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_CONTENTS);
		}
		else if (Objects.equals(
					objectDefinition.getExternalReferenceCode(),
					"L_BASIC_DOCUMENT")) {

			jsonObject = objectEntryJSONObject.getJSONObject(
				ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_FILES);
		}

		return jsonObject;
	}

	private void _setResourcePermissions(
			ObjectEntry objectEntry, JSONObject jsonObject)
		throws Exception {
		_resourcePermissionLocalService.deleteResourcePermissions(
			objectEntry.getCompanyId(), objectEntry.getModelClassName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(objectEntry.getObjectEntryId()));

		List<String> resourceActions = ResourceActionsUtil.getResourceActions(
			objectEntry.getModelClassName());

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			JSONArray jsonArray = jsonObject.getJSONArray(key);

			if ((jsonArray == null) || JSONUtil.isEmpty(jsonArray)) {
				continue;
			}

			Role role = _roleLocalService.fetchRole(
				objectEntry.getCompanyId(), key);

			if (role == null) {
				continue;
			}

			_resourcePermissionLocalService.setResourcePermissions(
				objectEntry.getCompanyId(), objectEntry.getModelClassName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(objectEntry.getObjectEntryId()),
				role.getRoleId(),
				ArrayUtil.filter(
					JSONUtil.toStringArray(jsonArray),
					action -> resourceActions.contains(action)));
		}
	}

	private void _setResourcePermissions(
			ObjectEntryFolder objectEntryFolder, JSONObject jsonObject)
		throws Exception {
		_resourcePermissionLocalService.deleteResourcePermissions(
			objectEntryFolder.getCompanyId(), objectEntryFolder.getModelClassName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(objectEntryFolder.getObjectEntryFolderId()));

		List<String> resourceActions = ResourceActionsUtil.getResourceActions(
			objectEntryFolder.getModelClassName());

		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			JSONArray jsonArray = jsonObject.getJSONArray(key);

			if ((jsonArray == null) || JSONUtil.isEmpty(jsonArray)) {
				continue;
			}

			Role role = _roleLocalService.fetchRole(
				objectEntryFolder.getCompanyId(), key);

			if (role == null) {
				continue;
			}

			_resourcePermissionLocalService.setResourcePermissions(
				objectEntryFolder.getCompanyId(),
				objectEntryFolder.getModelClassName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(objectEntryFolder.getObjectEntryFolderId()),
				role.getRoleId(),
				ArrayUtil.filter(
					JSONUtil.toStringArray(jsonArray),
					action -> resourceActions.contains(action)));
		}
	}

	@Reference(
		target = "(filter.factory.key=" + ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT + ")"
	)
	private FilterFactory<Predicate> _filterFactory;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}