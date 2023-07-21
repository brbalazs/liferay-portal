/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.page.internal.exportimport.data.handler;

import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.exportimport.data.handler.base.BaseStagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(immediate = true, service = StagedModelDataHandler.class)
public class AssetDisplayPageStagedModelDataHandler
	extends BaseStagedModelDataHandler<AssetDisplayPageEntry> {

	public static final String[] CLASS_NAMES = {
		AssetDisplayPageEntry.class.getName()
	};

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			AssetDisplayPageEntry assetDisplayPageEntry)
		throws Exception {

		Element assetDisplayPageElement =
			portletDataContext.getExportDataElement(assetDisplayPageEntry);

		portletDataContext.addClassedModel(
			assetDisplayPageElement,
			ExportImportPathUtil.getModelPath(assetDisplayPageEntry),
			assetDisplayPageEntry);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.fetchLayoutPageTemplateEntry(
				assetDisplayPageEntry.getLayoutPageTemplateEntryId());

		if (layoutPageTemplateEntry != null) {
			StagedModelDataHandlerUtil.exportReferenceStagedModel(
				portletDataContext, assetDisplayPageEntry,
				layoutPageTemplateEntry,
				PortletDataContext.REFERENCE_TYPE_DEPENDENCY);
		}
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			AssetDisplayPageEntry assetDisplayPageEntry)
		throws Exception {

		AssetDisplayPageEntry importedAssetDisplayPageEntry =
			(AssetDisplayPageEntry)assetDisplayPageEntry.clone();

		long layoutPageTemplateEntryId = 0;

		if (importedAssetDisplayPageEntry.getLayoutPageTemplateEntryId() > 0) {
			Map<Long, Long> layoutPageTemplateEntryIds =
				(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
					LayoutPageTemplateEntry.class);

			layoutPageTemplateEntryId = MapUtil.getLong(
				layoutPageTemplateEntryIds,
				assetDisplayPageEntry.getLayoutPageTemplateEntryId(),
				assetDisplayPageEntry.getLayoutPageTemplateEntryId());
		}

		importedAssetDisplayPageEntry.setGroupId(
			portletDataContext.getScopeGroupId());
		importedAssetDisplayPageEntry.setLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);

		AssetDisplayPageEntry existingAssetDisplayPageEntry =
			_stagedModelRepository.fetchStagedModelByUuidAndGroupId(
				assetDisplayPageEntry.getUuid(),
				portletDataContext.getScopeGroupId());

		if ((existingAssetDisplayPageEntry == null) ||
			!portletDataContext.isDataStrategyMirror()) {

			importedAssetDisplayPageEntry =
				_stagedModelRepository.addStagedModel(
					portletDataContext, importedAssetDisplayPageEntry);
		}
		else {
			importedAssetDisplayPageEntry.setAssetDisplayPageEntryId(
				existingAssetDisplayPageEntry.getAssetDisplayPageEntryId());
			importedAssetDisplayPageEntry.setClassPK(
				existingAssetDisplayPageEntry.getClassPK());
			importedAssetDisplayPageEntry.setLayoutPageTemplateEntryId(
				existingAssetDisplayPageEntry.getLayoutPageTemplateEntryId());

			importedAssetDisplayPageEntry =
				_stagedModelRepository.updateStagedModel(
					portletDataContext, importedAssetDisplayPageEntry);
		}

		portletDataContext.importClassedModel(
			assetDisplayPageEntry, importedAssetDisplayPageEntry);
	}

	@Override
	protected StagedModelRepository<AssetDisplayPageEntry>
		getStagedModelRepository() {

		return _stagedModelRepository;
	}

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.asset.display.page.model.AssetDisplayPageEntry)",
		unbind = "-"
	)
	private StagedModelRepository<AssetDisplayPageEntry> _stagedModelRepository;

}