/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.exportimport.content.processor;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.exportimport.content.processor.ExportImportContentProcessor;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.StagedModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.staging.StagingGroupHelper;
import com.liferay.staging.StagingGroupHelperUtil;

import java.util.Iterator;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = "model.class.name=com.liferay.fragment.model.FragmentEntryLink",
	service = {
		ExportImportContentProcessor.class,
		FragmentEntryLinkExportImportContentProcessor.class
	}
)
public class FragmentEntryLinkExportImportContentProcessor
	implements ExportImportContentProcessor<String> {

	@Override
	public String replaceExportContentReferences(
			PortletDataContext portletDataContext, StagedModel stagedModel,
			String content, boolean exportReferencedContent,
			boolean escapeContent)
		throws Exception {

		JSONObject editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
			content);

		String portletId = editableValuesJSONObject.getString("portletId");

		if (Validator.isNotNull(portletId)) {
			return content;
		}

		content =
			_dlReferencesExportImportContentProcessor.
				replaceExportContentReferences(
					portletDataContext, stagedModel, content,
					exportReferencedContent, escapeContent);
		content =
			_layoutReferencesExportImportContentProcessor.
				replaceExportContentReferences(
					portletDataContext, stagedModel, content,
					exportReferencedContent, escapeContent);

		JSONObject editableProcessorJSONObject =
			editableValuesJSONObject.getJSONObject(
				_KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR);

		if ((editableProcessorJSONObject == null) ||
			(editableProcessorJSONObject.length() <= 0)) {

			return content;
		}

		Iterator<String> editableKeysIterator =
			editableProcessorJSONObject.keys();

		while (editableKeysIterator.hasNext()) {
			String editableKey = editableKeysIterator.next();

			JSONObject editableJSONObject =
				editableProcessorJSONObject.getJSONObject(editableKey);

			long classNameId = editableJSONObject.getLong("classNameId");
			long classPK = editableJSONObject.getLong("classPK");

			if ((classNameId == 0) || (classPK == 0)) {
				continue;
			}

			AssetEntry assetEntry = _assetEntryLocalService.getEntry(
				_portal.getClassName(classNameId), classPK);

			AssetRenderer assetRenderer = assetEntry.getAssetRenderer();

			if (assetRenderer == null) {
				continue;
			}

			AssetRendererFactory assetRendererFactory =
				assetRenderer.getAssetRendererFactory();

			StagingGroupHelper stagingGroupHelper =
				StagingGroupHelperUtil.getStagingGroupHelper();

			if (!stagingGroupHelper.isStagedPortlet(
					portletDataContext.getScopeGroupId(),
					assetRendererFactory.getPortletId())) {

				continue;
			}

			editableJSONObject.put(
				"className", _portal.getClassName(classNameId));

			if (exportReferencedContent) {
				try {
					StagedModelDataHandlerUtil.exportReferenceStagedModel(
						portletDataContext, stagedModel,
						(StagedModel)assetRenderer.getAssetObject(),
						PortletDataContext.REFERENCE_TYPE_DEPENDENCY);
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						StringBundler messageSB = new StringBundler(11);

						messageSB.append("Staged model with class name ");
						messageSB.append(stagedModel.getModelClassName());
						messageSB.append(" and primary key ");
						messageSB.append(stagedModel.getPrimaryKeyObj());
						messageSB.append(" references asset entry with class ");
						messageSB.append("primary key ");
						messageSB.append(classPK);
						messageSB.append(" and class name ");
						messageSB.append(_portal.getClassName(classNameId));
						messageSB.append(" that could not be exported due to ");
						messageSB.append(e);

						String errorMessage = messageSB.toString();

						if (Validator.isNotNull(e.getMessage())) {
							errorMessage = StringBundler.concat(
								errorMessage, ": ", e.getMessage());
						}

						_log.debug(errorMessage, e);
					}
				}
			}
			else {
				Element entityElement = portletDataContext.getExportDataElement(
					stagedModel);

				portletDataContext.addReferenceElement(
					stagedModel, entityElement,
					(ClassedModel)assetRenderer.getAssetObject(),
					PortletDataContext.REFERENCE_TYPE_DEPENDENCY, true);
			}
		}

		return editableValuesJSONObject.toString();
	}

	@Override
	public String replaceImportContentReferences(
			PortletDataContext portletDataContext, StagedModel stagedModel,
			String content)
		throws Exception {

		JSONObject editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
			content);

		String portletId = editableValuesJSONObject.getString("portletId");

		if (Validator.isNotNull(portletId)) {
			return content;
		}

		content =
			_dlReferencesExportImportContentProcessor.
				replaceImportContentReferences(
					portletDataContext, stagedModel, content);

		content =
			_layoutReferencesExportImportContentProcessor.
				replaceImportContentReferences(
					portletDataContext, stagedModel, content);

		JSONObject editableProcessorJSONObject =
			editableValuesJSONObject.getJSONObject(
				_KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR);

		if ((editableProcessorJSONObject == null) ||
			(editableProcessorJSONObject.length() <= 0)) {

			return content;
		}

		Iterator<String> editableKeysIterator =
			editableProcessorJSONObject.keys();

		while (editableKeysIterator.hasNext()) {
			String editableKey = editableKeysIterator.next();

			JSONObject editableJSONObject =
				editableProcessorJSONObject.getJSONObject(editableKey);

			String className = GetterUtil.getString(
				editableJSONObject.remove("className"));

			if (Validator.isNull(className)) {
				continue;
			}

			AssetRendererFactory assetRendererFactory =
				AssetRendererFactoryRegistryUtil.
					getAssetRendererFactoryByClassName(className);

			StagingGroupHelper stagingGroupHelper =
				StagingGroupHelperUtil.getStagingGroupHelper();

			if (!stagingGroupHelper.isStagedPortlet(
					portletDataContext.getScopeGroupId(),
					assetRendererFactory.getPortletId())) {

				continue;
			}

			long classPK = editableJSONObject.getLong("classPK");

			if (classPK == 0) {
				continue;
			}

			editableJSONObject.put(
				"classNameId", _portal.getClassNameId(className));

			Map<Long, Long> primaryKeys =
				(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
					className);

			classPK = MapUtil.getLong(primaryKeys, classPK, classPK);

			editableJSONObject.put("classPK", classPK);
		}

		return editableValuesJSONObject.toString();
	}

	@Override
	public void validateContentReferences(long groupId, String content)
		throws PortalException {
	}

	private static final String _KEY_EDITABLE_FRAGMENT_ENTRY_PROCESSOR =
		"com.liferay.fragment.entry.processor.editable." +
			"EditableFragmentEntryProcessor";

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryLinkExportImportContentProcessor.class);

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference(target = "(content.processor.type=DLReferences)")
	private ExportImportContentProcessor<String>
		_dlReferencesExportImportContentProcessor;

	@Reference(target = "(content.processor.type=LayoutReferences)")
	private ExportImportContentProcessor<String>
		_layoutReferencesExportImportContentProcessor;

	@Reference
	private Portal _portal;

}