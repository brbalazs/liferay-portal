/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQIdentityInterestScoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class InterestCompositionDog {

	public CompositionResultBag getIndividualCompositionResultBag(
		Long channelId, String keywords, int size, Sort sort, int start) {

		return _bqIdentityInterestScoreRepository.
			getInterestCompositionResultBag(
				Boolean.FALSE, channelId, keywords, null,
				PageRequest.of(start / size, size, sort));
	}

	public CompositionResultBag getIndividualSegmentCompositionResultBag(
		boolean active, Long channelId, String keywords, Long segmentId,
		int size, Sort sort, int start) {

		return _bqIdentityInterestScoreRepository.
			getInterestCompositionResultBag(
				active, channelId, keywords, segmentId,
				PageRequest.of(start / size, size, sort));
	}

	@Autowired
	private BQIdentityInterestScoreRepository
		_bqIdentityInterestScoreRepository;

}