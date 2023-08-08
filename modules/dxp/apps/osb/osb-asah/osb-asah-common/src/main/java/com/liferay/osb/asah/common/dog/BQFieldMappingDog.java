/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author Robson Pastor
 */
@Component
public class BQFieldMappingDog {

	public long countIndividualBQFieldMappings(String displayName) {
		return _bqFieldMappingRepository.countIndividualBQFieldMappings(
			displayName);
	}

	public BQFieldMapping getBQFieldMapping(String fieldName) {
		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		return bqFieldMappingOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no field mapping with field name " + fieldName));
	}

	public List<BQFieldMapping> getBQFieldMappings(Set<String> fieldNames) {
		if (CollectionUtils.isEmpty(fieldNames)) {
			return Collections.emptyList();
		}

		return _bqFieldMappingRepository.findByFieldNameIn(fieldNames);
	}

	public Page<BQFieldMapping> searchBQFieldMappingPage(
		@Nullable String filterString, int page, int size,
		@Nullable String[] sorts) {

		PageRequest pageRequest = PageRequest.of(
			page, size,
			SortUtil.getSort(Sort.by(Sort.Order.asc("displayName")), sorts));

		return PageableExecutionUtils.getPage(
			_bqFieldMappingRepository.searchByFilterString(
				filterString, pageRequest),
			pageRequest,
			() -> _bqFieldMappingRepository.countByFilterString(filterString));
	}

	public Page<BQFieldMapping> searchIndividualBQFieldMappingPage(
		@Nullable String displayName, int page, int size, String[] sorts) {

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		List<BQFieldMapping> bqFieldMappings =
			_bqFieldMappingRepository.searchIndividualBQFieldMappings(
				displayName, pageRequest);

		_setDemographicsFieldsDataSourceIds(bqFieldMappings);

		return PageableExecutionUtils.getPage(
			bqFieldMappings, pageRequest,
			() -> _bqFieldMappingRepository.countIndividualBQFieldMappings(
				displayName));
	}

	private void _setDemographicsFieldsDataSourceIds(
		List<BQFieldMapping> bqFieldMappings) {

		List<DataSource> dataSources = _dataSourceDog.getDataSources();

		Set<String> dataSourceIds = new HashSet<>();

		for (DataSource dataSource : dataSources) {
			dataSourceIds.add(String.valueOf(dataSource.getId()));
		}

		for (BQFieldMapping bqFieldMapping : bqFieldMappings) {
			String context = bqFieldMapping.getContext();

			if (context.equals("demographics")) {
				bqFieldMapping.setDataSourceIds(dataSourceIds);
			}
		}
	}

	@Autowired
	private BQFieldMappingRepository _bqFieldMappingRepository;

	@Autowired
	private DataSourceDog _dataSourceDog;

}