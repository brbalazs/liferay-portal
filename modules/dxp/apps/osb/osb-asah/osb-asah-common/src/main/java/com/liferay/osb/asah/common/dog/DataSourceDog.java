/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.converter.helper.DefaultFilterStringConverterHelper;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.postgresql.converter.helper.DataSourceFilterStringConverterHelper;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.BeanUtils;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;
import com.liferay.osb.asah.common.wedeploy.data.WeDeployDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 * @author André Miranda
 */
@Component
public class DataSourceDog {

	public DataSource addDataSource(DataSource dataSource) {
		dataSource.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setName(_getDataSourceName(dataSource.getName()));

		dataSource = _dataSourceRepository.save(dataSource);

		dataSource.setIsNew(Boolean.FALSE);

		String providerType = dataSource.getProviderType();

		if (Objects.equals(providerType, "CSV")) {
			dataSource.setState("READY");
		}
		else if (Objects.equals(providerType, "LIFERAY")) {
			_addDefaultChannel(dataSource);

			dataSource.setStatus("ACTIVE");

			_updateTokenDataSourceCredentials(dataSource);
		}

		return _dataSourceRepository.save(dataSource);
	}

	public void deleteDataSource(DataSource dataSource) throws Exception {
		_clearDataSource(dataSource);

		Long dataSourceId = dataSource.getId();

		if (dataSourceId == null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Unable to delete a data source without ID");
		}

		_deleteFieldMappings(dataSourceId);

		_dataSourceRepository.deleteById(dataSourceId);
	}

	public void deleteDataSources() throws Exception {
		for (DataSource dataSource : getDataSources()) {
			deleteDataSource(dataSource);
		}
	}

	public DataSource disconnectDataSource(Long dataSourceId) {
		DataSource dataSource = getDataSource(dataSourceId);

		if (Objects.equals(dataSource.getState(), "DISCONNECTED") &&
			Objects.equals(dataSource.getStatus(), "INACTIVE")) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Data source already disconnected");
		}

		_clearChannels(dataSourceId);

		dataSource.setContactsSelected(false);
		dataSource.setFaroBackendSecuritySignature(null);
		dataSource.setSitesSelected(false);
		dataSource.setState("DISCONNECTED");
		dataSource.setStatus("INACTIVE");

		return _dataSourceRepository.save(dataSource);
	}

	public boolean existsDataSource(String faroBackendSecuritySignature) {
		return _dataSourceRepository.existsByFaroBackendSecuritySignature(
			faroBackendSecuritySignature);
	}

	public DataSource fetchDataSource(Long dataSourceId) {
		Optional<DataSource> dataSourceOptional =
			_dataSourceRepository.findById(dataSourceId);

		return dataSourceOptional.orElse(null);
	}

	public Long fetchDefaultChannelId(Long dataSourceId) {
		Channel defaultChannel = _channelDog.fetchDefaultChannel(dataSourceId);

		if (defaultChannel == null) {
			return null;
		}

		return defaultChannel.getId();
	}

	public DataSource getDataSource(Long dataSourceId) {
		Optional<DataSource> dataSourceOptional =
			_dataSourceRepository.findById(dataSourceId);

		return dataSourceOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no data source with ID " + dataSourceId));
	}

	public String getDataSourceName(Long dataSourceId) {
		DataSource dataSource = getDataSource(dataSourceId);

		return dataSource.getName();
	}

	public Page<DataSource> getDataSourcePage(
		String filterString, int page, int size, String[] sorts) {

		FilterHelper filterHelper = new FilterHelper(
			_defaultFilterStringConverterHelper, filterString,
			_dataSourceFilterStringConverterHelper);

		PageRequest pageRequest = PageRequest.of(page, size, _getSort(sorts));

		return PageableExecutionUtils.getPage(
			_dataSourceRepository.searchDataSources(filterHelper, pageRequest),
			pageRequest,
			() -> _dataSourceRepository.countDataSources(filterHelper));
	}

	public List<DataSource> getDataSources() {
		return IterableUtils.toList(_dataSourceRepository.findAll());
	}

	public List<DataSource> getDataSources(List<Long> dataSourceIds) {
		return IterableUtils.toList(
			_dataSourceRepository.findAllById(dataSourceIds));
	}

	public List<DataSource> getDataSources(String providerType) {
		return _dataSourceRepository.findByProviderType(providerType);
	}

	public List<DataSource> getDataSources(String providerType, String status) {
		return _dataSourceRepository.findByProviderTypeAndStatus(
			providerType, status);
	}

	public List<DataSource> getDataSources(
		String credentialType, String providerType, Integer size, Sort sort) {

		if ((credentialType != null) && (providerType != null)) {
			return _dataSourceRepository.findByCredentialTypeAndProviderType(
				credentialType, providerType, _getPageable(size, sort));
		}

		if (credentialType != null) {
			return _dataSourceRepository.findByCredentialType(
				credentialType, _getPageable(size, sort));
		}

		if (providerType != null) {
			return _dataSourceRepository.findByProviderType(
				providerType, _getPageable(size, sort));
		}

		Page<DataSource> dataSources = _dataSourceRepository.findAll(
			_getPageable(size, sort));

		return dataSources.getContent();
	}

	public Map<String, JSONObject> getDataSourcesJSONObjects(
			List<Individual> individuals)
		throws Exception {

		Map<String, JSONObject> dataSourcesJSONObjects = new HashMap<>();

		for (Individual individual : individuals) {
			List<Long> dataSourceIds =
				_bqIndividualRepository.searchIndividualDataSourceIds(
					individual.getId());

			dataSourcesJSONObjects.put(
				individual.getId(),
				JSONUtil.put(
					"data-sources",
					JSONUtil.toJSONArray(
						getDataSources(dataSourceIds),
						dataSource -> _objectMapper.convertValue(
							dataSource, JSONObject.class))));
		}

		return dataSourcesJSONObjects;
	}

	public boolean isAnalyticsConfigured() {
		return _dataSourceRepository.existsByProviderType("LIFERAY");
	}

	public DataSource patchDataSource(DataSource dataSource) {
		Long id = dataSource.getId();

		if (id == null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Unable to patch a data source without ID");
		}

		DataSource existingDataSource = getDataSource(id);

		String name = existingDataSource.getName();

		BeanUtils.copyProperties(dataSource, existingDataSource);

		if (!StringUtils.equals(dataSource.getName(), name)) {

			// TODO Update Individual's data source name reference

		}

		return updateDataSourceConfiguration(existingDataSource);
	}

	public DataSource updateDataSource(DataSource dataSource) {
		return _dataSourceRepository.save(dataSource);
	}

	public DataSource updateDataSourceConfiguration(DataSource dataSource) {
		String name = dataSource.getName();

		if ((name != null) &&
			_dataSourceRepository.existsByIdNotAndName(
				dataSource.getId(), name)) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Duplicate data source name " + name);
		}

		String providerType = dataSource.getProviderType();

		if (providerType.equals("LIFERAY")) {
			_updateTokenDataSourceCredentials(dataSource);
		}

		return _dataSourceRepository.save(dataSource);
	}

	public DataSource updateDataSourceDetails(
		Long dataSourceId, Boolean accountsSelected,
		Boolean commerceChannelsSelected, Boolean contactsSelected,
		Boolean sitesSelected) {

		DataSource dataSource = getDataSource(dataSourceId);

		if (accountsSelected != null) {
			dataSource.setAccountsSelected(accountsSelected);
		}

		if (commerceChannelsSelected != null) {
			dataSource.setCommerceChannelsSelected(commerceChannelsSelected);
		}

		if (contactsSelected != null) {
			dataSource.setContactsSelected(contactsSelected);
		}

		if (sitesSelected != null) {
			dataSource.setSitesSelected(sitesSelected);
		}

		return _dataSourceRepository.save(dataSource);
	}

	private void _addDefaultChannel(DataSource dataSource) {
		_channelDog.addChannel(
			Collections.singletonMap(
				dataSource.getId(), Collections.emptySet()),
			true, dataSource.getName(), true);
	}

	private void _clearChannels(Long dataSourceId) {
		for (Channel channel : _channelDog.getChannels(dataSourceId)) {
			Set<ChannelDataSource> channelDataSources = Stream.of(
				channel
			).map(
				Channel::getChannelDataSources
			).flatMap(
				Set::stream
			).filter(
				channelDataSource -> !Objects.equals(
					channelDataSource.getDataSourceId(), dataSourceId)
			).collect(
				Collectors.toSet()
			);

			channel.setChannelDataSources(channelDataSources);

			_channelDog.update(channel);
		}
	}

	private void _clearDataSource(DataSource dataSource) throws Exception {
		_deleteData(dataSource);

		_deleteRunLogs(dataSource);

		// TODO Remove Individual if it is a single data source case

	}

	private void _deleteData(DataSource dataSource) throws Exception {
		Long dataSourceId = dataSource.getId();

		String providerType = dataSource.getProviderType();

		if (providerType.equals("CSV")) {
			_bqCSVUserDog.deleteBQCSVUsers(dataSourceId);
		}
		else if (providerType.equals("LIFERAY")) {
			_asahMarkerDog.deleteAsahMarker(dataSourceId.toString());

			_deleteData(
				dataSourceId, "groups", "roles", "teams", "user-groups",
				"users");

			// TODO Remove data source fields references from Individual

		}
		else if (_log.isWarnEnabled()) {
			_log.warn(
				"Raw data for data source " + dataSourceId +
					" with unknown provider type " + providerType +
						" will not be deleted");
		}
	}

	private void _deleteData(Long dataSourceId, String... collectionNames) {
		for (String collectionName : collectionNames) {
			if (DXPEntity.Type.ofCollectionName(collectionName) == null) {
				continue;
			}

			_dxpEntityDog.deleteByFieldNameEqualsAndType(
				"dataSourceId", dataSourceId,
				DXPEntity.Type.ofCollectionName(collectionName));

			if (StringUtils.equals(collectionName, "organizations")) {

				// TODO Delete organizations

			}
		}
	}

	private void _deleteFieldMappings(Long dataSourceId) {

		// TODO Disable segments referencing fields from data source

		List<String> disabledFieldMappingFieldNames = new ArrayList<>();

		_segmentDog.disableDynamicSegments(
			dataSourceId, disabledFieldMappingFieldNames);
	}

	private void _deleteRunLogs(DataSource dataSource) {
		_runLogDog.deleteRunLogs(
			dataSource.getId(), WeDeployDataService.OSB_ASAH_FARO_INFO);

		String providerType = dataSource.getProviderType();

		if (providerType.equals("LIFERAY")) {
			_runLogDog.deleteRunLogs(
				dataSource.getId(), WeDeployDataService.OSB_ASAH_DXP_RAW);
		}
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
	}

	private String _getDataSourceName(String name) {
		int nameCount = 0;
		String originalName = name;

		while (_dataSourceRepository.existsByName(name)) {
			name = String.format("%s (%d)", originalName, ++nameCount);
		}

		return name;
	}

	private Pageable _getPageable(Integer size, Sort sort) {
		if (size == null) {
			size = 10;
		}

		if (sort == null) {
			sort = Sort.unsorted();
		}

		return PageRequest.of(0, size, sort);
	}

	private Sort _getSort(String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return Sort.by(Sort.Order.asc("id"));
		}

		List<Sort.Order> orders = new ArrayList<>();

		for (int i = 0; i < (sorts.length - 1); i = i + 2) {
			String sort = sorts[i];

			if (Objects.equals(sorts[i + 1], "asc")) {
				orders.add(Sort.Order.asc(sort));
			}
			else {
				orders.add(Sort.Order.desc(sort));
			}
		}

		return Sort.by(orders);
	}

	private void _updateTokenDataSourceCredentials(DataSource dataSource) {
		if (!Objects.equals(
				dataSource.getCredentialType(), "Token Authentication")) {

			return;
		}

		String dataSourceStatus = dataSource.getStatus();

		if (dataSourceStatus.equals("ACTIVE")) {
			dataSource.setState("CREDENTIALS_VALID");
		}

		try {
			if (StringUtils.isBlank(
					dataSource.getFaroBackendSecuritySignature())) {

				dataSource.setFaroBackendSecuritySignature(
					String.valueOf(UUID.randomUUID()));
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}
	}

	private static final Log _log = LogFactory.getLog(DataSourceDog.class);

	@Autowired
	private AsahMarkerDog _asahMarkerDog;

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(10, 1);

	@Autowired
	private BQCSVUserDog _bqCSVUserDog;

	@Autowired
	private BQIndividualRepository _bqIndividualRepository;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private DataSourceFilterStringConverterHelper
		_dataSourceFilterStringConverterHelper;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	private final DefaultFilterStringConverterHelper
		_defaultFilterStringConverterHelper =
			new DefaultFilterStringConverterHelper();

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private RunLogDog _runLogDog;

	@Autowired
	private SegmentDog _segmentDog;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}