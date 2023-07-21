/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.internal.service;

import com.liferay.document.library.file.rank.service.DLFileRankLocalService;
import com.liferay.document.library.kernel.model.DLFileRank;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLAppLocalServiceWrapper;
import com.liferay.petra.model.adapter.util.ModelAdapterUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(immediate = true, service = ServiceWrapper.class)
public class DLFileRankDLAppLocalServiceWrapper
	extends DLAppLocalServiceWrapper {

	public DLFileRankDLAppLocalServiceWrapper() {
		super(null);
	}

	public DLFileRankDLAppLocalServiceWrapper(
		DLAppLocalService dlAppLocalService) {

		super(dlAppLocalService);
	}

	@Override
	public DLFileRank addFileRank(
		long repositoryId, long companyId, long userId, long fileEntryId,
		ServiceContext serviceContext) {

		return ModelAdapterUtil.adapt(
			DLFileRank.class,
			_dlFileRankLocalService.addFileRank(
				repositoryId, companyId, userId, fileEntryId, serviceContext));
	}

	@Override
	public void deleteFileRanksByFileEntryId(long fileEntryId) {
		_dlFileRankLocalService.deleteFileRanksByFileEntryId(fileEntryId);
	}

	@Override
	public void deleteFileRanksByUserId(long userId) {
		_dlFileRankLocalService.deleteFileRanksByUserId(userId);
	}

	@Override
	public List<DLFileRank> getFileRanks(long repositoryId, long userId) {
		return ModelAdapterUtil.adapt(
			DLFileRank.class,
			_dlFileRankLocalService.getFileRanks(repositoryId, userId));
	}

	@Override
	public DLFileRank updateFileRank(
		long repositoryId, long companyId, long userId, long fileEntryId,
		ServiceContext serviceContext) {

		return ModelAdapterUtil.adapt(
			DLFileRank.class,
			_dlFileRankLocalService.updateFileRank(
				repositoryId, companyId, userId, fileEntryId, serviceContext));
	}

	@Reference
	private DLFileRankLocalService _dlFileRankLocalService;

}