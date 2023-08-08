/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.Segment;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Inácio Nery
 */
public interface SegmentRepository
	extends CustomSegmentRepository, Repository<Segment, Long> {

	@Cacheable
	public long countByCreateDateBetweenAndIdAfter(
		Date createDate1, Date createDate2, Long id);

	@Cacheable
	public long countByIdAfter(Long id);

	@CacheEvict(allEntries = true)
	@Modifying
	@Query("DELETE FROM Segment WHERE channelId IN (:channelIds)")
	public void deleteByChannelIdIn(@Param("channelIds") Set<Long> channelIds);

	@Cacheable
	public boolean existsByName(String name);

	@Cacheable
	public Optional<Segment> findByChannelIdAndNameIgnoreCase(
		Long channelId, String name);

	@Cacheable
	public List<Segment> findByChannelIdIn(
		@Param("channelIds") Set<Long> channelIds, Pageable pageable);

	@Cacheable
	public List<Segment> findByChannelIdIsNotNullOrNameStartingWith(
		String name, Pageable pageable);

	@Cacheable
	public List<Segment> findByCreateDateBetweenAndIdAfter(
		Date createDate1, Date createDate2, Long id, Pageable pageable);

	@Cacheable
	public List<Segment> findByIdAfter(Long id, Pageable pageable);

	@Cacheable
	public Optional<Segment> findByNameAndStatus(String name, String status);

	@Cacheable
	public List<Segment> findByReferencedDataSourceIdsAndStateNotAndType(
		@Param("referencedDataSourceId") Long referencedDataSourceId,
		@Param("state") String state, @Param("type") Segment.Type type);

	@Cacheable
	public List<Segment>
		findByReferencedDataSourceIdsOrReferencedFieldMappingFieldNameInAndStateNotAndType(
			@Param("referencedDataSourceId") Long referencedDataSourceId,
			@Param("referencedFieldMappingFieldNames") List<String>
				referencedFieldMappingFieldNames,
			@Param("state") String state, @Param("type") Segment.Type type);

	@Cacheable
	public List<Segment>
		findByReferencedFieldMappingFieldNameInAndStateNotAndType(
			@Param("referencedFieldMappingFieldNames") List<String>
				referencedFieldMappingFieldNames,
			@Param("state") String state, @Param("type") Segment.Type type);

	@Cacheable
	public List<Segment> findByStateNotAndType(String state, Segment.Type type);

	@Cacheable
	public List<Segment> findByType(Pageable pageable, Segment.Type type);

	@Cacheable
	public List<Long> findIdByFilterLike(@Param("filter") String filterString);

	@Cacheable
	public List<Long> findIdByNameInAndStatus(
		@Param("names") List<String> names, @Param("status") String status);

	@Cacheable
	public List<String> findNameByChannelIdAndIdInAndStatus(
		@Param("channelId") Long channelId, @Param("ids") List<Long> ids,
		@Param("status") String status);

	@Cacheable
	public List<String> findNameByIdInAndStatus(
		@Param("ids") List<Long> ids, @Param("status") String status);

}