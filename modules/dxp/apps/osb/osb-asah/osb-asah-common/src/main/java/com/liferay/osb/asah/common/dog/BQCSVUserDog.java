/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQCSVUserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Michael Bowerman
 * @author Marcellus Tavares
 */
@Component
public class BQCSVUserDog {

	public void addBQCSVUsers(List<BQCSVUser> bqCSVUsers) {
		if (bqCSVUsers.isEmpty()) {
			return;
		}

		_bqCSVUserRepository.insertAll(bqCSVUsers);

		BQCSVUser bqCSVUser = bqCSVUsers.get(0);

		_asahTaskDog.scheduleAsahTask(
			"CSVUsersNanite",
			JSONUtil.put(
				"dataSourceId", String.valueOf(bqCSVUser.getDataSourceId())
			).put(
				"type", "reprocess"
			));
	}

	public void deleteBQCSVUsers(Long dataSourceId) {
		_bqCSVUserRepository.deleteByDataSourceId(dataSourceId);
	}

	public void deleteBQCSVUsers(
		Long dataSourceId, List<String> dataSourceUserPKs) {

		_bqCSVUserRepository.deleteByDataSourceIdAndDataSourceUserPKIn(
			dataSourceId, dataSourceUserPKs);
	}

	public List<BQCSVUser> getBQCSVUsers(
		Long dataSourceId, int page, int size, Sort sort) {

		return _bqCSVUserRepository.findByDataSourceId(
			dataSourceId, PageRequest.of(page, size, sort));
	}

	public long getBQCSVUsersCount(Long dataSourceId) {
		return _bqCSVUserRepository.countByDataSourceId(dataSourceId);
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BQCSVUserRepository _bqCSVUserRepository;

}