/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.DataSourceOrganization;
import com.liferay.osb.asah.common.entity.DataSourceSite;
import com.liferay.osb.asah.common.entity.DataSourceUserGroup;
import com.liferay.osb.asah.common.repository.CustomDataSourceRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;

/**
 * @author Inácio Nery
 */
public class DataSourceRepositoryImpl
	extends BaseRepository implements CustomDataSourceRepository {

	public DataSourceRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countDataSources(FilterHelper filterHelper) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return selectSelectStep.from(
			"DataSource"
		).where(
			filterHelper.getCondition()
		).fetchOptional(
			0, Long.class
		).orElse(
			0L
		);
	}

	@Override
	public List<DataSource> searchDataSources(
		FilterHelper filterHelper, Pageable pageable) {

		Table<Record> dataSourceTable = DSL.table("DataSource");

		SelectSelectStep<Record> selectSelectStep = _dslContext.select(
			dataSourceTable.asterisk());

		return _populateDataSources(
			selectSelectStep.from(
				dataSourceTable
			).leftJoin(
				"ChannelDataSource"
			).on(
				DSL.field(
					"id"
				).eq(
					DSL.field("ChannelDataSource.dataSourceId")
				)
			).where(
				filterHelper.getCondition()
			).groupBy(
				DSL.field("id")
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			).fetch(
				record -> new DataSource(record.intoMap())
			));
	}

	private void _populateDataSourceOrganizations(
		Map<Long, DataSource> dataSourcesById) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		Field<Object> field = DSL.field("dataSourceId");

		selectSelectStep.from(
			"DataSourceOrganization"
		).where(
			field.in(dataSourcesById.keySet())
		).fetch(
		).forEach(
			record -> {
				DataSource dataSource = dataSourcesById.get(
					record.get("datasourceid"));

				dataSource.addDataSourceOrganization(
					new DataSourceOrganization(record.intoMap()));
			}
		);
	}

	private List<DataSource> _populateDataSources(
		List<DataSource> dataSources) {

		if (dataSources.isEmpty()) {
			return Collections.emptyList();
		}

		Stream<DataSource> stream = dataSources.stream();

		Map<Long, DataSource> dataSourcesById = stream.collect(
			Collectors.toMap(
				DataSource::getId, Function.identity(), (id, dataSource) -> id,
				LinkedHashMap::new));

		_populateDataSourceOrganizations(dataSourcesById);
		_populateDataSourceSite(dataSourcesById);
		_populateDataSourceUserGroup(dataSourcesById);

		return new ArrayList<>(dataSourcesById.values());
	}

	private void _populateDataSourceSite(
		Map<Long, DataSource> dataSourcesById) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		Field<Object> field = DSL.field("dataSourceId");

		selectSelectStep.from(
			"DataSourceSite"
		).where(
			field.in(dataSourcesById.keySet())
		).fetch(
		).forEach(
			record -> {
				DataSource dataSource = dataSourcesById.get(
					record.get("datasourceid"));

				dataSource.addDataSourceSite(
					new DataSourceSite(record.intoMap()));
			}
		);
	}

	private void _populateDataSourceUserGroup(
		Map<Long, DataSource> dataSourcesById) {

		SelectSelectStep<Record> selectSelectStep = _dslContext.select();

		Field<Object> field = DSL.field("dataSourceId");

		selectSelectStep.from(
			"DataSourceOrganization"
		).where(
			field.in(dataSourcesById.keySet())
		).fetch(
		).forEach(
			record -> {
				DataSource dataSource = dataSourcesById.get(
					record.get("datasourceid"));

				dataSource.addDataSourceUserGroup(
					new DataSourceUserGroup(record.intoMap()));
			}
		);
	}

	private final DSLContext _dslContext;

}