/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.repository.BQOrganizationRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class BQOrganizationDog extends BaseBQDXPEntityDog {

	public List<BQOrganization> findByDataSourceIdAndOrganizationIdIn(
		Long dataSourceId, Collection<Long> organizationIds) {

		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.findByDataSourceIdAndOrganizationIdIn(
				dataSourceId, organizationIds);

		for (BQOrganization bqOrganization : bqOrganizations) {
			_populateDataSourceName(bqOrganization);
			_populateParentOrganizationName(bqOrganization);
		}

		return bqOrganizations;
	}

	public BQOrganization getBQOrganization(String bqOrganizationId) {
		Optional<BQOrganization> bqOrganizationOptional =
			_bqOrganizationRepository.findById(bqOrganizationId);

		BQOrganization bqOrganization = bqOrganizationOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no organization with ID " + bqOrganizationId));

		_populateDataSourceName(bqOrganization);
		_populateParentOrganizationName(bqOrganization);

		return bqOrganization;
	}

	public Page<String> getBQOrganizationFieldValuePage(
		Long channelId, String filterString, String groupByField, int page,
		int size) {

		String[] fields = StringUtils.split(groupByField, "/");

		PageRequest pageRequest = PageRequest.of(page, size);

		return PageableExecutionUtils.getPage(
			_bqOrganizationRepository.searchOrganizationFieldValuesCustom(
				channelId, fields[1], filterString, pageRequest),
			pageRequest,
			() -> _bqOrganizationRepository.countOrganizationFieldValuesCustom(
				channelId, fields[1], filterString));
	}

	public Page<BQOrganization> getBQOrganizationPage(
		@Nullable Long channelId, @Nullable String name, int size, Sort sort,
		int start) {

		List<Long> dataSourceIds = getDataSourceIds(channelId);
		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.searchByDataSourceIdsAndName(
				dataSourceIds, name, pageRequest);

		for (BQOrganization bqOrganization : bqOrganizations) {
			_populateDataSourceName(bqOrganization);
			_populateParentOrganizationName(bqOrganization);
		}

		return PageableExecutionUtils.getPage(
			bqOrganizations, pageRequest,
			() -> _bqOrganizationRepository.countByDataSourceIdsAndName(
				dataSourceIds, name));
	}

	public List<BQOrganization> getBQOrganizations(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqOrganizationRepository.findByIdIn(ids);
	}

	public List<BQOrganization> searchBQOrganizations(
		String filterString, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size);

		List<BQOrganization> bqOrganizations =
			_bqOrganizationRepository.searchBQOrganizations(
				new FilterHelper(filterString), pageRequest);

		for (BQOrganization bqOrganization : bqOrganizations) {
			_populateDataSourceName(bqOrganization);
			_populateParentOrganizationName(bqOrganization);
		}

		return bqOrganizations;
	}

	private void _populateDataSourceName(BQOrganization bqOrganization) {
		if (bqOrganization == null) {
			return;
		}

		if (bqOrganization.getDataSourceId() == null) {
			return;
		}

		Optional<DataSource> dataSourceOptional =
			_dataSourceRepository.findById(bqOrganization.getDataSourceId());

		DataSource dataSource = dataSourceOptional.orElse(null);

		if (dataSource != null) {
			bqOrganization.setDataSourceName(dataSource.getName());
		}
	}

	private void _populateParentOrganizationName(
		BQOrganization bqOrganization) {

		if (bqOrganization == null) {
			return;
		}

		if ((bqOrganization.getParentOrganizationId() == null) ||
			(bqOrganization.getParentOrganizationId() == 0)) {

			return;
		}

		if (bqOrganization.getParentOrganizationId() != null) {
			Optional<BQOrganization> parentBQOrganizationOptional =
				_bqOrganizationRepository.findByDataSourceIdAndOrganizationId(
					bqOrganization.getDataSourceId(),
					bqOrganization.getParentOrganizationId());

			BQOrganization parentBQOrganization =
				parentBQOrganizationOptional.orElse(null);

			if (parentBQOrganization != null) {
				bqOrganization.setParentOrganizationName(
					parentBQOrganization.getName());
			}
		}
	}

	@Autowired
	private BQOrganizationRepository _bqOrganizationRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}