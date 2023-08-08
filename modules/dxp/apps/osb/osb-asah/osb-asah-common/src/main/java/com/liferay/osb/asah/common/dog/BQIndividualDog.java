/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.model.Distribution;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Robson Pastor
 * @author Ivica Cardic
 */
@Component
public class BQIndividualDog {

	public BQIndividualDog(
		BQFieldMappingRepository bqFieldMappingRepository,
		BQIdentityRepository bqIdentityRepository,
		BQIndividualRepository bqIndividualRepository,
		DataSourceRepository dataSourceRepository) {

		_bqFieldMappingRepository = bqFieldMappingRepository;
		_bqIdentityRepository = bqIdentityRepository;
		_bqIndividualRepository = bqIndividualRepository;
		_dataSourceRepository = dataSourceRepository;
	}

	public long countBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String filterString,
		@Nullable Boolean includeAnonymousUsers, @Nullable String interestName,
		@Nullable Long notSegmentId, @Nullable String query,
		@Nullable Long segmentId) {

		if (StringUtils.isNotBlank(filterString)) {
			return _bqIndividualRepository.countBQIndividuals(
				channelId, filterString, includeAnonymousUsers, query,
				segmentId);
		}

		return _bqIndividualRepository.countBQIndividuals(
			accountId, channelId, dataSourceId, interestName, notSegmentId,
			query, segmentId);
	}

	public long countBQIndividualsModifiedLast30Days(Long channelId) {
		return _bqIndividualRepository.countBQIndividualsModifiedLast30Days(
			channelId);
	}

	public long countIndividualCreatedSince(Date startDate) {
		return _bqIndividualRepository.countBQIndividualsCreatedSince(
			startDate);
	}

	public long countIndividuals(boolean includeAnonymousUsers) {
		return _bqIdentityRepository.countBQIndividuals(includeAnonymousUsers);
	}

	public Individual fetchBQIndividual(@Nullable Long channelId, String id) {
		Optional<Individual> bqIndividualOptional =
			_bqIndividualRepository.findByChannelIdAndId(channelId, id);

		if (!bqIndividualOptional.isPresent()) {
			return null;
		}

		Individual individual = bqIndividualOptional.get();

		_setDataSourceName(individual.getCustomFields());
		_setDataSourceName(individual.getFields());

		return individual;
	}

	public Individual fetchBQIndividual(String id) {
		return fetchBQIndividual(null, id);
	}

	public Page<String> getBQIndividualFieldValuePage(
		Long channelId, String filterString, String groupByField, int page,
		int size) {

		String[] fields = StringUtils.split(groupByField, "/");

		String attributeType = fields[0];

		PageRequest pageRequest = PageRequest.of(page, size);

		if (attributeType.equals("custom")) {
			return PageableExecutionUtils.getPage(
				_bqIndividualRepository.searchIndividualFieldValuesCustom(
					channelId, fields[1], filterString, pageRequest),
				pageRequest,
				() -> _bqIndividualRepository.countIndividualFieldValuesCustom(
					channelId, fields[1], filterString));
		}
		else if (attributeType.equals("demographics")) {
			return PageableExecutionUtils.getPage(
				_bqIndividualRepository.searchIndividualFieldValuesDemographics(
					channelId, fields[1], filterString, pageRequest),
				pageRequest,
				() ->
					_bqIndividualRepository.
						countIndividualFieldValuesDemographics(
							channelId, fields[1], filterString));
		}

		return null;
	}

	public Page<Distribution> getDistributionPage(
		@Nullable Long channelId, String fieldName,
		@Nullable Long individualSegmentId, int size, String[] sorts) {

		Optional<BQFieldMapping> bqFieldMappingOptional =
			_bqFieldMappingRepository.findByFieldName(fieldName);

		if (!bqFieldMappingOptional.isPresent()) {
			throw new RuntimeException("Invalid field name " + fieldName);
		}

		BQFieldMapping bqFieldMapping = bqFieldMappingOptional.get();
		PageRequest pageRequest = PageRequest.of(
			0, size,
			SortUtil.getSort(Sort.by(Sort.Order.desc("count")), sorts));

		List<Distribution> distributions =
			_bqIndividualRepository.getIndividualDistributions(
				channelId, bqFieldMapping.getFieldName(),
				bqFieldMapping.getFieldType(), individualSegmentId,
				pageRequest);

		return PageableExecutionUtils.getPage(
			distributions, pageRequest, distributions::size);
	}

	public Page<Individual> searchBQIndividualPage(
		@Nullable Long channelId, int page, @Nullable String query, int size) {

		return PageableExecutionUtils.getPage(
			_searchBQIndividuals(
				null, channelId, null, null, null, null, page, query, null,
				size, null),
			PageRequest.of(page, size),
			() -> countBQIndividuals(
				null, channelId, null, null, null, null, null, query, null));
	}

	public Page<Individual> searchBQIndividualPage(
		Long channelId, Long segmentId, Integer page, String query, int size) {

		return PageableExecutionUtils.getPage(
			_searchBQIndividuals(
				null, channelId, null, null, null, null, page, query, segmentId,
				size, null),
			PageRequest.of(page, size, _getSort(null)),
			() -> countBQIndividuals(
				null, channelId, null, null, null, null, null, query,
				segmentId));
	}

	public Page<Individual> searchBQIndividualPage(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String filterString,
		@Nullable Boolean includeAnonymousUsers, @Nullable String interestName,
		@Nullable Long notSegmentId, int page, @Nullable String query,
		@Nullable Long segmentId, int size, String[] sorts) {

		if ((page == 0) && (size == 0)) {
			return new PageImpl<>(
				Collections.emptyList(), Pageable.unpaged(),
				countBQIndividuals(
					accountId, channelId, dataSourceId, filterString,
					includeAnonymousUsers, interestName, notSegmentId, query,
					segmentId));
		}

		return PageableExecutionUtils.getPage(
			_searchBQIndividuals(
				accountId, channelId, dataSourceId, filterString, interestName,
				notSegmentId, page, query, segmentId, size, sorts),
			PageRequest.of(page, size, _getSort(sorts)),
			() -> countBQIndividuals(
				accountId, channelId, dataSourceId, filterString,
				includeAnonymousUsers, interestName, notSegmentId, query,
				segmentId));
	}

	public void suppress(String id) {
		_bqIndividualRepository.updateSuppressed(id, Boolean.TRUE);
	}

	public void unsuppress(String id) {
		_bqIndividualRepository.updateSuppressed(id, Boolean.FALSE);
	}

	private org.springframework.data.domain.Sort _getSort(String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return org.springframework.data.domain.Sort.by(
				org.springframework.data.domain.Sort.Order.asc("id"));
		}

		List<Sort.Order> orders = new ArrayList<>();

		for (int i = 0; i < sorts.length; i++) {
			String sort = sorts[i];

			String orderString = null;

			String[] properties = sort.split(",");

			if (properties.length == 1) {
				orderString = sorts[++i];
			}
			else {
				orderString = properties[1];
			}

			Sort.Order order = null;

			if (Objects.equals(orderString, "asc")) {
				order = Sort.Order.asc(properties[0]);
			}
			else {
				order = Sort.Order.desc(properties[0]);
			}

			if (StringUtils.containsIgnoreCase(properties[0], "date")) {
				orders.add(order);
			}
			else {
				orders.add(order.ignoreCase());
			}
		}

		return Sort.by(orders);
	}

	private List<Individual> _searchBQIndividuals(
		@Nullable Long accountId, @Nullable Long channelId,
		@Nullable Long dataSourceId, @Nullable String filterString,
		@Nullable String interestName, @Nullable Long notSegmentId, int page,
		@Nullable String query, @Nullable Long segmentId, int size,
		String[] sorts) {

		if (StringUtils.isNotBlank(filterString)) {
			return _bqIndividualRepository.searchBQIndividuals(
				channelId, filterString,
				PageRequest.of(page, size, _getSort(sorts)), query, segmentId);
		}

		return _bqIndividualRepository.searchBQIndividuals(
			accountId, channelId, dataSourceId, interestName, notSegmentId,
			PageRequest.of(page, size, _getSort(sorts)), query, segmentId);
	}

	private void _setDataSourceName(Set<Field> fields) {
		for (Field field : fields) {
			Optional<DataSource> dataSourceOptional =
				_dataSourceRepository.findById(field.getDataSourceId());

			if (dataSourceOptional.isPresent()) {
				DataSource dataSource = dataSourceOptional.get();

				field.setDataSourceName(dataSource.getName());
			}
		}
	}

	private final BQFieldMappingRepository _bqFieldMappingRepository;
	private final BQIdentityRepository _bqIdentityRepository;
	private final BQIndividualRepository _bqIndividualRepository;
	private final DataSourceRepository _dataSourceRepository;

}