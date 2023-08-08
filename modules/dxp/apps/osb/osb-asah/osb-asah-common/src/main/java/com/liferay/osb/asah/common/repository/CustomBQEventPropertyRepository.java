/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQEventProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQEventPropertyRepository {

	public long countValues(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, String keywords);

	public Map<String, Date>
		findBQEventPropertyValuesByEventAttributeDefinitionName(
			String eventAttributeDefinitionName, int size);

	public BQEventProperty insert(BQEventProperty bqEventProperty);

	public List<String> searchValues(
		Long channelId, String eventAttributeDefinitionName,
		String eventDefinitionName, String keywords, Pageable pageable);

}