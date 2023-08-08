/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQExpandoValueRepository;
import com.liferay.osb.asah.common.repository.BQUserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class BQUserDog extends BaseBQDXPEntityDog {

	public Page<BQUser> getBQUserPage(
		@Nullable Long channelId, @Nullable String keywords, int size,
		Sort sort, int start) {

		List<Long> dataSourceIds = getDataSourceIds(channelId);
		PageRequest pageRequest = PageRequest.of(start / size, size, sort);

		List<BQUser> bqUsers =
			_bqUserRepository.searchByDataSourceIdsAndKeywords(
				dataSourceIds, keywords, pageRequest);

		Map<Long, String> dataSourceNames = getDataSourceNames(dataSourceIds);

		for (BQUser bqUser : bqUsers) {
			bqUser.setDataSourceName(
				dataSourceNames.get(bqUser.getDataSourceId()));
		}

		return PageableExecutionUtils.getPage(
			bqUsers, pageRequest,
			() -> _bqUserRepository.countByDataSourceIdsAndKeywords(
				dataSourceIds, keywords));
	}

	public List<BQUser> getBQUsers(Collection<String> ids) {
		if (ids.isEmpty()) {
			return Collections.emptyList();
		}

		return _bqUserRepository.findByIdIn(ids);
	}

	public List<BQUser> getBQUsers(
		Map<String, Object> fields, Pageable pageable) {

		List<BQUser> bqUsers = _bqUserRepository.findByFields(fields, pageable);

		for (BQUser bqUser : bqUsers) {
			_populateExpandoFields(bqUser);
		}

		return bqUsers;
	}

	private BQUser _populateExpandoFields(BQUser bqUser) {
		bqUser.setExpandoFields(
			getExpandoFields(
				_expandoValueRepository.
					findByClassPKAndClassTypeAndDataSourceId(
						bqUser.getDXPUserId(), DXPEntity.Type.CLASS_NAME_USER,
						bqUser.getDataSourceId())));

		return bqUser;
	}

	@Autowired
	private BQUserRepository _bqUserRepository;

	@Autowired
	private BQExpandoValueRepository _expandoValueRepository;

}