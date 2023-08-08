/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.BQAssetDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.BQAssetDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQAsset;
import com.liferay.osb.asah.common.entity.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author Ivica Cardic
 */
@RequestMapping("/activities")
@RestController
public class ActivitiesRestController extends BaseRestController {

	public ActivitiesRestController(
		BQAssetDog bqAssetDog, DataSourceDog dataSourceDog) {

		_bqAssetDog = bqAssetDog;
		_dataSourceDog = dataSourceDog;
	}

	@GetMapping("/assets")
	public PageDTO<BQAssetDTO> getBQAssetDTOPageDTO(
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		Page<BQAsset> bqAssetsPage = _bqAssetDog.searchBQAssets(
			filterString, PageRequest.of(page, size, SortUtil.getSort(sorts)));

		return new PageDTO(
			"_embedded",
			new BQAssetDTO(_toBQAssetDTOs(bqAssetsPage.getContent())),
			bqAssetsPage.getNumber(), bqAssetsPage.getSize(),
			bqAssetsPage.getTotalElements(), bqAssetsPage.getTotalPages());
	}

	private List<BQAssetDTO> _toBQAssetDTOs(List<BQAsset> bqAssets) {
		List<BQAssetDTO> bqAssetDTOs = new ArrayList<>();

		if (bqAssets.isEmpty()) {
			return bqAssetDTOs;
		}

		List<DataSource> dataSources = _dataSourceDog.getDataSources();

		for (BQAsset bqAsset : bqAssets) {
			BQAssetDTO bqAssetDTO = new BQAssetDTO(bqAsset);

			for (DataSource dataSource : dataSources) {
				if (Objects.equals(
						dataSource.getId(), bqAssetDTO.getDataSourceId())) {

					bqAssetDTO.setDataSourceName(dataSource.getName());

					break;
				}
			}

			bqAssetDTOs.add(bqAssetDTO);
		}

		return bqAssetDTOs;
	}

	private final BQAssetDog _bqAssetDog;
	private final DataSourceDog _dataSourceDog;

}