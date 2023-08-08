/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.DataSourceFieldMapping;
import com.liferay.osb.asah.common.repository.DataSourceFieldMappingRepository;

import java.util.Collection;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import org.springframework.stereotype.Repository;

/**
 * @author Marcos Martins
 */
@Repository
public class DataSourceFieldMappingRepositoryImpl
	implements DataSourceFieldMappingRepository {

	public DataSourceFieldMappingRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public List<DataSourceFieldMapping> findByFieldMappingFieldNames(
		Collection<Long> fieldMappingFieldNames) {

		return _dslContext.select(
			DSL.asterisk()
		).from(
			"DataSourceFieldMapping"
		).where(
			DSL.field(
				"fieldMappingFieldName"
			).in(
				fieldMappingFieldNames
			)
		).fetch(
			record -> new DataSourceFieldMapping(record.intoMap())
		);
	}

	private final DSLContext _dslContext;

}