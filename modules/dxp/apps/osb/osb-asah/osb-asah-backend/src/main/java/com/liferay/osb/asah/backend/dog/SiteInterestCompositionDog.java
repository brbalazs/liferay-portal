/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQSessionInterestScoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class SiteInterestCompositionDog {

	public CompositionResultBag getCompositionResultBag(
		Long channelId, int size, int start, TimeRange timeRange) {

		return _bqSessionInterestScoreRepository.
			getInterestCompositionResultBag(
				channelId, PageRequest.of(start / size, size), timeRange);
	}

	@Autowired
	private BQSessionInterestScoreRepository _bqSessionInterestScoreRepository;

}