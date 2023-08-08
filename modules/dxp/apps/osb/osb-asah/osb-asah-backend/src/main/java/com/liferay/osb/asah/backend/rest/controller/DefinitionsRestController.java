/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.BQFieldMappingDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.BQFieldMappingDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.math3.util.Pair;

import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rachael Koestartyo
 */
@RequestMapping("/definitions")
@RestController
public class DefinitionsRestController extends BaseRestController {

	@GetMapping("/individual-attributes")
	public PageDTO<BQFieldMappingDTO> getIndividualBQFieldMappingDTOPageDTO(
		@RequestParam(required = false) String displayName) {

		Page<BQFieldMapping> bqFieldMappingPage =
			_bqFieldMappingDog.searchIndividualBQFieldMappingPage(
				displayName, 0,
				Math.max(
					1,
					(int)_bqFieldMappingDog.countIndividualBQFieldMappings(
						displayName)),
				new String[] {"displayName", "asc"});

		Map<String, BQFieldMappingDTO> bqFieldMappingDTOs = Stream.of(
			bqFieldMappingPage.getContent()
		).flatMap(
			List::stream
		).map(
			this::_toBQFieldMappingDTO
		).collect(
			LinkedHashMap::new,
			(map, bqFieldMappingDTO) -> map.put(
				bqFieldMappingDTO.getFieldName(), bqFieldMappingDTO),
			Map::putAll
		);

		_addDataSources(bqFieldMappingDTOs);

		return _toPageDTO(
			new BQFieldMappingDTO(bqFieldMappingDTOs.values()),
			bqFieldMappingPage);
	}

	private void _addDataSources(
		Map<String, BQFieldMappingDTO> bqFieldMappingDTOs) {

		for (Map.Entry<String, BQFieldMappingDTO> entry :
				bqFieldMappingDTOs.entrySet()) {

			BQFieldMappingDTO bqFieldMappingDTO = entry.getValue();

			List<Pair<String, String>> pairs = new ArrayList<>();

			Map<String, String> dataSourceFieldNames =
				bqFieldMappingDTO.getDataSourceFieldNames();

			for (Map.Entry<String, String> dataSourceFieldNameEntry :
					dataSourceFieldNames.entrySet()) {

				pairs.add(
					new Pair<>(
						_dataSourceDog.getDataSourceName(
							Long.valueOf(dataSourceFieldNameEntry.getKey())),
						dataSourceFieldNameEntry.getValue()));
			}

			pairs.sort(Comparator.comparing(Pair::getKey));

			List<Map<String, String>> dataSources = new ArrayList<>();

			for (Pair<String, String> pair : pairs) {
				dataSources.add(
					new HashMap<String, String>() {
						{
							put("dataSourceFieldName", pair.getValue());
							put("dataSourceName", pair.getKey());
						}
					});
			}

			bqFieldMappingDTO.setDataSources(dataSources);
		}
	}

	@NotNull
	private BQFieldMappingDTO _toBQFieldMappingDTO(
		BQFieldMapping bqFieldMapping) {

		BQFieldMappingDTO bqFieldMappingDTO = new BQFieldMappingDTO(
			bqFieldMapping);

		List<DataSource> dataSources = _dataSourceDog.getDataSources(
			ListUtil.map(bqFieldMapping.getDataSourceIds(), Long::valueOf));

		Map<String, String> dataSourceFieldNames = new HashMap<>();

		for (DataSource dataSource : dataSources) {
			dataSourceFieldNames.put(
				String.valueOf(dataSource.getId()),
				bqFieldMapping.getFieldName());
		}

		bqFieldMappingDTO.setDataSourceFieldNames(dataSourceFieldNames);

		return bqFieldMappingDTO;
	}

	private PageDTO<BQFieldMappingDTO> _toPageDTO(
		BQFieldMappingDTO bqFieldMappingDTO,
		Page<BQFieldMapping> bqFieldMappingsPage) {

		return new PageDTO<>(
			"_embedded", bqFieldMappingDTO, bqFieldMappingsPage.getNumber(),
			bqFieldMappingsPage.getSize(),
			bqFieldMappingsPage.getTotalElements(),
			bqFieldMappingsPage.getTotalPages());
	}

	@Autowired
	private BQFieldMappingDog _bqFieldMappingDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

}