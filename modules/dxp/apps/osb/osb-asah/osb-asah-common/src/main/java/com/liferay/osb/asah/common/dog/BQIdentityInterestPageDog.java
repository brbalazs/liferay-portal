/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.repository.BQIdentityInterestPageRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class BQIdentityInterestPageDog {

	public long countActivePages(
		Long channelId, String filterString, String ownerId, String ownerType) {

		return _bqIdentityInterestPageRepository.
			countActivePagesTransformations(
				channelId, filterString, ownerId, ownerType);
	}

	public long countInactivePages(
		Long channelId, String filterString, String ownerId, String ownerType) {

		return _bqIdentityInterestPageRepository.
			countInactivePagesTransformations(
				channelId, filterString, ownerId, ownerType);
	}

	public List<Map<String, Object>> getActivePagesTransformations(
		Long channelId, String filterString, String ownerId, String ownerType,
		int page, int size, String[] sorts) {

		return _bqIdentityInterestPageRepository.getActivePagesTransformations(
			channelId, filterString, ownerId, ownerType,
			PageRequest.of(page, size, SortUtil.getSort(sorts)));
	}

	public List<Map<String, Object>> getInactivePagesTransformations(
		Long channelId, String filterString, String ownerId, String ownerType,
		int page, int size, String[] sorts) {

		return _bqIdentityInterestPageRepository.
			getInactivePagesTransformations(
				channelId, filterString, ownerId, ownerType,
				PageRequest.of(page, size, SortUtil.getSort(sorts)));
	}

	@Autowired
	private BQIdentityInterestPageRepository _bqIdentityInterestPageRepository;

}