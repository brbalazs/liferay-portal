/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQRole;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQRoleRepository;

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
public class BQRoleDog extends BaseBQDXPEntityDog {

	public Page<BQRole> getBQRolePage(
		@Nullable Long channelId, @Nullable String keywords, int size,
		Sort sort, int start) {

		List<Long> dataSourceIds = getDataSourceIds(channelId);
		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		List<BQRole> bqRoles =
			_bqRoleRepository.searchByDataSourceIdsAndKeywords(
				dataSourceIds, keywords, pageRequest);

		Map<Long, String> dataSourceNames = getDataSourceNames(dataSourceIds);

		for (BQRole bqRole : bqRoles) {
			bqRole.setDataSourceName(
				dataSourceNames.get(bqRole.getDataSourceId()));
		}

		return PageableExecutionUtils.getPage(
			bqRoles, pageRequest,
			() -> _bqRoleRepository.countByDataSourceIdsAndKeywords(
				dataSourceIds, keywords));
	}

	public List<BQRole> getBQRoles(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqRoleRepository.findByIdIn(ids);
	}

	@Autowired
	private BQRoleRepository _bqRoleRepository;

}