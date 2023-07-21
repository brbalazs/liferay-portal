/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.service.persistence.impl;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Shuyang Zhou
 */
public class TableMapperFactory {

	public static <L extends BaseModel<L>, R extends BaseModel<R>>
		TableMapper<L, R> getTableMapper(
			String tableName, String companyColumnName, String leftColumnName,
			String rightColumnName, BasePersistence<L> leftPersistence,
			BasePersistence<R> rightPersistence) {

		TableMapper<?, ?> tableMapper = tableMappers.get(tableName);

		if (tableMapper == null) {
			TableMapperImpl<L, R> tableMapperImpl = null;

			if (_cachelessMappingTableNames.contains(tableName)) {
				tableMapperImpl = new CachelessTableMapperImpl<>(
					tableName, companyColumnName, leftColumnName,
					rightColumnName, leftPersistence, rightPersistence);
			}
			else {
				tableMapperImpl = new TableMapperImpl<>(
					tableName, companyColumnName, leftColumnName,
					rightColumnName, leftPersistence, rightPersistence);
			}

			tableMapperImpl.setReverseTableMapper(
				new ReverseTableMapper<>(tableMapperImpl));

			tableMapper = tableMapperImpl;

			tableMappers.put(tableName, tableMapper);
		}
		else if (!tableMapper.matches(leftColumnName, rightColumnName)) {
			tableMapper = tableMapper.getReverseTableMapper();
		}

		return (TableMapper<L, R>)tableMapper;
	}

	public static void removeTableMapper(String tableName) {
		TableMapper<?, ?> tableMapper = tableMappers.remove(tableName);

		if (tableMapper != null) {
			tableMapper.destroy();
		}
	}

	/**
	 * @deprecated As of Judson (7.1.x)
	 */
	@Deprecated
	protected static final Set<String> cacheMappingTableNames = null;

	protected static final Map<String, TableMapper<?, ?>> tableMappers =
		new ConcurrentHashMap<>();

	private static final Set<String> _cachelessMappingTableNames =
		SetUtil.fromArray(
			PropsUtil.getArray(
				PropsKeys.TABLE_MAPPER_CACHELESS_MAPPING_TABLE_NAMES));

}