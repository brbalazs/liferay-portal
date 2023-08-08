/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQSessionInterestScore;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.TimeRange;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Robson Pastor
 */
public interface CustomBQSessionInterestScoreRepository {

	public void deleteByRecordedDate(Date recordedDate);

	public CompositionResultBag getInterestCompositionResultBag(
		@Nullable Long channelId, Pageable pageable, TimeRange timeRange);

	public void insertAll(List<BQSessionInterestScore> bqSessionInterestScores);

}