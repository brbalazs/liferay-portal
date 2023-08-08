/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;

import java.time.ZoneId;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQMembershipRepository {

	@Cacheable
	public long countActiveMembersBySegmentId(
		@Nullable Boolean includeAnonymousUsers, Long segmentId, ZoneId zoneId);

	@Cacheable
	public long countByIdentityIdAndSegmentId(
		String identityId, Long segmentId);

	@Cacheable
	public long countByIdentityIdInAndSegmentIdAndStatus(
		List<String> identityIds, Long segmentId, String status);

	@Cacheable
	public long countBySegmentId(Long segmentId);

	@Cacheable
	public long countBySegmentIdAndStatus(Long segmentId, String status);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteByIndividualIdAndSegmentId(
		String individualId, Long segmentId);

	@CacheEvict(allEntries = true)
	@Modifying
	public void deleteBySegmentIdIn(List<Long> segmentIds);

	@Cacheable
	public boolean existsByIdentityIdAndSegmentIdAndStatus(
		String identityId, Long segmentId, String status);

	public List<BQMembership> findAll();

	@Cacheable
	public List<BQMembership> findByIdentityIdAndStatus(
		String identityId, String status);

	@Cacheable
	public List<BQMembership> findByIdentityIdInAndSegmentIdAndStatus(
		List<String> identityIds, Long segmentId, String status,
		Pageable pageable);

	@Cacheable
	public List<BQMembership> findByIndividualIdAndSegmentIdInAndStatus(
		String individualId, List<Long> segmentIds, String status);

	@Cacheable
	public List<BQMembership> findBySegmentIdAndStatus(
		Long segmentId, String status, Pageable pageable);

	@Cacheable
	public List<String> findIdentityIdBySegmentIdAndStatus(
		Long segmentId, String status);

	@Cacheable
	public List<String> findIdentityIdBySegmentIdIn(
		List<Long> segmentIds, int max, int min, boolean ascending);

	@Cacheable
	public List<Long> findSegmentIdByIdentityIdAndStatus(
		String identityId, String status);

	@Cacheable
	public List<Long> findSegmentIdByIdentityIdInAndStatus(
		List<String> identityIds, String status);

	@Cacheable
	public List<Long> findSegmentIdByIndividualId(String individualId);

	@Cacheable
	public List<Map<String, Long>>
		findSegmentIdIdentitiesCountByIndividualIdAndStatus(
			String individualId, String status);

	@Cacheable
	public List<Long> findTop20SegmentIdByIndividualId(String individualId);

	@Cacheable
	public MembershipCountSnapshot getMembershipCountSnapshot(
		Long channelId, Long segmentId);

	@CacheEvict(allEntries = true)
	@Modifying
	public BQMembership insert(BQMembership bqMembership);

	@CacheEvict(allEntries = true)
	@Modifying
	public void insertAll(List<BQMembership> bqMemberships);

	@CacheEvict(allEntries = true)
	@Modifying
	public void updateBQMemberships(
		Long channelId, String filterString, Boolean includeAnonymousUsers,
		Long segmentId);

}