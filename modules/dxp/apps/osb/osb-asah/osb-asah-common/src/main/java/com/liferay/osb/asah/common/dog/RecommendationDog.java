/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.ItemRecommendation;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ItemRecommendationRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class RecommendationDog {

	public void deleteItemRecommendationsByJobId(Long jobId) {
		_itemRecommendationRepository.deleteByJobId(jobId);
	}

	public ItemRecommendation getItemRecommendation(String id) {
		Optional<ItemRecommendation> itemRecommendationOptional =
			_itemRecommendationRepository.findById(id);

		return itemRecommendationOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no item recommendation with ID " + id));
	}

	public Page<ItemRecommendation> getItemRecommendationPage(
		Long jobId, int page, int size, Sort sort) {

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_itemRecommendationRepository.findByJobId(jobId, pageRequest),
			pageRequest,
			() -> _itemRecommendationRepository.countByJobId(jobId));
	}

	@Autowired
	private ItemRecommendationRepository _itemRecommendationRepository;

}