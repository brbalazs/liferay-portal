/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQIdentity;
import com.liferay.osb.asah.common.model.MetricType;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Collection;
import java.util.List;

import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQIdentityRepository {

	public long countBQIndividuals(boolean includeAnonymousUsers);

	public List<BQIdentity> findAll();

	public List<BQIdentity> findByIdIn(Collection<String> ids);

	public List<String> getBQIdentityIds(String bqIndividualId);

	public String getBQIndividualId(String id);

	public long getBQIndividualsCount(
		@Nullable Boolean active, @Nullable Long channelId, LocalDate localDate,
		MetricType metricType, ZoneId zoneId);

	public List<Long> getBQIndividualsCounts(
		@Nullable Boolean active, @Nullable Long channelId,
		List<LocalDate> localDates, List<MetricType> metricTypes,
		ZoneId zoneId);

	public BQIdentity insert(BQIdentity bqIdentity);

}