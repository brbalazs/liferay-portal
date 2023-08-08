/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Experiment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomExperimentRepository {

	public long countExperimentsByChannelIdAndKeywords(
		Long channelId, @Nullable String keywords);

	public List<Experiment> searchExperimentsByChannelIdAndKeywords(
		Long channelId, @Nullable String keywords, Pageable pageable);

}