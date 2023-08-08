/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.DataSourceFieldMapping;

import java.util.Collection;
import java.util.List;

/**
 * @author Marcos Martins
 */
public interface DataSourceFieldMappingRepository {

	public List<DataSourceFieldMapping> findByFieldMappingFieldNames(
		Collection<Long> fieldMappingFieldNames);

}