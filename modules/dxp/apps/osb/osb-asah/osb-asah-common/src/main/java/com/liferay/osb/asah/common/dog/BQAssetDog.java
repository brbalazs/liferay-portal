/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQAsset;
import com.liferay.osb.asah.common.repository.BQAssetRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

/**
 * @author Ivica Cardic
 */
@Component
public class BQAssetDog {

	public BQAssetDog(BQAssetRepository bqAssetRepository) {
		_bqAssetEventRepository = bqAssetRepository;
	}

	public List<BQAsset> getBQAssets(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqAssetEventRepository.findByIdIn(ids);
	}

	public Page<BQAsset> searchBQAssets(
		String filterString, Pageable pageable) {

		return PageableExecutionUtils.getPage(
			_bqAssetEventRepository.searchBQAssets(filterString, pageable),
			pageable,
			() -> _bqAssetEventRepository.countBQAssets(filterString));
	}

	private final BQAssetRepository _bqAssetEventRepository;

}