/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DXPEntityRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class DXPEntityDog {

	public DXPEntity addDXPEntity(DXPEntity dxpEntity, DXPEntity.Type type) {
		dxpEntity.setId(null);
		dxpEntity.setModifiedDate(new Date());
		dxpEntity.setType(type);

		return _mapDXPEntity(_dxpEntityRepository.save(dxpEntity), type);
	}

	public void delete(DXPEntity dxpEntity) {
		_dxpEntityRepository.delete(dxpEntity);
	}

	public void delete(List<DXPEntity> dxpEntities) {
		_dxpEntityRepository.deleteAll(dxpEntities);
	}

	public void deleteByFieldNameEqualsAndType(
		String fieldName, Object fieldValue, DXPEntity.Type type) {

		_dxpEntityRepository.deleteByFieldNameAndFieldValueAndType(
			fieldName, fieldValue, type);
	}

	public void deleteByType(DXPEntity.Type type) {
		_dxpEntityRepository.deleteByType(type);
	}

	public List<DXPEntity> fetchAllByFieldsAndType(
		Map<String, Object> fields, DXPEntity.Type type) {

		List<DXPEntity> dxpEntities = _dxpEntityRepository.findByFieldsAndType(
			fields, type);

		Stream<DXPEntity> stream = dxpEntities.stream();

		return stream.map(
			dxpEntity -> _mapDXPEntity(dxpEntity, type)
		).collect(
			Collectors.toList()
		);
	}

	public DXPEntity fetchByFieldsAndType(
		Map<String, Object> fields, DXPEntity.Type type) {

		List<DXPEntity> dxpEntities = _dxpEntityRepository.findByFieldsAndType(
			fields, type);

		if ((dxpEntities == null) || dxpEntities.isEmpty()) {
			return null;
		}

		return _mapDXPEntity(dxpEntities.get(0), type);
	}

	public List<DXPEntity> findByAfterAndFieldsAndType(
		Long after, Map<String, Object> fields, int size, DXPEntity.Type type) {

		return _mapDXPEntities(
			_dxpEntityRepository.findByAfterAndFieldsAndType(
				after, fields, size, type),
			type);
	}

	public List<DXPEntity> findByFieldsAndType(
		Map<String, Object> fields, DXPEntity.Type type) {

		return _mapDXPEntities(
			_dxpEntityRepository.findByFieldsAndType(fields, type), type);
	}

	public Page<DXPEntity> getDXPEntityPage(
		Long dataSourceId, @Nullable Date fromModifiedDate, Date toModifiedDate,
		DXPEntity.Type type, Pageable pageable) {

		return PageableExecutionUtils.getPage(
			_mapDXPEntities(
				_dxpEntityRepository.findByModifiedDateBetweenAndType(
					dataSourceId, fromModifiedDate, toModifiedDate, type,
					pageable),
				type),
			pageable,
			() -> _dxpEntityRepository.countByModifiedDateBetweenAndType(
				dataSourceId, fromModifiedDate, toModifiedDate, type));
	}

	public Page<DXPEntity> getDXPEntityPage(
		@Nullable Long channelId, @Nullable String keywords, int size,
		Sort sort, int start, DXPEntity.Type type) {

		List<Long> dataSourceIds = _getDataSourceIds(channelId);

		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		return PageableExecutionUtils.getPage(
			_mapDXPEntities(
				_dxpEntityRepository.searchByDataSourceIdsAndKeywordsAndType(
					dataSourceIds, keywords, type, pageRequest),
				type),
			pageRequest,
			() -> _dxpEntityRepository.countByDataSourceIdsAndKeywordsAndType(
				dataSourceIds, keywords, type));
	}

	public DXPEntity updateDXPEntity(DXPEntity dxpEntity) {
		dxpEntity.setModifiedDate(new Date());

		return _mapDXPEntity(
			_dxpEntityRepository.save(dxpEntity), dxpEntity.getType());
	}

	private List<Long> _getDataSourceIds(Long channelId) {
		List<Long> dataSourceIds = new ArrayList<>();

		if (channelId != null) {
			Optional<Channel> channelOptional = _channelRepository.findById(
				channelId);

			Channel channel = channelOptional.orElse(null);

			if (channel != null) {
				dataSourceIds = ListUtil.map(
					channel.getChannelDataSources(),
					ChannelDataSource::getDataSourceId);
			}
		}

		return dataSourceIds;
	}

	private List<DXPEntity> _mapDXPEntities(
		List<DXPEntity> dxpEntities, DXPEntity.Type type) {

		dxpEntities.forEach(dxpEntity -> dxpEntity.setType(type));

		return _processDXPEntities(dxpEntities, this::_mapDXPEntity);
	}

	private DXPEntity _mapDXPEntity(DXPEntity dxpEntity, DXPEntity.Type type) {
		dxpEntity.setType(type);

		return _mapDXPEntity(new HashMap<>(), dxpEntity);
	}

	private DXPEntity _mapDXPEntity(
		Map<Long, String> dataSourceNames, DXPEntity dxpEntity) {

		dxpEntity.setDataSourceName(
			dataSourceNames.computeIfAbsent(
				dxpEntity.getDataSourceId(),
				dataSourceId -> {
					if (dataSourceId != null) {
						Optional<DataSource> dataSourceOptional =
							_dataSourceRepository.findById(dataSourceId);

						DataSource dataSource = dataSourceOptional.orElse(null);

						if (dataSource != null) {
							return dataSource.getName();
						}
					}

					return null;
				}));

		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		if (dxpEntity.getType() == DXPEntity.Type.USER) {
			dxpEntity.setName(
				fieldsJSONObject.optString("firstName") + " " +
					fieldsJSONObject.optString("lastName"));
		}
		else {
			dxpEntity.setName(fieldsJSONObject.optString("name"));
		}

		return dxpEntity;
	}

	private List<DXPEntity> _processDXPEntities(
		List<DXPEntity> dxpEntities,
		BiFunction<Map<Long, String>, DXPEntity, DXPEntity>
			dxpEntityModelMapperFunction) {

		Map<Long, String> dataSourceNames = new HashMap<>();

		Stream<DXPEntity> stream = dxpEntities.stream();

		return stream.map(
			dxpEntity -> dxpEntityModelMapperFunction.apply(
				dataSourceNames, dxpEntity)
		).collect(
			Collectors.toList()
		);
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private DXPEntityRepository _dxpEntityRepository;

}