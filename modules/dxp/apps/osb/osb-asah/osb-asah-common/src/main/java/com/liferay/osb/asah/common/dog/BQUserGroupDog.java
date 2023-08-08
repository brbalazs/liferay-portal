/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQUserGroupRepository;

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
public class BQUserGroupDog extends BaseBQDXPEntityDog {

	public Page<BQUserGroup> getBQUserGroupPage(
		@Nullable Long channelId, @Nullable String keywords, int size,
		Sort sort, int start) {

		List<Long> dataSourceIds = getDataSourceIds(channelId);
		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		List<BQUserGroup> bqUserGroups =
			_bqUserGroupRepository.searchByDataSourceIdsAndKeywords(
				dataSourceIds, keywords, pageRequest);

		Map<Long, String> dataSourceNames = getDataSourceNames(dataSourceIds);

		for (BQUserGroup bqUserGroup : bqUserGroups) {
			bqUserGroup.setDataSourceName(
				dataSourceNames.get(bqUserGroup.getDataSourceId()));
		}

		return PageableExecutionUtils.getPage(
			bqUserGroups, pageRequest,
			() -> _bqUserGroupRepository.countByDataSourceIdsAndKeywords(
				dataSourceIds, keywords));
	}

	public List<BQUserGroup> getBQUserGroups(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqUserGroupRepository.findByIdIn(ids);
	}

	@Autowired
	private BQUserGroupRepository _bqUserGroupRepository;

}