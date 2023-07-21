/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.exportimport.staged.model.repository;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelModifiedDateComparator;
import com.liferay.exportimport.staged.model.repository.StagedModelRepository;
import com.liferay.exportimport.staged.model.repository.StagedModelRepositoryHelper;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentEntryLocalService;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.fragment.model.FragmentEntry",
	service = StagedModelRepository.class
)
public class FragmentEntryStagedModelRepository
	implements StagedModelRepository<FragmentEntry> {

	@Override
	public FragmentEntry addStagedModel(
			PortletDataContext portletDataContext, FragmentEntry fragmentEntry)
		throws PortalException {

		long userId = portletDataContext.getUserId(fragmentEntry.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			fragmentEntry);

		if (portletDataContext.isDataStrategyMirror()) {
			serviceContext.setUuid(fragmentEntry.getUuid());
		}

		return _fragmentEntryLocalService.addFragmentEntry(
			userId, fragmentEntry.getGroupId(),
			fragmentEntry.getFragmentCollectionId(), fragmentEntry.getName(),
			fragmentEntry.getCss(), fragmentEntry.getHtml(),
			fragmentEntry.getJs(), fragmentEntry.getPreviewFileEntryId(),
			fragmentEntry.getStatus(), serviceContext);
	}

	@Override
	public void deleteStagedModel(FragmentEntry fragmentEntry)
		throws PortalException {

		_fragmentEntryLocalService.deleteFragmentEntry(fragmentEntry);
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		FragmentEntry fragmentEntry = fetchStagedModelByUuidAndGroupId(
			uuid, groupId);

		if (fragmentEntry != null) {
			deleteStagedModel(fragmentEntry);
		}
	}

	@Override
	public void deleteStagedModels(PortletDataContext portletDataContext)
		throws PortalException {
	}

	@Override
	public FragmentEntry fetchMissingReference(String uuid, long groupId) {
		return (FragmentEntry)
			_stagedModelRepositoryHelper.fetchMissingReference(
				uuid, groupId, this);
	}

	@Override
	public FragmentEntry fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _fragmentEntryLocalService.fetchFragmentEntryByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public List<FragmentEntry> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _fragmentEntryLocalService.getFragmentEntriesByUuidAndCompanyId(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new StagedModelModifiedDateComparator<FragmentEntry>());
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext) {

		return _fragmentEntryLocalService.getExportActionableDynamicQuery(
			portletDataContext);
	}

	@Override
	public FragmentEntry getStagedModel(long id) throws PortalException {
		return _fragmentEntryLocalService.getFragmentEntry(id);
	}

	@Override
	public FragmentEntry saveStagedModel(FragmentEntry fragmentEntry)
		throws PortalException {

		return _fragmentEntryLocalService.updateFragmentEntry(fragmentEntry);
	}

	@Override
	public FragmentEntry updateStagedModel(
			PortletDataContext portletDataContext, FragmentEntry fragmentEntry)
		throws PortalException {

		long userId = portletDataContext.getUserId(fragmentEntry.getUserUuid());

		return _fragmentEntryLocalService.updateFragmentEntry(
			userId, fragmentEntry.getFragmentEntryId(), fragmentEntry.getName(),
			fragmentEntry.getCss(), fragmentEntry.getHtml(),
			fragmentEntry.getJs(), fragmentEntry.getStatus());
	}

	@Reference
	private FragmentEntryLocalService _fragmentEntryLocalService;

	@Reference
	private StagedModelRepositoryHelper _stagedModelRepositoryHelper;

}