/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.EventAnalysis;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomEventAnalysisRepository {

	@Cacheable
	public long countEventAnalyses(Long channelId, @Nullable String keywords);

	@Cacheable
	public List<EventAnalysis> searchEventAnalyses(
		Long channelId, @Nullable String keywords, Pageable pageable);

}