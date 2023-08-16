/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;

import java.time.ZoneId;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;

/**
 * @author Ivica Cardic
 */
public interface CustomBQMembershipChangeRepository {

	@CacheEvict(allEntries = true)
	@Modifying
	public void addBQMembershipChange(
		MembershipCountSnapshot membershipCountSnapshot);

	@Cacheable
	public long countBQMembershipChanges(
		FilterHelper filterHelper, Long segmentId);

	public long countBySegmentId(Long segmentId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteBySegmentIdIn(List<Long> segmentIds);

	public List<BQMembershipChange> findAll();

	public List<BQMembershipChange> findBySegmentId(long segmentId);

	@Cacheable
	public List<BQMembershipChange> findLastBQMembershipChangeBySegmentIds(
		List<Long> segmentIds);

	@Cacheable
	public List<Long> findSegmentIdByFilterString(String filterString);

	public List<Transformation> getMembershipChangeTransformations(
		boolean includeToday, Long segmentId, Pageable pageable);

	@CacheEvict(allEntries = true)
	@Modifying
	public void initializeBQMembershipChanges(
		Long channelId, Long segmentId, ZoneId zoneId);

	public BQMembershipChange insert(BQMembershipChange bqMembershipChange);

	@Cacheable
	public List<BQMembershipChange> searchBQMembershipChanges(
		FilterHelper filterHelper, Long segmentId, Pageable pageable);

}