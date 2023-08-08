/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQExpandoValue;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
public interface CustomBQExpandoValueRepository {

	public long count();

	public void deleteById(String id);

	public List<BQExpandoValue> findByClassPKAndClassTypeAndDataSourceId(
		Long classPK, String classType, Long dataSourceId);

	public BQExpandoValue insert(BQExpandoValue bqExpandoValue);

}