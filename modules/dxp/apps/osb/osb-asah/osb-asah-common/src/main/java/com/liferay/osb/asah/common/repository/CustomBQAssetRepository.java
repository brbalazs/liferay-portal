/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQAsset;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQAssetRepository {

	public long countBQAssets(String filterString);

	public List<BQAsset> findByIdIn(Collection<String> ids);

	public List<BQAsset> searchBQAssets(String filterString, Pageable pageable);

}