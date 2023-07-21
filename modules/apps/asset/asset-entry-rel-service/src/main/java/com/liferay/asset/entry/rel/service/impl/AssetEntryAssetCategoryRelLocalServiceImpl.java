/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.entry.rel.service.impl;

import com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel;
import com.liferay.asset.entry.rel.service.base.AssetEntryAssetCategoryRelLocalServiceBaseImpl;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public class AssetEntryAssetCategoryRelLocalServiceImpl
	extends AssetEntryAssetCategoryRelLocalServiceBaseImpl {

	@Override
	public AssetEntryAssetCategoryRel addAssetEntryAssetCategoryRel(
		long assetEntryId, long assetCategoryId) {

		return assetEntryAssetCategoryRelLocalService.
			addAssetEntryAssetCategoryRel(assetEntryId, assetCategoryId, 0);
	}

	@Override
	public AssetEntryAssetCategoryRel addAssetEntryAssetCategoryRel(
		long assetEntryId, long assetCategoryId, int priority) {

		long assetEntryAssetCategoryRelId = counterLocalService.increment();

		AssetEntryAssetCategoryRel assetEntryAssetCategoryRel =
			assetEntryAssetCategoryRelPersistence.create(
				assetEntryAssetCategoryRelId);

		assetEntryAssetCategoryRel.setAssetEntryId(assetEntryId);
		assetEntryAssetCategoryRel.setAssetCategoryId(assetCategoryId);
		assetEntryAssetCategoryRel.setPriority(priority);

		return assetEntryAssetCategoryRelPersistence.update(
			assetEntryAssetCategoryRel);
	}

	@Override
	public void deleteAssetEntryAssetCategoryRel(
		long assetEntryId, long assetCategoryId) {

		AssetEntryAssetCategoryRel assetEntryAssetCategoryRel =
			assetEntryAssetCategoryRelPersistence.fetchByA_A(
				assetEntryId, assetCategoryId);

		if (assetEntryAssetCategoryRel != null) {
			assetEntryAssetCategoryRelPersistence.remove(
				assetEntryAssetCategoryRel);
		}
	}

	@Override
	public void deleteAssetEntryAssetCategoryRelByAssetCategoryId(
		long assetCategoryId) {

		assetEntryAssetCategoryRelPersistence.removeByAssetCategoryId(
			assetCategoryId);
	}

	@Override
	public void deleteAssetEntryAssetCategoryRelByAssetEntryId(
		long assetEntryId) {

		assetEntryAssetCategoryRelPersistence.removeByAssetEntryId(
			assetEntryId);
	}

	@Override
	public AssetEntryAssetCategoryRel fetchAssetEntryAssetCategoryRel(
		long assetEntryId, long assetCategoryId) {

		return assetEntryAssetCategoryRelPersistence.fetchByA_A(
			assetEntryId, assetCategoryId);
	}

	@Override
	public long[] getAssetCategoryPrimaryKeys(long assetEntryId) {
		List<AssetEntryAssetCategoryRel> assetEntryAssetCategoryRels =
			getAssetEntryAssetCategoryRelsByAssetEntryId(assetEntryId);

		return ListUtil.toLongArray(
			assetEntryAssetCategoryRels,
			AssetEntryAssetCategoryRel::getAssetCategoryId);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetCategoryId(long assetCategoryId) {

		return assetEntryAssetCategoryRelPersistence.findByAssetCategoryId(
			assetCategoryId);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetCategoryId(
			long assetCategoryId, int start, int end) {

		return assetEntryAssetCategoryRelPersistence.findByAssetCategoryId(
			assetCategoryId, start, end);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetCategoryId(
			long assetCategoryId, int start, int end,
			OrderByComparator<AssetEntryAssetCategoryRel> orderByComparator) {

		return assetEntryAssetCategoryRelPersistence.findByAssetCategoryId(
			assetCategoryId, start, end, orderByComparator);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetEntryId(long assetEntryId) {

		return assetEntryAssetCategoryRelPersistence.findByAssetEntryId(
			assetEntryId);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetEntryId(
			long assetEntryId, int start, int end) {

		return assetEntryAssetCategoryRelPersistence.findByAssetEntryId(
			assetEntryId, start, end);
	}

	@Override
	public List<AssetEntryAssetCategoryRel>
		getAssetEntryAssetCategoryRelsByAssetEntryId(
			long assetEntryId, int start, int end,
			OrderByComparator<AssetEntryAssetCategoryRel> orderByComparator) {

		return assetEntryAssetCategoryRelPersistence.findByAssetEntryId(
			assetEntryId, start, end, orderByComparator);
	}

	@Override
	public int getAssetEntryAssetCategoryRelsCount(long assetEntryId) {
		return assetEntryAssetCategoryRelPersistence.countByAssetEntryId(
			assetEntryId);
	}

	@Override
	public long[] getAssetEntryPrimaryKeys(long assetCategoryId) {
		List<AssetEntryAssetCategoryRel> assetEntryAssetCategoryRels =
			getAssetEntryAssetCategoryRelsByAssetCategoryId(assetCategoryId);

		return ListUtil.toLongArray(
			assetEntryAssetCategoryRels,
			AssetEntryAssetCategoryRel::getAssetEntryId);
	}

}