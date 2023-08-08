/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQGroup;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQGroupRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class BQGroupDog extends BaseBQDXPEntityDog {

	public Page<BQGroup> getBQGroupPage(
		@Nullable Long channelId, @Nullable String keywords, int size,
		Sort sort, int start) {

		List<Long> dataSourceIds = getDataSourceIds(channelId);
		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		List<BQGroup> bqGroups =
			_bqGroupRepository.searchByDataSourceIdsAndKeywords(
				dataSourceIds, keywords, pageRequest);

		Map<Long, String> dataSourceNames = getDataSourceNames(dataSourceIds);

		for (BQGroup bqGroup : bqGroups) {
			bqGroup.setDataSourceName(
				dataSourceNames.get(bqGroup.getDataSourceId()));
		}

		return PageableExecutionUtils.getPage(
			bqGroups, pageRequest,
			() -> _bqGroupRepository.countByDataSourceIdsAndKeywords(
				dataSourceIds, keywords));
	}

	public List<BQGroup> getBQGroups(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqGroupRepository.findByIdIn(ids);
	}

	@Autowired
	private BQGroupRepository _bqGroupRepository;

}